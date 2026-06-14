package com.cfpi.apresentacao.dashboard;

import com.cfpi.apresentacao.comum.AvaliadorDeAtivosFake;
import com.cfpi.apresentacao.comum.CalculadoraPrazoObjetivo;
import com.cfpi.apresentacao.comum.CalculadoraPrazoObjetivoFake;
import com.cfpi.apresentacao.shell.AppSession;
import com.cfpi.dominio.entidades.conta.Conta;
import com.cfpi.dominio.entidades.objetivo.Objetivo;
import com.cfpi.dominio.entidades.usuario.Usuario;
import org.junit.jupiter.api.Test;

import javax.swing.JComboBox;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DashboardViewTest {

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario("Ana Maria", "11111111111", "11999999999", "1990-01-01");
        new Conta("corrente", 1000.0, "111111", "BRL", null, usuario, 0.0);
        return usuario;
    }

    private DashboardView criarView(Usuario usuario, Objetivo objetivo, int diasRestantes) {
        AppSession appSession = new AppSession(new CalculadoraPrazoObjetivoFake(diasRestantes), new AvaliadorDeAtivosFake(0.0));
        appSession.setUsuarioAtual(usuario);
        List<Objetivo> ordemObjetivos = objetivo != null ? List.of(objetivo) : List.of();
        DashboardController controller = new DashboardController(appSession, ordemObjetivos, new DashboardViewModel());
        return new DashboardView(controller);
    }

    @Test
    void labelObjetivoDiasExibeIndeterminadoQuandoPrazoNaoPodeSerDeterminado() {
        Usuario usuario = criarUsuario();
        Objetivo objetivo = new Objetivo("Viagem", 5000.0, usuario);

        DashboardView view = criarView(usuario, objetivo, CalculadoraPrazoObjetivo.PRAZO_INDETERMINADO);

        assertEquals("Prazo indeterminado", view.getLabelObjetivoDias().getText());
    }

    @Test
    void labelObjetivoDiasExibeDiasRestantesQuandoPrazoDeterminado() {
        Usuario usuario = criarUsuario();
        Objetivo objetivo = new Objetivo("Viagem", 5000.0, usuario);

        DashboardView view = criarView(usuario, objetivo, 30);

        assertEquals("30 dias restantes", view.getLabelObjetivoDias().getText());
    }

    @Test
    void comboCategoriaLinhaContemApenasCategoriasDeCredito() {
        Usuario usuario = criarUsuario();

        DashboardView view = criarView(usuario, null, 0);

        JComboBox<String> combo = view.getComboCategoriaLinha();
        assertEquals(3, combo.getItemCount());
        assertEquals("Todas", combo.getItemAt(0));
        assertEquals("pagamento", combo.getItemAt(1));
        assertEquals("rendimento", combo.getItemAt(2));
    }
}
