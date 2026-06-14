package com.cfpi.apresentacao.designsystem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;

/**
 * Barra de progresso em formato de pílula, equivalente a {@code .progress-bar}
 * / {@code .progress-bar-fill} do design (ex: limite de crédito utilizado).
 */
public class BarraProgresso extends JComponent {

    private final Color corPreenchimento;
    private final Color corTrilho;
    private double percentual;

    public BarraProgresso(Color corPreenchimento, Color corTrilho) {
        this.corPreenchimento = corPreenchimento;
        this.corTrilho = corTrilho;
        setOpaque(false);
        setPreferredSize(new Dimension(200, 8));
    }

    /**
     * Define o percentual preenchido da barra.
     *
     * @param percentual valor entre 0 e 1; valores fora desse intervalo são
     *                    fixados em 0 ou 1.
     */
    public void setPercentual(double percentual) {
        this.percentual = Math.max(0.0, Math.min(1.0, percentual));
        repaint();
    }

    public double getPercentual() {
        return percentual;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int largura = getWidth();
        int altura = getHeight();

        g2.setColor(corTrilho);
        g2.fill(new RoundRectangle2D.Double(0, 0, largura, altura, altura, altura));

        int larguraPreenchida = (int) Math.round(largura * percentual);
        if (larguraPreenchida > 0) {
            g2.setColor(corPreenchimento);
            g2.fill(new RoundRectangle2D.Double(0, 0, larguraPreenchida, altura, altura, altura));
        }

        g2.dispose();
    }
}
