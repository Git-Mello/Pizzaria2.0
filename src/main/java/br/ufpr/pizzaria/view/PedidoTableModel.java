package br.ufpr.pizzaria.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.ufpr.pizzaria.model.Pedido;

public class PedidoTableModel extends AbstractTableModel {

    private final String[] colunas = {"Pedido", "Cliente", "Telefone", "Pizzas", "Total", "Situação"};
    private List<Pedido> pedidos = new ArrayList<>();

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
        fireTableDataChanged();
    }

    public Pedido getPedido(int linha) {
        return pedidos.get(linha);
    }

    @Override
    public int getRowCount() {
        return pedidos.size();
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
        Pedido p = pedidos.get(linha);
        switch (coluna) {
            case 0: return p.getId();
            case 1: return p.getCliente().getNome() + " " + p.getCliente().getSobrenome();
            case 2: return p.getCliente().getTelefone();
            case 3: return p.getPizzas().size();
            case 4: return Formato.moeda(p.getPrecoTotal());
            case 5: return p.getEstado().toString();
            default: return null;
        }
    }
}
