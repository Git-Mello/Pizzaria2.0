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

    /** Cria a forma com a dimensão (raio ou lado) informada. Os limites são validados pela própria forma. */
    public abstract Forma criar(double dimensao);

    /** Dimensão (raio ou lado) que gera a área informada. */
    public abstract double calcularDimensao(double area);

    /**
     * Cria a forma a partir da área em cm² (enunciado, item 2.c.iv):
     * valida a área (100 a 1600 cm²), calcula a dimensão e cria a forma (que valida os seus limites).
     */
    public Forma criarPorArea(double area) {
        if (area < Forma.AREA_MINIMA || area > Forma.AREA_MAXIMA) {
            throw new IllegalArgumentException(String.format(
                    "A área deve ficar entre %.0f e %.0f cm² (informado: %.2f cm²).",
                    Forma.AREA_MINIMA, Forma.AREA_MAXIMA, area));
        }
        return criar(calcularDimensao(area));
    }

    @Override
    public String toString() {
        return nome;
    }
}
