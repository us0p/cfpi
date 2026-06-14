package com.cfpi.apresentacao.comum;

import com.cfpi.dominio.entidades.objetivo.Objetivo;

/**
 * Fake de {@link CalculadoraPrazoObjetivo} para uso em testes, que sempre
 * retorna o valor fixo configurado na construção.
 */
public class CalculadoraPrazoObjetivoFake implements CalculadoraPrazoObjetivo {

    private final int diasRestantes;

    public CalculadoraPrazoObjetivoFake(int diasRestantes) {
        this.diasRestantes = diasRestantes;
    }

    @Override
    public int diasRestantes(Objetivo objetivo) {
        return diasRestantes;
    }
}
