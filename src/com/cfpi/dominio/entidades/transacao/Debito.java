package com.cfpi.dominio.entidades.transacao;

import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.excecoes.ValidacaoException;
import java.util.List;

public class Debito extends Transacao {

     private static final List<String> TIPOS_VALIDOS = List.of("credito", "avista");
    private static final List<String> CATEGORIAS_VALIDAS = List.of("lazer", "mercado", "saude", "indeterminado", "investimentos", "banco");

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
        setCategoria(categoria); 
        setTipo(tipo);           
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
    
        if (tipo == null || !TIPOS_VALIDOS.contains(tipo.trim().toLowerCase())) {
            throw new ValidacaoException("Tipo inválido. Use: credito ou avista.");
        }
        this.tipo = tipo.trim().toLowerCase();
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
        
        if (categoria == null || !CATEGORIAS_VALIDAS.contains(categoria.trim().toLowerCase())) {
            throw new ValidacaoException("Categoria inválida. Use: lazer, mercado, saude, indeterminado, investimentos ou banco.");
        }
        super.setCategoria(categoria.trim().toLowerCase());
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
        Conta conta = getConta();
        if (conta == null) return;

        String cat = getCategoria() != null ? getCategoria().trim().toLowerCase() : "";
        String tip = tipo != null ? tipo.trim().toLowerCase() : "";

        if (cat.equals("banco")) {
            
            conta.setValorConta(conta.getValorConta() - getValor());
            conta.setLimiteCreditoUtilizado(Math.max(0, conta.getLimiteCreditoUtilizado() - getValor()));

        } else if (tip.equals("credito")) {
            
            conta.setLimiteCreditoUtilizado(conta.getLimiteCreditoUtilizado() + getValor());

        } else {
            
            conta.setValorConta(conta.getValorConta() - getValor());
        }
    }

    /**
     * Reverte o efeito previamente aplicado por {@link #aplicarEfeito()},
     * desfazendo as alterações sobre {@code valorConta}/
     * {@code limiteCreditoUtilizado} da conta associada.
     */
    @Override
    public void reverterEfeito() {
        Conta conta = getConta();
        if (conta == null) return;

        String cat = getCategoria() != null ? getCategoria().trim().toLowerCase() : "";
        String tip = tipo != null ? tipo.trim().toLowerCase() : "";

        if (cat.equals("banco")) {
            
            conta.setValorConta(conta.getValorConta() + getValor());
            conta.setLimiteCreditoUtilizado(conta.getLimiteCreditoUtilizado() + getValor());

        } else if (tip.equals("credito")) {
            
            conta.setLimiteCreditoUtilizado(conta.getLimiteCreditoUtilizado() - getValor());

        } else {
            
            conta.setValorConta(conta.getValorConta() + getValor());
        }
    }
}
