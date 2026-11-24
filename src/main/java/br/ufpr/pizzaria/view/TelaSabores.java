package br.ufpr.pizzaria.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import br.ufpr.pizzaria.model.Sabor;
import br.ufpr.pizzaria.model.TipoPizza;

public class TelaSabores extends TelaBase {

    private final JTextField campoNome = new JTextField(20);
    private final JComboBox<TipoPizza> comboTipo = new JComboBox<>();
    private final JButton botaoIncluir = new JButton("Incluir");
    private final JButton botaoAtualizar = new JButton("Atualizar");
    private final JButton botaoExcluir = new JButton("Excluir");
    private final JButton botaoLimpar = new JButton("Limpar");
    private final JTable tabela;

    public TelaSabores(JFrame pai, SaborTableModel modelo) {
        super(pai, "Sabores");

        Formulario dados = new Formulario("Dados do sabor");
        dados.adiciona("Nome do sabor:", campoNome);
        dados.adiciona("Tipo:", comboTipo);
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(botaoIncluir);
        botoes.add(botaoAtualizar);
        botoes.add(botaoExcluir);
        botoes.add(botaoLimpar);
        JPanel topo = new JPanel(new BorderLayout());
        topo.add(dados, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane rolagem = new JScrollPane(tabela);
        rolagem.setPreferredSize(new Dimension(480, 220));

        JPanel painel = new JPanel(new BorderLayout(5, 5));
        painel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        painel.add(topo, BorderLayout.NORTH);
        painel.add(rolagem, BorderLayout.CENTER);
        painel.add(new JLabel("Clique em um sabor da lista para atualizar ou excluir."), BorderLayout.SOUTH);
        setContentPane(painel);

        pack();
        setLocationRelativeTo(pai);
    }

    public void setTipos(List<TipoPizza> tipos) {
        comboTipo.removeAllItems();
        for (TipoPizza tipo : tipos) {
            comboTipo.addItem(tipo);
        }
    }

    public String getNome() {
        return campoNome.getText();
    }

    public TipoPizza getTipo() {
        return (TipoPizza) comboTipo.getSelectedItem();
    }

    public void preencherCampos(Sabor sabor) {
        campoNome.setText(sabor.getNome());
        comboTipo.setSelectedItem(sabor.getTipo());
    }

    public void limparCampos() {
        campoNome.setText("");
        if (comboTipo.getItemCount() > 0) {
            comboTipo.setSelectedIndex(0);
        }
        tabela.clearSelection();
        campoNome.requestFocusInWindow();
    }

    public int getLinhaSelecionada() {
        return tabela.getSelectedRow();
    }

    public JTable getTabela() {
        return tabela;
    }

    public JButton getBotaoIncluir() {
        return botaoIncluir;
    }

    public JButton getBotaoAtualizar() {
        return botaoAtualizar;
    }

    public JButton getBotaoExcluir() {
        return botaoExcluir;
    }

    public JButton getBotaoLimpar() {
        return botaoLimpar;
    }
}
