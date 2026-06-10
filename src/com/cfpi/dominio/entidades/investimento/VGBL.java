package com.cfpi.dominio.entidades.investimento;

import com.cfpi.dominio.entidades.conta.Conta;

public class VGBL extends Investimento {

    public static final double IMPOSTO_PADRAO = 0.15;

    public VGBL() {
        super();
    }

    public VGBL(String nomeAtivo, double valor, Conta conta, double quantidade, double valorTotalAtivo, double imposto, String data, double valorRealizado, String operacao) {
        super(nomeAtivo, valor, conta, quantidade, valorTotalAtivo, imposto, data, valorRealizado, operacao);
    }

    public VGBL(int id) {
        super(id);
    }
}
