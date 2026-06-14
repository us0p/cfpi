package com.cfpi.apresentacao.cadastro;

import com.cfpi.apresentacao.shell.AppSession;
import com.cfpi.apresentacao.shell.Tela;
import com.cfpi.dominio.entidades.usuario.Usuario;
import com.cfpi.dominio.excecoes.ValidacaoException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Orquestra a criação do {@link Usuario} a partir do formulário de cadastro,
 * registrando-o na {@link AppSession} e navegando para o Dashboard em caso
 * de sucesso.
 */
public class CadastroUsuarioController {

    private final AppSession appSession;
    private final Consumer<Tela> navegador;

    public CadastroUsuarioController(AppSession appSession, Consumer<Tela> navegador) {
        this.appSession = appSession;
        this.navegador = navegador;
    }

    /**
     * Valida e cria o usuário a partir dos dados do {@code viewModel}.
     *
     * <p>Se a validação client-side falhar, retorna as mensagens de erro
     * sem chamar o domínio. Se a validação client-side passar mas o
     * construtor de {@link Usuario} lançar {@link ValidacaoException} (regra
     * ainda em implementação), retorna a mensagem dessa exceção.</p>
     *
     * @param viewModel dados do formulário de cadastro.
     * @return lista de mensagens de erro; vazia se o cadastro foi concluído
     *         com sucesso (usuário registrado na {@link AppSession} e
     *         navegação para {@link Tela#DASHBOARD} disparada).
     */
    public List<String> cadastrar(CadastroUsuarioViewModel viewModel) {
        List<String> erros = viewModel.validar();
        if (!erros.isEmpty()) {
            return erros;
        }
        try {
            Usuario usuario = new Usuario(viewModel.getNome(), viewModel.getCpf(), viewModel.getTelefone(), viewModel.getDataNascimento());
            appSession.setUsuarioAtual(usuario);
            navegador.accept(Tela.DASHBOARD);
            return List.of();
        } catch (ValidacaoException e) {
            return List.of(e.getMessage());
        }
    }
}
