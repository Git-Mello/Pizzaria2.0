package br.ufpr.pizzaria.model;

/** Sabor de pizza (tabela sabor), relacionado a um tipo (Simples, Especial ou Premium). */
public class Sabor {

    private long id;
    private String nome;
    private TipoPizza tipo;

    public Sabor(String nome, TipoPizza tipo) {
        setNome(nome);
        setTipo(tipo);
    }

    public Sabor(long id, String nome, TipoPizza tipo) {
        this(nome, tipo);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do sabor é obrigatório.");
        }
        if (nome.trim().length() > 45) {
            throw new IllegalArgumentException("O nome do sabor pode ter no máximo 45 caracteres.");
        }
        this.nome = nome.trim();
    }

    public TipoPizza getTipo() {
        return tipo;
    }

    public void setTipo(TipoPizza tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Escolha o tipo do sabor (Simples, Especial ou Premium).");
        }
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return nome + " (" + tipo.getNome() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Sabor && ((Sabor) obj).id == id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
