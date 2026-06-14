package com.cfpi.apresentacao.comum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormatadoresTest {

    @Test
    void formatarMoedaFormataValorPositivoEmPtBr() {
        assertEquals("R$ 1.234,56", Formatadores.formatarMoeda(1234.56));
    }

    @Test
    void formatarMoedaFormataZero() {
        assertEquals("R$ 0,00", Formatadores.formatarMoeda(0.0));
    }

    @Test
    void formatarMoedaFormataValorNegativo() {
        assertEquals("-R$ 100,00", Formatadores.formatarMoeda(-100.0));
    }

    @Test
    void formatarDataConverteIsoParaExibicao() {
        assertEquals("10/06/2026", Formatadores.formatarData("2026-06-10"));
    }

    @Test
    void formatarDataRetornaEntradaSeNaoForIsoValida() {
        assertEquals("data-invalida", Formatadores.formatarData("data-invalida"));
    }

    @Test
    void formatarDataRetornaNuloSeEntradaForNula() {
        assertEquals(null, Formatadores.formatarData(null));
    }

    @Test
    void formatarPercentualArredondaParaInteiroMaisProximo() {
        assertEquals("95%", Formatadores.formatarPercentual(0.953));
    }

    @Test
    void formatarPercentualFormataZero() {
        assertEquals("0%", Formatadores.formatarPercentual(0.0));
    }

    @Test
    void formatarPercentualFormataCemPorCento() {
        assertEquals("100%", Formatadores.formatarPercentual(1.0));
    }
}
