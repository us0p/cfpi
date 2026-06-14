package com.cfpi.dominio.entidades.investimento;

import com.cfpi.dominio.entidades.conta.Conta;

public class LCA extends Investimento {

    public static final double IMPOSTO_PADRAO = 0.0;

    public LCA() {
        super();
    }

    public LCA(String nomeAtivo, double valor, Conta conta, double quantidade, double imposto, String data, double valorRealizado, String operacao) {
        super(nomeAtivo, valor, conta, quantidade, imposto, data, valorRealizado, operacao);
    }

    public LCA(int id) {
        super(id);
    }

    @Override
    public double getImpostoPadrao() {
        return IMPOSTO_PADRAO;
    }
}
