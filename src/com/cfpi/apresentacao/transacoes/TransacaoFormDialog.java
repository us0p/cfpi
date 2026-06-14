package com.cfpi.apresentacao.transacoes;

import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.LinhaFormulario;
import com.cfpi.apresentacao.designsystem.Renderers;
import com.cfpi.apresentacao.designsystem.RoundedButton;
import com.cfpi.apresentacao.designsystem.RoundedTextField;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.transacao.Credito;
import com.cfpi.dominio.entidades.transacao.Debito;
import com.cfpi.dominio.entidades.transacao.Transacao;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * Formulário (criação/edição) de uma transação. O seletor de tipo
 * (Débito/Crédito) altera as opções de categoria disponíveis e a
 * visibilidade do seletor de tipo de débito (à vista/crédito).
 */
public class TransacaoFormDialog extends JDialog {

    public static final String TIPO_DEBITO = "Débito";
    public static final String TIPO_CREDITO = "Crédito";

    private final TransacoesViewModel viewModel;
    private final JComboBox<Conta> comboConta;
    private final JComboBox<String> comboTipoTransacao;
    private final JComboBox<String> comboCategoria;
    private final JComboBox<String> comboTipoDebito;
    private final RoundedTextField campoDescricao;
    private final RoundedTextField campoData;
    private final RoundedTextField campoValor;
    private final RoundedButton botaoSalvar;

    public TransacaoFormDialog(Frame owner, TransacoesViewModel viewModel) {
        super(owner, "Transação", true);
        this.viewModel = viewModel;

        setLayout(new BorderLayout());

        JPanel formulario = new JPanel(new GridLayout(0, 1, 0, 8));
        formulario.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        formulario.setBackground(Cores.FUNDO_PRINCIPAL);

        comboConta = new JComboBox<>();
        comboConta.setRenderer(Renderers.exibindo(Conta.class, Conta::getNumeroConta, ""));
        comboTipoTransacao = new JComboBox<>(new String[]{TIPO_DEBITO, TIPO_CREDITO});
        comboCategoria = new JComboBox<>();
        comboTipoDebito = new JComboBox<>();
        campoDescricao = new RoundedTextField(8, Cores.CARD_BRANCO, Cores.BORDA);
        campoData = new RoundedTextField(8, Cores.CARD_BRANCO, Cores.BORDA);
        campoValor = new RoundedTextField(8, Cores.CARD_BRANCO, Cores.BORDA);

        comboTipoTransacao.addActionListener(e -> atualizarParaTipo());
        comboConta.addActionListener(e -> repopularTipoDebito());

        formulario.add(LinhaFormulario.criar("Conta", comboConta));
        formulario.add(LinhaFormulario.criar("Tipo", comboTipoTransacao));
        formulario.add(LinhaFormulario.criar("Descrição", campoDescricao));
        formulario.add(LinhaFormulario.criar("Data (AAAA-MM-DD)", campoData));
        formulario.add(LinhaFormulario.criar("Valor", campoValor));
        formulario.add(LinhaFormulario.criar("Categoria", comboCategoria));
        formulario.add(LinhaFormulario.criar("Tipo de débito", comboTipoDebito));

        botaoSalvar = new RoundedButton("Salvar", 12, Cores.PRIMARIO, Cores.PRIMARIO_TEXTO);

        add(formulario, BorderLayout.CENTER);
        add(botaoSalvar, BorderLayout.SOUTH);

        atualizarParaTipo();
        repopularTipoDebito();
        pack();
        setSize(new Dimension(Math.max(getWidth(), 420), getHeight()));
        setLocationRelativeTo(owner);
    }

    private void atualizarParaTipo() {
        Class<? extends Transacao> tipo = isCredito() ? Credito.class : Debito.class;

        Object categoriaAtual = comboCategoria.getSelectedItem();
        comboCategoria.removeAllItems();
        for (String categoria : viewModel.categoriasParaTipo(tipo)) {
            comboCategoria.addItem(categoria);
        }
        if (categoriaAtual != null) {
            comboCategoria.setSelectedItem(categoriaAtual);
        }

        comboTipoDebito.setVisible(!isCredito());
    }

    private boolean isCredito() {
        return TIPO_CREDITO.equals(comboTipoTransacao.getSelectedItem());
    }

    /**
     * Repopula o seletor de tipo de débito (à vista/crédito) de acordo com o
     * tipo da conta selecionada: contas poupança não oferecem a opção
     * "crédito".
     */
    private void repopularTipoDebito() {
        Conta conta = (Conta) comboConta.getSelectedItem();
        String[] tipos = viewModel.tiposDebitoParaConta(conta);

        Object tipoAtual = comboTipoDebito.getSelectedItem();
        comboTipoDebito.removeAllItems();
        for (String tipo : tipos) {
            comboTipoDebito.addItem(tipo);
        }
        if (tipoAtual != null && List.of(tipos).contains(tipoAtual)) {
            comboTipoDebito.setSelectedItem(tipoAtual);
        }
    }

    /**
     * Define as contas disponíveis para a transação.
     *
     * @param contas contas do usuário, exibidas pelo {@code numeroConta}.
     */
    public void setContas(Conta[] contas) {
        comboConta.removeAllItems();
        for (Conta conta : contas) {
            comboConta.addItem(conta);
        }
        repopularTipoDebito();
    }

    public JComboBox<Conta> getComboConta() {
        return comboConta;
    }

    public JComboBox<String> getComboTipoTransacao() {
        return comboTipoTransacao;
    }

    public JComboBox<String> getComboCategoria() {
        return comboCategoria;
    }

    public JComboBox<String> getComboTipoDebito() {
        return comboTipoDebito;
    }

    public RoundedTextField getCampoDescricao() {
        return campoDescricao;
    }

    public RoundedTextField getCampoData() {
        return campoData;
    }

    public RoundedTextField getCampoValor() {
        return campoValor;
    }

    public RoundedButton getBotaoSalvar() {
        return botaoSalvar;
    }

    /**
     * Preenche o formulário a partir de uma transação existente, para edição.
     *
     * @param transacao transação cujos dados serão exibidos.
     */
    public void preencherParaEdicao(Transacao transacao) {
        comboConta.setSelectedItem(transacao.getConta());
        comboConta.setEnabled(false);
        comboTipoTransacao.setSelectedItem(transacao instanceof Debito ? TIPO_DEBITO : TIPO_CREDITO);
        campoDescricao.setText(transacao.getDescricao());
        campoData.setText(transacao.getData());
        campoValor.setText(String.valueOf(transacao.getValor()));
        comboCategoria.setSelectedItem(transacao.getCategoria());
        repopularTipoDebito();
        if (transacao instanceof Debito) {
            comboTipoDebito.setSelectedItem(((Debito) transacao).getTipo());
        }
    }
}
