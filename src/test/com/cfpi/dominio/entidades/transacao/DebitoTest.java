package com.cfpi.dominio.entidades.transacao;

import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.excecoes.RegraNegocioException;
import com.cfpi.dominio.excecoes.ValidacaoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DebitoTest {

    @Test
    void criarDebitoComTipoAvistaNaoLancaExcecao() {
        Conta conta = new Conta("corrente", 500.0, "123456", "BRL", null, null, 0.0);

        assertDoesNotThrow(() -> new Debito("Mercado", conta, "2026-06-10", 100.0, "mercado", "avista"));
    }

    @Test
    void criarDebitoComTipoCreditoNaoLancaExcecao() {
        Conta conta = new Conta();

        assertDoesNotThrow(() -> new Debito("Compra", conta, "2026-06-10", 100.0, "lazer", "credito"));
    }

    @Test
    void criarDebitoComTipoInvalidoLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> new Debito("Compra", conta, "2026-06-10", 100.0, "lazer", "boleto"));
    }

    @Test
    void criarDebitoComCategoriaValidaNaoLancaExcecao() {
        Conta conta = new Conta("corrente", 500.0, "123456", "BRL", null, null, 0.0);

        assertDoesNotThrow(() -> new Debito("Cinema", conta, "2026-06-10", 100.0, "lazer", "avista"));
    }

    @Test
    void criarDebitoComCategoriaInvalidaLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> new Debito("Compra", conta, "2026-06-10", 100.0, "outra", "avista"));
    }

    @Test
    void criarDebitoTipoAvistaSubtraiValorDeValorContaENaoAlteraLimiteCreditoUtilizado() {
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, null, 500.0);

        new Debito("Mercado", conta, "2026-06-10", 200.0, "mercado", "avista");

        assertEquals(800.0, conta.getValorConta());
        assertEquals(0.0, conta.getLimiteCreditoUtilizado());
    }

    @Test
    void criarDebitoTipoCreditoAumentaLimiteCreditoUtilizadoENaoAlteraValorConta() {
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, null, 500.0);

        new Debito("Compra", conta, "2026-06-10", 200.0, "lazer", "credito");

        assertEquals(200.0, conta.getLimiteCreditoUtilizado());
        assertEquals(1000.0, conta.getValorConta());
    }

    @Test
    void criarDebitoCategoriaBancoSubtraiDeValorContaEReduzLimiteCreditoUtilizado() {
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, null, 500.0);
        conta.setLimiteCreditoUtilizado(300.0);

        new Debito("Pagamento fatura", conta, "2026-06-10", 200.0, "banco", "avista");

        assertEquals(800.0, conta.getValorConta());
        assertEquals(100.0, conta.getLimiteCreditoUtilizado());
    }

    @Test
    void criarDebitoCategoriaBancoNaoReduzLimiteCreditoUtilizadoAbaixoDeZero() {
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, null, 500.0);
        conta.setLimiteCreditoUtilizado(100.0);

        new Debito("Pagamento fatura", conta, "2026-06-10", 200.0, "banco", "avista");

        assertEquals(0.0, conta.getLimiteCreditoUtilizado());
    }

    @Test
    void setTipoComValorInvalidoLancaValidacaoException() {
        Conta conta = new Conta("corrente", 500.0, "123456", "BRL", null, null, 0.0);
        Debito debito = new Debito("Mercado", conta, "2026-06-10", 100.0, "mercado", "avista");

        assertThrows(ValidacaoException.class, () -> debito.setTipo("boleto"));
    }

    @Test
    void setCategoriaComValorInvalidoLancaValidacaoException() {
        Conta conta = new Conta("corrente", 500.0, "123456", "BRL", null, null, 0.0);
        Debito debito = new Debito("Mercado", conta, "2026-06-10", 100.0, "mercado", "avista");

        assertThrows(ValidacaoException.class, () -> debito.setCategoria("outra"));
    }

    @Test
    void criarDebitoTipoAvistaComValorMaiorQueSaldoLancaRegraNegocioException() {
        Conta conta = new Conta("corrente", 5000.0, "123456", "BRL", null, null, 0.0);

        assertThrows(RegraNegocioException.class, () ->
                new Debito("Supermercado", conta, "2026-06-10", 6000.0, "mercado", "avista"));
    }
}
