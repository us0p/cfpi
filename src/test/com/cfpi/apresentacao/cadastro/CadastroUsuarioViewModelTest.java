package com.cfpi.apresentacao.cadastro;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CadastroUsuarioViewModelTest {

    private CadastroUsuarioViewModel viewModelValido() {
        CadastroUsuarioViewModel viewModel = new CadastroUsuarioViewModel();
        viewModel.setNome("Ana Maria");
        viewModel.setCpf("11111111111");
        viewModel.setTelefone("11999999999");
        viewModel.setDataNascimento("1990-01-01");
        return viewModel;
    }

    @Test
    void validarRetornaListaVaziaParaEntradaValida() {
        CadastroUsuarioViewModel viewModel = viewModelValido();

        assertTrue(viewModel.validar().isEmpty());
    }

    @Test
    void validarRetornaErroParaNomeCurto() {
        CadastroUsuarioViewModel viewModel = viewModelValido();
        viewModel.setNome("An");

        assertFalse(viewModel.validar().isEmpty());
    }

    @Test
    void validarRetornaErroParaNomeComDigitos() {
        CadastroUsuarioViewModel viewModel = viewModelValido();
        viewModel.setNome("Ana123");

        assertFalse(viewModel.validar().isEmpty());
    }

    @Test
    void validarRetornaErroParaCpfComMenosDe11Digitos() {
        CadastroUsuarioViewModel viewModel = viewModelValido();
        viewModel.setCpf("123");

        assertFalse(viewModel.validar().isEmpty());
    }

    @Test
    void validarRetornaErroParaCpfComCaracteresNaoNumericos() {
        CadastroUsuarioViewModel viewModel = viewModelValido();
        viewModel.setCpf("1111111111a");

        assertFalse(viewModel.validar().isEmpty());
    }

    @Test
    void validarRetornaErroParaTelefoneComMenosDe11Digitos() {
        CadastroUsuarioViewModel viewModel = viewModelValido();
        viewModel.setTelefone("119999999");

        assertFalse(viewModel.validar().isEmpty());
    }

    @Test
    void validarRetornaErroParaDataNaoIso() {
        CadastroUsuarioViewModel viewModel = viewModelValido();
        viewModel.setDataNascimento("01/01/1990");

        assertFalse(viewModel.validar().isEmpty());
    }

    @Test
    void validarRetornaErroParaDataFutura() {
        CadastroUsuarioViewModel viewModel = viewModelValido();
        viewModel.setDataNascimento("2099-01-01");

        assertFalse(viewModel.validar().isEmpty());
    }

    @Test
    void validarRetornaTodosOsErrosParaEntradaTotalmenteInvalida() {
        CadastroUsuarioViewModel viewModel = new CadastroUsuarioViewModel();
        viewModel.setNome("An");
        viewModel.setCpf("123");
        viewModel.setTelefone("456");
        viewModel.setDataNascimento("data-invalida");

        List<String> erros = viewModel.validar();

        assertTrue(erros.size() == 4);
    }

    @Test
    void criarPreencheTodosOsCamposDoViewModel() {
        CadastroUsuarioViewModel viewModel = CadastroUsuarioViewModel.criar("Ana Maria", "11111111111", "11999999999", "1990-01-01");

        assertEquals("Ana Maria", viewModel.getNome());
        assertEquals("11111111111", viewModel.getCpf());
        assertEquals("11999999999", viewModel.getTelefone());
        assertEquals("1990-01-01", viewModel.getDataNascimento());
    }

    @Test
    void campoDoErroIdentificaOCampoPeloPrefixoDaMensagem() {
        assertEquals(CadastroUsuarioViewModel.Campo.NOME, CadastroUsuarioViewModel.campoDoErro("Nome deve ter ao menos 3 letras e conter apenas letras e espaços."));
        assertEquals(CadastroUsuarioViewModel.Campo.CPF, CadastroUsuarioViewModel.campoDoErro("CPF deve conter exatamente 11 dígitos numéricos."));
        assertEquals(CadastroUsuarioViewModel.Campo.TELEFONE, CadastroUsuarioViewModel.campoDoErro("Telefone deve conter exatamente 11 dígitos numéricos."));
        assertEquals(CadastroUsuarioViewModel.Campo.DATA_NASCIMENTO, CadastroUsuarioViewModel.campoDoErro("Data de nascimento deve estar no formato AAAA-MM-DD e ser anterior a hoje."));
        assertEquals(CadastroUsuarioViewModel.Campo.GERAL, CadastroUsuarioViewModel.campoDoErro("O CPF deve conter exatamente 11 dígitos numéricos."));
    }
}
