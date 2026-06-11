package com.cfpi.dominio.arraydinamico;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArrayDinamicoTest {

    private ArrayDinamico<ItemTeste> array;

    @BeforeEach
    void setUp() {
        array = new ArrayDinamico<>(ItemTeste.class, 2);
    }

    @Test
    void inserirAdicionaItemAoFinal() {
        ItemTeste a = new ItemTeste(1, "a");
        ItemTeste b = new ItemTeste(2, "b");

        array.inserir(a);
        array.inserir(b);

        assertArrayEquals(new ItemTeste[]{a, b}, array.getArr());
    }

    @Test
    void inserirExpandeCapacidadeQuandoNecessario() {
        ItemTeste a = new ItemTeste(1, "a");
        ItemTeste b = new ItemTeste(2, "b");
        ItemTeste c = new ItemTeste(3, "c");

        array.inserir(a);
        array.inserir(b);
        array.inserir(c);

        assertArrayEquals(new ItemTeste[]{a, b, c}, array.getArr());
    }

    @Test
    void atualizarSubstituiItemEmIndiceValido() {
        ItemTeste a = new ItemTeste(1, "a");
        ItemTeste z = new ItemTeste(2, "z");

        array.inserir(a);

        assertTrue(array.atualizar(0, z));
        assertArrayEquals(new ItemTeste[]{z}, array.getArr());
    }

    @Test
    void atualizarRetornaFalsoParaIndiceInvalido() {
        ItemTeste a = new ItemTeste(1, "a");
        ItemTeste z = new ItemTeste(2, "z");

        array.inserir(a);

        assertFalse(array.atualizar(1, z));
        assertFalse(array.atualizar(-1, z));
    }

    @Test
    void removerRetornaFalsoParaIndiceInvalido() {
        array.inserir(new ItemTeste(1, "a"));

        assertFalse(array.remover(1));
        assertFalse(array.remover(-1));
    }

    @Test
    void removerDeslocaElementosEReduzTamanho() {
        ItemTeste a = new ItemTeste(1, "a");
        ItemTeste b = new ItemTeste(2, "b");
        ItemTeste c = new ItemTeste(3, "c");

        array.inserir(a);
        array.inserir(b);
        array.inserir(c);

        assertTrue(array.remover(0));
        assertArrayEquals(new ItemTeste[]{b, c}, array.getArr());
    }

    @Test
    void getArrNaoRetornaEspacosNaoUtilizadosAposRemocao() {
        ItemTeste a = new ItemTeste(1, "a");
        ItemTeste b = new ItemTeste(2, "b");
        ItemTeste c = new ItemTeste(3, "c");

        array.inserir(a);
        array.inserir(b);
        array.inserir(c);

        array.remover(2);

        assertArrayEquals(new ItemTeste[]{a, b}, array.getArr());
    }

    @Test
    void pesquisarRetornaItemComMesmoIdQuandoEncontrado() {
        ItemTeste a = new ItemTeste(1, "a");
        ItemTeste b = new ItemTeste(2, "b");

        array.inserir(a);
        array.inserir(b);

        assertSame(b, array.pesquisar(new ItemTeste(2, "outro nome")));
    }

    @Test
    void pesquisarRetornaNuloQuandoNaoEncontrado() {
        array.inserir(new ItemTeste(1, "a"));

        assertNull(array.pesquisar(new ItemTeste(99, "inexistente")));
    }

    @Test
    void pesquisarNaoEncontraItemRemovido() {
        ItemTeste a = new ItemTeste(1, "a");
        ItemTeste b = new ItemTeste(2, "b");

        array.inserir(a);
        array.inserir(b);
        array.remover(0);

        assertNull(array.pesquisar(new ItemTeste(1, "a")));
        assertSame(b, array.pesquisar(new ItemTeste(2, "b")));
    }
}
