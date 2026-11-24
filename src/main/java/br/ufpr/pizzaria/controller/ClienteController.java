package br.ufpr.pizzaria.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.ufpr.pizzaria.model.Cliente;
import br.ufpr.pizzaria.model.dao.ClienteDao;
import br.ufpr.pizzaria.model.dao.DaoFactory;
import br.ufpr.pizzaria.model.dao.PedidoDao;
import br.ufpr.pizzaria.view.ClienteTableModel;
import br.ufpr.pizzaria.view.TelaClientes;
import br.ufpr.pizzaria.view.TelaPrincipal;

public class ClienteController {

    private static final String TELEFONE_REPETIDO = "Já existe um cliente cadastrado com este telefone.";

    private final TelaPrincipal principal;
    private final ClienteTableModel model = new ClienteTableModel();
    private final ClienteDao dao = DaoFactory.getClienteDao();
    private final PedidoDao pedidoDao = DaoFactory.getPedidoDao();
    private TelaClientes view;

    public ClienteController(TelaPrincipal principal) {
        this.principal = principal;
        principal.getBotaoClientes().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTela();
            }
        });
    }

    private void abrirTela() {
        try {
            model.setClientes(dao.getAll());
        } catch (Exception e) {
            principal.mostrarErro(TratadorErros.mensagem(e));
            return;
        }
        view = new TelaClientes(principal, model);
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
        view.getBotaoFiltrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrar();
            }
        });
        view.getBotaoMostrarTodos().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarTodos();
            }
        });
        view.getTabela().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && view.getLinhaSelecionada() >= 0) {
                    view.preencherCampos(model.getCliente(view.getLinhaSelecionada()));
                }
            }
        });
        view.setVisible(true);
    }

    private void incluir() {
        try {
            Cliente cliente = new Cliente(view.getNome(), view.getSobrenome(), view.getTelefone());
            dao.add(cliente);
            recarregar();
            view.limparCampos();
            view.mostrarMensagem("Cliente " + cliente.getNome() + " incluído.");
        } catch (Exception e) {
            view.mostrarErro(TratadorErros.mensagem(e, TELEFONE_REPETIDO));
        }
    }

    private void atualizar() {
        Cliente cliente = clienteSelecionado();
        if (cliente == null) {
            return;
        }
        try {
            cliente.setNome(view.getNome());
            cliente.setSobrenome(view.getSobrenome());
            cliente.setTelefone(view.getTelefone());
            dao.update(cliente);
            view.mostrarMensagem("Cliente atualizado.");
        } catch (Exception e) {
            view.mostrarErro(TratadorErros.mensagem(e, TELEFONE_REPETIDO));
        }
        recarregar();
    }

    private void excluir() {
        Cliente cliente = clienteSelecionado();
        if (cliente == null) {
            return;
        }
        try {
            int pedidos = pedidoDao.getByCliente(cliente).size();
            String pergunta = "Excluir o cliente " + cliente.getNome() + " " + cliente.getSobrenome() + "?";
            if (pedidos > 0) {
                pergunta += "\nOs " + pedidos + " pedido(s) dele também serão excluídos.";
            }
            if (!view.confirmar(pergunta)) {
                return;
            }
            dao.delete(cliente);
            recarregar();
            view.limparCampos();
            view.mostrarMensagem("Cliente excluído.");
        } catch (Exception e) {
            view.mostrarErro(TratadorErros.mensagem(e));
        }
    }

    private void filtrar() {
        try {
            model.setClientes(dao.getByFiltro(view.getFiltroSobrenome(), view.getFiltroTelefone()));
            if (model.getRowCount() == 0) {
                view.mostrarMensagem("Nenhum cliente encontrado com esse filtro.");
            }
        } catch (Exception e) {
            view.mostrarErro(TratadorErros.mensagem(e));
        }
    }

    private void mostrarTodos() {
        view.limparFiltro();
        recarregar();
    }

    private void recarregar() {
        try {
            model.setClientes(dao.getByFiltro(view.getFiltroSobrenome(), view.getFiltroTelefone()));
        } catch (Exception e) {
            view.mostrarErro(TratadorErros.mensagem(e));
        }
    }

    private Cliente clienteSelecionado() {
        int linha = view.getLinhaSelecionada();
        if (linha < 0) {
            view.mostrarErro("Selecione um cliente na lista.");
            return null;
        }
        return model.getCliente(linha);
    }
}
