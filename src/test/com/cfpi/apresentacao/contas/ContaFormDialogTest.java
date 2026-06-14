package com.cfpi.apresentacao.contas;

import com.cfpi.dominio.entidades.banco.Banco;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContaFormDialogTest {

    @Test
    void comboTipoContemPoupancaECorrente() {
        ContaFormDialog dialog = new ContaFormDialog(null);

        assertEquals(2, dialog.getComboTipo().getItemCount());
        assertTrue(java.util.List.of("corrente", "poupança").containsAll(
                java.util.List.of((String) dialog.getComboTipo().getItemAt(0), (String) dialog.getComboTipo().getItemAt(1))));
    }

    @Test
    void setBancosPopulaComboBanco() {
        ContaFormDialog dialog = new ContaFormDialog(null);
        Banco banco1 = new Banco("Banco do Brasil", 100);
        Banco banco2 = new Banco("Itaú", 341);

        dialog.setBancos(new Banco[]{banco1, banco2});

        assertEquals(2, dialog.getComboBanco().getItemCount());
        assertEquals(banco1, dialog.getComboBanco().getItemAt(0));
        assertEquals(banco2, dialog.getComboBanco().getItemAt(1));
    }

    @Test
    void preencherParaEdicaoPopulaCamposComDadosDaConta() {
        Usuario usuario = new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
        Banco banco = new Banco("Banco do Brasil", 100);
        Conta conta = new Conta("corrente", 1500.0, "123456", "BRL", banco, usuario, 200.0);
        ContaFormDialog dialog = new ContaFormDialog(null);
        dialog.setBancos(new Banco[]{banco});

        dialog.preencherParaEdicao(conta);

        assertEquals("corrente", dialog.getComboTipo().getSelectedItem());
        assertEquals("123456", dialog.getCampoNumeroConta().getText());
        assertEquals("1500.0", dialog.getCampoValorConta().getText());
        assertEquals("BRL", dialog.getCampoMoeda().getText());
        assertEquals("200.0", dialog.getCampoLimiteCredito().getText());
        assertEquals(banco, dialog.getComboBanco().getSelectedItem());
    }

    @Test
    void contaCorrenteMostraCampoLimiteCredito() {
        ContaFormDialog dialog = new ContaFormDialog(null);

        dialog.getComboTipo().setSelectedItem("corrente");

        assertTrue(dialog.isCampoLimiteCreditoVisivel());
    }

    @Test
    void contaPoupancaEscondeCampoLimiteCreditoENaoExige(){
        ContaFormDialog dialog = new ContaFormDialog(null);

        dialog.getComboTipo().setSelectedItem("poupança");

        assertFalse(dialog.isCampoLimiteCreditoVisivel());
        assertEquals("0", dialog.getLimiteCreditoTexto());
    }

    @Test
    void voltarParaCorrenteRestauraCampoLimiteCredito() {
        ContaFormDialog dialog = new ContaFormDialog(null);

        dialog.getComboTipo().setSelectedItem("poupança");
        dialog.getComboTipo().setSelectedItem("corrente");

        assertTrue(dialog.isCampoLimiteCreditoVisivel());
    }
}
