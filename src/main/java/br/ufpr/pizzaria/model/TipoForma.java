package br.ufpr.pizzaria.model;

public enum TipoForma {

    CIRCULO("Círculo"),
    QUADRADO("Quadrado"),
    TRIANGULO("Triângulo");

    private final String nome;

    TipoForma(String nome) {
        this.nome = nome;
    }

    public Forma criar(double dimensao) {
        switch (this) {
            case CIRCULO:
                return new Circulo(dimensao);
            case QUADRADO:
                return new Quadrado(dimensao);
            default:
                return new Triangulo(dimensao);
        }
    }

    public double calcularDimensao(double area) {
        switch (this) {
            case CIRCULO:
                return Math.sqrt(area / Math.PI);
            case QUADRADO:
                return Math.sqrt(area);
            default:
                return Math.sqrt(4 * area / Math.sqrt(3));
        }
    }

    public double getDimensaoMinima() {
        switch (this) {
            case CIRCULO:
                return Circulo.MINIMO;
            case QUADRADO:
                return Quadrado.MINIMO;
            default:
                return Triangulo.MINIMO;
        }
    }

    public String getDescricaoLimites() {
        Forma menor = criar(getDimensaoMinima());
        return String.format("%s de %.0f a %.0f cm", menor.getNomeDimensao(),
                menor.getDimensaoMinima(), menor.getDimensaoMaxima());
    }

    public Forma criarPorArea(double area) {
        if (Double.isNaN(area) || area < Forma.AREA_MINIMA || area > Forma.AREA_MAXIMA) {
            throw new IllegalArgumentException(String.format(
                    "A área deve ficar entre %.0f e %.0f cm².", Forma.AREA_MINIMA, Forma.AREA_MAXIMA));
        }
        double dimensao = calcularDimensao(area);
        Forma menor = criar(getDimensaoMinima());
        if (dimensao < menor.getDimensaoMinima() || dimensao > menor.getDimensaoMaxima()) {
            Forma maior = criar(menor.getDimensaoMaxima());
            double areaMinima = Math.max(Forma.AREA_MINIMA, menor.calcularArea());
            double areaMaxima = Math.min(Forma.AREA_MAXIMA, maior.calcularArea());
            throw new IllegalArgumentException(String.format(
                    "Para o %s, a área deve ficar entre %.2f e %.2f cm² (%s de %.0f a %.0f cm).",
                    menor.getNome().toLowerCase(), Math.ceil(areaMinima * 100) / 100,
                    Math.floor(areaMaxima * 100) / 100, menor.getNomeDimensao(),
                    menor.getDimensaoMinima(), menor.getDimensaoMaxima()));
        }
        return criar(dimensao);
    }

    @Override
    public String toString() {
        return nome;
    }
}
