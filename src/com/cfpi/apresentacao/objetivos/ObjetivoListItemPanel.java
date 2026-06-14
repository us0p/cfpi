package com.cfpi.apresentacao.objetivos;

import com.cfpi.apresentacao.comum.Formatadores;
import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.Espacamentos;
import com.cfpi.apresentacao.designsystem.Fontes;
import com.cfpi.apresentacao.designsystem.IconButton;
import com.cfpi.apresentacao.designsystem.RoundedPanel;
import com.cfpi.dominio.entidades.objetivo.Objetivo;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Linha de exibição de um {@link Objetivo}: alça de prioridade (arrastável
 * para reordenar), nome, valor-alvo e ações de editar e remover.
 */
public class ObjetivoListItemPanel extends RoundedPanel {

    private final Objetivo objetivo;
    private final JLabel labelAlca;
    private final IconButton botaoEditar;
    private final IconButton botaoRemover;

    public ObjetivoListItemPanel(Objetivo objetivo) {
        super(Espacamentos.RAIO, Cores.CARD_BRANCO);
        this.objetivo = objetivo;

        setLayout(new BorderLayout(Espacamentos.ESPACO_2, 0));
        setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_2, Espacamentos.ESPACO_2, Espacamentos.ESPACO_2, Espacamentos.ESPACO_2));

        labelAlca = new JLabel("⋮⋮");
        labelAlca.setFont(Fontes.SUBTITULO);
        labelAlca.setForeground(Cores.TEXTO_SECUNDARIO);
        labelAlca.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

        JLabel labelNome = new JLabel(objetivo.getNome());
        labelNome.setFont(Fontes.SUBTITULO);
        labelNome.setForeground(Cores.TEXTO_PRIMARIO);

        JLabel labelValor = new JLabel(Formatadores.formatarMoeda(objetivo.getValor()));
        labelValor.setFont(Fontes.DESTAQUE_NUMERICO);
        labelValor.setForeground(Cores.TEXTO_PRIMARIO);

        botaoEditar = IconButton.editar();
        botaoRemover = IconButton.remover();

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, Espacamentos.ESPACO_1, 0));
        acoes.setOpaque(false);
        acoes.add(botaoEditar);
        acoes.add(botaoRemover);

        JPanel direita = new JPanel();
        direita.setOpaque(false);
        direita.setLayout(new BoxLayout(direita, BoxLayout.X_AXIS));
        direita.add(labelValor);
        direita.add(Box.createHorizontalStrut(Espacamentos.ESPACO_2));
        direita.add(acoes);

        add(labelAlca, BorderLayout.WEST);
        add(labelNome, BorderLayout.CENTER);
        add(direita, BorderLayout.EAST);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }

    public Objetivo getObjetivo() {
        return objetivo;
    }

    public JLabel getLabelAlca() {
        return labelAlca;
    }

    public IconButton getBotaoEditar() {
        return botaoEditar;
    }

    public IconButton getBotaoRemover() {
        return botaoRemover;
    }
}
