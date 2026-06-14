package com.cfpi.apresentacao.objetivos;

import com.cfpi.apresentacao.designsystem.CampoBusca;
import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.ConfirmacaoDialog;
import com.cfpi.apresentacao.designsystem.ErroValidacaoDialog;
import com.cfpi.apresentacao.designsystem.Espacamentos;
import com.cfpi.apresentacao.designsystem.Fontes;
import com.cfpi.apresentacao.designsystem.ListaPanelUtil;
import com.cfpi.apresentacao.designsystem.RoundedButton;
import com.cfpi.dominio.entidades.objetivo.Objetivo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
 * Tela de objetivos: busca por nome, lista de objetivos (com reordenação em
 * memória via subir/descer) e formulário de novo objetivo.
 */
public class ObjetivosView extends JPanel {

    private final ObjetivosController controller;
    private final JPanel listaPanel;
    private final CampoBusca campoBusca;
    private final RoundedButton botaoNovoObjetivo;
    private ObjetivoListItemPanel itemArrastado;
    private ObjetivoListItemPanel itemAlvo;

    public ObjetivosView(ObjetivosController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(0, Espacamentos.ESPACO_3));
        setBackground(Cores.FUNDO_PRINCIPAL);
        setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3));

        JLabel titulo = new JLabel("Objetivos");
        titulo.setFont(Fontes.TITULO);
        titulo.setForeground(Cores.TEXTO_PRIMARIO);

        campoBusca = new CampoBusca("Buscar objetivo por nome...");
        JTextField campoTexto = campoBusca.getCampo();
        campoTexto.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarLista();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarLista();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarLista();
            }
        });

        botaoNovoObjetivo = new RoundedButton("+ Novo objetivo", Espacamentos.RAIO, Cores.PRIMARIO, Cores.PRIMARIO_TEXTO);
        botaoNovoObjetivo.addActionListener(e -> abrirFormularioNovoObjetivo());

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);
        cabecalho.add(titulo, BorderLayout.WEST);

        JPanel acoesCabecalho = new JPanel(new FlowLayout(FlowLayout.RIGHT, Espacamentos.ESPACO_2, 0));
        acoesCabecalho.setOpaque(false);
        acoesCabecalho.add(botaoNovoObjetivo);
        cabecalho.add(acoesCabecalho, BorderLayout.EAST);

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filtros.setOpaque(false);
        filtros.add(campoBusca);

        JLabel dicaReordenar = new JLabel("Arraste pelo ícone ⋮⋮ para reordenar a prioridade dos objetivos.");
        dicaReordenar.setFont(Fontes.PEQUENO);
        dicaReordenar.setForeground(Cores.TEXTO_SECUNDARIO);

        JPanel topo = new JPanel();
        topo.setOpaque(false);
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.add(cabecalho);
        topo.add(Box.createVerticalStrut(Espacamentos.ESPACO_3));
        topo.add(filtros);
        topo.add(Box.createVerticalStrut(Espacamentos.ESPACO_1));
        topo.add(dicaReordenar);

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

    private void atualizarLista() {
        List<Objetivo> objetivos = controller.filtrarPorNome(campoBusca.getCampo().getText());
        ListaPanelUtil.repopular(listaPanel, objetivos, objetivo -> {
            ObjetivoListItemPanel item = new ObjetivoListItemPanel(objetivo);
            item.getBotaoEditar().addActionListener(e -> abrirFormularioEdicao(objetivo));
            item.getBotaoRemover().addActionListener(e -> remover(objetivo));
            configurarArraste(item);
            return item;
        }, Espacamentos.ESPACO_2);
    }

    /**
     * Permite reordenar a prioridade dos objetivos arrastando {@code item}
     * pela sua alça (ver {@link ObjetivoListItemPanel#getLabelAlca()}): ao
     * soltar o item sobre outra linha, a posição de ambos é trocada na
     * ordem de exibição.
     */
    private void configurarArraste(ObjetivoListItemPanel item) {
        MouseAdapter arraste = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                itemArrastado = item;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (itemArrastado == null) {
                    return;
                }
                ObjetivoListItemPanel alvo = itemSobMouse(e);
                if (alvo == itemAlvo) {
                    return;
                }
                if (itemAlvo != null) {
                    itemAlvo.setCorFundo(Cores.CARD_BRANCO);
                }
                itemAlvo = (alvo != itemArrastado) ? alvo : null;
                if (itemAlvo != null) {
                    itemAlvo.setCorFundo(Cores.DESERT_SAND_40);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (itemAlvo != null) {
                    itemAlvo.setCorFundo(Cores.CARD_BRANCO);
                    controller.moverPorArraste(itemArrastado.getObjetivo(), itemAlvo.getObjetivo());
                    atualizarLista();
                }
                itemArrastado = null;
                itemAlvo = null;
            }
        };
        item.getLabelAlca().addMouseListener(arraste);
        item.getLabelAlca().addMouseMotionListener(arraste);
    }

    private ObjetivoListItemPanel itemSobMouse(MouseEvent e) {
        Point ponto = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), listaPanel);
        for (Component componente : listaPanel.getComponents()) {
            if (componente instanceof ObjetivoListItemPanel && componente.getBounds().contains(ponto)) {
                return (ObjetivoListItemPanel) componente;
            }
        }
        return null;
    }

    private void abrirFormularioNovoObjetivo() {
        ObjetivoFormDialog dialog = new ObjetivoFormDialog(janelaPai());
        dialog.getBotaoSalvar().addActionListener(e -> {
            List<String> erros = controller.criar(dialog.getCampoNome().getText(), dialog.getCampoValor().getText());
            if (erros.isEmpty()) {
                dialog.dispose();
                atualizarLista();
            } else {
                ErroValidacaoDialog.exibir(dialog, erros);
            }
        });
        dialog.setVisible(true);
    }

    private void abrirFormularioEdicao(Objetivo objetivo) {
        ObjetivoFormDialog dialog = new ObjetivoFormDialog(janelaPai());
        dialog.preencherParaEdicao(objetivo);
        dialog.getBotaoSalvar().addActionListener(e -> {
            List<String> erros = controller.atualizar(objetivo, dialog.getCampoNome().getText(), dialog.getCampoValor().getText());
            if (erros.isEmpty()) {
                dialog.dispose();
                atualizarLista();
            } else {
                ErroValidacaoDialog.exibir(dialog, erros);
            }
        });
        dialog.setVisible(true);
    }

    private void remover(Objetivo objetivo) {
        controller.remover(objetivo, () -> ConfirmacaoDialog.confirmar(this, "Remover este objetivo?"));
        atualizarLista();
    }

    private Frame janelaPai() {
        return (Frame) SwingUtilities.getWindowAncestor(this);
    }

    public CampoBusca getCampoBusca() {
        return campoBusca;
    }

    public RoundedButton getBotaoNovoObjetivo() {
        return botaoNovoObjetivo;
    }

    public JPanel getListaPanel() {
        return listaPanel;
    }
}
