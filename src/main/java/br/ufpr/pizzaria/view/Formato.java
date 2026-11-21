package br.ufpr.pizzaria.view;

/**
 * Formatação de valores para exibição nas telas.
 * String.format usa o idioma do computador: no Windows em português, a vírgula é o separador decimal.
 */
public final class Formato {

    private Formato() {
    }

    /** Ex.: 1234.5 -> "R$ 1234,50". */
    public static String moeda(double valor) {
        return String.format("R$ %.2f", valor);
    }

    /** Ex.: 11.2838 -> "11,28". */
    public static String numero(double valor) {
        return String.format("%.2f", valor);
    }

    /** Preço do cm², com 4 casas. Ex.: 0.05 -> "0,0500". */
    public static String precoCm2(double valor) {
        return String.format("%.4f", valor);
    }
}
