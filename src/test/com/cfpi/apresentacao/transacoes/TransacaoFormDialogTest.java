package com.cfpi.apresentacao.transacoes;

import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransacaoFormDialogTest {

    private Usuario novoUsuario() {
        return new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
    }

    @Test
    void estadoInicialMostraCategoriasDeDebitoETipoDeDebitoVisivel() {
        TransacaoFormDialog dialog = new TransacaoFormDialog(null, new TransacoesViewModel());

        assertEquals(TransacaoFormDialog.TIPO_DEBITO, dialog.getComboTipoTransacao().getSelectedItem());
        assertEquals(6, dialog.getComboCategoria().getItemCount());
        assertEquals("lazer", dialog.getComboCategoria().getItemAt(0));
        assertTrue(dialog.getComboTipoDebito().isVisible());
    }

    @Test
    void selecionarCreditoAtualizaCategoriasEEscondeTipoDeDebito() {
        TransacaoFormDialog dialog = new TransacaoFormDialog(null, new TransacoesViewModel());

        dialog.getComboTipoTransacao().setSelectedItem(TransacaoFormDialog.TIPO_CREDITO);

        assertEquals(2, dialog.getComboCategoria().getItemCount());
        assertEquals("pagamento", dialog.getComboCategoria().getItemAt(0));
        assertFalse(dialog.getComboTipoDebito().isVisible());
    }

    @Test
    void voltarParaDebitoRestauraCategoriasDeDebitoEMostraTipoDeDebito() {
        TransacaoFormDialog dialog = new TransacaoFormDialog(null, new TransacoesViewModel());

        dialog.getComboTipoTransacao().setSelectedItem(TransacaoFormDialog.TIPO_CREDITO);
        dialog.getComboTipoTransacao().setSelectedItem(TransacaoFormDialog.TIPO_DEBITO);

        assertEquals(6, dialog.getComboCategoria().getItemCount());
        assertTrue(dialog.getComboTipoDebito().isVisible());
    }

    @Test
    void contaPoupancaNaoOferereOpcaoCreditoNoTipoDeDebito() {
        Usuario usuario = novoUsuario();
        Conta poupanca = new Conta("poupança", 1000.0, "111111", "BRL", null, usuario, 0.0);

        TransacaoFormDialog dialog = new TransacaoFormDialog(null, new TransacoesViewModel());
        dialog.setContas(usuario.getContas());

        assertEquals(1, dialog.getComboTipoDebito().getItemCount());
        assertEquals("avista", dialog.getComboTipoDebito().getItemAt(0));
    }

    @Test
    void contaCorrenteOfereceOpcaoCreditoNoTipoDeDebito() {
        Usuario usuario = novoUsuario();
        Conta corrente = new Conta("corrente", 1000.0, "222222", "BRL", null, usuario, 2000.0);

        TransacaoFormDialog dialog = new TransacaoFormDialog(null, new TransacoesViewModel());
        dialog.setContas(usuario.getContas());

        assertEquals(2, dialog.getComboTipoDebito().getItemCount());
        assertEquals("avista", dialog.getComboTipoDebito().getItemAt(0));
        assertEquals("credito", dialog.getComboTipoDebito().getItemAt(1));
    }

    @Test
    void trocarParaContaPoupancaRemoveOpcaoCreditoERedefineSelecao() {
        Usuario usuario = novoUsuario();
        Conta corrente = new Conta("corrente", 1000.0, "333333", "BRL", null, usuario, 2000.0);
        Conta poupanca = new Conta("poupança", 1000.0, "444444", "BRL", null, usuario, 0.0);

        TransacaoFormDialog dialog = new TransacaoFormDialog(null, new TransacoesViewModel());
        dialog.setContas(usuario.getContas());
        dialog.getComboConta().setSelectedItem(corrente);
        dialog.getComboTipoDebito().setSelectedItem("credito");

        dialog.getComboConta().setSelectedItem(poupanca);

        assertEquals(1, dialog.getComboTipoDebito().getItemCount());
        assertEquals("avista", dialog.getComboTipoDebito().getItemAt(0));
        assertEquals("avista", dialog.getComboTipoDebito().getSelectedItem());
    }
}
