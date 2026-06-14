package com.cfpi.dominio.entidades.objetivo;

import com.cfpi.dominio.entidades.usuario.Usuario;
import com.cfpi.dominio.excecoes.RegraNegocioException;
import com.cfpi.dominio.excecoes.ValidacaoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ObjetivoTest {

    private Usuario novoUsuario() {
        return new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");
    }

    @Test
    void idEhIncrementadoAutomaticamenteACadaNovaInstancia() {
        Usuario usuario = novoUsuario();

        Objetivo objetivo1 = new Objetivo("Viagem", 5000.0, usuario);
        Objetivo objetivo2 = new Objetivo("Carro", 30000.0, usuario);

        assertEquals(objetivo1.getId() + 1, objetivo2.getId());
    }

    @Test
    void criarObjetivoComUsuarioNuloLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Objetivo("Viagem", 5000.0, null));
    }

    @Test
    void criarObjetivoComValorZeroLancaValidacaoException() {
        Usuario usuario = novoUsuario();

        assertThrows(ValidacaoException.class, () -> new Objetivo("Viagem", 0.0, usuario));
    }

    @Test
    void criarObjetivoComValorNegativoLancaValidacaoException() {
        Usuario usuario = novoUsuario();

        assertThrows(ValidacaoException.class, () -> new Objetivo("Viagem", -100.0, usuario));
    }

    @Test
    void criarObjetivoComValorPositivoNaoLancaExcecao() {
        Usuario usuario = novoUsuario();

        assertDoesNotThrow(() -> new Objetivo("Viagem", 0.01, usuario));
    }

    @Test
    void criarObjetivoComNomeDuplicadoNoMesmoUsuarioLancaRegraNegocioException() {
        Usuario usuario = novoUsuario();
        new Objetivo("Viagem", 5000.0, usuario);

        assertThrows(RegraNegocioException.class, () -> new Objetivo("Viagem", 1000.0, usuario));
    }

    @Test
    void criarObjetivoComNomeDuplicadoComCaseDiferenteLancaRegraNegocioException() {
        Usuario usuario = novoUsuario();
        new Objetivo("Viagem", 5000.0, usuario);

        assertThrows(RegraNegocioException.class, () -> new Objetivo("viagem", 1000.0, usuario));
    }

    @Test
    void criarObjetivoComNomeDuplicadoEmUsuariosDiferentesNaoLancaExcecao() {
        Usuario usuario1 = novoUsuario();
        Usuario usuario2 = new Usuario("Ciclano", "22222222222", "11888888888", "1991-02-02");
        new Objetivo("Viagem", 5000.0, usuario1);

        assertDoesNotThrow(() -> new Objetivo("Viagem", 1000.0, usuario2));
    }

    @Test
    void criarObjetivoComNomesDiferentesNoMesmoUsuarioNaoLancaExcecao() {
        Usuario usuario = novoUsuario();
        new Objetivo("Viagem", 5000.0, usuario);

        assertDoesNotThrow(() -> new Objetivo("Carro", 30000.0, usuario));
    }

    @Test
    void construtorVazioNaoAplicaValidacao() {
        assertDoesNotThrow(() -> new Objetivo());
    }

    @Test
    void construtorComIdNaoAplicaValidacao() {
        assertDoesNotThrow(() -> new Objetivo(999));
    }

    @Test
    void setValorComValorZeroLancaValidacaoExceptionENaoAlteraOValor() {
        Usuario usuario = novoUsuario();
        Objetivo objetivo = new Objetivo("Viagem", 5000.0, usuario);

        assertThrows(ValidacaoException.class, () -> objetivo.setValor(0.0));
        assertEquals(5000.0, objetivo.getValor());
    }

    @Test
    void setValorComValorNegativoLancaValidacaoException() {
        Usuario usuario = novoUsuario();
        Objetivo objetivo = new Objetivo("Viagem", 5000.0, usuario);

        assertThrows(ValidacaoException.class, () -> objetivo.setValor(-1.0));
    }

    @Test
    void setValorComValorPositivoAtualizaValor() {
        Usuario usuario = novoUsuario();
        Objetivo objetivo = new Objetivo("Viagem", 5000.0, usuario);

        objetivo.setValor(2000.0);

        assertEquals(2000.0, objetivo.getValor());
    }

    @Test
    void setUsuarioComUsuarioNuloLancaValidacaoExceptionENaoAlteraOUsuario() {
        Usuario usuario = novoUsuario();
        Objetivo objetivo = new Objetivo("Viagem", 5000.0, usuario);

        assertThrows(ValidacaoException.class, () -> objetivo.setUsuario(null));
        assertEquals(usuario, objetivo.getUsuario());
    }

    @Test
    void setNomeParaNomeJaUsadoPorOutroObjetivoDoMesmoUsuarioLancaRegraNegocioException() {
        Usuario usuario = novoUsuario();
        new Objetivo("Viagem", 5000.0, usuario);
        Objetivo carro = new Objetivo("Carro", 30000.0, usuario);

        assertThrows(RegraNegocioException.class, () -> carro.setNome("Viagem"));
    }

    @Test
    void setNomeParaOMesmoNomeQueOProprioObjetivoJaTemNaoLancaExcecao() {
        Usuario usuario = novoUsuario();
        Objetivo objetivo = new Objetivo("Viagem", 5000.0, usuario);

        assertDoesNotThrow(() -> objetivo.setNome("Viagem"));
    }

    @Test
    void setNomeParaOMesmoNomeComCaseDiferenteQueOProprioObjetivoJaTemNaoLancaExcecao() {
        Usuario usuario = novoUsuario();
        Objetivo objetivo = new Objetivo("Viagem", 5000.0, usuario);

        assertDoesNotThrow(() -> objetivo.setNome("VIAGEM"));
    }

    @Test
    void setNomeParaNomeNaoUsadoAtualizaNome() {
        Usuario usuario = novoUsuario();
        Objetivo objetivo = new Objetivo("Viagem", 5000.0, usuario);

        objetivo.setNome("Viagem dos Sonhos");

        assertEquals("Viagem dos Sonhos", objetivo.getNome());
    }
}
