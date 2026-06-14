package com.cfpi.apresentacao.objetivos;

import com.cfpi.dominio.entidades.objetivo.Objetivo;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjetivosViewModelTest {

    private final ObjetivosViewModel viewModel = new ObjetivosViewModel();

    private Usuario criarUsuario() {
        return new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
    }

    @Test
    void filtrarPorNomeRetornaApenasObjetivosCujoNomeContemOTermo() {
        Usuario usuario = criarUsuario();
        Objetivo viagem = new Objetivo("Viagem para a praia", 5000.0, usuario);
        Objetivo carro = new Objetivo("Carro novo", 30000.0, usuario);
        List<Objetivo> objetivos = List.of(viagem, carro);

        List<Objetivo> resultado = viewModel.filtrarPorNome(objetivos, "viagem");

        assertEquals(List.of(viagem), resultado);
    }

    @Test
    void filtrarPorNomeComTermoEmBrancoRetornaTodosOsObjetivos() {
        Usuario usuario = criarUsuario();
        Objetivo viagem = new Objetivo("Viagem", 5000.0, usuario);
        Objetivo carro = new Objetivo("Carro", 30000.0, usuario);
        List<Objetivo> objetivos = List.of(viagem, carro);

        List<Objetivo> resultado = viewModel.filtrarPorNome(objetivos, " ");

        assertEquals(objetivos, resultado);
    }

    @Test
    void moverSobeUmObjetivoNaLista() {
        Usuario usuario = criarUsuario();
        Objetivo viagem = new Objetivo("Viagem", 5000.0, usuario);
        Objetivo carro = new Objetivo("Carro", 30000.0, usuario);
        Objetivo casa = new Objetivo("Casa", 300000.0, usuario);
        List<Objetivo> objetivos = List.of(viagem, carro, casa);

        List<Objetivo> resultado = viewModel.mover(objetivos, 2, 0);

        assertEquals(List.of(casa, viagem, carro), resultado);
    }

    @Test
    void moverComOrigemForaDoIntervaloRetornaListaInalterada() {
        Usuario usuario = criarUsuario();
        Objetivo viagem = new Objetivo("Viagem", 5000.0, usuario);
        Objetivo carro = new Objetivo("Carro", 30000.0, usuario);
        List<Objetivo> objetivos = List.of(viagem, carro);

        List<Objetivo> resultado = viewModel.mover(objetivos, -1, 0);

        assertEquals(objetivos, resultado);
    }

    @Test
    void moverComDestinoForaDoIntervaloRetornaListaInalterada() {
        Usuario usuario = criarUsuario();
        Objetivo viagem = new Objetivo("Viagem", 5000.0, usuario);
        Objetivo carro = new Objetivo("Carro", 30000.0, usuario);
        List<Objetivo> objetivos = List.of(viagem, carro);

        List<Objetivo> resultado = viewModel.mover(objetivos, 0, 5);

        assertEquals(objetivos, resultado);
    }
}
