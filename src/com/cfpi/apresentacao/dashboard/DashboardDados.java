package com.cfpi.apresentacao.dashboard;

import com.cfpi.dominio.entidades.objetivo.Objetivo;
import com.cfpi.dominio.entidades.transacao.Transacao;

import java.util.List;
import java.util.Map;

/**
 * Dados consolidados exibidos na tela de Dashboard.
 */
public class DashboardDados {

    private final double saldoTotal;
    private final double percentualLimiteConsumido;
    private final Map<String, Double> gastosPorCategoria;
    private final List<PontoPatrimonio> crescimentoPatrimonio;
    private final List<Transacao> transacoesRecentes;
    private final Objetivo objetivoPrincipal;
    private final int diasRestantesObjetivo;

    public DashboardDados(double saldoTotal, double percentualLimiteConsumido, Map<String, Double> gastosPorCategoria,
                           List<PontoPatrimonio> crescimentoPatrimonio, List<Transacao> transacoesRecentes,
                           Objetivo objetivoPrincipal, int diasRestantesObjetivo) {
        this.saldoTotal = saldoTotal;
        this.percentualLimiteConsumido = percentualLimiteConsumido;
        this.gastosPorCategoria = gastosPorCategoria;
        this.crescimentoPatrimonio = crescimentoPatrimonio;
        this.transacoesRecentes = transacoesRecentes;
        this.objetivoPrincipal = objetivoPrincipal;
        this.diasRestantesObjetivo = diasRestantesObjetivo;
    }

    public double getSaldoTotal() {
        return saldoTotal;
    }

    public double getPercentualLimiteConsumido() {
        return percentualLimiteConsumido;
    }

    public Map<String, Double> getGastosPorCategoria() {
        return gastosPorCategoria;
    }

    public List<PontoPatrimonio> getCrescimentoPatrimonio() {
        return crescimentoPatrimonio;
    }

    public List<Transacao> getTransacoesRecentes() {
        return transacoesRecentes;
    }

    public Objetivo getObjetivoPrincipal() {
        return objetivoPrincipal;
    }

    public int getDiasRestantesObjetivo() {
        return diasRestantesObjetivo;
    }
}
