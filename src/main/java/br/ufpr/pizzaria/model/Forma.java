package br.ufpr.pizzaria.model;

/**
 * Classe abstrata (base da herança) para as formas da pizza.
 * Cada subclasse (Circulo, Quadrado, Triangulo) define a sua fórmula de área
 * e os seus limites: o cálculo é polimórfico — quem usa a forma só chama calcularArea().
 */
public abstract class Forma {

    // Área mínima e máxima aceitas quando o usuário informa a pizza pela área (cm²)
    public static final double AREA_MINIMA = 100;
    public static final double AREA_MAXIMA = 1600;

    // Raio (círculo) ou lado (quadrado e triângulo), em cm
    private double dimensao;

    protected Forma(double dimensao) {
        setDimensao(dimensao);
    }

    public double getDimensao() {
        return dimensao;
    }

    /** Valida a dimensão conforme os limites da forma (enunciado, item 2.c.ii). */
    public final void setDimensao(double dimensao) {
        if (dimensao < getDimensaoMinima() || dimensao > getDimensaoMaxima()) {
            throw new IllegalArgumentException(String.format(
                    "O %s do %s deve ter entre %.0f e %.0f cm (informado: %.2f cm).",
                    getNomeDimensao(), getNome().toLowerCase(), getDimensaoMinima(), getDimensaoMaxima(), dimensao));
        }
        this.dimensao = dimensao;
    }

    // ---------- Métodos que cada forma implementa do seu jeito (polimorfismo) ----------

    /** Área da forma em cm². */
    public abstract double calcularArea();

    /** Nome da forma para exibição: "Círculo", "Quadrado" ou "Triângulo". */
    public abstract String getNome();

    /** Nome da dimensão: "raio" ou "lado". */
    public abstract String getNomeDimensao();

    public abstract double getDimensaoMinima();

    public abstract double getDimensaoMaxima();

    /** Tipo da forma, usado para gravar no banco (coluna pizza.forma). */
    public abstract TipoForma getTipo();

    @Override
    public String toString() {
        return String.format("%s (%s %.2f cm)", getNome(), getNomeDimensao(), dimensao);
    }
}
