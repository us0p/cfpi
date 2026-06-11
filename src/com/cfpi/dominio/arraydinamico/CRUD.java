package com.cfpi.dominio.arraydinamico;

import com.cfpi.dominio.Identificavel;

public interface CRUD<T extends Identificavel> {
    boolean inserir(T entidade);
    T pesquisar(T entidade);
    boolean remover(int id);
    boolean atualizar(int id, T novoValor);
}
