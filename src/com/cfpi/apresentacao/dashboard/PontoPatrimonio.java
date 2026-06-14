package com.cfpi.apresentacao.dashboard;

import java.util.Objects;

/**
 * Ponto da série de crescimento de patrimônio: saldo acumulado em uma data.
 */
public class PontoPatrimonio {

    private final String data;
    private final double saldoAcumulado;

    public PontoPatrimonio(String data, double saldoAcumulado) {
        this.data = data;
        this.saldoAcumulado = saldoAcumulado;
    }

    public String getData() {
        return data;
    }

    public double getSaldoAcumulado() {
        return saldoAcumulado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PontoPatrimonio)) {
            return false;
        }
        PontoPatrimonio outro = (PontoPatrimonio) o;
        return Double.compare(outro.saldoAcumulado, saldoAcumulado) == 0 && Objects.equals(data, outro.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, saldoAcumulado);
    }

    @Override
    public String toString() {
        return "PontoPatrimonio{data='" + data + "', saldoAcumulado=" + saldoAcumulado + "}";
    }
}
