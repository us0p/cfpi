package com.cfpi.apresentacao.investimentodetalhes;

import com.cfpi.apresentacao.comum.AvaliadorDeAtivosFake;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.investimento.Acao;
import com.cfpi.dominio.entidades.investimento.Investimento;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InvestimentoDetalhesControllerTest {

    private Usuario criarUsuario() {
        return new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
    }

    private InvestimentoDetalhesController criarController(Usuario usuario) {
        return new InvestimentoDetalhesController(usuario, new AvaliadorDeAtivosFake(5000.0), new InvestimentoDetalhesViewModel());
    }

    @Test
    void carregarRetornaOperacoesDoAtivoDeTodasAsContasDoUsuario() {
        Usuario usuario = criarUsuario();
        Conta contaCorrente = new Conta("corrente", 10000.0, "123456", "BRL", null, usuario, 0.0);
        Conta contaPoupanca = new Conta("poupança", 5000.0, "654321", "BRL", null, usuario, 0.0);
        new Acao("PETR4", 30.0, contaCorrente, 100.0, 0.0, "2026-01-01", 0.0, "compra");
        new Acao("PETR4", 32.0, contaPoupanca, 50.0, 0.0, "2026-01-15", 0.0, "compra");
        new Acao("VALE3", 60.0, contaCorrente, 10.0, 0.0, "2026-01-01", 0.0, "compra");
        InvestimentoDetalhesController controller = criarController(usuario);

        List<Investimento> resultado = controller.carregar("PETR4", Acao.class);

        assertEquals(2, resultado.size());
    }

    @Test
    void atualizarComEntradaValidaAlteraDadosDaOperacao() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, usuario, 0.0);
        Investimento investimento = new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-01-01", 0.0, "compra");
        InvestimentoDetalhesController controller = criarController(usuario);

        List<String> erros = controller.atualizar(investimento, "PETR4", "35.0", "120.0", "2026-02-01", "venda");

        assertTrue(erros.isEmpty());
        assertEquals(35.0, investimento.getValor(), 0.001);
        assertEquals(120.0, investimento.getQuantidade(), 0.001);
        assertEquals("2026-02-01", investimento.getData());
        assertEquals("venda", investimento.getOperacao());
    }

    @Test
    void atualizarComValorNaoPositivoRetornaErroENaoAltera() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, usuario, 0.0);
        Investimento investimento = new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-01-01", 0.0, "compra");
        InvestimentoDetalhesController controller = criarController(usuario);

        List<String> erros = controller.atualizar(investimento, "PETR4", "-35.0", "120.0", "2026-02-01", "venda");

        assertFalse(erros.isEmpty());
        assertEquals(30.0, investimento.getValor(), 0.001);
    }

    /**
     * (*) Vermelho esperado: a remoção de um investimento (documentada em
     * {@code Conta#removerInvestimento(int)}) — localizar o investimento pelo
     * id, chamar {@code reverterEfeito()} e remover do {@code ArrayDinamico}
     * interno — é um stub que sempre retorna {@code false} e não altera
     * {@code conta.getInvestimentos()}.
     */
    @Test
    void removerComConfirmacaoRemoveDaContaEReverteEfeito() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, usuario, 0.0);
        Investimento investimento = new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-01-01", 0.0, "compra");
        InvestimentoDetalhesController controller = criarController(usuario);

        boolean removido = controller.remover(investimento, () -> true);

        assertTrue(removido);
        assertEquals(0, conta.getInvestimentos().length);
    }

    @Test
    void removerComConfirmacaoNegadaNaoAlteraInvestimentos() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, usuario, 0.0);
        Investimento investimento = new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-01-01", 0.0, "compra");
        InvestimentoDetalhesController controller = criarController(usuario);

        boolean removido = controller.remover(investimento, () -> false);

        assertFalse(removido);
        assertEquals(1, conta.getInvestimentos().length);
    }

    @Test
    void getValorAtualDelegaParaAvaliador() {
        Usuario usuario = criarUsuario();
        InvestimentoDetalhesController controller = criarController(usuario);

        double resultado = controller.getValorAtual("PETR4", Acao.class, 100.0);

        assertEquals(5000.0, resultado, 0.001);
    }

    /**
     * (*) Vermelho esperado: {@code Conta#pesquisarInvestimentoPorId(int)} —
     * percorrer {@code investimentos.getArr()} e retornar o investimento cujo
     * {@code getId()} seja igual ao informado — é um stub que sempre retorna
     * {@code null}.
     */
    @Test
    void buscarPorIdUsaPesquisarInvestimentoPorId() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, usuario, 0.0);
        Investimento investimento = new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-01-01", 0.0, "compra");
        InvestimentoDetalhesController controller = criarController(usuario);

        Investimento resultado = controller.buscarPorId(conta, investimento.getId());

        assertEquals(investimento, resultado);
    }
}
