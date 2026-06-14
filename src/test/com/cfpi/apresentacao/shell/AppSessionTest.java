package com.cfpi.apresentacao.shell;

import com.cfpi.apresentacao.comum.AvaliadorDeAtivosFake;
import com.cfpi.apresentacao.comum.CalculadoraPrazoObjetivoFake;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class AppSessionTest {

    @Test
    void usuarioAtualPadraoEhNulo() {
        AppSession sessao = new AppSession(new CalculadoraPrazoObjetivoFake(0), new AvaliadorDeAtivosFake(0.0));

        assertNull(sessao.getUsuarioAtual());
    }

    @Test
    void setUsuarioAtualArmazenaUsuario() {
        AppSession sessao = new AppSession(new CalculadoraPrazoObjetivoFake(0), new AvaliadorDeAtivosFake(0.0));
        Usuario usuario = new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");

        sessao.setUsuarioAtual(usuario);

        assertSame(usuario, sessao.getUsuarioAtual());
    }

    @Test
    void getCalculadoraPrazoObjetivoRetornaInstanciaInjetada() {
        CalculadoraPrazoObjetivoFake calculadora = new CalculadoraPrazoObjetivoFake(5);
        AppSession sessao = new AppSession(calculadora, new AvaliadorDeAtivosFake(0.0));

        assertSame(calculadora, sessao.getCalculadoraPrazoObjetivo());
    }

    @Test
    void getAvaliadorDeAtivosRetornaInstanciaInjetada() {
        AvaliadorDeAtivosFake avaliador = new AvaliadorDeAtivosFake(100.0);
        AppSession sessao = new AppSession(new CalculadoraPrazoObjetivoFake(0), avaliador);

        assertSame(avaliador, sessao.getAvaliadorDeAtivos());
    }
}
