package com.cfpi.apresentacao.designsystem;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * Ponto único para a regra de sistema "sempre apresente uma confirmação para
 * operações de remoção".
 */
public final class ConfirmacaoDialog {

    private ConfirmacaoDialog() {}

    /**
     * Exibe um diálogo de confirmação de remoção.
     *
     * @param pai      componente pai do diálogo (pode ser {@code null}).
     * @param mensagem mensagem de confirmação exibida ao usuário.
     * @return {@code true} se o usuário confirmar a remoção, {@code false}
     *         caso contrário (incluindo se o diálogo for fechado sem
     *         escolha).
     */
    public static boolean confirmar(Component pai, String mensagem) {
        int opcao = JOptionPane.showConfirmDialog(pai, mensagem, "Confirmar remoção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return opcao == JOptionPane.YES_OPTION;
    }
}
