package com.cfpi.apresentacao.designsystem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Campo de busca com ícone de lupa, equivalente a {@code .search-input} do
 * design (input com fundo branco, borda e ícone à esquerda).
 */
public class CampoBusca extends JPanel {

    private final JTextField campo;

    public CampoBusca(String dica) {
        setOpaque(false);
        setLayout(new BorderLayout(Espacamentos.ESPACO_1, 0));
        setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel icone = new JLabel("🔍");
        icone.setFont(Fontes.CORPO);
        icone.setForeground(Cores.TEXTO_SECUNDARIO);

        campo = new JTextField();
        campo.setOpaque(false);
        campo.setBorder(null);
        campo.setFont(Fontes.CORPO);
        campo.setForeground(Cores.TEXTO_PRIMARIO);
        campo.setPreferredSize(new Dimension(220, 20));
        if (dica != null) {
            campo.setToolTipText(dica);
        }

        add(icone, BorderLayout.WEST);
        add(campo, BorderLayout.CENTER);
    }

    public JTextField getCampo() {
        return campo;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Cores.CARD_BRANCO);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1.0, getHeight() - 1.0, Espacamentos.RAIO, Espacamentos.RAIO));
        g2.setColor(Cores.BORDA);
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1.0, getHeight() - 1.0, Espacamentos.RAIO, Espacamentos.RAIO));
        g2.dispose();
        super.paintComponent(g);
    }
}
