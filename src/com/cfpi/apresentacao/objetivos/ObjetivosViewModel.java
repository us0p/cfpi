package com.cfpi.apresentacao.objetivos;

import com.cfpi.dominio.entidades.objetivo.Objetivo;

import java.util.ArrayList;
import java.util.List;

/**
 * Filtro e reordenação (em memória, restritos à sessão de UI) da lista de
 * objetivos.
 */
public class ObjetivosViewModel {

    /**
     * Filtra os objetivos cujo nome contém o termo informado.
     *
     * @param objetivos objetivos a serem filtrados.
     * @param termo     termo de busca, comparado de forma
     *                  case-insensitive com {@link Objetivo#getNome()}; se
     *                  {@code null} ou em branco, nenhum filtro é aplicado.
     * @return nova lista contendo apenas os objetivos cujo nome contém
     *         {@code termo} (ou todos, se {@code termo} for {@code null} ou
     *         em branco).
     */
    public List<Objetivo> filtrarPorNome(List<Objetivo> objetivos, String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return new ArrayList<>(objetivos);
        }
        String alvo = termo.trim().toLowerCase();
        List<Objetivo> filtrados = new ArrayList<>();
        for (Objetivo objetivo : objetivos) {
            if (objetivo.getNome() != null && objetivo.getNome().toLowerCase().contains(alvo)) {
                filtrados.add(objetivo);
            }
        }
        return filtrados;
    }

    /**
     * Move um objetivo de uma posição para outra dentro da lista.
     *
     * @param objetivos lista de objetivos, na ordem atual.
     * @param origem    índice atual do objetivo a ser movido.
     * @param destino   índice de destino do objetivo.
     * @return nova lista com o objetivo movido de {@code origem} para
     *         {@code destino}; se {@code origem} ou {@code destino}
     *         estiverem fora do intervalo {@code [0, objetivos.size())},
     *         retorna uma cópia de {@code objetivos} sem alterações.
     */
    public List<Objetivo> mover(List<Objetivo> objetivos, int origem, int destino) {
        List<Objetivo> resultado = new ArrayList<>(objetivos);
        if (origem < 0 || origem >= resultado.size() || destino < 0 || destino >= resultado.size()) {
            return resultado;
        }
        Objetivo item = resultado.remove(origem);
        resultado.add(destino, item);
        return resultado;
    }
}
