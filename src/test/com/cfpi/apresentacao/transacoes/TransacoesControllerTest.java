package com.cfpi.apresentacao.transacoes;

import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.transacao.Credito;
import com.cfpi.dominio.entidades.transacao.Debito;
import com.cfpi.dominio.entidades.transacao.Transacao;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransacoesControllerTest {

    private Conta criarContaComUsuario() {
        Usuario usuario = new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
        return new Conta("corrente", 1000.0, "1234567", "BRL", null, usuario, 500.0);
    }

    private TransacoesController criarController(Conta conta) {
        return new TransacoesController(conta.getUsuario(), new TransacoesViewModel());
    }

    @Test
    void criarDebitoValidoApareceNasTransacoesDaConta() {
        Conta conta = criarContaComUsuario();
        TransacoesController controller = criarController(conta);

        List<String> erros = controller.criarDebito(conta, "Mercado", "2026-06-01", "100.0", "mercado", "avista");

        assertTrue(erros.isEmpty());
        assertEquals(1, conta.getTransacoes().length);
        assertTrue(conta.getTransacoes()[0] instanceof Debito);
    }

    @Test
    void criarCreditoValidoApareceNasTransacoesDaConta() {
        Conta conta = criarContaComUsuario();
        TransacoesController controller = criarController(conta);

        List<String> erros = controller.criarCredito(conta, "Salário", "2026-06-01", "2000.0", "pagamento");

        assertTrue(erros.isEmpty());
        assertEquals(1, conta.getTransacoes().length);
        assertTrue(conta.getTransacoes()[0] instanceof Credito);
    }

    /**
     * (*) Vermelho esperado: {@code Debito.aplicarEfeito()} é um stub
     * (corpo vazio) — quando implementado, um débito "avista" deve
     * subtrair {@code valor} de {@code valorConta}.
     */
    @Test
    void criarDebitoTipoAvistaDiminuiValorDaConta() {
        Conta conta = criarContaComUsuario();
        TransacoesController controller = criarController(conta);

        controller.criarDebito(conta, "Mercado", "2026-06-01", "100.0", "mercado", "avista");

        assertEquals(900.0, conta.getValorConta(), 0.001);
    }

    /**
     * (*) Vermelho esperado: {@code Credito.aplicarEfeito()} é um stub
     * (corpo vazio) — quando implementado, um crédito deve somar
     * {@code valor} a {@code valorConta}.
     */
    @Test
    void criarCreditoAumentaValorDaConta() {
        Conta conta = criarContaComUsuario();
        TransacoesController controller = criarController(conta);

        controller.criarCredito(conta, "Salário", "2026-06-01", "2000.0", "pagamento");

        assertEquals(3000.0, conta.getValorConta(), 0.001);
    }

    @Test
    void criarDebitoComDescricaoVaziaRetornaErroENaoCriaTransacao() {
        Conta conta = criarContaComUsuario();
        TransacoesController controller = criarController(conta);

        List<String> erros = controller.criarDebito(conta, " ", "2026-06-01", "100.0", "mercado", "avista");

        assertFalse(erros.isEmpty());
        assertEquals(0, conta.getTransacoes().length);
    }

    @Test
    void criarDebitoComValorNaoPositivoRetornaErroENaoCriaTransacao() {
        Conta conta = criarContaComUsuario();
        TransacoesController controller = criarController(conta);

        List<String> erros = controller.criarDebito(conta, "Mercado", "2026-06-01", "-10.0", "mercado", "avista");

        assertFalse(erros.isEmpty());
        assertEquals(0, conta.getTransacoes().length);
    }

    @Test
    void criarDebitoComDataInvalidaRetornaErroENaoCriaTransacao() {
        Conta conta = criarContaComUsuario();
        TransacoesController controller = criarController(conta);

        List<String> erros = controller.criarDebito(conta, "Mercado", "01/06/2026", "100.0", "mercado", "avista");

        assertFalse(erros.isEmpty());
        assertEquals(0, conta.getTransacoes().length);
    }

    @Test
    void filtrarPorPeriodoRetornaArrayVazioQuandoDominioRetornaNulo() {
        Conta conta = criarContaComUsuario();
        TransacoesController controller = criarController(conta);

        Transacao[] resultado = controller.filtrarPorPeriodo(conta, "2026-01-01", "2026-12-31");

        assertNotNull(resultado);
        assertEquals(0, resultado.length);
    }

    /**
     * (*) Vermelho esperado: {@code Conta.pesquisarTransacoesPorPeriodo} é
     * um stub que sempre retorna {@code null} — quando implementado, deve
     * retornar apenas as transações cuja data está dentro do intervalo
     * informado.
     */
    @Test
    void filtrarPorPeriodoRetornaApenasTransacoesDentroDoIntervalo() {
        Conta conta = criarContaComUsuario();
        TransacoesController controller = criarController(conta);
        controller.criarDebito(conta, "Dentro do período", "2026-06-15", "10.0", "mercado", "avista");
        controller.criarDebito(conta, "Fora do período", "2025-01-01", "10.0", "mercado", "avista");

        Transacao[] resultado = controller.filtrarPorPeriodo(conta, "2026-01-01", "2026-12-31");

        assertEquals(1, resultado.length);
    }

    @Test
    void atualizarAlteraDescricaoDataValorECategoriaDaTransacao() {
        Conta conta = criarContaComUsuario();
        TransacoesController controller = criarController(conta);
        controller.criarDebito(conta, "Mercado", "2026-06-01", "100.0", "mercado", "avista");
        Transacao transacao = conta.getTransacoes()[0];

        List<String> erros = controller.atualizar(transacao, "Mercado atualizado", "2026-06-02", "150.0", "lazer");

        assertTrue(erros.isEmpty());
        assertEquals("Mercado atualizado", transacao.getDescricao());
        assertEquals("2026-06-02", transacao.getData());
        assertEquals(150.0, transacao.getValor(), 0.001);
        assertEquals("lazer", transacao.getCategoria());
    }

    @Test
    void atualizarComDataInvalidaRetornaErroENaoAlteraTransacao() {
        Conta conta = criarContaComUsuario();
        TransacoesController controller = criarController(conta);
        controller.criarDebito(conta, "Mercado", "2026-06-01", "100.0", "mercado", "avista");
        Transacao transacao = conta.getTransacoes()[0];

        List<String> erros = controller.atualizar(transacao, "Mercado atualizado", "01/06/2026", "150.0", "lazer");

        assertFalse(erros.isEmpty());
        assertEquals("Mercado", transacao.getDescricao());
        assertEquals("2026-06-01", transacao.getData());
        assertEquals(100.0, transacao.getValor(), 0.001);
        assertEquals("mercado", transacao.getCategoria());
    }

    @Test
    void atualizarComValorNaoNumericoRetornaErroENaoAlteraTransacao() {
        Conta conta = criarContaComUsuario();
        TransacoesController controller = criarController(conta);
        controller.criarDebito(conta, "Mercado", "2026-06-01", "100.0", "mercado", "avista");
        Transacao transacao = conta.getTransacoes()[0];

        List<String> erros = controller.atualizar(transacao, "Mercado atualizado", "2026-06-02", "abc", "lazer");

        assertFalse(erros.isEmpty());
        assertEquals(100.0, transacao.getValor(), 0.001);
    }

    @Test
    void removerComConfirmacaoNegadaNaoAlteraTransacoesDaConta() {
        Conta conta = criarContaComUsuario();
        TransacoesController controller = criarController(conta);
        controller.criarDebito(conta, "Mercado", "2026-06-01", "100.0", "mercado", "avista");
        Transacao transacao = conta.getTransacoes()[0];

        boolean removido = controller.remover(transacao, conta, () -> false);

        assertFalse(removido);
        assertEquals(1, conta.getTransacoes().length);
    }

    /**
     * (*) Vermelho esperado: {@code Conta.removerTransacao} é um stub que
     * sempre retorna {@code false} sem remover a transação nem chamar
     * {@code reverterEfeito()} — quando implementado, deve remover a
     * transação de {@code getTransacoes()} e reverter seu efeito sobre
     * {@code valorConta}.
     */
    @Test
    void removerComConfirmacaoRemoveTransacaoEReverteEfeitoNaConta() {
        Conta conta = criarContaComUsuario();
        TransacoesController controller = criarController(conta);
        controller.criarDebito(conta, "Mercado", "2026-06-01", "100.0", "mercado", "avista");
        Transacao transacao = conta.getTransacoes()[0];

        boolean removido = controller.remover(transacao, conta, () -> true);

        assertTrue(removido);
        assertEquals(0, conta.getTransacoes().length);
    }
}
