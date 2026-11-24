package br.ufpr.pizzaria.model;

public class Quadrado extends Forma {

    public static final double MINIMO = 10;
    public static final double MAXIMO = 40;

    public Quadrado(double lado) {
        super(lado);
    }

    @Override
    public double calcularArea() {
        return getDimensao() * getDimensao();
    }

    @Override
    public String getNomeDimensao() {
        return "lado";
    }

    @Override
    public double getDimensaoMinima() {
        return MINIMO;
    }

    @Override
    public double getDimensaoMaxima() {
        return MAXIMO;
    }

    @Override
    public TipoForma getTipo() {
        return TipoForma.QUADRADO;
    }
}
