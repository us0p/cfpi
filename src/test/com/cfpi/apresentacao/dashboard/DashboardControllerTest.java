package com.cfpi.apresentacao.dashboard;

import com.cfpi.apresentacao.comum.AvaliadorDeAtivosFake;
import com.cfpi.apresentacao.comum.CalculadoraPrazoObjetivoFake;
import com.cfpi.apresentacao.shell.AppSession;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.objetivo.Objetivo;
import com.cfpi.dominio.entidades.transacao.Credito;
import com.cfpi.dominio.entidades.transacao.Debito;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class DashboardControllerTest {

    @Test
    void carregarPopulaDadosConsolidadosDoUsuarioAtual() {
        Usuario usuario = new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
        Conta conta = new Conta("corrente", 1000.0, "111111", "BRL", null, usuario, 500.0);
        conta.setLimiteCreditoUtilizado(100.0);
        new Debito("Mercado", conta, LocalDate.now().toString(), 100.0, "mercado", "avista");
        new Credito("Salário", conta, "2026-01-02", 2000.0, "pagamento");
        Objetivo objetivo = new Objetivo("Viagem", 5000.0, usuario);

        AppSession appSession = new AppSession(new CalculadoraPrazoObjetivoFake(30), new AvaliadorDeAtivosFake(0.0));
        appSession.setUsuarioAtual(usuario);

        DashboardController controller = new DashboardController(appSession, List.of(objetivo), new DashboardViewModel());

        DashboardDados dados = controller.carregar("todas");

        assertEquals(2900.0, dados.getSaldoTotal(), 0.001);
        assertEquals(0.2, dados.getPercentualLimiteConsumido(), 0.001);
        assertEquals(100.0, dados.getGastosPorCategoria().get("mercado"), 0.001);
        assertEquals(2, dados.getCrescimentoPatrimonio().size());
        assertEquals(1, dados.getTransacoesRecentes().size());
        assertSame(objetivo, dados.getObjetivoPrincipal());
        assertEquals(30, dados.getDiasRestantesObjetivo());
    }

    @Test
    void carregarSemObjetivosRetornaObjetivoPrincipalNuloEZeroDiasRestantes() {
        Usuario usuario = new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
        new Conta("corrente", 1000.0, "111111", "BRL", null, usuario, 0.0);

        AppSession appSession = new AppSession(new CalculadoraPrazoObjetivoFake(30), new AvaliadorDeAtivosFake(0.0));
        appSession.setUsuarioAtual(usuario);

        DashboardController controller = new DashboardController(appSession, List.of(), new DashboardViewModel());

        DashboardDados dados = controller.carregar("todas");

        assertNull(dados.getObjetivoPrincipal());
        assertEquals(0, dados.getDiasRestantesObjetivo());
    }
}
