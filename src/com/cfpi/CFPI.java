package com.cfpi;

import com.cfpi.dominio.arraydinamico.ArrayDinamico;

public class CFPI {
    public static void main(String[] args) {
	ArrayDinamico<Integer> ad = new ArrayDinamico<>(Integer.class, 3);

	ad.inserir(1);
	ad.inserir(2);
	ad.inserir(3);

	for (int i : ad.getArr()){
	    System.out.println(i);
	}
    }
}
