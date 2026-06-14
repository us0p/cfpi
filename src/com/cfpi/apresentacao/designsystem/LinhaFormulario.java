package com.cfpi.apresentacao.designsystem;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Linha de formulário com rótulo em negrito acima do campo, usada nos
 * diálogos de criação/edição (ex: {@code TransacaoFormDialog},
 * {@code ContaFormDialog}, {@code InvestimentoFormDialog}).
 */
public final class LinhaFormulario {

    private LinhaFormulario() {}

    /**
     * Cria uma linha de formulário com {@code rotulo} acima de {@code campo}.
     *
     * @param rotulo rótulo do campo (ex: "Conta").
     * @param campo  componente de entrada (ex: {@code JComboBox}, {@code RoundedTextField}).
     * @return painel com o rótulo e o campo, pronto para ser adicionado ao formulário.
     */
    public static JPanel criar(String rotulo, JComponent campo) {
        JPanel linha = new JPanel(new BorderLayout(0, 4));
        linha.setOpaque(false);
        JLabel label = new JLabel(rotulo);
        label.setFont(Fontes.CORPO_NEGRITO);
        label.setForeground(Cores.TEXTO_PRIMARIO);
        linha.add(label, BorderLayout.NORTH);
        linha.add(campo, BorderLayout.CENTER);
        return linha;
    }
}
