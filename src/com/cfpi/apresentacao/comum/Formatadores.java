package com.cfpi.apresentacao.comum;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Funções puras de formatação de valores para exibição, no padrão pt-BR.
 */
public final class Formatadores {

    private Formatadores() {}

    private static final DateTimeFormatter FORMATO_DATA_EXIBICAO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final DecimalFormat FORMATO_MOEDA;
    static {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator(',');
        simbolos.setGroupingSeparator('.');
        FORMATO_MOEDA = new DecimalFormat("#,##0.00", simbolos);
    }

    /**
     * Formata um valor monetário no padrão pt-BR (ex: {@code "R$ 1.234,56"}).
     *
     * @param valor valor a ser formatado.
     * @return o valor formatado como moeda em pt-BR.
     */
    public static String formatarMoeda(double valor) {
        String prefixo = valor < 0 ? "-R$ " : "R$ ";
        return prefixo + FORMATO_MOEDA.format(Math.abs(valor));
    }

    /**
     * Formata uma data no formato ISO {@code yyyy-MM-dd} para o formato de
     * exibição {@code dd/MM/yyyy}.
     *
     * @param dataIso data no formato ISO {@code yyyy-MM-dd}.
     * @return a data formatada como {@code dd/MM/yyyy}, ou {@code dataIso}
     *         sem alterações se não for uma data ISO válida.
     */
    public static String formatarData(String dataIso) {
        if (dataIso == null) {
            return null;
        }
        try {
            return LocalDate.parse(dataIso).format(FORMATO_DATA_EXIBICAO);
        } catch (DateTimeParseException e) {
            return dataIso;
        }
    }

    /**
     * Formata uma fração no intervalo {@code [0, 1]} como percentual (ex:
     * {@code 0.953} vira {@code "95%"}).
     *
     * @param fracao fração a ser formatada como percentual.
     * @return o percentual formatado, arredondado para o inteiro mais próximo.
     */
    public static String formatarPercentual(double fracao) {
        return Math.round(fracao * 100) + "%";
    }
}
