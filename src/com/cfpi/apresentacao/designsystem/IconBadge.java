package com.cfpi.apresentacao.designsystem;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import javax.swing.JComponent;

/**
 * Badge circular com uma seta para cima ou para baixo e um anel de 3px na cor
 * da seta, usado para indicar transações de crédito/débito nas listas.
 */
public class IconBadge extends JComponent {

    private static final int ESPESSURA_ANEL = 3;

    public enum Direcao { CIMA, BAIXO }

    private final Direcao direcao;
    private final Color corFundo;
    private final Color corSeta;

    public IconBadge(Direcao direcao, Color corFundo, Color corSeta) {
        this.direcao = direcao;
        this.corFundo = corFundo;
        this.corSeta = corSeta;
        setPreferredSize(new Dimension(48, 48));
    }

    public static IconBadge credito() {
        return new IconBadge(Direcao.CIMA, Cores.CREDITO_FUNDO, Cores.CREDITO);
    }

    public static IconBadge debito() {
        return new IconBadge(Direcao.BAIXO, Cores.DEBITO_FUNDO, Cores.DEBITO);
    }

    public Direcao getDirecao() {
        return direcao;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int largura = getWidth();
        int altura = getHeight();

        g2.setColor(corFundo);
        g2.fill(new Ellipse2D.Double(0, 0, largura, altura));

        g2.setColor(corSeta);
        g2.setStroke(new BasicStroke(ESPESSURA_ANEL));
        double meiaEspessura = ESPESSURA_ANEL / 2.0;
        g2.draw(new Ellipse2D.Double(meiaEspessura, meiaEspessura, largura - ESPESSURA_ANEL, altura - ESPESSURA_ANEL));

        double cx = largura / 2.0;
        double cy = altura / 2.0;
        double tamanhoSeta = Math.min(largura, altura) * 0.3;

        Path2D seta = new Path2D.Double();
        if (direcao == Direcao.CIMA) {
            seta.moveTo(cx, cy - tamanhoSeta / 2);
            seta.lineTo(cx - tamanhoSeta / 2, cy + tamanhoSeta / 2);
            seta.lineTo(cx + tamanhoSeta / 2, cy + tamanhoSeta / 2);
        } else {
            seta.moveTo(cx, cy + tamanhoSeta / 2);
            seta.lineTo(cx - tamanhoSeta / 2, cy - tamanhoSeta / 2);
            seta.lineTo(cx + tamanhoSeta / 2, cy - tamanhoSeta / 2);
        }
        seta.closePath();

        g2.setColor(corSeta);
        g2.fill(seta);

        g2.dispose();
    }
}
