package com.cfpi.apresentacao.shell;

import com.cfpi.apresentacao.designsystem.Cores;
import com.cfpi.apresentacao.designsystem.Espacamentos;
import com.cfpi.apresentacao.designsystem.Fontes;
import com.cfpi.apresentacao.designsystem.SidebarButton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Barra lateral escura com navegação entre as telas principais da aplicação,
 * equivalente a {@code .sidebar} do design.
 */
public class Sidebar extends JPanel {

    private final Map<Tela, SidebarButton> botoes = new EnumMap<>(Tela.class);

    public Sidebar(Consumer<Tela> aoNavegar) {
        setLayout(new BorderLayout(0, Espacamentos.ESPACO_3));
        setBackground(Cores.SIDEBAR_FUNDO);
        setPreferredSize(new Dimension(240, 0));
        setBorder(BorderFactory.createEmptyBorder(Espacamentos.ESPACO_3, Espacamentos.ESPACO_2, Espacamentos.ESPACO_3, Espacamentos.ESPACO_2));

        JLabel titulo = new JLabel("CFPI");
        titulo.setForeground(Cores.BRANCO);
        titulo.setFont(Fontes.SUBTITULO);
        titulo.setHorizontalAlignment(SwingConstants.LEFT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, Espacamentos.ESPACO_1, 0, Espacamentos.ESPACO_1));

        JPanel navegacao = new JPanel();
        navegacao.setOpaque(false);
        navegacao.setLayout(new BoxLayout(navegacao, BoxLayout.Y_AXIS));

        adicionarBotao(navegacao, Tela.DASHBOARD, "Dashboard", aoNavegar);
        adicionarBotao(navegacao, Tela.CONTAS, "Contas", aoNavegar);
        adicionarBotao(navegacao, Tela.TRANSACOES, "Transações", aoNavegar);
        adicionarBotao(navegacao, Tela.OBJETIVOS, "Objetivos", aoNavegar);
        adicionarBotao(navegacao, Tela.INVESTIMENTOS, "Investimentos", aoNavegar);

        JPanel rodape = new JPanel();
        rodape.setOpaque(false);
        rodape.setLayout(new BoxLayout(rodape, BoxLayout.Y_AXIS));
        rodape.add(SidebarButton.rodape("Importar"));
        rodape.add(Box.createVerticalStrut(Espacamentos.ESPACO_1));
        rodape.add(SidebarButton.rodape("Exportar"));

        add(titulo, BorderLayout.NORTH);
        add(navegacao, BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);
    }

    private void adicionarBotao(JPanel container, Tela tela, String texto, Consumer<Tela> aoNavegar) {
        if (container.getComponentCount() > 0) {
            container.add(Box.createVerticalStrut(Espacamentos.ESPACO_1));
        }
        SidebarButton botao = new SidebarButton(texto);
        botao.addActionListener(e -> aoNavegar.accept(tela));
        botoes.put(tela, botao);
        container.add(botao);
    }

    /**
     * Marca como selecionado o botão correspondente à tela informada,
     * desmarcando os demais. Telas sem botão na sidebar (ex:
     * {@link Tela#CADASTRO_USUARIO}, {@link Tela#INVESTIMENTO_DETALHES}) não
     * alteram a seleção.
     *
     * @param tela tela a ser destacada na sidebar.
     */
    public void selecionar(Tela tela) {
        for (Map.Entry<Tela, SidebarButton> entrada : botoes.entrySet()) {
            entrada.getValue().setSelecionado(entrada.getKey() == tela);
        }
    }
}
