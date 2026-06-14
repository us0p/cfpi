package com.cfpi.apresentacao.designsystem;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;

/**
 * Botão quadrado com borda arredondada e fundo transparente, usado para
 * ações de visualizar/editar/remover nas listas (equivalente a
 * {@code .btn-icon} / {@code .btn-icon--danger} do design). Os ícones são
 * desenhados em Java2D para não depender da disponibilidade de glifos
 * Unicode na fonte do sistema.
 */
public class IconButton extends JButton {

    private enum Icone {
        EDITAR, REMOVER, VISUALIZAR
    }

    private final Icone icone;

    private IconButton(Icone icone, Color corTexto) {
        super("");
        this.icone = icone;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(corTexto);
        setPreferredSize(new Dimension(36, 36));
        setMinimumSize(new Dimension(36, 36));
        setMaximumSize(new Dimension(36, 36));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static IconButton editar() {
        return new IconButton(Icone.EDITAR, Cores.TEXTO_PRIMARIO);
    }

    public static IconButton remover() {
        return new IconButton(Icone.REMOVER, Cores.DEBITO);
    }

    public static IconButton visualizar() {
        return new IconButton(Icone.VISUALIZAR, Cores.TEXTO_PRIMARIO);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (!isEnabled()) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }
        g2.setColor(Cores.BORDA);
        g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 1.0, getHeight() - 1.0, Espacamentos.RAIO, Espacamentos.RAIO));
        super.paintComponent(g2);

        g2.setColor(getForeground());
        g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        switch (icone) {
            case EDITAR -> desenharLapis(g2, cx, cy);
            case REMOVER -> desenharLixeira(g2, cx, cy);
            case VISUALIZAR -> desenharOlho(g2, cx, cy);
        }

        g2.dispose();
    }

    private void desenharLapis(Graphics2D g2, int cx, int cy) {
        Graphics2D g2Lapis = (Graphics2D) g2.create();
        g2Lapis.translate(cx, cy);
        g2Lapis.rotate(-Math.PI / 4);

        Path2D corpo = new Path2D.Double();
        corpo.moveTo(-7, -2.5);
        corpo.lineTo(4, -2.5);
        corpo.lineTo(4, 2.5);
        corpo.lineTo(-7, 2.5);
        corpo.closePath();
        g2Lapis.draw(corpo);

        Path2D ponta = new Path2D.Double();
        ponta.moveTo(4, -2.5);
        ponta.lineTo(8, 0);
        ponta.lineTo(4, 2.5);
        ponta.closePath();
        g2Lapis.fill(ponta);

        g2Lapis.draw(new Line2D.Double(-5, -2.5, -5, 2.5));
        g2Lapis.dispose();
    }

    private void desenharLixeira(Graphics2D g2, int cx, int cy) {
        g2.draw(new Line2D.Double(cx - 6, cy - 6, cx + 6, cy - 6));
        g2.draw(new RoundRectangle2D.Double(cx - 2, cy - 8.5, 4, 2.5, 1, 1));

        Path2D corpo = new Path2D.Double();
        corpo.moveTo(cx - 5, cy - 6);
        corpo.lineTo(cx - 4, cy + 6);
        corpo.lineTo(cx + 4, cy + 6);
        corpo.lineTo(cx + 5, cy - 6);
        corpo.closePath();
        g2.draw(corpo);

        g2.draw(new Line2D.Double(cx - 2, cy - 3, cx - 2, cy + 3.5));
        g2.draw(new Line2D.Double(cx, cy - 3, cx, cy + 3.5));
        g2.draw(new Line2D.Double(cx + 2, cy - 3, cx + 2, cy + 3.5));
    }

    private void desenharOlho(Graphics2D g2, int cx, int cy) {
        Path2D olho = new Path2D.Double();
        olho.moveTo(cx - 8, cy);
        olho.quadTo(cx, cy - 6, cx + 8, cy);
        olho.quadTo(cx, cy + 6, cx - 8, cy);
        olho.closePath();
        g2.draw(olho);
        g2.fill(new Ellipse2D.Double(cx - 2.2, cy - 2.2, 4.4, 4.4));
    }
}
