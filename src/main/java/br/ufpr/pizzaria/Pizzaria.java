package br.ufpr.pizzaria;

import java.sql.Connection;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

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
        SwingUtilities.invokeLater(() -> {
            try {
                // Aparência do sistema operacional (no Windows, botões e janelas do Windows)
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Se não conseguir, segue com a aparência padrão do Java
            }

            TelaPrincipal principal = new TelaPrincipal();
            new PedidoController(principal);
            new ControlePedidosController(principal);
            new ClienteController(principal);
            new SaborController(principal);
            new PrecoController(principal);
            principal.setVisible(true);

            verificarBanco(principal);
        });
    }

    /** Avisa logo na abertura se o banco de dados não estiver acessível. */
    private static void verificarBanco(TelaPrincipal principal) {
        try (Connection con = ConnectionFactory.getConnection()) {
            // conexão ok
        } catch (Exception e) {
            principal.mostrarErro("Não foi possível acessar o banco de dados \"pizzaria\".\n"
                    + "Verifique se o MySQL está ligado, se o script banco/pizzaria.sql foi executado\n"
                    + "e se usuário e senha em db.properties estão corretos.\n\nDetalhe: " + e.getMessage());
        }
    }
}
