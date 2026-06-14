package com.cfpi.apresentacao.shell;

/**
 * Implementada por painéis que precisam recarregar seus dados sempre que a
 * tela é exibida via {@link MainFrame#mostrarTela(Tela)}.
 */
public interface TelaAtualizavel {
    void atualizar();
}
