package com.cfpi.dominio.entidades.transacao;

import com.cfpi.dominio.entidades.conta.Conta;

public class Credito extends Transacao {

    public Credito() {
        super();
    }

    public Credito(String descricao, Conta conta, String data, double valor, String categoria) {
        super(descricao, conta, data, valor, categoria);
    }

    public Credito(int id) {
        super(id);
    }
}
