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
     * @param e             a exceção capturada
     * @param regraViolada  mensagem para quando o banco recusa a operação por uma regra (restrição):
     *                      chave única repetida (ex.: telefone já cadastrado) ou registro em uso
     *                      (ex.: sabor que está em pizzas). Pode ser null.
     */
    static String mensagem(Exception e, String regraViolada) {
        if (e instanceof IllegalArgumentException) {
            // Validação de dados (lançada pelo Model ou pela conversão de números)
            return e.getMessage();
        }
        if (e instanceof SQLIntegrityConstraintViolationException) {
            // Violação de chave única ou estrangeira no MySQL (subclasse de SQLException: testada antes dela)
            return regraViolada != null ? regraViolada : "A operação viola uma regra do banco de dados: " + e.getMessage();
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
