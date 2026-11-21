package br.ufpr.pizzaria.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/** Testes das regras do Model: formas (herança e polimorfismo), preços e validações. */
public class ModelTest {

    private static final double DELTA = 0.0001;

    private final TipoPizza simples = new TipoPizza(1, "Simples", 0.10);
    private final TipoPizza premium = new TipoPizza(3, "Premium", 0.20);
    private final Sabor mussarela = new Sabor(1, "Mussarela", simples);
    private final Sabor camarao = new Sabor(2, "Camarão", premium);

    // ---------- Auxiliares: verificam que a operação é recusada (lança IllegalArgumentException) ----------

    /** Monta a lista de sabores de uma pizza. */
    private static List<Sabor> sabores(Sabor... lista) {
        List<Sabor> resultado = new ArrayList<>();
        for (Sabor sabor : lista) {
            resultado.add(sabor);
        }
        return resultado;
    }

    /** Criar a forma com essa dimensão deve ser recusado. Devolve a mensagem do erro. */
    private static String formaRecusada(TipoForma tipo, double dimensao) {
        try {
            tipo.criar(dimensao);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        fail(tipo + " com dimensão " + dimensao + " deveria ser recusado");
        return null;
    }

    /** Criar a forma por essa área deve ser recusado. Devolve a mensagem do erro. */
    private static String areaRecusada(TipoForma tipo, double area) {
        try {
            tipo.criarPorArea(area);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        fail(tipo + " com área " + area + " deveria ser recusado");
        return null;
    }

    private static void pizzaRecusada(List<Sabor> sabores) {
        try {
            new Pizza(new Quadrado(20), sabores);
            fail("Pizza com sabores " + sabores + " deveria ser recusada");
        } catch (IllegalArgumentException e) {
            // esperado
        }
    }

    private static void clienteRecusado(String nome, String sobrenome, String telefone) {
        try {
            new Cliente(nome, sobrenome, telefone);
            fail("Cliente \"" + nome + "\", \"" + sobrenome + "\", \"" + telefone + "\" deveria ser recusado");
        } catch (IllegalArgumentException e) {
            // esperado
        }
    }

    private static void precoRecusado(TipoPizza tipo, double preco) {
        try {
            tipo.setPrecoCm2(preco);
            fail("Preço do cm² " + preco + " deveria ser recusado");
        } catch (IllegalArgumentException e) {
            // esperado
        }
    }

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
        formaRecusada(TipoForma.CIRCULO, 6.99);
        formaRecusada(TipoForma.CIRCULO, 23.01);
        formaRecusada(TipoForma.QUADRADO, 9);
        formaRecusada(TipoForma.QUADRADO, 41);
        formaRecusada(TipoForma.TRIANGULO, 19);
        formaRecusada(TipoForma.TRIANGULO, 61);
        // Os próprios limites são aceitos
        new Circulo(7); new Circulo(23); new Quadrado(10); new Quadrado(40); new Triangulo(20); new Triangulo(60);
    }

    @Test
    public void mensagemDoLimiteExplicaOErro() {
        assertEquals("O lado do quadrado deve ter entre 10 e 40 cm.", formaRecusada(TipoForma.QUADRADO, 50));
    }

    @Test
    public void dimensaoNaNEhRecusada() {
        formaRecusada(TipoForma.CIRCULO, Double.NaN);
        areaRecusada(TipoForma.QUADRADO, Double.NaN);
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
        areaRecusada(TipoForma.QUADRADO, 99);
        areaRecusada(TipoForma.QUADRADO, 1601);
    }

    @Test
    public void areaForaDosLimitesDaFormaInformaAFaixaValida() {
        // 100 cm² num círculo dá raio de ~5,64 cm (mínimo 7)
        assertTrue(areaRecusada(TipoForma.CIRCULO, 100).startsWith("Para o círculo, a área deve ficar entre"));
        // 100 cm² num triângulo dá lado de ~15,2 cm (mínimo 20)
        areaRecusada(TipoForma.TRIANGULO, 100);
        // Os extremos da faixa informada são aceitos (triângulo: 173,21 a 1558,84 cm²)
        TipoForma.TRIANGULO.criarPorArea(173.21);
        TipoForma.TRIANGULO.criarPorArea(1558.84);
        areaRecusada(TipoForma.TRIANGULO, 1558.85);
        TipoForma.CIRCULO.criarPorArea(153.94);
        TipoForma.CIRCULO.criarPorArea(1600);
    }

    // ---------- Pizza: preço ----------

    @Test
    public void precoComUmSaborEhAreaVezesPrecoDoTipo() {
        Pizza pizza = new Pizza(new Quadrado(20), sabores(mussarela));
        assertEquals(400 * 0.10, pizza.getPreco(), DELTA);
    }

    @Test
    public void precoComDoisTiposUsaAMediaDosPrecos() {
        Pizza pizza = new Pizza(new Quadrado(20), sabores(mussarela, camarao));
        assertEquals(400 * (0.10 + 0.20) / 2, pizza.getPreco(), DELTA);
    }

    @Test
    public void saboresInvalidosSaoRecusados() {
        pizzaRecusada(sabores());                                                     // nenhum sabor
        pizzaRecusada(sabores(mussarela, mussarela));                                 // sabores iguais
        pizzaRecusada(sabores(mussarela, camarao, new Sabor(9, "Atum", simples)));    // mais de 2
    }

    @Test
    public void descricaoDosSaboresSeparadaPorBarra() {
        assertEquals("Mussarela / Camarão", new Pizza(new Quadrado(20), sabores(mussarela, camarao)).getDescricaoSabores());
        assertEquals("Mussarela", new Pizza(new Quadrado(20), sabores(mussarela)).getDescricaoSabores());
    }

    // ---------- Pedido ----------

    @Test
    public void totalDoPedidoSomaAsPizzas() {
        Pedido pedido = new Pedido(new Cliente("Ana", "Souza", "41999990000"));
        pedido.adicionarPizza(new Pizza(new Quadrado(20), sabores(mussarela)));   // 40,00
        pedido.adicionarPizza(new Pizza(new Quadrado(10), sabores(camarao)));     // 20,00
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
        clienteRecusado("", "Souza", "41999990000");
        clienteRecusado("Ana", "  ", "41999990000");
        clienteRecusado("Ana3", "Souza", "41999990000");
        clienteRecusado("Ana", "Souza", "(41) 99999-0000");
        clienteRecusado("Ana", "Souza", "123");
        clienteRecusado("Ana", "Souza", "419999900001");   // 12 dígitos
        new Cliente("Ana Maria", "D'Ávila-Souza", "4133334444");   // válido (acento, apóstrofo e hífen)
    }

    // ---------- Tipo de pizza: preço do cm² ----------

    @Test
    public void precoDoTipoDeveSerPositivo() {
        precoRecusado(simples, 0);
        precoRecusado(simples, -1);
    }

    @Test
    public void precoDoTipoTemLimitesEQuatroCasas() {
        precoRecusado(simples, 0.00001);   // viraria 0,0000 no banco (DECIMAL(10,4))
        precoRecusado(simples, 10.01);
        precoRecusado(simples, Double.NaN);
        simples.setPrecoCm2(0.00026);      // arredonda para 4 casas
        assertEquals(0.0003, simples.getPrecoCm2(), 0.0000001);
        simples.setPrecoCm2(10);           // o próprio limite é aceito
    }
}
