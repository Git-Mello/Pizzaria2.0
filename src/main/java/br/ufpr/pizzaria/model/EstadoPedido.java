package br.ufpr.pizzaria.model;

public enum EstadoPedido {

    ABERTO("Aberto"),
    A_CAMINHO("A caminho"),
    ENTREGUE("Entregue");

    private final String descricao;

    EstadoPedido(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
