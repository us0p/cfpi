package com.cfpi.apresentacao.contas;

import com.cfpi.dominio.entidades.conta.Conta;

import java.util.ArrayList;
import java.util.List;

/**
 * Transformações puras sobre {@link Conta}, sem dependências de Swing.
 */
public class ContasViewModel {

    /**
     * Filtra contas cujo {@code numeroConta} contenha o termo informado.
     *
     * @param contas contas a serem filtradas.
     * @param termo  termo de busca; se {@code null} ou em branco, todas as
     *               contas são retornadas.
     * @return cópia da lista de contas cujo {@code numeroConta} contenha
     *         {@code termo}, ou todas as contas se {@code termo} for nulo ou
     *         em branco.
     */
    public List<Conta> filtrarPorNumero(Conta[] contas, String termo) {
        List<Conta> resultado = new ArrayList<>();
        boolean semTermo = termo == null || termo.trim().isEmpty();
        for (Conta conta : contas) {
            if (semTermo || (conta.getNumeroConta() != null && conta.getNumeroConta().contains(termo.trim()))) {
                resultado.add(conta);
            }
        }
        return resultado;
    }

    /**
     * Indica se o campo "Limite de crédito" deve ser exibido no formulário
     * de conta, espelhando a regra de que apenas contas do tipo
     * {@code "corrente"} possuem limite de crédito.
     *
     * @param tipoConta tipo selecionado no formulário ({@code "corrente"}
     *                  ou {@code "poupança"}).
     * @return {@code true} se {@code tipoConta} for {@code "corrente"}.
     */
    public boolean exibeLimiteCredito(String tipoConta) {
        return "corrente".equals(tipoConta);
    }
}
