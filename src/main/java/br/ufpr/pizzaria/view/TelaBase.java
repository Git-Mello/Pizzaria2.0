package br.ufpr.pizzaria.view;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public abstract class TelaBase extends JDialog {

    protected TelaBase(JFrame pai, String titulo) {
        super(pai, titulo, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, getTitle(), JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    public boolean confirmar(String pergunta) {
        return JOptionPane.showConfirmDialog(this, pergunta, getTitle(),
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }
}
