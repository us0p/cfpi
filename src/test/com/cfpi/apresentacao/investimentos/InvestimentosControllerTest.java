package com.cfpi.apresentacao.investimentos;

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

class InvestimentosControllerTest {

    private Usuario criarUsuario() {
        return new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
    }

    private InvestimentosController criarController(Usuario usuario) {
        return new InvestimentosController(usuario, new AvaliadorDeAtivosFake(0.0), new InvestimentosViewModel());
    }

    @Test
    void criarComEntradaValidaAdicionaInvestimentoNaContaDoUsuario() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, usuario, 0.0);
        InvestimentosController controller = criarController(usuario);

        List<String> erros = controller.criar(Acao.class, "PETR4", "30.0", conta, "100.0", "2026-06-10", "compra");

        assertTrue(erros.isEmpty());
        assertEquals(1, conta.getInvestimentos().length);
        assertTrue(conta.getInvestimentos()[0] instanceof Acao);
        assertEquals("PETR4", conta.getInvestimentos()[0].getNomeAtivo());
    }

    /**
     * (*) Vermelho esperado: o efeito de uma operação de "compra" sobre
     * {@code conta.valorConta} (documentado em
     * {@code Investimento#aplicarEfeito()}) é um stub que não altera o
     * saldo. Quando implementado, criar uma compra de
     * {@code valor*quantidade = 3000.0} deve reduzir
     * {@code conta.getValorConta()} em {@code 3000.0}.
     */
    @Test
    void criarCompraReduzSaldoDaContaPeloValorTotalDaOperacao() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, usuario, 0.0);
        InvestimentosController controller = criarController(usuario);

        controller.criar(Acao.class, "PETR4", "30.0", conta, "100.0", "2026-06-10", "compra");

        assertEquals(10000.0 - 3000.0, conta.getValorConta(), 0.001);
    }

    /**
     * (*) Vermelho esperado: a regra de negócio documentada no construtor de
     * {@code Investimento} — rejeitar uma "venda" cuja quantidade exceda a
     * soma das quantidades já compradas do mesmo ativo na conta, lançando
     * {@code RegraNegocioException} — é um stub.
     */
    @Test
    void criarVendaComQuantidadeMaiorQueCompradaRetornaErro() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, usuario, 0.0);
        InvestimentosController controller = criarController(usuario);
        controller.criar(Acao.class, "PETR4", "30.0", conta, "100.0", "2026-06-10", "compra");

        List<String> erros = controller.criar(Acao.class, "PETR4", "35.0", conta, "200.0", "2026-06-15", "venda");

        assertFalse(erros.isEmpty());
    }

    @Test
    void criarCompraComSaldoInsuficienteRetornaErroENaoAlteraConta() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, usuario, 0.0);
        InvestimentosController controller = criarController(usuario);

        List<String> erros = controller.criar(Acao.class, "PETR4", "30.0", conta, "100.0", "2026-06-10", "compra");

        assertFalse(erros.isEmpty());
        assertEquals(0, conta.getInvestimentos().length);
        assertEquals(1000.0, conta.getValorConta(), 0.001);
    }

    @Test
    void criarComValorNaoPositivoRetornaErroENaoAdiciona() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, usuario, 0.0);
        InvestimentosController controller = criarController(usuario);

        List<String> erros = controller.criar(Acao.class, "PETR4", "-30.0", conta, "100.0", "2026-06-10", "compra");

        assertFalse(erros.isEmpty());
        assertEquals(0, conta.getInvestimentos().length);
    }

    @Test
    void criarParaCadaUmDosDozeSubtiposDeInvestimentoCriaAInstanciaCorreta() {
        for (Class<? extends Investimento> tipo : InvestimentoFormDialog.TIPOS) {
            Usuario usuario = criarUsuario();
            Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, usuario, 0.0);
            InvestimentosController controller = criarController(usuario);

            List<String> erros = controller.criar(tipo, "ATIVO", "10.0", conta, "1.0", "2026-06-10", "compra");

            assertTrue(erros.isEmpty(), "tipo " + tipo.getSimpleName() + " retornou erros: " + erros);
            assertEquals(1, conta.getInvestimentos().length);
            assertTrue(tipo.isInstance(conta.getInvestimentos()[0]), "esperado instância de " + tipo.getSimpleName());
        }
    }

    @Test
    void carregarAgrupaInvestimentosDeTodasAsContasDoUsuario() {
        Usuario usuario = criarUsuario();
        Conta contaCorrente = new Conta("corrente", 10000.0, "123456", "BRL", null, usuario, 0.0);
        Conta contaPoupanca = new Conta("poupança", 5000.0, "654321", "BRL", null, usuario, 0.0);
        InvestimentosController controller = criarController(usuario);
        controller.criar(Acao.class, "PETR4", "30.0", contaCorrente, "100.0", "2026-06-10", "compra");
        controller.criar(Acao.class, "VALE3", "60.0", contaPoupanca, "10.0", "2026-06-11", "compra");

        List<AtivoResumo> resultado = controller.carregar();

        assertEquals(2, resultado.size());
    }

    @Test
    void filtrarPorNomeRetornaApenasAtivosCorrespondentes() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, usuario, 0.0);
        InvestimentosController controller = criarController(usuario);
        controller.criar(Acao.class, "PETR4", "30.0", conta, "100.0", "2026-06-10", "compra");
        controller.criar(Acao.class, "VALE3", "60.0", conta, "10.0", "2026-06-11", "compra");

        List<AtivoResumo> resultado = controller.filtrarPorNome("petr");

        assertEquals(1, resultado.size());
        assertEquals("PETR4", resultado.get(0).getNomeAtivo());
    }
}
