package com.cfpi.dominio.entidades.transacao;

import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.excecoes.ValidacaoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreditoTest {

    @Test
    void criarCreditoComCategoriaPagamentoNaoLancaExcecao() {
        Conta conta = new Conta();

        assertDoesNotThrow(() -> new Credito("Estorno", conta, "2026-06-10", 500.0, "pagamento"));
    }

    @Test
    void criarCreditoComCategoriaRendimentoNaoLancaExcecao() {
        Conta conta = new Conta();

        assertDoesNotThrow(() -> new Credito("Salario", conta, "2026-06-10", 500.0, "rendimento"));
    }

    @Test
    void criarCreditoComCategoriaInvalidaLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> new Credito("Salario", conta, "2026-06-10", 500.0, "outra"));
    }

    @Test
    void criarCreditoSomaValorAoValorContaDaConta() {
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, null, 0.0);

        new Credito("Salario", conta, "2026-06-10", 500.0, "rendimento");

        assertEquals(1500.0, conta.getValorConta());
    }

    @Test
    void setCategoriaComValorInvalidoLancaValidacaoException() {
        Conta conta = new Conta();
        Credito credito = new Credito("Salario", conta, "2026-06-10", 500.0, "rendimento");

        assertThrows(ValidacaoException.class, () -> credito.setCategoria("outra"));
    }
}
