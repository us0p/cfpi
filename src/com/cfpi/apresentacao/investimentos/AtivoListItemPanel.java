package com.cfpi.apresentacao.investimentos;

import com.cfpi.apresentacao.comum.Formatadores;
import com.cfpi.apresentacao.designsystem.Chip;
import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.Espacamentos;
import com.cfpi.apresentacao.designsystem.Fontes;
import com.cfpi.apresentacao.designsystem.IconButton;
import com.cfpi.apresentacao.designsystem.RoundedPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Linha de exibição de um {@link AtivoResumo}: nome do ativo, chip de tipo,
 * quantidade atual, valor atual, ganho/perda (colorido) e botão para ver
 * detalhes.
 */
public class AtivoListItemPanel extends RoundedPanel {

    private final AtivoResumo ativo;
    private final IconButton botaoDetalhes;

    public AtivoListItemPanel(AtivoResumo ativo) {
        super(Espacamentos.RAIO, Cores.CARD_BRANCO);
        this.ativo = ativo;

        setLayout(new BorderLayout(Espacamentos.ESPACO_2, 0));
        setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_2, Espacamentos.ESPACO_2, Espacamentos.ESPACO_2, Espacamentos.ESPACO_2));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel labelNome = new JLabel(ativo.getNomeAtivo());
        labelNome.setFont(Fontes.SUBTITULO);
        labelNome.setForeground(Cores.TEXTO_PRIMARIO);
        labelNome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel linhaTipo = new JPanel(new FlowLayout(FlowLayout.LEFT, Espacamentos.ESPACO_1, 0));
        linhaTipo.setOpaque(false);
        linhaTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        linhaTipo.add(Chip.neutro(ativo.getTipo().getSimpleName()));

        JLabel labelQuantidade = new JLabel("Quantidade: " + ativo.getQuantidadeAtual());
        labelQuantidade.setFont(Fontes.PEQUENO);
        labelQuantidade.setForeground(Cores.TEXTO_SECUNDARIO);
        linhaTipo.add(labelQuantidade);

        info.add(labelNome);
        info.add(Box.createVerticalStrut(Espacamentos.ESPACO_1));
        info.add(linhaTipo);

        JPanel valores = new JPanel();
        valores.setOpaque(false);
        valores.setLayout(new BoxLayout(valores, BoxLayout.Y_AXIS));

        JLabel labelValorAtual = new JLabel(Formatadores.formatarMoeda(ativo.getValorAtual()));
        labelValorAtual.setFont(Fontes.DESTAQUE_NUMERICO);
        labelValorAtual.setForeground(Cores.TEXTO_PRIMARIO);
        labelValorAtual.setAlignmentX(Component.RIGHT_ALIGNMENT);

        boolean ganho = ativo.getGanhoPerda() >= 0;
        JLabel labelGanhoPerda = new JLabel((ganho ? "+ " : "- ") + Formatadores.formatarMoeda(Math.abs(ativo.getGanhoPerda())));
        labelGanhoPerda.setFont(Fontes.PEQUENO_NEGRITO);
        labelGanhoPerda.setForeground(ganho ? Cores.CREDITO : Cores.DEBITO);
        labelGanhoPerda.setAlignmentX(Component.RIGHT_ALIGNMENT);

        valores.add(labelValorAtual);
        valores.add(labelGanhoPerda);

        botaoDetalhes = IconButton.visualizar();

        JPanel direita = new JPanel();
        direita.setOpaque(false);
        direita.setLayout(new BoxLayout(direita, BoxLayout.X_AXIS));
        direita.add(valores);
        direita.add(Box.createHorizontalStrut(Espacamentos.ESPACO_2));
        direita.add(botaoDetalhes);

        add(info, BorderLayout.CENTER);
        add(direita, BorderLayout.EAST);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }

    public AtivoResumo getAtivo() {
        return ativo;
    }

    public IconButton getBotaoDetalhes() {
        return botaoDetalhes;
    }
}
