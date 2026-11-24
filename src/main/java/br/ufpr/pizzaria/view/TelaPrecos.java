package br.ufpr.pizzaria.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.ufpr.pizzaria.model.TipoPizza;

public class TelaPrecos extends TelaBase {

    private final Formulario formulario = new Formulario("Preço do cm² (R$) por tipo de pizza");
    private final Map<TipoPizza, JTextField> campos = new LinkedHashMap<>();
    private final JButton botaoSalvar = new JButton("Salvar");

    public TelaPrecos(JFrame pai) {
        super(pai, "Preço do cm²");

        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(botaoSalvar);
        botoes.add(botaoCancelar);

        JPanel painel = new JPanel(new BorderLayout(5, 5));
        painel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        painel.add(formulario, BorderLayout.CENTER);
        painel.add(new JLabel("Ex.: 0,05 = cinco centavos por cm². Use vírgula ou ponto."), BorderLayout.NORTH);
        painel.add(botoes, BorderLayout.SOUTH);
        setContentPane(painel);
    }

    public void setTipos(List<TipoPizza> tipos) {
        for (TipoPizza tipo : tipos) {
            JTextField campo = new JTextField(Formato.precoCm2(tipo.getPrecoCm2()), 10);
            campos.put(tipo, campo);
            formulario.adiciona(tipo.getNome() + ":", campo);
        }
        pack();
        setLocationRelativeTo(getOwner());
    }

    public Map<TipoPizza, String> getPrecosDigitados() {
        Map<TipoPizza, String> precos = new LinkedHashMap<>();
        for (Map.Entry<TipoPizza, JTextField> item : campos.entrySet()) {
            precos.put(item.getKey(), item.getValue().getText());
        }
        return precos;
    }

    public JButton getBotaoSalvar() {
        return botaoSalvar;
    }
}
