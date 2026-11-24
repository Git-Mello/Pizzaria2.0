package br.ufpr.pizzaria.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

final class TratadorErros {

    private TratadorErros() {
    }

    static String mensagem(Exception e, String regraViolada) {
        if (e instanceof IllegalArgumentException) {
            return e.getMessage();
        }
        if (e instanceof SQLIntegrityConstraintViolationException) {
            return regraViolada != null ? regraViolada : "A operação viola uma regra do banco de dados: " + e.getMessage();
        }
        if (e instanceof SQLException) {
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
