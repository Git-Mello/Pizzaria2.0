package br.ufpr.pizzaria.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import br.ufpr.pizzaria.model.TipoPizza;
import br.ufpr.pizzaria.model.dao.DaoFactory;
import br.ufpr.pizzaria.model.dao.TipoPizzaDao;
import br.ufpr.pizzaria.view.TelaPrecos;
import br.ufpr.pizzaria.view.TelaPrincipal;

public class PrecoController {

    private final TelaPrincipal principal;
    private final TipoPizzaDao dao = DaoFactory.getTipoPizzaDao();
    private TelaPrecos view;

    public PrecoController(TelaPrincipal principal) {
        this.principal = principal;
        principal.getBotaoPrecos().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTela();
            }
        });
    }

    private void abrirTela() {
        view = new TelaPrecos(principal);
        try {
            view.setTipos(dao.getAll());
        } catch (Exception e) {
            principal.mostrarErro(TratadorErros.mensagem(e));
            view.dispose();
            return;
        }
        view.getBotaoSalvar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvar();
            }
        });
        view.setVisible(true);
    }

    private void salvar() {
        try {
            Map<TipoPizza, String> digitados = view.getPrecosDigitados();
            for (Map.Entry<TipoPizza, String> item : digitados.entrySet()) {
                double preco = Entrada.lerNumero(item.getValue(), "preço da pizza " + item.getKey().getNome());
                item.getKey().setPrecoCm2(preco);
            }
            dao.update(new ArrayList<>(digitados.keySet()));
            view.mostrarMensagem("Preços atualizados. Valem para as próximas pizzas montadas.");
            view.dispose();
        } catch (Exception e) {
            view.mostrarErro(TratadorErros.mensagem(e));
        }
    }
}
