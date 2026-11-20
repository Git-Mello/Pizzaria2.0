package br.ufpr.pizzaria.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.ufpr.pizzaria.model.Sabor;

/** Modelo da tabela de sabores (enunciado, item 4). */
public class SaborTableModel extends AbstractTableModel {

    private final String[] colunas = {"Código", "Sabor", "Tipo", "Preço do cm²"};
    private List<Sabor> sabores = new ArrayList<>();

    public void setSabores(List<Sabor> sabores) {
        this.sabores = sabores;
        fireTableDataChanged();
    }

    public Sabor getSabor(int linha) {
        return sabores.get(linha);
    }

    @Override
    public int getRowCount() {
        return sabores.size();
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
        Sabor s = sabores.get(linha);
        switch (coluna) {
            case 0: return s.getId();
            case 1: return s.getNome();
            case 2: return s.getTipo().getNome();
            case 3: return "R$ " + Formato.precoCm2(s.getTipo().getPrecoCm2());
            default: return null;
        }
    }
}
