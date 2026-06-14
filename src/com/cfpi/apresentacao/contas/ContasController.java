package com.cfpi.apresentacao.contas;

import com.cfpi.apresentacao.comum.ValidadoresFormulario;
import com.cfpi.dominio.entidades.banco.Banco;
import com.cfpi.dominio.entidades.banco.BancoStore;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.usuario.Usuario;
import com.cfpi.dominio.excecoes.RegraNegocioException;
import com.cfpi.dominio.excecoes.ValidacaoException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Orquestra o carregamento, criação, atualização e remoção de contas.
 */
public class ContasController {

    private final Usuario usuario;
    private final BancoStore bancoStore;
    private final ContasViewModel viewModel;

    public ContasController(Usuario usuario, BancoStore bancoStore, ContasViewModel viewModel) {
        this.usuario = usuario;
        this.bancoStore = bancoStore;
        this.viewModel = viewModel;
    }

    /**
     * @return as contas do usuário atual.
     */
    public List<Conta> carregar() {
        return new ArrayList<>(Arrays.asList(usuario.getContas()));
    }

    /**
     * @return os bancos disponíveis para associação a uma conta.
     */
    public Banco[] getBancos() {
        return bancoStore.getBancos();
    }

    /**
     * Valida e cria uma nova conta para o usuário atual.
     *
     * @param tipo               tipo da conta ({@code "poupança"} ou {@code "corrente"}).
     * @param valorContaTexto    saldo inicial da conta, como texto (deve representar um número não negativo).
     * @param numeroConta        número da conta (somente dígitos, mínimo 6).
     * @param moeda              moeda da conta.
     * @param banco              banco ao qual a conta está associada (pode ser {@code null}).
     * @param limiteCreditoTexto limite de crédito, como texto (deve representar um número não negativo).
     * @return lista de mensagens de erro; vazia se a conta foi criada com sucesso.
     */
    public List<String> criar(String tipo, String valorContaTexto, String numeroConta, String moeda, Banco banco, String limiteCreditoTexto) {
        List<String> erros = validar(tipo, valorContaTexto, numeroConta, limiteCreditoTexto);
        if (!erros.isEmpty()) {
            return erros;
        }
        try {
            new Conta(tipo, Double.parseDouble(valorContaTexto), numeroConta, moeda, banco, usuario, Double.parseDouble(limiteCreditoTexto));
            return List.of();
        } catch (ValidacaoException | RegraNegocioException e) {
            return List.of(e.getMessage());
        }
    }

    private List<String> validar(String tipo, String valorContaTexto, String numeroConta, String limiteCreditoTexto) {
        List<String> erros = new ArrayList<>();
        String tipoNormalizado = tipo == null ? "" : tipo.trim().toLowerCase();
        if (!tipoNormalizado.equals("poupança") && !tipoNormalizado.equals("corrente")) {
            erros.add("Tipo deve ser \"poupança\" ou \"corrente\".");
        }
        if (!ValidadoresFormulario.numeroContaValido(numeroConta)) {
            erros.add("Número da conta deve conter apenas dígitos, com no mínimo 6 caracteres.");
        }
        if (!ValidadoresFormulario.valorNaoNegativo(valorContaTexto)) {
            erros.add("Saldo inicial não pode ser negativo.");
        }
        if (!ValidadoresFormulario.valorNaoNegativo(limiteCreditoTexto)) {
            erros.add("Limite de crédito não pode ser negativo.");
        }
        return erros;
    }

    /**
     * Valida e atualiza os dados de uma conta existente.
     *
     * @param conta              conta a ser atualizada.
     * @param tipo               novo tipo ({@code "poupança"} ou {@code "corrente"}).
     * @param valorContaTexto    novo saldo, como texto (deve representar um número não negativo).
     * @param numeroConta        novo número da conta (somente dígitos, mínimo 6).
     * @param moeda              nova moeda.
     * @param banco              novo banco associado.
     * @param limiteCreditoTexto novo limite de crédito, como texto (deve representar um número não negativo).
     * @return lista de mensagens de erro; vazia se a conta foi atualizada com sucesso.
     */
    public List<String> atualizar(Conta conta, String tipo, String valorContaTexto, String numeroConta, String moeda, Banco banco, String limiteCreditoTexto) {
        List<String> erros = validar(tipo, valorContaTexto, numeroConta, limiteCreditoTexto);
        if (!erros.isEmpty()) {
            return erros;
        }
        try {
            conta.setTipo(tipo);
            conta.setValorConta(Double.parseDouble(valorContaTexto));
            conta.setNumeroConta(numeroConta);
            conta.setMoeda(moeda);
            conta.setBanco(banco);
            conta.setLimiteCredito(Double.parseDouble(limiteCreditoTexto));
            return List.of();
        } catch (ValidacaoException | RegraNegocioException e) {
            return List.of(e.getMessage());
        }
    }

    /**
     * Remove a conta informada, mediante confirmação do usuário.
     *
     * @param conta       conta a ser removida.
     * @param confirmacao fornece {@code true} se o usuário confirmou a remoção.
     * @return {@code true} se a conta foi removida (via
     *         {@link Usuario#removerConta(int)}); {@code false} se a remoção
     *         foi cancelada ou se a conta não foi encontrada.
     */
    public boolean remover(Conta conta, BooleanSupplier confirmacao) {
        if (!confirmacao.getAsBoolean()) {
            return false;
        }
        return usuario.removerConta(conta.getId());
    }

    /**
     * Filtra as contas do usuário cujo {@code numeroConta} contenha o termo
     * informado.
     *
     * @param termo termo de busca; se {@code null} ou em branco, todas as
     *              contas são retornadas.
     * @return contas correspondentes ao termo de busca.
     */
    public List<Conta> filtrarPorNumero(String termo) {
        return viewModel.filtrarPorNumero(usuario.getContas(), termo);
    }
}
