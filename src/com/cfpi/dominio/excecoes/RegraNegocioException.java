package com.cfpi.dominio.excecoes;

/**
 * Exceção lançada quando uma operação viola uma regra de negócio cuja
 * validade depende do estado atual do sistema (de outras entidades já
 * cadastradas), e não apenas do valor isolado de um campo.
 *
 * <p>Exemplos de uso: duplicidade de banco por nome ou código, duplicidade
 * de objetivo por nome dentro do mesmo usuário, tentativa de remover um
 * banco que ainda possui contas associadas, saldo/limite insuficiente para
 * uma transação, e operações de investimento inválidas (ex: vender
 * quantidade maior que a disponível).</p>
 *
 * <p>É uma {@link RuntimeException} (unchecked) pelos mesmos motivos
 * descritos em {@link ValidacaoException}.</p>
 */
public class RegraNegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a exceção com uma mensagem descritiva da regra de negócio
     * violada.
     *
     * @param message descrição da regra violada (ex: "já existe um banco
     *                 cadastrado com este código").
     */
    public RegraNegocioException(String message) {
        super(message);
    }

    /**
     * Cria a exceção encapsulando uma causa original.
     *
     * @param message descrição da regra violada.
     * @param cause   exceção original que motivou esta falha.
     */
    public RegraNegocioException(String message, Throwable cause) {
        super(message, cause);
    }
}
