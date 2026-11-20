package br.ufpr.pizzaria.model.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import br.ufpr.pizzaria.model.TipoPizza;

/**
 * Operações de banco de dados dos tipos de pizza.
 * Os três tipos (Simples, Especial e Premium) são fixos: só o preço do cm² muda.
 */
public interface TipoPizzaDao {

    List<TipoPizza> getAll() throws SQLException, IOException;

    TipoPizza getById(long id) throws SQLException, IOException;

    /** Atualiza o preço do cm² (enunciado, item 3). */
    void update(TipoPizza tipo) throws SQLException, IOException;
}
