package com.cfpi.dominio.excecoes;

/**
 * Exceção lançada quando um valor de campo fornecido para uma entidade de
 * domínio é inválido isoladamente, isto é, a invalidade pode ser
 * determinada sem depender do estado de outras entidades do sistema.
 *
 * <p>Exemplos de uso: nome com tamanho/formato inválido, código numérico
 * fora da faixa esperada, CPF/telefone com quantidade de dígitos incorreta,
 * data em formato inválido ou fora do intervalo permitido (ex: data futura),
 * e referências obrigatórias nulas (ex: criar um {@code Objetivo} sem
 * informar o {@code Usuario}).</p>
 *
 * <p>É uma {@link RuntimeException} (unchecked): não é obrigatório declarar
 * {@code throws} nem capturá-la, refletindo que representa um erro de
 * programação/entrada de dados que deve ser corrigido pelo chamador.</p>
 */
public class ValidacaoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a exceção com uma mensagem descritiva do campo/valor inválido.
     *
     * @param message descrição do motivo da invalidade, destinada a quem
     *                 chama o método (ex: "nome deve ter mais de 3 letras").
     */
    public ValidacaoException(String message) {
        super(message);
    }

    /**
     * Cria a exceção encapsulando uma causa original (ex: falha de parsing
     * de data via {@link java.time.LocalDate#parse(CharSequence)}).
     *
     * @param message descrição do motivo da invalidade.
     * @param cause   exceção original que motivou esta validação a falhar.
     */
    public ValidacaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
