package br.ufpr.pizzaria.model.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import br.ufpr.pizzaria.model.Cliente;

/** Operações de banco de dados dos clientes. */
public interface ClienteDao {

    void add(Cliente cliente) throws SQLException, IOException;

    List<Cliente> getAll() throws SQLException, IOException;

    Cliente getById(long id) throws SQLException, IOException;

    /**
     * Clientes cujo sobrenome contém o texto informado e/ou cujo telefone contém os números
     * informados (enunciado, item 1.d). Campo vazio = não filtra por ele.
     */
    List<Cliente> getByFiltro(String sobrenome, String telefone) throws SQLException, IOException;

    void update(Cliente cliente) throws SQLException, IOException;

    /** Exclui o cliente e, em cascata, os pedidos dele (enunciado, item 1.c). */
    void delete(Cliente cliente) throws SQLException, IOException;
}
