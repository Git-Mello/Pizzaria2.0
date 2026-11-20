package br.ufpr.pizzaria.model.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import br.ufpr.pizzaria.model.Cliente;
import br.ufpr.pizzaria.model.Pedido;

/** Operações de banco de dados dos pedidos (com as suas pizzas e os sabores de cada pizza). */
public interface PedidoDao {

    /** Grava o pedido com todas as pizzas (numa única transação). */
    void add(Pedido pedido) throws SQLException, IOException;

    /** Todos os pedidos de todos os clientes (tela de controle de pedidos). */
    List<Pedido> getAll() throws SQLException, IOException;

    Pedido getById(long id) throws SQLException, IOException;

    /** Pedidos de um cliente, do mais recente para o mais antigo. */
    List<Pedido> getByCliente(Cliente cliente) throws SQLException, IOException;

    /** Regrava as pizzas, o estado e o preço total do pedido (numa única transação). */
    void update(Pedido pedido) throws SQLException, IOException;

    /** Altera só o estado do pedido (enunciado, item 5). */
    void updateEstado(Pedido pedido) throws SQLException, IOException;

    void delete(Pedido pedido) throws SQLException, IOException;
}
