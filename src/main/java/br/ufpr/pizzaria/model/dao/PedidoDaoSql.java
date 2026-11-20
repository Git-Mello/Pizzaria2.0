package br.ufpr.pizzaria.model.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.ufpr.pizzaria.model.Cliente;
import br.ufpr.pizzaria.model.EstadoPedido;
import br.ufpr.pizzaria.model.Pedido;
import br.ufpr.pizzaria.model.Pizza;
import br.ufpr.pizzaria.model.Sabor;
import br.ufpr.pizzaria.model.TipoForma;

/**
 * Implementação MySQL do PedidoDao (padrão Singleton).
 * Um pedido ocupa três tabelas: pedido, pizza e pizza_sabor.
 */
public class PedidoDaoSql implements PedidoDao {

    private static PedidoDaoSql dao;

    private final String insert = "INSERT INTO pedido (id_cliente, estado, preco_total) VALUES (?, ?, ?)";
    private final String insertPizza = "INSERT INTO pizza (id_pedido, forma, dimensao, area, preco) VALUES (?, ?, ?, ?, ?)";
    private final String insertPizzaSabor = "INSERT INTO pizza_sabor (id_pizza, id_sabor) VALUES (?, ?)";
    // Pedido com os dados do cliente (INNER JOIN)
    private final String selectBase = "SELECT p.id, p.estado, c.id AS id_cliente, c.nome, c.sobrenome, c.telefone"
            + " FROM pedido p INNER JOIN cliente c ON c.id = p.id_cliente";
    private final String selectAll = selectBase + " ORDER BY p.id DESC";
    private final String selectById = selectBase + " WHERE p.id = ?";
    private final String selectByCliente = selectBase + " WHERE p.id_cliente = ? ORDER BY p.id DESC";
    private final String selectPizzas = "SELECT id, forma, dimensao, preco FROM pizza WHERE id_pedido = ? ORDER BY id";
    private final String selectSaboresDaPizza = "SELECT s.id, s.nome, t.id AS id_tipo, t.nome AS nome_tipo, t.preco_cm2"
            + " FROM pizza_sabor ps INNER JOIN sabor s ON s.id = ps.id_sabor"
            + " INNER JOIN tipo_pizza t ON t.id = s.id_tipo WHERE ps.id_pizza = ? ORDER BY s.nome";
    private final String update = "UPDATE pedido SET estado = ?, preco_total = ? WHERE id = ?";
    private final String updateEstado = "UPDATE pedido SET estado = ? WHERE id = ?";
    private final String deletePizzas = "DELETE FROM pizza WHERE id_pedido = ?";   // pizza_sabor sai em cascata
    private final String delete = "DELETE FROM pedido WHERE id = ?";

    private PedidoDaoSql() {
    }

    public static PedidoDaoSql getInstance() {
        if (dao == null) {
            dao = new PedidoDaoSql();
        }
        return dao;
    }

    @Override
    public void add(Pedido pedido) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection()) {
            con.setAutoCommit(false);   // tudo ou nada: pedido + pizzas + sabores
            try (PreparedStatement stmt = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, pedido.getCliente().getId());
                stmt.setString(2, pedido.getEstado().name());
                stmt.setDouble(3, pedido.getPrecoTotal());
                stmt.executeUpdate();
                try (ResultSet chave = stmt.getGeneratedKeys()) {
                    chave.next();
                    pedido.setId(chave.getLong(1));
                }
                gravarPizzas(con, pedido);
                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        }
    }

    @Override
    public void update(Pedido pedido) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement stmt = con.prepareStatement(update);
                 PreparedStatement stmtApaga = con.prepareStatement(deletePizzas)) {
                stmt.setString(1, pedido.getEstado().name());
                stmt.setDouble(2, pedido.getPrecoTotal());
                stmt.setLong(3, pedido.getId());
                stmt.executeUpdate();
                // As pizzas são regravadas: apaga as antigas e grava a lista atual
                stmtApaga.setLong(1, pedido.getId());
                stmtApaga.executeUpdate();
                gravarPizzas(con, pedido);
                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        }
    }

    /** Grava cada pizza do pedido e, para cada pizza, os seus sabores. */
    private void gravarPizzas(Connection con, Pedido pedido) throws SQLException {
        try (PreparedStatement stmtPizza = con.prepareStatement(insertPizza, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtSabor = con.prepareStatement(insertPizzaSabor)) {
            for (Pizza pizza : pedido.getPizzas()) {
                stmtPizza.setLong(1, pedido.getId());
                stmtPizza.setString(2, pizza.getForma().getTipo().name());
                stmtPizza.setDouble(3, pizza.getForma().getDimensao());
                stmtPizza.setDouble(4, pizza.getArea());
                stmtPizza.setDouble(5, pizza.getPreco());
                stmtPizza.executeUpdate();
                try (ResultSet chave = stmtPizza.getGeneratedKeys()) {
                    chave.next();
                    pizza.setId(chave.getLong(1));
                }
                for (Sabor sabor : pizza.getSabores()) {
                    stmtSabor.setLong(1, pizza.getId());
                    stmtSabor.setLong(2, sabor.getId());
                    stmtSabor.executeUpdate();
                }
            }
        }
    }

    @Override
    public List<Pedido> getAll() throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(selectAll)) {
            return lerPedidos(con, stmt);
        }
    }

    @Override
    public Pedido getById(long id) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(selectById)) {
            stmt.setLong(1, id);
            List<Pedido> pedidos = lerPedidos(con, stmt);
            return pedidos.isEmpty() ? null : pedidos.get(0);
        }
    }

    @Override
    public List<Pedido> getByCliente(Cliente cliente) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(selectByCliente)) {
            stmt.setLong(1, cliente.getId());
            return lerPedidos(con, stmt);
        }
    }

    @Override
    public void updateEstado(Pedido pedido) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(updateEstado)) {
            stmt.setString(1, pedido.getEstado().name());
            stmt.setLong(2, pedido.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Pedido pedido) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(delete)) {
            stmt.setLong(1, pedido.getId());
            stmt.executeUpdate();
        }
    }

    /** Executa o SELECT de pedidos e monta cada Pedido com o Cliente e as Pizzas. */
    private List<Pedido> lerPedidos(Connection con, PreparedStatement stmt) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente(rs.getLong("id_cliente"), rs.getString("nome"),
                        rs.getString("sobrenome"), rs.getString("telefone"));
                pedidos.add(new Pedido(rs.getLong("id"), cliente, EstadoPedido.valueOf(rs.getString("estado"))));
            }
        }
        for (Pedido pedido : pedidos) {
            carregarPizzas(con, pedido);
        }
        return pedidos;
    }

    /** Lê as pizzas do pedido. A coluna forma diz qual subclasse de Forma criar. */
    private void carregarPizzas(Connection con, Pedido pedido) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(selectPizzas)) {
            stmt.setLong(1, pedido.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    long idPizza = rs.getLong("id");
                    TipoForma tipoForma = TipoForma.valueOf(rs.getString("forma"));
                    pedido.adicionarPizza(new Pizza(idPizza, tipoForma.criar(rs.getDouble("dimensao")),
                            lerSabores(con, idPizza), rs.getDouble("preco")));
                }
            }
        }
    }

    private List<Sabor> lerSabores(Connection con, long idPizza) throws SQLException {
        List<Sabor> sabores = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(selectSaboresDaPizza)) {
            stmt.setLong(1, idPizza);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sabores.add(SaborDaoSql.ler(rs));
                }
            }
        }
        return sabores;
    }
}
