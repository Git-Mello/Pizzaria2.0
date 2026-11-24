package br.ufpr.pizzaria.model.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import br.ufpr.pizzaria.model.Cliente;
import br.ufpr.pizzaria.model.Pedido;

public interface PedidoDao {

    void add(Pedido pedido) throws SQLException, IOException;

    List<Pedido> getAll() throws SQLException, IOException;

    Pedido getById(long id) throws SQLException, IOException;

    List<Pedido> getByCliente(Cliente cliente) throws SQLException, IOException;

    void update(Pedido pedido) throws SQLException, IOException;

    void updateEstado(Pedido pedido) throws SQLException, IOException;

    void delete(Pedido pedido) throws SQLException, IOException;
}
