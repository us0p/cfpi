package com.cfpi.aplicacao.servicos;

import com.cfpi.apresentacao.comum.CalculadoraPrazoObjetivo;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.objetivo.Objetivo;
import com.cfpi.dominio.entidades.transacao.Credito;
import com.cfpi.dominio.entidades.transacao.Debito;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraPrazoObjetivoServicoTest {

    private final CalculadoraPrazoObjetivoServico servico = new CalculadoraPrazoObjetivoServico();

    private Usuario novoUsuario() {
        return new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
    }

    @Test
    void retornaZeroQuandoPatrimonioAtualJaAtingeOValorDoObjetivo() {
        Usuario usuario = novoUsuario();
        new Conta("corrente", 6000.0, "111111", "BRL", null, usuario, 0.0);
        Objetivo objetivo = new Objetivo("Viagem", 5000.0, usuario);

        assertEquals(0, servico.diasRestantes(objetivo));
    }

    @Test
    void retornaDiasMaximoQuandoFluxoMensalNaoEPositivo() {
        Usuario usuario = novoUsuario();
        Conta conta = new Conta("corrente", 0.0, "111111", "BRL", null, usuario, 0.0);
        new Debito("Mercado", conta, "2026-01-10", 500.0, "mercado", "avista");
        Objetivo objetivo = new Objetivo("Viagem", 5000.0, usuario);

        assertEquals(CalculadoraPrazoObjetivo.PRAZO_INDETERMINADO, servico.diasRestantes(objetivo));
    }

    @Test
    void calculaDiasRestantesAPartirDoFluxoMensalProjetado() {
        Usuario usuario = novoUsuario();
        Conta conta = new Conta("corrente", 0.0, "111111", "BRL", null, usuario, 0.0);
        new Credito("Rendimento", conta, "2026-01-05", 1000.0, "rendimento");
        new Debito("Mercado", conta, "2026-01-10", 200.0, "mercado", "avista");
        Objetivo objetivo = new Objetivo("Viagem", 4000.0, usuario);

        // patrimonioAtual = 800; faltante = 3200; fluxoMensal = 1000 - 200 = 800
        // dias = ceil(3200 / 800 * 30) = 120
        assertEquals(120, servico.diasRestantes(objetivo));
    }

    @Test
    void mediaMensalConsideraNumeroDeMesesDistintosComTransacoes() {
        Usuario usuario = novoUsuario();
        Conta conta = new Conta("corrente", 0.0, "111111", "BRL", null, usuario, 0.0);
        new Credito("Rendimento jan", conta, "2026-01-05", 1000.0, "rendimento");
        new Credito("Rendimento fev", conta, "2026-02-05", 1000.0, "rendimento");
        new Debito("Mercado", conta, "2026-01-10", 200.0, "mercado", "avista");
        Objetivo objetivo = new Objetivo("Viagem", 4000.0, usuario);

        // patrimonioAtual = 1800; faltante = 2200
        // rendaProjetadaMensal = 2000 / 2 meses = 1000; gastosMediosMensais = 200 / 1 mes = 200
        // fluxoMensal = 800; dias = ceil(2200 / 800 * 30) = 83
        assertEquals(83, servico.diasRestantes(objetivo));
    }
}
