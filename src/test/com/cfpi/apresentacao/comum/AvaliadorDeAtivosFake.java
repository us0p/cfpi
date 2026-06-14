package com.cfpi.apresentacao.comum;

import com.cfpi.dominio.entidades.investimento.Investimento;

/**
 * Fake de {@link AvaliadorDeAtivos} para uso em testes, que sempre retorna o
 * valor fixo configurado na construção.
 */
public class AvaliadorDeAtivosFake implements AvaliadorDeAtivos {

    private final double valorAtual;

    public AvaliadorDeAtivosFake(double valorAtual) {
        this.valorAtual = valorAtual;
    }

    @Override
    public double valorAtual(String nomeAtivo, Class<? extends Investimento> tipo, double quantidadeAtual) {
        return valorAtual;
    }
}
