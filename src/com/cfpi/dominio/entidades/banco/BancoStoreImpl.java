package com.cfpi.dominio.entidades.banco;

import com.cfpi.dominio.entidades.usuario.Usuario;
import com.cfpi.dominio.arraydinamico.ArrayDinamico;

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
        // TODO: validar duplicidade por nome/código (RegraNegocioException) - a implementar
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
        // TODO: validar colisão de nome/código com outro banco existente
        // (RegraNegocioException) e ausência do banco (RegraNegocioException)
        // - a implementar
        int idx = indexOf(id);
        if (idx < 0) {
            return false;
        }
        return bancos.atualizar(idx, novoValor);
    }

    @Override
    public boolean remover(int id) {
        // TODO: validar dependência de contas associadas ao banco
        // (RegraNegocioException) - a implementar, usando usuario.getContas()
        int idx = indexOf(id);
        if (idx < 0) {
            return false;
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
