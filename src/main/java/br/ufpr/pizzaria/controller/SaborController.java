package br.ufpr.pizzaria.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.ufpr.pizzaria.model.Sabor;
import br.ufpr.pizzaria.model.dao.DaoFactory;
import br.ufpr.pizzaria.model.dao.SaborDao;
import br.ufpr.pizzaria.model.dao.TipoPizzaDao;
import br.ufpr.pizzaria.view.SaborTableModel;
import br.ufpr.pizzaria.view.TelaPrincipal;
import br.ufpr.pizzaria.view.TelaSabores;

/** Controller dos sabores (enunciado, item 4): cadastra e relaciona cada sabor a um tipo. */
public class SaborController {

    private static final String NOME_REPETIDO = "Já existe um sabor com este nome.";

    private final TelaPrincipal principal;
    private final SaborTableModel model = new SaborTableModel();
    private final SaborDao dao = DaoFactory.getSaborDao();
    private final TipoPizzaDao tipoDao = DaoFactory.getTipoPizzaDao();
    private TelaSabores view;

    public SaborController(TelaPrincipal principal) {
        this.principal = principal;
        principal.getBotaoSabores().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTela();
            }
        });
    }

    private void abrirTela() {
        view = new TelaSabores(principal, model);
        try {
            view.setTipos(tipoDao.getAll());
            model.setSabores(dao.getAll());
        } catch (Exception e) {
            principal.mostrarErro(TratadorErros.mensagem(e));
            view.dispose();
            return;
        }
        view.getBotaoIncluir().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                incluir();
            }
        });
        view.getBotaoAtualizar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizar();
            }
        });
        view.getBotaoExcluir().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluir();
            }
        });
        view.getBotaoLimpar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.limparCampos();
            }
        });
        // Ao clicar num sabor da lista, os dados vão para os campos
        view.getTabela().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && view.getLinhaSelecionada() >= 0) {
                    view.preencherCampos(model.getSabor(view.getLinhaSelecionada()));
                }
            }
        });
        view.setVisible(true);
    }

    private void incluir() {
        try {
            Sabor sabor = new Sabor(view.getNome(), view.getTipo());
            dao.add(sabor);
            recarregar();
            view.limparCampos();
            view.mostrarMensagem("Sabor " + sabor.getNome() + " (" + sabor.getTipo() + ") incluído.");
        } catch (Exception e) {
            view.mostrarErro(TratadorErros.mensagem(e, NOME_REPETIDO));
        }
    }

    private void atualizar() {
        Sabor sabor = saborSelecionado();
        if (sabor == null) {
            return;
        }
        try {
            sabor.setNome(view.getNome());
            sabor.setTipo(view.getTipo());
            dao.update(sabor);
            view.mostrarMensagem("Sabor atualizado.");
        } catch (Exception e) {
            view.mostrarErro(TratadorErros.mensagem(e, NOME_REPETIDO));
        }
        recarregar();
    }

    /** Sabor que já está em alguma pizza pedida não pode ser excluído. */
    private void excluir() {
        Sabor sabor = saborSelecionado();
        if (sabor == null) {
            return;
        }
        try {
            if (dao.isEmUso(sabor)) {
                view.mostrarErro("O sabor " + sabor.getNome() + " não pode ser excluído porque está "
                        + "em pizzas de pedidos já feitos.");
                return;
            }
            if (!view.confirmar("Excluir o sabor " + sabor.getNome() + "?")) {
                return;
            }
            dao.delete(sabor);
            recarregar();
            view.limparCampos();
            view.mostrarMensagem("Sabor excluído.");
        } catch (Exception e) {
            view.mostrarErro(TratadorErros.mensagem(e, "O sabor está em uso e não pode ser excluído."));
        }
    }

    private void recarregar() {
        try {
            model.setSabores(dao.getAll());
        } catch (Exception e) {
            view.mostrarErro(TratadorErros.mensagem(e));
        }
    }

    private Sabor saborSelecionado() {
        int linha = view.getLinhaSelecionada();
        if (linha < 0) {
            view.mostrarErro("Selecione um sabor na lista.");
            return null;
        }
        return model.getSabor(linha);
    }
}
