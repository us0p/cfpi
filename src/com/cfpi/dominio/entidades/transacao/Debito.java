package com.cfpi.dominio.entidades.transacao;

import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.excecoes.ValidacaoException;

public class Debito extends Transacao {

    private String tipo;

    public Debito() {
        super();
    }

    /**
     * Cria um débito com os dados informados.
     *
     * <p><b>Validações previstas (a implementar):</b></p>
     * <ul>
     *   <li>{@code tipo}: após {@code trim()} e ignorando case, deve ser
     *       {@code "credito"} ou {@code "avista"}.</li>
     *   <li>{@code categoria}: após {@code trim()} e ignorando case, deve
     *       ser uma das seguintes: {@code "lazer"}, {@code "mercado"},
     *       {@code "saude"}, {@code "indeterminado"},
     *       {@code "investimentos"} ou {@code "banco"}.</li>
     * </ul>
     *
     * <p><b>Efeito colateral:</b> ao final, chama {@link #aplicarEfeito()}
     * para refletir este débito sobre {@code valorConta}/
     * {@code limiteCreditoUtilizado} da conta associada.</p>
     *
     * @param descricao descrição do débito.
     * @param conta     conta à qual o débito pertence (pode ser {@code null}).
     * @param data      data do débito, formato {@code yyyy-MM-dd}.
     * @param valor     valor do débito (deve ser maior que zero).
     * @param categoria categoria do débito.
     * @param tipo      tipo do débito ({@code "credito"} ou {@code "avista"}).
     * @throws ValidacaoException se {@code valor}, {@code data},
     *         {@code categoria} ou {@code tipo} forem inválidos. (a ser
     *         lançada quando a validação for implementada)
     */
    public Debito(String descricao, Conta conta, String data, double valor, String categoria, String tipo) {
        super(descricao, conta, data, valor, categoria);
        this.tipo = tipo;
        aplicarEfeito();
    }

    public Debito(int id) {
        super(id);
    }

    public String getTipo() {
        return tipo;
    }

    /**
     * Define o tipo do débito.
     *
     * <p><b>Validação prevista (a implementar):</b> mesma regra do
     * construtor {@link #Debito(String, Conta, String, double, String,
     * String)} para {@code tipo}.</p>
     *
     * @param tipo novo tipo do débito ({@code "credito"} ou {@code "avista"}).
     * @throws ValidacaoException se {@code tipo} for inválido. (a ser
     *         lançada quando a validação for implementada)
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Define a categoria do débito.
     *
     * <p><b>Validação prevista (a implementar):</b> mesma regra do
     * construtor {@link #Debito(String, Conta, String, double, String,
     * String)} para {@code categoria}.</p>
     *
     * @param categoria nova categoria do débito.
     * @throws ValidacaoException se {@code categoria} for inválida. (a ser
     *         lançada quando a validação for implementada)
     */
    @Override
    public void setCategoria(String categoria) {
        super.setCategoria(categoria);
    }

    /**
     * Aplica o efeito deste débito sobre a conta associada.
     *
     * <p><b>Comportamento previsto (a implementar):</b></p>
     * <ul>
     *   <li>Se {@code categoria == "banco"} (tem precedência sobre
     *       {@code tipo}): subtrai {@code valor} de {@code valorConta} e
     *       subtrai {@code valor} de {@code limiteCreditoUtilizado} da
     *       conta, sem deixar {@code limiteCreditoUtilizado} ficar negativo
     *       (usando {@code Math.max(0, ...)}).</li>
     *   <li>Senão, se {@code tipo == "credito"}: soma {@code valor} a
     *       {@code limiteCreditoUtilizado} da conta; não altera
     *       {@code valorConta}.</li>
     *   <li>Senão (ou seja, {@code tipo == "avista"}): subtrai
     *       {@code valor} de {@code valorConta} da conta; não altera
     *       {@code limiteCreditoUtilizado}.</li>
     * </ul>
     */
    @Override
    public void aplicarEfeito() {
        // TODO: a implementar - aplicar o efeito deste débito sobre
        // valorConta/limiteCreditoUtilizado da conta associada, conforme
        // documentado acima.
    }

    /**
     * Reverte o efeito previamente aplicado por {@link #aplicarEfeito()},
     * desfazendo as alterações sobre {@code valorConta}/
     * {@code limiteCreditoUtilizado} da conta associada.
     */
    @Override
    public void reverterEfeito() {
        // TODO: a implementar - reverter o efeito aplicado por
        // aplicarEfeito() sobre a conta associada.
    }
}
