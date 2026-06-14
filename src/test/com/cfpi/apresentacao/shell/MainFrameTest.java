package com.cfpi.apresentacao.shell;

import com.cfpi.apresentacao.comum.AvaliadorDeAtivosFake;
import com.cfpi.apresentacao.comum.CalculadoraPrazoObjetivoFake;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import javax.swing.JPanel;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MainFrameTest {

    private AppSession novaSessaoComUsuario() {
        AppSession sessao = new AppSession(new CalculadoraPrazoObjetivoFake(0), new AvaliadorDeAtivosFake(0.0));
        sessao.setUsuarioAtual(new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01"));
        return sessao;
    }

    @Test
    void mostrarTelaChamaAtualizarNoPainelQueImplementaTelaAtualizavel() {
        MainFrame mainFrame = new MainFrame(novaSessaoComUsuario());
        AtomicInteger chamadas = new AtomicInteger();

        class PainelAtualizavel extends JPanel implements TelaAtualizavel {
            @Override
            public void atualizar() {
                chamadas.incrementAndGet();
            }
        }

        mainFrame.registrarPainel(Tela.DASHBOARD, new PainelAtualizavel());

        mainFrame.mostrarTela(Tela.DASHBOARD);

        assertEquals(1, chamadas.get());
    }

    @Test
    void mostrarTelaNaoFalhaParaPainelQueNaoImplementaTelaAtualizavel() {
        MainFrame mainFrame = new MainFrame(novaSessaoComUsuario());

        assertDoesNotThrow(() -> mainFrame.mostrarTela(Tela.CONTAS));
    }
}
