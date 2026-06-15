package com.cfpi.apresentacao.transacoes;

import com.cfpi.apresentacao.comum.ValidadoresFormulario;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.transacao.Credito;
import com.cfpi.dominio.entidades.transacao.Debito;
import com.cfpi.dominio.entidades.transacao.Transacao;
import com.cfpi.dominio.entidades.usuario.Usuario;
import com.cfpi.dominio.excecoes.RegraNegocioException;
import com.cfpi.dominio.excecoes.ValidacaoException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Orquestra o carregamento, criação, atualização e remoção de transações.
 */
public class TransacoesController {

    private final Usuario usuario;
    private final TransacoesViewModel viewModel;

    public TransacoesController(Usuario usuario, TransacoesViewModel viewModel) {
        this.usuario = usuario;
        this.viewModel = viewModel;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Reúne as transações de todas as contas do usuário, ordenadas pela
     * data mais recente primeiro.
     *
     * @return lista de transações de todas as contas, ordenada por
     *         {@link Transacao#getData()} decrescente.
     */
    public List<Transacao> carregar() {
        List<Transacao> todas = new ArrayList<>();
        for (Conta conta : usuario.getContas()) {
            todas.addAll(Arrays.asList(conta.getTransacoes()));
        }
        return viewModel.ordenarPorDataDesc(todas.toArray(new Transacao[0]));
    }

    /**
     * Pesquisa as transações da conta dentro do período informado.
     *
     * @param conta      conta cujas transações serão pesquisadas.
     * @param dataInicio data inicial do intervalo (inclusive), formato {@code yyyy-MM-dd}.
     * @param dataFim    data final do intervalo (inclusive), formato {@code yyyy-MM-dd}.
     * @return as transações encontradas por
     *         {@link Conta#pesquisarTransacoesPorPeriodo(String, String)},
     *         ou um array vazio caso esse método retorne {@code null}.
     */
    public Transacao[] filtrarPorPeriodo(Conta conta, String dataInicio, String dataFim) {
        Transacao[] resultado = conta.pesquisarTransacoesPorPeriodo(dataInicio, dataFim);
        return resultado != null ? resultado : new Transacao[0];
    }

    /**
     * Valida e cria um novo {@link Debito} na conta informada.
     *
     * @param conta     conta à qual o débito pertence.
     * @param descricao descrição do débito.
     * @param data      data do débito, formato {@code yyyy-MM-dd}.
     * @param valorTexto valor do débito, como texto (deve representar um número maior que zero).
     * @param categoria categoria do débito.
     * @param tipo      tipo do débito ({@code "credito"} ou {@code "avista"}).
     * @return lista de mensagens de erro; vazia se o débito foi criado com sucesso.
     */
    public List<String> criarDebito(Conta conta, String descricao, String data, String valorTexto, String categoria, String tipo) {
        List<String> erros = validarComuns(descricao, data, valorTexto);
        if (!erros.isEmpty()) {
            return erros;
        }
        try {
            new Debito(descricao, conta, data, Double.parseDouble(valorTexto), categoria, tipo);
            return List.of();
        } catch (ValidacaoException | RegraNegocioException e) {
            return List.of(e.getMessage());
        }
    }

    /**
     * Valida e cria um novo {@link Credito} na conta informada.
     *
     * @param conta     conta à qual o crédito pertence.
     * @param descricao descrição do crédito.
     * @param data      data do crédito, formato {@code yyyy-MM-dd}.
     * @param valorTexto valor do crédito, como texto (deve representar um número maior que zero).
     * @param categoria categoria do crédito ({@code "pagamento"} ou {@code "rendimento"}).
     * @return lista de mensagens de erro; vazia se o crédito foi criado com sucesso.
     */
    public List<String> criarCredito(Conta conta, String descricao, String data, String valorTexto, String categoria) {
        List<String> erros = validarComuns(descricao, data, valorTexto);
        if (!erros.isEmpty()) {
            return erros;
        }
        try {
            new Credito(descricao, conta, data, Double.parseDouble(valorTexto), categoria);
            return List.of();
        } catch (ValidacaoException e) {
            return List.of(e.getMessage());
        }
    }

    private List<String> validarComuns(String descricao, String data, String valorTexto) {
        List<String> erros = new ArrayList<>();
        if (descricao == null || descricao.trim().isEmpty()) {
            erros.add("Descrição é obrigatória.");
        }
        if (!ValidadoresFormulario.dataIsoValida(data)) {
            erros.add("Data deve estar no formato AAAA-MM-DD.");
        }
        if (!ValidadoresFormulario.numeroValido(valorTexto)) {
            erros.add("Valor deve ser um número.");
        } else if (!ValidadoresFormulario.valorPositivo(valorTexto)) {
            erros.add("Valor deve ser maior que zero.");
        }
        return erros;
    }

    /**
     * Valida e atualiza os dados de uma transação existente.
     *
     * @param transacao  transação a ser atualizada.
     * @param descricao  nova descrição.
     * @param data       nova data, formato {@code yyyy-MM-dd}.
     * @param valorTexto novo valor, como texto (deve representar um número maior que zero).
     * @param categoria  nova categoria.
     * @return lista de mensagens de erro; vazia se a transação foi atualizada com sucesso.
     */
    public List<String> atualizar(Transacao transacao, String descricao, String data, String valorTexto, String categoria) {
        List<String> erros = validarComuns(descricao, data, valorTexto);
        if (!erros.isEmpty()) {
            return erros;
        }
        try {
            transacao.setDescricao(descricao);
            transacao.setData(data);
            transacao.setValor(Double.parseDouble(valorTexto));
            transacao.setCategoria(categoria);
            return List.of();
        } catch (ValidacaoException e) {
            return List.of(e.getMessage());
        }
    }

    /**
     * Remove a transação informada da conta, mediante confirmação do
     * usuário.
     *
     * @param transacao   transação a ser removida.
     * @param conta       conta à qual a transação pertence.
     * @param confirmacao fornece {@code true} se o usuário confirmou a remoção.
     * @return {@code true} se a transação foi removida (via
     *         {@link Conta#removerTransacao(int)}); {@code false} se a
     *         remoção foi cancelada ou se a transação não foi encontrada.
     */
    public boolean remover(Transacao transacao, Conta conta, BooleanSupplier confirmacao) {
        if (!confirmacao.getAsBoolean()) {
            return false;
        }
        return conta.removerTransacao(transacao.getId());
    }
}
