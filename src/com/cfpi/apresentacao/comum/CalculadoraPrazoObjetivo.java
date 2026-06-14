package com.cfpi.apresentacao.comum;

import com.cfpi.dominio.entidades.objetivo.Objetivo;

/**
 * Abstrai o cálculo do número de dias restantes para um {@link Objetivo},
 * exibido no card "objetivo principal" da homepage.
 */
public interface CalculadoraPrazoObjetivo {

    /**
     * Valor sentinela retornado por {@link #diasRestantes(Objetivo)} quando
     * o prazo para alcançar o objetivo não pode ser determinado a partir do
     * fluxo de caixa projetado atual (ex.: fluxo mensal projetado não
     * positivo).
     */
    int PRAZO_INDETERMINADO = 36500;

    /**
     * Calcula o número de dias restantes para alcançar o objetivo informado.
     *
     * @param objetivo objetivo cujo prazo será calculado.
     * @return número de dias restantes, ou {@link #PRAZO_INDETERMINADO} se o
     *         prazo não puder ser determinado.
     */
    int diasRestantes(Objetivo objetivo);
}
