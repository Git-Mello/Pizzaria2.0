package br.ufpr.pizzaria.view;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Base das telas secundárias: janela modal com as mensagens padrão ao usuário.
 * As telas só mostram dados e mensagens; quem decide o que fazer é o Controller.
 */
public abstract class TelaBase extends JDialog {

    protected TelaBase(JFrame pai, String titulo) {
        super(pai, titulo, true);   // true = modal: bloqueia a tela principal enquanto estiver aberta
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, getTitle(), JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    /** Pergunta Sim/Não ao usuário. */
    public boolean confirmar(String pergunta) {
        return JOptionPane.showConfirmDialog(this, pergunta, getTitle(),
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }
}
