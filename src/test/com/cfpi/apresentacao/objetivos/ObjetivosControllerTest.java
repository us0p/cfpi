package com.cfpi.apresentacao.objetivos;

import com.cfpi.dominio.entidades.objetivo.Objetivo;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObjetivosControllerTest {

    private Usuario criarUsuario() {
        return new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
    }

    private ObjetivosController criarController(Usuario usuario) {
        return new ObjetivosController(usuario, new ObjetivosViewModel());
    }

    @Test
    void carregarInicializaOrdemSessaoComObjetivosDoUsuario() {
        Usuario usuario = criarUsuario();
        Objetivo viagem = new Objetivo("Viagem", 5000.0, usuario);
        Objetivo carro = new Objetivo("Carro", 30000.0, usuario);

        ObjetivosController controller = criarController(usuario);

        assertEquals(List.of(viagem, carro), controller.carregar());
    }

    @Test
    void moverAtualizaOrdemSessaoSemAlterarObjetivosDoUsuario() {
        Usuario usuario = criarUsuario();
        Objetivo viagem = new Objetivo("Viagem", 5000.0, usuario);
        Objetivo carro = new Objetivo("Carro", 30000.0, usuario);
        ObjetivosController controller = criarController(usuario);

        List<Objetivo> resultado = controller.mover(1, 0);

        assertEquals(List.of(carro, viagem), resultado);
        assertEquals(List.of(viagem, carro), List.of(usuario.getObjetivos()));
    }

    @Test
    void moverPorArrasteTrocaAsPosicoesDosObjetivosArrastadoEAlvo() {
        Usuario usuario = criarUsuario();
        Objetivo viagem = new Objetivo("Viagem", 5000.0, usuario);
        Objetivo carro = new Objetivo("Carro", 30000.0, usuario);
        ObjetivosController controller = criarController(usuario);

        List<Objetivo> resultado = controller.moverPorArraste(viagem, carro);

        assertEquals(List.of(carro, viagem), resultado);
        assertEquals(List.of(carro, viagem), controller.getOrdemSessao());
    }

    @Test
    void criarComEntradaValidaAdicionaObjetivoNaOrdemSessao() {
        Usuario usuario = criarUsuario();
        ObjetivosController controller = criarController(usuario);

        List<String> erros = controller.criar("Viagem", "5000.0");

        assertTrue(erros.isEmpty());
        assertEquals(1, controller.carregar().size());
        assertEquals("Viagem", controller.carregar().get(0).getNome());
    }

    /**
     * (*) Vermelho esperado: a validação de duplicidade de nome do
     * {@code Objetivo} (documentada no construtor
     * {@code Objetivo(String, double, Usuario)}) é um stub — quando
     * implementada, criar um segundo objetivo com o mesmo nome (após
     * trim/case-insensitive) deve lançar {@code RegraNegocioException} e
     * não adicioná-lo à ordem de exibição.
     */
    @Test
    void criarComNomeDuplicadoRetornaErroENaoAdiciona() {
        Usuario usuario = criarUsuario();
        ObjetivosController controller = criarController(usuario);
        controller.criar("Viagem", "5000.0");

        List<String> erros = controller.criar("Viagem", "1000.0");

        assertFalse(erros.isEmpty());
        assertEquals(1, controller.carregar().size());
    }

    @Test
    void criarComNomeCurtoRetornaErroENaoAdiciona() {
        Usuario usuario = criarUsuario();
        ObjetivosController controller = criarController(usuario);

        List<String> erros = controller.criar("AB", "5000.0");

        assertFalse(erros.isEmpty());
        assertTrue(controller.carregar().isEmpty());
    }

    @Test
    void criarComValorNaoPositivoRetornaErroENaoAdiciona() {
        Usuario usuario = criarUsuario();
        ObjetivosController controller = criarController(usuario);

        List<String> erros = controller.criar("Viagem", "-100.0");

        assertFalse(erros.isEmpty());
        assertTrue(controller.carregar().isEmpty());
    }

    @Test
    void atualizarComEntradaValidaAlteraNomeEValor() {
        Usuario usuario = criarUsuario();
        Objetivo viagem = new Objetivo("Viagem", 5000.0, usuario);
        ObjetivosController controller = criarController(usuario);

        List<String> erros = controller.atualizar(viagem, "Viagem internacional", "8000.0");

        assertTrue(erros.isEmpty());
        assertEquals("Viagem internacional", viagem.getNome());
        assertEquals(8000.0, viagem.getValor(), 0.001);
    }

    /**
     * (*) Vermelho esperado: a validação de duplicidade de nome em
     * {@code Objetivo.setNome} é um stub — quando implementada, renomear um
     * objetivo para o nome de outro objetivo já existente do mesmo usuário
     * deve lançar {@code RegraNegocioException} e não retornar lista de
     * erros vazia.
     */
    @Test
    void atualizarComNomeColidenteRetornaErro() {
        Usuario usuario = criarUsuario();
        new Objetivo("Carro", 30000.0, usuario);
        Objetivo viagem = new Objetivo("Viagem", 5000.0, usuario);
        ObjetivosController controller = criarController(usuario);

        List<String> erros = controller.atualizar(viagem, "Carro", "5000.0");

        assertFalse(erros.isEmpty());
    }

    @Test
    void removerComConfirmacaoRemoveDaOrdemSessao() {
        Usuario usuario = criarUsuario();
        Objetivo viagem = new Objetivo("Viagem", 5000.0, usuario);
        ObjetivosController controller = criarController(usuario);

        boolean removido = controller.remover(viagem, () -> true);

        assertTrue(removido);
        assertTrue(controller.carregar().isEmpty());
    }

    @Test
    void removerComConfirmacaoNegadaNaoAlteraOrdemSessao() {
        Usuario usuario = criarUsuario();
        Objetivo viagem = new Objetivo("Viagem", 5000.0, usuario);
        ObjetivosController controller = criarController(usuario);

        boolean removido = controller.remover(viagem, () -> false);

        assertFalse(removido);
        assertEquals(1, controller.carregar().size());
    }

    @Test
    void filtrarPorNomeRetornaApenasObjetivosCorrespondentesDaOrdemSessao() {
        Usuario usuario = criarUsuario();
        new Objetivo("Viagem", 5000.0, usuario);
        new Objetivo("Carro", 30000.0, usuario);
        ObjetivosController controller = criarController(usuario);

        List<Objetivo> resultado = controller.filtrarPorNome("via");

        assertEquals(1, resultado.size());
        assertEquals("Viagem", resultado.get(0).getNome());
    }
}
