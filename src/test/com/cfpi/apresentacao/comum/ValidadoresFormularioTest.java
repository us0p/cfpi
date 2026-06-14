package com.cfpi.apresentacao.comum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidadoresFormularioTest {

    @Test
    void nomeValidoAceitaNomeComposto() {
        assertTrue(ValidadoresFormulario.nomeValido("Ana Maria"));
    }

    @Test
    void nomeValidoRejeitaNomeCurto() {
        assertFalse(ValidadoresFormulario.nomeValido("An"));
    }

    @Test
    void nomeValidoRejeitaNomeComDigitos() {
        assertFalse(ValidadoresFormulario.nomeValido("Ana123"));
    }

    @Test
    void nomeValidoRejeitaNulo() {
        assertFalse(ValidadoresFormulario.nomeValido(null));
    }

    @Test
    void cpfValidoAceitaOnzeDigitos() {
        assertTrue(ValidadoresFormulario.cpfValido("11111111111"));
    }

    @Test
    void cpfValidoRejeitaQuantidadeDiferenteDeOnzeDigitos() {
        assertFalse(ValidadoresFormulario.cpfValido("123"));
    }

    @Test
    void cpfValidoRejeitaCaracteresNaoNumericos() {
        assertFalse(ValidadoresFormulario.cpfValido("1111111111a"));
    }

    @Test
    void telefoneValidoAceitaOnzeDigitos() {
        assertTrue(ValidadoresFormulario.telefoneValido("11999999999"));
    }

    @Test
    void telefoneValidoRejeitaQuantidadeDiferenteDeOnzeDigitos() {
        assertFalse(ValidadoresFormulario.telefoneValido("119999999"));
    }

    @Test
    void dataIsoValidaAceitaFormatoIso() {
        assertTrue(ValidadoresFormulario.dataIsoValida("2026-06-10"));
    }

    @Test
    void dataIsoValidaRejeitaFormatoInvalido() {
        assertFalse(ValidadoresFormulario.dataIsoValida("10/06/2026"));
    }

    @Test
    void dataPassadaValidaAceitaDataNoPassado() {
        assertTrue(ValidadoresFormulario.dataPassadaValida("1990-01-01"));
    }

    @Test
    void dataPassadaValidaRejeitaDataFutura() {
        assertFalse(ValidadoresFormulario.dataPassadaValida("2099-01-01"));
    }

    @Test
    void dataPassadaValidaRejeitaFormatoInvalido() {
        assertFalse(ValidadoresFormulario.dataPassadaValida("data-invalida"));
    }

    @Test
    void numeroContaValidoAceitaSeisOuMaisDigitos() {
        assertTrue(ValidadoresFormulario.numeroContaValido("123456"));
    }

    @Test
    void numeroContaValidoRejeitaMenosDeSeisDigitos() {
        assertFalse(ValidadoresFormulario.numeroContaValido("12345"));
    }

    @Test
    void numeroContaValidoRejeitaCaracteresNaoNumericos() {
        assertFalse(ValidadoresFormulario.numeroContaValido("12345a"));
    }

    @Test
    void valorPositivoAceitaValorMaiorQueZero() {
        assertTrue(ValidadoresFormulario.valorPositivo(0.01));
    }

    @Test
    void valorPositivoRejeitaZero() {
        assertFalse(ValidadoresFormulario.valorPositivo(0.0));
    }

    @Test
    void valorPositivoRejeitaNegativo() {
        assertFalse(ValidadoresFormulario.valorPositivo(-1.0));
    }

    @Test
    void valorPositivoTextoAceitaValorMaiorQueZero() {
        assertTrue(ValidadoresFormulario.valorPositivo("0.01"));
    }

    @Test
    void valorPositivoTextoRejeitaZeroENegativo() {
        assertFalse(ValidadoresFormulario.valorPositivo("0"));
        assertFalse(ValidadoresFormulario.valorPositivo("-1"));
    }

    @Test
    void valorPositivoTextoRejeitaNaoNumericoENulo() {
        assertFalse(ValidadoresFormulario.valorPositivo("abc"));
        assertFalse(ValidadoresFormulario.valorPositivo(null));
    }

    @Test
    void valorNaoNegativoAceitaZeroEPositivo() {
        assertTrue(ValidadoresFormulario.valorNaoNegativo("0"));
        assertTrue(ValidadoresFormulario.valorNaoNegativo("1.5"));
    }

    @Test
    void valorNaoNegativoRejeitaNegativoNaoNumericoENulo() {
        assertFalse(ValidadoresFormulario.valorNaoNegativo("-0.01"));
        assertFalse(ValidadoresFormulario.valorNaoNegativo("abc"));
        assertFalse(ValidadoresFormulario.valorNaoNegativo(null));
    }

    @Test
    void numeroValidoAceitaNumeroValido() {
        assertTrue(ValidadoresFormulario.numeroValido("10.5"));
    }

    @Test
    void numeroValidoRejeitaNaoNumericoENulo() {
        assertFalse(ValidadoresFormulario.numeroValido("abc"));
        assertFalse(ValidadoresFormulario.numeroValido(null));
    }

    @Test
    void operacaoValidaAceitaCompraEVendaIndependenteDeCaixaEEspacos() {
        assertTrue(ValidadoresFormulario.operacaoValida("compra"));
        assertTrue(ValidadoresFormulario.operacaoValida(" VENDA "));
    }

    @Test
    void operacaoValidaRejeitaValorDesconhecidoENulo() {
        assertFalse(ValidadoresFormulario.operacaoValida("transferencia"));
        assertFalse(ValidadoresFormulario.operacaoValida(null));
    }
}
