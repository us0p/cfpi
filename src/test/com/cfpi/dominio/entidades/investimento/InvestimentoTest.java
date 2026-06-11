package com.cfpi.dominio.entidades.investimento;

import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.excecoes.ValidacaoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InvestimentoTest {

    @Test
    void idEhIncrementadoAutomaticamenteEntreSubclasses() {
        Conta conta = new Conta();

        Acao acao = new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "compra");
        CDB cdb = new CDB("CDB Banco X", 1000.0, conta, 1.0, 0.0, "2026-06-11", 0.0, "compra");

        assertEquals(acao.getId() + 1, cdb.getId());
    }

    // --- nomeAtivo ---

    @Test
    void criarInvestimentoComNomeAtivoNuloLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> new Acao(null, 30.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "compra"));
    }

    @Test
    void criarInvestimentoComNomeAtivoVazioLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> new Acao("   ", 30.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "compra"));
    }

    @Test
    void criarInvestimentoComNomeAtivoValidoNaoLancaExcecao() {
        Conta conta = new Conta();

        assertDoesNotThrow(() -> new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "compra"));
    }

    // --- valor ---

    @Test
    void criarInvestimentoComValorZeroLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> new Acao("PETR4", 0.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "compra"));
    }

    @Test
    void criarInvestimentoComValorNegativoLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> new Acao("PETR4", -10.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "compra"));
    }

    @Test
    void criarInvestimentoComValorPositivoNaoLancaExcecao() {
        Conta conta = new Conta();

        assertDoesNotThrow(() -> new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "compra"));
    }

    // --- quantidade ---

    @Test
    void criarInvestimentoComQuantidadeZeroLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> new Acao("PETR4", 30.0, conta, 0.0, 0.0, "2026-06-10", 0.0, "compra"));
    }

    @Test
    void criarInvestimentoComQuantidadeNegativaLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> new Acao("PETR4", 30.0, conta, -1.0, 0.0, "2026-06-10", 0.0, "compra"));
    }

    @Test
    void criarInvestimentoComQuantidadePositivaNaoLancaExcecao() {
        Conta conta = new Conta();

        assertDoesNotThrow(() -> new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "compra"));
    }

    // --- data ---

    @Test
    void criarInvestimentoComDataEmFormatoInvalidoLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> new Acao("PETR4", 30.0, conta, 100.0, 0.0, "10-06-2026", 0.0, "compra"));
    }

    @Test
    void criarInvestimentoComDataNulaLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> new Acao("PETR4", 30.0, conta, 100.0, 0.0, null, 0.0, "compra"));
    }

    @Test
    void criarInvestimentoComDataValidaNaoLancaExcecao() {
        Conta conta = new Conta();

        assertDoesNotThrow(() -> new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "compra"));
    }

    // --- operacao ---

    @Test
    void criarInvestimentoComOperacaoCompraNaoLancaExcecao() {
        Conta conta = new Conta();

        assertDoesNotThrow(() -> new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "compra"));
    }

    @Test
    void criarInvestimentoComOperacaoVendaSemContaNaoLancaExcecao() {
        assertDoesNotThrow(() -> new Acao("PETR4", 30.0, null, 100.0, 0.0, "2026-06-10", 0.0, "venda"));
    }

    @Test
    void criarInvestimentoComOperacaoInvalidaLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "transferencia"));
    }

    // --- setters ---

    @Test
    void setNomeAtivoComValorInvalidoLancaValidacaoExceptionENaoAlteraONomeAtivo() {
        Conta conta = new Conta();
        Acao acao = new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "compra");

        assertThrows(ValidacaoException.class, () -> acao.setNomeAtivo(null));
        assertEquals("PETR4", acao.getNomeAtivo());
    }

    @Test
    void setValorComValorInvalidoLancaValidacaoExceptionENaoAlteraOValor() {
        Conta conta = new Conta();
        Acao acao = new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "compra");

        assertThrows(ValidacaoException.class, () -> acao.setValor(0.0));
        assertEquals(30.0, acao.getValor());
    }

    @Test
    void setQuantidadeComValorInvalidoLancaValidacaoExceptionENaoAlteraAQuantidade() {
        Conta conta = new Conta();
        Acao acao = new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "compra");

        assertThrows(ValidacaoException.class, () -> acao.setQuantidade(0.0));
        assertEquals(100.0, acao.getQuantidade());
    }

    @Test
    void setDataComFormatoInvalidoLancaValidacaoException() {
        Conta conta = new Conta();
        Acao acao = new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "compra");

        assertThrows(ValidacaoException.class, () -> acao.setData("31/12/2000"));
    }

    @Test
    void setOperacaoComValorInvalidoLancaValidacaoException() {
        Conta conta = new Conta();
        Acao acao = new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "compra");

        assertThrows(ValidacaoException.class, () -> acao.setOperacao("transferencia"));
    }

    // --- getImpostoPadrao por subtipo ---

    @Test
    void acaoRetornaImpostoPadraoDe15PorCento() {
        assertEquals(0.15, new Acao().getImpostoPadrao());
    }

    @Test
    void cdbRetornaImpostoPadraoDe15PorCento() {
        assertEquals(0.15, new CDB().getImpostoPadrao());
    }

    @Test
    void craRetornaImpostoPadraoZero() {
        assertEquals(0.0, new CRA().getImpostoPadrao());
    }

    @Test
    void criRetornaImpostoPadraoZero() {
        assertEquals(0.0, new CRI().getImpostoPadrao());
    }

    @Test
    void criptoRetornaImpostoPadraoDe15PorCento() {
        assertEquals(0.15, new Cripto().getImpostoPadrao());
    }

    @Test
    void debRetornaImpostoPadraoDe15PorCento() {
        assertEquals(0.15, new DEB().getImpostoPadrao());
    }

    @Test
    void fiiRetornaImpostoPadraoDe17_5PorCento() {
        assertEquals(0.175, new FII().getImpostoPadrao());
    }

    @Test
    void lcaRetornaImpostoPadraoZero() {
        assertEquals(0.0, new LCA().getImpostoPadrao());
    }

    @Test
    void lciRetornaImpostoPadraoZero() {
        assertEquals(0.0, new LCI().getImpostoPadrao());
    }

    @Test
    void pgblRetornaImpostoPadraoDe15PorCento() {
        assertEquals(0.15, new PGBL().getImpostoPadrao());
    }

    @Test
    void tesouroDiretoRetornaImpostoPadraoDe15PorCento() {
        assertEquals(0.15, new TesouroDireto().getImpostoPadrao());
    }

    @Test
    void vgblRetornaImpostoPadraoDe15PorCento() {
        assertEquals(0.15, new VGBL().getImpostoPadrao());
    }
}
