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

/**
 * Ponto de partida da Pizzaria 2.0 (como o Main do exemplo Contato MVC):
 * cria a tela principal e os Controllers, que ligam cada botão à sua tela.
 */
public class Pizzaria {

    public static void main(String[] args) {
        // As telas Swing devem ser criadas na thread de eventos do Swing (Event Dispatch Thread).
        // invokeLater recebe um Runnable (aula de Threads) e executa o run() nessa thread.
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

    /** Avisa logo na abertura se o banco de dados não estiver acessível. */
    private static void verificarBanco(TelaPrincipal principal) {
        try (Connection con = ConnectionFactory.getConnection()) {
            // conexão ok
        } catch (Exception e) {
            // SQLException: a ConnectionFactory já explica o que verificar; IOException: db.properties não encontrado
            principal.mostrarErro(e.getMessage());
        }
    }
}
