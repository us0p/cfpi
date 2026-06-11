package com.cfpi.dominio.entidades.conta;

import com.cfpi.dominio.Identificavel;
import com.cfpi.dominio.arraydinamico.ArrayDinamico;
import com.cfpi.dominio.entidades.banco.Banco;
import com.cfpi.dominio.entidades.investimento.Investimento;
import com.cfpi.dominio.entidades.transacao.Transacao;
import com.cfpi.dominio.entidades.usuario.Usuario;
import com.cfpi.dominio.excecoes.RegraNegocioException;
import com.cfpi.dominio.excecoes.ValidacaoException;

public class Conta implements Identificavel {

    private static int contadorId = 1;
    private static final int CAPACIDADE_INICIAL_TRANSACOES = 10;
    private static final int CAPACIDADE_INICIAL_INVESTIMENTOS = 10;

    private int id;
    private String tipo;
    private double valorConta;
    private String numeroConta;
    private String moeda;
    private Banco banco;
    private Usuario usuario;
    private double limiteCredito;
    private double limiteCreditoUtilizado;
    private ArrayDinamico<Transacao> transacoes;
    private ArrayDinamico<Investimento> investimentos;

    /**
     * Construtor de pesquisa/placeholder, usado apenas para representar uma
     * referência a uma conta. Não aplica nenhuma validação.
     */
    public Conta() {
        this.id = contadorId++;
        this.limiteCreditoUtilizado = 0.0;
        this.transacoes = new ArrayDinamico<>(Transacao.class, CAPACIDADE_INICIAL_TRANSACOES);
        this.investimentos = new ArrayDinamico<>(Investimento.class, CAPACIDADE_INICIAL_INVESTIMENTOS);
    }

    /**
     * Cria uma conta com os dados informados.
     *
     * <p><b>Validações previstas (a implementar):</b></p>
     * <ul>
     *   <li>{@code tipo}: após {@code trim()} e ignorando case, deve ser
     *       {@code "poupança"} ou {@code "corrente"}.</li>
     *   <li>{@code numeroConta}: deve casar com a expressão regular
     *       {@code ^\d{6,}$} (somente dígitos, mínimo de 6 caracteres).</li>
     *   <li>{@code valorConta}: deve ser maior ou igual a zero.</li>
     *   <li>{@code limiteCredito}: deve ser maior ou igual a zero.</li>
     * </ul>
     *
     * <p><b>Regra de negócio prevista (a implementar):</b> se
     * {@code usuario != null && banco != null} e já existir em
     * {@code usuario.getContas()} outra conta com o mesmo
     * {@code banco.getId()} e o mesmo {@code tipo} (comparação
     * case-insensitive, após {@code trim()}), a criação é rejeitada por
     * representar uma conta duplicada.</p>
     *
     * <p><b>Efeito colateral:</b> inicializa {@code limiteCreditoUtilizado}
     * em {@code 0.0}, inicializa as coleções internas de transações e
     * investimentos e, se {@code usuario != null}, registra esta conta em
     * {@code usuario} via {@link Usuario#adicionarConta(Conta)}.</p>
     *
     * @param tipo          tipo da conta ({@code "poupança"} ou {@code "corrente"}).
     * @param valorConta    saldo inicial da conta.
     * @param numeroConta   número da conta (somente dígitos, mínimo 6).
     * @param moeda         moeda da conta (ex: "BRL", "USD").
     * @param banco         banco ao qual a conta está associada (pode ser {@code null}).
     * @param usuario       usuário dono da conta (pode ser {@code null}).
     * @param limiteCredito limite de crédito máximo da conta.
     * @throws ValidacaoException se {@code tipo}, {@code numeroConta},
     *         {@code valorConta} ou {@code limiteCredito} forem inválidos.
     *         (a ser lançada quando a validação for implementada)
     * @throws RegraNegocioException se já existir, para o mesmo usuário e
     *         banco, outra conta com o mesmo {@code tipo}. (a ser lançada
     *         quando a regra for implementada)
     */
    public Conta(String tipo, double valorConta, String numeroConta, String moeda, Banco banco, Usuario usuario, double limiteCredito) {
        this.id = contadorId++;
        this.tipo = tipo;
        this.valorConta = valorConta;
        this.numeroConta = numeroConta;
        this.moeda = moeda;
        this.banco = banco;
        this.usuario = usuario;
        this.limiteCredito = limiteCredito;
        this.limiteCreditoUtilizado = 0.0;
        this.transacoes = new ArrayDinamico<>(Transacao.class, CAPACIDADE_INICIAL_TRANSACOES);
        this.investimentos = new ArrayDinamico<>(Investimento.class, CAPACIDADE_INICIAL_INVESTIMENTOS);

        if (usuario != null) {
            usuario.adicionarConta(this);
        }
    }

    /**
     * Construtor de pesquisa/placeholder, usado apenas para representar uma
     * referência a uma conta já existente a partir do seu {@code id}. Não
     * aplica nenhuma validação e não altera a sequência automática de ids.
     */
    public Conta(int id) {
        this.id = id;
        this.limiteCreditoUtilizado = 0.0;
        this.transacoes = new ArrayDinamico<>(Transacao.class, CAPACIDADE_INICIAL_TRANSACOES);
        this.investimentos = new ArrayDinamico<>(Investimento.class, CAPACIDADE_INICIAL_INVESTIMENTOS);
    }

    @Override
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id; }

    public String getTipo() {
        return tipo;
    }

    /**
     * Define o tipo da conta.
     *
     * <p><b>Validação prevista (a implementar):</b> mesma regra do
     * construtor {@link #Conta(String, double, String, String, Banco,
     * Usuario, double)} para {@code tipo}.</p>
     *
     * @param tipo novo tipo da conta ({@code "poupança"} ou {@code "corrente"}).
     * @throws ValidacaoException se {@code tipo} for inválido. (a ser
     *         lançada quando a validação for implementada)
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValorConta() {
        return valorConta;
    }

    /**
     * Define o saldo da conta.
     *
     * <p><b>Validação prevista (a implementar):</b> mesma regra do
     * construtor {@link #Conta(String, double, String, String, Banco,
     * Usuario, double)} para {@code valorConta} ({@code valorConta >= 0}).</p>
     *
     * @param valorConta novo saldo da conta.
     * @throws ValidacaoException se {@code valorConta} for negativo. (a ser
     *         lançada quando a validação for implementada)
     */
    public void setValorConta(double valorConta) {
        this.valorConta = valorConta;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    /**
     * Define o número da conta.
     *
     * <p><b>Validação prevista (a implementar):</b> mesma regra do
     * construtor {@link #Conta(String, double, String, String, Banco,
     * Usuario, double)} para {@code numeroConta} (regex {@code ^\d{6,}$}).</p>
     *
     * @param numeroConta novo número da conta.
     * @throws ValidacaoException se {@code numeroConta} for inválido. (a ser
     *         lançada quando a validação for implementada)
     */
    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getMoeda() {
        return moeda;
    }
    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public Banco getBanco() {
        return banco;
    }
    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public double getLimiteCredito() {
        return limiteCredito;
    }

    /**
     * Define o limite de crédito máximo da conta.
     *
     * <p><b>Validação prevista (a implementar):</b> mesma regra do
     * construtor {@link #Conta(String, double, String, String, Banco,
     * Usuario, double)} para {@code limiteCredito} ({@code limiteCredito
     * >= 0}).</p>
     *
     * @param limiteCredito novo limite de crédito máximo da conta.
     * @throws ValidacaoException se {@code limiteCredito} for negativo. (a
     *         ser lançada quando a validação for implementada)
     */
    public void setLimiteCredito(double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public double getLimiteCreditoUtilizado() {
        return limiteCreditoUtilizado;
    }

    /**
     * Define o valor do limite de crédito atualmente utilizado.
     *
     * <p>{@code limiteCreditoUtilizado} representa quanto do
     * {@link #getLimiteCredito() limite de crédito máximo} está
     * atualmente em uso; o crédito disponível é {@code limiteCredito -
     * limiteCreditoUtilizado}. Este valor é ajustado pelos efeitos de
     * débitos/créditos sobre a conta, mas também pode ser definido
     * diretamente por este setter.</p>
     *
     * <p><b>Validação prevista (a implementar):</b>
     * {@code limiteCreditoUtilizado >= 0}.</p>
     *
     * @param limiteCreditoUtilizado novo valor do limite de crédito utilizado.
     * @throws ValidacaoException se {@code limiteCreditoUtilizado} for
     *         negativo. (a ser lançada quando a validação for implementada)
     */
    public void setLimiteCreditoUtilizado(double limiteCreditoUtilizado) {
        this.limiteCreditoUtilizado = limiteCreditoUtilizado;
    }

    public boolean adicionarTransacao(Transacao transacao) {
        return transacoes.inserir(transacao);
    }

    public Transacao[] getTransacoes() {
        return transacoes.getArr();
    }

    /**
     * Pesquisa, dentre as transações desta conta, aquela com o {@code id}
     * informado.
     *
     * @param id id da transação procurada.
     * @return a {@link Transacao} correspondente, ou {@code null} se não
     *         encontrada.
     */
    public Transacao pesquisarTransacaoPorId(int id) {
        // TODO: a implementar - percorrer transacoes.getArr() e retornar a
        // transação cujo getId() seja igual a id, ou null se não encontrada.
        return null;
    }

    /**
     * Pesquisa, dentre as transações desta conta, aquelas cuja {@code data}
     * esteja no intervalo {@code [dataInicio, dataFim]} (ambos os limites
     * inclusivos).
     *
     * <p><b>Validação prevista (a implementar):</b> {@code dataInicio} e
     * {@code dataFim} devem ser datas válidas no formato ISO
     * {@code yyyy-MM-dd} (validável via
     * {@code java.time.LocalDate#parse(CharSequence)}), e
     * {@code dataInicio} não pode ser posterior a {@code dataFim}.</p>
     *
     * @param dataInicio data inicial do intervalo (inclusive), formato {@code yyyy-MM-dd}.
     * @param dataFim    data final do intervalo (inclusive), formato {@code yyyy-MM-dd}.
     * @return array com as transações cuja {@code data} esteja dentro do
     *         intervalo informado, podendo ser vazio se nenhuma for
     *         encontrada.
     * @throws ValidacaoException se {@code dataInicio} ou {@code dataFim}
     *         estiverem em formato inválido, ou se {@code dataInicio} for
     *         posterior a {@code dataFim}. (a ser lançada quando a
     *         validação for implementada)
     */
    public Transacao[] pesquisarTransacoesPorPeriodo(String dataInicio, String dataFim) {
        // TODO: a implementar - validar dataInicio/dataFim e filtrar
        // transacoes.getArr() por data dentro do intervalo informado.
        return null;
    }

    /**
     * Remove, da conta, a transação com o {@code id} informado, revertendo
     * seu efeito sobre {@code valorConta}/{@code limiteCreditoUtilizado}.
     *
     * <p><b>Mecânica prevista (a implementar):</b> localizar a transação com
     * o {@code id} informado em {@code transacoes}, chamar
     * {@code transacao.reverterEfeito()} e removê-la do
     * {@link ArrayDinamico} interno.</p>
     *
     * @param id id da transação a ser removida.
     * @return {@code true} se a transação foi encontrada e removida,
     *         {@code false} caso contrário.
     */
    public boolean removerTransacao(int id) {
        // TODO: a implementar - localizar a transação pelo id, chamar
        // reverterEfeito() e remover do ArrayDinamico.
        return false;
    }

    public boolean adicionarInvestimento(Investimento investimento) {
        return investimentos.inserir(investimento);
    }

    public Investimento[] getInvestimentos() {
        return investimentos.getArr();
    }

    /**
     * Pesquisa, dentre os investimentos desta conta, aquele com o
     * {@code id} informado.
     *
     * @param id id do investimento procurado.
     * @return o {@link Investimento} correspondente, ou {@code null} se não
     *         encontrado.
     */
    public Investimento pesquisarInvestimentoPorId(int id) {
        // TODO: a implementar - percorrer investimentos.getArr() e retornar
        // o investimento cujo getId() seja igual a id, ou null se não
        // encontrado.
        return null;
    }

    /**
     * Remove, da conta, o investimento com o {@code id} informado,
     * revertendo seu efeito sobre {@code valorConta}.
     *
     * <p><b>Mecânica prevista (a implementar):</b> localizar o investimento
     * com o {@code id} informado em {@code investimentos}, chamar
     * {@code investimento.reverterEfeito()} e removê-lo do
     * {@link ArrayDinamico} interno.</p>
     *
     * @param id id do investimento a ser removido.
     * @return {@code true} se o investimento foi encontrado e removido,
     *         {@code false} caso contrário.
     */
    public boolean removerInvestimento(int id) {
        // TODO: a implementar - localizar o investimento pelo id, chamar
        // reverterEfeito() e remover do ArrayDinamico.
        return false;
    }
}
