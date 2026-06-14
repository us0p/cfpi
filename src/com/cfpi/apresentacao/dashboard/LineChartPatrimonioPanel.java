package com.cfpi.apresentacao.dashboard;

import com.cfpi.apresentacao.comum.Formatadores;
import com.cfpi.apresentacao.designsystem.Cores;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;

/**
 * Gráfico de linha com a evolução do saldo acumulado ao longo do tempo.
 */
public class LineChartPatrimonioPanel extends JComponent {

    private static final int RAIO_PONTO = 5;
    private static final int RAIO_DETECCAO = RAIO_PONTO + 4;

    private List<PontoPatrimonio> dados = new ArrayList<>();

    public LineChartPatrimonioPanel() {
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
     * @param dados série cronológica de saldo acumulado, em ordem
     *              crescente de data.
     */
    public void setDados(List<PontoPatrimonio> dados) {
        this.dados = dados;
        setToolTipText(null);
        repaint();
    }

    public List<PontoPatrimonio> getDados() {
        return dados;
    }

    private void atualizarTooltip(java.awt.Point mouse) {
        List<Point2D> pontosTela = calcularPontosTela();
        for (int i = 0; i < pontosTela.size(); i++) {
            if (mouse.distance(pontosTela.get(i)) <= RAIO_DETECCAO) {
                PontoPatrimonio ponto = dados.get(i);
                setToolTipText(Formatadores.formatarData(ponto.getData()) + ": " + Formatadores.formatarMoeda(ponto.getSaldoAcumulado()));
                return;
            }
        }
        setToolTipText(null);
    }

    private List<Point2D> calcularPontosTela() {
        List<Point2D> pontos = new ArrayList<>();
        if (dados.size() < 2) {
            return pontos;
        }

        int largura = getWidth();
        int altura = getHeight();
        int margem = 12;

        double maior = Double.NEGATIVE_INFINITY;
        double menor = Double.POSITIVE_INFINITY;
        for (PontoPatrimonio ponto : dados) {
            maior = Math.max(maior, ponto.getSaldoAcumulado());
            menor = Math.min(menor, ponto.getSaldoAcumulado());
        }
        double amplitude = maior - menor;
        if (amplitude == 0.0) {
            amplitude = 1.0;
        }

        double larguraUtil = largura - 2.0 * margem;
        double alturaUtil = altura - 2.0 * margem;

        for (int i = 0; i < dados.size(); i++) {
            double x = margem + larguraUtil * i / (dados.size() - 1);
            double proporcao = (dados.get(i).getSaldoAcumulado() - menor) / amplitude;
            double y = margem + alturaUtil * (1 - proporcao);
            pontos.add(new Point2D.Double(x, y));
        }
        return pontos;
    }

    @Override
    protected void paintComponent(Graphics g) {
        List<Point2D> pontosTela = calcularPontosTela();
        if (pontosTela.isEmpty()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Path2D linha = new Path2D.Double();
        for (int i = 0; i < pontosTela.size(); i++) {
            Point2D p = pontosTela.get(i);
            if (i == 0) {
                linha.moveTo(p.getX(), p.getY());
            } else {
                linha.lineTo(p.getX(), p.getY());
            }
        }

        g2.setColor(Cores.FERN);
        g2.setStroke(new BasicStroke(2.5f));
        g2.draw(linha);

        for (Point2D p : pontosTela) {
            g2.fill(new Ellipse2D.Double(p.getX() - RAIO_PONTO, p.getY() - RAIO_PONTO, RAIO_PONTO * 2, RAIO_PONTO * 2));
        }

        g2.dispose();
    }
}
