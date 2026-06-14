package com.cfpi.apresentacao.investimentodetalhes;

import com.cfpi.apresentacao.comum.Formatadores;
import com.cfpi.apresentacao.designsystem.Chip;
import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.ConfirmacaoDialog;
import com.cfpi.apresentacao.designsystem.ErroValidacaoDialog;
import com.cfpi.apresentacao.designsystem.Espacamentos;
import com.cfpi.apresentacao.designsystem.Fontes;
import com.cfpi.apresentacao.designsystem.ListaPanelUtil;
import com.cfpi.apresentacao.designsystem.RoundedPanel;
import com.cfpi.apresentacao.investimentos.AtivoResumo;
import com.cfpi.apresentacao.investimentos.InvestimentoFormDialog;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.investimento.Investimento;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * Tela de detalhes de um ativo: resumo da posição (total investido, data da
 * primeira compra, quantidade atual, valor atual e ganho/perda) e lista de
 * operações de compra/venda, com edição e remoção.
 */
public class InvestimentoDetalhesView extends JPanel {

    private final InvestimentoDetalhesController controller;
    private final Runnable aoVoltar;
    private final JPanel conteudo;
    private AtivoResumo ativoAtual;

    public InvestimentoDetalhesView(InvestimentoDetalhesController controller, Runnable aoVoltar) {
        this.controller = controller;
        this.aoVoltar = aoVoltar;

        setLayout(new BorderLayout(0, Espacamentos.ESPACO_3));
        setBackground(Cores.FUNDO_PRINCIPAL);
        setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3));

        JButton botaoVoltar = new JButton("← Voltar para Investimentos");
        botaoVoltar.setContentAreaFilled(false);
        botaoVoltar.setBorderPainted(false);
        botaoVoltar.setFocusPainted(false);
        botaoVoltar.setForeground(Cores.TEXTO_SECUNDARIO);
        botaoVoltar.setFont(Fontes.PEQUENO_NEGRITO);
        botaoVoltar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botaoVoltar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        botaoVoltar.addActionListener(e -> aoVoltar.run());

        JLabel titulo = new JLabel("Detalhes do investimento");
        titulo.setFont(Fontes.TITULO);
        titulo.setForeground(Cores.TEXTO_PRIMARIO);

        JPanel topo = new JPanel();
        topo.setOpaque(false);
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        JPanel linhaVoltar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        linhaVoltar.setOpaque(false);
        linhaVoltar.add(botaoVoltar);
        topo.add(linhaVoltar);
        topo.add(Box.createVerticalStrut(Espacamentos.ESPACO_1));
        topo.add(criarLinhaEsquerda(titulo));

        conteudo = new JPanel();
        conteudo.setOpaque(false);
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(conteudo);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        add(topo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    /**
     * Carrega e exibe os dados e operações do ativo informado.
     *
     * @param ativo ativo cujos detalhes serão exibidos.
     */
    public void exibir(AtivoResumo ativo) {
        this.ativoAtual = ativo;
        atualizarConteudo();
    }

    private void atualizarConteudo() {
        conteudo.removeAll();

        List<Investimento> operacoes = controller.carregar(ativoAtual.getNomeAtivo(), ativoAtual.getTipo());
        double quantidadeAtual = controller.getQuantidadeAtual(operacoes);
        double totalInvestido = controller.getTotalInvestido(operacoes);
        double valorAtual = controller.getValorAtual(ativoAtual.getNomeAtivo(), ativoAtual.getTipo(), quantidadeAtual);
        String dataPrimeiraCompra = controller.getDataPrimeiraCompra(operacoes);

        conteudo.add(criarResumo(quantidadeAtual, totalInvestido, valorAtual, dataPrimeiraCompra));
        conteudo.add(Box.createVerticalStrut(Espacamentos.ESPACO_3));

        JLabel tituloOperacoes = new JLabel("Operações");
        tituloOperacoes.setFont(Fontes.SUBTITULO);
        tituloOperacoes.setForeground(Cores.TEXTO_PRIMARIO);
        conteudo.add(criarLinhaEsquerda(tituloOperacoes));
        conteudo.add(Box.createVerticalStrut(Espacamentos.ESPACO_2));

        ListaPanelUtil.adicionarItens(conteudo, operacoes, operacao -> {
            OperacaoListItemPanel item = new OperacaoListItemPanel(operacao);
            item.getBotaoEditar().addActionListener(e -> abrirFormularioEdicao(operacao));
            item.getBotaoRemover().addActionListener(e -> remover(operacao));
            return item;
        }, Espacamentos.ESPACO_2);

        conteudo.revalidate();
        conteudo.repaint();
    }

    private RoundedPanel criarResumo(double quantidadeAtual, double totalInvestido, double valorAtual, String dataPrimeiraCompra) {
        RoundedPanel resumo = new RoundedPanel(Espacamentos.RAIO, Cores.CARD_BRANCO) {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
            }

            @Override
            public float getAlignmentX() {
                return 0.5f;
            }
        };
        resumo.setLayout(new BoxLayout(resumo, BoxLayout.Y_AXIS));
        resumo.setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3));

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);
        cabecalho.setAlignmentX(0f);

        JPanel info = new JPanel(new FlowLayout(FlowLayout.LEFT, Espacamentos.ESPACO_2, 0));
        info.setOpaque(false);

        JLabel labelNome = new JLabel(ativoAtual.getNomeAtivo());
        labelNome.setFont(Fontes.TITULO);
        labelNome.setForeground(Cores.TEXTO_PRIMARIO);
        info.add(labelNome);
        info.add(Chip.fern(ativoAtual.getTipo().getSimpleName()));
        cabecalho.add(info, BorderLayout.WEST);

        String dataExibicao = dataPrimeiraCompra != null ? Formatadores.formatarData(dataPrimeiraCompra) : "-";
        JLabel labelDataPrimeiraCompra = new JLabel("Primeira compra em " + dataExibicao);
        labelDataPrimeiraCompra.setFont(Fontes.PEQUENO);
        labelDataPrimeiraCompra.setForeground(Cores.TEXTO_SECUNDARIO);
        cabecalho.add(labelDataPrimeiraCompra, BorderLayout.EAST);

        JPanel estatisticas = new JPanel(new FlowLayout(FlowLayout.LEFT, Espacamentos.ESPACO_4, 0));
        estatisticas.setOpaque(false);
        estatisticas.setAlignmentX(0f);
        estatisticas.setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_2, 0, 0, 0));

        double ganhoPerda = valorAtual - totalInvestido;
        estatisticas.add(criarEstatistica("Quantidade atual", String.valueOf(quantidadeAtual), Cores.TEXTO_PRIMARIO));
        estatisticas.add(criarEstatistica("Total investido", Formatadores.formatarMoeda(totalInvestido), Cores.TEXTO_PRIMARIO));
        estatisticas.add(criarEstatistica("Valor atual", Formatadores.formatarMoeda(valorAtual), Cores.TEXTO_PRIMARIO));
        estatisticas.add(criarEstatistica("Ganho/perda", Formatadores.formatarMoeda(ganhoPerda), ganhoPerda >= 0 ? Cores.CREDITO : Cores.DEBITO));

        resumo.add(cabecalho);
        resumo.add(estatisticas);

        return resumo;
    }

    private JPanel criarLinhaEsquerda(JLabel componente) {
        JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
            }
        };
        linha.setOpaque(false);
        linha.add(componente);
        return linha;
    }

    private JPanel criarEstatistica(String rotulo, String valor, java.awt.Color corValor) {
        JPanel painel = new JPanel();
        painel.setOpaque(false);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        JLabel labelRotulo = new JLabel(rotulo);
        labelRotulo.setFont(Fontes.PEQUENO);
        labelRotulo.setForeground(Cores.TEXTO_SECUNDARIO);

        JLabel labelValor = new JLabel(valor);
        labelValor.setFont(Fontes.DESTAQUE_NUMERICO);
        labelValor.setForeground(corValor);

        painel.add(labelRotulo);
        painel.add(labelValor);
        return painel;
    }

    private void abrirFormularioEdicao(Investimento investimento) {
        InvestimentoFormDialog dialog = new InvestimentoFormDialog(janelaPai());
        if (investimento.getConta() != null) {
            dialog.setContas(new Conta[]{investimento.getConta()});
        }
        dialog.preencherParaEdicao(investimento);
        dialog.getBotaoSalvar().addActionListener(e -> {
            List<String> erros = controller.atualizar(
                    investimento,
                    dialog.getCampoNomeAtivo().getText(),
                    dialog.getCampoValor().getText(),
                    dialog.getCampoQuantidade().getText(),
                    dialog.getCampoData().getText(),
                    (String) dialog.getComboOperacao().getSelectedItem());
            if (erros.isEmpty()) {
                dialog.dispose();
                atualizarConteudo();
            } else {
                ErroValidacaoDialog.exibir(dialog, erros);
            }
        });
        dialog.setVisible(true);
    }

    private void remover(Investimento investimento) {
        controller.remover(investimento, () -> ConfirmacaoDialog.confirmar(this, "Remover esta operação?"));
        atualizarConteudo();
    }

    private Frame janelaPai() {
        return (Frame) SwingUtilities.getWindowAncestor(this);
    }

    public JPanel getConteudo() {
        return conteudo;
    }
}
