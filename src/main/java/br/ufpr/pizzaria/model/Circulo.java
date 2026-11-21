package br.ufpr.pizzaria.model;

/** Pizza circular: a dimensão é o raio (7 a 23 cm). */
public class Circulo extends Forma {

    public static final double MINIMO = 7;
    public static final double MAXIMO = 23;

    public Circulo(double raio) {
        super(raio);
    }

    /** Área do círculo: PI x raio². */
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
