package com.cfpi.apresentacao.designsystem;

import java.util.List;
import java.util.function.Function;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Utilitário para popular painéis de lista (ex: lista de transações, contas,
 * objetivos), aplicando o espaçamento vertical padrão entre os itens.
 */
public final class ListaPanelUtil {

    private ListaPanelUtil() {}

    /**
     * Adiciona a {@code painel} um componente para cada item de {@code itens},
     * seguido de um espaçamento vertical de {@code espaco} pixels. Não
     * remove o conteúdo existente nem revalida/repinta o painel.
     *
     * @param painel    painel ao qual os itens serão adicionados.
     * @param itens     itens a serem exibidos.
     * @param criarItem cria o componente correspondente a cada item.
     * @param espaco    espaçamento vertical, em pixels, após cada item.
     */
    public static <T> void adicionarItens(JPanel painel, List<T> itens, Function<T, ? extends JComponent> criarItem, int espaco) {
        for (T item : itens) {
            painel.add(criarItem.apply(item));
            painel.add(Box.createVerticalStrut(espaco));
        }
    }

    /**
     * Remove todo o conteúdo de {@code painel}, adiciona um componente para
     * cada item de {@code itens} (ver {@link #adicionarItens}) e revalida/repinta
     * o painel.
     *
     * @param painel    painel a ser repopulado.
     * @param itens     itens a serem exibidos.
     * @param criarItem cria o componente correspondente a cada item.
     * @param espaco    espaçamento vertical, em pixels, após cada item.
     */
    public static <T> void repopular(JPanel painel, List<T> itens, Function<T, ? extends JComponent> criarItem, int espaco) {
        painel.removeAll();
        adicionarItens(painel, itens, criarItem, espaco);
        painel.revalidate();
        painel.repaint();
    }
}
