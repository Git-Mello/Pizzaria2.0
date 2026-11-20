package br.ufpr.pizzaria.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Painel auxiliar das telas: monta linhas "rótulo: campo" alinhadas. */
class Formulario extends JPanel {

    private int linha = 0;

    Formulario(String titulo) {
        super(new GridBagLayout());
        if (titulo != null) {
            setBorder(BorderFactory.createTitledBorder(titulo));
        }
    }

    /** Acrescenta uma linha com rótulo e um ou mais componentes lado a lado. */
    void adiciona(String rotulo, JComponent... componentes) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = linha++;
        c.insets = new Insets(3, 5, 3, 5);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        add(new JLabel(rotulo), c);
        for (int i = 0; i < componentes.length; i++) {
            c.gridx = i + 1;
            c.fill = (i == 0) ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
            c.weightx = (i == 0) ? 1 : 0;
            add(componentes[i], c);
        }
    }
}
