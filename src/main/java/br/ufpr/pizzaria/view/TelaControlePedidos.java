package br.ufpr.pizzaria.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import br.ufpr.pizzaria.model.EstadoPedido;

/**
 * Tela com os pedidos de todos os clientes e os seus estados (enunciado, item 5).
 * Permite alterar o estado do pedido selecionado. Quem trata os botões é o ControlePedidosController.
 */
public class TelaControlePedidos extends TelaBase {

    private final JTable tabela;
    private final JComboBox<EstadoPedido> comboEstado = new JComboBox<>(EstadoPedido.values());
    private final JButton botaoAlterarEstado = new JButton("Alterar estado");
    private final JButton botaoAtualizarLista = new JButton("Atualizar lista");

    public TelaControlePedidos(Window pai, PedidoTableModel modelo) {
        super(pai, "Controle de Pedidos");

        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane rolagem = new JScrollPane(tabela);
        rolagem.setPreferredSize(new Dimension(700, 280));

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acoes.setBorder(BorderFactory.createTitledBorder("Pedido selecionado"));
        acoes.add(new JLabel("Novo estado:"));
        acoes.add(comboEstado);
        acoes.add(botaoAlterarEstado);
        acoes.add(botaoAtualizarLista);

        JPanel painel = new JPanel(new BorderLayout(5, 5));
        painel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        painel.add(new JLabel("Pedidos de todos os clientes (mais recentes primeiro)"), BorderLayout.NORTH);
        painel.add(rolagem, BorderLayout.CENTER);
        painel.add(acoes, BorderLayout.SOUTH);
        setContentPane(painel);

        pack();
        setLocationRelativeTo(pai);
    }

    /** Linha selecionada na tabela, ou -1 se nenhuma. */
    public int getLinhaSelecionada() {
        return tabela.getSelectedRow();
    }

    /** Seleciona a linha informada da tabela. */
    public void selecionarLinha(int linha) {
        tabela.setRowSelectionInterval(linha, linha);
    }

    public JTable getTabela() {
        return tabela;
    }

    public EstadoPedido getEstadoEscolhido() {
        return (EstadoPedido) comboEstado.getSelectedItem();
    }

    public void setEstadoEscolhido(EstadoPedido estado) {
        comboEstado.setSelectedItem(estado);
    }

    public JButton getBotaoAlterarEstado() {
        return botaoAlterarEstado;
    }

    public JButton getBotaoAtualizarLista() {
        return botaoAtualizarLista;
    }
}
