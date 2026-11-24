package br.ufpr.pizzaria.model;

public class Circulo extends Forma {

    public static final double MINIMO = 7;
    public static final double MAXIMO = 23;

    public Circulo(double raio) {
        super(raio);
    }

    @Override
    public double calcularArea() {
        return Math.PI * getDimensao() * getDimensao();
    }

    @Override
    public String getNomeDimensao() {
        return "raio";
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
        return TipoForma.CIRCULO;
    }
}
