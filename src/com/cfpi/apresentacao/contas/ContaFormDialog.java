package com.cfpi.apresentacao.contas;

import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.LinhaFormulario;
import com.cfpi.apresentacao.designsystem.Renderers;
import com.cfpi.apresentacao.designsystem.RoundedButton;
import com.cfpi.apresentacao.designsystem.RoundedTextField;
import com.cfpi.dominio.entidades.banco.Banco;
import com.cfpi.dominio.entidades.conta.Conta;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * Formulário (criação/edição) de uma {@link Conta}.
 */
public class ContaFormDialog extends JDialog {

    private final ContasViewModel viewModel = new ContasViewModel();
    private final JComboBox<String> comboTipo;
    private final RoundedTextField campoNumeroConta;
    private final RoundedTextField campoValorConta;
    private final RoundedTextField campoMoeda;
    private final RoundedTextField campoLimiteCredito;
    private final JPanel linhaLimiteCredito;
    private final JComboBox<Banco> comboBanco;
    private final RoundedButton botaoSalvar;

    public ContaFormDialog(Frame owner) {
        super(owner, "Conta", true);

        setLayout(new BorderLayout());

        JPanel formulario = new JPanel(new GridLayout(0, 1, 0, 8));
        formulario.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        formulario.setBackground(Cores.FUNDO_PRINCIPAL);

        comboTipo = new JComboBox<>(new String[]{"corrente", "poupança"});
        campoNumeroConta = new RoundedTextField(8, Cores.CARD_BRANCO, Cores.BORDA);
        campoValorConta = new RoundedTextField(8, Cores.CARD_BRANCO, Cores.BORDA);
        campoMoeda = new RoundedTextField(8, Cores.CARD_BRANCO, Cores.BORDA);
        campoLimiteCredito = new RoundedTextField(8, Cores.CARD_BRANCO, Cores.BORDA);

        comboBanco = new JComboBox<>();
        comboBanco.setRenderer(Renderers.exibindo(Banco.class, Banco::getNome, ""));

        comboTipo.addActionListener(e -> atualizarVisibilidadeLimiteCredito());

        linhaLimiteCredito = LinhaFormulario.criar("Limite de crédito", campoLimiteCredito);

        formulario.add(LinhaFormulario.criar("Tipo", comboTipo));
        formulario.add(LinhaFormulario.criar("Número da conta", campoNumeroConta));
        formulario.add(LinhaFormulario.criar("Saldo inicial", campoValorConta));
        formulario.add(LinhaFormulario.criar("Moeda", campoMoeda));
        formulario.add(linhaLimiteCredito);
        formulario.add(LinhaFormulario.criar("Banco", comboBanco));

        botaoSalvar = new RoundedButton("Salvar", 12, Cores.PRIMARIO, Cores.PRIMARIO_TEXTO);

        add(formulario, BorderLayout.CENTER);
        add(botaoSalvar, BorderLayout.SOUTH);

        pack();
        setSize(new Dimension(Math.max(getWidth(), 420), getHeight()));
        setLocationRelativeTo(owner);
    }

    /**
     * Mostra ou esconde o campo "Limite de crédito" de acordo com o tipo de
     * conta selecionado: o campo só é exibido para contas do tipo
     * {@code "corrente"}. A linha permanece no layout (apenas oculta) para
     * que o tamanho do diálogo não mude ao trocar o tipo da conta.
     */
    private void atualizarVisibilidadeLimiteCredito() {
        linhaLimiteCredito.setVisible(viewModel.exibeLimiteCredito((String) comboTipo.getSelectedItem()));
    }

    /**
     * Define os bancos disponíveis para a conta.
     *
     * @param bancos bancos cadastrados, exibidos pelo {@code nome}.
     */
    public void setBancos(Banco[] bancos) {
        comboBanco.removeAllItems();
        for (Banco banco : bancos) {
            comboBanco.addItem(banco);
        }
    }

    public JComboBox<String> getComboTipo() {
        return comboTipo;
    }

    public RoundedTextField getCampoNumeroConta() {
        return campoNumeroConta;
    }

    public RoundedTextField getCampoValorConta() {
        return campoValorConta;
    }

    public RoundedTextField getCampoMoeda() {
        return campoMoeda;
    }

    public RoundedTextField getCampoLimiteCredito() {
        return campoLimiteCredito;
    }

    /**
     * @return {@code true} se o campo "Limite de crédito" está visível no
     *         formulário (apenas para contas do tipo {@code "corrente"}).
     */
    public boolean isCampoLimiteCreditoVisivel() {
        return linhaLimiteCredito.isVisible();
    }

    /**
     * @return o texto do campo "Limite de crédito", ou {@code "0"} se o
     *         campo não estiver visível (conta do tipo {@code "poupança"},
     *         para a qual o limite de crédito não é obrigatório).
     */
    public String getLimiteCreditoTexto() {
        return isCampoLimiteCreditoVisivel() ? campoLimiteCredito.getText() : "0";
    }

    public JComboBox<Banco> getComboBanco() {
        return comboBanco;
    }

    public RoundedButton getBotaoSalvar() {
        return botaoSalvar;
    }

    /**
     * Preenche o formulário a partir de uma conta existente, para edição.
     *
     * @param conta conta cujos dados serão exibidos.
     */
    public void preencherParaEdicao(Conta conta) {
        comboTipo.setSelectedItem(conta.getTipo());
        campoNumeroConta.setText(conta.getNumeroConta());
        campoValorConta.setText(String.valueOf(conta.getValorConta()));
        campoMoeda.setText(conta.getMoeda());
        campoLimiteCredito.setText(String.valueOf(conta.getLimiteCredito()));
        comboBanco.setSelectedItem(conta.getBanco());
        atualizarVisibilidadeLimiteCredito();
    }
}
