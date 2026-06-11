package com.cfpi.dominio.entidades.conta;

import com.cfpi.dominio.entidades.banco.Banco;
import com.cfpi.dominio.entidades.investimento.Acao;
import com.cfpi.dominio.entidades.investimento.Investimento;
import com.cfpi.dominio.entidades.investimento.LCA;
import com.cfpi.dominio.entidades.transacao.Credito;
import com.cfpi.dominio.entidades.transacao.Debito;
import com.cfpi.dominio.entidades.transacao.Transacao;
import com.cfpi.dominio.entidades.usuario.Usuario;
import com.cfpi.dominio.excecoes.RegraNegocioException;
import com.cfpi.dominio.excecoes.ValidacaoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContaTest {

    private Usuario novoUsuario() {
        return new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");
    }

    private Banco novoBanco() {
        return new Banco("Banco A", 100);
    }

    @Test
    void criarTransacaoComContaRegistraTransacaoNaConta() {
        Conta conta = new Conta();

        Transacao transacao = new Credito("Salario", conta, "2026-06-10", 1000.0, "Renda");

        Transacao[] transacoes = conta.getTransacoes();
        assertEquals(1, transacoes.length);
        assertSame(transacao, transacoes[0]);
    }

    @Test
    void criarInvestimentoComContaRegistraInvestimentoNaConta() {
        Conta conta = new Conta();

        Investimento investimento = new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "compra");

        Investimento[] investimentos = conta.getInvestimentos();
        assertEquals(1, investimentos.length);
        assertSame(investimento, investimentos[0]);
    }

    @Test
    void idEhIncrementadoAutomaticamenteACadaNovaInstancia() {
        Conta conta1 = new Conta();
        Conta conta2 = new Conta();

        assertEquals(conta1.getId() + 1, conta2.getId());
    }

    @Test
    void criarContaComUsuarioRegistraContaNoUsuario() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");

        Conta conta = new Conta("Corrente", 100.0, "0001", "BRL", null, usuario, 0.0);

        Conta[] contas = usuario.getContas();
        assertEquals(1, contas.length);
        assertSame(conta, contas[0]);
    }

    @Test
    void criarMultiplasContasComMesmoUsuarioRegistraTodas() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");

        Conta conta1 = new Conta("Corrente", 100.0, "0001", "BRL", null, usuario, 0.0);
        Conta conta2 = new Conta("Poupanca", 200.0, "0002", "BRL", null, usuario, 0.0);

        Conta[] contas = usuario.getContas();
        assertEquals(2, contas.length);
        assertSame(conta1, contas[0]);
        assertSame(conta2, contas[1]);
    }

    @Test
    void contaSemUsuarioNaoEhRegistradaEmNenhumUsuario() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");

        new Conta();

        assertEquals(0, usuario.getContas().length);
    }

    @Test
    void contaArmazenaReferenciasParaBancoEUsuario() {
        Banco banco = new Banco("Banco Teste", 237);
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");

        Conta conta = new Conta("Corrente", 100.0, "0001", "BRL", banco, usuario, 0.0);

        assertSame(banco, conta.getBanco());
        assertSame(usuario, conta.getUsuario());
    }

    // --- tipo ---

    @Test
    void criarContaComTipoCorrenteNaoLancaExcecao() {
        Usuario usuario = novoUsuario();
        Banco banco = novoBanco();

        assertDoesNotThrow(() -> new Conta("corrente", 1000.0, "123456", "BRL", banco, usuario, 0.0));
    }

    @Test
    void criarContaComTipoPoupancaNaoLancaExcecao() {
        Usuario usuario = novoUsuario();
        Banco banco = novoBanco();

        assertDoesNotThrow(() -> new Conta("poupança", 1000.0, "123456", "BRL", banco, usuario, 0.0));
    }

    @Test
    void criarContaComTipoInvalidoLancaValidacaoException() {
        Usuario usuario = novoUsuario();
        Banco banco = novoBanco();

        assertThrows(ValidacaoException.class, () -> new Conta("investimento", 1000.0, "123456", "BRL", banco, usuario, 0.0));
    }

    @Test
    void criarContaComTipoNuloLancaValidacaoException() {
        Usuario usuario = novoUsuario();
        Banco banco = novoBanco();

        assertThrows(ValidacaoException.class, () -> new Conta(null, 1000.0, "123456", "BRL", banco, usuario, 0.0));
    }

    // --- numeroConta ---

    @Test
    void criarContaComNumeroContaDe6DigitosNaoLancaExcecao() {
        Usuario usuario = novoUsuario();
        Banco banco = novoBanco();

        assertDoesNotThrow(() -> new Conta("corrente", 1000.0, "123456", "BRL", banco, usuario, 0.0));
    }

    @Test
    void criarContaComNumeroContaDe5DigitosLancaValidacaoException() {
        Usuario usuario = novoUsuario();
        Banco banco = novoBanco();

        assertThrows(ValidacaoException.class, () -> new Conta("corrente", 1000.0, "12345", "BRL", banco, usuario, 0.0));
    }

    @Test
    void criarContaComNumeroContaContendoLetrasLancaValidacaoException() {
        Usuario usuario = novoUsuario();
        Banco banco = novoBanco();

        assertThrows(ValidacaoException.class, () -> new Conta("corrente", 1000.0, "12345a", "BRL", banco, usuario, 0.0));
    }

    @Test
    void criarContaComNumeroContaNuloLancaValidacaoException() {
        Usuario usuario = novoUsuario();
        Banco banco = novoBanco();

        assertThrows(ValidacaoException.class, () -> new Conta("corrente", 1000.0, null, "BRL", banco, usuario, 0.0));
    }

    // --- valorConta ---

    @Test
    void criarContaComValorContaNegativoLancaValidacaoException() {
        Usuario usuario = novoUsuario();
        Banco banco = novoBanco();

        assertThrows(ValidacaoException.class, () -> new Conta("corrente", -1.0, "123456", "BRL", banco, usuario, 0.0));
    }

    @Test
    void criarContaComValorContaZeroNaoLancaExcecao() {
        Usuario usuario = novoUsuario();
        Banco banco = novoBanco();

        assertDoesNotThrow(() -> new Conta("corrente", 0.0, "123456", "BRL", banco, usuario, 0.0));
    }

    // --- limiteCredito ---

    @Test
    void criarContaComLimiteCreditoNegativoLancaValidacaoException() {
        Usuario usuario = novoUsuario();
        Banco banco = novoBanco();

        assertThrows(ValidacaoException.class, () -> new Conta("corrente", 1000.0, "123456", "BRL", banco, usuario, -1.0));
    }

    @Test
    void criarContaComLimiteCreditoZeroNaoLancaExcecao() {
        Usuario usuario = novoUsuario();
        Banco banco = novoBanco();

        assertDoesNotThrow(() -> new Conta("corrente", 1000.0, "123456", "BRL", banco, usuario, 0.0));
    }

    // --- limiteCreditoUtilizado ---

    @Test
    void novaContaIniciaComLimiteCreditoUtilizadoZero() {
        Conta conta = new Conta();

        assertEquals(0.0, conta.getLimiteCreditoUtilizado());
    }

    @Test
    void setLimiteCreditoUtilizadoComValorNegativoLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> conta.setLimiteCreditoUtilizado(-1.0));
    }

    @Test
    void setLimiteCreditoUtilizadoComValorValidoAtualiza() {
        Conta conta = new Conta();

        conta.setLimiteCreditoUtilizado(100.0);

        assertEquals(100.0, conta.getLimiteCreditoUtilizado());
    }

    // --- conta duplicada ---

    @Test
    void criarContaComMesmoUsuarioBancoETipoDeOutraContaLancaRegraNegocioException() {
        Usuario usuario = novoUsuario();
        Banco banco = novoBanco();
        new Conta("corrente", 1000.0, "123456", "BRL", banco, usuario, 0.0);

        assertThrows(RegraNegocioException.class, () -> new Conta("corrente", 2000.0, "654321", "BRL", banco, usuario, 0.0));
    }

    @Test
    void criarContaComMesmoUsuarioBancoETipoComCaseDiferenteLancaRegraNegocioException() {
        Usuario usuario = novoUsuario();
        Banco banco = novoBanco();
        new Conta("corrente", 1000.0, "123456", "BRL", banco, usuario, 0.0);

        assertThrows(RegraNegocioException.class, () -> new Conta("Corrente", 2000.0, "654321", "BRL", banco, usuario, 0.0));
    }

    @Test
    void criarContaComMesmoUsuarioEBancoMasTipoDiferenteNaoLancaExcecao() {
        Usuario usuario = novoUsuario();
        Banco banco = novoBanco();
        new Conta("corrente", 1000.0, "123456", "BRL", banco, usuario, 0.0);

        assertDoesNotThrow(() -> new Conta("poupança", 2000.0, "654321", "BRL", banco, usuario, 0.0));
    }

    @Test
    void criarContaComMesmoUsuarioETipoMasBancoDiferenteNaoLancaExcecao() {
        Usuario usuario = novoUsuario();
        Banco bancoA = new Banco("Banco A", 100);
        Banco bancoB = new Banco("Banco B", 200);
        new Conta("corrente", 1000.0, "123456", "BRL", bancoA, usuario, 0.0);

        assertDoesNotThrow(() -> new Conta("corrente", 2000.0, "654321", "BRL", bancoB, usuario, 0.0));
    }

    @Test
    void criarContaComMesmoBancoETipoMasUsuarioDiferenteNaoLancaExcecao() {
        Usuario usuario1 = novoUsuario();
        Usuario usuario2 = new Usuario("Ciclano", "22222222222", "11888888888", "1991-02-02");
        Banco banco = novoBanco();
        new Conta("corrente", 1000.0, "123456", "BRL", banco, usuario1, 0.0);

        assertDoesNotThrow(() -> new Conta("corrente", 2000.0, "654321", "BRL", banco, usuario2, 0.0));
    }

    // --- setters ---

    @Test
    void setTipoComValorInvalidoLancaValidacaoExceptionENaoAlteraOTipo() {
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, null, 0.0);

        assertThrows(ValidacaoException.class, () -> conta.setTipo("investimento"));
        assertEquals("corrente", conta.getTipo());
    }

    @Test
    void setNumeroContaComValorInvalidoLancaValidacaoExceptionENaoAlteraONumero() {
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, null, 0.0);

        assertThrows(ValidacaoException.class, () -> conta.setNumeroConta("123"));
        assertEquals("123456", conta.getNumeroConta());
    }

    @Test
    void setValorContaComValorNegativoLancaValidacaoExceptionENaoAlteraOValor() {
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, null, 0.0);

        assertThrows(ValidacaoException.class, () -> conta.setValorConta(-1.0));
        assertEquals(1000.0, conta.getValorConta());
    }

    @Test
    void setLimiteCreditoComValorNegativoLancaValidacaoExceptionENaoAlteraOLimite() {
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, null, 500.0);

        assertThrows(ValidacaoException.class, () -> conta.setLimiteCredito(-1.0));
        assertEquals(500.0, conta.getLimiteCredito());
    }

    // --- pesquisarTransacaoPorId ---

    @Test
    void pesquisarTransacaoPorIdRetornaTransacaoExistente() {
        Conta conta = new Conta();
        Transacao transacao = new Credito("Salario", conta, "2026-06-10", 1000.0, "rendimento");

        assertSame(transacao, conta.pesquisarTransacaoPorId(transacao.getId()));
    }

    @Test
    void pesquisarTransacaoPorIdComIdInexistenteRetornaNull() {
        Conta conta = new Conta();

        assertNull(conta.pesquisarTransacaoPorId(9999));
    }

    // --- pesquisarTransacoesPorPeriodo ---

    @Test
    void pesquisarTransacoesPorPeriodoRetornaTransacoesNoIntervalo() {
        Conta conta = new Conta();
        Transacao dentro = new Credito("Salario", conta, "2026-06-10", 1000.0, "rendimento");
        new Credito("Outro", conta, "2026-07-01", 500.0, "rendimento");

        Transacao[] resultado = conta.pesquisarTransacoesPorPeriodo("2026-06-01", "2026-06-30");

        assertEquals(1, resultado.length);
        assertSame(dentro, resultado[0]);
    }

    @Test
    void pesquisarTransacoesPorPeriodoComLimitesInclusivosIncluiDatasNasExtremidades() {
        Conta conta = new Conta();
        new Credito("Inicio", conta, "2026-06-01", 1000.0, "rendimento");
        new Credito("Fim", conta, "2026-06-30", 500.0, "rendimento");

        Transacao[] resultado = conta.pesquisarTransacoesPorPeriodo("2026-06-01", "2026-06-30");

        assertEquals(2, resultado.length);
    }

    @Test
    void pesquisarTransacoesPorPeriodoSemTransacoesNoIntervaloRetornaArrayVazio() {
        Conta conta = new Conta();
        new Credito("Salario", conta, "2026-08-10", 1000.0, "rendimento");

        Transacao[] resultado = conta.pesquisarTransacoesPorPeriodo("2026-06-01", "2026-06-30");

        assertEquals(0, resultado.length);
    }

    @Test
    void pesquisarTransacoesPorPeriodoComDataInicioAposDataFimLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> conta.pesquisarTransacoesPorPeriodo("2026-06-30", "2026-06-01"));
    }

    @Test
    void pesquisarTransacoesPorPeriodoComFormatoDeDataInvalidoLancaValidacaoException() {
        Conta conta = new Conta();

        assertThrows(ValidacaoException.class, () -> conta.pesquisarTransacoesPorPeriodo("10-06-2026", "2026-06-30"));
    }

    // --- removerTransacao ---

    @Test
    void removerTransacaoExistenteRemoveDaConta() {
        Conta conta = new Conta();
        Transacao transacao = new Credito("Salario", conta, "2026-06-10", 1000.0, "rendimento");

        assertTrue(conta.removerTransacao(transacao.getId()));
        assertEquals(0, conta.getTransacoes().length);
    }

    @Test
    void removerTransacaoComIdInexistenteRetornaFalse() {
        Conta conta = new Conta();

        assertFalse(conta.removerTransacao(9999));
    }

    @Test
    void removerCreditoRevertaEfeitoEmValorConta() {
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, null, 0.0);
        Transacao credito = new Credito("Salario", conta, "2026-06-10", 500.0, "rendimento");

        conta.removerTransacao(credito.getId());

        assertEquals(1000.0, conta.getValorConta());
    }

    @Test
    void removerDebitoAvistaRevertaEfeitoEmValorConta() {
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, null, 0.0);
        Transacao debito = new Debito("Mercado", conta, "2026-06-10", 200.0, "mercado", "avista");

        conta.removerTransacao(debito.getId());

        assertEquals(1000.0, conta.getValorConta());
    }

    @Test
    void removerDebitoTipoCreditoRevertaEfeitoEmLimiteCreditoUtilizado() {
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, null, 1000.0);
        Transacao debito = new Debito("Compra", conta, "2026-06-10", 200.0, "lazer", "credito");

        conta.removerTransacao(debito.getId());

        assertEquals(0.0, conta.getLimiteCreditoUtilizado());
    }

    // --- efeitos de Investimento sobre valorConta ---

    @Test
    void criarInvestimentoCompraSubtraiValorVezesQuantidadeDeValorConta() {
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, null, 0.0);

        new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-01", 0.0, "compra");

        assertEquals(7000.0, conta.getValorConta());
    }

    @Test
    void criarInvestimentoVendaComQuantidadeMaiorQueCompradaLancaRegraNegocioException() {
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, null, 0.0);
        new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-01", 0.0, "compra");

        assertThrows(RegraNegocioException.class, () -> new Acao("PETR4", 50.0, conta, 200.0, 0.0, "2026-06-10", 0.0, "venda"));
    }

    @Test
    void criarInvestimentoVendaComQuantidadeIgualACompradaNaoLancaExcecao() {
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, null, 0.0);
        new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-01", 0.0, "compra");

        assertDoesNotThrow(() -> new Acao("PETR4", 50.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "venda"));
    }

    @Test
    void criarInvestimentoVendaCalculaImpostoEValorRealizadoComBaseNoCustoMedio() {
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, null, 0.0);
        new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-01", 0.0, "compra");
        new Acao("PETR4", 40.0, conta, 100.0, 0.0, "2026-06-02", 0.0, "compra");

        // custo medio = (100*30 + 100*40) / 200 = 35
        // imposto = (50 - 35) * 100 * 0.15 = 225
        // valorRealizado = 50*100 - 225 = 4775
        Investimento venda = new Acao("PETR4", 50.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "venda");

        assertEquals(225.0, venda.getImposto());
        assertEquals(4775.0, venda.getValorRealizado());
    }

    @Test
    void criarInvestimentoVendaSomaValorRealizadoAValorConta() {
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, null, 0.0);
        new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-01", 0.0, "compra");
        new Acao("PETR4", 40.0, conta, 100.0, 0.0, "2026-06-02", 0.0, "compra");

        new Acao("PETR4", 50.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "venda");

        // valorConta = 10000 - 3000 - 4000 + 4775 = 7775
        assertEquals(7775.0, conta.getValorConta());
    }

    @Test
    void criarInvestimentoIsentoNaoGeraImpostoNaVendaComLucro() {
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, null, 0.0);
        new LCA("CDB Banco X", 100.0, conta, 10.0, 0.0, "2026-06-01", 0.0, "compra");

        Investimento venda = new LCA("CDB Banco X", 150.0, conta, 10.0, 0.0, "2026-06-10", 0.0, "venda");

        assertEquals(0.0, venda.getImposto());
        assertEquals(1500.0, venda.getValorRealizado());
    }

    // --- pesquisarInvestimentoPorId ---

    @Test
    void pesquisarInvestimentoPorIdRetornaInvestimentoExistente() {
        Conta conta = new Conta();
        Investimento investimento = new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-01", 0.0, "compra");

        assertSame(investimento, conta.pesquisarInvestimentoPorId(investimento.getId()));
    }

    @Test
    void pesquisarInvestimentoPorIdComIdInexistenteRetornaNull() {
        Conta conta = new Conta();

        assertNull(conta.pesquisarInvestimentoPorId(9999));
    }

    // --- removerInvestimento ---

    @Test
    void removerInvestimentoExistenteRemoveDaConta() {
        Conta conta = new Conta();
        Investimento investimento = new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-01", 0.0, "compra");

        assertTrue(conta.removerInvestimento(investimento.getId()));
        assertEquals(0, conta.getInvestimentos().length);
    }

    @Test
    void removerInvestimentoComIdInexistenteRetornaFalse() {
        Conta conta = new Conta();

        assertFalse(conta.removerInvestimento(9999));
    }

    @Test
    void removerInvestimentoCompraRevertaEfeitoEmValorConta() {
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, null, 0.0);
        Investimento compra = new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-01", 0.0, "compra");

        conta.removerInvestimento(compra.getId());

        assertEquals(10000.0, conta.getValorConta());
    }

    @Test
    void removerInvestimentoVendaRevertaEfeitoEmValorConta() {
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, null, 0.0);
        new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-01", 0.0, "compra");
        Investimento venda = new Acao("PETR4", 50.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "venda");

        conta.removerInvestimento(venda.getId());

        // valorConta deve voltar ao estado anterior a venda (10000 - 3000 = 7000)
        assertEquals(7000.0, conta.getValorConta());
    }
}
