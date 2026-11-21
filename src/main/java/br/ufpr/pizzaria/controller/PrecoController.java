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

/**
 * Controller da atualização do preço do cm² de cada tipo de pizza (enunciado, item 3).
 * Guarda a view e o dao (esta tela não tem tabela, por isso não há TableModel).
 */
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

    /** Valida todos os preços antes de gravar qualquer um. */
    private void salvar() {
        try {
            Map<TipoPizza, String> digitados = view.getPrecosDigitados();
            for (Map.Entry<TipoPizza, String> item : digitados.entrySet()) {
                double preco = Entrada.lerNumero(item.getValue(), "preço da pizza " + item.getKey().getNome());
                item.getKey().setPrecoCm2(preco);   // valida: de 0,0001 a 10,0000
            }
            // Grava os três preços numa única transação: se der erro, nenhum é alterado
            dao.update(new ArrayList<>(digitados.keySet()));
            view.mostrarMensagem("Preços atualizados. Valem para as próximas pizzas montadas.");
            view.dispose();
        } catch (Exception e) {
            view.mostrarErro(TratadorErros.mensagem(e));
        }
    }
}
