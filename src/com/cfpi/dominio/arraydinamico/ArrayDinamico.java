package com.cfpi.dominio.arraydinamico;

import java.lang.reflect.Array;

import com.cfpi.dominio.Identificavel;

public class ArrayDinamico<T extends Identificavel> implements CRUD<T> {
    private int tamanho;
    private int capacidade;
    private Object[] arr;
    private Class<T> tipo;

    private ArrayDinamico() {}

    public ArrayDinamico(Class<T> tipo, int capacidade) {
        this.arr = new Object[capacidade];
        this.tamanho = 0;
        this.capacidade = capacidade;
	this.tipo = tipo;
    }

    private void expandir() {
        this.capacidade = (int)(this.capacidade * 1.5) + 1;
        Object[] arr2 = new Object[this.capacidade];
        for (int i = 0; i < this.tamanho; i++) {
            arr2[i] = arr[i];
        }
        arr = arr2;
    }

    @Override
    public boolean inserir(T item) {
        if (tamanho >= capacidade) {
            this.expandir();
        }
        this.arr[this.tamanho] = item;
        this.tamanho++;
        return true;

    }

    @Override
    public boolean atualizar(int idx, T novoItem) {
        if (idx >= tamanho || idx < 0) {
            return false;
        }
        arr[idx] = novoItem;
	return true;
    }

    @Override
    public boolean remover(int idx) {
        if (idx >= tamanho || idx < 0) {
            return false;
        }
        for (int i = idx; i < tamanho - 1; i++) {
            arr[i] = arr[i + 1];
        }
        tamanho--;
	return true;
    }

    @SuppressWarnings("unchecked")
    public T[] getArr() {
	T[] castedArray = (T[]) Array.newInstance(tipo, tamanho);
	System.arraycopy(arr, 0, castedArray, 0, tamanho);
	return castedArray;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T pesquisar(T entidade) {
        int id = entidade.getId();
        for (int i = 0; i < tamanho; i++) {
            T item = (T) arr[i];
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
