package br.ufpr.pizzaria;

import java.sql.Connection;

import javax.swing.SwingUtilities;

import br.ufpr.pizzaria.controller.ClienteController;
import br.ufpr.pizzaria.controller.ControlePedidosController;
import br.ufpr.pizzaria.controller.PedidoController;
import br.ufpr.pizzaria.controller.PrecoController;
import br.ufpr.pizzaria.controller.SaborController;
import br.ufpr.pizzaria.model.dao.ConnectionFactory;
import br.ufpr.pizzaria.view.TelaPrincipal;

public class Pizzaria {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TelaPrincipal principal = new TelaPrincipal();
                new PedidoController(principal);
                new ControlePedidosController(principal);
                new ClienteController(principal);
                new SaborController(principal);
                new PrecoController(principal);
                principal.setVisible(true);

                verificarBanco(principal);
            }
        });
    }

    private static void verificarBanco(TelaPrincipal principal) {
        try (Connection con = ConnectionFactory.getConnection()) {
        } catch (Exception e) {
            principal.mostrarErro(e.getMessage());
        }
    }
}
