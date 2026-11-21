package br.ufpr.pizzaria.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

/** Testes das regras do Model: formas (herança e polimorfismo), preços e validações. */
public class ModelTest {

    private static final double DELTA = 0.0001;

    private final TipoPizza simples = new TipoPizza(1, "Simples", 0.10);
    private final TipoPizza premium = new TipoPizza(3, "Premium", 0.20);
    private final Sabor mussarela = new Sabor(1, "Mussarela", simples);
    private final Sabor camarao = new Sabor(2, "Camarão", premium);

    // ---------- Formas: área (polimorfismo) ----------

    @Test
    public void areaCalculadaPorCadaFormaPeloTipoDaSuperclasse() {
        Forma[] formas = {new Circulo(10), new Quadrado(20), new Triangulo(30)};
        assertEquals(Math.PI * 100, formas[0].calcularArea(), DELTA);
        assertEquals(400, formas[1].calcularArea(), DELTA);
        assertEquals(Math.sqrt(3) / 4 * 900, formas[2].calcularArea(), DELTA);
    }

    @Test
    public void limitesDasDimensoesSaoValidados() {
        assertThrows(IllegalArgumentException.class, () -> new Circulo(6.99));
        assertThrows(IllegalArgumentException.class, () -> new Circulo(23.01));
        assertThrows(IllegalArgumentException.class, () -> new Quadrado(9));
        assertThrows(IllegalArgumentException.class, () -> new Quadrado(41));
        assertThrows(IllegalArgumentException.class, () -> new Triangulo(19));
        assertThrows(IllegalArgumentException.class, () -> new Triangulo(61));
        // Os próprios limites são aceitos
        new Circulo(7); new Circulo(23); new Quadrado(10); new Quadrado(40); new Triangulo(20); new Triangulo(60);
    }

    @Test
    public void mensagemDoLimiteExplicaOErro() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Quadrado(50));
        assertTrue(e.getMessage().startsWith("O lado do quadrado deve ter entre 10 e 40 cm"));
    }

    // ---------- Formas: criação pela área ----------

    @Test
    public void criarPelaAreaCalculaADimensaoDeCadaForma() {
        assertEquals(20, TipoForma.QUADRADO.criarPorArea(400).getDimensao(), DELTA);
        assertEquals(Math.sqrt(1000 / Math.PI), TipoForma.CIRCULO.criarPorArea(1000).getDimensao(), DELTA);
        Forma triangulo = TipoForma.TRIANGULO.criarPorArea(1000);
        assertEquals(1000, triangulo.calcularArea(), DELTA);   // ida e volta bate
    }

    @Test
    public void areaForaDe100a1600EhRecusada() {
        assertThrows(IllegalArgumentException.class, () -> TipoForma.QUADRADO.criarPorArea(99));
        assertThrows(IllegalArgumentException.class, () -> TipoForma.QUADRADO.criarPorArea(1601));
    }

    @Test
    public void areaValidaMasQueGeraDimensaoForaDoLimiteEhRecusada() {
        // 100 cm² num triângulo dá lado de ~15,2 cm (mínimo 20)
        assertThrows(IllegalArgumentException.class, () -> TipoForma.TRIANGULO.criarPorArea(100));
    }

    // ---------- Pizza: preço ----------

    @Test
    public void precoComUmSaborEhAreaVezesPrecoDoTipo() {
        Pizza pizza = new Pizza(new Quadrado(20), List.of(mussarela));
        assertEquals(400 * 0.10, pizza.getPreco(), DELTA);
    }

    @Test
    public void precoComDoisTiposUsaAMediaDosPrecos() {
        Pizza pizza = new Pizza(new Quadrado(20), List.of(mussarela, camarao));
        assertEquals(400 * (0.10 + 0.20) / 2, pizza.getPreco(), DELTA);
    }

    @Test
    public void saboresInvalidosSaoRecusados() {
        assertThrows(IllegalArgumentException.class, () -> new Pizza(new Quadrado(20), List.of()));
        assertThrows(IllegalArgumentException.class, () -> new Pizza(new Quadrado(20), List.of(mussarela, mussarela)));
        assertThrows(IllegalArgumentException.class,
                () -> new Pizza(new Quadrado(20), List.of(mussarela, camarao, new Sabor(9, "Atum", simples))));
    }

    // ---------- Pedido ----------

    @Test
    public void totalDoPedidoSomaAsPizzas() {
        Pedido pedido = new Pedido(new Cliente("Ana", "Souza", "41999990000"));
        pedido.adicionarPizza(new Pizza(new Quadrado(20), List.of(mussarela)));   // 40,00
        pedido.adicionarPizza(new Pizza(new Quadrado(10), List.of(camarao)));     // 20,00
        assertEquals(60.0, pedido.getPrecoTotal(), DELTA);
        pedido.removerPizza(0);
        assertEquals(20.0, pedido.getPrecoTotal(), DELTA);
    }

    @Test
    public void soPedidoAbertoPodeSerAlterado() {
        Pedido pedido = new Pedido(new Cliente("Ana", "Souza", "41999990000"));
        assertTrue(pedido.podeSerAlterado());
        pedido.setEstado(EstadoPedido.A_CAMINHO);
        assertFalse(pedido.podeSerAlterado());
    }

    @Test
    public void estadoDoPedidoSoAvanca() {
        Pedido pedido = new Pedido(new Cliente("Ana", "Souza", "41999990000"));
        assertTrue(pedido.podeMudarPara(EstadoPedido.A_CAMINHO));
        assertTrue(pedido.podeMudarPara(EstadoPedido.ENTREGUE));   // pode pular etapa
        assertFalse(pedido.podeMudarPara(EstadoPedido.ABERTO));    // já está aberto
        pedido.setEstado(EstadoPedido.ENTREGUE);
        assertFalse(pedido.podeMudarPara(EstadoPedido.A_CAMINHO));
        assertFalse(pedido.podeMudarPara(EstadoPedido.ABERTO));
    }

    // ---------- Cliente: validações ----------

    @Test
    public void clienteValidaOsCampos() {
        assertThrows(IllegalArgumentException.class, () -> new Cliente("", "Souza", "41999990000"));
        assertThrows(IllegalArgumentException.class, () -> new Cliente("Ana", "  ", "41999990000"));
        assertThrows(IllegalArgumentException.class, () -> new Cliente("Ana3", "Souza", "41999990000"));
        assertThrows(IllegalArgumentException.class, () -> new Cliente("Ana", "Souza", "(41) 99999-0000"));
        assertThrows(IllegalArgumentException.class, () -> new Cliente("Ana", "Souza", "123"));
        new Cliente("Ana Maria", "D'Ávila-Souza", "4133334444");   // válido
    }

    @Test
    public void precoDoTipoDeveSerPositivo() {
        assertThrows(IllegalArgumentException.class, () -> simples.setPrecoCm2(0));
        assertThrows(IllegalArgumentException.class, () -> simples.setPrecoCm2(-1));
    }

    @Test
    public void precoDoTipoTemLimitesEQuatroCasas() {
        // 0,00001 viraria 0,0000 no banco (DECIMAL(10,4)): é recusado
        assertThrows(IllegalArgumentException.class, () -> simples.setPrecoCm2(0.00001));
        assertThrows(IllegalArgumentException.class, () -> simples.setPrecoCm2(10.01));
        assertThrows(IllegalArgumentException.class, () -> simples.setPrecoCm2(Double.NaN));
        simples.setPrecoCm2(0.00026);   // arredonda para 4 casas
        assertEquals(0.0003, simples.getPrecoCm2(), 0.0000001);
        simples.setPrecoCm2(10);        // o próprio limite é aceito
    }

    @Test
    public void dimensaoNaNEhRecusada() {
        assertThrows(IllegalArgumentException.class, () -> new Circulo(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> TipoForma.QUADRADO.criarPorArea(Double.NaN));
    }

    @Test
    public void areaForaDosLimitesDaFormaInformaAFaixaValida() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> TipoForma.CIRCULO.criarPorArea(100));
        assertTrue(e.getMessage().startsWith("Para o círculo, a área deve ficar entre"));
        // Os extremos da faixa informada são aceitos (triângulo: 173,21 a 1558,84 cm²)
        TipoForma.TRIANGULO.criarPorArea(173.21);
        TipoForma.TRIANGULO.criarPorArea(1558.84);
        assertThrows(IllegalArgumentException.class, () -> TipoForma.TRIANGULO.criarPorArea(1558.85));
        TipoForma.CIRCULO.criarPorArea(153.94);
        TipoForma.CIRCULO.criarPorArea(1600);
    }
}
