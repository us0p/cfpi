package com.cfpi.apresentacao.transacoes;

import com.cfpi.apresentacao.designsystem.CampoFiltro;
import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.ConfirmacaoDialog;
import com.cfpi.apresentacao.designsystem.ErroValidacaoDialog;
import com.cfpi.apresentacao.designsystem.Espacamentos;
import com.cfpi.apresentacao.designsystem.Fontes;
import com.cfpi.apresentacao.designsystem.ListaPanelUtil;
import com.cfpi.apresentacao.designsystem.RoundedButton;
import com.cfpi.apresentacao.designsystem.RoundedPanel;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.transacao.Credito;
import com.cfpi.dominio.entidades.transacao.Debito;
import com.cfpi.dominio.entidades.transacao.Transacao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * Tela de transações: filtros por tipo e categoria, lista de transações de
 * todas as contas do usuário e formulário de nova transação.
 */
public class TransacoesView extends JPanel {

    private static final String TODAS_CATEGORIAS = "Todas";

    private final TransacoesController controller;
    private final TransacoesViewModel viewModel;
    private final JPanel listaPanel;
    private final JComboBox<String> comboFiltroTipo;
    private final JComboBox<String> comboFiltroCategoria;
    private final RoundedButton botaoNovaTransacao;

    public TransacoesView(TransacoesController controller, TransacoesViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        setLayout(new BorderLayout(0, Espacamentos.ESPACO_3));
        setBackground(Cores.FUNDO_PRINCIPAL);
        setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3));

        JLabel titulo = new JLabel("Transações");
        titulo.setFont(Fontes.TITULO);
        titulo.setForeground(Cores.TEXTO_PRIMARIO);

        comboFiltroTipo = new JComboBox<>(new String[]{"Todos", "Débito", "Crédito"});
        comboFiltroTipo.addActionListener(e -> {
            repopularCategorias();
            atualizarLista();
        });

        comboFiltroCategoria = new JComboBox<>();
        repopularCategorias();
        comboFiltroCategoria.addActionListener(e -> atualizarLista());

        botaoNovaTransacao = new RoundedButton("+ Nova transação", Espacamentos.RAIO, Cores.PRIMARIO, Cores.PRIMARIO_TEXTO);
        botaoNovaTransacao.addActionListener(e -> abrirFormularioNovaTransacao());

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);
        cabecalho.add(titulo, BorderLayout.WEST);

        JPanel acoesCabecalho = new JPanel(new FlowLayout(FlowLayout.RIGHT, Espacamentos.ESPACO_2, 0));
        acoesCabecalho.setOpaque(false);
        acoesCabecalho.add(botaoNovaTransacao);
        cabecalho.add(acoesCabecalho, BorderLayout.EAST);

        RoundedPanel filtros = new RoundedPanel(Espacamentos.RAIO, Cores.CARD_BRANCO);
        filtros.setLayout(new FlowLayout(FlowLayout.LEFT, Espacamentos.ESPACO_2, 0));
        filtros.setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3));
        filtros.add(CampoFiltro.criar("Tipo", comboFiltroTipo));
        filtros.add(CampoFiltro.criar("Categoria", comboFiltroCategoria));

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

        atualizarLista();
    }

    private void repopularCategorias() {
        String tipo = (String) comboFiltroTipo.getSelectedItem();
        LinkedHashSet<String> categorias = new LinkedHashSet<>();
        categorias.add(TODAS_CATEGORIAS);
        if ("Débito".equals(tipo) || "Todos".equals(tipo)) {
            categorias.addAll(List.of(viewModel.categoriasParaTipo(Debito.class)));
        }
        if ("Crédito".equals(tipo) || "Todos".equals(tipo)) {
            categorias.addAll(List.of(viewModel.categoriasParaTipo(Credito.class)));
        }

        comboFiltroCategoria.removeAllItems();
        for (String categoria : categorias) {
            comboFiltroCategoria.addItem(categoria);
        }
    }

    private void atualizarLista() {
        List<Transacao> transacoes = controller.carregar();
        String filtroTipo = (String) comboFiltroTipo.getSelectedItem();
        if ("Débito".equals(filtroTipo)) {
            transacoes = viewModel.filtrarPorTipo(transacoes, Debito.class);
        } else if ("Crédito".equals(filtroTipo)) {
            transacoes = viewModel.filtrarPorTipo(transacoes, Credito.class);
        }

        String filtroCategoria = (String) comboFiltroCategoria.getSelectedItem();
        if (filtroCategoria != null && !TODAS_CATEGORIAS.equals(filtroCategoria)) {
            transacoes = viewModel.filtrarPorCategoria(transacoes, filtroCategoria);
        }

        ListaPanelUtil.repopular(listaPanel, transacoes, transacao -> {
            TransacaoListItemPanel item = new TransacaoListItemPanel(transacao);
            item.getBotaoEditar().addActionListener(e -> abrirFormularioEdicao(transacao));
            item.getBotaoRemover().addActionListener(e -> remover(transacao));
            return item;
        }, Espacamentos.ESPACO_2);
    }

    private void abrirFormularioNovaTransacao() {
        TransacaoFormDialog dialog = new TransacaoFormDialog(janelaPai(), viewModel);
        dialog.setContas(controller.getUsuario().getContas());
        dialog.getBotaoSalvar().addActionListener(e -> {
            List<String> erros = salvarNovaTransacao(dialog);
            if (erros.isEmpty()) {
                dialog.dispose();
                atualizarLista();
            } else {
                ErroValidacaoDialog.exibir(dialog, erros);
            }
        });
        dialog.setVisible(true);
    }

    private List<String> salvarNovaTransacao(TransacaoFormDialog dialog) {
        Conta conta = (Conta) dialog.getComboConta().getSelectedItem();
        if (conta == null) {
            return List.of("Selecione uma conta.");
        }
        String descricao = dialog.getCampoDescricao().getText();
        String data = dialog.getCampoData().getText();
        String valor = dialog.getCampoValor().getText();
        String categoria = (String) dialog.getComboCategoria().getSelectedItem();

        if (TransacaoFormDialog.TIPO_DEBITO.equals(dialog.getComboTipoTransacao().getSelectedItem())) {
            String tipoDebito = (String) dialog.getComboTipoDebito().getSelectedItem();
            return controller.criarDebito(conta, descricao, data, valor, categoria, tipoDebito);
        } else {
            return controller.criarCredito(conta, descricao, data, valor, categoria);
        }
    }

    private void abrirFormularioEdicao(Transacao transacao) {
        TransacaoFormDialog dialog = new TransacaoFormDialog(janelaPai(), viewModel);
        dialog.setContas(controller.getUsuario().getContas());
        dialog.preencherParaEdicao(transacao);
        dialog.getBotaoSalvar().addActionListener(e -> {
            List<String> erros = controller.atualizar(
                    transacao,
                    dialog.getCampoDescricao().getText(),
                    dialog.getCampoData().getText(),
                    dialog.getCampoValor().getText(),
                    (String) dialog.getComboCategoria().getSelectedItem());
            if (erros.isEmpty()) {
                dialog.dispose();
                atualizarLista();
            } else {
                ErroValidacaoDialog.exibir(dialog, erros);
            }
        });
        dialog.setVisible(true);
    }

    private void remover(Transacao transacao) {
        Conta conta = transacao.getConta();
        controller.remover(transacao, conta, () -> ConfirmacaoDialog.confirmar(this, "Remover esta transação?"));
        atualizarLista();
    }

    private java.awt.Frame janelaPai() {
        return (java.awt.Frame) SwingUtilities.getWindowAncestor(this);
    }

    public JComboBox<String> getComboFiltroTipo() {
        return comboFiltroTipo;
    }

    public JComboBox<String> getComboFiltroCategoria() {
        return comboFiltroCategoria;
    }

    public RoundedButton getBotaoNovaTransacao() {
        return botaoNovaTransacao;
    }

    public JPanel getListaPanel() {
        return listaPanel;
    }
}
