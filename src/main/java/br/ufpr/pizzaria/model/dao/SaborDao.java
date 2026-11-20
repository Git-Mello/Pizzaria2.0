package br.ufpr.pizzaria.model.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import br.ufpr.pizzaria.model.Sabor;

/** Operações de banco de dados dos sabores. */
public interface SaborDao {

    void add(Sabor sabor) throws SQLException, IOException;

    List<Sabor> getAll() throws SQLException, IOException;

    Sabor getById(long id) throws SQLException, IOException;

    void update(Sabor sabor) throws SQLException, IOException;

    void delete(Sabor sabor) throws SQLException, IOException;

    /** Indica se o sabor está em alguma pizza já pedida (nesse caso não pode ser excluído). */
    boolean isEmUso(Sabor sabor) throws SQLException, IOException;
}
