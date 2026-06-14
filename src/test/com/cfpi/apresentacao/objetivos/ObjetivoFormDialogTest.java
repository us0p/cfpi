package com.cfpi.apresentacao.objetivos;

import com.cfpi.dominio.entidades.objetivo.Objetivo;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObjetivoFormDialogTest {

    @Test
    void camposIniciamVaziosEmModoCriacao() {
        ObjetivoFormDialog dialog = new ObjetivoFormDialog(null);

        assertTrue(dialog.getCampoNome().getText().isEmpty());
        assertTrue(dialog.getCampoValor().getText().isEmpty());
    }

    @Test
    void preencherParaEdicaoPopulaCamposComDadosDoObjetivo() {
        Usuario usuario = new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
        Objetivo objetivo = new Objetivo("Viagem", 5000.0, usuario);
        ObjetivoFormDialog dialog = new ObjetivoFormDialog(null);

        dialog.preencherParaEdicao(objetivo);

        assertEquals("Viagem", dialog.getCampoNome().getText());
        assertEquals("5000.0", dialog.getCampoValor().getText());
    }
}
