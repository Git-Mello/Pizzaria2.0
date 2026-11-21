package br.ufpr.pizzaria.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Pedido de um cliente (tabela pedido): identificador, pizzas, estado e preço total.
 * Um cliente pode ter vários pedidos.
 */
public class Pedido {

    private long id;
    private Cliente cliente;
    private List<Pizza> pizzas = new ArrayList<>();
    private EstadoPedido estado = EstadoPedido.ABERTO;

    public Pedido(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Escolha o cliente do pedido.");
        }
        this.cliente = cliente;
    }

    public Pedido(long id, Cliente cliente, EstadoPedido estado) {
        this(cliente);
        this.id = id;
        setEstado(estado);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Devolve uma cópia da lista: para incluir ou remover pizzas é preciso usar
     * adicionarPizza/removerPizza (que validam).
     */
    public List<Pizza> getPizzas() {
        return new ArrayList<>(pizzas);
    }

    public void adicionarPizza(Pizza pizza) {
        if (pizza == null) {
            throw new IllegalArgumentException("Pizza inválida.");
        }
        pizzas.add(pizza);
    }

    public void removerPizza(int posicao) {
        if (posicao < 0 || posicao >= pizzas.size()) {
            throw new IllegalArgumentException("Selecione a pizza que deseja remover.");
        }
        pizzas.remove(posicao);
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        if (estado == null) {
            throw new IllegalArgumentException("Escolha o estado do pedido.");
        }
        this.estado = estado;
    }

    /**
     * O estado só avança: Aberto → A caminho → Entregue (pode pular etapa, mas não volta).
     * ordinal() é a posição da constante no enum EstadoPedido (ABERTO = 0, A_CAMINHO = 1, ENTREGUE = 2).
     */
    public boolean podeMudarPara(EstadoPedido novo) {
        return novo != null && novo.ordinal() > estado.ordinal();
    }

    /** Só pedidos abertos podem ter as pizzas alteradas. */
    public boolean podeSerAlterado() {
        return estado == EstadoPedido.ABERTO;
    }

    /** Preço total = soma dos preços das pizzas (enunciado, itens 2.d, 2.f e 2.g). */
    public double getPrecoTotal() {
        double total = 0;
        for (Pizza pizza : pizzas) {
            total += pizza.getPreco();
        }
        return total;
    }

    @Override
    public String toString() {
        return String.format("Pedido %d - %s - R$ %.2f", id, estado, getPrecoTotal());
    }
}
