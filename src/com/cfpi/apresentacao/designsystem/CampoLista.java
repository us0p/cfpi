package com.cfpi.apresentacao.designsystem;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Par rótulo/valor exibido em uma linha de lista, equivalente a
 * {@code .list-item-field} do design (rótulo em negrito seguido do valor).
 */
public final class CampoLista {

    private CampoLista() {}

    /**
     * Cria um par rótulo/valor para uma linha de lista.
     *
     * @param rotulo rótulo do campo (ex: "Categoria:").
     * @param valor  valor exibido após o rótulo.
     * @return painel com o par rótulo/valor, pronto para ser adicionado a um
     *         {@code list-item-main}.
     */
    public static JPanel criar(String rotulo, String valor) {
        JPanel campo = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        campo.setOpaque(false);

        JLabel labelRotulo = new JLabel(rotulo);
        labelRotulo.setFont(Fontes.MEDIO_NEGRITO);
        labelRotulo.setForeground(Cores.TEXTO_PRIMARIO);

        JLabel labelValor = new JLabel(valor);
        labelValor.setFont(Fontes.MEDIO);
        labelValor.setForeground(Cores.TEXTO_PRIMARIO);

        campo.add(labelRotulo);
        campo.add(labelValor);
        return campo;
    }
}
