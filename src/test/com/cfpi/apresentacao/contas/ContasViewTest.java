package com.cfpi.apresentacao.contas;

import com.cfpi.apresentacao.comum.Formatadores;
import com.cfpi.dominio.entidades.banco.BancoStoreImpl;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.transacao.Debito;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import javax.swing.JPanel;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContasViewTest {

    private Usuario novoUsuario() {
        return new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
    }

    private ContaListItemPanel itemDaConta(ContasView view, Conta conta) {
        for (var componente : view.getListaPanel().getComponents()) {
            if (componente instanceof ContaListItemPanel item && item.getConta() == conta) {
                return item;
            }
        }
        return null;
    }

    @Test
    void atualizarReflateLimiteCreditoUtilizadoAposNovoDebitoNaConta() {
        Usuario usuario = novoUsuario();
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, usuario, 2000.0);
        ContasController controller = new ContasController(usuario, new BancoStoreImpl(usuario), new ContasViewModel());
        ContasView view = new ContasView(controller);

        ContaListItemPanel itemInicial = itemDaConta(view, conta);
        assertEquals(Formatadores.formatarMoeda(0.0) + " de " + Formatadores.formatarMoeda(2000.0),
                itemInicial.getLabelLimiteValor().getText());

        new Debito("Compra", conta, "2026-06-10", 500.0, "lazer", "credito");

        view.atualizar();

        ContaListItemPanel itemAtualizado = itemDaConta(view, conta);
        assertEquals(Formatadores.formatarMoeda(500.0) + " de " + Formatadores.formatarMoeda(2000.0),
                itemAtualizado.getLabelLimiteValor().getText());
    }
}
