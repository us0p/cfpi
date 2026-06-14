package com.cfpi.apresentacao.objetivos;

import com.cfpi.apresentacao.comum.ValidadoresFormulario;
import com.cfpi.dominio.entidades.objetivo.Objetivo;
import com.cfpi.dominio.entidades.usuario.Usuario;
import com.cfpi.dominio.excecoes.RegraNegocioException;
import com.cfpi.dominio.excecoes.ValidacaoException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Orquestra o carregamento, criação, atualização, remoção e reordenação de
 * objetivos.
 *
 * <p>A ordem de exibição ({@code ordemSessao}) é mantida apenas em memória,
 * para a sessão de UI atual; remover ou reordenar um objetivo não altera
 * {@code usuario.getObjetivos()}.</p>
 */
public class ObjetivosController {

    private final Usuario usuario;
    private final ObjetivosViewModel viewModel;
    private final List<Objetivo> ordemSessao;

    public ObjetivosController(Usuario usuario, ObjetivosViewModel viewModel) {
        this.usuario = usuario;
        this.viewModel = viewModel;
        this.ordemSessao = new ArrayList<>(Arrays.asList(usuario.getObjetivos()));
    }

    /**
     * @return a ordem de exibição atual dos objetivos.
     */
    public List<Objetivo> carregar() {
        return new ArrayList<>(ordemSessao);
    }

    /**
     * Valida e cria um novo objetivo para o usuário, adicionando-o ao final
     * da ordem de exibição.
     *
     * @param nome       nome do objetivo.
     * @param valorTexto valor-alvo do objetivo, como texto (deve
     *                   representar um número maior que zero).
     * @return lista de mensagens de erro; vazia se o objetivo foi criado
     *         com sucesso.
     */
    public List<String> criar(String nome, String valorTexto) {
        List<String> erros = validar(nome, valorTexto);
        if (!erros.isEmpty()) {
            return erros;
        }
        try {
            Objetivo objetivo = new Objetivo(nome, Double.parseDouble(valorTexto), usuario);
            ordemSessao.add(objetivo);
            return List.of();
        } catch (ValidacaoException | RegraNegocioException e) {
            return List.of(e.getMessage());
        }
    }

    /**
     * Valida e atualiza o nome e o valor-alvo de um objetivo existente.
     *
     * @param objetivo      objetivo a ser atualizado.
     * @param novoNome      novo nome do objetivo.
     * @param novoValorTexto novo valor-alvo, como texto (deve representar
     *                       um número maior que zero).
     * @return lista de mensagens de erro; vazia se o objetivo foi
     *         atualizado com sucesso.
     */
    public List<String> atualizar(Objetivo objetivo, String novoNome, String novoValorTexto) {
        List<String> erros = validar(novoNome, novoValorTexto);
        if (!erros.isEmpty()) {
            return erros;
        }
        try {
            objetivo.setNome(novoNome);
            objetivo.setValor(Double.parseDouble(novoValorTexto));
            return List.of();
        } catch (ValidacaoException | RegraNegocioException e) {
            return List.of(e.getMessage());
        }
    }

    /**
     * Remove o objetivo da ordem de exibição, mediante confirmação do
     * usuário.
     *
     * @param objetivo    objetivo a ser removido.
     * @param confirmacao fornece {@code true} se o usuário confirmou a
     *                    remoção.
     * @return {@code true} se o objetivo foi removido da ordem de exibição;
     *         {@code false} se a remoção foi cancelada ou se o objetivo não
     *         estava na ordem de exibição.
     */
    public boolean remover(Objetivo objetivo, BooleanSupplier confirmacao) {
        if (!confirmacao.getAsBoolean()) {
            return false;
        }
        return ordemSessao.remove(objetivo);
    }

    /**
     * Move um objetivo de uma posição para outra na ordem de exibição.
     *
     * @param origem  índice atual do objetivo a ser movido.
     * @param destino índice de destino do objetivo.
     * @return a nova ordem de exibição.
     */
    public List<Objetivo> mover(int origem, int destino) {
        List<Objetivo> novaOrdem = viewModel.mover(ordemSessao, origem, destino);
        ordemSessao.clear();
        ordemSessao.addAll(novaOrdem);
        return carregar();
    }

    /**
     * Move {@code arrastado} para a posição de {@code alvo} na ordem de
     * exibição, refletindo uma operação de arrastar-e-soltar na lista de
     * objetivos.
     *
     * @param arrastado objetivo que foi arrastado.
     * @param alvo      objetivo sobre o qual {@code arrastado} foi solto.
     * @return a nova ordem de exibição.
     */
    public List<Objetivo> moverPorArraste(Objetivo arrastado, Objetivo alvo) {
        int origem = ordemSessao.indexOf(arrastado);
        int destino = ordemSessao.indexOf(alvo);
        return mover(origem, destino);
    }

    /**
     * Filtra a ordem de exibição pelos objetivos cujo nome contém o termo
     * informado.
     *
     * @param termo termo de busca.
     * @return os objetivos da ordem de exibição cujo nome contém
     *         {@code termo}.
     */
    public List<Objetivo> filtrarPorNome(String termo) {
        return viewModel.filtrarPorNome(ordemSessao, termo);
    }

    /**
     * @return a ordem de exibição atual dos objetivos, para uso por outras
     *         telas (ex: {@code DashboardController}).
     */
    public List<Objetivo> getOrdemSessao() {
        return ordemSessao;
    }

    private List<String> validar(String nome, String valorTexto) {
        List<String> erros = new ArrayList<>();
        if (!ValidadoresFormulario.nomeValido(nome)) {
            erros.add("Nome deve ter ao menos 3 letras.");
        }
        if (!ValidadoresFormulario.numeroValido(valorTexto)) {
            erros.add("Valor deve ser um número.");
        } else if (!ValidadoresFormulario.valorPositivo(valorTexto)) {
            erros.add("Valor deve ser maior que zero.");
        }
        return erros;
    }
}
