package com.cfpi.dominio.entidades.banco;

import com.cfpi.dominio.excecoes.ValidacaoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BancoTest {

    @Test
    void idEhIncrementadoAutomaticamenteACadaNovaInstancia() {
        Banco banco1 = new Banco("Banco A", 237);
        Banco banco2 = new Banco("Banco B", 237);

        assertEquals(banco1.getId() + 1, banco2.getId());
    }

    @Test
    void construtorComIdNaoAlteraSequenciaAutomatica() {
        Banco bancoExistente = new Banco(999);
        Banco bancoNovo = new Banco("Banco Novo", 237);

        assertEquals(999, bancoExistente.getId());
        assertNotEquals(999, bancoNovo.getId());
    }

    @Test
    void criarBancoComNomeECodigoValidosNaoLancaExcecao() {
        assertDoesNotThrow(() -> new Banco("Banco Teste", 237));
    }

    @Test
    void criarBancoComNomeDeExatamente3CaracteresLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Banco("ABC", 237));
    }

    @Test
    void criarBancoComNomeDeExatamente4CaracteresNaoLancaExcecao() {
        assertDoesNotThrow(() -> new Banco("ABCD", 237));
    }

    @Test
    void criarBancoComNomeContendoApenasEspacosLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Banco("    ", 237));
    }

    @Test
    void criarBancoComNomePontuacaoEDigitosEhValido() {
        assertDoesNotThrow(() -> new Banco("Banco B3 S.A.", 237));
    }

    @Test
    void criarBancoComNomeContendoCaracteresEspeciaisLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Banco("Banco@X", 237));
    }

    @Test
    void criarBancoComNomeNuloLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Banco(null, 237));
    }

    @Test
    void criarBancoComCodigo1NaoLancaExcecao() {
        assertDoesNotThrow(() -> new Banco("Banco Teste", 1));
    }

    @Test
    void criarBancoComCodigo100NaoLancaExcecao() {
        assertDoesNotThrow(() -> new Banco("Banco Teste", 100));
    }

    @Test
    void criarBancoComCodigo999NaoLancaExcecao() {
        assertDoesNotThrow(() -> new Banco("Banco Teste", 999));
    }

    @Test
    void criarBancoComCodigoDe4DigitosLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Banco("Banco Teste", 1000));
    }

    @Test
    void criarBancoComCodigoZeroLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Banco("Banco Teste", 0));
    }

    @Test
    void criarBancoComCodigoNegativoLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Banco("Banco Teste", -100));
    }

    @Test
    void setNomeComValorValidoAtualizaNome() {
        Banco banco = new Banco("Banco Teste", 237);

        banco.setNome("Banco Novo");

        assertEquals("Banco Novo", banco.getNome());
    }

    @Test
    void setNomeComValorInvalidoLancaValidacaoExceptionENaoAlteraONome() {
        Banco banco = new Banco("Banco Teste", 237);

        assertThrows(ValidacaoException.class, () -> banco.setNome("ab"));
        assertEquals("Banco Teste", banco.getNome());
    }

    @Test
    void setCodigoComValorValidoAtualizaCodigo() {
        Banco banco = new Banco("Banco Teste", 237);

        banco.setCodigo(341);

        assertEquals(341, banco.getCodigo());
    }

    @Test
    void setCodigoComValorInvalidoLancaValidacaoExceptionENaoAlteraOCodigo() {
        Banco banco = new Banco("Banco Teste", 237);

        assertThrows(ValidacaoException.class, () -> banco.setCodigo(0));
        assertEquals(237, banco.getCodigo());
    }

    @Test
    void construtorVazioNaoAplicaValidacao() {
        assertDoesNotThrow(() -> new Banco());
    }

    @Test
    void construtorComIdNaoAplicaValidacao() {
        assertDoesNotThrow(() -> new Banco(999));
    }
}
