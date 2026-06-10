package com.cfpi.dominio.entidades.transacao;

import com.cfpi.dominio.entidades.conta.Conta;

public class Debito extends Transacao {

    private String tipo;

    public Debito() {
        super();
    }

    public Debito(String descricao, Conta conta, String data, double valor, String categoria, String tipo) {
        super(descricao, conta, data, valor, categoria);
        this.tipo = tipo;
    }

    public Debito(int id) {
        super(id);
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
