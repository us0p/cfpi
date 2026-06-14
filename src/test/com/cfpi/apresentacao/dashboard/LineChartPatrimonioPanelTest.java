package com.cfpi.apresentacao.dashboard;

import org.junit.jupiter.api.Test;

import java.awt.event.MouseEvent;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LineChartPatrimonioPanelTest {

    @Test
    void getDadosRetornaListaVaziaPorPadrao() {
        LineChartPatrimonioPanel painel = new LineChartPatrimonioPanel();

        assertTrue(painel.getDados().isEmpty());
    }

    @Test
    void setDadosArmazenaAListaInformada() {
        LineChartPatrimonioPanel painel = new LineChartPatrimonioPanel();
        List<PontoPatrimonio> dados = List.of(
                new PontoPatrimonio("2026-06-01", 1000.0),
                new PontoPatrimonio("2026-06-02", 800.0));

        painel.setDados(dados);

        assertEquals(dados, painel.getDados());
    }

    private MouseEvent eventoMouseMovido(LineChartPatrimonioPanel painel, int x, int y) {
        return new MouseEvent(painel, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false);
    }

    @Test
    void moverMouseSobreUmPontoExibeTooltipComDataEValor() {
        LineChartPatrimonioPanel painel = new LineChartPatrimonioPanel();
        painel.setSize(360, 220);
        painel.setDados(List.of(
                new PontoPatrimonio("2026-06-01", 1000.0),
                new PontoPatrimonio("2026-06-02", 1000.0)));

        // Margem = 12, pontos com o mesmo saldo ficam em y = altura - margem.
        painel.dispatchEvent(eventoMouseMovido(painel, 12, 208));

        assertEquals("01/06/2026: R$ 1.000,00", painel.getToolTipText());
    }

    @Test
    void moverMouseForaDosPontosNaoExibeTooltip() {
        LineChartPatrimonioPanel painel = new LineChartPatrimonioPanel();
        painel.setSize(360, 220);
        painel.setDados(List.of(
                new PontoPatrimonio("2026-06-01", 1000.0),
                new PontoPatrimonio("2026-06-02", 1000.0)));

        painel.dispatchEvent(eventoMouseMovido(painel, 180, 20));

        assertNull(painel.getToolTipText());
    }
}
