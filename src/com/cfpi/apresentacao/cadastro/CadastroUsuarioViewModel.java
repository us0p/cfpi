package com.cfpi.apresentacao.cadastro;

import com.cfpi.apresentacao.comum.ValidadoresFormulario;
import java.util.ArrayList;
import java.util.List;

/**
 * Dados do formulário de cadastro de usuário e sua validação client-side.
 */
public class CadastroUsuarioViewModel {

    private String nome;
    private String cpf;
    private String telefone;
    private String dataNascimento;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    /**
     * Cria um {@link CadastroUsuarioViewModel} a partir dos valores dos
     * campos do formulário de cadastro.
     *
     * @param nome           nome completo informado.
     * @param cpf            CPF informado.
     * @param telefone       telefone informado.
     * @param dataNascimento data de nascimento informada.
     * @return o {@link CadastroUsuarioViewModel} correspondente.
     */
    public static CadastroUsuarioViewModel criar(String nome, String cpf, String telefone, String dataNascimento) {
        CadastroUsuarioViewModel viewModel = new CadastroUsuarioViewModel();
        viewModel.setNome(nome);
        viewModel.setCpf(cpf);
        viewModel.setTelefone(telefone);
        viewModel.setDataNascimento(dataNascimento);
        return viewModel;
    }

    /**
     * Campo do formulário de cadastro ao qual uma mensagem de erro de
     * {@link #validar()} se refere.
     */
    public enum Campo {
        NOME, CPF, TELEFONE, DATA_NASCIMENTO, GERAL
    }

    /**
     * Identifica a qual {@link Campo} do formulário uma mensagem de erro
     * retornada por {@link #validar()} se refere, com base no prefixo da
     * mensagem.
     *
     * @param mensagemErro mensagem de erro retornada por {@link #validar()}.
     * @return o {@link Campo} correspondente, ou {@link Campo#GERAL} se a
     *         mensagem não corresponder a nenhum campo específico.
     */
    public static Campo campoDoErro(String mensagemErro) {
        if (mensagemErro.startsWith("Nome")) {
            return Campo.NOME;
        }
        if (mensagemErro.startsWith("CPF")) {
            return Campo.CPF;
        }
        if (mensagemErro.startsWith("Telefone")) {
            return Campo.TELEFONE;
        }
        if (mensagemErro.startsWith("Data de nascimento")) {
            return Campo.DATA_NASCIMENTO;
        }
        return Campo.GERAL;
    }

    /**
     * Valida os campos do formulário, espelhando as regras documentadas em
     * {@code Usuario(String, String, String, String)}.
     *
     * @return lista de mensagens de erro, vazia se todos os campos forem
     *         válidos.
     */
    public List<String> validar() {
        List<String> erros = new ArrayList<>();
        if (!ValidadoresFormulario.nomeValido(nome)) {
            erros.add("Nome deve ter ao menos 3 letras e conter apenas letras e espaços.");
        }
        if (!ValidadoresFormulario.cpfValido(cpf)) {
            erros.add("CPF deve conter exatamente 11 dígitos numéricos.");
        }
        if (!ValidadoresFormulario.telefoneValido(telefone)) {
            erros.add("Telefone deve conter exatamente 11 dígitos numéricos.");
        }
        if (!ValidadoresFormulario.dataPassadaValida(dataNascimento)) {
            erros.add("Data de nascimento deve estar no formato AAAA-MM-DD e ser anterior a hoje.");
        }
        return erros;
    }
}
