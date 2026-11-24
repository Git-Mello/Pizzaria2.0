package br.ufpr.pizzaria.model.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import br.ufpr.pizzaria.model.Cliente;

public interface ClienteDao {

    void add(Cliente cliente) throws SQLException, IOException;

    List<Cliente> getAll() throws SQLException, IOException;

    Cliente getById(long id) throws SQLException, IOException;

    List<Cliente> getByFiltro(String sobrenome, String telefone) throws SQLException, IOException;

    void update(Cliente cliente) throws SQLException, IOException;

    void delete(Cliente cliente) throws SQLException, IOException;
}
