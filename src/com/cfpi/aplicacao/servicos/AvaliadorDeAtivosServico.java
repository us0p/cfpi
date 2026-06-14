package com.cfpi.aplicacao.servicos;

import com.cfpi.apresentacao.comum.AvaliadorDeAtivos;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.investimento.Investimento;
import com.cfpi.dominio.entidades.usuario.Usuario;

import java.util.function.Supplier;

/**
 * Implementação de produção de {@link AvaliadorDeAtivos}.
 *
 * <p>Avalia a posição de um ativo pelo seu <b>custo médio de compra</b> (não
 * por preço de mercado externo): {@code valorAtual = custoMedioCompra *
 * quantidadeAtual}, onde {@code custoMedioCompra} é a média ponderada (por
 * {@code quantidade}) de {@code valor} de todas as operações
 * {@code "compra"} do ativo (mesmo {@code nomeAtivo}, case-insensitive após
 * {@code trim()}, e mesmo subtipo concreto de {@link Investimento}) em
 * todas as contas do usuário — mesma lógica usada por
 * {@code Investimento.aplicarEfeito()} para operações {@code "venda"}.</p>
 *
 * <p>Lucro/prejuízo/imposto de operações {@code "venda"} já são calculados
 * pelas regras de negócio existentes em {@code Investimento.aplicarEfeito()}
 * e não são recalculados aqui.</p>
 *
 * <p>O usuário atual é resolvido em tempo de chamada via {@code usuarioAtual}
 * (tipicamente {@code appSession::getUsuarioAtual}), pois no momento da
 * composição da aplicação ainda não há usuário autenticado.</p>
 */
public class AvaliadorDeAtivosServico implements AvaliadorDeAtivos {

    private final Supplier<Usuario> usuarioAtual;

    public AvaliadorDeAtivosServico(Supplier<Usuario> usuarioAtual) {
        this.usuarioAtual = usuarioAtual;
    }

    @Override
    public double valorAtual(String nomeAtivo, Class<? extends Investimento> tipo, double quantidadeAtual) {
        Usuario usuario = usuarioAtual.get();
        if (usuario == null || quantidadeAtual <= 0) {
            return 0.0;
        }

        double somaValorQuantidade = 0.0;
        double somaQuantidade = 0.0;
        String alvo = nomeAtivo.trim().toLowerCase();

        for (Conta conta : usuario.getContas()) {
            for (Investimento investimento : conta.getInvestimentos()) {
                if (investimento.getClass() != tipo) continue;
                if (!investimento.getNomeAtivo().trim().toLowerCase().equals(alvo)) continue;
                if (!"compra".equalsIgnoreCase(investimento.getOperacao())) continue;

                somaValorQuantidade += investimento.getValor() * investimento.getQuantidade();
                somaQuantidade += investimento.getQuantidade();
            }
        }

        double custoMedioCompra = somaQuantidade > 0 ? somaValorQuantidade / somaQuantidade : 0.0;
        return custoMedioCompra * quantidadeAtual;
    }
}
