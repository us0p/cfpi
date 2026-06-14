package com.cfpi.apresentacao.designsystem;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import javax.swing.JComponent;

/**
 * Componente que desenha um donut de progresso circular com um rótulo
 * percentual centralizado, usado pelo card de "limite de crédito consumido".
 */
public class CircularProgressDonut extends JComponent {

    private double percentual;
    private Color corProgresso;
    private Color corTrilha;
    private final int espessura;

    public CircularProgressDonut(Color corProgresso, Color corTrilha, int espessura) {
        this.percentual = 0.0;
        this.corProgresso = corProgresso;
        this.corTrilha = corTrilha;
        this.espessura = espessura;
        setPreferredSize(new Dimension(120, 120));
    }

    public double getPercentual() {
        return percentual;
    }

    /**
     * Define o percentual de progresso exibido pelo donut.
     *
     * <p>Valores fora do intervalo {@code [0, 1]} são fixados (clamp) nos
     * limites: valores menores que {@code 0} viram {@code 0} e valores
     * maiores que {@code 1} viram {@code 1}.</p>
     *
     * @param percentual novo percentual de progresso.
     */
    public void setPercentual(double percentual) {
        this.percentual = Math.max(0.0, Math.min(1.0, percentual));
        repaint();
    }

    public Color getCorProgresso() {
        return corProgresso;
    }

    public void setCorProgresso(Color corProgresso) {
        this.corProgresso = corProgresso;
        repaint();
    }

    public Color getCorTrilha() {
        return corTrilha;
    }

    public void setCorTrilha(Color corTrilha) {
        this.corTrilha = corTrilha;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int tamanho = Math.min(getWidth(), getHeight());
        int x = (getWidth() - tamanho) / 2;
        int y = (getHeight() - tamanho) / 2;

        g2.setStroke(new BasicStroke(espessura, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        double diametro = tamanho - espessura;
        double offset = espessura / 2.0;

        g2.setColor(corTrilha);
        g2.draw(new Ellipse2D.Double(x + offset, y + offset, diametro, diametro));

        g2.setColor(corProgresso);
        double angulo = 360.0 * percentual;
        g2.draw(new Arc2D.Double(x + offset, y + offset, diametro, diametro, 90, -angulo, Arc2D.OPEN));

        String texto = Math.round(percentual * 100) + "%";
        g2.setFont(Fontes.SUBTITULO);
        FontMetrics fm = g2.getFontMetrics();
        int textoX = x + (tamanho - fm.stringWidth(texto)) / 2;
        int textoY = y + (tamanho + fm.getAscent() - fm.getDescent()) / 2;
        g2.setColor(Cores.TEXTO_PRIMARIO);
        g2.drawString(texto, textoX, textoY);

        g2.dispose();
    }
}
