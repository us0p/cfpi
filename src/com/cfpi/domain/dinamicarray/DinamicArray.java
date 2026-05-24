package com.cfpi.domain.dinamicarray;

public class DinamicArray<T> {
    private T[] arr;
    private int lenght;
    private int capacity;


    public DinamicArray(int capacity) {
        this.arr = new T[capacity];
        this.lenght = 0;
        this.capacity = capacity;
    }

    private void expandir() {
        this.capacity *= 1.5;
        T[] arr2 = new T[capacity];
        for (int i = 0; i < lenght; i++) {
            arr2[i] = arr[i];
        }
        arr = arr2;
    }

    public boolean inserir(T item) {
        if (lenght >= capacity) {
            this.expandir();
        }
        this.arr[this.lenght] = item;
        this.lenght++;
        return true;

    }

    public boolean atualizar(int idx, T novoItem) {
        if (idx >= lenght || idx < 0) {
            return false;
        }
        arr[idx] = novoItem;

    }

    public boolean remover(int idx) {
        if (idx >= lenght || idx < 0) {
            return false;
        }
        for (int i = idx; i < lenght - 1; i++) {
            arr[i] = arr[i + 1];

        }
    }
}
