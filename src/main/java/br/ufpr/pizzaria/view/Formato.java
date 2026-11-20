package br.ufpr.pizzaria.view;

import java.text.NumberFormat;
import java.util.Locale;

/** Formatação de valores para exibição nas telas (padrão brasileiro). */
public final class Formato {

    private static final Locale BRASIL = Locale.forLanguageTag("pt-BR");

    private Formato() {
    }

    /** Ex.: 1234.5 -> "R$ 1.234,50". */
    public static String moeda(double valor) {
        return NumberFormat.getCurrencyInstance(BRASIL).format(valor).replace(' ', ' ');
    }

    /** Ex.: 11.2838 -> "11,28". */
    public static String numero(double valor) {
        return String.format(BRASIL, "%.2f", valor);
    }

    /** Preço do cm², com 4 casas. Ex.: 0.05 -> "0,0500". */
    public static String precoCm2(double valor) {
        return String.format(BRASIL, "%.4f", valor);
    }
}
