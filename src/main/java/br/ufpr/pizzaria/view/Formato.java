package br.ufpr.pizzaria.view;

public final class Formato {

    private Formato() {
    }

    public static String moeda(double valor) {
        return String.format("R$ %.2f", valor);
    }

    public static String numero(double valor) {
        return String.format("%.2f", valor);
    }

    public static String precoCm2(double valor) {
        return String.format("%.4f", valor);
    }
}
