package com.cfpi.apresentacao.comum;

import com.cfpi.dominio.entidades.investimento.Acao;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AvaliadorDeAtivosFakeTest {

    @Test
    void retornaValorFixoConfiguradoNaConstrucao() {
        AvaliadorDeAtivosFake fake = new AvaliadorDeAtivosFake(1234.56);

        assertEquals(1234.56, fake.valorAtual("PETR4", Acao.class, 100.0));
    }

    @Test
    void retornaMesmoValorIndependenteDosParametros() {
        AvaliadorDeAtivosFake fake = new AvaliadorDeAtivosFake(500.0);

        assertEquals(fake.valorAtual("PETR4", Acao.class, 100.0), fake.valorAtual("VALE3", Acao.class, 1.0));
    }
}
