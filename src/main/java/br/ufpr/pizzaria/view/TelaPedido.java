package br.ufpr.pizzaria.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import br.ufpr.pizzaria.model.Cliente;
import br.ufpr.pizzaria.model.Pedido;
import br.ufpr.pizzaria.model.Sabor;
import br.ufpr.pizzaria.model.TipoForma;

/**
 * Tela de realização e alteração de pedidos (enunciado, item 2).
 * Fluxo: busca o cliente pelo telefone, escolhe um pedido dele (ou "Novo pedido"),
 * monta as pizzas (forma + dimensão ou área + até 2 sabores) e salva.
 * Quem trata os eventos é o PedidoController.
 */
public class TelaPedido extends TelaBase {

    /** Item da lista de pedidos que representa "criar um pedido novo". */
    private static final String NOVO_PEDIDO = "Novo pedido";

    // Cliente e pedido
    private final JTextField campoTelefone = new JTextField(11);
    private final JButton botaoBuscar = new JButton("Buscar cliente");
    private final JComboBox<Cliente> comboClientes = new JComboBox<>();
    private final JComboBox<Object> comboPedidos = new JComboBox<>();
    private final JLabel rotuloSituacao = new JLabel(" ");

    // Pizza
    private final JComboBox<TipoForma> comboForma = new JComboBox<>(TipoForma.values());
    private final JRadioButton opcaoDimensao = new JRadioButton("Dimensão", true);
    private final JRadioButton opcaoArea = new JRadioButton("Área (cm²)");
    private final JTextField campoDimensao = new JTextField(8);
    private final JLabel rotuloLimites = new JLabel();
    private final JTextField campoArea = new JTextField(8);
    private final JButton botaoCalcular = new JButton("Calcular dimensão");
    private final JComboBox<Sabor> comboSabor1 = new JComboBox<>();
    private final JComboBox<Sabor> comboSabor2 = new JComboBox<>();
    private final JButton botaoAdicionar = new JButton("Adicionar pizza");

    // Pizzas do pedido
    private final JTable tabela;
    private final JButton botaoRemover = new JButton("Remover pizza selecionada");
    private final JButton botaoSalvar = new JButton("Salvar pedido");
    private final JLabel rotuloTotal = new JLabel();

    public TelaPedido(JFrame pai, PizzaTableModel modelo) {
        super(pai, "Realizar / Alterar Pedido");

        // ---- Cliente e pedido ----
        JPanel buscaTelefone = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buscaTelefone.add(campoTelefone);
        buscaTelefone.add(botaoBuscar);
        Formulario cliente = new Formulario("1. Cliente e pedido");
        cliente.adiciona("Telefone do cliente:", buscaTelefone);
        cliente.adiciona("Cliente:", comboClientes);
        cliente.adiciona("Pedido:", comboPedidos);
        cliente.adiciona("", rotuloSituacao);

        // ---- Pizza ----
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(opcaoDimensao);
        grupo.add(opcaoArea);
        JPanel linhaDimensao = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        linhaDimensao.add(opcaoDimensao);
        linhaDimensao.add(campoDimensao);
        linhaDimensao.add(new JLabel("cm"));
        linhaDimensao.add(rotuloLimites);
        JPanel linhaArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        linhaArea.add(opcaoArea);
        linhaArea.add(campoArea);
        linhaArea.add(botaoCalcular);
        linhaArea.add(new JLabel("(100 a 1600 cm²)"));

        // Classe anônima que herda de DefaultListCellRenderer: mostra um texto no lugar do item vazio (null)
        comboSabor2.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor,
                    int indice, boolean selecionado, boolean foco) {
                return super.getListCellRendererComponent(lista, valor == null ? "(sem segundo sabor)" : valor,
                        indice, selecionado, foco);
            }
        });

        Formulario pizza = new Formulario("2. Montar a pizza");
        pizza.adiciona("Forma:", comboForma);
        pizza.adiciona("Tamanho por:", linhaDimensao);
        pizza.adiciona("", linhaArea);
        pizza.adiciona("1º sabor:", comboSabor1);
        pizza.adiciona("2º sabor (opcional):", comboSabor2);
        pizza.adiciona("", botaoAdicionar);

        // ---- Pizzas do pedido ----
        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane rolagem = new JScrollPane(tabela);
        rolagem.setPreferredSize(new Dimension(720, 170));
        rotuloTotal.setFont(rotuloTotal.getFont().deriveFont(Font.BOLD, 15f));
        JPanel rodape = new JPanel(new BorderLayout());
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(botaoRemover);
        botoes.add(botaoSalvar);
        rodape.add(botoes, BorderLayout.WEST);
        rodape.add(rotuloTotal, BorderLayout.EAST);
        JPanel painelPizzas = new JPanel(new BorderLayout());
        painelPizzas.setBorder(BorderFactory.createTitledBorder("3. Pizzas do pedido"));
        painelPizzas.add(rolagem, BorderLayout.CENTER);
        painelPizzas.add(rodape, BorderLayout.SOUTH);

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(cliente, BorderLayout.NORTH);
        topo.add(pizza, BorderLayout.CENTER);

        JPanel painel = new JPanel(new BorderLayout(5, 5));
        painel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        painel.add(topo, BorderLayout.NORTH);
        painel.add(painelPizzas, BorderLayout.CENTER);
        setContentPane(painel);

        // Ajustes visuais que não envolvem regra: habilitar o campo da opção escolhida
        ActionListener trocaModo = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarModoTamanho();
            }
        };
        opcaoDimensao.addActionListener(trocaModo);
        opcaoArea.addActionListener(trocaModo);
        atualizarModoTamanho();
        setTotal(0);
        setPodeEditar(false, "Busque o cliente pelo telefone para começar.");

        pack();
        setLocationRelativeTo(pai);
    }

    private void atualizarModoTamanho() {
        boolean porArea = opcaoArea.isSelected();
        campoArea.setEnabled(porArea);
        botaoCalcular.setEnabled(porArea && botaoAdicionar.isEnabled());
        campoDimensao.setEditable(!porArea);   // no modo área, a dimensão calculada é só mostrada
    }

    // ---------- Cliente e pedido ----------

    public String getTelefoneBusca() {
        return campoTelefone.getText();
    }

    public void setClientes(List<Cliente> clientes) {
        comboClientes.removeAllItems();
        for (Cliente c : clientes) {
            comboClientes.addItem(c);
        }
    }

    public Cliente getClienteSelecionado() {
        return (Cliente) comboClientes.getSelectedItem();
    }

    /** Preenche a lista de pedidos do cliente; o primeiro item é sempre "Novo pedido". */
    public void setPedidos(List<Pedido> pedidos) {
        comboPedidos.removeAllItems();
        comboPedidos.addItem(NOVO_PEDIDO);
        for (Pedido p : pedidos) {
            comboPedidos.addItem(p);
        }
    }

    /** O pedido escolhido, ou null se for "Novo pedido". */
    public Pedido getPedidoSelecionado() {
        Object item = comboPedidos.getSelectedItem();
        return (item instanceof Pedido) ? (Pedido) item : null;
    }

    public void selecionarPedido(long idPedido) {
        for (int i = 0; i < comboPedidos.getItemCount(); i++) {
            Object item = comboPedidos.getItemAt(i);
            if (item instanceof Pedido && ((Pedido) item).getId() == idPedido) {
                comboPedidos.setSelectedIndex(i);
                return;
            }
        }
    }

    public boolean temItemPedido() {
        return comboPedidos.getSelectedItem() != null;
    }

    /** Habilita ou não a montagem de pizzas, mostrando a situação do pedido. */
    public void setPodeEditar(boolean pode, String situacao) {
        rotuloSituacao.setText(situacao);
        for (JComponent c : new JComponent[] {comboForma, opcaoDimensao, opcaoArea,
                campoDimensao, comboSabor1, comboSabor2, botaoAdicionar, botaoRemover, botaoSalvar}) {
            c.setEnabled(pode);
        }
        atualizarModoTamanho();
        campoArea.setEnabled(pode && opcaoArea.isSelected());
    }

    // ---------- Pizza ----------

    public void setSabores(List<Sabor> sabores) {
        comboSabor1.removeAllItems();
        comboSabor2.removeAllItems();
        comboSabor2.addItem(null);   // segundo sabor é opcional
        for (Sabor s : sabores) {
            comboSabor1.addItem(s);
            comboSabor2.addItem(s);
        }
    }

    public TipoForma getForma() {
        return (TipoForma) comboForma.getSelectedItem();
    }

    public boolean isTamanhoPorArea() {
        return opcaoArea.isSelected();
    }

    public String getDimensao() {
        return campoDimensao.getText();
    }

    public void setDimensao(String dimensao) {
        campoDimensao.setText(dimensao);
    }

    public String getArea() {
        return campoArea.getText();
    }

    public void setLimites(String texto) {
        rotuloLimites.setText(texto);
    }

    public Sabor getSabor1() {
        return (Sabor) comboSabor1.getSelectedItem();
    }

    public Sabor getSabor2() {
        return (Sabor) comboSabor2.getSelectedItem();
    }

    public void limparPizza() {
        campoDimensao.setText("");
        campoArea.setText("");
        comboSabor2.setSelectedIndex(0);
    }

    // ---------- Pizzas do pedido ----------

    public int getLinhaSelecionada() {
        return tabela.getSelectedRow();
    }

    public void setTotal(double total) {
        rotuloTotal.setText("Total do pedido: " + Formato.moeda(total) + "  ");
    }

    // ---------- Componentes em que o Controller registra as ações ----------

    public JButton getBotaoBuscar() {
        return botaoBuscar;
    }

    public JComboBox<Cliente> getComboClientes() {
        return comboClientes;
    }

    public JComboBox<Object> getComboPedidos() {
        return comboPedidos;
    }

    public JComboBox<TipoForma> getComboForma() {
        return comboForma;
    }

    public JButton getBotaoCalcular() {
        return botaoCalcular;
    }

    public JButton getBotaoAdicionar() {
        return botaoAdicionar;
    }

    public JButton getBotaoRemover() {
        return botaoRemover;
    }

    public JButton getBotaoSalvar() {
        return botaoSalvar;
    }
}
