package com.cfpi.apresentacao.investimentos;

import com.cfpi.apresentacao.comum.AvaliadorDeAtivos;
import com.cfpi.apresentacao.comum.ValidadoresFormulario;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.investimento.Acao;
import com.cfpi.dominio.entidades.investimento.CDB;
import com.cfpi.dominio.entidades.investimento.CRA;
import com.cfpi.dominio.entidades.investimento.CRI;
import com.cfpi.dominio.entidades.investimento.Cripto;
import com.cfpi.dominio.entidades.investimento.DEB;
import com.cfpi.dominio.entidades.investimento.FII;
import com.cfpi.dominio.entidades.investimento.Investimento;
import com.cfpi.dominio.entidades.investimento.LCA;
import com.cfpi.dominio.entidades.investimento.LCI;
import com.cfpi.dominio.entidades.investimento.PGBL;
import com.cfpi.dominio.entidades.investimento.TesouroDireto;
import com.cfpi.dominio.entidades.investimento.VGBL;
import com.cfpi.dominio.entidades.usuario.Usuario;
import com.cfpi.dominio.excecoes.RegraNegocioException;
import com.cfpi.dominio.excecoes.ValidacaoException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Orquestra o carregamento e a criação de operações de investimento.
 */
public class InvestimentosController {

    private final Usuario usuario;
    private final AvaliadorDeAtivos avaliador;
    private final InvestimentosViewModel viewModel;

    public InvestimentosController(Usuario usuario, AvaliadorDeAtivos avaliador, InvestimentosViewModel viewModel) {
        this.usuario = usuario;
        this.avaliador = avaliador;
        this.viewModel = viewModel;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Reúne as operações de investimento de todas as contas do usuário e as
     * agrupa por ativo.
     *
     * @return um {@link AtivoResumo} por ativo do usuário.
     */
    public List<AtivoResumo> carregar() {
        List<Investimento> todos = new ArrayList<>();
        for (Conta conta : usuario.getContas()) {
            todos.addAll(Arrays.asList(conta.getInvestimentos()));
        }
        return viewModel.agruparPorAtivo(todos.toArray(new Investimento[0]), avaliador);
    }

    /**
     * Filtra os ativos do usuário cujo {@code nomeAtivo} contenha o termo
     * informado.
     *
     * @param termo termo de busca; se {@code null} ou em branco, todos os
     *              ativos são retornados.
     * @return ativos correspondentes ao termo de busca.
     */
    public List<AtivoResumo> filtrarPorNome(String termo) {
        return viewModel.filtrarPorNome(carregar(), termo);
    }

    /**
     * Valida e registra uma nova operação de investimento na conta
     * informada.
     *
     * @param tipo            subtipo de {@link Investimento} a ser criado.
     * @param nomeAtivo       nome/identificador do ativo.
     * @param valorTexto      preço unitário do ativo, como texto (deve representar um número maior que zero).
     * @param conta           conta à qual a operação pertence.
     * @param quantidadeTexto quantidade de unidades do ativo, como texto (deve representar um número maior que zero).
     * @param data            data da operação, formato {@code yyyy-MM-dd}.
     * @param operacao        tipo da operação ({@code "compra"} ou {@code "venda"}).
     * @return lista de mensagens de erro; vazia se a operação foi criada com sucesso.
     */
    public List<String> criar(Class<? extends Investimento> tipo, String nomeAtivo, String valorTexto, Conta conta, String quantidadeTexto, String data, String operacao) {
        List<String> erros = validar(nomeAtivo, valorTexto, quantidadeTexto, data, operacao, conta);
        if (!erros.isEmpty()) {
            return erros;
        }
        try {
            criarInvestimento(tipo, nomeAtivo, Double.parseDouble(valorTexto), conta, Double.parseDouble(quantidadeTexto), data, operacao);
            return List.of();
        } catch (ValidacaoException | RegraNegocioException e) {
            return List.of(e.getMessage());
        }
    }

    private void criarInvestimento(Class<? extends Investimento> tipo, String nomeAtivo, double valor, Conta conta, double quantidade, String data, String operacao) {
        if (tipo == Acao.class) {
            new Acao(nomeAtivo, valor, conta, quantidade, 0.0, data, 0.0, operacao);
        } else if (tipo == CDB.class) {
            new CDB(nomeAtivo, valor, conta, quantidade, 0.0, data, 0.0, operacao);
        } else if (tipo == CRA.class) {
            new CRA(nomeAtivo, valor, conta, quantidade, 0.0, data, 0.0, operacao);
        } else if (tipo == CRI.class) {
            new CRI(nomeAtivo, valor, conta, quantidade, 0.0, data, 0.0, operacao);
        } else if (tipo == Cripto.class) {
            new Cripto(nomeAtivo, valor, conta, quantidade, 0.0, data, 0.0, operacao);
        } else if (tipo == DEB.class) {
            new DEB(nomeAtivo, valor, conta, quantidade, 0.0, data, 0.0, operacao);
        } else if (tipo == FII.class) {
            new FII(nomeAtivo, valor, conta, quantidade, 0.0, data, 0.0, operacao);
        } else if (tipo == LCA.class) {
            new LCA(nomeAtivo, valor, conta, quantidade, 0.0, data, 0.0, operacao);
        } else if (tipo == LCI.class) {
            new LCI(nomeAtivo, valor, conta, quantidade, 0.0, data, 0.0, operacao);
        } else if (tipo == PGBL.class) {
            new PGBL(nomeAtivo, valor, conta, quantidade, 0.0, data, 0.0, operacao);
        } else if (tipo == TesouroDireto.class) {
            new TesouroDireto(nomeAtivo, valor, conta, quantidade, 0.0, data, 0.0, operacao);
        } else if (tipo == VGBL.class) {
            new VGBL(nomeAtivo, valor, conta, quantidade, 0.0, data, 0.0, operacao);
        } else {
            throw new IllegalArgumentException("Tipo de investimento desconhecido: " + tipo);
        }
    }

    private List<String> validar(String nomeAtivo, String valorTexto, String quantidadeTexto, String data, String operacao, Conta conta) {
        List<String> erros = new ArrayList<>();
        if (nomeAtivo == null || nomeAtivo.trim().isEmpty()) {
            erros.add("Nome do ativo é obrigatório.");
        }
        boolean valorValido = ValidadoresFormulario.valorPositivo(valorTexto);
        if (!valorValido) {
            erros.add("Valor deve ser maior que zero.");
        }
        boolean quantidadeValida = ValidadoresFormulario.valorPositivo(quantidadeTexto);
        if (!quantidadeValida) {
            erros.add("Quantidade deve ser maior que zero.");
        }
        if (!ValidadoresFormulario.dataIsoValida(data)) {
            erros.add("Data deve estar no formato AAAA-MM-DD.");
        }
        if (!ValidadoresFormulario.operacaoValida(operacao)) {
            erros.add("Operação deve ser \"compra\" ou \"venda\".");
        }
        String operacaoNormalizada = operacao == null ? "" : operacao.trim().toLowerCase();
        if (operacaoNormalizada.equals("compra") && valorValido && quantidadeValida && conta != null) {
            double total = Double.parseDouble(valorTexto) * Double.parseDouble(quantidadeTexto);
            if (total > conta.getValorConta()) {
                erros.add("Saldo insuficiente para esta operação.");
            }
        }
        return erros;
    }
}
