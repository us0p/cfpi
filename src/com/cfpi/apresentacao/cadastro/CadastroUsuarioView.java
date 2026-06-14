package com.cfpi.apresentacao.cadastro;

import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.Espacamentos;
import com.cfpi.apresentacao.designsystem.Fontes;
import com.cfpi.apresentacao.designsystem.RoundedButton;
import com.cfpi.apresentacao.designsystem.RoundedPanel;
import com.cfpi.apresentacao.designsystem.RoundedTextField;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Tela de cadastro de usuário: card centralizado com formulário de nome,
 * CPF, telefone e data de nascimento, com mensagens de erro exibidas junto a
 * cada campo.
 */
public class CadastroUsuarioView extends JPanel {

    private static final int LARGURA_CARD = 480;
    private static final int LARGURA_CAMPO = LARGURA_CARD - 2 * Espacamentos.ESPACO_3;
    private static final int LARGURA_CAMPO_METADE = (LARGURA_CAMPO - Espacamentos.ESPACO_2) / 2;
    private static final int ALTURA_CAMPO = 40;

    private final RoundedTextField campoNome;
    private final RoundedTextField campoCpf;
    private final RoundedTextField campoTelefone;
    private final RoundedTextField campoDataNascimento;
    private final RoundedButton botaoCadastrar;
    private final JLabel erroNome;
    private final JLabel erroCpf;
    private final JLabel erroTelefone;
    private final JLabel erroDataNascimento;
    private final JLabel erroGeral;

    public CadastroUsuarioView(CadastroUsuarioController controller) {
        setLayout(new GridBagLayout());
        setBackground(Cores.FUNDO_PRINCIPAL);
        setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3));

        RoundedPanel card = new RoundedPanel(Espacamentos.RAIO, Cores.CARD_BRANCO);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3, Espacamentos.ESPACO_3));

        JLabel titulo = new JLabel("Criar conta", SwingConstants.CENTER);
        titulo.setFont(Fontes.TITULO);
        titulo.setForeground(Cores.TEXTO_PRIMARIO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Preencha seus dados para começar a usar o CFPI.", SwingConstants.CENTER);
        subtitulo.setFont(Fontes.CORPO);
        subtitulo.setForeground(Cores.TEXTO_SECUNDARIO);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoNome = criarCampo();
        campoCpf = criarCampo();
        campoTelefone = criarCampo();
        campoDataNascimento = criarCampo();

        erroNome = criarLabelErro();
        erroCpf = criarLabelErro();
        erroTelefone = criarLabelErro();
        erroDataNascimento = criarLabelErro();

        JPanel linhaNome = criarLinha("Nome completo", campoNome, erroNome, LARGURA_CAMPO);
        campoNome.setToolTipText("Nome completo (apenas letras e espaços)");

        JPanel linhaCpf = criarLinha("CPF", campoCpf, erroCpf, LARGURA_CAMPO_METADE);
        campoCpf.setToolTipText("CPF com 11 dígitos numéricos");

        JPanel linhaTelefone = criarLinha("Telefone", campoTelefone, erroTelefone, LARGURA_CAMPO_METADE);
        campoTelefone.setToolTipText("Telefone com 11 dígitos numéricos");

        JPanel linhaCpfTelefone = new JPanel();
        linhaCpfTelefone.setOpaque(false);
        linhaCpfTelefone.setLayout(new BoxLayout(linhaCpfTelefone, BoxLayout.X_AXIS));
        linhaCpfTelefone.setAlignmentX(Component.CENTER_ALIGNMENT);
        linhaCpfTelefone.add(linhaCpf);
        linhaCpfTelefone.add(Box.createHorizontalStrut(Espacamentos.ESPACO_2));
        linhaCpfTelefone.add(linhaTelefone);

        JPanel linhaDataNascimento = criarLinha("Data de nascimento", campoDataNascimento, erroDataNascimento, LARGURA_CAMPO);
        campoDataNascimento.setToolTipText("Data de nascimento no formato AAAA-MM-DD");

        erroGeral = new JLabel(" ", SwingConstants.CENTER);
        erroGeral.setFont(Fontes.PEQUENO);
        erroGeral.setForeground(Cores.DEBITO);
        erroGeral.setAlignmentX(Component.CENTER_ALIGNMENT);
        erroGeral.setMaximumSize(new Dimension(LARGURA_CAMPO, Integer.MAX_VALUE));
        erroGeral.setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_1, 0, 0, 0));
        erroGeral.setVisible(false);

        botaoCadastrar = new RoundedButton("Criar conta", Espacamentos.RAIO, Cores.PRIMARIO, Cores.PRIMARIO_TEXTO);
        botaoCadastrar.setPreferredSize(new Dimension(LARGURA_CAMPO, 44));
        botaoCadastrar.setMaximumSize(new Dimension(LARGURA_CAMPO, 44));
        botaoCadastrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoCadastrar.addActionListener(e -> aoClicarCadastrar(controller));

        card.add(titulo);
        card.add(Box.createVerticalStrut(Espacamentos.ESPACO_1));
        card.add(subtitulo);
        card.add(Box.createVerticalStrut(Espacamentos.ESPACO_3));
        card.add(linhaNome);
        card.add(Box.createVerticalStrut(Espacamentos.ESPACO_2));
        card.add(linhaCpfTelefone);
        card.add(Box.createVerticalStrut(Espacamentos.ESPACO_2));
        card.add(linhaDataNascimento);
        card.add(Box.createVerticalStrut(Espacamentos.ESPACO_2));
        card.add(erroGeral);
        card.add(Box.createVerticalStrut(Espacamentos.ESPACO_1));
        card.add(botaoCadastrar);

        add(card, new GridBagConstraints());
    }

    private void aoClicarCadastrar(CadastroUsuarioController controller) {
        CadastroUsuarioViewModel viewModel = CadastroUsuarioViewModel.criar(
                campoNome.getText(), campoCpf.getText(), campoTelefone.getText(), campoDataNascimento.getText());

        List<String> erros = controller.cadastrar(viewModel);
        exibirErros(erros);
    }

    private void exibirErros(List<String> erros) {
        limparErros();
        for (String erro : erros) {
            switch (CadastroUsuarioViewModel.campoDoErro(erro)) {
                case NOME:
                    exibirErroCampo(campoNome, erroNome, erro);
                    break;
                case CPF:
                    exibirErroCampo(campoCpf, erroCpf, erro);
                    break;
                case TELEFONE:
                    exibirErroCampo(campoTelefone, erroTelefone, erro);
                    break;
                case DATA_NASCIMENTO:
                    exibirErroCampo(campoDataNascimento, erroDataNascimento, erro);
                    break;
                default:
                    erroGeral.setText(erro);
                    erroGeral.setVisible(true);
                    break;
            }
        }
    }

    private void limparErros() {
        for (RoundedTextField campo : new RoundedTextField[]{campoNome, campoCpf, campoTelefone, campoDataNascimento}) {
            campo.setCorBorda(Cores.BORDA);
        }
        for (JLabel erro : new JLabel[]{erroNome, erroCpf, erroTelefone, erroDataNascimento}) {
            erro.setText(" ");
            erro.setVisible(false);
        }
        erroGeral.setText(" ");
        erroGeral.setVisible(false);
    }

    private void exibirErroCampo(RoundedTextField campo, JLabel erro, String mensagem) {
        campo.setCorBorda(Cores.DEBITO);
        int largura = erro.getMaximumSize().width;
        erro.setText("<html><table width='" + largura + "'><tr><td>" + mensagem + "</td></tr></table></html>");
        erro.setVisible(true);
    }

    private RoundedTextField criarCampo() {
        return new RoundedTextField(Espacamentos.RAIO, Cores.CARD_BRANCO, Cores.BORDA);
    }

    private JLabel criarLabelErro() {
        JLabel erro = new JLabel(" ");
        erro.setFont(Fontes.PEQUENO);
        erro.setForeground(Cores.DEBITO);
        erro.setAlignmentX(Component.LEFT_ALIGNMENT);
        erro.setVisible(false);
        return erro;
    }

    private JPanel criarLinha(String rotulo, RoundedTextField campo, JLabel erro, int largura) {
        JPanel linha = new JPanel();
        linha.setOpaque(false);
        linha.setLayout(new BoxLayout(linha, BoxLayout.Y_AXIS));
        linha.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(rotulo);
        label.setFont(Fontes.PEQUENO_NEGRITO);
        label.setForeground(Cores.TEXTO_PRIMARIO);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setPreferredSize(new Dimension(largura, ALTURA_CAMPO));
        campo.setMaximumSize(new Dimension(largura, ALTURA_CAMPO));

        erro.setMaximumSize(new Dimension(largura, Integer.MAX_VALUE));
        erro.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        linha.add(label);
        linha.add(Box.createVerticalStrut(4));
        linha.add(campo);
        linha.add(erro);
        return linha;
    }

    public RoundedTextField getCampoNome() {
        return campoNome;
    }

    public RoundedTextField getCampoCpf() {
        return campoCpf;
    }

    public RoundedTextField getCampoTelefone() {
        return campoTelefone;
    }

    public RoundedTextField getCampoDataNascimento() {
        return campoDataNascimento;
    }

    public RoundedButton getBotaoCadastrar() {
        return botaoCadastrar;
    }
}
