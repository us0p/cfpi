package com.cfpi.dominio.entidades.transacao;

import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.excecoes.ValidacaoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransacaoTest {

    @Test
    void idEhIncrementadoAutomaticamenteEntreSubclasses() {
        Conta conta = new Conta();

        Credito credito = new Credito("Salario", conta, "2026-06-10", 1000.0, "pagamento");
        Debito debito = new Debito("Mercado", conta, "2026-06-11", 50.0, "mercado", "avista");

        assertEquals(credito.getId() + 1, debito.getId());
    }

    @Test
    void criarTransacaoComValorZeroLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> new Credito("Salario", conta, "2026-06-10", 0.0, "rendimento"));
    }

    @Test
    void criarTransacaoComValorNegativoLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> new Credito("Salario", conta, "2026-06-10", -10.0, "rendimento"));
    }

    @Test
    void criarTransacaoComDataEmFormatoInvalidoLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> new Credito("Salario", conta, "10-06-2026", 1000.0, "rendimento"));
    }

    @Test
    void criarTransacaoComDataNulaLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> new Credito("Salario", conta, null, 1000.0, "rendimento"));
    }

    @Test
    void setValorComValorInvalidoLancaValidacaoExceptionENaoAlteraOValor() {
        Conta conta = new Conta();
        Credito credito = new Credito("Salario", conta, "2026-06-10", 1000.0, "rendimento");

        assertThrows(ValidacaoException.class, () -> credito.setValor(0.0));
        assertEquals(1000.0, credito.getValor());
    }

    @Test
    void setDataComFormatoInvalidoLancaValidacaoException() {
        Conta conta = new Conta();
        Credito credito = new Credito("Salario", conta, "2026-06-10", 1000.0, "rendimento");

        assertThrows(ValidacaoException.class, () -> credito.setData("31/12/2000"));
    }
}
