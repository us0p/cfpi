package com.cfpi.apresentacao.investimentos;

import com.cfpi.apresentacao.comum.AvaliadorDeAtivosFake;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.investimento.Acao;
import com.cfpi.dominio.entidades.investimento.Investimento;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvestimentosViewTest {

    private Usuario criarUsuario() {
        return new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
    }

    @Test
    void atualizarRemoveAtivoDaListaAposRemocaoDaUltimaOperacao() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 10000.0, "123456", "BRL", null, usuario, 0.0);
        InvestimentosController controller = new InvestimentosController(usuario, new AvaliadorDeAtivosFake(0.0), new InvestimentosViewModel());
        controller.criar(Acao.class, "PETR4", "30.0", conta, "100.0", "2026-06-10", "compra");

        InvestimentosView view = new InvestimentosView(controller, new InvestimentosViewModel(), ativo -> {});
        assertEquals(2, view.getListaPanel().getComponentCount());

        Investimento operacao = conta.getInvestimentos()[0];
        conta.removerInvestimento(operacao.getId());

        view.atualizar();

        assertEquals(0, view.getListaPanel().getComponentCount());
    }
}
