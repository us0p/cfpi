package com.cfpi.apresentacao.comum;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Pré-validações client-side, espelhando as regras de negócio documentadas
 * (mas ainda não implementadas) nas entidades de domínio. Permitem barrar
 * entradas inválidas na UI antes de chamar os construtores/setters do
 * domínio.
 */
public final class ValidadoresFormulario {

    private ValidadoresFormulario() {}

    /**
     * Valida um nome de pessoa, espelhando a regra documentada em
     * {@code Usuario(String, String, String, String)}.
     *
     * @param nome nome a ser validado.
     * @return {@code true} se, após {@code trim()}, {@code nome} tiver ao
     *         menos 3 caracteres e contiver apenas letras (Unicode) e
     *         espaços.
     */
    public static boolean nomeValido(String nome) {
        if (nome == null) {
            return false;
        }
        String alvo = nome.trim();
        if (alvo.length() < 3) {
            return false;
        }
        for (int i = 0; i < alvo.length(); i++) {
            char c = alvo.charAt(i);
            if (!Character.isLetter(c) && c != ' ') {
                return false;
            }
        }
        return true;
    }

    /**
     * Valida um CPF, espelhando a regra documentada em
     * {@code Usuario(String, String, String, String)}.
     *
     * @param cpf CPF a ser validado.
     * @return {@code true} se {@code cpf} casar com {@code ^\d{11}$}.
     */
    public static boolean cpfValido(String cpf) {
        return cpf != null && cpf.matches("^\\d{11}$");
    }

    /**
     * Valida um telefone, espelhando a regra documentada em
     * {@code Usuario(String, String, String, String)}.
     *
     * @param telefone telefone a ser validado.
     * @return {@code true} se {@code telefone} casar com {@code ^\d{11}$}.
     */
    public static boolean telefoneValido(String telefone) {
        return telefone != null && telefone.matches("^\\d{11}$");
    }

    /**
     * Valida se uma data está no formato ISO {@code yyyy-MM-dd}.
     *
     * @param data data a ser validada.
     * @return {@code true} se {@code data} puder ser interpretada por
     *         {@link LocalDate#parse(CharSequence)}.
     */
    public static boolean dataIsoValida(String data) {
        if (data == null) {
            return false;
        }
        try {
            LocalDate.parse(data);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Valida uma data de nascimento, espelhando a regra documentada em
     * {@code Usuario(String, String, String, String)}.
     *
     * @param data data a ser validada, formato {@code yyyy-MM-dd}.
     * @return {@code true} se {@code data} for uma data ISO válida e
     *         estritamente anterior a {@link LocalDate#now()}.
     */
    public static boolean dataPassadaValida(String data) {
        if (!dataIsoValida(data)) {
            return false;
        }
        return LocalDate.parse(data).isBefore(LocalDate.now());
    }

    /**
     * Valida um número de conta, espelhando a regra documentada em
     * {@code Conta(String, double, String, String, Banco, Usuario, double)}.
     *
     * @param numeroConta número de conta a ser validado.
     * @return {@code true} se {@code numeroConta} casar com {@code ^\d{6,}$}.
     */
    public static boolean numeroContaValido(String numeroConta) {
        return numeroConta != null && numeroConta.matches("^\\d{6,}$");
    }

    /**
     * Valida se um valor é estritamente positivo.
     *
     * @param valor valor a ser validado.
     * @return {@code true} se {@code valor > 0}.
     */
    public static boolean valorPositivo(double valor) {
        return valor > 0;
    }

    /**
     * Valida se um valor textual representa um número estritamente positivo.
     *
     * @param texto valor a ser validado, como texto.
     * @return {@code true} se {@code texto} representar um número
     *         {@code > 0}; {@code false} se for {@code null} ou não puder
     *         ser convertido para {@code double}.
     */
    public static boolean valorPositivo(String texto) {
        try {
            return Double.parseDouble(texto) > 0;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    /**
     * Valida se um valor textual representa um número não negativo.
     *
     * @param texto valor a ser validado, como texto.
     * @return {@code true} se {@code texto} representar um número
     *         {@code >= 0}; {@code false} se for {@code null} ou não puder
     *         ser convertido para {@code double}.
     */
    public static boolean valorNaoNegativo(String texto) {
        try {
            return Double.parseDouble(texto) >= 0;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    /**
     * Valida se um valor textual pode ser convertido para {@code double}.
     *
     * @param texto valor a ser validado, como texto.
     * @return {@code true} se {@code texto} representar um número válido.
     */
    public static boolean numeroValido(String texto) {
        try {
            Double.parseDouble(texto);
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    /**
     * Valida o tipo de uma operação de investimento, espelhando a regra
     * documentada em {@code Investimento(String, double, Conta, double,
     * double, String, double, String)}.
     *
     * @param operacao tipo da operação.
     * @return {@code true} se, após {@code trim()} e conversão para
     *         minúsculas, {@code operacao} for {@code "compra"} ou
     *         {@code "venda"}.
     */
    public static boolean operacaoValida(String operacao) {
        String normalizada = operacao == null ? "" : operacao.trim().toLowerCase();
        return normalizada.equals("compra") || normalizada.equals("venda");
    }
}
