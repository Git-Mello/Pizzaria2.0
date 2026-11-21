package br.ufpr.pizzaria.model;

/**
 * Os três tipos de forma. Também serve de "fábrica": cria a subclasse certa de Forma
 * a partir da dimensão ou da área. O nome da constante é o que fica gravado no banco.
 */
public enum TipoForma {

    CIRCULO("Círculo") {
        @Override
        public Forma criar(double raio) {
            return new Circulo(raio);
        }

        @Override
        public double getDimensaoMinima() {
            return Circulo.MINIMO;
        }

        /** Raio a partir da área: raiz(área / PI). */
        @Override
        public double calcularDimensao(double area) {
            return Math.sqrt(area / Math.PI);
        }
    },
    QUADRADO("Quadrado") {
        @Override
        public Forma criar(double lado) {
            return new Quadrado(lado);
        }

        @Override
        public double getDimensaoMinima() {
            return Quadrado.MINIMO;
        }

        /** Lado a partir da área: raiz(área). */
        @Override
        public double calcularDimensao(double area) {
            return Math.sqrt(area);
        }
    },
    TRIANGULO("Triângulo") {
        @Override
        public Forma criar(double lado) {
            return new Triangulo(lado);
        }

        @Override
        public double getDimensaoMinima() {
            return Triangulo.MINIMO;
        }

        /** Lado do triângulo equilátero a partir da área: raiz(4 x área / raiz(3)). */
        @Override
        public double calcularDimensao(double area) {
            return Math.sqrt(4 * area / Math.sqrt(3));
        }
    };

    private final String nome;

    TipoForma(String nome) {
        this.nome = nome;
    }

    /** Menor dimensão aceita pela forma (vem da constante da subclasse). */
    public abstract double getDimensaoMinima();

    /** Texto com os limites, para mostrar na tela. Ex.: "raio de 7 a 23 cm". */
    public String getDescricaoLimites() {
        Forma exemplo = criar(getDimensaoMinima());
        return String.format("%s de %.0f a %.0f cm", exemplo.getNomeDimensao(),
                exemplo.getDimensaoMinima(), exemplo.getDimensaoMaxima());
    }

    /** Cria a forma com a dimensão (raio ou lado) informada. Os limites são validados pela própria forma. */
    public abstract Forma criar(double dimensao);

    /** Dimensão (raio ou lado) que gera a área informada. */
    public abstract double calcularDimensao(double area);

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
