package com.cfpi.dominio.entidades.transacao;

import com.cfpi.dominio.entidades.conta.Conta;

public abstract class Transacao {

    private static int contadorId = 1;

    private int id;
    private String descricao;
    private Conta conta;
    private String data;
    private double valor;
    private String categoria;

    public Transacao() {
        this.id = contadorId++;
    }

    public Transacao(String descricao, Conta conta, String data, double valor, String categoria) {
        this.id = contadorId++;
        this.descricao = descricao;
        this.conta = conta;
        this.data = data;
        this.valor = valor;
        this.categoria = categoria;
    }

    public Transacao(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Conta getConta() {
        return conta;
    }
    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}