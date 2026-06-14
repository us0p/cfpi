package com.cfpi.apresentacao.designsystem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingConstants;

/**
 * Botão de navegação da {@code Sidebar}, com destaque visual quando
 * selecionado, equivalente a {@code .sidebar-nav-item} do design. A variante
 * {@link #rodape(String)} corresponde a {@code .sidebar-footer
 * .sidebar-nav-item} (texto centralizado, com borda).
 */
public class SidebarButton extends JButton {

    private static final Color BORDA_RODAPE = new Color(0xFF, 0xFF, 0xFF, 64);

    private final boolean estiloRodape;
    private boolean selecionado;

    public SidebarButton(String texto) {
        this(texto, false);
    }

    private SidebarButton(String texto, boolean estiloRodape) {
        super(texto);
        this.estiloRodape = estiloRodape;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Cores.BRANCO);
        setFont(Fontes.CORPO_NEGRITO);
        setHorizontalAlignment(estiloRodape ? SwingConstants.CENTER : SwingConstants.LEFT);
        setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_1, Espacamentos.ESPACO_2, Espacamentos.ESPACO_1, Espacamentos.ESPACO_2));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        this.selecionado = false;
    }

    /**
     * Cria um botão no estilo do rodapé da sidebar (texto centralizado, com
     * borda 1px translúcida).
     */
    public static SidebarButton rodape(String texto) {
        return new SidebarButton(texto, true);
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
        setForeground(selecionado ? Cores.TEXTO_PRIMARIO : Cores.BRANCO);
        repaint();
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (selecionado) {
            g2.setColor(Cores.FUNDO_PRINCIPAL);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), Espacamentos.RAIO, Espacamentos.RAIO));
        }
        if (estiloRodape) {
            g2.setColor(BORDA_RODAPE);
            g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 1.0, getHeight() - 1.0, Espacamentos.RAIO, Espacamentos.RAIO));
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
