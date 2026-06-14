package com.cfpi.apresentacao.transacoes;

import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.transacao.Credito;
import com.cfpi.dominio.entidades.transacao.Debito;
import com.cfpi.dominio.entidades.transacao.Transacao;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TransacoesViewModelTest {

    private final TransacoesViewModel viewModel = new TransacoesViewModel();

    @Test
    void ordenarPorDataDescOrdenaDaMaisRecenteParaAMaisAntiga() {
        Debito d1 = new Debito(1);
        d1.setData("2026-01-01");
        Debito d2 = new Debito(2);
        d2.setData("2026-06-15");
        Debito d3 = new Debito(3);
        d3.setData("2026-03-10");

        List<Transacao> ordenadas = viewModel.ordenarPorDataDesc(new Transacao[]{d1, d2, d3});

        assertEquals(List.of(d2, d3, d1), ordenadas);
    }

    @Test
    void filtrarPorTipoRetornaApenasInstanciasDoTipoInformado() {
        Debito debito = new Debito(1);
        Credito credito = new Credito(1);
        List<Transacao> transacoes = List.of(debito, credito);

        List<Transacao> apenasDebitos = viewModel.filtrarPorTipo(transacoes, Debito.class);
        List<Transacao> apenasCreditos = viewModel.filtrarPorTipo(transacoes, Credito.class);

        assertEquals(List.of(debito), apenasDebitos);
        assertEquals(List.of(credito), apenasCreditos);
    }

    @Test
    void filtrarPorCategoriaRetornaApenasTransacoesComCategoriaInformada() {
        Debito mercado = new Debito(1);
        mercado.setCategoria("mercado");
        Debito lazer = new Debito(2);
        lazer.setCategoria("lazer");
        List<Transacao> transacoes = List.of(mercado, lazer);

        List<Transacao> filtradas = viewModel.filtrarPorCategoria(transacoes, "Mercado");

        assertEquals(List.of(mercado), filtradas);
    }

    @Test
    void categoriasParaTipoRetornaSeisCategoriasParaDebito() {
        String[] categorias = viewModel.categoriasParaTipo(Debito.class);

        assertArrayEquals(new String[]{"lazer", "mercado", "saude", "indeterminado", "investimentos", "banco"}, categorias);
    }

    @Test
    void categoriasParaTipoRetornaDuasCategoriasParaCredito() {
        String[] categorias = viewModel.categoriasParaTipo(Credito.class);

        assertArrayEquals(new String[]{"pagamento", "rendimento"}, categorias);
    }

    @Test
    void categoriasComOpcaoTodasPrefixaCategoriasComOpcaoTodas() {
        String[] categorias = viewModel.categoriasComOpcaoTodas(Debito.class);

        assertArrayEquals(new String[]{"Todas", "lazer", "mercado", "saude", "indeterminado", "investimentos", "banco"}, categorias);
    }

    @Test
    void tiposDebitoParaContaRetornaApenasAVistaParaContaPoupanca() {
        Usuario usuario = new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
        Conta poupanca = new Conta("poupança", 1000.0, "111111", "BRL", null, usuario, 0.0);

        assertArrayEquals(new String[]{"avista"}, viewModel.tiposDebitoParaConta(poupanca));
    }

    @Test
    void tiposDebitoParaContaRetornaAVistaECreditoParaContaCorrenteOuNula() {
        Usuario usuario = new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
        Conta corrente = new Conta("corrente", 1000.0, "222222", "BRL", null, usuario, 2000.0);

        assertArrayEquals(new String[]{"avista", "credito"}, viewModel.tiposDebitoParaConta(corrente));
        assertArrayEquals(new String[]{"avista", "credito"}, viewModel.tiposDebitoParaConta(null));
    }
}
