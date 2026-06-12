package com.cfpi.dominio.entidades.transacao;

import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.excecoes.ValidacaoException;
import java.util.List;

public class Credito extends Transacao {

  private static final List<String> CATEGORIAS_VALIDAS = List.of("pagamento", "rendimento");

    public Credito() {
        super();
    }


    /**
     * Cria um crédito com os dados informados.
     *
     * <p><b>Validação prevista (a implementar):</b> {@code categoria}, após
     * {@code trim()} e ignorando case, deve ser {@code "pagamento"} ou
     * {@code "rendimento"}.</p>
     *
     * <p><b>Efeito colateral:</b> ao final, chama {@link #aplicarEfeito()}
     * para refletir este crédito sobre {@code valorConta} da conta
     * associada.</p>
     *
     * @param descricao descrição do crédito.
     * @param conta     conta à qual o crédito pertence (pode ser {@code null}).
     * @param data      data do crédito, formato {@code yyyy-MM-dd}.
     * @param valor     valor do crédito (deve ser maior que zero).
     * @param categoria categoria do crédito ({@code "pagamento"} ou {@code "rendimento"}).
     * @throws ValidacaoException se {@code valor}, {@code data} ou
     *         {@code categoria} forem inválidos. (a ser lançada quando a
     *         validação for implementada)
     */
    public Credito(String descricao, Conta conta, String data, double valor, String categoria) {
        super(descricao, conta, data, valor, categoria);
        setCategoria(categoria);
        aplicarEfeito();         
    }

    public Credito(int id) {
        super(id);
    }
    
    /**
     * Define a categoria do crédito.
     *
     * <p><b>Validação prevista (a implementar):</b> mesma regra do
     * construtor {@link #Credito(String, Conta, String, double, String)}
     * para {@code categoria}.</p>
     *
     * @param categoria nova categoria do crédito ({@code "pagamento"} ou {@code "rendimento"}).
     * @throws ValidacaoException se {@code categoria} for inválida. (a ser
     *         lançada quando a validação for implementada)
     */
    
      public void setCategoria(String categoria) {
    
        if (categoria == null || !CATEGORIAS_VALIDAS.contains(categoria.trim().toLowerCase())) {
            throw new ValidacaoException("Categoria inválida. Use: pagamento ou rendimento.");
        }
        super.setCategoria(categoria);
    }

    /**
     * Aplica o efeito deste crédito sobre a conta associada.
     *
     * <p><b>Comportamento previsto (a implementar):</b> soma {@code valor}
     * a {@code valorConta} da conta associada, independente da
     * {@code categoria} (que é apenas classificação).</p>
     */
    @Override
     public void aplicarEfeito() {
        Conta conta = getConta();
        if (conta != null) {
            conta.setValorConta(conta.getValorConta() + getValor());
        }
    }
    /**
     * Reverte o efeito previamente aplicado por {@link #aplicarEfeito()},
     * subtraindo {@code valor} de {@code valorConta} da conta associada.
     */
    @Override
    public void reverterEfeito() {
        Conta conta = getConta();
        if (conta != null) {
            conta.setValorConta(conta.getValorConta() - getValor());
        }
    }
}
