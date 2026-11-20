package br.ufpr.pizzaria.model;

/**
 * Tipo da pizza (tabela tipo_pizza): Simples, Especial ou Premium,
 * cada um com o seu preço por centímetro quadrado.
 */
public class TipoPizza {

    private long id;
    private String nome;
    private double precoCm2;

    public TipoPizza(long id, String nome, double precoCm2) {
        this.id = id;
        this.nome = nome;
        setPrecoCm2(precoCm2);
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPrecoCm2() {
        return precoCm2;
    }

    public void setPrecoCm2(double precoCm2) {
        if (precoCm2 <= 0) {
            throw new IllegalArgumentException("O preço do cm² da pizza " + nome + " deve ser maior que zero.");
        }
        this.precoCm2 = precoCm2;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TipoPizza && ((TipoPizza) obj).id == id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
