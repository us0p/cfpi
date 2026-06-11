package com.cfpi.dominio.entidades.transacao;

import com.cfpi.dominio.Identificavel;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.excecoes.ValidacaoException;

public abstract class Transacao implements Identificavel {

    private static int contadorId = 1;

    private int id;
    private String descricao;
    private Conta conta;
    private String data;
    private double valor;
    private String categoria;

    public Transacao() {
        this.id = contadorId++;
    }

    /**
     * Cria uma transação com os dados informados.
     *
     * <p><b>Validações previstas (a implementar):</b></p>
     * <ul>
     *   <li>{@code valor}: deve ser maior que zero.</li>
     *   <li>{@code data}: deve estar no formato ISO {@code yyyy-MM-dd}
     *       (validável via {@code java.time.LocalDate#parse(CharSequence)});
     *       datas futuras são permitidas.</li>
     * </ul>
     *
     * <p><b>Efeito colateral:</b> se {@code conta != null}, registra esta
     * transação em {@code conta} via
     * {@link Conta#adicionarTransacao(Transacao)}.</p>
     *
     * @param descricao descrição da transação.
     * @param conta     conta à qual a transação pertence (pode ser {@code null}).
     * @param data      data da transação, formato {@code yyyy-MM-dd}.
     * @param valor     valor da transação (deve ser maior que zero).
     * @param categoria categoria da transação.
     * @throws ValidacaoException se {@code valor} ou {@code data} forem
     *         inválidos. (a ser lançada quando a validação for implementada)
     */
    public Transacao(String descricao, Conta conta, String data, double valor, String categoria) {
        this.id = contadorId++;
        this.descricao = descricao;
        this.conta = conta;
        this.data = data;
        this.valor = valor;
        this.categoria = categoria;

        if (conta != null) {
            conta.adicionarTransacao(this);
        }
    }

    public Transacao(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Conta getConta() {
        return conta;
    }
    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public String getData() {
        return data;
    }

    /**
     * Define a data da transação.
     *
     * <p><b>Validação prevista (a implementar):</b> mesma regra do
     * construtor {@link #Transacao(String, Conta, String, double, String)}
     * para {@code data} (formato ISO {@code yyyy-MM-dd}).</p>
     *
     * @param data nova data da transação, formato {@code yyyy-MM-dd}.
     * @throws ValidacaoException se {@code data} estiver em formato
     *         inválido. (a ser lançada quando a validação for implementada)
     */
    public void setData(String data) {
        this.data = data;
    }

    public double getValor() {
        return valor;
    }

    /**
     * Define o valor da transação.
     *
     * <p><b>Validação prevista (a implementar):</b> mesma regra do
     * construtor {@link #Transacao(String, Conta, String, double, String)}
     * para {@code valor} ({@code valor > 0}).</p>
     *
     * @param valor novo valor da transação.
     * @throws ValidacaoException se {@code valor} for menor ou igual a
     *         zero. (a ser lançada quando a validação for implementada)
     */
    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Aplica, sobre a {@link Conta} desta transação, o efeito correspondente
     * a este tipo de transação (ex: somar/subtrair {@code valor} de
     * {@code valorConta} e/ou {@code limiteCreditoUtilizado}).
     *
     * <p>Cada subclasse documenta, em sua própria implementação, exatamente
     * quais campos da conta são afetados e como.</p>
     */
    public abstract void aplicarEfeito();

    /**
     * Reverte, sobre a {@link Conta} desta transação, o efeito previamente
     * aplicado por {@link #aplicarEfeito()}.
     *
     * <p>Cada subclasse documenta, em sua própria implementação, exatamente
     * quais campos da conta são revertidos e como.</p>
     */
    public abstract void reverterEfeito();
}