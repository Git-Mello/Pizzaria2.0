package br.ufpr.pizzaria.model;

/**
 * Os três tipos de forma. Também serve de "fábrica": cria a subclasse certa de Forma
 * a partir da dimensão ou da área. O nome da constante é o que fica gravado no banco
 * (coluna pizza.forma).
 */
public enum TipoForma {

    CIRCULO("Círculo"),
    QUADRADO("Quadrado"),
    TRIANGULO("Triângulo");

    private final String nome;

    TipoForma(String nome) {
        this.nome = nome;
    }

    /** Cria a forma com a dimensão (raio ou lado) informada. Os limites são validados pela própria forma. */
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

    /** Dimensão (raio ou lado) que gera a área informada. */
    public double calcularDimensao(double area) {
        switch (this) {
            case CIRCULO:
                return Math.sqrt(area / Math.PI);           // raio = raiz(área / PI)
            case QUADRADO:
                return Math.sqrt(area);                     // lado = raiz(área)
            default:
                return Math.sqrt(4 * area / Math.sqrt(3));  // lado do triângulo equilátero = raiz(4 x área / raiz(3))
        }
    }

    /** Menor dimensão aceita pela forma (vem da constante da subclasse). */
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

    /** Texto com os limites, para mostrar na tela. Ex.: "raio de 7 a 23 cm". */
    public String getDescricaoLimites() {
        Forma menor = criar(getDimensaoMinima());   // a forma no menor tamanho sabe o nome e os limites da dimensão
        return String.format("%s de %.0f a %.0f cm", menor.getNomeDimensao(),
                menor.getDimensaoMinima(), menor.getDimensaoMaxima());
    }

    /**
     * Cria a forma a partir da área em cm² (enunciado, item 2.c.iv):
     * valida a área (100 a 1600 cm²), calcula a dimensão e cria a forma.
     * Como cada forma tem limites de raio/lado, nem toda área de 100 a 1600 serve para todas:
     * ex.: 100 cm² num círculo dá raio 5,64 (mínimo 7). Nesse caso a mensagem diz
     * qual é a faixa de área válida para aquela forma.
     */
    public Forma criarPorArea(double area) {
        if (Double.isNaN(area) || area < Forma.AREA_MINIMA || area > Forma.AREA_MAXIMA) {
            throw new IllegalArgumentException(String.format(
                    "A área deve ficar entre %.0f e %.0f cm².", Forma.AREA_MINIMA, Forma.AREA_MAXIMA));
        }
        double dimensao = calcularDimensao(area);
        Forma menor = criar(getDimensaoMinima());   // a forma no menor tamanho permitido
        if (dimensao < menor.getDimensaoMinima() || dimensao > menor.getDimensaoMaxima()) {
            // Faixa de área desta forma: área da menor e da maior forma (polimorfismo: calcularArea),
            // limitada a 100..1600. Arredonda "para dentro" (2 casas) para a mensagem não prometer
            // um valor que seria recusado.
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
