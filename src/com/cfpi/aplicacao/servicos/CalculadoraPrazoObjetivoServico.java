package com.cfpi.aplicacao.servicos;

import com.cfpi.apresentacao.comum.CalculadoraPrazoObjetivo;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.objetivo.Objetivo;
import com.cfpi.dominio.entidades.transacao.Credito;
import com.cfpi.dominio.entidades.transacao.Debito;
import com.cfpi.dominio.entidades.transacao.Transacao;
import com.cfpi.dominio.entidades.usuario.Usuario;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementação de produção de {@link CalculadoraPrazoObjetivo}.
 *
 * <p>Estima o número de dias restantes para {@code objetivo} a partir do
 * fluxo de caixa projetado do usuário:</p>
 *
 * <pre>
 * faltante     = objetivo.valor - patrimonioAtual
 * fluxoMensal  = rendaProjetadaMensal - gastosMediosMensais
 * diasRestantes = ceil(faltante / fluxoMensal * 30)
 * </pre>
 *
 * <p>{@code patrimonioAtual} é a soma de {@link Conta#getValorConta()} de
 * todas as contas do usuário. {@code rendaProjetadaMensal} é a média mensal
 * da soma de {@link Credito} com categoria {@code "rendimento"} (única
 * categoria do domínio que representa retorno de investimentos).
 * {@code gastosMediosMensais} é a média mensal da soma de todos os
 * {@link Debito}. "Média mensal" é o total dividido pelo número de meses
 * distintos ({@code yyyy-MM}) em que houve transação do tipo considerado.</p>
 */
public class CalculadoraPrazoObjetivoServico implements CalculadoraPrazoObjetivo {

    private static final int DIAS_POR_MES = 30;

    @Override
    public int diasRestantes(Objetivo objetivo) {
        Conta[] contas = objetivo.getUsuario().getContas();

        double faltante = objetivo.getValor() - patrimonioAtual(contas);
        if (faltante <= 0) {
            return 0;
        }

        double rendaProjetadaMensal = mediaMensal(contas, true);
        double gastosMediosMensais = mediaMensal(contas, false);
        double fluxoMensal = rendaProjetadaMensal - gastosMediosMensais;

        if (fluxoMensal <= 0) {
            return PRAZO_INDETERMINADO;
        }

        long dias = (long) Math.ceil(faltante / fluxoMensal * DIAS_POR_MES);
        return (int) Math.min(dias, PRAZO_INDETERMINADO);
    }

    private double patrimonioAtual(Conta[] contas) {
        double total = 0.0;
        for (Conta conta : contas) {
            total += conta.getValorConta();
        }
        return total;
    }

    private double mediaMensal(Conta[] contas, boolean rendimentos) {
        double total = 0.0;
        Set<String> meses = new HashSet<>();
        for (Conta conta : contas) {
            for (Transacao transacao : conta.getTransacoes()) {
                if (rendimentos) {
                    if (!(transacao instanceof Credito)) continue;
                    if (!"rendimento".equalsIgnoreCase(transacao.getCategoria())) continue;
                } else if (!(transacao instanceof Debito)) {
                    continue;
                }
                total += transacao.getValor();
                meses.add(transacao.getData().substring(0, 7));
            }
        }
        return meses.isEmpty() ? 0.0 : total / meses.size();
    }
}
