package com.cfpi.apresentacao.investimentos;

import com.cfpi.apresentacao.comum.AvaliadorDeAtivos;
import com.cfpi.dominio.entidades.investimento.Investimento;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Transformações puras sobre {@link Investimento}, sem dependências de
 * Swing.
 */
public class InvestimentosViewModel {

    /**
     * Agrupa operações de investimento por ativo (mesmo {@code nomeAtivo},
     * após {@code trim()} e ignorando case, e mesmo subtipo concreto de
     * {@link Investimento}), consolidando quantidade atual, total investido
     * e, via {@code avaliador}, o valor de mercado atual e o ganho/perda não
     * realizado.
     *
     * @param investimentos operações de investimento a serem agrupadas.
     * @param avaliador     usado para calcular o valor de mercado atual de
     *                      cada ativo.
     * @return um {@link AtivoResumo} por ativo, na ordem de primeira
     *         ocorrência em {@code investimentos}.
     */
    public List<AtivoResumo> agruparPorAtivo(Investimento[] investimentos, AvaliadorDeAtivos avaliador) {
        Map<String, List<Investimento>> grupos = new LinkedHashMap<>();
        for (Investimento investimento : investimentos) {
            grupos.computeIfAbsent(chaveAgrupamento(investimento), chave -> new ArrayList<>()).add(investimento);
        }

        List<AtivoResumo> resultado = new ArrayList<>();
        for (List<Investimento> grupo : grupos.values()) {
            Investimento primeiro = grupo.get(0);
            double quantidadeComprada = 0;
            double quantidadeVendida = 0;
            double totalInvestido = 0;
            for (Investimento investimento : grupo) {
                if ("venda".equalsIgnoreCase(investimento.getOperacao())) {
                    quantidadeVendida += investimento.getQuantidade();
                } else {
                    quantidadeComprada += investimento.getQuantidade();
                    totalInvestido += investimento.getValor() * investimento.getQuantidade();
                }
            }
            double quantidadeAtual = quantidadeComprada - quantidadeVendida;
            double valorAtual = avaliador.valorAtual(primeiro.getNomeAtivo(), primeiro.getClass(), quantidadeAtual);
            double ganhoPerda = valorAtual - totalInvestido;
            resultado.add(new AtivoResumo(primeiro.getNomeAtivo(), primeiro.getClass(), quantidadeAtual, totalInvestido, valorAtual, ganhoPerda));
        }
        return resultado;
    }

    private String chaveAgrupamento(Investimento investimento) {
        return investimento.getNomeAtivo().trim().toLowerCase() + "|" + investimento.getClass().getName();
    }

    /**
     * Filtra ativos cujo {@code nomeAtivo} contenha o termo informado.
     *
     * @param ativos ativos a serem filtrados.
     * @param termo  termo de busca; se {@code null} ou em branco, todos os
     *               ativos são retornados.
     * @return cópia da lista de ativos cujo {@code nomeAtivo} contenha
     *         {@code termo} (case-insensitive), ou todos os ativos se
     *         {@code termo} for nulo ou em branco.
     */
    public List<AtivoResumo> filtrarPorNome(List<AtivoResumo> ativos, String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return new ArrayList<>(ativos);
        }
        String alvo = termo.trim().toLowerCase();
        List<AtivoResumo> resultado = new ArrayList<>();
        for (AtivoResumo ativo : ativos) {
            if (ativo.getNomeAtivo().toLowerCase().contains(alvo)) {
                resultado.add(ativo);
            }
        }
        return resultado;
    }

    /**
     * Filtra ativos pelo subtipo de {@link Investimento}.
     *
     * @param ativos ativos a serem filtrados.
     * @param tipo   subtipo desejado; se {@code null}, todos os ativos são
     *               retornados.
     * @return cópia da lista de ativos cujo {@link AtivoResumo#getTipo()}
     *         seja igual a {@code tipo}, ou todos os ativos se {@code tipo}
     *         for {@code null}.
     */
    public List<AtivoResumo> filtrarPorTipo(List<AtivoResumo> ativos, Class<? extends Investimento> tipo) {
        if (tipo == null) {
            return new ArrayList<>(ativos);
        }
        List<AtivoResumo> resultado = new ArrayList<>();
        for (AtivoResumo ativo : ativos) {
            if (ativo.getTipo() == tipo) {
                resultado.add(ativo);
            }
        }
        return resultado;
    }
}
