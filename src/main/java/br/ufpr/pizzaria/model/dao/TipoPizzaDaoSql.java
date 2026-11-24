package br.ufpr.pizzaria.model.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ufpr.pizzaria.model.TipoPizza;

public class TipoPizzaDaoSql implements TipoPizzaDao {

    private static TipoPizzaDaoSql dao;

    private final String selectAll = "SELECT id, nome, preco_cm2 FROM tipo_pizza ORDER BY id";
    private final String selectById = "SELECT id, nome, preco_cm2 FROM tipo_pizza WHERE id = ?";
    private final String update = "UPDATE tipo_pizza SET preco_cm2 = ? WHERE id = ?";

    private TipoPizzaDaoSql() {
    }

    public static TipoPizzaDaoSql getInstance() {
        if (dao == null) {
            dao = new TipoPizzaDaoSql();
        }
        return dao;
    }

    @Override
    public List<TipoPizza> getAll() throws SQLException, IOException {
        List<TipoPizza> tipos = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(selectAll);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tipos.add(new TipoPizza(rs.getLong("id"), rs.getString("nome"), rs.getDouble("preco_cm2")));
            }
        }
        return tipos;
    }

    @Override
    public TipoPizza getById(long id) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(selectById)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? new TipoPizza(rs.getLong("id"), rs.getString("nome"), rs.getDouble("preco_cm2")) : null;
            }
        }
    }

    @Override
    public void update(TipoPizza tipo) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(update)) {
            stmt.setDouble(1, tipo.getPrecoCm2());
            stmt.setLong(2, tipo.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(List<TipoPizza> tipos) throws SQLException, IOException {
        try (Connection con = ConnectionFactory.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement stmt = con.prepareStatement(update)) {
                for (TipoPizza tipo : tipos) {
                    stmt.setDouble(1, tipo.getPrecoCm2());
                    stmt.setLong(2, tipo.getId());
                    stmt.executeUpdate();
                }
                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        }
    }
}
