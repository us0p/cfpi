package com.cfpi.dominio.entidades.banco;

import com.cfpi.dominio.excecoes.RegraNegocioException;
import com.cfpi.dominio.excecoes.ValidacaoException;

/**
 * Contrato de armazenamento e integridade dos {@link Banco} cadastrados no
 * sistema: garante unicidade por nome e por código, oferece pesquisas por
 * id/nome/código, e impede a remoção de bancos que ainda possuam contas
 * associadas.
 *
 * <p>{@code Usuario} não mantém nenhuma referência direta a {@link Banco};
 * a única ligação do domínio com bancos é {@code Conta.getBanco()}, que
 * aponta para um {@link Banco} obtido a partir de uma implementação deste
 * contrato. Implementações desta interface dependem do usuário do sistema
 * para verificar, em {@link #remover(int)}, se há contas associadas ao
 * banco que se deseja remover.</p>
 */
public interface BancoStore {

    /**
     * Insere um novo banco na coleção, garantindo unicidade por nome e por
     * código.
     *
     * <p><b>Comportamento previsto:</b> antes de inserir, verifica se já
     * existe um banco com o mesmo {@code nome} (comparação
     * case-insensitive, após {@code trim()}) ou o mesmo {@code codigo}. Se
     * existir, lança {@link RegraNegocioException} e não insere.</p>
     *
     * <p><b>Efeito colateral:</b> adiciona {@code banco} à coleção interna
     * de bancos.</p>
     *
     * @param banco banco a ser inserido (já validado quanto a nome/código
     *              individualmente, conforme {@link Banco}).
     * @return {@code true} se inserido com sucesso.
     * @throws RegraNegocioException se já existir banco com mesmo nome ou
     *         mesmo código. (a ser lançada quando a validação for
     *         implementada)
     */
    boolean inserir(Banco banco);

    /**
     * Pesquisa um banco pelo seu {@code id}.
     *
     * @param id identificador do banco.
     * @return o {@link Banco} correspondente, ou {@code null} se não houver
     *         banco com esse id.
     */
    Banco pesquisarPorId(int id);

    /**
     * Pesquisa um banco pelo seu {@code nome}.
     *
     * <p><b>Comportamento previsto:</b> comparação case-insensitive, após
     * {@code trim()} de ambos os lados.</p>
     *
     * @param nome nome do banco a buscar.
     * @return o {@link Banco} correspondente, ou {@code null} se não
     *         encontrado ou se {@code nome} for {@code null}.
     */
    Banco pesquisarPorNome(String nome);

    /**
     * Pesquisa um banco pelo seu {@code código}.
     *
     * @param codigo código do banco a buscar.
     * @return o {@link Banco} correspondente, ou {@code null} se não
     *         encontrado.
     */
    Banco pesquisarPorCodigo(int codigo);

    /**
     * Atualiza os dados de um banco já cadastrado, garantindo que o novo
     * nome/código não colida com os de outro banco existente (diferente do
     * que está sendo atualizado).
     *
     * <p><b>Comportamento previsto:</b> localiza o banco atual por
     * {@code id}; se não existir, lança {@link RegraNegocioException}. Em
     * seguida verifica se {@code novoValor.getNome()} ou
     * {@code novoValor.getCodigo()} colidem com outro banco (id diferente);
     * se colidir, lança {@link RegraNegocioException}. Caso contrário,
     * substitui o banco na posição correspondente da coleção interna.</p>
     *
     * <p><b>Efeito colateral:</b> substitui a instância de {@link Banco}
     * armazenada na posição correspondente da coleção interna pelo objeto
     * {@code novoValor}.</p>
     *
     * @param id        id do banco a ser atualizado.
     * @param novoValor novo estado do banco (id deve corresponder a
     *                  {@code id}; nome/código já validados
     *                  individualmente conforme {@link Banco}).
     * @return {@code true} se atualizado com sucesso.
     * @throws RegraNegocioException se não existir banco com o {@code id}
     *         informado, ou se o novo nome/código colidir com outro banco
     *         existente. (a ser lançada quando a validação for
     *         implementada)
     */
    boolean atualizar(int id, Banco novoValor);

    /**
     * Remove o banco com o {@code id} informado, desde que não existam
     * contas associadas a ele.
     *
     * <p><b>Comportamento previsto:</b> percorre as contas do usuário do
     * sistema e verifica, para cada conta, se {@code conta.getBanco()} não
     * é {@code null} e {@code conta.getBanco().getId() == id}. Se houver ao
     * menos uma conta associada, lança {@link RegraNegocioException} e não
     * remove. Caso contrário, remove o banco da coleção interna.</p>
     *
     * <p><b>Efeito colateral:</b> remove o banco da coleção interna,
     * deslocando os elementos subsequentes (comportamento de
     * {@code ArrayDinamico#remover(int)}).</p>
     *
     * @param id id do banco a remover.
     * @return {@code true} se removido com sucesso, {@code false} se não
     *         existir banco com esse id.
     * @throws RegraNegocioException se existir alguma conta do usuário
     *         associada a este banco. (a ser lançada quando a validação for
     *         implementada)
     */
    boolean remover(int id);

    /**
     * Retorna todos os bancos cadastrados.
     *
     * @return cópia do array de bancos cadastrados (pode ser vazio, nunca
     *         {@code null}).
     */
    Banco[] getBancos();
}
