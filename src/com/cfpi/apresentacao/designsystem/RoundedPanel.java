package com.cfpi.apresentacao.designsystem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

/**
 * {@link JPanel} com fundo desenhado como um retângulo de cantos
 * arredondados, usado como base visual dos cards do mockup.
 */
public class RoundedPanel extends JPanel {

    private int raio;
    private Color corFundo;

    public RoundedPanel(int raio, Color corFundo) {
        this.raio = raio;
        this.corFundo = corFundo;
        setOpaque(false);
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

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(corFundo);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), raio, raio));
        g2.dispose();
        super.paintComponent(g);
    }
}
