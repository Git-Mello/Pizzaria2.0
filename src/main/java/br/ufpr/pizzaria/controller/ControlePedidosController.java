package br.ufpr.pizzaria.controller;

import br.ufpr.pizzaria.model.EstadoPedido;
import br.ufpr.pizzaria.model.Pedido;
import br.ufpr.pizzaria.model.dao.DaoFactory;
import br.ufpr.pizzaria.model.dao.PedidoDao;
import br.ufpr.pizzaria.view.PedidoTableModel;
import br.ufpr.pizzaria.view.TelaControlePedidos;
import br.ufpr.pizzaria.view.TelaPrincipal;

/** Controller da tela de pedidos de todos os clientes e seus estados (enunciado, item 5). */
public class ControlePedidosController {

    private final TelaPrincipal principal;
    private final PedidoTableModel model = new PedidoTableModel();
    private final PedidoDao dao = DaoFactory.getPedidoDao();
    private TelaControlePedidos view;

    public ControlePedidosController(TelaPrincipal principal) {
        this.principal = principal;
        principal.getBotaoControlePedidos().addActionListener(e -> abrirTela());
    }

    private void abrirTela() {
        try {
            model.setPedidos(dao.getAll());
        } catch (Exception e) {
            principal.mostrarErro(TratadorErros.mensagem(e));
            return;
        }
        view = new TelaControlePedidos(principal, model);
        view.getBotaoAlterarEstado().addActionListener(e -> alterarEstado());
        view.getBotaoAtualizarLista().addActionListener(e -> recarregar());
        // Ao selecionar um pedido, a caixa de estado mostra o estado atual dele
        view.getTabela().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && view.getLinhaSelecionada() >= 0) {
                view.setEstadoEscolhido(model.getPedido(view.getLinhaSelecionada()).getEstado());
            }
        });
        view.setVisible(true);
    }

    private void alterarEstado() {
        int linha = view.getLinhaSelecionada();
        if (linha < 0) {
            view.mostrarErro("Selecione um pedido na lista.");
            return;
        }
        Pedido pedido = model.getPedido(linha);
        EstadoPedido novo = view.getEstadoEscolhido();
        if (novo == pedido.getEstado()) {
            view.mostrarErro("O pedido " + pedido.getId() + " já está \"" + novo + "\".");
            return;
        }
        EstadoPedido anterior = pedido.getEstado();
        try {
            pedido.setEstado(novo);
            dao.updateEstado(pedido);
            view.mostrarMensagem("Pedido " + pedido.getId() + ": " + anterior + " → " + novo + ".");
        } catch (Exception e) {
            pedido.setEstado(anterior);
            view.mostrarErro(TratadorErros.mensagem(e));
        }
        recarregar();
        // Mantém o mesmo pedido selecionado depois de recarregar a lista
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getPedido(i).getId() == pedido.getId()) {
                view.selecionarLinha(i);
            }
        }
    }

    private void recarregar() {
        try {
            model.setPedidos(dao.getAll());
        } catch (Exception e) {
            view.mostrarErro(TratadorErros.mensagem(e));
        }
    }
}
