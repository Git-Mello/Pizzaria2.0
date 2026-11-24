package br.ufpr.pizzaria.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.ufpr.pizzaria.model.Circulo;
import br.ufpr.pizzaria.model.Cliente;
import br.ufpr.pizzaria.model.EstadoPedido;
import br.ufpr.pizzaria.model.Pedido;
import br.ufpr.pizzaria.model.Pizza;
import br.ufpr.pizzaria.model.Quadrado;
import br.ufpr.pizzaria.model.Sabor;
import br.ufpr.pizzaria.model.TipoPizza;

public class DaoTest {

    private final ClienteDao clienteDao = DaoFactory.getClienteDao();
    private final SaborDao saborDao = DaoFactory.getSaborDao();
    private final TipoPizzaDao tipoDao = DaoFactory.getTipoPizzaDao();
    private final PedidoDao pedidoDao = DaoFactory.getPedidoDao();

    private Cliente cliente;
    private Sabor saborTeste;

    @BeforeEach
    public void preparar() throws Exception {
        assumeTrue(bancoDisponivel(), "MySQL indisponível: testes de DAO pulados");
        limpar();
        cliente = new Cliente("Teste", "Junit", "00000000001");
        clienteDao.add(cliente);
        saborTeste = new Sabor("Sabor de Teste JUnit", tipoDao.getAll().get(0));
        saborDao.add(saborTeste);
    }

    @AfterEach
    public void limpar() throws Exception {
        if (!bancoDisponivel()) {
            return;
        }
        for (Cliente c : clienteDao.getByFiltro("", "0000000000")) {
            clienteDao.delete(c);
        }
        for (Sabor s : saborDao.getAll()) {
            if (s.getNome().endsWith("JUnit")) {
                saborDao.delete(s);
            }
        }
    }

    private static boolean bancoDisponivel() {
        try (Connection con = ConnectionFactory.getConnection()) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static List<Sabor> sabores(Sabor... lista) {
        List<Sabor> resultado = new ArrayList<>();
        for (Sabor sabor : lista) {
            resultado.add(sabor);
        }
        return resultado;
    }

    @Test
    public void clienteIncluidoRecebeIdELidoPeloId() throws Exception {
        assertTrue(cliente.getId() > 0);
        Cliente lido = clienteDao.getById(cliente.getId());
        assertEquals("Junit", lido.getSobrenome());
    }

    @Test
    public void clienteAtualizado() throws Exception {
        cliente.setSobrenome("Alterado");
        clienteDao.update(cliente);
        assertEquals("Alterado", clienteDao.getById(cliente.getId()).getSobrenome());
    }

    @Test
    public void filtroPorParteDoSobrenomeETelefone() throws Exception {
        clienteDao.add(new Cliente("Outro", "Junitino", "00000000002"));
        assertEquals(2, clienteDao.getByFiltro("unit", "0000000000").size());
        assertEquals(1, clienteDao.getByFiltro("unit", "00000000002").size());
        assertEquals(0, clienteDao.getByFiltro("NaoExiste", "0000000000").size());
    }

    @Test
    public void telefoneRepetidoEhRecusadoPeloBanco() throws Exception {
        try {
            clienteDao.add(new Cliente("Dup", "Junit", "00000000001"));
            fail("O banco deveria recusar o telefone repetido");
        } catch (SQLException e) {
        }
    }

    @Test
    public void precoDoTipoAtualizado() throws Exception {
        TipoPizza tipo = tipoDao.getAll().get(0);
        double original = tipo.getPrecoCm2();
        tipo.setPrecoCm2(original + 0.01);
        tipoDao.update(tipo);
        assertEquals(original + 0.01, tipoDao.getById(tipo.getId()).getPrecoCm2(), 0.00001);
        tipo.setPrecoCm2(original);
        tipoDao.update(tipo);
    }

    @Test
    public void precosDeTodosOsTiposAtualizadosJuntos() throws Exception {
        List<TipoPizza> tipos = tipoDao.getAll();
        List<TipoPizza> originais = tipoDao.getAll();
        for (TipoPizza tipo : tipos) {
            tipo.setPrecoCm2(tipo.getPrecoCm2() + 0.01);
        }
        tipoDao.update(tipos);
        for (int i = 0; i < tipos.size(); i++) {
            assertEquals(tipos.get(i).getPrecoCm2(), tipoDao.getById(tipos.get(i).getId()).getPrecoCm2(), 0.00001);
        }
        tipoDao.update(originais);
    }

    @Test
    public void saborComTipoIncluidoEUsoDetectado() throws Exception {
        assertNotNull(saborDao.getById(saborTeste.getId()));
        assertFalse(saborDao.isEmUso(saborTeste));
        Pedido pedido = new Pedido(cliente);
        pedido.adicionarPizza(new Pizza(new Quadrado(20), sabores(saborTeste)));
        pedidoDao.add(pedido);
        assertTrue(saborDao.isEmUso(saborTeste));
    }

    @Test
    public void pedidoGravadoComPizzasESaboresELidoDeVolta() throws Exception {
        List<Sabor> sabores = saborDao.getAll();
        Pedido pedido = new Pedido(cliente);
        pedido.adicionarPizza(new Pizza(new Quadrado(30), sabores(sabores.get(0), saborTeste)));
        pedido.adicionarPizza(new Pizza(new Circulo(10), sabores(saborTeste)));
        pedidoDao.add(pedido);

        Pedido lido = pedidoDao.getById(pedido.getId());
        assertEquals(2, lido.getPizzas().size());
        assertEquals(2, lido.getPizzas().get(0).getSabores().size());
        assertEquals("Quadrado", lido.getPizzas().get(0).getForma().getNome());
        assertEquals(pedido.getPrecoTotal(), lido.getPrecoTotal(), 0.01);
        assertEquals(1, pedidoDao.getByCliente(cliente).size());
    }

    @Test
    public void pedidoAlteradoEEstadoAtualizado() throws Exception {
        Pedido pedido = new Pedido(cliente);
        pedido.adicionarPizza(new Pizza(new Quadrado(20), sabores(saborTeste)));
        pedidoDao.add(pedido);

        pedido.adicionarPizza(new Pizza(new Circulo(15), sabores(saborTeste)));
        pedidoDao.update(pedido);
        assertEquals(2, pedidoDao.getById(pedido.getId()).getPizzas().size());

        pedido.setEstado(EstadoPedido.ENTREGUE);
        pedidoDao.updateEstado(pedido);
        assertEquals(EstadoPedido.ENTREGUE, pedidoDao.getById(pedido.getId()).getEstado());
    }

    @Test
    public void pedidoExcluido() throws Exception {
        Pedido pedido = new Pedido(cliente);
        pedido.adicionarPizza(new Pizza(new Quadrado(20), sabores(saborTeste)));
        pedidoDao.add(pedido);
        pedidoDao.delete(pedido);
        assertNull(pedidoDao.getById(pedido.getId()));
        assertFalse(saborDao.isEmUso(saborTeste));
    }

    @Test
    public void excluirClienteExcluiOsPedidos() throws Exception {
        Pedido pedido = new Pedido(cliente);
        pedido.adicionarPizza(new Pizza(new Quadrado(20), sabores(saborTeste)));
        pedidoDao.add(pedido);
        clienteDao.delete(cliente);
        assertNull(clienteDao.getById(cliente.getId()));
        assertNull(pedidoDao.getById(pedido.getId()));
    }
}
