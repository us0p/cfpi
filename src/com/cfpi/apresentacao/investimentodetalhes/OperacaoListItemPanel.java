package com.cfpi.apresentacao.investimentodetalhes;

import com.cfpi.apresentacao.comum.Formatadores;
import com.cfpi.apresentacao.designsystem.CampoLista;
import com.cfpi.apresentacao.designsystem.Chip;
import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.Espacamentos;
import com.cfpi.apresentacao.designsystem.IconButton;
import com.cfpi.apresentacao.designsystem.RoundedPanel;
import com.cfpi.dominio.entidades.investimento.Investimento;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Linha de exibição de uma operação (compra/venda) de um {@link Investimento}:
 * chip de operação, data, valor unitário, quantidade, total e (para vendas)
 * imposto/valor realizado, além de ações de editar/remover.
 */
public class OperacaoListItemPanel extends RoundedPanel {

    private final Investimento operacao;
    private final IconButton botaoEditar;
    private final IconButton botaoRemover;

    public OperacaoListItemPanel(Investimento operacao) {
        super(Espacamentos.RAIO, Cores.CARD_BRANCO);
        this.operacao = operacao;

        setLayout(new BorderLayout(Espacamentos.ESPACO_2, 0));
        setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_2, Espacamentos.ESPACO_2, Espacamentos.ESPACO_2, Espacamentos.ESPACO_2));

        boolean ehVenda = "venda".equalsIgnoreCase(operacao.getOperacao());
        Chip chip = ehVenda ? Chip.rose("Venda") : Chip.fern("Compra");

        JPanel main = new JPanel(new FlowLayout(FlowLayout.LEFT, Espacamentos.ESPACO_3, 0));
        main.setOpaque(false);

        main.add(CampoLista.criar("Data:", Formatadores.formatarData(operacao.getData())));
        main.add(CampoLista.criar("Valor unitário:", Formatadores.formatarMoeda(operacao.getValor())));
        main.add(CampoLista.criar("Quantidade:", String.valueOf(operacao.getQuantidade())));
        main.add(CampoLista.criar("Total:", Formatadores.formatarMoeda(operacao.getValor() * operacao.getQuantidade())));
        if (ehVenda) {
            main.add(CampoLista.criar("Imposto:", Formatadores.formatarMoeda(operacao.getImposto())));
            main.add(CampoLista.criar("Valor realizado:", Formatadores.formatarMoeda(operacao.getValorRealizado())));
        }

        botaoEditar = IconButton.editar();
        botaoRemover = IconButton.remover();

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, Espacamentos.ESPACO_1, 0));
        acoes.setOpaque(false);
        acoes.add(botaoEditar);
        acoes.add(botaoRemover);

        add(chip, BorderLayout.WEST);
        add(main, BorderLayout.CENTER);
        add(acoes, BorderLayout.EAST);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }

    public Investimento getOperacao() {
        return operacao;
    }

    public IconButton getBotaoEditar() {
        return botaoEditar;
    }

    public IconButton getBotaoRemover() {
        return botaoRemover;
    }
}
