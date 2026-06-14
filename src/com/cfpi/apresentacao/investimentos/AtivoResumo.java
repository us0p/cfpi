package com.cfpi.apresentacao.investimentos;

import com.cfpi.dominio.entidades.investimento.Investimento;

/**
 * Resumo consolidado da posição de um usuário em um ativo (mesmo
 * {@code nomeAtivo} e mesmo subtipo de {@link Investimento}).
 */
public class AtivoResumo {

    private final String nomeAtivo;
    private final Class<? extends Investimento> tipo;
    private final double quantidadeAtual;
    private final double totalInvestido;
    private final double valorAtual;
    private final double ganhoPerda;

    public AtivoResumo(String nomeAtivo, Class<? extends Investimento> tipo, double quantidadeAtual, double totalInvestido, double valorAtual, double ganhoPerda) {
        this.nomeAtivo = nomeAtivo;
        this.tipo = tipo;
        this.quantidadeAtual = quantidadeAtual;
        this.totalInvestido = totalInvestido;
        this.valorAtual = valorAtual;
        this.ganhoPerda = ganhoPerda;
    }

    public String getNomeAtivo() {
        return nomeAtivo;
    }

    public Class<? extends Investimento> getTipo() {
        return tipo;
    }

    public double getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public double getTotalInvestido() {
        return totalInvestido;
    }

    public double getValorAtual() {
        return valorAtual;
    }

    public double getGanhoPerda() {
        return ganhoPerda;
    }
}
