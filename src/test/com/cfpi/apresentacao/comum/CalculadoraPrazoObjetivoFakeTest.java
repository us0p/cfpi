package com.cfpi.apresentacao.comum;

import com.cfpi.dominio.entidades.objetivo.Objetivo;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraPrazoObjetivoFakeTest {

    @Test
    void retornaValorFixoConfiguradoNaConstrucao() {
        CalculadoraPrazoObjetivoFake fake = new CalculadoraPrazoObjetivoFake(42);
        Usuario usuario = new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
        Objetivo objetivo = new Objetivo("Viagem", 5000.0, usuario);

        assertEquals(42, fake.diasRestantes(objetivo));
    }

    @Test
    void retornaMesmoValorParaObjetivosDiferentes() {
        CalculadoraPrazoObjetivoFake fake = new CalculadoraPrazoObjetivoFake(10);
        Usuario usuario = new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
        Objetivo objetivoA = new Objetivo("Viagem", 5000.0, usuario);
        Objetivo objetivoB = new Objetivo("Carro", 30000.0, usuario);

        assertEquals(fake.diasRestantes(objetivoA), fake.diasRestantes(objetivoB));
    }
}
