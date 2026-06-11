package com.cfpi.dominio.entidades.banco;

import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.usuario.Usuario;
import com.cfpi.dominio.excecoes.RegraNegocioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BancoStoreImplTest {

    private Usuario novoUsuario() {
        return new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");
    }

    @Test
    void inserirBancoComNomeECodigoUnicosRetornaTrueERegistraBanco() {
        BancoStore store = new BancoStoreImpl(novoUsuario());
        Banco banco = new Banco("Banco A", 100);

        assertTrue(store.inserir(banco));
        assertEquals(1, store.getBancos().length);
        assertSame(banco, store.getBancos()[0]);
    }

    @Test
    void inserirBancoComNomeDuplicadoLancaRegraNegocioException() {
        BancoStore store = new BancoStoreImpl(novoUsuario());
        store.inserir(new Banco("Banco A", 100));

        assertThrows(RegraNegocioException.class, () -> store.inserir(new Banco("Banco A", 200)));
    }

    @Test
    void inserirBancoComNomeDuplicadoIgnorandoCaseLancaRegraNegocioException() {
        BancoStore store = new BancoStoreImpl(novoUsuario());
        store.inserir(new Banco("Banco A", 100));

        assertThrows(RegraNegocioException.class, () -> store.inserir(new Banco("banco a", 200)));
    }

    @Test
    void inserirBancoComCodigoDuplicadoLancaRegraNegocioException() {
        BancoStore store = new BancoStoreImpl(novoUsuario());
        store.inserir(new Banco("Banco A", 100));

        assertThrows(RegraNegocioException.class, () -> store.inserir(new Banco("Banco B", 100)));
    }

    @Test
    void inserirBancoComNomeECodigoDiferentesNaoLancaExcecao() {
        BancoStore store = new BancoStoreImpl(novoUsuario());
        store.inserir(new Banco("Banco A", 100));

        assertDoesNotThrow(() -> store.inserir(new Banco("Banco B", 200)));
        assertEquals(2, store.getBancos().length);
    }

    @Test
    void pesquisarPorIdRetornaBancoExistente() {
        BancoStore store = new BancoStoreImpl(novoUsuario());
        Banco banco = new Banco("Banco A", 100);
        store.inserir(banco);

        assertSame(banco, store.pesquisarPorId(banco.getId()));
    }

    @Test
    void pesquisarPorIdComIdInexistenteRetornaNull() {
        BancoStore store = new BancoStoreImpl(novoUsuario());

        assertNull(store.pesquisarPorId(9999));
    }

    @Test
    void pesquisarPorNomeRetornaBancoExistente() {
        BancoStore store = new BancoStoreImpl(novoUsuario());
        Banco banco = new Banco("Banco A", 100);
        store.inserir(banco);

        assertSame(banco, store.pesquisarPorNome("Banco A"));
    }

    @Test
    void pesquisarPorNomeIgnorandoCaseEEspacosRetornaBancoExistente() {
        BancoStore store = new BancoStoreImpl(novoUsuario());
        Banco banco = new Banco("Banco A", 100);
        store.inserir(banco);

        assertSame(banco, store.pesquisarPorNome(" banco a "));
    }

    @Test
    void pesquisarPorNomeComNomeInexistenteRetornaNull() {
        BancoStore store = new BancoStoreImpl(novoUsuario());

        assertNull(store.pesquisarPorNome("Banco Inexistente"));
    }

    @Test
    void pesquisarPorCodigoRetornaBancoExistente() {
        BancoStore store = new BancoStoreImpl(novoUsuario());
        Banco banco = new Banco("Banco A", 100);
        store.inserir(banco);

        assertSame(banco, store.pesquisarPorCodigo(100));
    }

    @Test
    void pesquisarPorCodigoComCodigoInexistenteRetornaNull() {
        BancoStore store = new BancoStoreImpl(novoUsuario());

        assertNull(store.pesquisarPorCodigo(999));
    }

    @Test
    void atualizarBancoComDadosValidosAlteraNomeECodigo() {
        BancoStore store = new BancoStoreImpl(novoUsuario());
        Banco banco = new Banco("Banco A", 100);
        store.inserir(banco);

        Banco novoValor = new Banco(banco.getId());
        novoValor.setNome("Banco Atualizado");
        novoValor.setCodigo(200);

        assertTrue(store.atualizar(banco.getId(), novoValor));
        assertEquals("Banco Atualizado", store.pesquisarPorId(banco.getId()).getNome());
        assertEquals(200, store.pesquisarPorId(banco.getId()).getCodigo());
    }

    @Test
    void atualizarBancoParaNomeDeOutroBancoLancaRegraNegocioException() {
        BancoStore store = new BancoStoreImpl(novoUsuario());
        Banco bancoA = new Banco("Banco A", 100);
        Banco bancoB = new Banco("Banco B", 200);
        store.inserir(bancoA);
        store.inserir(bancoB);

        Banco novoValor = new Banco(bancoB.getId());
        novoValor.setNome("Banco A");
        novoValor.setCodigo(200);

        assertThrows(RegraNegocioException.class, () -> store.atualizar(bancoB.getId(), novoValor));
    }

    @Test
    void atualizarBancoParaCodigoDeOutroBancoLancaRegraNegocioException() {
        BancoStore store = new BancoStoreImpl(novoUsuario());
        Banco bancoA = new Banco("Banco A", 100);
        Banco bancoB = new Banco("Banco B", 200);
        store.inserir(bancoA);
        store.inserir(bancoB);

        Banco novoValor = new Banco(bancoB.getId());
        novoValor.setNome("Banco B");
        novoValor.setCodigo(100);

        assertThrows(RegraNegocioException.class, () -> store.atualizar(bancoB.getId(), novoValor));
    }

    @Test
    void atualizarBancoMantendoSeuProprioNomeECodigoNaoLancaExcecao() {
        BancoStore store = new BancoStoreImpl(novoUsuario());
        Banco banco = new Banco("Banco A", 100);
        store.inserir(banco);

        Banco novoValor = new Banco(banco.getId());
        novoValor.setNome("Banco A");
        novoValor.setCodigo(100);

        assertDoesNotThrow(() -> store.atualizar(banco.getId(), novoValor));
    }

    @Test
    void atualizarBancoComIdInexistenteLancaRegraNegocioException() {
        BancoStore store = new BancoStoreImpl(novoUsuario());
        Banco novoValor = new Banco(9999);
        novoValor.setNome("Banco Inexistente");
        novoValor.setCodigo(100);

        assertThrows(RegraNegocioException.class, () -> store.atualizar(9999, novoValor));
    }

    @Test
    void removerBancoSemContasAssociadasRemoveComSucesso() {
        BancoStore store = new BancoStoreImpl(novoUsuario());
        Banco banco = new Banco("Banco A", 100);
        store.inserir(banco);

        assertTrue(store.remover(banco.getId()));
        assertEquals(0, store.getBancos().length);
    }

    @Test
    void removerBancoComContaAssociadaLancaRegraNegocioException() {
        Usuario usuario = novoUsuario();
        BancoStore store = new BancoStoreImpl(usuario);
        Banco banco = new Banco("Banco A", 100);
        store.inserir(banco);
        new Conta("corrente", 1000.0, "123456", "BRL", banco, usuario, 0.0);

        assertThrows(RegraNegocioException.class, () -> store.remover(banco.getId()));
        assertSame(banco, store.pesquisarPorId(banco.getId()));
    }

    @Test
    void removerBancoComIdInexistenteRetornaFalse() {
        BancoStore store = new BancoStoreImpl(novoUsuario());

        assertFalse(store.remover(9999));
    }

    @Test
    void getBancosRetornaTodosOsBancosInseridos() {
        BancoStore store = new BancoStoreImpl(novoUsuario());
        store.inserir(new Banco("Banco A", 100));
        store.inserir(new Banco("Banco B", 200));

        assertEquals(2, store.getBancos().length);
    }

    @Test
    void getBancosComNenhumBancoInseridoRetornaArrayVazio() {
        BancoStore store = new BancoStoreImpl(novoUsuario());

        assertEquals(0, store.getBancos().length);
    }
}
