# Arquitetura do CFPI

Este documento descreve a arquitetura de código do CFPI (Consultor de
Finanças Pessoais Inteligente): como o sistema é dividido em camadas, os
princípios de design e as técnicas de engenharia adotadas, e — com atenção
especial — como a camada de apresentação foi construída em Swing: como cada
tela é montada, quais padrões de layout, pintura customizada, diálogos e
eventos são usados, e como tudo se conecta na inicialização da aplicação.

---

## 1. Visão geral das camadas

O código de produção em `src/com/cfpi` está dividido em três camadas, com
uma direção de dependência única (de fora para dentro):

```mermaid
graph TD
    apresentacao["apresentacao<br/>(Views, ViewModels, Controllers, Swing)"]
    aplicacao["aplicacao.servicos<br/>(implementações de regras/cálculos)"]
    dominio["dominio<br/>(entidades, exceções, ArrayDinamico)"]

    apresentacao -->|usa entidades de| dominio
    apresentacao -->|define interfaces<br/>(apresentacao.comum)| aplicacao
    aplicacao -->|implementa interfaces de| apresentacao
    aplicacao -->|usa entidades de| dominio
```

- **`dominio`** — o núcleo do sistema: entidades (`Usuario`, `Conta`,
  `Transacao`/`Debito`/`Credito`, `Investimento` e seus 12 subtipos,
  `Objetivo`, `Banco`), exceções (`ValidacaoException`,
  `RegraNegocioException`) e a coleção própria `ArrayDinamico`. Não depende
  de nenhuma outra camada.
- **`aplicacao.servicos`** — implementações concretas de cálculos/regras que
  a apresentação precisa, mas que não pertencem a uma entidade específica
  (ex.: avaliação de ativos, cálculo de prazo de objetivo). Implementam
  interfaces *definidas pela camada de apresentação* (`apresentacao.comum`),
  numa inversão de dependência clássica: quem consome a abstração é quem a
  declara.
- **`apresentacao`** — toda a interface gráfica (Swing): telas, modelos de
  apresentação, orquestração de casos de uso e o "shell" da aplicação
  (janela principal, navegação, sessão).

Build e testes são feitos sem Maven/Gradle: `javac` puro para o código de
produção e `JUnit Platform Console Standalone` para os testes (ver
`README.md`). Isso significa que **não há injeção de dependências via
framework** — toda a "fiação" do sistema é feita manualmente em
`CFPI.main()` (a "composition root", ver seção 5).

---

## 2. Camada de domínio (`dominio`)

### 2.1 Entidades

Todas as entidades implementam `Identificavel` (`int getId()`), o que
permite que sejam armazenadas em `ArrayDinamico<T extends Identificavel>`.

| Entidade | Pacote | Observações |
|---|---|---|
| `Usuario` | `entidades.usuario` | possui `ArrayDinamico<Objetivo>` e `ArrayDinamico<Conta>` |
| `Conta` | `entidades.conta` | possui `ArrayDinamico<Transacao>` e `ArrayDinamico<Investimento>`; referencia `Banco` e `Usuario` |
| `Transacao` (abstrata) | `entidades.transacao` | subtipos `Debito` e `Credito` |
| `Investimento` (abstrata) | `entidades.investimento` | 12 subtipos: `Acao`, `CDB`, `CRA`, `CRI`, `Cripto`, `DEB`, `FII`, `LCA`, `LCI`, `PGBL`, `TesouroDireto`, `VGBL` |
| `Objetivo` | `entidades.objetivo` | meta financeira do usuário |
| `Banco` | `entidades.banco` | catálogo de bancos, via `BancoStore`/`BancoStoreImpl` |

### 2.2 `ArrayDinamico<T>` e `CRUD<T>`

`ArrayDinamico` (`dominio.arraydinamico`) é uma lista própria, implementando
a interface `CRUD<T>` (`inserir`, `atualizar`, etc.) sobre um array Java
(`Object[]`) que expande automaticamente (`capacidade = capacidade * 1.5 +
1`). É o "armazenamento" usado por todas as entidades-container (`Usuario`,
`Conta`) para suas coleções filhas.

### 2.3 Exceções de domínio

Duas exceções *unchecked* (`RuntimeException`) com responsabilidades bem
delimitadas:

- **`ValidacaoException`** — valor de campo inválido isoladamente (formato
  de CPF, data, nome, valores negativos, referências obrigatórias nulas).
- **`RegraNegocioException`** — violação que depende do *estado* de outras
  entidades (duplicidade de banco/objetivo, saldo insuficiente, venda maior
  que posição disponível).

Essa distinção é o que permite à camada de apresentação decidir, sem
precisar inspecionar mensagens, se um erro é "este campo está errado" vs.
"esta operação não é permitida dado o estado atual" — embora, na prática
atual, ambas sejam tratadas de forma uniforme pelos Controllers (capturadas
e convertidas em `List<String>` de mensagens).

### 2.4 Estado de implementação (stubs documentados)

Boa parte dos construtores/métodos de `dominio` ainda estão documentados via
Javadoc ("Validações previstas (a implementar)", "Regra de negócio prevista
(a implementar)") mas **com corpo neutro/stub** — ainda não lançam as
exceções nem aplicam os efeitos descritos. Essa é uma decisão deliberada de
TDD: os testes correspondentes já existem e falham de forma esperada
("(*) Vermelho esperado"), documentando o contrato antes da implementação. A
implementação desses stubs é uma frente de trabalho **separada** da camada de
apresentação — por isso a apresentação não pode depender desse
comportamento e precisa de sua própria camada de pré-validação (ver
`ValidadoresFormulario`, seção 4.2).

---

## 3. Camada de aplicação (`aplicacao.servicos`)

Contém implementações de produção de duas interfaces declaradas em
`apresentacao.comum`:

- **`AvaliadorDeAtivosServico implements AvaliadorDeAtivos`** — calcula o
  valor de mercado atual de uma posição em um ativo pelo **custo médio de
  compra** (`valorAtual = custoMedioCompra * quantidadeAtual`), percorrendo
  todas as contas do usuário atual em busca de operações `"compra"` do mesmo
  ativo (mesmo `nomeAtivo`, case-insensitive, mesmo subtipo concreto de
  `Investimento`).
- **`CalculadoraPrazoObjetivoServico implements CalculadoraPrazoObjetivo`** —
  estima dias restantes para atingir um `Objetivo` a partir de um fluxo de
  caixa projetado: `faltante = objetivo.valor - patrimonioAtual`,
  `fluxoMensal = rendaProjetadaMensal - gastosMediosMensais`,
  `diasRestantes = ceil(faltante / fluxoMensal * 30)`. Se o fluxo mensal não
  é positivo, retorna o sentinela `PRAZO_INDETERMINADO` (36500).

Por que essas duas classes existem fora de `dominio` e fora de
`apresentacao.comum`? Porque são **cálculos derivados**, que dependem de
percorrer múltiplas entidades (todas as contas/transações de um usuário) e
não pertencem naturalmente a nenhuma entidade individual — e porque a
interface que descrevem é um *port* definido pelo consumidor (a UI), não
pelo domínio.

---

## 4. Camada de apresentação (`apresentacao`) — visão geral

`src/com/cfpi/apresentacao` está organizado em um pacote por
responsabilidade transversal (`shell`, `designsystem`, `comum`) e um pacote
por funcionalidade/tela (`cadastro`, `dashboard`, `contas`, `transacoes`,
`objetivos`, `investimentos`, `investimentodetalhes`):

```
apresentacao/
├── shell/          → janela principal, navegação, sessão
├── designsystem/   → tokens visuais + componentes Swing reutilizáveis
├── comum/          → "ports" (interfaces) + validadores/formatadores puros
├── cadastro/
├── dashboard/
├── contas/
├── transacoes/
├── objetivos/
├── investimentos/
└── investimentodetalhes/
```

### 4.1 `apresentacao.comum` — ports e utilitários puros

Quatro arquivos, sem nenhuma dependência de Swing:

- **`AvaliadorDeAtivos`** / **`CalculadoraPrazoObjetivo`** — interfaces
  (*ports*) implementadas por `aplicacao.servicos` (seção 3). Definem o que
  a UI precisa, sem saber como é calculado.
- **`Formatadores`** — formatação pt-BR pura: `formatarMoeda` (ex. `"R$
  1.234,56"`, com `DecimalFormat` configurado com vírgula/ponto pt-BR),
  `formatarData` (`yyyy-MM-dd` → `dd/MM/yyyy`, com fallback para o texto
  original se não for uma data ISO válida) e `formatarPercentual`
  (`0.953` → `"95%"`).
- **`ValidadoresFormulario`** — pré-validações client-side que **espelham**
  as regras de negócio ainda não implementadas no domínio (nome, CPF,
  telefone, datas ISO/passadas, número de conta, valores positivos/não
  negativos, "operação" compra/venda). É a peça central que permite à UI
  barrar entradas inválidas *antes* de chamar construtores/setters do
  domínio, mesmo enquanto os stubs de `dominio` não validam nada.

Cada feature tem, tipicamente, um **`*Controller`** (orquestração + chamadas
de validação + delegação ao domínio) que usa `ValidadoresFormulario`
diretamente, retornando `List<String>` de mensagens de erro — vazia em caso
de sucesso.

### 4.2 Padrão por feature: Controller / ViewModel / View / FormDialog / ListItemPanel

Cada uma das seis telas funcionais (`cadastro`, `dashboard`, `contas`,
`transacoes`, `objetivos`, `investimentos`, `investimentodetalhes`) segue,
com variações, a mesma divisão de responsabilidades — um padrão MVC/MVVM
adaptado a Swing:

| Peça | Responsabilidade | Depende de Swing? |
|---|---|---|
| `*ViewModel` | Transformações puras: filtros, ordenação, agrupamento, cálculos de exibição | Não |
| `*Controller` | Orquestra casos de uso: carrega dados do domínio, valida (via `ValidadoresFormulario`), chama construtores/setters do domínio, captura exceções e as converte em `List<String>` | Não |
| `*View` (`JPanel`) | Monta a tela: layout, componentes do design system, conecta listeners aos métodos do `Controller`/`ViewModel` | Sim |
| `*FormDialog` (`JDialog`) | Formulário modal de criação/edição | Sim |
| `*ListItemPanel` (`RoundedPanel`) | Renderização de uma linha de lista | Sim |

Essa separação mantém **toda regra/transformação testável sem inicializar
Swing** (os `*ViewModel` e `*Controller` são testados diretamente por JUnit),
enquanto as classes Swing (`*View`, `*FormDialog`, `*ListItemPanel`) lidam
apenas com construção visual e *wiring* de eventos.

---

## 5. Shell: navegação, sessão e composição (`apresentacao.shell` + `CFPI.java`)

### 5.1 `Tela` — enum de telas navegáveis

```java
public enum Tela {
    CADASTRO_USUARIO, DASHBOARD, CONTAS, TRANSACOES,
    OBJETIVOS, INVESTIMENTOS, INVESTIMENTO_DETALHES
}
```

Identifica cada card do `CardLayout` (ver 5.2) e cada botão da `Sidebar`
(`INVESTIMENTO_DETALHES` e `CADASTRO_USUARIO` não têm botão próprio — a
primeira é alcançada a partir de Investimentos, a segunda é a tela inicial
antes do login).

### 5.2 `MainFrame` — janela principal e `CardLayout`

`MainFrame extends JFrame` é o contêiner de tudo:

- `getContentPane()` usa `BorderLayout`: `Sidebar` a `WEST`, área de telas
  (`telas`, um `JPanel` com `CardLayout`) ao `CENTER`.
- No construtor, **todas** as `Tela` recebem um painel *placeholder*
  (`criarPainelPlaceholder`, um `JLabel` "TELA (em construção)" centrado),
  adicionado ao `CardLayout` com `telas.add(painel, tela.name())` — a chave
  do card é o nome do enum.
- `paineis` é um `Map<Tela, JPanel>` (`EnumMap`) que guarda o painel
  atualmente registrado para cada tela.
- **`registrarPainel(Tela, JPanel)`** — substitui o placeholder (ou a view
  anterior) pela view real: remove o painel antigo de `telas`, adiciona o
  novo com a mesma chave (`tela.name()`). É assim que os Controllers
  "plugam" suas Views reais no shell, tanto na inicialização quanto quando
  uma tela autenticada é registrada após o cadastro.
- **`mostrarTela(Tela)`** — três efeitos:
  1. `cardLayout.show(telas, tela.name())` troca o card visível;
  2. `sidebar.setVisible(tela != CADASTRO_USUARIO)` esconde a sidebar na tela
     de cadastro;
  3. `sidebar.selecionar(tela)` atualiza o destaque visual do botão ativo;
  4. se o painel da tela implementa **`TelaAtualizavel`**, chama
     `atualizar()` — é assim que, por exemplo, o Dashboard recalcula seus
     dados toda vez que o usuário navega para ele.
- No construtor, a tela inicial é decidida por
  `appSession.getUsuarioAtual() == null ? CADASTRO_USUARIO : DASHBOARD`.

### 5.3 `TelaAtualizavel` — refresh ao navegar

```java
public interface TelaAtualizavel {
    void atualizar();
}
```

Interface mínima implementada por `DashboardView`, `ContasView`,
`TransacoesView` e `InvestimentosView` (todas as telas cuja lista/dados
dependem de estado que pode ter mudado desde a última visita).
`InvestimentoDetalhesView` **não** implementa essa interface — ela é
atualizada explicitamente via `exibir(AtivoResumo)`, chamado pelo callback de
navegação a partir de Investimentos (ver 5.5).

### 5.4 `Sidebar` — navegação lateral

`Sidebar extends JPanel`, `BorderLayout`:

- `NORTH`: título "CFPI" (`Fontes.SUBTITULO`, branco).
- `CENTER`: `JPanel` com `BoxLayout(Y_AXIS)` contendo um
  `SidebarButton` por tela navegável (Dashboard, Contas, Transações,
  Objetivos, Investimentos), cada um com `Box.createVerticalStrut` entre eles
  para espaçamento.
- `SOUTH`: rodapé com dois `SidebarButton.rodape(...)` ("Importar" /
  "Exportar") — variante com borda translúcida, usados apenas como elementos
  visuais (sem ação registrada).
- `botoes` é um `Map<Tela, SidebarButton>` (`EnumMap`) usado por
  `selecionar(Tela)` para alternar `setSelecionado(true/false)` em todos os
  botões — destacando apenas o da tela atual.
- Cada botão é ligado via `botao.addActionListener(e ->
  aoNavegar.accept(tela))`, onde `aoNavegar` é o `Consumer<Tela>` recebido no
  construtor — que, na composição (`CFPI.java`), é o `navegador` que decide
  se precisa registrar as telas autenticadas antes de mostrar o Dashboard.

### 5.5 `AppSession` — estado compartilhado da sessão

POJO simples carregando o estado em memória válido durante a execução:

```java
public class AppSession {
    private Usuario usuarioAtual;
    private final CalculadoraPrazoObjetivo calculadoraPrazoObjetivo;
    private final AvaliadorDeAtivos avaliadorDeAtivos;
    // getters/setters
}
```

É injetada em todos os Controllers que precisam do usuário logado ou dos
serviços de cálculo — funcionando como um pequeno *service locator* /
contexto de sessão, em vez de cada tela receber dezenas de dependências
individuais.

### 5.6 `CFPI.main()` — composition root

`CFPI.java` é o único ponto onde `new` é chamado para construir o grafo de
objetos da aplicação. Roda inteiramente dentro de
`SwingUtilities.invokeLater(...)`, como exige Swing (toda manipulação de
componentes deve ocorrer na *Event Dispatch Thread*).

**Passo 1 — bootstrap com referência circular resolvida via array-de-1:**

```java
AppSession[] sessao = new AppSession[1];
AppSession appSession = new AppSession(
        new CalculadoraPrazoObjetivoServico(),
        new AvaliadorDeAtivosServico(() -> sessao[0].getUsuarioAtual()));
sessao[0] = appSession;
```

`AvaliadorDeAtivosServico` precisa de um `Supplier<Usuario>` que aponte para
`appSession.getUsuarioAtual()` — mas `appSession` ainda está sendo
construído nesse ponto (ainda não existe a variável). O array de tamanho 1
(`sessao[0]`) permite criar uma *closure* que referencia uma "caixa" mutável,
preenchida com a instância de `appSession` imediatamente após sua
construção. É um truque clássico para resolver "preciso de uma referência a
um objeto que estou construindo agora".

**Passo 2 — janela principal + tela de cadastro:**

```java
MainFrame mainFrame = new MainFrame(appSession);
Consumer<Tela> navegador = tela -> {
    if (tela == Tela.DASHBOARD && appSession.getUsuarioAtual() != null) {
        registrarTelasAutenticadas(mainFrame, appSession);
    }
    mainFrame.mostrarTela(tela);
};
CadastroUsuarioController cadastroController = new CadastroUsuarioController(appSession, navegador);
mainFrame.registrarPainel(Tela.CADASTRO_USUARIO, new CadastroUsuarioView(cadastroController));
mainFrame.mostrarTela(Tela.CADASTRO_USUARIO);
mainFrame.setVisible(true);
```

O `navegador` é o único `Consumer<Tela>` da aplicação: ele é passado para o
`CadastroUsuarioController`, que o chama com `Tela.DASHBOARD` após um
cadastro bem-sucedido. Nesse momento — e só nesse momento —
`registrarTelasAutenticadas` é chamado, porque é a primeira vez que
`appSession.getUsuarioAtual()` é não-nulo.

**Passo 3 — `registrarTelasAutenticadas`**: instancia, nesta ordem,
Transações, Objetivos, (BancoStore + 4 bancos pré-cadastrados +) Contas,
InvestimentoDetalhes, Investimentos e Dashboard, chamando
`mainFrame.registrarPainel(...)` para cada uma. A ordem importa em dois
pontos:

- `DashboardController` recebe `objetivosController.getOrdemSessao()` — por
  isso `ObjetivosController` precisa existir primeiro.
- `InvestimentosView` recebe um `Consumer<AtivoResumo> aoClicarDetalhes` que
  chama `investimentoDetalhesView.exibir(ativo)` seguido de
  `mainFrame.mostrarTela(Tela.INVESTIMENTO_DETALHES)` — por isso
  `InvestimentoDetalhesView` precisa existir antes de `InvestimentosView`.

Esse método é o "grafo de dependências" da aplicação por extenso: cada
`*Controller` recebe exatamente as colaborações de que precisa (usuário,
`AppSession`, `AvaliadorDeAtivos`, `BancoStore`, `*ViewModel`), sem nenhum
container de DI.

---

## 6. Design system (`apresentacao.designsystem`)

Dezenove classes utilitárias/componentes que formam a "linguagem visual"
compartilhada por todas as telas. Dividem-se em três grupos.

### 6.1 Tokens (cores, tipografia, espaçamento)

- **`Cores`** — paleta de marca (`TAUPE_GREY`, `SMOKY_ROSE`, `FERN`,
  `DESERT_SAND`, `SEASHELL`, `BRANCO`) + variantes com opacidade derivadas
  (ex. `TAUPE_GREY_08`, `TAUPE_GREY_15`, `TAUPE_GREY_40`, `DESERT_SAND_40`) +
  papéis semânticos que **todo o resto do código usa** em vez das cores
  brutas: `FUNDO_PRINCIPAL`, `SIDEBAR_FUNDO`, `CARD_BRANCO`, `CARD_DESTAQUE`,
  `TEXTO_PRIMARIO`, `TEXTO_SECUNDARIO`, `BORDA`, `PRIMARIO`,
  `PRIMARIO_TEXTO`, `CREDITO` (verde), `DEBITO` (vermelho/rosa). Trocar o
  tema visual do app inteiro significa editar apenas esta classe.
- **`Fontes`** — escala tipográfica com duas famílias: **Nunito** para texto
  de interface (`TITULO` 32px, `SUBTITULO` 24px, `MEDIO` 20px, `CORPO` 16px,
  `PEQUENO` 14px, e variantes `_NEGRITO`) e **Inter Bold** para números em
  destaque (`DESTAQUE_NUMERICO`, 24px) — separando visualmente texto
  corrido de valores monetários/quantidades.
- **`Espacamentos`** — escala de espaçamento em base 8px:
  `ESPACO_1`..`ESPACO_6` = 8, 16, 24, 32, 40, 50px, mais `RAIO` (8, raio de
  borda padrão) e `RAIO_PILL` (999, para elementos "pill"/totalmente
  arredondados).

### 6.2 Componentes Swing com pintura customizada

Todos seguem o mesmo padrão: estender um componente Swing (`JPanel`,
`JButton`, `JTextField`, `JComponent`, `JLabel`), desabilitar a pintura
padrão (`setOpaque(false)`/`setContentAreaFilled(false)`/
`setBorderPainted(false)` conforme o caso) e sobrescrever
`paintComponent(Graphics g)` usando `Graphics2D` com
`RenderingHints.KEY_ANTIALIASING = VALUE_ANTIALIAS_ON`, desenhando formas do
pacote `java.awt.geom` (`RoundRectangle2D`, `Ellipse2D`, `Arc2D`, `Path2D`,
`Line2D`).

| Componente | Base Swing | O que desenha |
|---|---|---|
| `RoundedPanel` | `JPanel` | Retângulo de fundo com `RoundRectangle2D` — base de todos os "cards" |
| `RoundedButton` | `JButton` | Retângulo arredondado preenchido com a cor de fundo do botão |
| `RoundedTextField` | `JTextField` | Retângulo arredondado + borda opcional colorida (`corBorda`), usada para estado de erro |
| `SidebarButton` | (componente próprio) | Botão de navegação com destaque arredondado quando selecionado; `rodape(String)` é uma factory estática para a variante de borda translúcida do rodapé |
| `IconButton` | 36×36, quadrado | Ícones vetoriais desenhados à mão (`Path2D`/`Line2D`/`Ellipse2D`): lápis (`editar()`), lixeira (`remover()`), olho (`visualizar()`); estado desabilitado com `AlphaComposite` |
| `Chip` | `JLabel` | "Pílula" (raio = altura/2); factories `neutro()`, `fern()`, `rose()` |
| `CircularProgressDonut` | — | Anel "trilha" (`Ellipse2D`) + arco de progresso (`Arc2D`, começando em 90° e andando no sentido negativo/horário) + percentual centralizado |
| `BarraProgresso` | — | Barra "pill" (`RoundRectangle2D`) com trilho + porção preenchida |
| `IconBadge` | — | Badge circular com anel colorido (`BasicStroke`) + seta para cima/baixo (`Path2D`); factories `credito()`, `debito()` |
| `CampoBusca` | `JPanel` | Ícone de lupa (🔍, `WEST`) + `JTextField` sem borda (`CENTER`), fundo/borda arredondados pintados manualmente |

### 6.3 Helpers de composição/layout

Extraídos para eliminar duplicação entre Views/Dialogs:

- **`LinhaFormulario.criar(String rotulo, JComponent campo)`** — `JPanel`
  com `BorderLayout`: rótulo (`Fontes.CORPO_NEGRITO`) em `NORTH`, campo em
  `CENTER`. Usado em todos os `*FormDialog`.
- **`Renderers.exibindo(Class<T> tipo, Function<T,String> texto, String
  textoNulo)`** — fábrica genérica de `DefaultListCellRenderer` para
  `JComboBox`: faz `instanceof`/cast seguro e usa `texto.apply(valor)`, ou
  `textoNulo` se o valor for `null`. Usado para combos de `Conta` (mostra
  `numeroConta`), `Banco` (mostra `nome`) e `Class<? extends Investimento>`
  (mostra `getSimpleName()`).
- **`CampoFiltro.criar(String rotulo, JComboBox<?> combo)`** — `JPanel` com
  `BoxLayout(Y_AXIS)`: rótulo pequeno em negrito acima do combo. Usado nas
  barras de filtro de Transações e Investimentos.
- **`ListaPanelUtil`** — duas operações para popular painéis de lista
  (`BoxLayout(Y_AXIS)` dentro de `JScrollPane`):
  - `adicionarItens(JPanel painel, List<T> itens, Function<T,? extends
    JComponent> criarItem, int espaco)` — adiciona um componente por item,
    com `Box.createVerticalStrut(espaco)` entre eles;
  - `repopular(...)` — `removeAll()` + `adicionarItens(...)` +
    `revalidate()`/`repaint()`. É o método chamado por toda View ao
    recarregar sua lista (busca, filtro, após criar/editar/remover).
- **`CampoLista.criar(String rotulo, String valor)`** — par rótulo/valor em
  `FlowLayout`, usado dentro dos `*ListItemPanel` para mostrar campos como
  "Categoria: mercado", "Data: 10/06/2026".

### 6.4 Diálogos transversais

- **`ConfirmacaoDialog.confirmar(Component pai, String mensagem)`** —
  `JOptionPane.showConfirmDialog` com `YES_NO_OPTION` +
  `WARNING_MESSAGE`, retornando `boolean`. Usado antes de qualquer remoção
  (`BooleanSupplier confirmacao` nos Controllers).
- **`ErroValidacaoDialog.exibir(Component pai, List<String> erros)`** —
  `JOptionPane.showMessageDialog` com `ERROR_MESSAGE`, concatenando os erros
  com `\n`. Usado por todos os `*FormDialog` quando
  `controller.criar(...)`/`atualizar(...)` retorna uma lista não vazia.

Centralizar essas duas interações em classes dedicadas garante que toda
confirmação de remoção e toda mensagem de erro de validação tenham a mesma
aparência/comportamento em todas as seis telas.

---

## 7. Como o Swing é usado — guia detalhado

### 7.1 Gerenciadores de layout

Cada layout manager do AWT/Swing é usado com um propósito específico e
consistente em todo o código:

| Layout | Onde / para quê |
|---|---|
| `CardLayout` | `MainFrame.telas` — alternância entre as 7 telas principais |
| `BorderLayout` | Esqueleto de quase toda tela (`NORTH`=título/cabeçalho, `CENTER`=conteúdo/`JScrollPane`, `WEST`/`EAST`=ações ou ícones); também usado dentro dos `*ListItemPanel` (ícone à `WEST`, texto ao `CENTER`, ações à `EAST`) |
| `BoxLayout` (`Y_AXIS`) | Empilhamento vertical de seções (`topo`, `listaPanel`, `conteudo`) e de blocos de texto dentro de cards |
| `BoxLayout` (`X_AXIS`) | Agrupamentos horizontais de botões/valores (ex.: valor + botões de editar/remover em `*ListItemPanel`) |
| `FlowLayout` | Linhas de filtros (`RoundedPanel filtros`), grupos de campos dentro de um `*ListItemPanel` (`CampoLista`), ações (`acoes`) |
| `GridLayout` | `cards`/`graficos` do Dashboard (`GridLayout(1, 3, ...)` / `GridLayout(1, 2, ...)`); corpo dos `*FormDialog` (`GridLayout(0, 1, 0, 8)` — uma linha de formulário por linha de grid) |
| `GridBagLayout` | `CadastroUsuarioView` — usado apenas para centralizar o card de cadastro na tela (`add(card, new GridBagConstraints())`, sem mais constraints, resulta em centralização padrão) |

### 7.2 Pintura customizada com `Graphics2D`

Todo componente "de design" (seção 6.2) e os dois gráficos do Dashboard
(`BarChartCategoriaPanel`, `LineChartPatrimonioPanel`) seguem o mesmo
esqueleto:

```java
@Override
protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    // ... desenhar formas (Ellipse2D, RoundRectangle2D, Arc2D, Path2D, Line2D) ...
    g2.dispose();
}
```

Pontos relevantes:

- `g.create()` cria uma cópia do contexto gráfico, para que mudanças de
  cor/stroke/hints não afetem o componente pai — e `g2.dispose()` libera
  essa cópia ao final.
- **`BarChartCategoriaPanel`** (gráfico de barras de "Gastos por categoria"):
  calcula a geometria das barras em `calcularBarras()` (proporcional ao
  maior valor do mapa `Map<String, Double>`), desenha cada barra como
  `RoundRectangle2D` na cor `Cores.DESERT_SAND` e o rótulo da categoria sob
  a barra. Registra um `MouseMotionListener` (`mouseMoved`) que testa
  `barra.area.contains(mouse)` para mostrar uma *tooltip* com
  categoria + valor formatado — a mesma geometria calculada para desenhar é
  reaproveitada para a detecção de hover.
- **`LineChartPatrimonioPanel`** (gráfico de linha de "Crescimento de
  patrimônio"): normaliza a série `List<PontoPatrimonio>` para coordenadas
  de tela em `calcularPontosTela()` (min/max do saldo acumulado → eixo Y;
  índice → eixo X), desenha um `Path2D` ligando os pontos com
  `BasicStroke(2.5f)` na cor `Cores.FERN`, e um `Ellipse2D` em cada ponto.
  *Tooltip* por proximidade: `mouse.distance(pontoTela) <= RAIO_DETECCAO`.
- **`CircularProgressDonut`**: desenha um anel de "trilha" completo
  (`Ellipse2D` com `BasicStroke` grosso) e, sobre ele, um `Arc2D` que começa
  em 90° (topo) e avança no sentido horário por `-360 * percentual` graus —
  com o percentual exibido como texto centralizado.

### 7.3 Diálogos: `JDialog` vs `JOptionPane`

Dois padrões coexistem, para propósitos diferentes:

1. **Formulários de criação/edição → `*FormDialog extends JDialog`** (modal
   `true`). Cada um:
   - usa `BorderLayout`: formulário (`GridLayout(0,1,0,8)`) em `CENTER`,
     botão "Salvar" (`RoundedButton`) em `SOUTH`;
   - monta cada linha com `LinhaFormulario.criar(rotulo, campo)`;
   - chama `pack()` e depois `setSize(new Dimension(Math.max(getWidth(),
     420), getHeight()))` — garante uma largura mínima de 420px sem
     sacrificar a altura calculada por `pack()`;
   - `setLocationRelativeTo(owner)` centraliza sobre a janela principal;
   - expõe getters dos campos/combos e do botão "Salvar" para que a `*View`
     conecte o `ActionListener` (o diálogo **não** chama o `Controller`
     diretamente — quem decide o que fazer com os dados é a View);
   - opcionalmente expõe `preencherParaEdicao(entidade)` para reaproveitar o
     mesmo diálogo na edição (geralmente desabilitando campos que não podem
     mudar, como tipo/conta).

2. **Confirmações e erros → `JOptionPane` via `ConfirmacaoDialog` /
   `ErroValidacaoDialog`** (seção 6.4) — diálogos de uma linha de código,
   sem estado, sem necessidade de uma classe própria.

### 7.4 Eventos: como a UI reage a interações

- **`ActionListener`** — botões (`RoundedButton`, `IconButton`,
  `SidebarButton`) e `JComboBox` (mudança de seleção). Em quase todos os
  casos, o listener chama um método privado da própria View
  (`atualizar()`, `abrirFormularioXxx()`, `remover(...)`).
- **`DocumentListener`** — busca/filtro "ao vivo": `CampoBusca`/campos de
  texto de filtro registram um `DocumentListener` cujos três métodos
  (`insertUpdate`, `removeUpdate`, `changedUpdate`) todos chamam
  `atualizar()`. Usado em `ObjetivosView`, `ContasView`, `InvestimentosView`.
  Em `TransacoesView`/`TransacaoFormDialog`, o filtro é por `JComboBox` (tipo
  e categoria) via `ActionListener`, não por texto.
- **`MouseAdapter`** — dois usos:
  - **Tooltips de gráficos** (`mouseMoved` em `BarChartCategoriaPanel` /
    `LineChartPatrimonioPanel`, seção 7.2);
  - **Drag-and-drop de reordenação** em `ObjetivosView` (seção 8).

---

## 8. Anatomia completa de uma tela: Objetivos

A tela de Objetivos é o exemplo mais rico de composição Swing no projeto —
combina busca ao vivo, lista reordenável por arrastar-e-soltar, e formulário
modal. Vale como roteiro para entender o padrão geral.

### 8.1 Estrutura visual (`ObjetivosView`)

```
ObjetivosView (BorderLayout)
├── NORTH: topo (BoxLayout Y_AXIS)
│   ├── cabecalho (BorderLayout): título "Objetivos" (WEST) + botão "+ Novo objetivo" (EAST, dentro de um FlowLayout)
│   ├── CampoBusca ("Buscar por nome...")
│   └── hint label
└── CENTER: JScrollPane(listaPanel)
    └── listaPanel (BoxLayout Y_AXIS) — preenchido dinamicamente
```

### 8.2 Busca ao vivo

O `JTextField` interno do `CampoBusca` recebe um `DocumentListener` cujas
três implementações (`insertUpdate`/`removeUpdate`/`changedUpdate`) chamam
`atualizarLista()` — ou seja, **cada tecla digitada** re-filtra e
re-renderiza a lista.

### 8.3 `atualizarLista()` e `ListaPanelUtil.repopular`

```java
private void atualizarLista() {
    List<Objetivo> objetivos = controller.filtrarPorNome(campoBusca.getCampo().getText());
    ListaPanelUtil.repopular(listaPanel, objetivos, objetivo -> {
        ObjetivoListItemPanel item = new ObjetivoListItemPanel(objetivo);
        item.getBotaoEditar().addActionListener(e -> abrirFormularioEdicao(objetivo));
        item.getBotaoRemover().addActionListener(e -> remover(objetivo));
        configurarArraste(item);
        return item;
    }, Espacamentos.ESPACO_2);
}
```

`controller.filtrarPorNome(termo)` delega a
`ObjetivosViewModel.filtrarPorNome(ordemSessao, termo)` (substring
case-insensitive sobre o nome; retorna cópia da lista inteira se o termo for
vazio). `ListaPanelUtil.repopular` então `removeAll()` o `listaPanel`,
recria um `ObjetivoListItemPanel` por objetivo (com os listeners de
editar/remover/arrastar já conectados) e chama `revalidate()`/`repaint()`.

### 8.4 `ObjetivoListItemPanel` — linha da lista

`extends RoundedPanel`, `BorderLayout`:

- **`WEST`**: label "⋮⋮" com `Cursor.MOVE_CURSOR` — a "alça" de arraste
  (`getLabelAlca()`).
- **`CENTER`**: nome do objetivo (`Fontes.SUBTITULO`).
- **`EAST`**: `BoxLayout(X_AXIS)` com valor formatado
  (`Formatadores.formatarMoeda`, `Fontes.DESTAQUE_NUMERICO`) + `IconButton`
  de editar + `IconButton` de remover.
- Sobrescreve `getMaximumSize()` para `(MAX_VALUE, preferredHeight)` —
  padrão necessário em todo item de uma lista `BoxLayout(Y_AXIS)`, para que
  o item não estique verticalmente além do necessário.

### 8.5 Drag-and-drop de reordenação

`configurarArraste(item)` registra um único `MouseAdapter` na alça
(`item.getLabelAlca()`) com três callbacks:

1. **`mousePressed`** — guarda o item arrastado (`itemArrastado = item`).
2. **`mouseDragged`** — converte a posição do mouse para coordenadas de
   `listaPanel` via `SwingUtilities.convertPoint(origem, e.getPoint(),
   listaPanel)`, percorre `listaPanel.getComponents()` testando
   `bounds.contains(ponto)` para achar o item sob o cursor
   (`itemSobMouse(e)`), e destaca visualmente esse item com
   `setCorFundo(Cores.DESERT_SAND_40)` (removendo o destaque do item
   anteriormente realçado).
3. **`mouseReleased`** — se houver um item de destino diferente do
   arrastado, chama `controller.moverPorArraste(itemArrastado.getObjetivo(),
   itemAlvo.getObjetivo())` e depois `atualizarLista()` (que recria toda a
   lista na nova ordem); limpa o destaque visual.

A lógica de **onde** cada item está (índices) fica inteiramente no
Controller/ViewModel — a View só sabe "qual painel está sob o mouse" e "qual
objetivo esse painel representa".

### 8.6 `ObjetivosController` — sessão de ordenação

```java
public class ObjetivosController {
    private final List<Objetivo> ordemSessao; // cópia mutável, só em memória

    public List<Objetivo> moverPorArraste(Objetivo arrastado, Objetivo alvo) {
        int origem = ordemSessao.indexOf(arrastado);
        int destino = ordemSessao.indexOf(alvo);
        return mover(origem, destino);
    }

    public List<Objetivo> mover(int origem, int destino) {
        List<Objetivo> novaOrdem = viewModel.mover(ordemSessao, origem, destino);
        ordemSessao.clear();
        ordemSessao.addAll(novaOrdem);
        return carregar();
    }
}
```

`ordemSessao` é inicializada como cópia de `usuario.getObjetivos()` e vive
apenas na sessão de UI — remover/reordenar não altera as coleções do
domínio. `ObjetivosViewModel.mover(lista, origem, destino)` retorna uma
**cópia reordenada** (no-op se os índices forem inválidos), mantendo a
transformação pura e testável sem Swing.

### 8.7 Formulário modal (`ObjetivoFormDialog`)

`JDialog` modal, 420×260, `GridLayout(2,2,8,8)` com dois pares
rótulo/campo (Nome, Valor) via `LinhaFormulario`. `preencherParaEdicao(Objetivo)`
popula os campos para o caso de edição. A View conecta
`getBotaoSalvar()`:

```java
dialog.getBotaoSalvar().addActionListener(e -> {
    List<String> erros = controller.criar(dialog.getCampoNome().getText(), dialog.getCampoValor().getText());
    if (erros.isEmpty()) {
        dialog.dispose();
        atualizarLista();
    } else {
        ErroValidacaoDialog.exibir(dialog, erros);
    }
});
```

Esse mesmo formato — `Controller` retorna `List<String>`; vazio fecha o
diálogo e atualiza a lista, não-vazio mostra `ErroValidacaoDialog` — se
repete identicamente em **todas** as telas com formulário (Contas,
Transações, Investimentos, InvestimentoDetalhes).

---

## 9. As demais telas

### 9.1 Dashboard

`DashboardView implements TelaAtualizavel`. `BorderLayout`: título `NORTH`,
`conteudo` (`BoxLayout Y_AXIS`) `CENTER`, composto por:

- **`cards`** (`GridLayout(1,3,...)`): três `RoundedPanel`:
  - **Saldo total** (`CARD_DESTAQUE`): rótulo + valor (`DESTAQUE_NUMERICO`)
    + subtítulo, em `BoxLayout Y_AXIS`.
  - **Limite de crédito consumido** (`CARD_BRANCO`, `BorderLayout`):
    `CircularProgressDonut` (`WEST`) + textos (`CENTER`).
  - **Objetivo principal** (`CARD_BRANCO`): nome do objetivo
    (`DESTAQUE_NUMERICO`), meta formatada, e dias restantes em
    `Cores.FERN` — ou "Nenhum objetivo cadastrado" / "Prazo indeterminado"
    quando aplicável (`diasRestantes >=
    CalculadoraPrazoObjetivo.PRAZO_INDETERMINADO`).
- **`graficos`** (`GridLayout(1,2,...)`): dois `RoundedPanel` (via
  `criarPainelGrafico(titulo, combo, grafico)`, `BorderLayout`: título+combo
  `NORTH`, gráfico `CENTER`):
  - "Gastos por categoria" → `comboCategoriaBarras` +
    `BarChartCategoriaPanel`;
  - "Crescimento de patrimônio" → `comboCategoriaLinha` +
    `LineChartPatrimonioPanel`.
  As opções dos combos vêm de
  `transacoesViewModel.categoriasComOpcaoTodas(Debito.class /
  Credito.class)` — reaproveitando o `TransacoesViewModel` para listar
  categorias válidas prefixadas com "Todas".
- **"Últimos 7 dias"**: `listaRecentes` (`BoxLayout Y_AXIS` em
  `JScrollPane`), populado com `ListaPanelUtil.repopular(...,
  TransacaoListItemPanel::new, ...)` — **reaproveita** o
  `TransacaoListItemPanel` da feature de Transações.

`atualizar()` chama `controller.carregar(filtroSelecionado(combo))` (uma vez
por gráfico, pois cada combo pode ter filtro diferente) e atualiza todos os
componentes a partir do `DashboardDados` retornado. `DashboardViewModel`
calcula, a partir dos getters de `Conta`/`Transacao`/`Objetivo` (sem chamar
`aplicarEfeito()`): saldo total, percentual de limite consumido, gastos por
categoria (`Map<String,Double>`), série de patrimônio acumulado
(`List<PontoPatrimonio>`), transações dos últimos 7 dias, e o objetivo
principal (primeiro da `ordemSessao` de Objetivos).

### 9.2 Cadastro de usuário

`CadastroUsuarioView` é a única tela com `GridBagLayout` no nível raiz —
usado apenas para centralizar um único `RoundedPanel` ("card") na janela.
Dentro do card (`BoxLayout Y_AXIS`): título, subtítulo, linha "Nome
completo" (largura cheia), linha dupla CPF/Telefone (`BoxLayout X_AXIS`,
cada um com metade da largura), linha "Data de nascimento", label de erro
geral, botão "Criar conta".

Fluxo de submissão:

```java
private void aoClicarCadastrar(CadastroUsuarioController controller) {
    CadastroUsuarioViewModel viewModel = CadastroUsuarioViewModel.criar(
            campoNome.getText(), campoCpf.getText(), campoTelefone.getText(), campoDataNascimento.getText());
    exibirErros(controller.cadastrar(viewModel));
}
```

`exibirErros` usa `CadastroUsuarioViewModel.campoDoErro(mensagem)` — um
`enum Campo { NOME, CPF, TELEFONE, DATA_NASCIMENTO, GERAL }` que classifica
cada mensagem de erro pelo prefixo ("Nome...", "CPF...", etc.) — para decidir
em qual `JLabel`/campo destacar o erro (borda vermelha via
`RoundedTextField.setCorBorda(Cores.DEBITO)` + texto do erro em HTML, para
permitir quebra de linha dentro de uma largura fixa) ou, se `GERAL`, no
`erroGeral` abaixo do formulário. `CadastroUsuarioController.cadastrar`
primeiro chama `viewModel.validar()` (client-side); se passar, tenta `new
Usuario(...)` e, em sucesso, registra o usuário na `AppSession` e dispara
`navegador.accept(Tela.DASHBOARD)`.

### 9.3 Contas

`ContasView implements TelaAtualizavel`. Estrutura igual ao padrão geral
(`BorderLayout`: `topo` com título+busca em `NORTH`, lista em `CENTER`).
Busca por número de conta via `DocumentListener` →
`controller.filtrarPorNumero(termo)` →
`ContasViewModel.filtrarPorNumero(contas, termo)` (substring sobre
`numeroConta`).

`ContaListItemPanel`: chip "Poupança"/"Corrente" (`Chip.fern`/`Chip.rose`),
número da conta, banco/moeda, saldo (`CampoLista`), e — apenas se
`limiteCredito > 0` — uma seção extra com `BarraProgresso` mostrando
`limiteCreditoUtilizado / limiteCredito`. O botão remover é desabilitado
(`setEnabled(false)` + tooltip) se `limiteCreditoUtilizado > 0`.

`ContaFormDialog`: campos Tipo (combo corrente/poupança), Número, Saldo
inicial, Moeda, Limite de crédito, Banco (combo com `Renderers.exibindo`). A
linha "Limite de crédito" é mostrada/escondida via
`ContasViewModel.exibeLimiteCredito(tipo)` (`true` apenas para
`"corrente"`) — a linha permanece no layout (`setVisible(false)`, não
removida) para que o tamanho do diálogo não mude ao trocar o tipo.
`getLimiteCreditoTexto()` retorna `"0"` quando o campo está oculto.

### 9.4 Transações

`TransacoesView implements TelaAtualizavel`. Dois filtros via `JComboBox`
(não busca por texto): "Tipo" (Todos/Débito/Crédito) e "Categoria"
(dependente do tipo — `repopularCategorias()` reconstrói as opções de
categoria sempre que o tipo muda, usando `viewModel.categoriasParaTipo(...)`
mais a opção "Todas"). `atualizarLista()` aplica os dois filtros em sequência
sobre `controller.carregar()` (todas as transações de todas as contas,
ordenadas por data decrescente).

`TransacaoListItemPanel`: `IconBadge.credito()`/`debito()` à `WEST`; ao
centro, campos via `CampoLista` (tipo de débito mapeado para texto amigável —
"avista"→"À vista", "credito"→"Crédito" — ou "Crédito" para `Credito`,
categoria, data formatada, descrição); à direita, valor com sinal
(negativo/vermelho para débito) + editar/remover.

`TransacaoFormDialog`: combo Conta (`Renderers.exibindo`), combo Tipo
(Débito/Crédito), descrição, data, valor, combo Categoria (dependente do
tipo, via `viewModel.categoriasParaTipo`) e combo "Tipo de débito"
(visível apenas para Débito). `repopularTipoDebito()` usa
`viewModel.tiposDebitoParaConta(conta)` — `{"avista"}` para conta poupança,
`{"avista","credito"}` nos demais casos — preservando a seleção atual se
ainda válida.

### 9.5 Investimentos

`InvestimentosView implements TelaAtualizavel`. `CampoBusca` (nome do ativo,
`DocumentListener`) + `JComboBox<Class<? extends Investimento>>` para filtro
de tipo (12 subtipos de `InvestimentoFormDialog.TIPOS`, renderizados via
`Renderers.exibindo(Class.class, Class::getSimpleName, "Todos os tipos")`,
com `null` = "Todos os tipos" como primeira opção).

`controller.carregar()`/`filtrarPorNome(...)` retornam `List<AtivoResumo>` —
`InvestimentosViewModel.agruparPorAtivo(...)` agrupa todas as operações
(`Investimento[]`) por `nomeAtivo` (normalizado) + subtipo concreto, somando
quantidade comprada/vendida (→ `quantidadeAtual`), total investido (apenas
compras) e, via `AvaliadorDeAtivos`, `valorAtual` e `ganhoPerda = valorAtual
- totalInvestido`.

`AtivoListItemPanel`: nome do ativo + `Chip.neutro(tipo.getSimpleName())` +
quantidade; à direita, valor atual (`DESTAQUE_NUMERICO`) e ganho/perda com
sinal e cor (`Cores.CREDITO`/`DEBITO`); botão `IconButton.visualizar()` que
dispara `aoClicarDetalhes.accept(ativo)` — o callback de navegação para
`INVESTIMENTO_DETALHES` definido em `CFPI.registrarTelasAutenticadas`.

`InvestimentoFormDialog`: combo Tipo (12 subtipos, `Renderers.exibindo`),
combo Conta, Ativo, Valor unitário, Quantidade, Data, combo Operação
(compra/venda). `InvestimentosController.criar(...)` valida (incluindo a
regra "saldo insuficiente para compra": `valor * quantidade >
conta.getValorConta()`) e despacha para o construtor correto via um
`if/else` sobre os 12 subtipos (`criarInvestimento`).

### 9.6 Detalhes do investimento

`InvestimentoDetalhesView` (não implementa `TelaAtualizavel` — é atualizada
via `exibir(AtivoResumo)`, chamado pela navegação a partir de
Investimentos). `BorderLayout`: `topo` (`NORTH`) com botão "← Voltar para
Investimentos" (`JButton` "flat" — sem preenchimento/borda, cursor de mão) +
título; `conteudo` (`BoxLayout Y_AXIS` em `JScrollPane`, `CENTER`).

`atualizarConteudo()` faz `conteudo.removeAll()` e reconstrói:

1. **Resumo** (`criarResumo`, `RoundedPanel`): cabeçalho com nome do ativo +
   `Chip.fern(tipo)` + "Primeira compra em DD/MM/AAAA"; linha de estatísticas
   (`FlowLayout`) com Quantidade atual, Total investido, Valor atual,
   Ganho/perda (cor condicional).
2. **Lista de operações**: `ListaPanelUtil.adicionarItens(conteudo,
   operacoes, ...)` — note que aqui é `adicionarItens`, não `repopular`,
   porque o `removeAll()` já foi feito no início de `atualizarConteudo()`
   para *todo* o painel `conteudo` (resumo + lista juntos).

`OperacaoListItemPanel`: `Chip.rose("Venda")`/`Chip.fern("Compra")` à
`WEST`; campos via `CampoLista` (Data, Valor unitário, Quantidade, Total —
e, apenas para vendas, Imposto e Valor realizado); editar/remover à `EAST`.
Editar abre `InvestimentoFormDialog.preencherParaEdicao(...)` com tipo e
conta desabilitados (não podem mudar em uma edição).

---

## 10. Princípios de design aplicados

- **Separação de responsabilidades por camada e por papel** — `dominio` não
  conhece `apresentacao`; dentro de `apresentacao`, `*ViewModel`/`*Controller`
  não conhecem Swing, e `*View`/`*FormDialog`/`*ListItemPanel` não contêm
  regras de negócio nem transformações de dados (apenas leitura de campos e
  delegação).
- **Inversão de dependência via interfaces definidas pelo consumidor** —
  `AvaliadorDeAtivos` e `CalculadoraPrazoObjetivo` são interfaces de
  `apresentacao.comum`, implementadas por `aplicacao.servicos`. A UI declara
  o que precisa; a aplicação fornece.
- **Composition root única** — todo `new` de objetos "de aplicação"
  acontece em `CFPI.main()`; nenhuma classe constrói suas próprias
  dependências internamente (exceto utilitários sem estado como
  `new TransacoesViewModel()`, que são efetivamente *stateless services*).
- **Design tokens centralizados** — `Cores`/`Fontes`/`Espacamentos` são a
  única fonte de valores visuais; nenhum componente usa cores/fontes/medidas
  "mágicas" inline.
- **DRY via designsystem** — qualquer padrão visual ou de composição usado
  em 2+ lugares (linha de formulário, renderer de combo, campo de filtro,
  repopulação de lista, par rótulo/valor) foi extraído para
  `apresentacao.designsystem` em vez de duplicado.
- **Contrato uniforme de erro** — toda operação de escrita em um Controller
  (`criar`, `atualizar`, `cadastrar`) retorna `List<String>` (vazia =
  sucesso); toda View trata esse retorno da mesma forma
  (`ErroValidacaoDialog.exibir` ou fechar diálogo + atualizar lista).
- **Pré-validação client-side espelhando regras de domínio** —
  `ValidadoresFormulario` permite que a UI rejeite entradas inválidas mesmo
  enquanto as validações equivalentes em `dominio` ainda são stubs,
  documentando explicitamente (via Javadoc com referências cruzadas) qual
  regra de qual entidade cada validador espelha.

---

## 11. Técnicas de engenharia

- **TDD com "vermelho esperado"** — testes para regras de negócio ainda não
  implementadas no domínio existem e falham deliberadamente, comentados como
  "(*) Vermelho esperado". Isso documenta o contrato esperado antes da
  implementação e mantém a suíte como especificação executável, sem bloquear
  o desenvolvimento da camada de apresentação (que usa
  `ValidadoresFormulario` como substituto temporário).
- **Stubs com Javadoc, sem lógica** — onde uma regra ainda não está
  implementada, o método existe com assinatura final e Javadoc completo
  (`@param`, `@return`, `@throws`, "Validações previstas (a implementar)")
  mas corpo neutro — permitindo que o restante do sistema compile e seja
  testado contra a interface final.
- **Build sem framework** — `javac` direto para produção,
  `junit-platform-console-standalone` para testes; estrutura de pastas
  `src/com/cfpi` (produção) espelhada em `src/test/com/cfpi` (testes).
- **Testabilidade por construção** — toda lógica não-visual (filtros,
  agrupamentos, formatações, cálculos de prazo/avaliação, validações) vive em
  classes sem dependência de `javax.swing`/`java.awt`, podendo ser testada
  por JUnit sem inicializar a *Event Dispatch Thread*.
