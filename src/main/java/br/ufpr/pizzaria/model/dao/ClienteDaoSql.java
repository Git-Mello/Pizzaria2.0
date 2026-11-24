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

public class ClienteDaoSql implements ClienteDao {

    private static ClienteDaoSql dao;

    private final String insert = "INSERT INTO cliente (nome, sobrenome, telefone) VALUES (?, ?, ?)";
    private final String selectAll = "SELECT id, nome, sobrenome, telefone FROM cliente ORDER BY nome, sobrenome";
    private final String selectById = "SELECT id, nome, sobrenome, telefone FROM cliente WHERE id = ?";
    private final String selectByFiltro = "SELECT id, nome, sobrenome, telefone FROM cliente"
            + " WHERE sobrenome LIKE ? AND telefone LIKE ? ORDER BY nome, sobrenome";
    private final String update = "UPDATE cliente SET nome = ?, sobrenome = ?, telefone = ? WHERE id = ?";
    private final String delete = "DELETE FROM cliente WHERE id = ?";

    private ClienteDaoSql() {
    }

    public static ClienteDaoSql getInstance() {
        if (dao == null) {
            dao = new ClienteDaoSql();
        }
        return dao;
    }

    @Override
    public void add(Cliente cliente) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSobrenome());
            stmt.setString(3, cliente.getTelefone());
            stmt.executeUpdate();
            try (ResultSet chave = stmt.getGeneratedKeys()) {
                chave.next();
                cliente.setId(chave.getLong(1));
            }
        }
    }

    @Override
    public List<Cliente> getAll() throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(selectAll);
             ResultSet rs = stmt.executeQuery()) {
            return lerLista(rs);
        }
    }

    @Override
    public Cliente getById(long id) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(selectById)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? ler(rs) : null;
            }
        }
    }

    @Override
    public List<Cliente> getByFiltro(String sobrenome, String telefone) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(selectByFiltro)) {
            stmt.setString(1, "%" + (sobrenome == null ? "" : sobrenome.trim()) + "%");
            stmt.setString(2, "%" + (telefone == null ? "" : telefone.trim()) + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                return lerLista(rs);
            }
        }
    }

    @Override
    public void update(Cliente cliente) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(update)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSobrenome());
            stmt.setString(3, cliente.getTelefone());
            stmt.setLong(4, cliente.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Cliente cliente) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(delete)) {
            stmt.setLong(1, cliente.getId());
            stmt.executeUpdate();
        }
    }

    private List<Cliente> lerLista(ResultSet rs) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        while (rs.next()) {
            clientes.add(ler(rs));
        }
        return clientes;
    }

    private Cliente ler(ResultSet rs) throws SQLException {
        return new Cliente(rs.getLong("id"), rs.getString("nome"), rs.getString("sobrenome"), rs.getString("telefone"));
    }
}
