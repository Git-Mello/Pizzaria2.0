package br.ufpr.pizzaria.model;

/** Pizza quadrada: a dimensão é o lado (10 a 40 cm). */
public class Quadrado extends Forma {

    public Quadrado(double lado) {
        super(lado);
    }

    /** Área do quadrado: lado². */
    @Override
    public double calcularArea() {
        return getDimensao() * getDimensao();
    }

    @Override
    public String getNome() {
        return "Quadrado";
    }

    @Override
    public String getNomeDimensao() {
        return "lado";
    }

    @Override
    public double getDimensaoMinima() {
        return 10;
    }

    @Override
    public double getDimensaoMaxima() {
        return 40;
    }

    @Override
    public TipoForma getTipo() {
        return TipoForma.QUADRADO;
    }
}
