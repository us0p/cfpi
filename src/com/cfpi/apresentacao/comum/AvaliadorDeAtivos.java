package com.cfpi.apresentacao.comum;

import com.cfpi.dominio.entidades.investimento.Investimento;

/**
 * Abstrai o cálculo do valor de mercado atual de uma posição em um ativo,
 * usado para exibir ganho/perda não realizado nas telas de investimentos.
 */
public interface AvaliadorDeAtivos {

    /**
     * Calcula o valor de mercado atual de uma posição em um ativo.
     *
     * @param nomeAtivo       nome/identificador do ativo (ex: "PETR4").
     * @param tipo            subtipo concreto de {@link Investimento} do ativo.
     * @param quantidadeAtual quantidade atualmente em posse do ativo.
     * @return valor de mercado atual da posição.
     */
    double valorAtual(String nomeAtivo, Class<? extends Investimento> tipo, double quantidadeAtual);
}
