package com.cfpi.apresentacao.designsystem;

import java.awt.Component;
import java.util.function.Function;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * Fábricas de {@code ListCellRenderer} para {@code JComboBox}, usadas para
 * exibir um texto derivado do valor selecionado (ex: o número de uma
 * {@code Conta}, o nome de um {@code Banco}).
 */
public final class Renderers {

    private Renderers() {}

    /**
     * Cria um renderer que exibe {@code texto.apply(valor)} quando o valor
     * for uma instância de {@code tipo}, ou {@code textoNulo} para qualquer
     * outro valor (incluindo {@code null}).
     *
     * @param tipo      tipo esperado do valor selecionado.
     * @param texto     função que extrai o texto a ser exibido a partir do valor.
     * @param textoNulo texto exibido quando o valor não for uma instância de {@code tipo}.
     * @return renderer pronto para ser passado a {@code JComboBox.setRenderer}.
     */
    public static <T> DefaultListCellRenderer exibindo(Class<T> tipo, Function<T, String> texto, String textoNulo) {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                String exibido = tipo.isInstance(value) ? texto.apply(tipo.cast(value)) : textoNulo;
                return super.getListCellRendererComponent(list, exibido, index, isSelected, cellHasFocus);
            }
        };
    }
}
