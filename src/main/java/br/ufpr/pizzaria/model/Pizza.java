package br.ufpr.pizzaria.model;

import java.util.ArrayList;
import java.util.List;

public class Pizza {

    public static final int MAXIMO_SABORES = 2;

    private long id;
    private Forma forma;
    private List<Sabor> sabores;
    private double preco;

    public Pizza(Forma forma, List<Sabor> sabores) {
        setForma(forma);
        setSabores(sabores);
        this.preco = calcularPreco();
    }

    public Pizza(long id, Forma forma, List<Sabor> sabores, double preco) {
        setForma(forma);
        setSabores(sabores);
        this.id = id;
        this.preco = preco;
    }

    private double calcularPreco() {
        double somaPrecoCm2 = 0;
        for (Sabor sabor : sabores) {
            somaPrecoCm2 += sabor.getTipo().getPrecoCm2();
        }
        double mediaPrecoCm2 = somaPrecoCm2 / sabores.size();
        return forma.calcularArea() * mediaPrecoCm2;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Forma getForma() {
        return forma;
    }

    private void setForma(Forma forma) {
        if (forma == null) {
            throw new IllegalArgumentException("Escolha a forma da pizza.");
        }
        this.forma = forma;
    }

    public List<Sabor> getSabores() {
        return new ArrayList<>(sabores);
    }

    private void setSabores(List<Sabor> sabores) {
        if (sabores == null || sabores.isEmpty()) {
            throw new IllegalArgumentException("Escolha pelo menos um sabor.");
        }
        if (sabores.size() > MAXIMO_SABORES) {
            throw new IllegalArgumentException("A pizza pode ter no máximo " + MAXIMO_SABORES + " sabores.");
        }
        if (sabores.size() == 2 && sabores.get(0).equals(sabores.get(1))) {
            throw new IllegalArgumentException("Os dois sabores são iguais: escolha sabores diferentes ou só um.");
        }
        this.sabores = new ArrayList<>(sabores);
    }

    public double getPreco() {
        return preco;
    }

    public double getArea() {
        return forma.calcularArea();
    }

    public String getDescricaoSabores() {
        String descricao = "";
        for (Sabor sabor : sabores) {
            if (!descricao.isEmpty()) {
                descricao += " / ";
            }
            descricao += sabor.getNome();
        }
        return descricao;
    }
}
