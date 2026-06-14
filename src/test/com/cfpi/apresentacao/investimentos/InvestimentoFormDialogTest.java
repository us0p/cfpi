package com.cfpi.apresentacao.investimentos;

import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.investimento.Acao;
import com.cfpi.dominio.entidades.investimento.Investimento;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InvestimentoFormDialogTest {

    @Test
    void comboTipoListaOsDozeSubtipos() {
        InvestimentoFormDialog dialog = new InvestimentoFormDialog(null);

        assertEquals(12, dialog.getComboTipo().getItemCount());
        assertEquals(12, InvestimentoFormDialog.TIPOS.length);
    }

    @Test
    void setContasPopulaComboConta() {
        Usuario usuario = new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, usuario, 0.0);
        InvestimentoFormDialog dialog = new InvestimentoFormDialog(null);

        dialog.setContas(usuario.getContas());

        assertEquals(1, dialog.getComboConta().getItemCount());
        assertEquals(conta, dialog.getComboConta().getItemAt(0));
    }

    @Test
    void preencherParaEdicaoPopulaCamposComDadosDoInvestimento() {
        Usuario usuario = new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, usuario, 0.0);
        Investimento investimento = new Acao("PETR4", 30.0, conta, 100.0, 0.0, "2026-06-10", 0.0, "compra");
        InvestimentoFormDialog dialog = new InvestimentoFormDialog(null);
        dialog.setContas(usuario.getContas());

        dialog.preencherParaEdicao(investimento);

        assertEquals(Acao.class, dialog.getComboTipo().getSelectedItem());
        assertTrue(!dialog.getComboTipo().isEnabled());
        assertEquals(conta, dialog.getComboConta().getSelectedItem());
        assertTrue(!dialog.getComboConta().isEnabled());
        assertEquals("PETR4", dialog.getCampoNomeAtivo().getText());
        assertEquals("30.0", dialog.getCampoValor().getText());
        assertEquals("100.0", dialog.getCampoQuantidade().getText());
        assertEquals("2026-06-10", dialog.getCampoData().getText());
        assertEquals("compra", dialog.getComboOperacao().getSelectedItem());
    }
}
