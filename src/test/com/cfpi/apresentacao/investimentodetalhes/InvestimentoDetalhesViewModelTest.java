package com.cfpi.apresentacao.investimentodetalhes;

import com.cfpi.dominio.entidades.investimento.Acao;
import com.cfpi.dominio.entidades.investimento.CDB;
import com.cfpi.dominio.entidades.investimento.Investimento;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class InvestimentoDetalhesViewModelTest {

    private final InvestimentoDetalhesViewModel viewModel = new InvestimentoDetalhesViewModel();

    @Test
    void operacoesDoAtivoFiltraPorNomeETipo() {
        Investimento compraAcao = new Acao("PETR4", 30.0, null, 100.0, 0.0, "2026-01-01", 0.0, "compra");
        Investimento vendaAcao = new Acao("PETR4", 35.0, null, 40.0, 0.0, "2026-02-01", 0.0, "venda");
        Investimento outroAtivo = new Acao("VALE3", 60.0, null, 10.0, 0.0, "2026-01-01", 0.0, "compra");
        Investimento outroTipo = new CDB("PETR4", 100.0, null, 5.0, 0.0, "2026-01-01", 0.0, "compra");
        Investimento[] todos = {compraAcao, vendaAcao, outroAtivo, outroTipo};

        List<Investimento> resultado = viewModel.operacoesDoAtivo(todos, " petr4 ", Acao.class);

        assertEquals(List.of(compraAcao, vendaAcao), resultado);
    }

    @Test
    void dataPrimeiraCompraRetornaMenorDataEntreCompras() {
        Investimento compra1 = new Acao("PETR4", 30.0, null, 100.0, 0.0, "2026-02-01", 0.0, "compra");
        Investimento compra2 = new Acao("PETR4", 32.0, null, 50.0, 0.0, "2026-01-15", 0.0, "compra");
        Investimento venda = new Acao("PETR4", 35.0, null, 20.0, 0.0, "2026-01-01", 0.0, "venda");
        List<Investimento> operacoes = List.of(compra1, compra2, venda);

        String resultado = viewModel.dataPrimeiraCompra(operacoes);

        assertEquals("2026-01-15", resultado);
    }

    @Test
    void dataPrimeiraCompraRetornaNuloSeNaoHouverCompras() {
        Investimento venda = new Acao("PETR4", 35.0, null, 20.0, 0.0, "2026-01-01", 0.0, "venda");
        List<Investimento> operacoes = List.of(venda);

        assertNull(viewModel.dataPrimeiraCompra(operacoes));
    }

    @Test
    void totalInvestidoSomaApenasCompras() {
        Investimento compra1 = new Acao("PETR4", 30.0, null, 100.0, 0.0, "2026-01-01", 0.0, "compra");
        Investimento compra2 = new Acao("PETR4", 32.0, null, 50.0, 0.0, "2026-01-15", 0.0, "compra");
        Investimento venda = new Acao("PETR4", 35.0, null, 20.0, 0.0, "2026-02-01", 0.0, "venda");
        List<Investimento> operacoes = List.of(compra1, compra2, venda);

        double resultado = viewModel.totalInvestido(operacoes);

        assertEquals(30.0 * 100.0 + 32.0 * 50.0, resultado, 0.001);
    }

    @Test
    void quantidadeAtualSubtraiVendasDeCompras() {
        Investimento compra1 = new Acao("PETR4", 30.0, null, 100.0, 0.0, "2026-01-01", 0.0, "compra");
        Investimento compra2 = new Acao("PETR4", 32.0, null, 50.0, 0.0, "2026-01-15", 0.0, "compra");
        Investimento venda = new Acao("PETR4", 35.0, null, 20.0, 0.0, "2026-02-01", 0.0, "venda");
        List<Investimento> operacoes = List.of(compra1, compra2, venda);

        double resultado = viewModel.quantidadeAtual(operacoes);

        assertEquals(130.0, resultado, 0.001);
    }
}
