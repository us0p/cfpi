package com.cfpi.dominio.entidades.investimento;

import com.cfpi.dominio.entidades.conta.Conta;

public abstract class Investimento {

    private static int contadorId = 1;

    private int id;
    private String nomeAtivo;
    private double valor;
    private Conta conta;
    private double quantidade;
    private double valorTotalAtivo;
    private double imposto;
    private String data;
    private double valorRealizado;
    private String operacao;

    public Investimento() {
        this.id = contadorId++;
    }

    public Investimento(String nomeAtivo, double valor, Conta conta, double quantidade, double valorTotalAtivo, double imposto, String data, double valorRealizado, String operacao) {
        this.id = contadorId++;
        this.nomeAtivo = nomeAtivo;
        this.valor = valor;
        this.conta = conta;
        this.quantidade = quantidade;
        this.valorTotalAtivo = valorTotalAtivo;
        this.imposto = imposto;
        this.data = data;
        this.valorRealizado = valorRealizado;
        this.operacao = operacao;
    }

    public Investimento(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNomeAtivo() {
        return nomeAtivo;
    }
    public void setNomeAtivo(String nomeAtivo) {
        this.nomeAtivo = nomeAtivo;
    }

    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }

    public Conta getConta() {
        return conta;
    }
    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public double getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorTotalAtivo() {
        return valorTotalAtivo;
    }
    public void setValorTotalAtivo(double valorTotalAtivo) {
        this.valorTotalAtivo = valorTotalAtivo;
    }

    public double getImposto() {
        return imposto;
    }
    public void setImposto(double imposto) {
        this.imposto = imposto;
    }

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public double getValorRealizado() {
        return valorRealizado;
    }
    public void setValorRealizado(double valorRealizado) {
        this.valorRealizado = valorRealizado;
    }

    public String getOperacao() {
        return operacao;
    }
    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }
}