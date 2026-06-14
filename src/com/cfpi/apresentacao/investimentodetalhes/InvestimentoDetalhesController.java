package com.cfpi.apresentacao.investimentodetalhes;

import com.cfpi.apresentacao.comum.AvaliadorDeAtivos;
import com.cfpi.apresentacao.comum.ValidadoresFormulario;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.investimento.Investimento;
import com.cfpi.dominio.entidades.usuario.Usuario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Orquestra o carregamento, atualização e remoção das operações de um único
 * ativo.
 */
public class InvestimentoDetalhesController {

    private final Usuario usuario;
    private final AvaliadorDeAtivos avaliador;
    private final InvestimentoDetalhesViewModel viewModel;

    public InvestimentoDetalhesController(Usuario usuario, AvaliadorDeAtivos avaliador, InvestimentoDetalhesViewModel viewModel) {
        this.usuario = usuario;
        this.avaliador = avaliador;
        this.viewModel = viewModel;
    }

    /**
     * Reúne, de todas as contas do usuário, as operações pertencentes ao
     * ativo informado.
     *
     * @param nomeAtivo nome/identificador do ativo.
     * @param tipo      subtipo concreto de {@link Investimento}.
     * @return as operações do ativo informado.
     */
    public List<Investimento> carregar(String nomeAtivo, Class<? extends Investimento> tipo) {
        List<Investimento> todos = new ArrayList<>();
        for (Conta conta : usuario.getContas()) {
            todos.addAll(Arrays.asList(conta.getInvestimentos()));
        }
        return viewModel.operacoesDoAtivo(todos.toArray(new Investimento[0]), nomeAtivo, tipo);
    }

    public String getDataPrimeiraCompra(List<Investimento> operacoes) {
        return viewModel.dataPrimeiraCompra(operacoes);
    }

    public double getTotalInvestido(List<Investimento> operacoes) {
        return viewModel.totalInvestido(operacoes);
    }

    public double getQuantidadeAtual(List<Investimento> operacoes) {
        return viewModel.quantidadeAtual(operacoes);
    }

    /**
     * @param nomeAtivo       nome/identificador do ativo.
     * @param tipo            subtipo concreto de {@link Investimento}.
     * @param quantidadeAtual quantidade atual do ativo.
     * @return o valor de mercado atual do ativo, calculado pelo
     *         {@link AvaliadorDeAtivos} injetado.
     */
    public double getValorAtual(String nomeAtivo, Class<? extends Investimento> tipo, double quantidadeAtual) {
        return avaliador.valorAtual(nomeAtivo, tipo, quantidadeAtual);
    }

    /**
     * Valida e atualiza os dados de uma operação existente.
     *
     * @param investimento    operação a ser atualizada.
     * @param nomeAtivo       novo nome/identificador do ativo.
     * @param valorTexto      novo preço unitário, como texto (deve representar um número maior que zero).
     * @param quantidadeTexto nova quantidade, como texto (deve representar um número maior que zero).
     * @param data            nova data da operação, formato {@code yyyy-MM-dd}.
     * @param operacao        novo tipo da operação ({@code "compra"} ou {@code "venda"}).
     * @return lista de mensagens de erro; vazia se a operação foi atualizada com sucesso.
     */
    public List<String> atualizar(Investimento investimento, String nomeAtivo, String valorTexto, String quantidadeTexto, String data, String operacao) {
        List<String> erros = validar(nomeAtivo, valorTexto, quantidadeTexto, data, operacao);
        if (!erros.isEmpty()) {
            return erros;
        }
        investimento.setNomeAtivo(nomeAtivo);
        investimento.setValor(Double.parseDouble(valorTexto));
        investimento.setQuantidade(Double.parseDouble(quantidadeTexto));
        investimento.setData(data);
        investimento.setOperacao(operacao);
        return List.of();
    }

    private List<String> validar(String nomeAtivo, String valorTexto, String quantidadeTexto, String data, String operacao) {
        List<String> erros = new ArrayList<>();
        if (nomeAtivo == null || nomeAtivo.trim().isEmpty()) {
            erros.add("Nome do ativo é obrigatório.");
        }
        if (!ValidadoresFormulario.valorPositivo(valorTexto)) {
            erros.add("Valor deve ser maior que zero.");
        }
        if (!ValidadoresFormulario.valorPositivo(quantidadeTexto)) {
            erros.add("Quantidade deve ser maior que zero.");
        }
        if (!ValidadoresFormulario.dataIsoValida(data)) {
            erros.add("Data deve estar no formato AAAA-MM-DD.");
        }
        if (!ValidadoresFormulario.operacaoValida(operacao)) {
            erros.add("Operação deve ser \"compra\" ou \"venda\".");
        }
        return erros;
    }

    /**
     * Remove a operação informada, mediante confirmação do usuário.
     *
     * @param investimento operação a ser removida.
     * @param confirmacao  fornece {@code true} se o usuário confirmou a remoção.
     * @return {@code true} se a operação foi removida (via
     *         {@link Conta#removerInvestimento(int)}); {@code false} se a
     *         remoção foi cancelada, se a operação não estiver associada a
     *         uma conta, ou se a operação não foi encontrada.
     */
    public boolean remover(Investimento investimento, BooleanSupplier confirmacao) {
        if (!confirmacao.getAsBoolean()) {
            return false;
        }
        Conta conta = investimento.getConta();
        if (conta == null) {
            return false;
        }
        return conta.removerInvestimento(investimento.getId());
    }

    /**
     * Pesquisa, na conta informada, a operação de investimento com o
     * {@code id} informado.
     *
     * @param conta conta onde a operação será pesquisada.
     * @param id    id da operação procurada.
     * @return a {@link Investimento} correspondente, ou {@code null} se não
     *         encontrada.
     */
    public Investimento buscarPorId(Conta conta, int id) {
        return conta.pesquisarInvestimentoPorId(id);
    }
}
