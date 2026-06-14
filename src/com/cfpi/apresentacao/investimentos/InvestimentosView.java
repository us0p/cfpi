package com.cfpi.apresentacao.investimentos;

import com.cfpi.apresentacao.designsystem.CampoBusca;
import com.cfpi.apresentacao.designsystem.CampoFiltro;
import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.ErroValidacaoDialog;
import com.cfpi.apresentacao.designsystem.Espacamentos;
import com.cfpi.apresentacao.designsystem.Fontes;
import com.cfpi.apresentacao.designsystem.ListaPanelUtil;
import com.cfpi.apresentacao.designsystem.Renderers;
import com.cfpi.apresentacao.designsystem.RoundedButton;
import com.cfpi.apresentacao.designsystem.RoundedPanel;
import com.cfpi.apresentacao.shell.TelaAtualizavel;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.investimento.Investimento;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Tela de investimentos: busca por nome, filtro por tipo, lista de ativos
 * (agrupados a partir das operações de todas as contas) e formulário de
 * nova operação.
 */
public class InvestimentosView extends JPanel implements TelaAtualizavel {

    private final InvestimentosController controller;
    private final InvestimentosViewModel viewModel;
    private final Consumer<AtivoResumo> aoClicarDetalhes;
    private final JPanel listaPanel;
    private final CampoBusca campoBusca;
    private final JComboBox<Class<? extends Investimento>> comboFiltroTipo;
    private final RoundedButton botaoNovoInvestimento;

    public InvestimentosView(InvestimentosController controller, InvestimentosViewModel viewModel, Consumer<AtivoResumo> aoClicarDetalhes) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.aoClicarDetalhes = aoClicarDetalhes;

        setLayout(new BorderLayout(0, Espacamentos.ESPACO_3));
        setBackground(Cores.FUNDO_PRINCIPAL);
        setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3));

        JLabel titulo = new JLabel("Investimentos");
        titulo.setFont(Fontes.TITULO);
        titulo.setForeground(Cores.TEXTO_PRIMARIO);

        campoBusca = new CampoBusca("Buscar ativo por nome...");
        JTextField campoTexto = campoBusca.getCampo();
        campoTexto.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizar();
            }
        });

        comboFiltroTipo = new JComboBox<>();
        comboFiltroTipo.addItem(null);
        for (Class<? extends Investimento> tipo : InvestimentoFormDialog.TIPOS) {
            comboFiltroTipo.addItem(tipo);
        }
        comboFiltroTipo.setRenderer(Renderers.exibindo(Class.class, Class::getSimpleName, "Todos os tipos"));
        comboFiltroTipo.addActionListener(e -> atualizar());

        botaoNovoInvestimento = new RoundedButton("+ Novo investimento", Espacamentos.RAIO, Cores.PRIMARIO, Cores.PRIMARIO_TEXTO);
        botaoNovoInvestimento.addActionListener(e -> abrirFormularioNovoInvestimento());

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);
        cabecalho.add(titulo, BorderLayout.WEST);

        JPanel acoesCabecalho = new JPanel(new FlowLayout(FlowLayout.RIGHT, Espacamentos.ESPACO_2, 0));
        acoesCabecalho.setOpaque(false);
        acoesCabecalho.add(botaoNovoInvestimento);
        cabecalho.add(acoesCabecalho, BorderLayout.EAST);

        RoundedPanel filtros = new RoundedPanel(Espacamentos.RAIO, Cores.CARD_BRANCO);
        filtros.setLayout(new FlowLayout(FlowLayout.LEFT, Espacamentos.ESPACO_2, 0));
        filtros.setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3));
        filtros.add(campoBusca);
        filtros.add(CampoFiltro.criar("Tipo", comboFiltroTipo));

        JPanel topo = new JPanel();
        topo.setOpaque(false);
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.add(cabecalho);
        topo.add(Box.createVerticalStrut(Espacamentos.ESPACO_3));
        topo.add(filtros);

        listaPanel = new JPanel();
        listaPanel.setOpaque(false);
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(listaPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        add(topo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        atualizar();
    }

    @Override
    public void atualizar() {
        @SuppressWarnings("unchecked")
        Class<? extends Investimento> filtroTipo = (Class<? extends Investimento>) comboFiltroTipo.getSelectedItem();
        List<AtivoResumo> ativos = viewModel.filtrarPorTipo(controller.filtrarPorNome(campoBusca.getCampo().getText()), filtroTipo);

        ListaPanelUtil.repopular(listaPanel, ativos, ativo -> {
            AtivoListItemPanel item = new AtivoListItemPanel(ativo);
            item.getBotaoDetalhes().addActionListener(e -> aoClicarDetalhes.accept(ativo));
            return item;
        }, Espacamentos.ESPACO_2);
    }

    private void abrirFormularioNovoInvestimento() {
        InvestimentoFormDialog dialog = new InvestimentoFormDialog(janelaPai());
        dialog.setContas(controller.getUsuario().getContas());
        dialog.getBotaoSalvar().addActionListener(e -> {
            Conta conta = (Conta) dialog.getComboConta().getSelectedItem();
            if (conta == null) {
                return;
            }
            @SuppressWarnings("unchecked")
            Class<? extends Investimento> tipo = (Class<? extends Investimento>) dialog.getComboTipo().getSelectedItem();
            List<String> erros = controller.criar(
                    tipo,
                    dialog.getCampoNomeAtivo().getText(),
                    dialog.getCampoValor().getText(),
                    conta,
                    dialog.getCampoQuantidade().getText(),
                    dialog.getCampoData().getText(),
                    (String) dialog.getComboOperacao().getSelectedItem());
            if (erros.isEmpty()) {
                dialog.dispose();
                atualizar();
            } else {
                ErroValidacaoDialog.exibir(dialog, erros);
            }
        });
        dialog.setVisible(true);
    }

    private Frame janelaPai() {
        return (Frame) SwingUtilities.getWindowAncestor(this);
    }

    public CampoBusca getCampoBusca() {
        return campoBusca;
    }

    public JComboBox<Class<? extends Investimento>> getComboFiltroTipo() {
        return comboFiltroTipo;
    }

    public RoundedButton getBotaoNovoInvestimento() {
        return botaoNovoInvestimento;
    }

    public JPanel getListaPanel() {
        return listaPanel;
    }
}
