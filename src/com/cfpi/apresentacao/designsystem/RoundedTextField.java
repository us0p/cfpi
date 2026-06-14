package com.cfpi.apresentacao.designsystem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * {@link JTextField} com fundo e borda desenhados como um retângulo de
 * cantos arredondados.
 */
public class RoundedTextField extends JTextField {

    private int raio;
    private Color corFundo;
    private Color corBorda;

    public RoundedTextField(int raio, Color corFundo, Color corBorda) {
        this.raio = raio;
        this.corFundo = corFundo;
        this.corBorda = corBorda;
        setOpaque(false);
        setBorder(new EmptyBorder(8, 12, 8, 12));
        setFont(Fontes.CORPO);
    }

    public int getRaio() {
        return raio;
    }

    public void setRaio(int raio) {
        this.raio = raio;
        repaint();
    }

    public Color getCorFundo() {
        return corFundo;
    }

    public void setCorFundo(Color corFundo) {
        this.corFundo = corFundo;
        repaint();
    }

    public Color getCorBorda() {
        return corBorda;
    }

    public void setCorBorda(Color corBorda) {
        this.corBorda = corBorda;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(corFundo);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1.0, getHeight() - 1.0, raio, raio));
        if (corBorda != null) {
            g2.setColor(corBorda);
            g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1.0, getHeight() - 1.0, raio, raio));
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
