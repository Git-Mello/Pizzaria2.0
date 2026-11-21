package br.ufpr.pizzaria.model;

/**
 * Tipo da pizza (tabela tipo_pizza): Simples, Especial ou Premium,
 * cada um com o seu preço por centímetro quadrado.
 */
public class TipoPizza {

    // Limites do preço do cm² (R$). O mínimo é a menor fração que o banco guarda (4 casas).
    public static final double PRECO_MINIMO = 0.0001;
    public static final double PRECO_MAXIMO = 10;

    private long id;
    private String nome;
    private double precoCm2;

    public TipoPizza(long id, String nome, double precoCm2) {
        this.id = id;
        this.nome = nome;
        setPrecoCm2(precoCm2);
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPrecoCm2() {
        return precoCm2;
    }

    /**
     * O banco guarda o preço com 4 casas decimais (DECIMAL(10,4)); por isso o valor é arredondado
     * para 4 casas antes de validar. Assim, um valor como 0,00001 (que viraria 0,0000 no banco)
     * é recusado aqui, com mensagem, em vez de gravar zero.
     */
    public void setPrecoCm2(double precoCm2) {
        double arredondado = Math.round(precoCm2 * 10000) / 10000.0;
        if (Double.isNaN(precoCm2) || arredondado < PRECO_MINIMO || arredondado > PRECO_MAXIMO) {
            throw new IllegalArgumentException(String.format(
                    "O preço do cm² da pizza %s deve ficar entre %.4f e %.4f (até 4 casas decimais).",
                    nome, PRECO_MINIMO, PRECO_MAXIMO));
        }
        this.precoCm2 = arredondado;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TipoPizza && ((TipoPizza) obj).id == id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
