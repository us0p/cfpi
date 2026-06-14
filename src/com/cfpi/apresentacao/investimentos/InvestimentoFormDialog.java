package com.cfpi.apresentacao.investimentos;

import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.LinhaFormulario;
import com.cfpi.apresentacao.designsystem.Renderers;
import com.cfpi.apresentacao.designsystem.RoundedButton;
import com.cfpi.apresentacao.designsystem.RoundedTextField;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.investimento.Acao;
import com.cfpi.dominio.entidades.investimento.CDB;
import com.cfpi.dominio.entidades.investimento.CRA;
import com.cfpi.dominio.entidades.investimento.CRI;
import com.cfpi.dominio.entidades.investimento.Cripto;
import com.cfpi.dominio.entidades.investimento.DEB;
import com.cfpi.dominio.entidades.investimento.FII;
import com.cfpi.dominio.entidades.investimento.Investimento;
import com.cfpi.dominio.entidades.investimento.LCA;
import com.cfpi.dominio.entidades.investimento.LCI;
import com.cfpi.dominio.entidades.investimento.PGBL;
import com.cfpi.dominio.entidades.investimento.TesouroDireto;
import com.cfpi.dominio.entidades.investimento.VGBL;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * Formulário (criação/edição) de uma operação de {@link Investimento}.
 */
public class InvestimentoFormDialog extends JDialog {

    @SuppressWarnings("unchecked")
    public static final Class<? extends Investimento>[] TIPOS = new Class[]{
            Acao.class, CDB.class, CRA.class, CRI.class, Cripto.class, DEB.class,
            FII.class, LCA.class, LCI.class, PGBL.class, TesouroDireto.class, VGBL.class
    };

    private final JComboBox<Class<? extends Investimento>> comboTipo;
    private final JComboBox<Conta> comboConta;
    private final RoundedTextField campoNomeAtivo;
    private final RoundedTextField campoValor;
    private final RoundedTextField campoQuantidade;
    private final RoundedTextField campoData;
    private final JComboBox<String> comboOperacao;
    private final RoundedButton botaoSalvar;

    public InvestimentoFormDialog(Frame owner) {
        super(owner, "Investimento", true);

        setLayout(new BorderLayout());

        JPanel formulario = new JPanel(new GridLayout(0, 1, 0, 8));
        formulario.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        formulario.setBackground(Cores.FUNDO_PRINCIPAL);

        comboTipo = new JComboBox<>(TIPOS);
        comboTipo.setRenderer(Renderers.exibindo(Class.class, Class::getSimpleName, ""));

        comboConta = new JComboBox<>();
        comboConta.setRenderer(Renderers.exibindo(Conta.class, Conta::getNumeroConta, ""));

        campoNomeAtivo = new RoundedTextField(8, Cores.CARD_BRANCO, Cores.BORDA);
        campoValor = new RoundedTextField(8, Cores.CARD_BRANCO, Cores.BORDA);
        campoQuantidade = new RoundedTextField(8, Cores.CARD_BRANCO, Cores.BORDA);
        campoData = new RoundedTextField(8, Cores.CARD_BRANCO, Cores.BORDA);
        comboOperacao = new JComboBox<>(new String[]{"compra", "venda"});

        formulario.add(LinhaFormulario.criar("Tipo", comboTipo));
        formulario.add(LinhaFormulario.criar("Conta", comboConta));
        formulario.add(LinhaFormulario.criar("Ativo", campoNomeAtivo));
        formulario.add(LinhaFormulario.criar("Valor unitário", campoValor));
        formulario.add(LinhaFormulario.criar("Quantidade", campoQuantidade));
        formulario.add(LinhaFormulario.criar("Data (AAAA-MM-DD)", campoData));
        formulario.add(LinhaFormulario.criar("Operação", comboOperacao));

        botaoSalvar = new RoundedButton("Salvar", 12, Cores.PRIMARIO, Cores.PRIMARIO_TEXTO);

        add(formulario, BorderLayout.CENTER);
        add(botaoSalvar, BorderLayout.SOUTH);

        pack();
        setSize(new Dimension(Math.max(getWidth(), 420), getHeight()));
        setLocationRelativeTo(owner);
    }

    /**
     * Define as contas disponíveis para a operação de investimento.
     *
     * @param contas contas do usuário, exibidas pelo {@code numeroConta}.
     */
    public void setContas(Conta[] contas) {
        comboConta.removeAllItems();
        for (Conta conta : contas) {
            comboConta.addItem(conta);
        }
    }

    public JComboBox<Class<? extends Investimento>> getComboTipo() {
        return comboTipo;
    }

    public JComboBox<Conta> getComboConta() {
        return comboConta;
    }

    public RoundedTextField getCampoNomeAtivo() {
        return campoNomeAtivo;
    }

    public RoundedTextField getCampoValor() {
        return campoValor;
    }

    public RoundedTextField getCampoQuantidade() {
        return campoQuantidade;
    }

    public RoundedTextField getCampoData() {
        return campoData;
    }

    public JComboBox<String> getComboOperacao() {
        return comboOperacao;
    }

    public RoundedButton getBotaoSalvar() {
        return botaoSalvar;
    }

    /**
     * Preenche o formulário a partir de uma operação existente, para edição.
     * O tipo e a conta não podem ser alterados em uma edição.
     *
     * @param investimento operação cujos dados serão exibidos.
     */
    public void preencherParaEdicao(Investimento investimento) {
        comboTipo.setSelectedItem(investimento.getClass());
        comboTipo.setEnabled(false);
        comboConta.setSelectedItem(investimento.getConta());
        comboConta.setEnabled(false);
        campoNomeAtivo.setText(investimento.getNomeAtivo());
        campoValor.setText(String.valueOf(investimento.getValor()));
        campoQuantidade.setText(String.valueOf(investimento.getQuantidade()));
        campoData.setText(investimento.getData());
        comboOperacao.setSelectedItem(investimento.getOperacao());
    }
}
