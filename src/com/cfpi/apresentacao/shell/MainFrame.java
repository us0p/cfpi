package com.cfpi.apresentacao.shell;

import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.Fontes;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Janela principal da aplicação: combina a {@link Sidebar} com uma área
 * central que alterna entre as telas via {@link CardLayout}.
 *
 * <p>Cada tela começa registrada com um painel placeholder; os controllers
 * de cada etapa substituem esse placeholder pela view real através de
 * {@link #registrarPainel(Tela, JPanel)}.</p>
 */
public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel telas;
    private final Sidebar sidebar;
    private final Map<Tela, JPanel> paineis = new EnumMap<>(Tela.class);

    public MainFrame(AppSession appSession) {
        super("CFPI");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(960, 640));
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        telas = new JPanel(cardLayout);
        telas.setBackground(Cores.FUNDO_PRINCIPAL);

        sidebar = new Sidebar(this::mostrarTela);

        for (Tela tela : Tela.values()) {
            JPanel painel = criarPainelPlaceholder(tela);
            paineis.put(tela, painel);
            telas.add(painel, tela.name());
        }

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidebar, BorderLayout.WEST);
        getContentPane().add(telas, BorderLayout.CENTER);

        Tela telaInicial = appSession.getUsuarioAtual() == null ? Tela.CADASTRO_USUARIO : Tela.DASHBOARD;
        mostrarTela(telaInicial);
    }

    /**
     * Substitui o painel associado à tela informada, registrando-o no
     * {@link CardLayout}. Usado pelos controllers de cada etapa para
     * conectar suas views reais ao shell, no lugar do placeholder inicial.
     *
     * @param tela   tela cujo painel será substituído.
     * @param painel novo painel a ser exibido para a tela informada.
     */
    public void registrarPainel(Tela tela, JPanel painel) {
        JPanel painelAtual = paineis.get(tela);
        if (painelAtual != null) {
            telas.remove(painelAtual);
        }
        paineis.put(tela, painel);
        telas.add(painel, tela.name());
    }

    /**
     * Exibe a tela informada, atualizando também a seleção destacada na
     * sidebar.
     *
     * @param tela tela a ser exibida.
     */
    public void mostrarTela(Tela tela) {
        cardLayout.show(telas, tela.name());
        sidebar.setVisible(tela != Tela.CADASTRO_USUARIO);
        sidebar.selecionar(tela);

        JPanel painel = paineis.get(tela);
        if (painel instanceof TelaAtualizavel) {
            ((TelaAtualizavel) painel).atualizar();
        }
    }

    private JPanel criarPainelPlaceholder(Tela tela) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Cores.FUNDO_PRINCIPAL);
        JLabel label = new JLabel(tela.name() + " (em construção)");
        label.setFont(Fontes.SUBTITULO);
        label.setForeground(Cores.TEXTO_SECUNDARIO);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        painel.add(label, BorderLayout.CENTER);
        return painel;
    }
}
