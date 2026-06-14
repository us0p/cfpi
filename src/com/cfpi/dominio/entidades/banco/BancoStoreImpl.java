package com.cfpi.dominio.entidades.banco;

import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.usuario.Usuario;
import com.cfpi.dominio.arraydinamico.ArrayDinamico;
import com.cfpi.dominio.excecoes.RegraNegocioException;

/**
 * Implementação de {@link BancoStore} baseada em {@link ArrayDinamico}.
 *
 * <p>Mantém sua própria coleção de bancos (nenhuma outra classe possui
 * referência direta a ela) e depende do {@link Usuario} do sistema apenas
 * para verificar, em {@link #remover(int)}, se há {@link Conta contas}
 * associadas ao banco que se deseja remover.</p>
 */
public class BancoStoreImpl implements BancoStore {

    private static final int CAPACIDADE_INICIAL = 10;

    private final Usuario usuario;
    private final ArrayDinamico<Banco> bancos;

    /**
     * Cria um {@code BancoStoreImpl} associado ao usuário do sistema.
     *
     * @param usuario usuário cujas contas serão consultadas em
     *                {@link #remover(int)} para verificar dependências.
     * @throws ValidacaoException se {@code usuario} for {@code null}. (a
     *         ser lançada quando a validação for implementada)
     */
    public BancoStoreImpl(Usuario usuario) {
        this.usuario = usuario;
        this.bancos = new ArrayDinamico<>(Banco.class, CAPACIDADE_INICIAL);
    }

    @Override
    public boolean inserir(Banco banco) {
        if (pesquisarPorNome(banco.getNome()) != null || pesquisarPorCodigo(banco.getCodigo()) != null) {
            throw new RegraNegocioException("Já existe um banco cadastrado com este nome ou código.");
        }
        return bancos.inserir(banco);
    }

    @Override
    public Banco pesquisarPorId(int id) {
        for (Banco banco : bancos.getArr()) {
            if (banco.getId() == id) {
                return banco;
            }
        }
        return null;
    }

    @Override
    public Banco pesquisarPorNome(String nome) {
        if (nome == null) {
            return null;
        }
        String alvo = nome.trim();
        for (Banco banco : bancos.getArr()) {
            if (banco.getNome() != null && banco.getNome().trim().equalsIgnoreCase(alvo)) {
                return banco;
            }
        }
        return null;
    }

    @Override
    public Banco pesquisarPorCodigo(int codigo) {
        for (Banco banco : bancos.getArr()) {
            if (banco.getCodigo() == codigo) {
                return banco;
            }
        }
        return null;
    }

    @Override
    public boolean atualizar(int id, Banco novoValor) {
        int idx = indexOf(id);
        if (idx < 0) {
            throw new RegraNegocioException("Não existe banco cadastrado com este id.");
        }

        Banco existentePorNome = pesquisarPorNome(novoValor.getNome());
        if (existentePorNome != null && existentePorNome.getId() != id) {
            throw new RegraNegocioException("Já existe outro banco cadastrado com este nome.");
        }

        Banco existentePorCodigo = pesquisarPorCodigo(novoValor.getCodigo());
        if (existentePorCodigo != null && existentePorCodigo.getId() != id) {
            throw new RegraNegocioException("Já existe outro banco cadastrado com este código.");
        }

        return bancos.atualizar(idx, novoValor);
    }

    @Override
    public boolean remover(int id) {
        int idx = indexOf(id);
        if (idx < 0) {
            return false;
        }

        for (Conta conta : usuario.getContas()) {
            if (conta.getBanco() != null && conta.getBanco().getId() == id) {
                throw new RegraNegocioException("Não é possível remover o banco pois há contas associadas a ele.");
            }
        }

        return bancos.remover(idx);
    }

    @Override
    public Banco[] getBancos() {
        return bancos.getArr();
    }

    private int indexOf(int id) {
        Banco[] arr = bancos.getArr();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }
}
