# CFPI — Mockups visuais (HTML/CSS)

Esta pasta contém os mockups estáticos das 7 telas da aplicação CFPI,
construídos em HTML/CSS puro a partir da paleta oficial da marca e do estilo
visual da Homepage do Figma do projeto. O objetivo é servir como referência
visual para a implementação em Java Swing — cada componente CSS tem um
comentário com seu equivalente Swing direto.

> Estes arquivos são apenas design (não fazem parte do código-fonte Java da
> aplicação). Abra qualquer `.html` diretamente no navegador.

## Como navegar

Abra `01-cadastro-usuario.html` para o fluxo inicial, ou `02-homepage.html`
para entrar direto no dashboard. Todas as telas com sidebar têm navegação
funcional entre si pelos links do menu lateral.

## Paleta oficial

| Variável CSS       | Valor     | Uso                                         |
|---------------------|-----------|----------------------------------------------|
| `--seashell`        | `#FEF5EF` | Fundo principal da aplicação                  |
| `--taupe-grey`      | `#584B53` | Sidebar, texto primário, botão/ação principal |
| `--smoky-rose`      | `#9D5C63` | Débito, valores negativos, ações destrutivas  |
| `--fern`            | `#628B48` | Crédito, valores positivos                    |
| `--desert-sand`     | `#E4BB97` | Cards de destaque                             |
| `--white`           | `#FFFFFF` | Cards neutros                                 |

Tons neutros (bordas, hover, fundos de badge/chip) são derivados via
`rgba()` das cores acima — nenhuma cor nova foi introduzida
(`--taupe-grey-08/15/40`, `--smoky-rose-12`, `--fern-12`,
`--desert-sand-40`). Ver `styles/tokens.css`.

## Tipografia, espaçamento e forma

- **Nunito** (400/600/700) — títulos, labels, botões, texto geral.
- **Inter** (600) — valores monetários (ex.: "R$ 15.000,00").
- Tamanhos: `--fs-sm` 14px, `--fs-base` 16px, `--fs-md` 20px, `--fs-lg` 24px,
  `--fs-xl` 32px.
- Espaçamento em múltiplos de 8px (`--space-1` a `--space-6`).
- Radius padrão `8px` (`--radius`), pílulas `999px` (`--radius-pill`).
- Sombra de card: `0 4px 4px rgba(0,0,0,0.25)` (`--shadow-card`), extraída da
  Homepage do Figma.

## Catálogo de componentes (`styles/components.css`)

Cada classe abaixo tem um comentário no CSS com o componente Swing
equivalente.

| Componente CSS | Uso | Equivalente Swing |
|---|---|---|
| `.sidebar`, `.sidebar-nav-item` | Menu lateral de navegação | `apresentacao.shell.Sidebar` (já existente) |
| `.card`, `.card--destaque` | Cartões de conteúdo | `RoundedPanel` |
| `.btn-primary/secondary/danger/icon` | Botões | `RoundedButton` / `JButton` customizado |
| `.input`, `.select` | Campos de formulário | `RoundedTextField`, `JComboBox`, `JSpinner` |
| `.search-input` | Busca com ícone | `JTextField` + filtro ao vivo |
| `.filter-bar` | Barra de filtros | `JPanel` com `FlowLayout`/`GridBagLayout` |
| `.list`, `.list-item` | Listas de itens | `JList` + `ListCellRenderer` customizado |
| `.badge-credito/débito` | Indicador de tipo de transação | `IconBadge` |
| `.chip`, `.chip-fern`, `.chip-rose` | Etiquetas de categoria/tipo | `JLabel` com `RoundedPanel` |
| `.donut` | Progresso circular | `CircularProgressDonut` (já existente) |
| `.progress-bar` | Progresso linear (limite de crédito) | `JProgressBar` |
| `.drag-handle` | Alça de reordenação | `TransferHandler` + `DropMode.INSERT` na `JList` |
| `.modal-overlay`, `.modal` | Diálogos | `JDialog` (`ConfirmacaoDialog`, `*FormDialog`) |
| `.bar-chart`, `.line-chart` | Gráficos | `BarChartCategoriaPanel`, `LineChartPatrimonioPanel` (já existentes) |

## Telas

| # | Arquivo | Descrição | Sidebar ativa |
|---|---|---|---|
| 1 | `01-cadastro-usuario.html` | Cadastro de usuário (onboarding, sem sidebar) | — |
| 2 | `02-homepage.html` | Dashboard: saldo total, limite de crédito, objetivo principal, gráficos, transações recentes | Dashboard |
| 3 | `03-objetivos.html` | Lista de objetivos com prioridade reordenável (drag-and-drop), busca, criar/editar/remover | Objetivos |
| 4 | `04-transacoes.html` | Lista de transações com filtros por período/tipo/categoria, criar/editar/remover | Transações |
| 5 | `05-contas.html` | Lista de contas com saldo e limite de crédito, busca por número, criar/editar/remover | Contas |
| 6 | `06-investimentos.html` | Lista de ativos com ganho/perda, busca e filtro por tipo, criar; clique abre detalhes | Investimentos |
| 7 | `07-investimento-detalhes.html` | Detalhes de um ativo: resumo + lista de operações (compra/venda), editar/remover | Investimentos (sem item próprio) |

## Mapa de navegação

```
01-cadastro-usuario ──(Entrar)──► 02-homepage
                                       │
            ┌──────────────────────────┼──────────────────────────┐
            ▼              ▼            ▼             ▼            ▼
       02-homepage    05-contas    04-transacoes  03-objetivos  06-investimentos
            ▲              ▲            ▲             ▲            │
            └──────────────┴────────────┴─────────────┘    (clique no ativo)
                                                                     ▼
                                                       07-investimento-detalhes
                                                            │
                                                       (← Voltar)
                                                            ▼
                                                       06-investimentos
```

A sidebar (telas 2–7) permite navegar livremente entre Dashboard, Contas,
Transações, Objetivos e Investimentos. A tela de detalhes de investimento
(7) não tem item próprio na sidebar — "Investimentos" permanece destacado,
seguindo o comportamento de `apresentacao.shell.Sidebar` para
`Tela.INVESTIMENTO_DETALHES` — e usa um link "← Voltar" para retornar à
lista.

## Limitações do Swing consideradas

- **Sem date-pickers de calendário**: campos de data usam `JSpinner` +
  `SpinnerDateModel` (representados nos mockups como `.input` de texto
  formatado `dd/mm/aaaa`).
- **Sem blur/backdrop-filter/animações complexas**: modais usam overlay
  semitransparente simples (`rgba`), sem desfoque.
- **Drag-and-drop de objetivos**: usa `TransferHandler` + `DropMode.INSERT`,
  suporte nativo do `JList` desde Java 6 — sem bibliotecas externas.
- **Gráficos**: barras e linhas são desenhados com `Graphics2D` em
  `JPanel` customizado, seguindo o padrão já existente em
  `BarChartCategoriaPanel` e `LineChartPatrimonioPanel`.
- **Confirmação de remoção**: toda ação destrutiva (objetivo, transação,
  conta, operação de investimento) passa por `ConfirmacaoDialog` antes de
  efetivar, conforme regra geral do sistema.
