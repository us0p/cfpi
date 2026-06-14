package com.cfpi.apresentacao.dashboard;

import com.cfpi.apresentacao.comum.CalculadoraPrazoObjetivo;
import com.cfpi.apresentacao.comum.Formatadores;
import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.CircularProgressDonut;
import com.cfpi.apresentacao.designsystem.Espacamentos;
import com.cfpi.apresentacao.designsystem.Fontes;
import com.cfpi.apresentacao.designsystem.ListaPanelUtil;
import com.cfpi.apresentacao.designsystem.RoundedPanel;
import com.cfpi.apresentacao.shell.TelaAtualizavel;
import com.cfpi.apresentacao.transacoes.TransacaoListItemPanel;
import com.cfpi.apresentacao.transacoes.TransacoesViewModel;
import com.cfpi.dominio.entidades.objetivo.Objetivo;
import com.cfpi.dominio.entidades.transacao.Credito;
import com.cfpi.dominio.entidades.transacao.Debito;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Tela de Dashboard: cards de resumo (saldo total, limite de crédito
 * consumido, objetivo principal), gráficos de gastos por categoria e
 * crescimento de patrimônio, e lista de transações dos últimos 7 dias.
 */
public class DashboardView extends JPanel implements TelaAtualizavel {

    private final DashboardController controller;
    private final TransacoesViewModel transacoesViewModel = new TransacoesViewModel();

    private final JLabel labelSaldoTotal;
    private final CircularProgressDonut donutLimite;
    private final JLabel labelLimiteSubtexto;
    private final JLabel labelObjetivoNome;
    private final JLabel labelObjetivoMeta;
    private final JLabel labelObjetivoDias;
    private final JComboBox<String> comboCategoriaBarras;
    private final JComboBox<String> comboCategoriaLinha;
    private final BarChartCategoriaPanel graficoBarras;
    private final LineChartPatrimonioPanel graficoLinha;
    private final JPanel listaRecentes;

    public DashboardView(DashboardController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(0, Espacamentos.ESPACO_3));
        setBackground(Cores.FUNDO_PRINCIPAL);
        setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3));

        JLabel titulo = new JLabel("Dashboard");
        titulo.setFont(Fontes.TITULO);
        titulo.setForeground(Cores.TEXTO_PRIMARIO);
        add(titulo, BorderLayout.NORTH);

        JPanel conteudo = new JPanel();
        conteudo.setOpaque(false);
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));

        JPanel cards = new JPanel(new GridLayout(1, 3, Espacamentos.ESPACO_3, 0));
        cards.setOpaque(false);

        RoundedPanel cardSaldo = new RoundedPanel(Espacamentos.RAIO, Cores.CARD_DESTAQUE);
        cardSaldo.setLayout(new BoxLayout(cardSaldo, BoxLayout.Y_AXIS));
        cardSaldo.setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3));
        JLabel rotuloSaldo = new JLabel("Saldo total");
        rotuloSaldo.setFont(Fontes.SUBTITULO);
        rotuloSaldo.setForeground(Cores.TEXTO_PRIMARIO);
        rotuloSaldo.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelSaldoTotal = new JLabel("R$ 0,00");
        labelSaldoTotal.setFont(Fontes.DESTAQUE_NUMERICO);
        labelSaldoTotal.setForeground(Cores.TEXTO_PRIMARIO);
        labelSaldoTotal.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel subtextoSaldo = new JLabel("Soma do saldo de todas as contas");
        subtextoSaldo.setFont(Fontes.PEQUENO);
        subtextoSaldo.setForeground(Cores.TEXTO_SECUNDARIO);
        subtextoSaldo.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardSaldo.add(rotuloSaldo);
        cardSaldo.add(Box.createVerticalStrut(Espacamentos.ESPACO_1));
        cardSaldo.add(labelSaldoTotal);
        cardSaldo.add(Box.createVerticalStrut(Espacamentos.ESPACO_1));
        cardSaldo.add(subtextoSaldo);

        RoundedPanel cardLimite = new RoundedPanel(Espacamentos.RAIO, Cores.CARD_BRANCO);
        cardLimite.setLayout(new BorderLayout(Espacamentos.ESPACO_3, 0));
        cardLimite.setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3));
        donutLimite = new CircularProgressDonut(Cores.DEBITO, Cores.DESERT_SAND_40, 10);
        JLabel rotuloLimite = new JLabel("Limite de crédito consumido");
        rotuloLimite.setFont(Fontes.CORPO_NEGRITO);
        rotuloLimite.setForeground(Cores.TEXTO_PRIMARIO);
        rotuloLimite.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelLimiteSubtexto = new JLabel(" ");
        labelLimiteSubtexto.setFont(Fontes.PEQUENO);
        labelLimiteSubtexto.setForeground(Cores.TEXTO_SECUNDARIO);
        labelLimiteSubtexto.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel textosLimite = new JPanel();
        textosLimite.setOpaque(false);
        textosLimite.setLayout(new BoxLayout(textosLimite, BoxLayout.Y_AXIS));
        textosLimite.add(rotuloLimite);
        textosLimite.add(Box.createVerticalStrut(Espacamentos.ESPACO_1));
        textosLimite.add(labelLimiteSubtexto);
        cardLimite.add(donutLimite, BorderLayout.WEST);
        cardLimite.add(textosLimite, BorderLayout.CENTER);

        RoundedPanel cardObjetivo = new RoundedPanel(Espacamentos.RAIO, Cores.CARD_BRANCO);
        cardObjetivo.setLayout(new BoxLayout(cardObjetivo, BoxLayout.Y_AXIS));
        cardObjetivo.setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3));
        JLabel rotuloObjetivo = new JLabel("Objetivo principal");
        rotuloObjetivo.setFont(Fontes.SUBTITULO);
        rotuloObjetivo.setForeground(Cores.TEXTO_PRIMARIO);
        rotuloObjetivo.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelObjetivoNome = new JLabel("Nenhum objetivo cadastrado");
        labelObjetivoNome.setFont(Fontes.DESTAQUE_NUMERICO);
        labelObjetivoNome.setForeground(Cores.TEXTO_PRIMARIO);
        labelObjetivoNome.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelObjetivoMeta = new JLabel(" ");
        labelObjetivoMeta.setFont(Fontes.PEQUENO);
        labelObjetivoMeta.setForeground(Cores.TEXTO_SECUNDARIO);
        labelObjetivoMeta.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelObjetivoDias = new JLabel(" ");
        labelObjetivoDias.setFont(Fontes.DESTAQUE_NUMERICO);
        labelObjetivoDias.setForeground(Cores.FERN);
        labelObjetivoDias.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardObjetivo.add(rotuloObjetivo);
        cardObjetivo.add(Box.createVerticalStrut(Espacamentos.ESPACO_1));
        cardObjetivo.add(labelObjetivoNome);
        cardObjetivo.add(Box.createVerticalStrut(Espacamentos.ESPACO_1));
        cardObjetivo.add(labelObjetivoMeta);
        cardObjetivo.add(Box.createVerticalStrut(Espacamentos.ESPACO_2));
        cardObjetivo.add(labelObjetivoDias);

        cards.add(cardSaldo);
        cards.add(cardLimite);
        cards.add(cardObjetivo);

        JPanel graficos = new JPanel(new GridLayout(1, 2, Espacamentos.ESPACO_3, 0));
        graficos.setOpaque(false);

        comboCategoriaBarras = new JComboBox<>(categoriasDebitoComOpcaoTodas());
        comboCategoriaBarras.addActionListener(e -> atualizar());
        graficoBarras = new BarChartCategoriaPanel();
        graficos.add(criarPainelGrafico("Gastos por categoria", comboCategoriaBarras, graficoBarras));

        comboCategoriaLinha = new JComboBox<>(categoriasCreditoComOpcaoTodas());
        comboCategoriaLinha.addActionListener(e -> atualizar());
        graficoLinha = new LineChartPatrimonioPanel();
        graficos.add(criarPainelGrafico("Crescimento de patrimônio", comboCategoriaLinha, graficoLinha));

        listaRecentes = new JPanel();
        listaRecentes.setOpaque(false);
        listaRecentes.setLayout(new BoxLayout(listaRecentes, BoxLayout.Y_AXIS));

        JLabel tituloRecentes = new JLabel("Últimos 7 dias");
        tituloRecentes.setFont(Fontes.SUBTITULO);
        tituloRecentes.setForeground(Cores.TEXTO_PRIMARIO);
        tituloRecentes.setBorder(BorderFactory.createEmptyBorder(0, 0, Espacamentos.ESPACO_2, 0));

        JScrollPane scrollRecentes = new JScrollPane(listaRecentes);
        scrollRecentes.setOpaque(false);
        scrollRecentes.getViewport().setOpaque(false);
        scrollRecentes.setBorder(BorderFactory.createEmptyBorder());

        conteudo.add(cards);
        conteudo.add(Box.createVerticalStrut(Espacamentos.ESPACO_3));
        conteudo.add(graficos);
        conteudo.add(Box.createVerticalStrut(Espacamentos.ESPACO_3));
        conteudo.add(tituloRecentes);
        conteudo.add(scrollRecentes);

        add(conteudo, BorderLayout.CENTER);

        atualizar();
    }

    private JPanel criarPainelGrafico(String titulo, JComboBox<String> combo, javax.swing.JComponent grafico) {
        RoundedPanel painel = new RoundedPanel(Espacamentos.RAIO, Cores.CARD_BRANCO);
        painel.setLayout(new BorderLayout(0, Espacamentos.ESPACO_2));
        painel.setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        JLabel label = new JLabel(titulo);
        label.setFont(Fontes.SUBTITULO);
        label.setForeground(Cores.TEXTO_PRIMARIO);
        topo.add(label, BorderLayout.WEST);
        topo.add(combo, BorderLayout.EAST);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(grafico, BorderLayout.CENTER);
        return painel;
    }

    private String[] categoriasDebitoComOpcaoTodas() {
        return transacoesViewModel.categoriasComOpcaoTodas(Debito.class);
    }

    private String[] categoriasCreditoComOpcaoTodas() {
        return transacoesViewModel.categoriasComOpcaoTodas(Credito.class);
    }

    private String filtroSelecionado(JComboBox<String> combo) {
        String selecionado = (String) combo.getSelectedItem();
        return "Todas".equals(selecionado) ? "todas" : selecionado;
    }

    @Override
    public void atualizar() {
        DashboardDados dadosBarras = controller.carregar(filtroSelecionado(comboCategoriaBarras));
        DashboardDados dadosLinha = controller.carregar(filtroSelecionado(comboCategoriaLinha));

        labelSaldoTotal.setText(Formatadores.formatarMoeda(dadosBarras.getSaldoTotal()));
        donutLimite.setPercentual(dadosBarras.getPercentualLimiteConsumido());
        labelLimiteSubtexto.setText(Formatadores.formatarPercentual(dadosBarras.getPercentualLimiteConsumido()) + " do limite utilizado");

        Objetivo objetivo = dadosBarras.getObjetivoPrincipal();
        if (objetivo != null) {
            labelObjetivoNome.setText(objetivo.getNome());
            labelObjetivoMeta.setText("Meta: " + Formatadores.formatarMoeda(objetivo.getValor()));
            int diasRestantes = dadosBarras.getDiasRestantesObjetivo();
            if (diasRestantes >= CalculadoraPrazoObjetivo.PRAZO_INDETERMINADO) {
                labelObjetivoDias.setText("Prazo indeterminado");
            } else {
                labelObjetivoDias.setText(diasRestantes + " dias restantes");
            }
        } else {
            labelObjetivoNome.setText("Nenhum objetivo cadastrado");
            labelObjetivoMeta.setText(" ");
            labelObjetivoDias.setText(" ");
        }

        graficoBarras.setDados(dadosBarras.getGastosPorCategoria());
        graficoLinha.setDados(dadosLinha.getCrescimentoPatrimonio());

        ListaPanelUtil.repopular(listaRecentes, dadosBarras.getTransacoesRecentes(), TransacaoListItemPanel::new, Espacamentos.ESPACO_2);
    }

    public JLabel getLabelSaldoTotal() {
        return labelSaldoTotal;
    }

    public CircularProgressDonut getDonutLimite() {
        return donutLimite;
    }

    public JLabel getLabelObjetivoNome() {
        return labelObjetivoNome;
    }

    public JLabel getLabelObjetivoMeta() {
        return labelObjetivoMeta;
    }

    public JLabel getLabelObjetivoDias() {
        return labelObjetivoDias;
    }

    public JComboBox<String> getComboCategoriaBarras() {
        return comboCategoriaBarras;
    }

    public JComboBox<String> getComboCategoriaLinha() {
        return comboCategoriaLinha;
    }

    public BarChartCategoriaPanel getGraficoBarras() {
        return graficoBarras;
    }

    public LineChartPatrimonioPanel getGraficoLinha() {
        return graficoLinha;
    }

    public JPanel getListaRecentes() {
        return listaRecentes;
    }
}
