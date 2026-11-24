package br.ufpr.pizzaria.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Tela principal (menu). Não sabe o que os botões fazem:
 * cada Controller registra a sua ação no botão correspondente.
 */
public class TelaPrincipal extends JFrame {

    private final JButton botaoClientes = new JButton("Clientes");
    private final JButton botaoPedido = new JButton("Realizar / Alterar Pedido");
    private final JButton botaoControlePedidos = new JButton("Controle de Pedidos");
    private final JButton botaoSabores = new JButton("Sabores");
    private final JButton botaoPrecos = new JButton("Preço do cm²");

    public TelaPrincipal() {
        super("Pizzaria 2.0");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel titulo = new JLabel("Pizzaria 2.0", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 22f));
    

        JPanel cabecalho = new JPanel(new GridLayout(2, 1));
        cabecalho.add(titulo);

        JPanel botoes = new JPanel(new GridLayout(0, 1, 8, 8));
        botoes.add(botaoPedido);
        botoes.add(botaoControlePedidos);
        botoes.add(botaoClientes);
        botoes.add(botaoSabores);
        botoes.add(botaoPrecos);

        JPanel painel = new JPanel(new BorderLayout(10, 15));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 40, 25, 40));
        painel.add(cabecalho, BorderLayout.NORTH);
        painel.add(botoes, BorderLayout.CENTER);
        setContentPane(painel);

        pack();
        setSize(380, getHeight());
        setLocationRelativeTo(null);
    }

    public JButton getBotaoClientes() {
        return botaoClientes;
    }

    public JButton getBotaoPedido() {
        return botaoPedido;
    }

    public JButton getBotaoControlePedidos() {
        return botaoControlePedidos;
    }

    public JButton getBotaoSabores() {
        return botaoSabores;
    }

    public JButton getBotaoPrecos() {
        return botaoPrecos;
    }

    public void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }
}
