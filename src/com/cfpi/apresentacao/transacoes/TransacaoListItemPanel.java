package com.cfpi.apresentacao.transacoes;

import com.cfpi.apresentacao.comum.Formatadores;
import com.cfpi.apresentacao.designsystem.CampoLista;
import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.Espacamentos;
import com.cfpi.apresentacao.designsystem.Fontes;
import com.cfpi.apresentacao.designsystem.IconBadge;
import com.cfpi.apresentacao.designsystem.IconButton;
import com.cfpi.apresentacao.designsystem.RoundedPanel;
import com.cfpi.dominio.entidades.transacao.Debito;
import com.cfpi.dominio.entidades.transacao.Transacao;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Linha de exibição de uma {@link Transacao}: badge de direção
 * (crédito/débito), campos com tipo/categoria/data/descrição, valor e ações
 * de editar/remover.
 */
public class TransacaoListItemPanel extends RoundedPanel {

    private final Transacao transacao;
    private final IconButton botaoEditar;
    private final IconButton botaoRemover;

    public TransacaoListItemPanel(Transacao transacao) {
        super(Espacamentos.RAIO, Cores.CARD_BRANCO);
        this.transacao = transacao;

        setLayout(new BorderLayout(Espacamentos.ESPACO_2, 0));
        setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_2, Espacamentos.ESPACO_2, Espacamentos.ESPACO_2, Espacamentos.ESPACO_2));

        boolean ehDebito = transacao instanceof Debito;
        IconBadge badge = ehDebito ? IconBadge.debito() : IconBadge.credito();

        JPanel main = new JPanel(new FlowLayout(FlowLayout.LEFT, Espacamentos.ESPACO_3, 0));
        main.setOpaque(false);

        if (ehDebito) {
            String tipo = ((Debito) transacao).getTipo();
            main.add(CampoLista.criar("Débito:", mapearTipoDebito(tipo)));
        } else {
            JLabel labelCredito = new JLabel("Crédito");
            labelCredito.setFont(Fontes.MEDIO_NEGRITO);
            labelCredito.setForeground(Cores.TEXTO_PRIMARIO);
            main.add(labelCredito);
        }
        main.add(CampoLista.criar("Categoria:", transacao.getCategoria()));
        main.add(CampoLista.criar("Data:", Formatadores.formatarData(transacao.getData())));
        main.add(CampoLista.criar("Descrição:", transacao.getDescricao()));

        double valorExibido = ehDebito ? -transacao.getValor() : transacao.getValor();
        JLabel labelValor = new JLabel(Formatadores.formatarMoeda(valorExibido));
        labelValor.setFont(Fontes.DESTAQUE_NUMERICO);
        labelValor.setForeground(ehDebito ? Cores.DEBITO : Cores.CREDITO);

        botaoEditar = IconButton.editar();
        botaoRemover = IconButton.remover();

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, Espacamentos.ESPACO_1, 0));
        acoes.setOpaque(false);
        acoes.add(botaoEditar);
        acoes.add(botaoRemover);

        JPanel direita = new JPanel();
        direita.setOpaque(false);
        direita.setLayout(new BoxLayout(direita, BoxLayout.X_AXIS));
        direita.add(labelValor);
        direita.add(Box.createHorizontalStrut(Espacamentos.ESPACO_2));
        direita.add(acoes);

        add(badge, BorderLayout.WEST);
        add(main, BorderLayout.CENTER);
        add(direita, BorderLayout.EAST);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }

    private String mapearTipoDebito(String tipo) {
        if (tipo == null) {
            return "";
        }
        switch (tipo.trim().toLowerCase()) {
            case "credito":
                return "Crédito";
            case "avista":
                return "À vista";
            default:
                return tipo;
        }
    }

    public Transacao getTransacao() {
        return transacao;
    }

    public IconButton getBotaoEditar() {
        return botaoEditar;
    }

    public IconButton getBotaoRemover() {
        return botaoRemover;
    }
}
