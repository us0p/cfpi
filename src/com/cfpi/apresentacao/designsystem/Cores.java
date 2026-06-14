package com.cfpi.apresentacao.designsystem;

import java.awt.Color;

/**
 * Paleta de cores compartilhada pela camada de apresentação, derivada da
 * paleta oficial da marca (taupe-grey, smoky-rose, fern, desert-sand,
 * seashell). Tons neutros são obtidos por opacidade das próprias cores da
 * paleta, sem introduzir novas cores.
 */
public final class Cores {

    private Cores() {}

    // ---- Paleta oficial ----------------------------------------------------
    public static final Color TAUPE_GREY = new Color(0x58, 0x4B, 0x53);
    public static final Color SMOKY_ROSE = new Color(0x9D, 0x5C, 0x63);
    public static final Color FERN = new Color(0x62, 0x8B, 0x48);
    public static final Color DESERT_SAND = new Color(0xE4, 0xBB, 0x97);
    public static final Color SEASHELL = new Color(0xFE, 0xF5, 0xEF);
    public static final Color BRANCO = new Color(0xFF, 0xFF, 0xFF);

    // ---- Tons derivados (opacidade da paleta oficial) ----------------------
    public static final Color TAUPE_GREY_08 = new Color(0x58, 0x4B, 0x53, 20);
    public static final Color TAUPE_GREY_15 = new Color(0x58, 0x4B, 0x53, 38);
    public static final Color TAUPE_GREY_40 = new Color(0x58, 0x4B, 0x53, 102);
    public static final Color SMOKY_ROSE_12 = new Color(0x9D, 0x5C, 0x63, 31);
    public static final Color FERN_12 = new Color(0x62, 0x8B, 0x48, 31);
    public static final Color DESERT_SAND_40 = new Color(0xE4, 0xBB, 0x97, 102);

    // ---- Papéis semânticos ---------------------------------------------------
    public static final Color FUNDO_PRINCIPAL = SEASHELL;
    public static final Color SIDEBAR_FUNDO = TAUPE_GREY;
    public static final Color CARD_BRANCO = BRANCO;
    public static final Color CARD_DESTAQUE = DESERT_SAND;

    public static final Color TEXTO_PRIMARIO = TAUPE_GREY;
    public static final Color TEXTO_SECUNDARIO = TAUPE_GREY_40;
    public static final Color BORDA = TAUPE_GREY_15;

    public static final Color PRIMARIO = TAUPE_GREY;
    public static final Color PRIMARIO_TEXTO = BRANCO;

    public static final Color CREDITO = FERN;
    public static final Color CREDITO_FUNDO = FERN_12;
    public static final Color DEBITO = SMOKY_ROSE;
    public static final Color DEBITO_FUNDO = SMOKY_ROSE_12;
}
