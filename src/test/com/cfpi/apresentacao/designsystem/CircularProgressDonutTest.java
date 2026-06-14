package com.cfpi.apresentacao.designsystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CircularProgressDonutTest {

    @Test
    void percentualPadraoEhZero() {
        CircularProgressDonut donut = new CircularProgressDonut(Cores.PRIMARIO, Cores.BORDA, 10);

        assertEquals(0.0, donut.getPercentual());
    }

    @Test
    void setPercentualArmazenaValorDentroDoIntervalo() {
        CircularProgressDonut donut = new CircularProgressDonut(Cores.PRIMARIO, Cores.BORDA, 10);

        donut.setPercentual(0.5);

        assertEquals(0.5, donut.getPercentual());
    }

    @Test
    void setPercentualFixaValorAbaixoDeZeroEmZero() {
        CircularProgressDonut donut = new CircularProgressDonut(Cores.PRIMARIO, Cores.BORDA, 10);

        donut.setPercentual(-0.2);

        assertEquals(0.0, donut.getPercentual());
    }

    @Test
    void setPercentualFixaValorAcimaDeUmEmUm() {
        CircularProgressDonut donut = new CircularProgressDonut(Cores.PRIMARIO, Cores.BORDA, 10);

        donut.setPercentual(1.5);

        assertEquals(1.0, donut.getPercentual());
    }
}
