package com.cfpi.apresentacao.investimentos;

import com.cfpi.apresentacao.comum.AvaliadorDeAtivosFake;
import com.cfpi.dominio.entidades.investimento.Acao;
import com.cfpi.dominio.entidades.investimento.CDB;
import com.cfpi.dominio.entidades.investimento.Investimento;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvestimentosViewModelTest {

    private final InvestimentosViewModel viewModel = new InvestimentosViewModel();

    @Test
    void agruparPorAtivoConsolidaCompraEVendaDoMesmoAtivo() {
        Investimento compra = new Acao("PETR4", 30.0, null, 100.0, 0.0, "2026-01-01", 0.0, "compra");
        Investimento venda = new Acao("PETR4", 35.0, null, 40.0, 0.0, "2026-02-01", 0.0, "venda");
        Investimento[] investimentos = {compra, venda};

        List<AtivoResumo> resultado = viewModel.agruparPorAtivo(investimentos, new AvaliadorDeAtivosFake(5000.0));

        assertEquals(1, resultado.size());
        AtivoResumo ativo = resultado.get(0);
        assertEquals("PETR4", ativo.getNomeAtivo());
        assertEquals(Acao.class, ativo.getTipo());
        assertEquals(60.0, ativo.getQuantidadeAtual(), 0.001);
        assertEquals(3000.0, ativo.getTotalInvestido(), 0.001);
        assertEquals(5000.0, ativo.getValorAtual(), 0.001);
        assertEquals(2000.0, ativo.getGanhoPerda(), 0.001);
    }

    @Test
    void agruparPorAtivoDistingueMesmoNomeEmSubtiposDiferentes() {
        Investimento acao = new Acao("PETR4", 30.0, null, 100.0, 0.0, "2026-01-01", 0.0, "compra");
        Investimento cdb = new CDB("PETR4", 100.0, null, 10.0, 0.0, "2026-01-01", 0.0, "compra");
        Investimento[] investimentos = {acao, cdb};

        List<AtivoResumo> resultado = viewModel.agruparPorAtivo(investimentos, new AvaliadorDeAtivosFake(0.0));

        assertEquals(2, resultado.size());
        assertEquals(Acao.class, resultado.get(0).getTipo());
        assertEquals(CDB.class, resultado.get(1).getTipo());
    }

    @Test
    void agruparPorAtivoTotalInvestidoSomaApenasCompras() {
        Investimento compra1 = new Acao("PETR4", 30.0, null, 100.0, 0.0, "2026-01-01", 0.0, "compra");
        Investimento compra2 = new Acao("PETR4", 32.0, null, 50.0, 0.0, "2026-01-15", 0.0, "compra");
        Investimento venda = new Acao("PETR4", 35.0, null, 20.0, 0.0, "2026-02-01", 0.0, "venda");
        Investimento[] investimentos = {compra1, compra2, venda};

        List<AtivoResumo> resultado = viewModel.agruparPorAtivo(investimentos, new AvaliadorDeAtivosFake(0.0));

        assertEquals(1, resultado.size());
        assertEquals(30.0 * 100.0 + 32.0 * 50.0, resultado.get(0).getTotalInvestido(), 0.001);
        assertEquals(130.0, resultado.get(0).getQuantidadeAtual(), 0.001);
    }

    @Test
    void filtrarPorNomeRetornaApenasAtivosCujoNomeContemOTermo() {
        AtivoResumo petr = new AtivoResumo("PETR4", Acao.class, 100.0, 3000.0, 3500.0, 500.0);
        AtivoResumo vale = new AtivoResumo("VALE3", Acao.class, 50.0, 2000.0, 2200.0, 200.0);
        List<AtivoResumo> ativos = List.of(petr, vale);

        List<AtivoResumo> resultado = viewModel.filtrarPorNome(ativos, "petr");

        assertEquals(List.of(petr), resultado);
    }

    @Test
    void filtrarPorTipoRetornaApenasAtivosDoTipoInformado() {
        AtivoResumo acao = new AtivoResumo("PETR4", Acao.class, 100.0, 3000.0, 3500.0, 500.0);
        AtivoResumo cdb = new AtivoResumo("CDB Banco X", CDB.class, 10.0, 1000.0, 1100.0, 100.0);
        List<AtivoResumo> ativos = List.of(acao, cdb);

        List<AtivoResumo> resultado = viewModel.filtrarPorTipo(ativos, CDB.class);

        assertEquals(List.of(cdb), resultado);
    }

    @Test
    void filtrarPorTipoComTipoNuloRetornaTodosOsAtivos() {
        AtivoResumo acao = new AtivoResumo("PETR4", Acao.class, 100.0, 3000.0, 3500.0, 500.0);
        AtivoResumo cdb = new AtivoResumo("CDB Banco X", CDB.class, 10.0, 1000.0, 1100.0, 100.0);
        List<AtivoResumo> ativos = List.of(acao, cdb);

        List<AtivoResumo> resultado = viewModel.filtrarPorTipo(ativos, null);

        assertEquals(ativos, resultado);
    }
}
