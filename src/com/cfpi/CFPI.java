package com.cfpi;

import com.cfpi.dominio.arraydinamico.ArrayDinamico;
import com.cfpi.dominio.entidades.objetivo.Objetivo;
import com.cfpi.dominio.entidades.usuario.Usuario;

public class CFPI {
    public static void main(String[] args) {
	Usuario usuario = new Usuario("Fulano", "11111111111", "11999999999", "1990-01-01");

	ArrayDinamico<Objetivo> ad = new ArrayDinamico<>(Objetivo.class, 3);

	ad.inserir(new Objetivo("Viagem", 5000.0, usuario));
	ad.inserir(new Objetivo("Carro", 30000.0, usuario));
	ad.inserir(new Objetivo("Reserva de emergência", 10000.0, usuario));

	for (Objetivo o : ad.getArr()){
	    System.out.println(o.getNome() + ": " + o.getValor());
	}
    }
}
