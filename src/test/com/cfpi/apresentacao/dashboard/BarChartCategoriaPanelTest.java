package com.cfpi.apresentacao.dashboard;

import org.junit.jupiter.api.Test;

import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BarChartCategoriaPanelTest {

    @Test
    void getDadosRetornaMapaVazioPorPadrao() {
        BarChartCategoriaPanel painel = new BarChartCategoriaPanel();

        assertTrue(painel.getDados().isEmpty());
    }

    @Test
    void setDadosArmazenaOMapaInformado() {
        BarChartCategoriaPanel painel = new BarChartCategoriaPanel();
        Map<String, Double> dados = new LinkedHashMap<>();
        dados.put("mercado", 130.0);
        dados.put("lazer", 50.0);

        painel.setDados(dados);

        assertEquals(dados, painel.getDados());
    }

    private MouseEvent eventoMouseMovido(BarChartCategoriaPanel painel, int x, int y) {
        return new MouseEvent(painel, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false);
    }

    @Test
    void moverMouseSobreUmaBarraExibeTooltipComCategoriaEValor() {
        BarChartCategoriaPanel painel = new BarChartCategoriaPanel();
        painel.setSize(360, 220);
        Map<String, Double> dados = new LinkedHashMap<>();
        dados.put("mercado", 100.0);
        dados.put("lazer", 50.0);
        painel.setDados(dados);

        // "mercado" é a maior barra, ocupando toda a área útil de 0 a 180.
        painel.dispatchEvent(eventoMouseMovido(painel, 50, 100));

        assertEquals("mercado: R$ 100,00", painel.getToolTipText());
    }

    @Test
    void moverMouseForaDasBarrasNaoExibeTooltip() {
        BarChartCategoriaPanel painel = new BarChartCategoriaPanel();
        painel.setSize(360, 220);
        Map<String, Double> dados = new LinkedHashMap<>();
        dados.put("mercado", 100.0);
        dados.put("lazer", 50.0);
        painel.setDados(dados);

        painel.dispatchEvent(eventoMouseMovido(painel, 0, 0));

        assertNull(painel.getToolTipText());
    }
}
