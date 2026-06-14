package com.cfpi.apresentacao.contas;

import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContasViewModelTest {

    private final ContasViewModel viewModel = new ContasViewModel();

    private Usuario criarUsuario() {
        return new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
    }

    @Test
    void filtrarPorNumeroRetornaApenasContasCujoNumeroContemOTermo() {
        Usuario usuario = criarUsuario();
        Conta corrente = new Conta("corrente", 1000.0, "123456", "BRL", null, usuario, 0.0);
        Conta poupanca = new Conta("poupança", 2000.0, "654321", "BRL", null, usuario, 0.0);
        Conta[] contas = {corrente, poupanca};

        List<Conta> resultado = viewModel.filtrarPorNumero(contas, "1234");

        assertEquals(List.of(corrente), resultado);
    }

    @Test
    void filtrarPorNumeroComTermoEmBrancoRetornaTodasAsContas() {
        Usuario usuario = criarUsuario();
        Conta corrente = new Conta("corrente", 1000.0, "123456", "BRL", null, usuario, 0.0);
        Conta poupanca = new Conta("poupança", 2000.0, "654321", "BRL", null, usuario, 0.0);
        Conta[] contas = {corrente, poupanca};

        List<Conta> resultado = viewModel.filtrarPorNumero(contas, " ");

        assertEquals(List.of(corrente, poupanca), resultado);
    }

    @Test
    void exibeLimiteCreditoRetornaTrueApenasParaContaCorrente() {
        assertTrue(viewModel.exibeLimiteCredito("corrente"));
        assertFalse(viewModel.exibeLimiteCredito("poupança"));
    }
}
