package br.ufpr.pizzaria.model.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import br.ufpr.pizzaria.model.TipoPizza;

public interface TipoPizzaDao {

    List<TipoPizza> getAll() throws SQLException, IOException;

    TipoPizza getById(long id) throws SQLException, IOException;

    void update(TipoPizza tipo) throws SQLException, IOException;

    void update(List<TipoPizza> tipos) throws SQLException, IOException;
}
