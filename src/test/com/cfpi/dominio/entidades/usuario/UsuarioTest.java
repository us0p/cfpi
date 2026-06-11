package com.cfpi.dominio.entidades.usuario;

import com.cfpi.dominio.entidades.banco.Banco;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.objetivo.Objetivo;
import com.cfpi.dominio.excecoes.RegraNegocioException;
import com.cfpi.dominio.excecoes.ValidacaoException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioTest {

    @Test
    void idEhIncrementadoAutomaticamenteACadaNovaInstancia() {
        Usuario usuario1 = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");
        Usuario usuario2 = new Usuario("Ciclano", "22222222222", "11888888888", "1991-02-02");

        assertEquals(usuario1.getId() + 1, usuario2.getId());
    }

    @Test
    void construtorComIdNaoAlteraSequenciaAutomatica() {
        Usuario usuarioExistente = new Usuario(999);
        Usuario usuarioNovo = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");

        assertEquals(999, usuarioExistente.getId());
        assertNotEquals(999, usuarioNovo.getId());
    }

    @Test
    void criarObjetivoComUsuarioRegistraObjetivoNoUsuario() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");

        Objetivo objetivo = new Objetivo("Viagem", 5000.0, usuario);

        Objetivo[] objetivos = usuario.getObjetivos();
        assertEquals(1, objetivos.length);
        assertSame(objetivo, objetivos[0]);
    }

    // --- nome ---

    @Test
    void criarUsuarioComNomeValidoNaoLancaExcecao() {
        assertDoesNotThrow(() -> new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01"));
    }

    @Test
    void criarUsuarioComNomeDeExatamente2CaracteresLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Usuario("Jo", "11111111111", "11999999999", "1990-01-01"));
    }

    @Test
    void criarUsuarioComNomeDeExatamente3CaracteresNaoLancaExcecao() {
        assertDoesNotThrow(() -> new Usuario("Ana", "11111111111", "11999999999", "1990-01-01"));
    }

    @Test
    void criarUsuarioComNomeContendoDigitosLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Usuario("Ana123", "11111111111", "11999999999", "1990-01-01"));
    }

    @Test
    void criarUsuarioComNomeContendoApenasEspacosLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Usuario("   ", "11111111111", "11999999999", "1990-01-01"));
    }

    @Test
    void criarUsuarioComNomeNuloLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Usuario(null, "11111111111", "11999999999", "1990-01-01"));
    }

    @Test
    void criarUsuarioComNomeCompostoContendoMultiplosEspacosNaoLancaExcecao() {
        assertDoesNotThrow(() -> new Usuario("Maria da Silva", "11111111111", "11999999999", "1990-01-01"));
    }

    // --- cpf ---

    @Test
    void criarUsuarioComCpfDe11DigitosNaoLancaExcecao() {
        assertDoesNotThrow(() -> new Usuario("Fulano", "12345678901", "11999999999", "1990-01-01"));
    }

    @Test
    void criarUsuarioComCpfDe10DigitosLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Usuario("Fulano", "1234567890", "11999999999", "1990-01-01"));
    }

    @Test
    void criarUsuarioComCpfDe12DigitosLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Usuario("Fulano", "123456789012", "11999999999", "1990-01-01"));
    }

    @Test
    void criarUsuarioComCpfContendoLetrasLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Usuario("Fulano", "1234567890a", "11999999999", "1990-01-01"));
    }

    @Test
    void criarUsuarioComCpfContendoCaracteresEspeciaisLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Usuario("Fulano", "123.456.789-01", "11999999999", "1990-01-01"));
    }

    @Test
    void criarUsuarioComCpfNuloLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Usuario("Fulano", null, "11999999999", "1990-01-01"));
    }

    @Test
    void getCpfFormatadoRetornaCpfNoPadraoComPontosEHifen() {
        Usuario usuario = new Usuario("Fulano", "12345678901", "11999999999", "1990-01-01");

        assertEquals("123.456.789-01", usuario.getCpfFormatado());
    }

    // --- telefone ---

    @Test
    void criarUsuarioComTelefoneDe11DigitosNaoLancaExcecao() {
        assertDoesNotThrow(() -> new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01"));
    }

    @Test
    void criarUsuarioComTelefoneDe10DigitosLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Usuario("Fulano", "11111111111", "1199999999", "1990-01-01"));
    }

    @Test
    void criarUsuarioComTelefoneDe12DigitosLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Usuario("Fulano", "11111111111", "119999999999", "1990-01-01"));
    }

    @Test
    void criarUsuarioComTelefoneContendoLetrasLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Usuario("Fulano", "11111111111", "1199999999a", "1990-01-01"));
    }

    @Test
    void criarUsuarioComTelefoneNuloLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Usuario("Fulano", "11111111111", null, "1990-01-01"));
    }

    @Test
    void getTelefoneFormatadoRetornaTelefoneNoPadraoComParentesesEHifen() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");

        assertEquals("(11) 99999-9999", usuario.getTelefoneFormatado());
    }

    // --- data de nascimento ---

    @Test
    void criarUsuarioComDataNascimentoPassadaNaoLancaExcecao() {
        assertDoesNotThrow(() -> new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01"));
    }

    @Test
    void criarUsuarioComDataNascimentoIgualAHojeLancaValidacaoException() {
        String hoje = LocalDate.now().toString();

        assertThrows(ValidacaoException.class, () -> new Usuario("Fulano", "11111111111", "11999999999", hoje));
    }

    @Test
    void criarUsuarioComDataNascimentoFuturaLancaValidacaoException() {
        String amanha = LocalDate.now().plusDays(1).toString();

        assertThrows(ValidacaoException.class, () -> new Usuario("Fulano", "11111111111", "11999999999", amanha));
    }

    @Test
    void criarUsuarioComDataNascimentoEmFormatoInvalidoLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Usuario("Fulano", "11111111111", "11999999999", "10-06-2026"));
    }

    @Test
    void criarUsuarioComDataNascimentoComMesInvalidoLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Usuario("Fulano", "11111111111", "11999999999", "2026-13-40"));
    }

    @Test
    void criarUsuarioComDataNascimentoComDiaInvalidoParaOMesLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Usuario("Fulano", "11111111111", "11999999999", "2026-02-30"));
    }

    @Test
    void criarUsuarioComDataNascimentoNulaLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Usuario("Fulano", "11111111111", "11999999999", null));
    }

    @Test
    void criarUsuarioComDataNascimentoVaziaLancaValidacaoException() {
        assertThrows(ValidacaoException.class, () -> new Usuario("Fulano", "11111111111", "11999999999", ""));
    }

    // --- setters ---

    @Test
    void setNomeComValorInvalidoLancaValidacaoExceptionENaoAlteraONome() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");

        assertThrows(ValidacaoException.class, () -> usuario.setNome("X"));
        assertEquals("Fulano", usuario.getNome());
    }

    @Test
    void setCpfComValorInvalidoLancaValidacaoExceptionENaoAlteraOCpf() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");

        assertThrows(ValidacaoException.class, () -> usuario.setCpf("123"));
        assertEquals("11111111111", usuario.getCpf());
    }

    @Test
    void setTelefoneComValorInvalidoLancaValidacaoExceptionENaoAlteraOTelefone() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");

        assertThrows(ValidacaoException.class, () -> usuario.setTelefone("123"));
        assertEquals("11999999999", usuario.getTelefone());
    }

    @Test
    void setDataNascimentoComDataFuturaLancaValidacaoExceptionENaoAlteraAData() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");
        String amanha = LocalDate.now().plusDays(1).toString();

        assertThrows(ValidacaoException.class, () -> usuario.setDataNascimento(amanha));
        assertEquals("1990-01-01", usuario.getDataNascimento());
    }

    @Test
    void setDataNascimentoComFormatoInvalidoLancaValidacaoException() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");

        assertThrows(ValidacaoException.class, () -> usuario.setDataNascimento("31/12/2000"));
    }

    // --- construtores de pesquisa ---

    @Test
    void construtorVazioNaoAplicaValidacao() {
        assertDoesNotThrow(() -> new Usuario());
    }

    @Test
    void construtorComIdNaoAplicaValidacao() {
        assertDoesNotThrow(() -> new Usuario(999));
    }

    // --- pesquisa de objetivos ---

    @Test
    void pesquisarObjetivoPorIdRetornaObjetivoExistente() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");
        Objetivo objetivo = new Objetivo("Viagem", 5000.0, usuario);

        assertSame(objetivo, usuario.pesquisarObjetivoPorId(objetivo.getId()));
    }

    @Test
    void pesquisarObjetivoPorIdComIdInexistenteRetornaNull() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");

        assertNull(usuario.pesquisarObjetivoPorId(9999));
    }

    @Test
    void pesquisarObjetivoPorNomeRetornaObjetivoExistente() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");
        Objetivo objetivo = new Objetivo("Viagem", 5000.0, usuario);

        assertSame(objetivo, usuario.pesquisarObjetivoPorNome("Viagem"));
    }

    @Test
    void pesquisarObjetivoPorNomeIgnorandoCaseRetornaObjetivoExistente() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");
        Objetivo objetivo = new Objetivo("Viagem", 5000.0, usuario);

        assertSame(objetivo, usuario.pesquisarObjetivoPorNome("viagem"));
    }

    @Test
    void pesquisarObjetivoPorNomeComNomeInexistenteRetornaNull() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");

        assertNull(usuario.pesquisarObjetivoPorNome("Inexistente"));
    }

    // --- pesquisa de contas ---

    @Test
    void pesquisarContaPorIdRetornaContaExistente() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");
        Banco banco = new Banco("Banco A", 100);
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", banco, usuario, 0.0);

        assertSame(conta, usuario.pesquisarContaPorId(conta.getId()));
    }

    @Test
    void pesquisarContaPorIdComIdInexistenteRetornaNull() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");

        assertNull(usuario.pesquisarContaPorId(9999));
    }

    @Test
    void pesquisarContaPorNumeroRetornaContaExistente() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");
        Banco banco = new Banco("Banco A", 100);
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", banco, usuario, 0.0);

        assertSame(conta, usuario.pesquisarContaPorNumero("123456"));
    }

    @Test
    void pesquisarContaPorNumeroComNumeroInexistenteRetornaNull() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");

        assertNull(usuario.pesquisarContaPorNumero("000000"));
    }

    // --- removerConta ---

    @Test
    void removerContaSemLimiteCreditoUtilizadoRemoveComSucesso() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");
        Banco banco = new Banco("Banco A", 100);
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", banco, usuario, 0.0);

        assertTrue(usuario.removerConta(conta.getId()));
        assertEquals(0, usuario.getContas().length);
    }

    @Test
    void removerContaComLimiteCreditoUtilizadoPendenteLancaRegraNegocioException() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");
        Banco banco = new Banco("Banco A", 100);
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", banco, usuario, 1000.0);
        conta.setLimiteCreditoUtilizado(200.0);

        assertThrows(RegraNegocioException.class, () -> usuario.removerConta(conta.getId()));
    }

    @Test
    void removerContaComIdInexistenteRetornaFalse() {
        Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");

        assertFalse(usuario.removerConta(9999));
    }
}
