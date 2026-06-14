package com.cfpi.apresentacao.transacoes;

import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.transacao.Credito;
import com.cfpi.dominio.entidades.transacao.Debito;
import com.cfpi.dominio.entidades.transacao.Transacao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Ordenação, filtros e categorias para a lista de transações.
 */
public class TransacoesViewModel {

    private static final String[] CATEGORIAS_DEBITO = {"lazer", "mercado", "saude", "indeterminado", "investimentos", "banco"};
    private static final String[] CATEGORIAS_CREDITO = {"pagamento", "rendimento"};

    /**
     * Ordena as transações pela data, da mais recente para a mais antiga.
     *
     * @param transacoes transações a serem ordenadas.
     * @return nova lista ordenada por {@link Transacao#getData()}
     *         decrescente (datas no formato ISO {@code yyyy-MM-dd} ordenam
     *         lexicograficamente como cronologicamente).
     */
    public List<Transacao> ordenarPorDataDesc(Transacao[] transacoes) {
        List<Transacao> ordenadas = new ArrayList<>(Arrays.asList(transacoes));
        ordenadas.sort(Comparator.comparing(Transacao::getData).reversed());
        return ordenadas;
    }

    /**
     * Filtra as transações pelo tipo informado.
     *
     * @param transacoes transações a serem filtradas.
     * @param tipo       subtipo de {@link Transacao} desejado ({@link Debito}
     *                   ou {@link Credito}).
     * @return nova lista contendo apenas as transações que são instâncias de
     *         {@code tipo}.
     */
    public List<Transacao> filtrarPorTipo(List<Transacao> transacoes, Class<? extends Transacao> tipo) {
        List<Transacao> filtradas = new ArrayList<>();
        for (Transacao transacao : transacoes) {
            if (tipo.isInstance(transacao)) {
                filtradas.add(transacao);
            }
        }
        return filtradas;
    }

    /**
     * Filtra as transações pela categoria informada.
     *
     * @param transacoes transações a serem filtradas.
     * @param categoria  categoria desejada, comparada de forma
     *                    case-insensitive com {@link Transacao#getCategoria()}.
     * @return nova lista contendo apenas as transações cuja categoria é
     *         igual a {@code categoria}.
     */
    public List<Transacao> filtrarPorCategoria(List<Transacao> transacoes, String categoria) {
        List<Transacao> filtradas = new ArrayList<>();
        for (Transacao transacao : transacoes) {
            if (transacao.getCategoria() != null && transacao.getCategoria().equalsIgnoreCase(categoria)) {
                filtradas.add(transacao);
            }
        }
        return filtradas;
    }

    /**
     * Lista as categorias válidas para o subtipo de transação informado,
     * espelhando as regras documentadas em {@link Debito#Debito(String,
     * com.cfpi.dominio.entidades.conta.Conta, String, double, String,
     * String)} e {@link Credito#Credito(String,
     * com.cfpi.dominio.entidades.conta.Conta, String, double, String)}.
     *
     * @param tipo {@link Debito} ou {@link Credito}.
     * @return as categorias válidas para {@code tipo}, ou um array vazio se
     *         {@code tipo} não for {@link Debito} nem {@link Credito}.
     */
    public String[] categoriasParaTipo(Class<? extends Transacao> tipo) {
        if (tipo == Debito.class) {
            return CATEGORIAS_DEBITO.clone();
        }
        if (tipo == Credito.class) {
            return CATEGORIAS_CREDITO.clone();
        }
        return new String[0];
    }

    /**
     * Lista as categorias válidas para o subtipo de transação informado (ver
     * {@link #categoriasParaTipo(Class)}), prefixadas pela opção "Todas".
     *
     * @param tipo {@link Debito} ou {@link Credito}.
     * @return {@code {"Todas", ...categoriasParaTipo(tipo)}}.
     */
    public String[] categoriasComOpcaoTodas(Class<? extends Transacao> tipo) {
        String[] categorias = categoriasParaTipo(tipo);
        String[] resultado = new String[categorias.length + 1];
        resultado[0] = "Todas";
        System.arraycopy(categorias, 0, resultado, 1, categorias.length);
        return resultado;
    }

    /**
     * Lista os tipos de débito (à vista/crédito) disponíveis para a
     * {@code conta} informada, espelhando a regra documentada em
     * {@link Debito#Debito(String, Conta, String, double, String, String)}:
     * contas do tipo {@code "poupança"} não oferecem a opção
     * {@code "credito"}.
     *
     * @param conta conta selecionada, ou {@code null} se nenhuma conta
     *              estiver selecionada.
     * @return {@code {"avista"}} para conta poupança, ou
     *         {@code {"avista", "credito"}} nos demais casos (incluindo
     *         {@code conta == null}).
     */
    public String[] tiposDebitoParaConta(Conta conta) {
        boolean ehPoupanca = conta != null && "poupança".equals(conta.getTipo());
        return ehPoupanca ? new String[]{"avista"} : new String[]{"avista", "credito"};
    }
}
