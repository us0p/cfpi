package com.cfpi.dominio.entidades.conta;

import com.cfpi.dominio.Identificavel;
import com.cfpi.dominio.arraydinamico.ArrayDinamico;
import com.cfpi.dominio.entidades.banco.Banco;
import com.cfpi.dominio.entidades.investimento.Investimento;
import com.cfpi.dominio.entidades.transacao.Transacao;
import com.cfpi.dominio.entidades.usuario.Usuario;
import com.cfpi.dominio.excecoes.RegraNegocioException;
import com.cfpi.dominio.excecoes.ValidacaoException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
     * <p><b>Validações:</b></p>
     * <ul>
     *   <li>{@code tipo}: após {@code trim()} e ignorando case, deve ser
     *       {@code "poupança"} ou {@code "corrente"}.</li>
     *   <li>{@code numeroConta}: deve casar com a expressão regular
     *       {@code ^\d{6,}$} (somente dígitos, mínimo de 6 caracteres).</li>
     *   <li>{@code valorConta}: deve ser maior ou igual a zero.</li>
     *   <li>{@code limiteCredito}: deve ser maior ou igual a zero.</li>
     * </ul>
     *
     * <p><b>Regra de negócio:</b> se {@code usuario != null && banco !=
     * null} e já existir em {@code usuario.getContas()} outra conta com o
     * mesmo {@code banco.getId()} e o mesmo {@code tipo} (comparação
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
     * @throws RegraNegocioException se já existir, para o mesmo usuário e
     *         banco, outra conta com o mesmo {@code tipo}.
     */
    public Conta(String tipo, double valorConta, String numeroConta, String moeda, Banco banco, Usuario usuario, double limiteCredito) {
        validarTipo(tipo);
        validarNumeroConta(numeroConta);
        validarValorConta(valorConta);
        validarLimiteCredito(limiteCredito);

        if (usuario != null && banco != null) {
            verificarContaDuplicada(usuario, banco, tipo);
        }

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

    // -------------------------------------------------------------------------
    // Métodos privados de validação
    // -------------------------------------------------------------------------

    private static void validarTipo(String tipo) {
        if (tipo == null) {
            throw new ValidacaoException("O tipo da conta não pode ser nulo.");
        }
        String t = tipo.trim().toLowerCase();
        if (!t.equals("poupança") && !t.equals("corrente")) {
            throw new ValidacaoException("O tipo da conta deve ser 'poupança' ou 'corrente'.");
        }
    }

    private static void validarNumeroConta(String numeroConta) {
        if (numeroConta == null || !numeroConta.matches("^\\d{6,}$")) {
            throw new ValidacaoException("O número da conta deve conter apenas dígitos, com no mínimo 6 caracteres.");
        }
    }

    private static void validarValorConta(double valorConta) {
        if (valorConta < 0) {
            throw new ValidacaoException("O valor da conta não pode ser negativo.");
        }
    }

    private static void validarLimiteCredito(double limiteCredito) {
        if (limiteCredito < 0) {
            throw new ValidacaoException("O limite de crédito não pode ser negativo.");
        }
    }

    private static void validarLimiteCreditoUtilizado(double limiteCreditoUtilizado) {
        if (limiteCreditoUtilizado < 0) {
            throw new ValidacaoException("O limite de crédito utilizado não pode ser negativo.");
        }
    }

    private static void verificarContaDuplicada(Usuario usuario, Banco banco, String tipo) {
        String alvo = tipo.trim();
        for (Conta conta : usuario.getContas()) {
            if (conta.getBanco() != null
                    && conta.getBanco().getId() == banco.getId()
                    && conta.getTipo() != null
                    && conta.getTipo().trim().equalsIgnoreCase(alvo)) {
                throw new RegraNegocioException(
                        "Já existe uma conta deste tipo para este banco e usuário.");
            }
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
     * <p><b>Validação:</b> mesma regra do construtor {@link #Conta(String,
     * double, String, String, Banco, Usuario, double)} para {@code tipo}.</p>
     *
     * @param tipo novo tipo da conta ({@code "poupança"} ou {@code "corrente"}).
     * @throws ValidacaoException se {@code tipo} for inválido.
     */
    public void setTipo(String tipo) {
        validarTipo(tipo);
        this.tipo = tipo;
    }

    public double getValorConta() {
        return valorConta;
    }

    /**
     * Define o saldo da conta.
     *
     * <p><b>Validação:</b> mesma regra do construtor {@link #Conta(String,
     * double, String, String, Banco, Usuario, double)} para
     * {@code valorConta} ({@code valorConta >= 0}).</p>
     *
     * @param valorConta novo saldo da conta.
     * @throws ValidacaoException se {@code valorConta} for negativo.
     */
    public void setValorConta(double valorConta) {
        validarValorConta(valorConta);
        this.valorConta = valorConta;
    }

    /**
     * Ajusta o saldo da conta somando {@code delta} ao valor atual.
     *
     * <p>Usado por {@code aplicarEfeito()}/{@code reverterEfeito()} de
     * {@link Transacao} e {@link Investimento}, que podem legitimamente
     * deixar {@code valorConta} negativo (ex: saldo devedor); por isso, ao
     * contrário de {@link #setValorConta(double)}, este método não valida
     * {@code valorConta >= 0}.</p>
     *
     * @param delta valor a ser somado ao saldo atual (pode ser negativo).
     */
    public void ajustarValorConta(double delta) {
        this.valorConta += delta;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    /**
     * Define o número da conta.
     *
     * <p><b>Validação:</b> mesma regra do construtor {@link #Conta(String,
     * double, String, String, Banco, Usuario, double)} para
     * {@code numeroConta} (regex {@code ^\d{6,}$}).</p>
     *
     * @param numeroConta novo número da conta.
     * @throws ValidacaoException se {@code numeroConta} for inválido.
     */
    public void setNumeroConta(String numeroConta) {
        validarNumeroConta(numeroConta);
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
     * <p><b>Validação:</b> mesma regra do construtor {@link #Conta(String,
     * double, String, String, Banco, Usuario, double)} para
     * {@code limiteCredito} ({@code limiteCredito >= 0}).</p>
     *
     * @param limiteCredito novo limite de crédito máximo da conta.
     * @throws ValidacaoException se {@code limiteCredito} for negativo.
     */
    public void setLimiteCredito(double limiteCredito) {
        validarLimiteCredito(limiteCredito);
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
     * <p><b>Validação:</b> {@code limiteCreditoUtilizado >= 0}.</p>
     *
     * @param limiteCreditoUtilizado novo valor do limite de crédito utilizado.
     * @throws ValidacaoException se {@code limiteCreditoUtilizado} for
     *         negativo.
     */
    public void setLimiteCreditoUtilizado(double limiteCreditoUtilizado) {
        validarLimiteCreditoUtilizado(limiteCreditoUtilizado);
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
        for (Transacao transacao : transacoes.getArr()) {
            if (transacao.getId() == id) {
                return transacao;
            }
        }
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
     *         posterior a {@code dataFim}.
     */
    public Transacao[] pesquisarTransacoesPorPeriodo(String dataInicio, String dataFim) {
        LocalDate inicio;
        LocalDate fim;
        try {
            inicio = LocalDate.parse(dataInicio);
            fim = LocalDate.parse(dataFim);
        } catch (DateTimeParseException e) {
            throw new ValidacaoException("dataInicio e dataFim devem estar no formato yyyy-MM-dd.", e);
        }
        if (inicio.isAfter(fim)) {
            throw new ValidacaoException("dataInicio não pode ser posterior a dataFim.");
        }

        ArrayDinamico<Transacao> resultado = new ArrayDinamico<>(Transacao.class, CAPACIDADE_INICIAL_TRANSACOES);
        for (Transacao transacao : transacoes.getArr()) {
            LocalDate data = LocalDate.parse(transacao.getData());
            if (!data.isBefore(inicio) && !data.isAfter(fim)) {
                resultado.inserir(transacao);
            }
        }
        return resultado.getArr();
    }

    /**
     * Remove, da conta, a transação com o {@code id} informado, revertendo
     * seu efeito sobre {@code valorConta}/{@code limiteCreditoUtilizado}.
     *
     * @param id id da transação a ser removida.
     * @return {@code true} se a transação foi encontrada e removida,
     *         {@code false} caso contrário.
     */
    public boolean removerTransacao(int id) {
        Transacao[] arr = transacoes.getArr();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].getId() == id) {
                arr[i].reverterEfeito();
                return transacoes.remover(i);
            }
        }
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
        for (Investimento investimento : investimentos.getArr()) {
            if (investimento.getId() == id) {
                return investimento;
            }
        }
        return null;
    }

    /**
     * Remove, da conta, o investimento com o {@code id} informado,
     * revertendo seu efeito sobre {@code valorConta}.
     *
     * @param id id do investimento a ser removido.
     * @return {@code true} se o investimento foi encontrado e removido,
     *         {@code false} caso contrário.
     */
    public boolean removerInvestimento(int id) {
        Investimento[] arr = investimentos.getArr();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].getId() == id) {
                arr[i].reverterEfeito();
                return investimentos.remover(i);
            }
        }
        return false;
    }
}
