package br.ufpr.pizzaria.model;

public class TipoPizza {

    public static final double PRECO_MINIMO = 0.0001;
    public static final double PRECO_MAXIMO = 10;

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
        double arredondado = Math.round(precoCm2 * 10000) / 10000.0;
        if (Double.isNaN(precoCm2) || arredondado < PRECO_MINIMO || arredondado > PRECO_MAXIMO) {
            throw new IllegalArgumentException(String.format(
                    "O preço do cm² da pizza %s deve ficar entre %.4f e %.4f (até 4 casas decimais).",
                    nome, PRECO_MINIMO, PRECO_MAXIMO));
        }
        this.precoCm2 = arredondado;
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
