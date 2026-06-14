package com.cfpi.apresentacao.cadastro;

import com.cfpi.apresentacao.comum.AvaliadorDeAtivosFake;
import com.cfpi.apresentacao.comum.CalculadoraPrazoObjetivoFake;
import com.cfpi.apresentacao.shell.AppSession;
import com.cfpi.apresentacao.shell.Tela;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CadastroUsuarioControllerTest {

    private CadastroUsuarioViewModel viewModelValido() {
        CadastroUsuarioViewModel viewModel = new CadastroUsuarioViewModel();
        viewModel.setNome("Ana Maria");
        viewModel.setCpf("11111111111");
        viewModel.setTelefone("11999999999");
        viewModel.setDataNascimento("1990-01-01");
        return viewModel;
    }

    @Test
    void cadastrarComEntradaValidaCriaUsuarioESetaNaSessao() {
        AppSession appSession = new AppSession(new CalculadoraPrazoObjetivoFake(0), new AvaliadorDeAtivosFake(0.0));
        CadastroUsuarioController controller = new CadastroUsuarioController(appSession, tela -> { });

        List<String> erros = controller.cadastrar(viewModelValido());

        assertTrue(erros.isEmpty());
        assertNotNull(appSession.getUsuarioAtual());
        assertEquals("Ana Maria", appSession.getUsuarioAtual().getNome());
    }

    @Test
    void cadastrarComEntradaValidaNavegaParaDashboard() {
        AppSession appSession = new AppSession(new CalculadoraPrazoObjetivoFake(0), new AvaliadorDeAtivosFake(0.0));
        List<Tela> telasNavegadas = new java.util.ArrayList<>();
        CadastroUsuarioController controller = new CadastroUsuarioController(appSession, telasNavegadas::add);

        controller.cadastrar(viewModelValido());

        assertEquals(List.of(Tela.DASHBOARD), telasNavegadas);
    }

    @Test
    void cadastrarComNomeInvalidoRetornaErroENaoAlteraSessao() {
        AppSession appSession = new AppSession(new CalculadoraPrazoObjetivoFake(0), new AvaliadorDeAtivosFake(0.0));
        CadastroUsuarioController controller = new CadastroUsuarioController(appSession, tela -> { });
        CadastroUsuarioViewModel viewModel = viewModelValido();
        viewModel.setNome("An");

        List<String> erros = controller.cadastrar(viewModel);

        assertFalse(erros.isEmpty());
        assertNull(appSession.getUsuarioAtual());
    }

    @Test
    void cadastrarComEntradaInvalidaNaoNavega() {
        AppSession appSession = new AppSession(new CalculadoraPrazoObjetivoFake(0), new AvaliadorDeAtivosFake(0.0));
        List<Tela> telasNavegadas = new java.util.ArrayList<>();
        CadastroUsuarioController controller = new CadastroUsuarioController(appSession, telasNavegadas::add);
        CadastroUsuarioViewModel viewModel = viewModelValido();
        viewModel.setCpf("123");

        controller.cadastrar(viewModel);

        assertTrue(telasNavegadas.isEmpty());
    }
}
