package com.cfpi.dominio.entidades.investimento;

import com.cfpi.dominio.Identificavel;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.excecoes.RegraNegocioException;
import com.cfpi.dominio.excecoes.ValidacaoException;

public abstract class Investimento implements Identificavel {

    private static int contadorId = 1;

    private int id;
    private String nomeAtivo;
    private double valor;
    private Conta conta;
    private double quantidade;
    private double imposto;
    private String data;
    private double valorRealizado;
    private String operacao;

    public Investimento() {
        this.id = contadorId++;
    }

    /**
     * Cria um investimento com os dados informados.
     *
     * <p><b>Validações previstas (a implementar):</b></p>
     * <ul>
     *   <li>{@code nomeAtivo}: não pode ser {@code null} nem vazio após
     *       {@code trim()}.</li>
     *   <li>{@code valor}: deve ser maior que zero (preço unitário do
     *       ativo).</li>
     *   <li>{@code quantidade}: deve ser maior que zero.</li>
     *   <li>{@code data}: deve ser uma data válida no formato ISO
     *       {@code yyyy-MM-dd} (validável via
     *       {@code java.time.LocalDate#parse(CharSequence)}); datas futuras
     *       são permitidas.</li>
     *   <li>{@code operacao}: após {@code trim()} e ignorando case, deve ser
     *       {@code "compra"} ou {@code "venda"}.</li>
     * </ul>
     *
     * <p><b>Regra de negócio prevista (a implementar):</b> se
     * {@code conta != null} e {@code operacao} for {@code "venda"}, a soma
     * das {@code quantidade} de operações {@code "venda"} do mesmo ativo
     * (mesmo {@code nomeAtivo}, case-insensitive após {@code trim()}, e
     * mesmo subtipo concreto de {@code Investimento}) já registradas em
     * {@code conta}, somada à {@code quantidade} desta nova operação, não
     * pode exceder a soma das {@code quantidade} de operações
     * {@code "compra"} do mesmo ativo já registradas em {@code conta}. Se
     * {@code conta == null}, esta checagem não se aplica.</p>
     *
     * <p><b>Efeito colateral:</b> se {@code conta != null}, registra este
     * investimento em {@code conta} via
     * {@link Conta#adicionarInvestimento(Investimento)} e em seguida chama
     * {@link #aplicarEfeito()}.</p>
     *
     * @param nomeAtivo      nome/identificador do ativo (ex: "PETR4").
     * @param valor          preço unitário do ativo na operação.
     * @param conta          conta à qual este investimento está associado (pode ser {@code null}).
     * @param quantidade     quantidade de unidades do ativo na operação.
     * @param imposto        imposto inicial informado (recalculado por
     *                        {@link #aplicarEfeito()} quando {@code operacao} for {@code "venda"}).
     * @param data           data da operação, formato {@code yyyy-MM-dd}.
     * @param valorRealizado valor realizado inicial informado (recalculado por
     *                        {@link #aplicarEfeito()} quando {@code operacao} for {@code "venda"}).
     * @param operacao       tipo da operação ({@code "compra"} ou {@code "venda"}).
     * @throws ValidacaoException se {@code nomeAtivo}, {@code valor},
     *         {@code quantidade}, {@code data} ou {@code operacao} forem
     *         inválidos. (a ser lançada quando a validação for implementada)
     * @throws RegraNegocioException se {@code operacao} for {@code "venda"}
     *         e a quantidade vendida do ativo exceder a quantidade comprada
     *         registrada em {@code conta}. (a ser lançada quando a regra for
     *         implementada)
     */
    public Investimento(String nomeAtivo, double valor, Conta conta, double quantidade, double imposto, String data, double valorRealizado, String operacao) {
        this.id = contadorId++;
        this.nomeAtivo = nomeAtivo;
        this.valor = valor;
        this.conta = conta;
        this.quantidade = quantidade;
        this.imposto = imposto;
        this.data = data;
        this.valorRealizado = valorRealizado;
        this.operacao = operacao;

        if (conta != null) {
            conta.adicionarInvestimento(this);
        }

        aplicarEfeito();
    }

    public Investimento(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNomeAtivo() {
        return nomeAtivo;
    }

    /**
     * Define o nome/identificador do ativo.
     *
     * <p><b>Validação prevista (a implementar):</b> mesma regra do
     * construtor {@link #Investimento(String, double, Conta, double, double,
     * String, double, String)} para {@code nomeAtivo}.</p>
     *
     * @param nomeAtivo novo nome/identificador do ativo.
     * @throws ValidacaoException se {@code nomeAtivo} for inválido. (a ser
     *         lançada quando a validação for implementada)
     */
    public void setNomeAtivo(String nomeAtivo) {
        this.nomeAtivo = nomeAtivo;
    }

    public double getValor() {
        return valor;
    }

    /**
     * Define o preço unitário do ativo.
     *
     * <p><b>Validação prevista (a implementar):</b> mesma regra do
     * construtor {@link #Investimento(String, double, Conta, double, double,
     * String, double, String)} para {@code valor} ({@code valor > 0}).</p>
     *
     * @param valor novo preço unitário do ativo.
     * @throws ValidacaoException se {@code valor} não for maior que zero. (a
     *         ser lançada quando a validação for implementada)
     */
    public void setValor(double valor) {
        this.valor = valor;
    }

    public Conta getConta() {
        return conta;
    }
    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public double getQuantidade() {
        return quantidade;
    }

    /**
     * Define a quantidade de unidades do ativo.
     *
     * <p><b>Validação prevista (a implementar):</b> mesma regra do
     * construtor {@link #Investimento(String, double, Conta, double, double,
     * String, double, String)} para {@code quantidade} ({@code quantidade >
     * 0}).</p>
     *
     * @param quantidade nova quantidade de unidades do ativo.
     * @throws ValidacaoException se {@code quantidade} não for maior que
     *         zero. (a ser lançada quando a validação for implementada)
     */
    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getImposto() {
        return imposto;
    }
    public void setImposto(double imposto) {
        this.imposto = imposto;
    }

    public String getData() {
        return data;
    }

    /**
     * Define a data da operação.
     *
     * <p><b>Validação prevista (a implementar):</b> mesma regra do
     * construtor {@link #Investimento(String, double, Conta, double, double,
     * String, double, String)} para {@code data} (formato ISO
     * {@code yyyy-MM-dd}).</p>
     *
     * @param data nova data da operação.
     * @throws ValidacaoException se {@code data} estiver em formato
     *         inválido. (a ser lançada quando a validação for implementada)
     */
    public void setData(String data) {
        this.data = data;
    }

    public double getValorRealizado() {
        return valorRealizado;
    }
    public void setValorRealizado(double valorRealizado) {
        this.valorRealizado = valorRealizado;
    }

    public String getOperacao() {
        return operacao;
    }

    /**
     * Define o tipo da operação.
     *
     * <p><b>Validação prevista (a implementar):</b> mesma regra do
     * construtor {@link #Investimento(String, double, Conta, double, double,
     * String, double, String)} para {@code operacao} ({@code "compra"} ou
     * {@code "venda"}).</p>
     *
     * @param operacao novo tipo da operação.
     * @throws ValidacaoException se {@code operacao} for inválida. (a ser
     *         lançada quando a validação for implementada)
     */
    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    /**
     * Retorna a alíquota padrão de imposto sobre o ganho de capital deste
     * tipo de investimento, usada por {@link #aplicarEfeito()} no cálculo de
     * {@code imposto} em operações de {@code "venda"}.
     *
     * @return a alíquota padrão (ex: {@code 0.15} para 15%), definida pela
     *         constante {@code IMPOSTO_PADRAO} de cada subtipo.
     */
    public abstract double getImpostoPadrao();

    /**
     * Aplica o efeito desta operação sobre {@code conta.valorConta} e, em
     * operações de {@code "venda"}, recalcula {@code imposto} e
     * {@code valorRealizado}.
     *
     * <p><b>Comportamento previsto (a implementar):</b></p>
     * <ul>
     *   <li>Se {@code conta == null}, não faz nada.</li>
     *   <li>Se {@code operacao} for {@code "compra"}, subtrai
     *       {@code valor * quantidade} de {@code conta.valorConta}.</li>
     *   <li>Se {@code operacao} for {@code "venda"}, calcula
     *       {@code custoMedioCompra} como a média ponderada (por
     *       {@code quantidade}) de {@code valor} de todas as operações
     *       {@code "compra"} do mesmo ativo (mesmo {@code nomeAtivo},
     *       case-insensitive após {@code trim()}, e mesmo subtipo concreto)
     *       já registradas em {@code conta}; em seguida define
     *       {@code this.imposto = Math.max(0, (valor - custoMedioCompra) *
     *       quantidade) * getImpostoPadrao()} e
     *       {@code this.valorRealizado = valor * quantidade - imposto}, e
     *       soma {@code valorRealizado} a {@code conta.valorConta}.</li>
     * </ul>
     */
    public void aplicarEfeito() {
        // TODO: a implementar - aplicar o efeito desta operação sobre
        // conta.valorConta e, se operacao == "venda", recalcular imposto e
        // valorRealizado com base no custo médio de compra do mesmo ativo,
        // conforme documentado acima.
    }

    /**
     * Reverte o efeito aplicado por {@link #aplicarEfeito()} sobre
     * {@code conta.valorConta}.
     *
     * <p><b>Comportamento previsto (a implementar), de forma simples e sem
     * checagem em cascata sobre outras operações do mesmo ativo:</b></p>
     * <ul>
     *   <li>Se {@code conta == null}, não faz nada.</li>
     *   <li>Se {@code operacao} for {@code "compra"}, soma de volta
     *       {@code valor * quantidade} a {@code conta.valorConta}.</li>
     *   <li>Se {@code operacao} for {@code "venda"}, subtrai
     *       {@code this.valorRealizado} de {@code conta.valorConta}.</li>
     * </ul>
     */
    public void reverterEfeito() {
        // TODO: a implementar - reverter o efeito aplicado por
        // aplicarEfeito() sobre conta.valorConta, conforme documentado
        // acima.
    }
}
