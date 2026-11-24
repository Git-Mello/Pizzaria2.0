package br.ufpr.pizzaria.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import br.ufpr.pizzaria.model.Cliente;

/**
 * Tela de clientes (enunciado, item 1): incluir, listar, atualizar, excluir e filtrar.
 * Quem trata os botões é o ClienteController.
 */
public class TelaClientes extends TelaBase {

    private final JTextField campoNome = new JTextField(20);
    private final JTextField campoSobrenome = new JTextField(20);
    private final JTextField campoTelefone = new JTextField(12);
    private final JButton botaoIncluir = new JButton("Incluir");
    private final JButton botaoAtualizar = new JButton("Atualizar");
    private final JButton botaoExcluir = new JButton("Excluir");
    private final JButton botaoLimpar = new JButton("Limpar");

    private final JTextField campoFiltroSobrenome = new JTextField(14);
    private final JTextField campoFiltroTelefone = new JTextField(11);
    private final JButton botaoFiltrar = new JButton("Buscar");
    private final JButton botaoMostrarTodos = new JButton("Mostrar todos");

    private final JTable tabela;

    public TelaClientes(JFrame pai, ClienteTableModel modelo) {
        super(pai, "Clientes");

        // Dados do cliente
        Formulario dados = new Formulario("Dados do cliente");
        dados.adiciona("Nome:", campoNome);
        dados.adiciona("Sobrenome:", campoSobrenome);
        dados.adiciona("Telefone:", campoTelefone);
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(botaoIncluir);
        botoes.add(botaoAtualizar);
        botoes.add(botaoExcluir);
        botoes.add(botaoLimpar);
        JPanel painelDados = new JPanel(new BorderLayout());
        painelDados.add(dados, BorderLayout.CENTER);
        painelDados.add(botoes, BorderLayout.SOUTH);

        // Filtro (enunciado, item 1.d)
        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBorder(BorderFactory.createTitledBorder("Filtrar clientes"));
        filtro.add(new JLabel("Sobrenome:"));
        filtro.add(campoFiltroSobrenome);
        filtro.add(new JLabel("Telefone:"));
        filtro.add(campoFiltroTelefone);
        filtro.add(botaoFiltrar);
        filtro.add(botaoMostrarTodos);

        // Lista (enunciado, item 1.a)
        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane rolagem = new JScrollPane(tabela);
        rolagem.setPreferredSize(new Dimension(620, 220));

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(painelDados, BorderLayout.NORTH);
        topo.add(filtro, BorderLayout.SOUTH);

        JPanel painel = new JPanel(new BorderLayout(5, 5));
        painel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        painel.add(topo, BorderLayout.NORTH);
        painel.add(rolagem, BorderLayout.CENTER);
        painel.add(new JLabel("Clique em um cliente da lista para atualizar ou excluir."), BorderLayout.SOUTH);
        setContentPane(painel);

        pack();
        setLocationRelativeTo(pai);
    }

    // ---------- Campos (lidos e preenchidos pelo Controller) ----------

    public String getNome() {
        return campoNome.getText();
    }

    public String getSobrenome() {
        return campoSobrenome.getText();
    }

    public String getTelefone() {
        return campoTelefone.getText();
    }

    public String getFiltroSobrenome() {
        return campoFiltroSobrenome.getText();
    }

    public String getFiltroTelefone() {
        return campoFiltroTelefone.getText();
    }

    public void limparFiltro() {
        campoFiltroSobrenome.setText("");
        campoFiltroTelefone.setText("");
    }

    public void preencherCampos(Cliente cliente) {
        campoNome.setText(cliente.getNome());
        campoSobrenome.setText(cliente.getSobrenome());
        campoTelefone.setText(cliente.getTelefone());
    }

    public void limparCampos() {
        campoNome.setText("");
        campoSobrenome.setText("");
        campoTelefone.setText("");
        tabela.clearSelection();
        campoNome.requestFocusInWindow();
    }

    /** Linha selecionada na tabela, ou -1 se nenhuma. */
    public int getLinhaSelecionada() {
        return tabela.getSelectedRow();
    }

    public JTable getTabela() {
        return tabela;
    }

    // ---------- Botões (o Controller registra as ações) ----------

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

    public JButton getBotaoFiltrar() {
        return botaoFiltrar;
    }

    public JButton getBotaoMostrarTodos() {
        return botaoMostrarTodos;
    }
}
