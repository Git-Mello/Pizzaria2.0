package br.ufpr.pizzaria.controller;

/** Conversão e validação dos números digitados nas telas. */
final class Entrada {

    private Entrada() {
    }

    /**
     * Converte o texto digitado em número. Aceita vírgula ou ponto como separador decimal.
     * Lança IllegalArgumentException com uma mensagem para o usuário se o texto não for um número.
     */
    static double lerNumero(String texto, String nomeCampo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Preencha o campo " + nomeCampo + ".");
        }
        String limpo = texto.trim();
        if (!limpo.matches("\\d+([.,]\\d+)?")) {
            throw new IllegalArgumentException("O campo " + nomeCampo + " deve ser um número (ex.: 12,5). Valor digitado: \"" + limpo + "\".");
        }
        return Double.parseDouble(limpo.replace(',', '.'));
    }
}
