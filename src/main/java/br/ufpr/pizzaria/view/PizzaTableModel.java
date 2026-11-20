package br.ufpr.pizzaria.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.ufpr.pizzaria.model.Pizza;

/** Modelo da tabela com as pizzas do pedido (tela de pedido). */
public class PizzaTableModel extends AbstractTableModel {

    private final String[] colunas = {"Forma", "Dimensão (cm)", "Área (cm²)", "Sabores", "Preço"};
    private List<Pizza> pizzas = new ArrayList<>();

    public void setPizzas(List<Pizza> pizzas) {
        this.pizzas = pizzas;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return pizzas.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int coluna) {
        return colunas[coluna];
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        Pizza p = pizzas.get(linha);
        switch (coluna) {
            case 0: return p.getForma().getNome();
            case 1: return p.getForma().getNomeDimensao() + " " + Formato.numero(p.getForma().getDimensao());
            case 2: return Formato.numero(p.getArea());
            case 3: return p.getDescricaoSabores();
            case 4: return Formato.moeda(p.getPreco());
            default: return null;
        }
    }
}
