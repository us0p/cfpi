package com.cfpi.dominio.entidades.conta;

import com.cfpi.dominio.entidades.banco.Banco;

public class Conta {

    private static int contadorId = 1;

    private int id;
    private String tipo;
    private double valorConta;
    private String numeroConta;
    private String nomeDono;
    private String moeda;
    private Banco banco;
    private double limiteCredito;

    public Conta() {
        this.id = contadorId++;
    }

    public Conta(String tipo, double valorConta, String numeroConta, String nomeDono, String moeda, Banco banco, double limiteCredito) {
        this.id = contadorId++;
        this.tipo = tipo;
        this.valorConta = valorConta;
        this.numeroConta = numeroConta;
        this.nomeDono = nomeDono;
        this.moeda = moeda;
        this.banco = banco;
        this.limiteCredito = limiteCredito;
    }

    public Conta(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id; }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValorConta() {
        return valorConta;
    }
    public void setValorConta(double valorConta) {
        this.valorConta = valorConta;
    }

    public String getNumeroConta() {
        return numeroConta;
    }
    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getNomeDono() {
        return nomeDono;
    }
    public void setNomeDono(String nomeDono) {
        this.nomeDono = nomeDono;
    }

    public String getMoeda() {
        return moeda;
    }
    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public Banco getBanco() {
        return banco;
    }
    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public double getLimiteCredito() {
        return limiteCredito;
    }
    public void setLimiteCredito(double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }
}