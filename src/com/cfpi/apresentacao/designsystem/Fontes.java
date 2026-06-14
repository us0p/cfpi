package com.cfpi.apresentacao.designsystem;

import java.awt.Font;

/**
 * Fontes compartilhadas pela camada de apresentação, seguindo a escala
 * tipográfica do design (Nunito para títulos/labels, Inter para valores
 * monetários). Caso essas famílias não estejam instaladas no ambiente, o AWT
 * recorre automaticamente a uma fonte padrão equivalente.
 */
public final class Fontes {

    private Fontes() {}

    private static final String FAMILIA_DISPLAY = "Nunito";
    private static final String FAMILIA_NUMERICA = "Inter";

    public static final Font TITULO = new Font(FAMILIA_DISPLAY, Font.BOLD, 32);
    public static final Font SUBTITULO = new Font(FAMILIA_DISPLAY, Font.BOLD, 24);
    public static final Font CORPO = new Font(FAMILIA_DISPLAY, Font.PLAIN, 16);
    public static final Font CORPO_NEGRITO = new Font(FAMILIA_DISPLAY, Font.BOLD, 16);
    public static final Font MEDIO = new Font(FAMILIA_DISPLAY, Font.PLAIN, 20);
    public static final Font MEDIO_NEGRITO = new Font(FAMILIA_DISPLAY, Font.BOLD, 20);
    public static final Font PEQUENO = new Font(FAMILIA_DISPLAY, Font.PLAIN, 14);
    public static final Font PEQUENO_NEGRITO = new Font(FAMILIA_DISPLAY, Font.BOLD, 14);
    public static final Font DESTAQUE_NUMERICO = new Font(FAMILIA_NUMERICA, Font.BOLD, 24);
}
