package com.cfpi.apresentacao.contas;

import com.cfpi.dominio.entidades.banco.Banco;
import com.cfpi.dominio.entidades.banco.BancoStoreImpl;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.usuario.Usuario;
import com.cfpi.dominio.excecoes.RegraNegocioException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContasControllerTest {

    private Usuario criarUsuario() {
        return new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
    }

    private ContasController criarController(Usuario usuario) {
        return new ContasController(usuario, new BancoStoreImpl(usuario), new ContasViewModel());
    }

    @Test
    void criarComEntradaValidaAdicionaContaNasContasDoUsuario() {
        Usuario usuario = criarUsuario();
        ContasController controller = criarController(usuario);

        List<String> erros = controller.criar("corrente", "1000.0", "123456", "BRL", null, "0.0");

        assertTrue(erros.isEmpty());
        assertEquals(1, usuario.getContas().length);
        assertEquals("123456", usuario.getContas()[0].getNumeroConta());
    }

    /**
     * (*) Vermelho esperado: a regra de negócio documentada no construtor
     * {@code Conta(String, double, String, String, Banco, Usuario, double)}
     * — rejeitar uma conta com o mesmo {@code banco} e {@code tipo} de outra
     * já existente do mesmo usuário — é um stub. Quando implementada, criar
     * uma segunda conta com o mesmo banco e tipo deve retornar erro e não
     * adicioná-la às contas do usuário.
     */
    @Test
    void criarComBancoETipoDuplicadosRetornaErroENaoAdiciona() {
        Usuario usuario = criarUsuario();
        BancoStoreImpl bancoStore = new BancoStoreImpl(usuario);
        Banco banco = new Banco("Banco Teste", 100);
        bancoStore.inserir(banco);
        ContasController controller = new ContasController(usuario, bancoStore, new ContasViewModel());
        controller.criar("corrente", "1000.0", "123456", "BRL", banco, "0.0");

        List<String> erros = controller.criar("corrente", "2000.0", "654321", "BRL", banco, "0.0");

        assertFalse(erros.isEmpty());
        assertEquals(1, usuario.getContas().length);
    }

    @Test
    void criarComSaldoNegativoRetornaErroENaoAdiciona() {
        Usuario usuario = criarUsuario();
        ContasController controller = criarController(usuario);

        List<String> erros = controller.criar("corrente", "-100.0", "123456", "BRL", null, "0.0");

        assertFalse(erros.isEmpty());
        assertEquals(0, usuario.getContas().length);
    }

    /**
     * (*) Vermelho esperado: a regra de negócio documentada em
     * {@code Usuario.removerConta(int)} — rejeitar a remoção de uma conta
     * com {@code limiteCreditoUtilizado > 0} lançando
     * {@code RegraNegocioException} — é um stub que sempre retorna
     * {@code false} sem lançar exceção.
     */
    @Test
    void removerComLimiteUtilizadoPendenteLancaExcecaoENaoRemove() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, usuario, 500.0);
        conta.setLimiteCreditoUtilizado(100.0);
        ContasController controller = criarController(usuario);

        assertThrows(RegraNegocioException.class, () -> controller.remover(conta, () -> true));
        assertEquals(1, usuario.getContas().length);
    }

    /**
     * (*) Vermelho esperado: {@code Usuario.removerConta(int)} é um stub que
     * sempre retorna {@code false} sem remover a conta; quando implementado,
     * remover uma conta sem limite de crédito utilizado pendente deve
     * retirá-la de {@code usuario.getContas()}.
     */
    @Test
    void removerSemLimiteUtilizadoRemoveDasContasDoUsuario() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, usuario, 0.0);
        ContasController controller = criarController(usuario);

        boolean removido = controller.remover(conta, () -> true);

        assertTrue(removido);
        assertEquals(0, usuario.getContas().length);
    }

    @Test
    void removerComConfirmacaoNegadaNaoAlteraContas() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, usuario, 0.0);
        ContasController controller = criarController(usuario);

        boolean removido = controller.remover(conta, () -> false);

        assertFalse(removido);
        assertEquals(1, usuario.getContas().length);
    }

    @Test
    void atualizarComEntradaValidaAlteraDadosDaConta() {
        Usuario usuario = criarUsuario();
        Banco banco = new Banco("Banco Teste", 100);
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, usuario, 0.0);
        ContasController controller = criarController(usuario);

        List<String> erros = controller.atualizar(conta, "poupança", "2000.0", "654321", "USD", banco, "500.0");

        assertTrue(erros.isEmpty());
        assertEquals("poupança", conta.getTipo());
        assertEquals(2000.0, conta.getValorConta(), 0.001);
        assertEquals("654321", conta.getNumeroConta());
        assertEquals("USD", conta.getMoeda());
        assertEquals(banco, conta.getBanco());
        assertEquals(500.0, conta.getLimiteCredito(), 0.001);
    }

    @Test
    void atualizarComNumeroContaInvalidoRetornaErroENaoAlteraConta() {
        Usuario usuario = criarUsuario();
        Banco banco = new Banco("Banco Teste", 100);
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, usuario, 0.0);
        ContasController controller = criarController(usuario);

        List<String> erros = controller.atualizar(conta, "poupança", "2000.0", "123", "USD", banco, "500.0");

        assertFalse(erros.isEmpty());
        assertEquals("corrente", conta.getTipo());
        assertEquals(1000.0, conta.getValorConta(), 0.001);
        assertEquals("123456", conta.getNumeroConta());
        assertEquals("BRL", conta.getMoeda());
        assertEquals(0.0, conta.getLimiteCredito(), 0.001);
    }

    @Test
    void atualizarComSaldoNaoNumericoRetornaErroENaoAlteraConta() {
        Usuario usuario = criarUsuario();
        Conta conta = new Conta("corrente", 1000.0, "123456", "BRL", null, usuario, 0.0);
        ContasController controller = criarController(usuario);

        List<String> erros = controller.atualizar(conta, "corrente", "abc", "123456", "BRL", null, "0.0");

        assertFalse(erros.isEmpty());
        assertEquals(1000.0, conta.getValorConta(), 0.001);
    }

    @Test
    void filtrarPorNumeroRetornaApenasContasCorrespondentes() {
        Usuario usuario = criarUsuario();
        new Conta("corrente", 1000.0, "123456", "BRL", null, usuario, 0.0);
        new Conta("poupança", 2000.0, "654321", "BRL", null, usuario, 0.0);
        ContasController controller = criarController(usuario);

        List<Conta> resultado = controller.filtrarPorNumero("1234");

        assertEquals(1, resultado.size());
        assertEquals("123456", resultado.get(0).getNumeroConta());
    }
}
