package com.cfpi.apresentacao.designsystem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

/**
 * Etiqueta com fundo arredondado em forma de pílula, usada para indicar o
 * tipo de uma conta, ativo ou operação (equivalente a {@code .chip} /
 * {@code .chip-fern} / {@code .chip-rose} do design).
 */
public class Chip extends JLabel {

    private final Color corFundo;

    public Chip(String texto, Color corFundo, Color corTexto) {
        super(texto);
        this.corFundo = corFundo;
        setOpaque(false);
        setForeground(corTexto);
        setFont(Fontes.PEQUENO_NEGRITO);
        setBorder(new EmptyBorder(4, 12, 4, 12));
    }

    public static Chip neutro(String texto) {
        return new Chip(texto, Cores.TAUPE_GREY_08, Cores.TEXTO_PRIMARIO);
    }

    public static Chip fern(String texto) {
        return new Chip(texto, Cores.FERN_12, Cores.FERN);
    }

    public static Chip rose(String texto) {
        return new Chip(texto, Cores.SMOKY_ROSE_12, Cores.SMOKY_ROSE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(corFundo);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
        g2.dispose();
        super.paintComponent(g);
    }
}
