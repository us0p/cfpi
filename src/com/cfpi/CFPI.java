package com.cfpi;

import com.cfpi.aplicacao.servicos.AvaliadorDeAtivosServico;
import com.cfpi.aplicacao.servicos.CalculadoraPrazoObjetivoServico;
import com.cfpi.aplicacao.servicos.ImportExportServico;
import com.cfpi.apresentacao.cadastro.CadastroUsuarioController;
import com.cfpi.apresentacao.cadastro.CadastroUsuarioView;
import com.cfpi.apresentacao.contas.ContasController;
import com.cfpi.apresentacao.contas.ContasView;
import com.cfpi.apresentacao.contas.ContasViewModel;
import com.cfpi.apresentacao.dashboard.DashboardController;
import com.cfpi.apresentacao.dashboard.DashboardView;
import com.cfpi.apresentacao.dashboard.DashboardViewModel;
import com.cfpi.apresentacao.investimentodetalhes.InvestimentoDetalhesController;
import com.cfpi.apresentacao.investimentodetalhes.InvestimentoDetalhesView;
import com.cfpi.apresentacao.investimentodetalhes.InvestimentoDetalhesViewModel;
import com.cfpi.apresentacao.investimentos.InvestimentosController;
import com.cfpi.apresentacao.investimentos.InvestimentosView;
import com.cfpi.apresentacao.investimentos.InvestimentosViewModel;
import com.cfpi.apresentacao.objetivos.ObjetivosController;
import com.cfpi.apresentacao.objetivos.ObjetivosView;
import com.cfpi.apresentacao.objetivos.ObjetivosViewModel;
import com.cfpi.apresentacao.shell.AppSession;
import com.cfpi.apresentacao.shell.MainFrame;
import com.cfpi.apresentacao.shell.Tela;
import com.cfpi.apresentacao.transacoes.TransacoesController;
import com.cfpi.apresentacao.transacoes.TransacoesView;
import com.cfpi.apresentacao.transacoes.TransacoesViewModel;
import com.cfpi.dominio.entidades.banco.BancoStoreImpl;
import com.cfpi.dominio.entidades.usuario.Usuario;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;

public class CFPI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppSession[] sessao = new AppSession[1];
            AppSession appSession = new AppSession(
                    new CalculadoraPrazoObjetivoServico(),
                    new AvaliadorDeAtivosServico(() -> sessao[0].getUsuarioAtual()));
            sessao[0] = appSession;

            ImportExportServico importExport = new ImportExportServico();
            Runnable aoExportar = () -> importExport.exportar(appSession);

            MainFrame mainFrame = new MainFrame(appSession, aoExportar);

            Consumer<Tela> navegador = tela -> {
                if (tela == Tela.DASHBOARD && appSession.getUsuarioAtual() != null) {
                    registrarTelasAutenticadas(mainFrame, appSession, importExport);
                }
                mainFrame.mostrarTela(tela);
            };

            CadastroUsuarioController cadastroController = new CadastroUsuarioController(appSession, navegador, importExport);
            mainFrame.registrarPainel(Tela.CADASTRO_USUARIO, new CadastroUsuarioView(cadastroController));
            mainFrame.mostrarTela(Tela.CADASTRO_USUARIO);

            mainFrame.setVisible(true);
        });
    }

    private static BancoStoreImpl carregarBancosSeed(ImportExportServico importExport, Usuario usuario) {
        try {
            File arquivo = new File("lista_bancos.json");
            if (arquivo.exists()) {
                String json = new String(Files.readAllBytes(arquivo.toPath()), StandardCharsets.UTF_8);
                return importExport.carregarBancosDoArquivoSeed(json, usuario);
            }
        } catch (Exception ignored) {
        }
        return new BancoStoreImpl(usuario);
    }

    /**
     * Registra as telas que dependem de um {@code Usuario} autenticado,
     * chamadas após a conclusão do cadastro.
     */
    private static void registrarTelasAutenticadas(MainFrame mainFrame, AppSession appSession, ImportExportServico importExport) {
        TransacoesController transacoesController = new TransacoesController(appSession.getUsuarioAtual(), new TransacoesViewModel());
        mainFrame.registrarPainel(Tela.TRANSACOES, new TransacoesView(transacoesController, new TransacoesViewModel()));

        ObjetivosController objetivosController = new ObjetivosController(appSession.getUsuarioAtual(), new ObjetivosViewModel());
        mainFrame.registrarPainel(Tela.OBJETIVOS, new ObjetivosView(objetivosController));

        BancoStoreImpl bancoStore = appSession.getBancoStore();
        if (bancoStore == null) {
            bancoStore = carregarBancosSeed(importExport, appSession.getUsuarioAtual());
            appSession.setBancoStore(bancoStore);
        }
        ContasController contasController = new ContasController(appSession.getUsuarioAtual(), bancoStore, new ContasViewModel());
        mainFrame.registrarPainel(Tela.CONTAS, new ContasView(contasController));

        InvestimentoDetalhesController investimentoDetalhesController = new InvestimentoDetalhesController(
                appSession.getUsuarioAtual(), appSession.getAvaliadorDeAtivos(), new InvestimentoDetalhesViewModel());
        InvestimentoDetalhesView investimentoDetalhesView = new InvestimentoDetalhesView(
                investimentoDetalhesController, () -> mainFrame.mostrarTela(Tela.INVESTIMENTOS));
        mainFrame.registrarPainel(Tela.INVESTIMENTO_DETALHES, investimentoDetalhesView);

        InvestimentosController investimentosController = new InvestimentosController(
                appSession.getUsuarioAtual(), appSession.getAvaliadorDeAtivos(), new InvestimentosViewModel());
        mainFrame.registrarPainel(Tela.INVESTIMENTOS, new InvestimentosView(investimentosController, new InvestimentosViewModel(), ativo -> {
            investimentoDetalhesView.exibir(ativo);
            mainFrame.mostrarTela(Tela.INVESTIMENTO_DETALHES);
        }));

        DashboardController dashboardController = new DashboardController(
                appSession,
                objetivosController.getOrdemSessao(),
                new DashboardViewModel());
        mainFrame.registrarPainel(Tela.DASHBOARD, new DashboardView(dashboardController));
    }
}
