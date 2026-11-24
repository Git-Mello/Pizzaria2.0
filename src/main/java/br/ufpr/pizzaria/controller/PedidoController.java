package br.ufpr.pizzaria.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.WindowConstants;

import br.ufpr.pizzaria.model.Cliente;
import br.ufpr.pizzaria.model.Forma;
import br.ufpr.pizzaria.model.Pedido;
import br.ufpr.pizzaria.model.Pizza;
import br.ufpr.pizzaria.model.Sabor;
import br.ufpr.pizzaria.model.TipoForma;
import br.ufpr.pizzaria.model.dao.ClienteDao;
import br.ufpr.pizzaria.model.dao.DaoFactory;
import br.ufpr.pizzaria.model.dao.PedidoDao;
import br.ufpr.pizzaria.model.dao.SaborDao;
import br.ufpr.pizzaria.view.Formato;
import br.ufpr.pizzaria.view.PizzaTableModel;
import br.ufpr.pizzaria.view.TelaPedido;
import br.ufpr.pizzaria.view.TelaPrincipal;

public class PedidoController {

    private final TelaPrincipal principal;
    private final PizzaTableModel model = new PizzaTableModel();
    private final PedidoDao dao = DaoFactory.getPedidoDao();
    private final ClienteDao clienteDao = DaoFactory.getClienteDao();
    private final SaborDao saborDao = DaoFactory.getSaborDao();

    private TelaPedido view;
    private Pedido pedidoAtual;
    private boolean alterado;
    private boolean ignorarEventos;
    private int indicePedidoAnterior;
    private int indiceClienteAnterior;

    public PedidoController(TelaPrincipal principal) {
        this.principal = principal;
        principal.getBotaoPedido().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTela();
            }
        });
    }

    private void abrirTela() {
        List<Sabor> sabores;
        try {
            sabores = saborDao.getAll();
        } catch (Exception e) {
            principal.mostrarErro(TratadorErros.mensagem(e));
            return;
        }
        if (sabores.isEmpty()) {
            principal.mostrarErro("Não há sabores cadastrados. Cadastre os sabores antes de fazer pedidos.");
            return;
        }
        pedidoAtual = null;
        alterado = false;
        model.setPizzas(new ArrayList<>());

        view = new TelaPedido(principal, model);
        view.setSabores(sabores);
        atualizarLimites();

        view.getBotaoBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarCliente();
            }
        });
        view.getComboClientes().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ignorarEventos) {
                    clienteEscolhido();
                }
            }
        });
        view.getComboPedidos().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ignorarEventos) {
                    pedidoEscolhido();
                }
            }
        });
        view.getComboForma().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarLimites();
            }
        });
        view.getBotaoCalcular().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularDimensao();
            }
        });
        view.getBotaoAdicionar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarPizza();
            }
        });
        view.getBotaoRemover().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerPizza();
            }
        });
        view.getBotaoSalvar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvar();
            }
        });

        view.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (podeDescartar()) {
                    view.dispose();
                }
            }
        });
        view.setVisible(true);
    }

    private void buscarCliente() {
        String telefone = view.getTelefoneBusca().trim();
        if (!Cliente.soNumeros(telefone)) {
            view.mostrarErro("Digite o telefone do cliente (ou parte dele), só com números.");
            return;
        }
        if (!podeDescartar()) {
            return;
        }
        try {
            List<Cliente> clientes = clienteDao.getByFiltro("", telefone);
            if (clientes.isEmpty()) {
                view.mostrarErro("Nenhum cliente com esse telefone. Cadastre o cliente antes na tela de Clientes.");
                return;
            }
            ignorarEventos = true;
            view.setClientes(clientes);
            ignorarEventos = false;
            alterado = false;
            clienteEscolhido();
        } catch (Exception e) {
            ignorarEventos = false;
            view.mostrarErro(TratadorErros.mensagem(e));
        }
    }

    private void clienteEscolhido() {
        Cliente cliente = view.getClienteSelecionado();
        if (cliente == null) {
            return;
        }
        if (!podeDescartar()) {
            ignorarEventos = true;
            view.getComboClientes().setSelectedIndex(indiceClienteAnterior);
            ignorarEventos = false;
            return;
        }
        indiceClienteAnterior = view.getComboClientes().getSelectedIndex();
        try {
            List<Pedido> pedidos = dao.getByCliente(cliente);
            ignorarEventos = true;
            view.setPedidos(pedidos);
            if (!pedidos.isEmpty()) {
                view.getComboPedidos().setSelectedIndex(1);
            }
            ignorarEventos = false;
            alterado = false;
            pedidoEscolhido();
        } catch (Exception e) {
            ignorarEventos = false;
            view.mostrarErro(TratadorErros.mensagem(e));
        }
    }

    private void pedidoEscolhido() {
        if (!view.temItemPedido() || view.getClienteSelecionado() == null) {
            return;
        }
        if (!podeDescartar()) {
            ignorarEventos = true;
            view.getComboPedidos().setSelectedIndex(indicePedidoAnterior);
            ignorarEventos = false;
            return;
        }
        indicePedidoAnterior = view.getComboPedidos().getSelectedIndex();
        Pedido escolhido = view.getPedidoSelecionado();
        if (escolhido == null) {
            pedidoAtual = new Pedido(view.getClienteSelecionado());
            view.setPodeEditar(true, "Novo pedido: monte as pizzas e clique em Salvar pedido.");
        } else {
            try {
                pedidoAtual = dao.getById(escolhido.getId());
            } catch (Exception e) {
                pedidoAtual = null;
                view.setPodeEditar(false, "Não foi possível abrir o pedido " + escolhido.getId() + ".");
                atualizarPizzas();
                view.mostrarErro(TratadorErros.mensagem(e));
                return;
            }
            if (pedidoAtual == null) {
                view.setPodeEditar(false, "O pedido " + escolhido.getId() + " não existe mais.");
                atualizarPizzas();
                return;
            }
            if (pedidoAtual.podeSerAlterado()) {
                view.setPodeEditar(true, "Pedido " + pedidoAtual.getId() + " aberto: pode ser alterado.");
            } else {
                view.setPodeEditar(false, "Pedido " + pedidoAtual.getId() + " está \"" + pedidoAtual.getEstado()
                        + "\" e não pode mais ser alterado.");
            }
        }
        alterado = false;
        atualizarPizzas();
    }

    private void atualizarLimites() {
        view.setLimites("(" + view.getForma().getDescricaoLimites() + ")");
    }

    private void calcularDimensao() {
        try {
            Forma forma = formaPelaArea();
            view.setDimensao(Formato.numero(forma.getDimensao()));
        } catch (Exception e) {
            view.setDimensao("");
            view.mostrarErro(TratadorErros.mensagem(e));
        }
    }

    private Forma formaPelaArea() {
        double area = Entrada.lerNumero(view.getArea(), "área");
        return view.getForma().criarPorArea(area);
    }

    private void adicionarPizza() {
        try {
            TipoForma tipo = view.getForma();
            Forma forma;
            if (view.isTamanhoPorArea()) {
                forma = formaPelaArea();
                view.setDimensao(Formato.numero(forma.getDimensao()));
            } else {
                forma = tipo.criar(Entrada.lerNumero(view.getDimensao(), "dimensão"));
            }
            List<Sabor> sabores = new ArrayList<>();
            if (view.getSabor1() != null) {
                sabores.add(view.getSabor1());
            }
            if (view.getSabor2() != null) {
                sabores.add(view.getSabor2());
            }
            Pizza pizza = new Pizza(forma, sabores);
            pedidoAtual.adicionarPizza(pizza);
            alterado = true;
            atualizarPizzas();
            view.limparPizza();
        } catch (Exception e) {
            view.mostrarErro(TratadorErros.mensagem(e));
        }
    }

    private void removerPizza() {
        try {
            pedidoAtual.removerPizza(view.getLinhaSelecionada());
            alterado = true;
            atualizarPizzas();
        } catch (Exception e) {
            view.mostrarErro(TratadorErros.mensagem(e));
        }
    }

    private void atualizarPizzas() {
        model.setPizzas(pedidoAtual == null ? new ArrayList<>() : pedidoAtual.getPizzas());
        view.setTotal(pedidoAtual == null ? 0 : pedidoAtual.getPrecoTotal());
    }

    private void salvar() {
        if (pedidoAtual == null) {
            view.mostrarErro("Busque o cliente pelo telefone antes de montar o pedido.");
            return;
        }
        if (pedidoAtual.getPizzas().isEmpty()) {
            view.mostrarErro("Adicione pelo menos uma pizza ao pedido.");
            return;
        }
        try {
            boolean novo = pedidoAtual.getId() == 0;
            if (novo) {
                dao.add(pedidoAtual);
            } else {
                dao.update(pedidoAtual);
            }
            alterado = false;
            long id = pedidoAtual.getId();
            view.mostrarMensagem("Pedido " + (novo ? " criado" : " alterado") + " para "
                    + pedidoAtual.getCliente().getNome() + ".\nTotal: " + Formato.moeda(pedidoAtual.getPrecoTotal()));
            ignorarEventos = true;
            view.setPedidos(dao.getByCliente(pedidoAtual.getCliente()));
            view.selecionarPedido(id);
            ignorarEventos = false;
            pedidoEscolhido();
        } catch (Exception e) {
            ignorarEventos = false;
            view.mostrarErro(TratadorErros.mensagem(e));
        }
    }

    private boolean podeDescartar() {
        return !alterado || view.confirmar("As alterações do pedido atual não foram salvas. Descartar?");
    }
}
