package com.cfpi.apresentacao.shell;

import com.cfpi.apresentacao.comum.AvaliadorDeAtivos;
import com.cfpi.apresentacao.comum.CalculadoraPrazoObjetivo;
import com.cfpi.dominio.entidades.usuario.Usuario;

/**
 * Estado em memória compartilhado pela sessão atual da aplicação: o
 * {@link Usuario} logado e as implementações de
 * {@link CalculadoraPrazoObjetivo}/{@link AvaliadorDeAtivos} usadas pelas
 * telas.
 */
public class AppSession {

    private Usuario usuarioAtual;
    private final CalculadoraPrazoObjetivo calculadoraPrazoObjetivo;
    private final AvaliadorDeAtivos avaliadorDeAtivos;

    public AppSession(CalculadoraPrazoObjetivo calculadoraPrazoObjetivo, AvaliadorDeAtivos avaliadorDeAtivos) {
        this.calculadoraPrazoObjetivo = calculadoraPrazoObjetivo;
        this.avaliadorDeAtivos = avaliadorDeAtivos;
    }

    public Usuario getUsuarioAtual() {
        return usuarioAtual;
    }

    public void setUsuarioAtual(Usuario usuarioAtual) {
        this.usuarioAtual = usuarioAtual;
    }

    public CalculadoraPrazoObjetivo getCalculadoraPrazoObjetivo() {
        return calculadoraPrazoObjetivo;
    }

    public AvaliadorDeAtivos getAvaliadorDeAtivos() {
        return avaliadorDeAtivos;
    }
}
