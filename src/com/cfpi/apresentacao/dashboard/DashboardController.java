package com.cfpi.apresentacao.dashboard;

import com.cfpi.apresentacao.shell.AppSession;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.objetivo.Objetivo;

import java.time.LocalDate;
import java.util.List;

/**
 * Orquestra o carregamento dos dados exibidos na tela de Dashboard.
 */
public class DashboardController {

    private final AppSession appSession;
    private final List<Objetivo> ordemObjetivos;
    private final DashboardViewModel viewModel;

    public DashboardController(AppSession appSession, List<Objetivo> ordemObjetivos, DashboardViewModel viewModel) {
        this.appSession = appSession;
        this.ordemObjetivos = ordemObjetivos;
        this.viewModel = viewModel;
    }

    /**
     * Carrega os dados consolidados do Dashboard para o usuário atual.
     *
     * @param filtroCategoria categoria usada para filtrar os gráficos de
     *                         gastos por categoria e crescimento de
     *                         patrimônio ({@code null} ou {@code "todas"}
     *                         para não filtrar).
     * @return os dados consolidados do Dashboard.
     */
    public DashboardDados carregar(String filtroCategoria) {
        Conta[] contas = appSession.getUsuarioAtual().getContas();

        Objetivo objetivoPrincipal = viewModel.objetivoPrincipal(ordemObjetivos);
        int diasRestantes = objetivoPrincipal != null
                ? appSession.getCalculadoraPrazoObjetivo().diasRestantes(objetivoPrincipal)
                : 0;

        return new DashboardDados(
                viewModel.saldoTotal(contas),
                viewModel.percentualLimiteConsumido(contas),
                viewModel.gastosPorCategoria(contas, filtroCategoria),
                viewModel.crescimentoPatrimonio(contas, filtroCategoria),
                viewModel.transacoesUltimos7Dias(contas, LocalDate.now()),
                objetivoPrincipal,
                diasRestantes);
    }
}
