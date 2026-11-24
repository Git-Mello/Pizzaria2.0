package br.ufpr.pizzaria.model.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.ufpr.pizzaria.model.Sabor;
import br.ufpr.pizzaria.model.TipoPizza;

public class SaborDaoSql implements SaborDao {

    private static SaborDaoSql dao;

    private final String insert = "INSERT INTO sabor (nome, id_tipo) VALUES (?, ?)";
    private final String selectAll = "SELECT s.id, s.nome, t.id AS id_tipo, t.nome AS nome_tipo, t.preco_cm2"
            + " FROM sabor s INNER JOIN tipo_pizza t ON t.id = s.id_tipo ORDER BY t.id, s.nome";
    private final String selectById = "SELECT s.id, s.nome, t.id AS id_tipo, t.nome AS nome_tipo, t.preco_cm2"
            + " FROM sabor s INNER JOIN tipo_pizza t ON t.id = s.id_tipo WHERE s.id = ?";
    private final String update = "UPDATE sabor SET nome = ?, id_tipo = ? WHERE id = ?";
    private final String delete = "DELETE FROM sabor WHERE id = ?";
    private final String countUso = "SELECT COUNT(*) FROM pizza_sabor WHERE id_sabor = ?";

    private SaborDaoSql() {
    }

    public static SaborDaoSql getInstance() {
        if (dao == null) {
            dao = new SaborDaoSql();
        }
        return dao;
    }

    @Override
    public void add(Sabor sabor) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, sabor.getNome());
            stmt.setLong(2, sabor.getTipo().getId());
            stmt.executeUpdate();
            try (ResultSet chave = stmt.getGeneratedKeys()) {
                chave.next();
                sabor.setId(chave.getLong(1));
            }
        }
    }

    @Override
    public List<Sabor> getAll() throws SQLException, IOException {
        List<Sabor> sabores = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(selectAll);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                sabores.add(ler(rs));
            }
        }
        return sabores;
    }

    @Override
    public Sabor getById(long id) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(selectById)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? ler(rs) : null;
            }
        }
    }

    @Override
    public void update(Sabor sabor) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(update)) {
            stmt.setString(1, sabor.getNome());
            stmt.setLong(2, sabor.getTipo().getId());
            stmt.setLong(3, sabor.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Sabor sabor) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(delete)) {
            stmt.setLong(1, sabor.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean isEmUso(Sabor sabor) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(countUso)) {
            stmt.setLong(1, sabor.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    static Sabor ler(ResultSet rs) throws SQLException {
        TipoPizza tipo = new TipoPizza(rs.getLong("id_tipo"), rs.getString("nome_tipo"), rs.getDouble("preco_cm2"));
        return new Sabor(rs.getLong("id"), rs.getString("nome"), tipo);
    }
}
