package com.cfpi.apresentacao.designsystem;

import java.awt.Component;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Campo de filtro com rótulo acima do combo, usado nas barras de filtro das
 * telas de listagem (ex: {@code TransacoesView}, {@code InvestimentosView}).
 */
public final class CampoFiltro {

    private CampoFiltro() {}

    /**
     * Cria um campo de filtro com {@code rotulo} acima de {@code combo}.
     *
     * @param rotulo rótulo do filtro (ex: "Tipo", "Categoria").
     * @param combo  combo de seleção do filtro.
     * @return painel com o rótulo e o combo, pronto para ser adicionado à
     *         barra de filtros.
     */
    public static JPanel criar(String rotulo, JComboBox<?> combo) {
        JPanel campo = new JPanel();
        campo.setOpaque(false);
        campo.setLayout(new BoxLayout(campo, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(rotulo);
        label.setFont(Fontes.PEQUENO_NEGRITO);
        label.setForeground(Cores.TEXTO_PRIMARIO);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        combo.setAlignmentX(Component.LEFT_ALIGNMENT);

        campo.add(label);
        campo.add(Box.createVerticalStrut(4));
        campo.add(combo);
        return campo;
    }
}
