package br.ufpr.pizzaria.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.ufpr.pizzaria.model.Cliente;

public class ClienteTableModel extends AbstractTableModel {

    private final String[] colunas = {"ID", "Nome", "Sobrenome", "Telefone"};
    private List<Cliente> clientes = new ArrayList<>();

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
        fireTableDataChanged();
    }

    public Cliente getCliente(int linha) {
        return clientes.get(linha);
    }

    @Override
    public int getRowCount() {
        return clientes.size();
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
        Cliente c = clientes.get(linha);
        switch (coluna) {
            case 0: return c.getId();
            case 1: return c.getNome();
            case 2: return c.getSobrenome();
            case 3: return c.getTelefone();
            default: return null;
        }
    }
}
