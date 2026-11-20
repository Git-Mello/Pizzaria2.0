package br.ufpr.pizzaria.model.dao;

/**
 * Fábrica de DAOs (padrão Factory, como na aula de DAO).
 * Quem usa um DAO pede à fábrica e enxerga só a interface: a implementação
 * (aqui, a de MySQL) pode ser trocada sem mudar os Controllers.
 */
public class DaoFactory {

    private DaoFactory() {
    }

    public static ClienteDao getClienteDao() {
        return ClienteDaoSql.getInstance();
    }

    public static TipoPizzaDao getTipoPizzaDao() {
        return TipoPizzaDaoSql.getInstance();
    }

    public static SaborDao getSaborDao() {
        return SaborDaoSql.getInstance();
    }

    public static PedidoDao getPedidoDao() {
        return PedidoDaoSql.getInstance();
    }
}
