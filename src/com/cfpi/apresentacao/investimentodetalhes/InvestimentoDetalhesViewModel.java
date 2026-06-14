package com.cfpi.apresentacao.investimentodetalhes;

import com.cfpi.dominio.entidades.investimento.Investimento;

import java.util.ArrayList;
import java.util.List;

/**
 * Transformações puras sobre as operações de um único ativo, sem
 * dependências de Swing.
 */
public class InvestimentoDetalhesViewModel {

    /**
     * Filtra as operações de investimento que pertencem ao ativo informado
     * (mesmo {@code nomeAtivo}, após {@code trim()} e ignorando case, e mesmo
     * subtipo concreto de {@link Investimento}).
     *
     * @param todos     operações a serem filtradas.
     * @param nomeAtivo nome/identificador do ativo.
     * @param tipo      subtipo concreto de {@link Investimento}.
     * @return as operações de {@code todos} que pertencem ao ativo informado,
     *         na ordem em que aparecem em {@code todos}.
     */
    public List<Investimento> operacoesDoAtivo(Investimento[] todos, String nomeAtivo, Class<? extends Investimento> tipo) {
        String alvo = nomeAtivo.trim().toLowerCase();
        List<Investimento> resultado = new ArrayList<>();
        for (Investimento investimento : todos) {
            if (investimento.getNomeAtivo().trim().toLowerCase().equals(alvo) && investimento.getClass() == tipo) {
                resultado.add(investimento);
            }
        }
        return resultado;
    }

    /**
     * @param operacoes operações de um único ativo.
     * @return a menor {@code data} (formato ISO {@code yyyy-MM-dd}) entre as
     *         operações de {@code "compra"}, ou {@code null} se não houver
     *         nenhuma.
     */
    public String dataPrimeiraCompra(List<Investimento> operacoes) {
        String menor = null;
        for (Investimento investimento : operacoes) {
            if ("compra".equalsIgnoreCase(investimento.getOperacao())) {
                if (menor == null || investimento.getData().compareTo(menor) < 0) {
                    menor = investimento.getData();
                }
            }
        }
        return menor;
    }

    /**
     * @param operacoes operações de um único ativo.
     * @return a soma de {@code valor * quantidade} das operações de
     *         {@code "compra"}.
     */
    public double totalInvestido(List<Investimento> operacoes) {
        double total = 0;
        for (Investimento investimento : operacoes) {
            if (!"venda".equalsIgnoreCase(investimento.getOperacao())) {
                total += investimento.getValor() * investimento.getQuantidade();
            }
        }
        return total;
    }

    /**
     * @param operacoes operações de um único ativo.
     * @return a soma das {@code quantidade} de operações de {@code "compra"}
     *         menos a soma das {@code quantidade} de operações de
     *         {@code "venda"}.
     */
    public double quantidadeAtual(List<Investimento> operacoes) {
        double comprada = 0;
        double vendida = 0;
        for (Investimento investimento : operacoes) {
            if ("venda".equalsIgnoreCase(investimento.getOperacao())) {
                vendida += investimento.getQuantidade();
            } else {
                comprada += investimento.getQuantidade();
            }
        }
        return comprada - vendida;
    }
}
