package com.cfpi.dominio.entidades.objetivo;

public class Objetivo {

    private static int contadorId = 1;

    private int id;
    private String nome;
    private double valor;

    public Objetivo() {
        this.id = contadorId++;
    }

    public Objetivo(String nome, double valor) {
        this.id = contadorId++;
        this.nome = nome;
        this.valor = valor;
    }

    public Objetivo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }
}
