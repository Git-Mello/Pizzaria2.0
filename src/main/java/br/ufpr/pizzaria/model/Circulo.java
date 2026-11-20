package br.ufpr.pizzaria.model;

/** Pizza circular: a dimensão é o raio (7 a 23 cm). */
public class Circulo extends Forma {

    public Circulo(double raio) {
        super(raio);
    }

    /** Área do círculo: PI x raio². */
    @Override
    public double calcularArea() {
        return Math.PI * getDimensao() * getDimensao();
    }

    @Override
    public String getNome() {
        return "Círculo";
    }

    @Override
    public String getNomeDimensao() {
        return "raio";
    }

    @Override
    public double getDimensaoMinima() {
        return 7;
    }

    @Override
    public double getDimensaoMaxima() {
        return 23;
    }

    @Override
    public TipoForma getTipo() {
        return TipoForma.CIRCULO;
    }
}
