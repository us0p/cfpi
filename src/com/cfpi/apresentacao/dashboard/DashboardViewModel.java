package com.cfpi.apresentacao.dashboard;

import com.cfpi.apresentacao.transacoes.TransacoesViewModel;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.objetivo.Objetivo;
import com.cfpi.dominio.entidades.transacao.Credito;
import com.cfpi.dominio.entidades.transacao.Debito;
import com.cfpi.dominio.entidades.transacao.Transacao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Cálculos e agregações exibidos na tela de Dashboard, derivados dos
 * getters de {@link Conta}/{@link Transacao}/{@link Objetivo} (sem chamar
 * {@code aplicarEfeito()}).
 */
public class DashboardViewModel {

    private static final String TODAS_CATEGORIAS = "todas";

    private final TransacoesViewModel transacoesViewModel = new TransacoesViewModel();

    /**
     * Soma o saldo de todas as contas informadas.
     *
     * @param contas contas do usuário.
     * @return soma de {@link Conta#getValorConta()} de todas as contas.
     */
    public double saldoTotal(Conta[] contas) {
        double total = 0.0;
        for (Conta conta : contas) {
            total += conta.getValorConta();
        }
        return total;
    }

    /**
     * Calcula a fração do limite de crédito total já utilizada.
     *
     * @param contas contas do usuário.
     * @return {@code soma(limiteCreditoUtilizado) / soma(limiteCredito)},
     *         ou {@code 0.0} se a soma dos limites de crédito for zero.
     */
    public double percentualLimiteConsumido(Conta[] contas) {
        double limiteTotal = 0.0;
        double utilizadoTotal = 0.0;
        for (Conta conta : contas) {
            limiteTotal += conta.getLimiteCredito();
            utilizadoTotal += conta.getLimiteCreditoUtilizado();
        }
        if (limiteTotal == 0.0) {
            return 0.0;
        }
        return utilizadoTotal / limiteTotal;
    }

    /**
     * Soma o valor dos débitos de todas as contas, agrupado por categoria.
     *
     * @param contas         contas do usuário.
     * @param filtroCategoria se {@code null} ou {@code "todas"}
     *                        (case-insensitive), considera débitos de todas
     *                        as categorias; caso contrário, considera apenas
     *                        débitos cuja categoria seja igual a
     *                        {@code filtroCategoria} (case-insensitive).
     * @return mapa de categoria para soma de {@link Transacao#getValor()}
     *         dos débitos correspondentes.
     */
    public Map<String, Double> gastosPorCategoria(Conta[] contas, String filtroCategoria) {
        Map<String, Double> gastos = new LinkedHashMap<>();
        for (Conta conta : contas) {
            for (Transacao transacao : conta.getTransacoes()) {
                if (!(transacao instanceof Debito)) {
                    continue;
                }
                if (!aceitaCategoria(transacao.getCategoria(), filtroCategoria)) {
                    continue;
                }
                gastos.merge(transacao.getCategoria(), transacao.getValor(), Double::sum);
            }
        }
        return gastos;
    }

    /**
     * Calcula a série de saldo acumulado ao longo do tempo, a partir das
     * transações de todas as contas.
     *
     * <p>Cada {@link Credito} soma {@code valor} ao saldo acumulado e cada
     * {@link Debito} subtrai {@code valor}; transações são ordenadas por
     * {@link Transacao#getData()} crescente, e transações na mesma data são
     * agregadas em um único ponto. Esta é uma aproximação derivada na UI a
     * partir dos getters, <b>não</b> um replay de
     * {@code aplicarEfeito()}.</p>
     *
     * @param contas         contas do usuário.
     * @param filtroCategoria se {@code null} ou {@code "todas"}
     *                        (case-insensitive), considera transações de
     *                        todas as categorias; caso contrário, considera
     *                        apenas transações cuja categoria seja igual a
     *                        {@code filtroCategoria} (case-insensitive).
     * @return série cronológica de {@link PontoPatrimonio}, um por data
     *         distinta presente nas transações consideradas.
     */
    public List<PontoPatrimonio> crescimentoPatrimonio(Conta[] contas, String filtroCategoria) {
        List<Transacao> transacoes = new ArrayList<>();
        for (Conta conta : contas) {
            for (Transacao transacao : conta.getTransacoes()) {
                if (aceitaCategoria(transacao.getCategoria(), filtroCategoria)) {
                    transacoes.add(transacao);
                }
            }
        }
        transacoes.sort((a, b) -> a.getData().compareTo(b.getData()));

        List<PontoPatrimonio> serie = new ArrayList<>();
        double saldoAcumulado = 0.0;
        for (Transacao transacao : transacoes) {
            saldoAcumulado += transacao instanceof Debito ? -transacao.getValor() : transacao.getValor();
            if (!serie.isEmpty() && serie.get(serie.size() - 1).getData().equals(transacao.getData())) {
                serie.set(serie.size() - 1, new PontoPatrimonio(transacao.getData(), saldoAcumulado));
            } else {
                serie.add(new PontoPatrimonio(transacao.getData(), saldoAcumulado));
            }
        }
        return serie;
    }

    /**
     * Lista as transações dos últimos 7 dias (incluindo {@code hoje}), de
     * todas as contas, mais recentes primeiro.
     *
     * @param contas contas do usuário.
     * @param hoje   data de referência.
     * @return transações cuja {@link Transacao#getData()} está no intervalo
     *         {@code [hoje.minusDays(6), hoje]}, ordenadas por data
     *         decrescente.
     */
    public List<Transacao> transacoesUltimos7Dias(Conta[] contas, LocalDate hoje) {
        LocalDate inicio = hoje.minusDays(6);
        List<Transacao> recentes = new ArrayList<>();
        for (Conta conta : contas) {
            for (Transacao transacao : conta.getTransacoes()) {
                LocalDate data = LocalDate.parse(transacao.getData());
                if (!data.isBefore(inicio) && !data.isAfter(hoje)) {
                    recentes.add(transacao);
                }
            }
        }
        return transacoesViewModel.ordenarPorDataDesc(recentes.toArray(new Transacao[0]));
    }

    /**
     * Determina o objetivo principal a ser exibido no Dashboard.
     *
     * @param ordenados objetivos do usuário, na ordem de exibição (ver
     *                   {@code ObjetivosController.getOrdemSessao()}).
     * @return o primeiro objetivo de {@code ordenados}, ou {@code null} se
     *         a lista estiver vazia.
     */
    public Objetivo objetivoPrincipal(List<Objetivo> ordenados) {
        return ordenados.isEmpty() ? null : ordenados.get(0);
    }

    private boolean aceitaCategoria(String categoria, String filtroCategoria) {
        if (filtroCategoria == null || filtroCategoria.equalsIgnoreCase(TODAS_CATEGORIAS)) {
            return true;
        }
        return categoria != null && categoria.equalsIgnoreCase(filtroCategoria);
    }
}
