package com.cfpi.apresentacao.dashboard;

import com.cfpi.apresentacao.comum.Formatadores;
import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.Fontes;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;

/**
 * Gráfico de barras verticais com o total gasto por categoria.
 */
public class BarChartCategoriaPanel extends JComponent {

    private Map<String, Double> dados = new LinkedHashMap<>();

    public BarChartCategoriaPanel() {
        setPreferredSize(new Dimension(360, 220));
        ToolTipManager.sharedInstance().registerComponent(this);
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                atualizarTooltip(e.getPoint());
            }
        });
    }

    /**
     * Define os dados exibidos pelo gráfico.
     *
     * @param dados mapa de categoria para total gasto, na ordem em que as
     *              barras serão desenhadas.
     */
    public void setDados(Map<String, Double> dados) {
        this.dados = dados;
        setToolTipText(null);
        repaint();
    }

    public Map<String, Double> getDados() {
        return dados;
    }

    /**
     * Geometria de uma barra desenhada, junto com a categoria e o valor que
     * representa (usados para a tooltip ao passar o mouse).
     */
    private static final class Barra {
        final Rectangle area;
        final String categoria;
        final double valor;

        Barra(Rectangle area, String categoria, double valor) {
            this.area = area;
            this.categoria = categoria;
            this.valor = valor;
        }
    }

    private void atualizarTooltip(java.awt.Point mouse) {
        for (Barra barra : calcularBarras()) {
            if (barra.area.contains(mouse)) {
                setToolTipText(barra.categoria + ": " + Formatadores.formatarMoeda(barra.valor));
                return;
            }
        }
        setToolTipText(null);
    }

    private List<Barra> calcularBarras() {
        List<Barra> barras = new ArrayList<>();
        if (dados.isEmpty()) {
            return barras;
        }

        int largura = getWidth();
        int altura = getHeight();
        int margemInferior = 24;
        int areaUtilAltura = altura - margemInferior;

        double maiorValor = 0.0;
        for (double valor : dados.values()) {
            maiorValor = Math.max(maiorValor, valor);
        }

        int quantidadeBarras = dados.size();
        double larguraBarra = largura / (double) quantidadeBarras;

        int indice = 0;
        for (Map.Entry<String, Double> entrada : dados.entrySet()) {
            double proporcao = maiorValor == 0.0 ? 0.0 : entrada.getValue() / maiorValor;
            int alturaBarra = (int) Math.round(proporcao * (areaUtilAltura - 8));
            int x = (int) Math.round(indice * larguraBarra) + 4;
            int larguraDesenho = (int) Math.round(larguraBarra) - 8;
            int y = areaUtilAltura - alturaBarra;

            barras.add(new Barra(new Rectangle(x, y, Math.max(larguraDesenho, 1), Math.max(alturaBarra, 1)), entrada.getKey(), entrada.getValue()));
            indice++;
        }
        return barras;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (dados.isEmpty()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(Fontes.PEQUENO);

        int altura = getHeight();
        FontMetrics metrics = g2.getFontMetrics();

        for (Barra barra : calcularBarras()) {
            g2.setColor(Cores.DESERT_SAND);
            g2.fill(new RoundRectangle2D.Double(barra.area.x, barra.area.y, barra.area.width, barra.area.height, 6, 6));

            String rotulo = barra.categoria;
            int rotuloX = barra.area.x + (barra.area.width - metrics.stringWidth(rotulo)) / 2;
            g2.setColor(Cores.TEXTO_SECUNDARIO);
            g2.drawString(rotulo, Math.max(rotuloX, barra.area.x), altura - 8);
        }

        g2.dispose();
    }
}
