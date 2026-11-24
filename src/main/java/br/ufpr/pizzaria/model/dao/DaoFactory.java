package br.ufpr.pizzaria.model.dao;

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
