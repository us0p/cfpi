package com.cfpi.apresentacao.contas;

import com.cfpi.apresentacao.comum.Formatadores;
import com.cfpi.apresentacao.designsystem.BarraProgresso;
import com.cfpi.apresentacao.designsystem.CampoLista;
import com.cfpi.apresentacao.designsystem.Chip;
import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.Espacamentos;
import com.cfpi.apresentacao.designsystem.Fontes;
import com.cfpi.apresentacao.designsystem.IconButton;
import com.cfpi.apresentacao.designsystem.RoundedPanel;
import com.cfpi.dominio.entidades.conta.Conta;

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
 * Linha de exibição de uma {@link Conta}: chip de tipo, número, banco/moeda,
 * saldo, barra de progresso do limite de crédito (quando houver) e ações de
 * editar/remover.
 */
public class ContaListItemPanel extends RoundedPanel {

    private final Conta conta;
    private final IconButton botaoEditar;
    private final IconButton botaoRemover;
    private JLabel labelLimiteValor;

    public ContaListItemPanel(Conta conta) {
        super(Espacamentos.RAIO, Cores.CARD_BRANCO);
        this.conta = conta;

        setLayout(new BorderLayout(Espacamentos.ESPACO_2, 0));
        setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_2, Espacamentos.ESPACO_2, Espacamentos.ESPACO_2, Espacamentos.ESPACO_2));

        JPanel main = new JPanel();
        main.setOpaque(false);
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        JPanel titulo = new JPanel(new FlowLayout(FlowLayout.LEFT, Espacamentos.ESPACO_2, 0));
        titulo.setOpaque(false);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        boolean ehPoupanca = "poupança".equals(conta.getTipo());
        titulo.add(ehPoupanca ? Chip.fern("Poupança") : Chip.rose("Corrente"));

        JLabel labelNumero = new JLabel(conta.getNumeroConta());
        labelNumero.setFont(Fontes.SUBTITULO);
        labelNumero.setForeground(Cores.TEXTO_PRIMARIO);
        titulo.add(labelNumero);

        String nomeBanco = conta.getBanco() != null ? conta.getBanco().getNome() : "Sem banco";
        JLabel labelDetalhes = new JLabel(nomeBanco + " · " + conta.getMoeda());
        labelDetalhes.setFont(Fontes.PEQUENO);
        labelDetalhes.setForeground(Cores.TEXTO_SECUNDARIO);
        titulo.add(labelDetalhes);

        main.add(titulo);
        main.add(Box.createVerticalStrut(Espacamentos.ESPACO_1));

        JPanel campoSaldo = CampoLista.criar("Saldo:", Formatadores.formatarMoeda(conta.getValorConta()));
        campoSaldo.setAlignmentX(Component.LEFT_ALIGNMENT);
        main.add(campoSaldo);

        if (conta.getLimiteCredito() > 0) {
            main.add(Box.createVerticalStrut(Espacamentos.ESPACO_1));

            JPanel rotuloLimite = new JPanel(new BorderLayout());
            rotuloLimite.setOpaque(false);
            rotuloLimite.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel labelLimiteRotulo = new JLabel("Limite de crédito utilizado");
            labelLimiteRotulo.setFont(Fontes.PEQUENO);
            labelLimiteRotulo.setForeground(Cores.TEXTO_SECUNDARIO);

            labelLimiteValor = new JLabel(Formatadores.formatarMoeda(conta.getLimiteCreditoUtilizado())
                    + " de " + Formatadores.formatarMoeda(conta.getLimiteCredito()));
            labelLimiteValor.setFont(Fontes.PEQUENO);
            labelLimiteValor.setForeground(Cores.TEXTO_SECUNDARIO);

            rotuloLimite.add(labelLimiteRotulo, BorderLayout.WEST);
            rotuloLimite.add(labelLimiteValor, BorderLayout.EAST);
            main.add(rotuloLimite);

            main.add(Box.createVerticalStrut(4));

            BarraProgresso barraLimite = new BarraProgresso(Cores.DEBITO, Cores.DESERT_SAND_40);
            barraLimite.setAlignmentX(Component.LEFT_ALIGNMENT);
            barraLimite.setPercentual(conta.getLimiteCreditoUtilizado() / conta.getLimiteCredito());
            main.add(barraLimite);
        }

        botaoEditar = IconButton.editar();
        botaoRemover = IconButton.remover();
        if (conta.getLimiteCreditoUtilizado() > 0) {
            botaoRemover.setEnabled(false);
            botaoRemover.setToolTipText("Não é possível remover: limite de crédito utilizado.");
        }

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, Espacamentos.ESPACO_1, 0));
        acoes.setOpaque(false);
        acoes.add(botaoEditar);
        acoes.add(botaoRemover);

        add(main, BorderLayout.CENTER);
        add(acoes, BorderLayout.EAST);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }

    public Conta getConta() {
        return conta;
    }

    /**
     * @return o rótulo "X de Y" do limite de crédito utilizado, ou
     *         {@code null} se {@code conta.getLimiteCredito() <= 0}.
     */
    public JLabel getLabelLimiteValor() {
        return labelLimiteValor;
    }

    public IconButton getBotaoEditar() {
        return botaoEditar;
    }

    public IconButton getBotaoRemover() {
        return botaoRemover;
    }
}
