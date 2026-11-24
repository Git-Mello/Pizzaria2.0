# Pizzaria 2.0

Sistema de pedidos de uma pizzaria que vende pizza por **centímetro quadrado**, em três formas (círculo, quadrado e triângulo) e três tipos de sabor (Simples, Especial e Premium).

Trabalho prático de **LPOO II (UFPR/SEPT)**: refatoração do [Pizzaria LPOO I](https://github.com/Git-Mello/Pizzaria-LPOO1) para usar **banco de dados MySQL com JDBC e DAO** e o padrão **MVC**, em Java Swing.

Autor: Marcos Mello

## Funcionalidades

| Tela | O que faz |
| --- | --- |
| Clientes | Incluir, listar (JTable), atualizar, excluir (com os pedidos do cliente) e filtrar por sobrenome e/ou telefone |
| Realizar / Alterar Pedido | Busca o cliente pelo telefone, lista os pedidos dele, monta pizzas por dimensão ou por área (mostra o raio/lado calculado), até 2 sabores, mostra o total e salva |
| Controle de Pedidos | Pedidos de todos os clientes e seus estados (Aberto, A caminho, Entregue); permite alterar o estado |
| Sabores | Cadastra sabores e relaciona cada um a um tipo; não exclui sabor usado em pedidos |
| Preço do cm² | Atualiza o preço do cm² de cada tipo |

**Regras principais**
- Limites: quadrado de 10 a 40 cm de lado, triângulo equilátero de 20 a 60 cm de lado, círculo de 7 a 23 cm de raio; pela área, de 100 a 1600 cm² (respeitando também os limites da forma: círculo de 153,94 a 1600 cm², triângulo de 173,21 a 1558,84 cm²).
- Preço da pizza = área × preço do cm² do tipo; com dois sabores de tipos diferentes, usa a **média** dos dois preços.
- Preço do cm² de cada tipo: de 0,0001 a 10,0000 (até 4 casas decimais).
- A área é calculada por **polimorfismo**: `Forma` é abstrata e `Circulo`, `Quadrado` e `Triangulo` implementam `calcularArea()`.
- Um cliente pode ter vários pedidos; só pedidos **abertos** podem ter as pizzas alteradas.
- O estado do pedido só avança: Aberto → A caminho → Entregue.

## Como executar

**Pré-requisitos:** Java 17 ou mais novo e MySQL 8.

1. **Crie o banco** executando o script [`banco/pizzaria.sql`](banco/pizzaria.sql) no MySQL (ele cria o banco `pizzaria`, as tabelas, os três tipos de pizza e alguns sabores). No PowerShell:
   ```powershell
   $mysql = (Get-ChildItem "C:\Program Files\MySQL" -Recurse -Filter mysql.exe | Select-Object -First 1).FullName
   Get-Content banco\pizzaria.sql | & $mysql -u root -proot
   ```
2. **Confira usuário e senha** em [`src/main/resources/db.properties`](src/main/resources/db.properties) (padrão: `root` / `root`).
3. **Rode** a classe `br.ufpr.pizzaria.Pizzaria` pela IDE (NetBeans, Eclipse, IntelliJ ou VS Code abrem o projeto Maven direto).

## Gerar o .jar executável

```bash
mvn clean package
```
Gera `target/Pizzaria-jar-with-dependencies.jar`, já com o driver do MySQL. Para executar:
```bash
java -jar target/Pizzaria-jar-with-dependencies.jar
```

Os testes dos DAOs (`DaoTest`) usam o banco `pizzaria`; se o MySQL não estiver disponível, eles são pulados e o `.jar` é gerado mesmo assim.

## Estrutura (MVC + DAO)

```
src/main/java/br/ufpr/pizzaria/
├── Pizzaria.java          ponto de partida: cria a tela principal e os Controllers
├── model/                 entidades e regras (Forma e subclasses, Pizza, Pedido, Cliente, Sabor, TipoPizza)
│   └── dao/               acesso ao banco: interfaces, implementações MySQL (Singleton), DaoFactory e ConnectionFactory
├── controller/            um Controller por tela: guarda a view, o TableModel e o DAO e trata os eventos
└── view/                  telas Swing e TableModels (AbstractTableModel)
```