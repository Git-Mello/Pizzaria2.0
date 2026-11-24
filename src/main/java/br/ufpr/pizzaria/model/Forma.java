package br.ufpr.pizzaria.model;

public abstract class Forma {

    public static final double AREA_MINIMA = 100;
    public static final double AREA_MAXIMA = 1600;

    private double dimensao;

    protected Forma(double dimensao) {
        setDimensao(dimensao);
    }

    public double getDimensao() {
        return dimensao;
    }

    public final void setDimensao(double dimensao) {
        if (Double.isNaN(dimensao) || dimensao < getDimensaoMinima() || dimensao > getDimensaoMaxima()) {
            throw new IllegalArgumentException(String.format(
                    "O %s do %s deve ter entre %.0f e %.0f cm.",
                    getNomeDimensao(), getNome().toLowerCase(), getDimensaoMinima(), getDimensaoMaxima()));
        }
        this.dimensao = dimensao;
    }

    public abstract double calcularArea();

    public String getNome() {
        return getTipo().toString();
    }

    public abstract String getNomeDimensao();

    public abstract double getDimensaoMinima();

    public abstract double getDimensaoMaxima();

    public abstract TipoForma getTipo();
}
