package br.ufpr.pizzaria.model;

/** Cliente da pizzaria (tabela cliente). O id é gerado pelo MySQL. */
public class Cliente {

    private long id;
    private String nome;
    private String sobrenome;
    private String telefone;

    public Cliente(String nome, String sobrenome, String telefone) {
        setNome(nome);
        setSobrenome(sobrenome);
        setTelefone(telefone);
    }

    public Cliente(long id, String nome, String sobrenome, String telefone) {
        this(nome, sobrenome, telefone);
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
        this.nome = validaTexto(nome, "nome");
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = validaTexto(sobrenome, "sobrenome");
    }

    public String getTelefone() {
        return telefone;
    }

    /** Telefone: só números, com DDD (10 ou 11 dígitos). */
    public void setTelefone(String telefone) {
        if (telefone == null || !telefone.trim().matches("\\d{10,11}")) {
            throw new IllegalArgumentException("O telefone deve ter 10 ou 11 números, com DDD e sem espaços ou traços.");
        }
        this.telefone = telefone.trim();
    }

    /** Nome e sobrenome: obrigatórios, até 45 letras (aceita acentos, espaço, apóstrofo e hífen). */
    private static String validaTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("O " + campo + " é obrigatório.");
        }
        String limpo = texto.trim();
        if (limpo.length() > 45) {
            throw new IllegalArgumentException("O " + campo + " pode ter no máximo 45 caracteres.");
        }
        if (!limpo.matches("[\\p{L} '\\-]+")) {
            throw new IllegalArgumentException("O " + campo + " deve conter apenas letras.");
        }
        return limpo;
    }

    @Override
    public String toString() {
        return nome + " " + sobrenome + " (" + telefone + ")";
    }
}
