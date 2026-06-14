package com.cfpi.apresentacao.objetivos;

import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.Fontes;
import com.cfpi.apresentacao.designsystem.RoundedButton;
import com.cfpi.apresentacao.designsystem.RoundedTextField;
import com.cfpi.dominio.entidades.objetivo.Objetivo;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Formulário de criação/edição de um {@link Objetivo}: nome e valor-alvo.
 */
public class ObjetivoFormDialog extends JDialog {

    private final RoundedTextField campoNome;
    private final RoundedTextField campoValor;
    private final RoundedButton botaoSalvar;

    public ObjetivoFormDialog(Frame owner) {
        super(owner, "Objetivo", true);
        setSize(420, 260);
        setLocationRelativeTo(owner);

        JPanel painel = new JPanel(new BorderLayout(12, 12));
        painel.setBackground(Cores.FUNDO_PRINCIPAL);
        painel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel campos = new JPanel(new GridLayout(2, 2, 8, 8));
        campos.setOpaque(false);

        JLabel labelNome = new JLabel("Nome");
        labelNome.setFont(Fontes.CORPO);
        labelNome.setForeground(Cores.TEXTO_PRIMARIO);
        campoNome = new RoundedTextField(8, Cores.CARD_BRANCO, Cores.BORDA);

        JLabel labelValor = new JLabel("Valor");
        labelValor.setFont(Fontes.CORPO);
        labelValor.setForeground(Cores.TEXTO_PRIMARIO);
        campoValor = new RoundedTextField(8, Cores.CARD_BRANCO, Cores.BORDA);

        campos.add(labelNome);
        campos.add(campoNome);
        campos.add(labelValor);
        campos.add(campoValor);

        botaoSalvar = new RoundedButton("Salvar", 12, Cores.PRIMARIO, Cores.PRIMARIO_TEXTO);

        painel.add(campos, BorderLayout.CENTER);
        painel.add(botaoSalvar, BorderLayout.SOUTH);

        setContentPane(painel);
    }

    /**
     * Preenche os campos do formulário com os dados de um objetivo
     * existente, para edição.
     *
     * @param objetivo objetivo a ser editado.
     */
    public void preencherParaEdicao(Objetivo objetivo) {
        campoNome.setText(objetivo.getNome());
        campoValor.setText(String.valueOf(objetivo.getValor()));
    }

    public RoundedTextField getCampoNome() {
        return campoNome;
    }

    public RoundedTextField getCampoValor() {
        return campoValor;
    }

    public RoundedButton getBotaoSalvar() {
        return botaoSalvar;
    }
}
