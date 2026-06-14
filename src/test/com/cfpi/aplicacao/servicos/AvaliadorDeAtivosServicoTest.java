package com.cfpi.aplicacao.servicos;

import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.investimento.Acao;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AvaliadorDeAtivosServicoTest {

    private Usuario novoUsuario() {
        return new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
    }

    @Test
    void retornaZeroQuandoNaoHaUsuarioAtual() {
        AvaliadorDeAtivosServico servico = new AvaliadorDeAtivosServico(() -> null);

        assertEquals(0.0, servico.valorAtual("PETR4", Acao.class, 100.0));
    }

    @Test
    void retornaZeroQuandoQuantidadeAtualNaoEPositiva() {
        Usuario usuario = novoUsuario();
        AvaliadorDeAtivosServico servico = new AvaliadorDeAtivosServico(() -> usuario);

        assertEquals(0.0, servico.valorAtual("PETR4", Acao.class, 0.0));
    }

    @Test
    void retornaZeroQuandoNaoHaCompraDoAtivo() {
        Usuario usuario = novoUsuario();
        new Conta("corrente", 0.0, "111111", "BRL", null, usuario, 0.0);
        AvaliadorDeAtivosServico servico = new AvaliadorDeAtivosServico(() -> usuario);

        assertEquals(0.0, servico.valorAtual("PETR4", Acao.class, 100.0));
    }

    @Test
    void calculaValorAtualPeloCustoMedioPonderadoDeCompra() {
        Usuario usuario = novoUsuario();
        Conta conta = new Conta("corrente", 100000.0, "111111", "BRL", null, usuario, 0.0);
        new Acao("PETR4", 10.0, conta, 100.0, 0.0, "2026-01-05", 0.0, "compra");
        new Acao("PETR4", 20.0, conta, 100.0, 0.0, "2026-02-05", 0.0, "compra");
        AvaliadorDeAtivosServico servico = new AvaliadorDeAtivosServico(() -> usuario);

        // custoMedioCompra = (10*100 + 20*100) / (100+100) = 15
        assertEquals(15.0 * 50, servico.valorAtual("petr4", Acao.class, 50.0), 0.001);
    }
}
