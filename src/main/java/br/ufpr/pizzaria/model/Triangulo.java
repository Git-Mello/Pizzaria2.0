package br.ufpr.pizzaria.model;

/** Pizza triangular (triângulo equilátero): a dimensão é o lado (20 a 60 cm). */
public class Triangulo extends Forma {

    public static final double MINIMO = 20;
    public static final double MAXIMO = 60;

    public Triangulo(double lado) {
        super(lado);
    }

    /** Área do triângulo equilátero: (raiz(3) / 4) x lado². */
    @Override
    public double calcularArea() {
        return (Math.sqrt(3) / 4) * getDimensao() * getDimensao();
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
        return TipoForma.TRIANGULO;
    }
}
