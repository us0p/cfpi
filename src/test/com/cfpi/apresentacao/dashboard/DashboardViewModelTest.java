package com.cfpi.apresentacao.dashboard;

import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.objetivo.Objetivo;
import com.cfpi.dominio.entidades.transacao.Credito;
import com.cfpi.dominio.entidades.transacao.Debito;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DashboardViewModelTest {

    private final DashboardViewModel viewModel = new DashboardViewModel();

    private Usuario criarUsuario() {
        return new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
    }

    @Test
    void saldoTotalSomaValorDeTodasAsContas() {
        Usuario usuario = criarUsuario();
        Conta contaA = new Conta("corrente", 1000.0, "111111", "BRL", null, usuario, 0.0);
        Conta contaB = new Conta("poupança", 500.0, "222222", "BRL", null, usuario, 0.0);

        double resultado = viewModel.saldoTotal(new Conta[] { contaA, contaB });

        assertEquals(1500.0, resultado, 0.001);
    }

    @Test
    void percentualLimiteConsumidoDivideUtilizadoPorLimiteTotal() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 1000.0, "111111", "BRL", null, usuario, 1000.0);
        conta.setLimiteCreditoUtilizado(250.0);

        double resultado = viewModel.percentualLimiteConsumido(new Conta[] { conta });

        assertEquals(0.25, resultado, 0.001);
    }

    @Test
    void percentualLimiteConsumidoRetornaZeroQuandoLimiteTotalEhZero() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 1000.0, "111111", "BRL", null, usuario, 0.0);

        double resultado = viewModel.percentualLimiteConsumido(new Conta[] { conta });

        assertEquals(0.0, resultado, 0.001);
    }

    @Test
    void gastosPorCategoriaSomaApenasDebitosAgrupadosPorCategoria() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 1000.0, "111111", "BRL", null, usuario, 0.0);
        new Debito("Mercado", conta, "2026-06-01", 100.0, "mercado", "avista");
        new Debito("Cinema", conta, "2026-06-02", 50.0, "lazer", "avista");
        new Debito("Padaria", conta, "2026-06-03", 30.0, "mercado", "avista");
        new Credito("Salário", conta, "2026-06-01", 2000.0, "pagamento");

        Map<String, Double> resultado = viewModel.gastosPorCategoria(new Conta[] { conta }, "todas");

        assertEquals(130.0, resultado.get("mercado"), 0.001);
        assertEquals(50.0, resultado.get("lazer"), 0.001);
        assertEquals(2, resultado.size());
    }

    @Test
    void gastosPorCategoriaComFiltroConsideraApenasACategoriaInformada() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 1000.0, "111111", "BRL", null, usuario, 0.0);
        new Debito("Mercado", conta, "2026-06-01", 100.0, "mercado", "avista");
        new Debito("Cinema", conta, "2026-06-02", 50.0, "lazer", "avista");

        Map<String, Double> resultado = viewModel.gastosPorCategoria(new Conta[] { conta }, "lazer");

        assertEquals(1, resultado.size());
        assertEquals(50.0, resultado.get("lazer"), 0.001);
    }

    @Test
    void crescimentoPatrimonioCalculaSaldoAcumuladoOrdenadoPorData() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 1000.0, "111111", "BRL", null, usuario, 0.0);
        new Credito("Salário", conta, "2026-06-01", 1000.0, "pagamento");
        new Debito("Mercado", conta, "2026-06-02", 200.0, "mercado", "avista");
        new Credito("Rendimento", conta, "2026-06-03", 50.0, "rendimento");

        List<PontoPatrimonio> resultado = viewModel.crescimentoPatrimonio(new Conta[] { conta }, "todas");

        assertEquals(3, resultado.size());
        assertEquals(new PontoPatrimonio("2026-06-01", 1000.0), resultado.get(0));
        assertEquals(new PontoPatrimonio("2026-06-02", 800.0), resultado.get(1));
        assertEquals(new PontoPatrimonio("2026-06-03", 850.0), resultado.get(2));
    }

    @Test
    void crescimentoPatrimonioComFiltroConsideraApenasACategoriaInformada() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 1000.0, "111111", "BRL", null, usuario, 0.0);
        new Credito("Salário", conta, "2026-06-01", 1000.0, "pagamento");
        new Debito("Mercado", conta, "2026-06-02", 200.0, "mercado", "avista");

        List<PontoPatrimonio> resultado = viewModel.crescimentoPatrimonio(new Conta[] { conta }, "mercado");

        assertEquals(1, resultado.size());
        assertEquals(new PontoPatrimonio("2026-06-02", -200.0), resultado.get(0));
    }

    @Test
    void transacoesUltimos7DiasRetornaApenasTransacoesDentroDaJanelaOrdenadasDecrescente() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 1000.0, "111111", "BRL", null, usuario, 0.0);
        LocalDate hoje = LocalDate.of(2026, 6, 11);
        new Debito("Dentro - hoje", conta, "2026-06-11", 10.0, "mercado", "avista");
        new Debito("Dentro - limite", conta, "2026-06-05", 20.0, "mercado", "avista");
        new Debito("Fora - antes do limite", conta, "2026-06-04", 30.0, "mercado", "avista");
        new Debito("Fora - futuro", conta, "2026-06-12", 40.0, "mercado", "avista");

        List<?> resultado = viewModel.transacoesUltimos7Dias(new Conta[] { conta }, hoje);

        assertEquals(2, resultado.size());
        assertEquals("Dentro - hoje", ((com.cfpi.dominio.entidades.transacao.Transacao) resultado.get(0)).getDescricao());
        assertEquals("Dentro - limite", ((com.cfpi.dominio.entidades.transacao.Transacao) resultado.get(1)).getDescricao());
    }

    @Test
    void objetivoPrincipalRetornaPrimeiroDaListaOrdenada() {
        Usuario usuario = criarUsuario();
        Objetivo objetivoA = new Objetivo("Viagem", 5000.0, usuario);
        Objetivo objetivoB = new Objetivo("Carro", 30000.0, usuario);

        Objetivo resultado = viewModel.objetivoPrincipal(List.of(objetivoA, objetivoB));

        assertSame(objetivoA, resultado);
    }

    @Test
    void objetivoPrincipalRetornaNuloQuandoListaVazia() {
        Objetivo resultado = viewModel.objetivoPrincipal(List.of());

        assertNull(resultado);
    }
}
