package com.cfpi.apresentacao.contas;

import com.cfpi.apresentacao.designsystem.CampoBusca;
import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.ConfirmacaoDialog;
import com.cfpi.apresentacao.designsystem.ErroValidacaoDialog;
import com.cfpi.apresentacao.designsystem.Espacamentos;
import com.cfpi.apresentacao.designsystem.Fontes;
import com.cfpi.apresentacao.designsystem.ListaPanelUtil;
import com.cfpi.apresentacao.designsystem.RoundedButton;
import com.cfpi.apresentacao.shell.TelaAtualizavel;
import com.cfpi.dominio.entidades.banco.Banco;
import com.cfpi.dominio.entidades.conta.Conta;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Tela de contas: busca por número, lista de contas do usuário e formulário
 * de nova conta.
 */
public class ContasView extends JPanel implements TelaAtualizavel {

    private final ContasController controller;
    private final JPanel listaPanel;
    private final CampoBusca campoBusca;
    private final RoundedButton botaoNovaConta;

    public ContasView(ContasController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(0, Espacamentos.ESPACO_3));
        setBackground(Cores.FUNDO_PRINCIPAL);
        setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3));

        JLabel titulo = new JLabel("Contas");
        titulo.setFont(Fontes.TITULO);
        titulo.setForeground(Cores.TEXTO_PRIMARIO);

        campoBusca = new CampoBusca("Buscar por número da conta...");
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

        botaoNovaConta = new RoundedButton("+ Nova conta", Espacamentos.RAIO, Cores.PRIMARIO, Cores.PRIMARIO_TEXTO);
        botaoNovaConta.addActionListener(e -> abrirFormularioNovaConta());

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);
        cabecalho.add(titulo, BorderLayout.WEST);

        JPanel acoesCabecalho = new JPanel(new FlowLayout(FlowLayout.RIGHT, Espacamentos.ESPACO_2, 0));
        acoesCabecalho.setOpaque(false);
        acoesCabecalho.add(botaoNovaConta);
        cabecalho.add(acoesCabecalho, BorderLayout.EAST);

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filtros.setOpaque(false);
        filtros.add(campoBusca);

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
        List<Conta> contas = controller.filtrarPorNumero(campoBusca.getCampo().getText());
        ListaPanelUtil.repopular(listaPanel, contas, conta -> {
            ContaListItemPanel item = new ContaListItemPanel(conta);
            item.getBotaoEditar().addActionListener(e -> abrirFormularioEdicao(conta));
            item.getBotaoRemover().addActionListener(e -> remover(conta));
            return item;
        }, Espacamentos.ESPACO_2);
    }

    private void abrirFormularioNovaConta() {
        ContaFormDialog dialog = new ContaFormDialog(janelaPai());
        dialog.setBancos(controller.getBancos());
        dialog.getBotaoSalvar().addActionListener(e -> {
            Banco banco = (Banco) dialog.getComboBanco().getSelectedItem();
            List<String> erros = controller.criar(
                    (String) dialog.getComboTipo().getSelectedItem(),
                    dialog.getCampoValorConta().getText(),
                    dialog.getCampoNumeroConta().getText(),
                    dialog.getCampoMoeda().getText(),
                    banco,
                    dialog.getLimiteCreditoTexto());
            if (erros.isEmpty()) {
                dialog.dispose();
                atualizar();
            } else {
                ErroValidacaoDialog.exibir(dialog, erros);
            }
        });
        dialog.setVisible(true);
    }

    private void abrirFormularioEdicao(Conta conta) {
        ContaFormDialog dialog = new ContaFormDialog(janelaPai());
        dialog.setBancos(controller.getBancos());
        dialog.preencherParaEdicao(conta);
        dialog.getBotaoSalvar().addActionListener(e -> {
            Banco banco = (Banco) dialog.getComboBanco().getSelectedItem();
            List<String> erros = controller.atualizar(
                    conta,
                    (String) dialog.getComboTipo().getSelectedItem(),
                    dialog.getCampoValorConta().getText(),
                    dialog.getCampoNumeroConta().getText(),
                    dialog.getCampoMoeda().getText(),
                    banco,
                    dialog.getLimiteCreditoTexto());
            if (erros.isEmpty()) {
                dialog.dispose();
                atualizar();
            } else {
                ErroValidacaoDialog.exibir(dialog, erros);
            }
        });
        dialog.setVisible(true);
    }

    private void remover(Conta conta) {
        controller.remover(conta, () -> ConfirmacaoDialog.confirmar(this, "Remover esta conta?"));
        atualizar();
    }

    private Frame janelaPai() {
        return (Frame) SwingUtilities.getWindowAncestor(this);
    }

    public CampoBusca getCampoBusca() {
        return campoBusca;
    }

    public RoundedButton getBotaoNovaConta() {
        return botaoNovaConta;
    }

    public JPanel getListaPanel() {
        return listaPanel;
    }
}
