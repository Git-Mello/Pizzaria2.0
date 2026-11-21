package br.ufpr.pizzaria.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Tratamento qualificado das exceções (enunciado, item 7):
 * cada tipo de erro vira uma mensagem que o usuário entende.
 */
final class TratadorErros {

    private TratadorErros() {
    }

    /**
     * @param e          a exceção capturada
     * @param duplicado  mensagem para o caso de registro repetido no banco (ex.: telefone já cadastrado)
     */
    static String mensagem(Exception e, String duplicado) {
        if (e instanceof IllegalArgumentException) {
            // Validação de dados (lançada pelo Model ou pela conversão de números)
            return e.getMessage();
        }
        if (e instanceof SQLIntegrityConstraintViolationException) {
            // Violação de chave única ou estrangeira no MySQL
            return duplicado != null ? duplicado : "A operação viola uma regra do banco de dados: " + e.getMessage();
        }
        if (e instanceof SQLException) {
            // Inclui a falha de conexão, cuja mensagem já vem explicada pela ConnectionFactory
            return "Erro no banco de dados:\n" + e.getMessage();
        }
        if (e instanceof IOException) {
            return "Erro ao ler o arquivo de configuração do banco (db.properties): " + e.getMessage();
        }
        return "Erro inesperado: " + e;
    }

    static String mensagem(Exception e) {
        return mensagem(e, null);
    }
}
