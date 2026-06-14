package com.cfpi.apresentacao.designsystem;

import java.awt.Component;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Ponto único para a regra de sistema "sempre exiba os erros de validação de
 * um formulário em um diálogo modal, mantendo o formulário aberto para
 * correção".
 */
public final class ErroValidacaoDialog {

    private ErroValidacaoDialog() {}

    /**
     * Exibe um diálogo de erro de validação com as mensagens informadas.
     *
     * @param pai   componente pai do diálogo (pode ser {@code null}).
     * @param erros lista de mensagens de erro a serem exibidas, uma por linha.
     */
    public static void exibir(Component pai, List<String> erros) {
        String mensagem = String.join("\n", erros);
        JOptionPane.showMessageDialog(pai, mensagem, "Erro de validação", JOptionPane.ERROR_MESSAGE);
    }
}
