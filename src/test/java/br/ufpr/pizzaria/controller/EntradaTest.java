package br.ufpr.pizzaria.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/** Testes da conversão dos números digitados nas telas (aceita vírgula, recusa texto, NaN e infinito). */
public class EntradaTest {

    @Test
    public void aceitaVirgulaOuPonto() {
        assertEquals(12.5, Entrada.lerNumero("12,5", "dimensão"), 0.0001);
        assertEquals(12.5, Entrada.lerNumero(" 12.5 ", "dimensão"), 0.0001);
        assertEquals(20, Entrada.lerNumero("20", "dimensão"), 0.0001);
    }

    @Test
    public void recusaTextoVazioENaoNumeros() {
        String[] invalidos = {"", "   ", "abc", "12,5,3", "NaN", "Infinity", "-Infinity"};
        for (String texto : invalidos) {
            try {
                Entrada.lerNumero(texto, "dimensão");
                fail("Deveria recusar: \"" + texto + "\"");
            } catch (IllegalArgumentException e) {
                // esperado: a mensagem é mostrada ao usuário
            }
        }
    }
}
