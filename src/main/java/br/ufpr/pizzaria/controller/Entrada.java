package br.ufpr.pizzaria.controller;

final class Entrada {

    private Entrada() {
    }

    static double lerNumero(String texto, String nomeCampo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Preencha o campo " + nomeCampo + ".");
        }
        String limpo = texto.trim();
        String mensagem = "O campo " + nomeCampo + " deve ser um número (ex.: 12,5). Valor digitado: \"" + limpo + "\".";
        double numero;
        try {
            numero = Double.parseDouble(limpo.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(mensagem);
        }
        if (Double.isNaN(numero) || Double.isInfinite(numero)) {
            throw new IllegalArgumentException(mensagem);
        }
        return numero;
    }
}
