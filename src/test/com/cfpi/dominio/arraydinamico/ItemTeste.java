package com.cfpi.dominio.arraydinamico;

import java.util.Objects;

import com.cfpi.dominio.Identificavel;

class ItemTeste implements Identificavel {

    private final int id;
    private final String nome;

    ItemTeste(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    @Override
    public int getId() {
        return id;
    }

    String getNome() {
        return nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ItemTeste)) {
            return false;
        }
        ItemTeste outro = (ItemTeste) obj;
        return id == outro.id && Objects.equals(nome, outro.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    @Override
    public String toString() {
        return "ItemTeste{id=" + id + ", nome='" + nome + "'}";
    }
}
