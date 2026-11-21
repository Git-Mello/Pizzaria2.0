# CLAUDE.md — Pizzaria 2.0

Contexto para o Claude Code trabalhar neste projeto. Leia antes de qualquer mudança.

## Quem é o usuário e como trabalhar com ele

- Marcos Mello, aluno de LPOO II (UFPR/SEPT). Chame-o de **Mellinho**. Responda em **português**.
- Trabalho **individual** (se alguém entrar na equipe, ele avisa; aí os nomes vão no README e nos diagramas).
- **Faça só o que foi pedido pelo professor/enunciado** — nada além. Na dúvida, pergunte.
- **Deixe comentários** no código (em português) para ele se localizar: ele defende o código individualmente.
- Antes de codar algo grande, resuma o que vai fazer e aponte o que for ambíguo para ele decidir.
- Ambiente dele: **Windows**, **VS Code** (com Extension Pack for Java), **MySQL 8** com usuário/senha `root`/`root`.
  Ao digitar números decimais nos programas, ele usa vírgula.

## O trabalho (enunciado "Trabalho - Disciplina - 2026SEM2", valor: 100% da 4ª nota)

Refatorar o Pizzaria do LPOO I usando **JDBC + DAO** (MySQL) e **MVC**, em **Java Swing**.
Pizza vendida por cm², formas círculo/quadrado/triângulo, sabores Simples/Especial/Premium.

Requisitos:
1. Tela de clientes (nome, sobrenome, telefone): listar em JTable, atualizar, excluir (excluindo os pedidos dele),
   filtrar por sobrenome, parte do sobrenome e/ou telefone.
2. Tela de pedido: cliente previamente cadastrado, escolhido por filtro de **telefone**; se já tiver pedido,
   os itens são listados e podem ser atualizados; cada item é uma pizza: forma, dimensão
   (quadrado: lado 10–40 cm; triângulo equilátero: lado 20–60 cm; círculo: raio 7–23 cm), até 2 sabores,
   ou a área em cm² (100–1600) com o sistema calculando e mostrando o raio/lado. Mostrar o preço total.
   Preço = área × preço do cm² do tipo; dois tipos → média aritmética. Armazenar pizzas, id e total do pedido.
   Mais de uma pizza por pedido; permitir alterar o pedido.
3. Tela para atualizar o preço do cm² de cada tipo.
4. Tela para cadastrar sabores e relacioná-los ao tipo.
5. Estados do pedido: aberto, a caminho, entregue; tela com pedidos de todos os clientes, permitindo alterar o estado.
6. Todas as validações nos campos, informando o usuário.
7. Tratamento qualificado das exceções.
- Obrigatório: **herança** (Forma, Quadrado, Triângulo, Círculo) e **polimorfismo no cálculo da área**.
- Entregas: diagrama de classes, projeto na IDE (Eclipse ou NetBeans — o projeto é Maven, abre em ambos),
  arquivo .jar executável. Avaliação: material, bugs encontrados na defesa e defesa individual do código.

Decisões combinadas com o Mellinho:
- Um cliente pode ter **vários pedidos**. Na tela de pedido, ao escolher o cliente, o pedido mais recente é aberto
  e os outros ficam na lista "Pedido" (com a opção "Novo pedido"). Só pedidos **Abertos** podem ser alterados.
- Excluir sabor que está em pizza de algum pedido é **bloqueado com mensagem**.
- O estado do pedido **só avança** (Aberto → A caminho → Entregue; pode pular etapa, não volta) — `Pedido.podeMudarPara`.

## Padrões do professor (seguir sempre)

- **ConnectionFactory**: construtor privado, `getConnection()` estático, lê `db.url`, `db.user`, `db.pwd`
  de `db.properties` (aqui em `src/main/resources`, lido pelo classpath para funcionar no .jar).
- **DAO completo** (aula de DAO): para cada entidade, interface (`ClienteDao`) + implementação MySQL
  (`ClienteDaoSql`, **Singleton** com `getInstance()`, comandos SQL em constantes `private final String`)
  + `DaoFactory` (métodos estáticos). Nomes dos métodos: `add`, `getAll`, `getById`, `update`, `delete`.
  Métodos dos DAOs lançam `SQLException, IOException`.
- **MVC do exemplo "Contato MVC"** (aula A06, slide 5): o **Controller guarda `view`, `model` (TableModel) e `dao`**,
  registra os listeners nos botões da tela e decide o que fazer; a **tela só mostra dados** e expõe campos/botões
  por getters (não conhece Controller nem DAO). O `main` cria a tela principal e os Controllers.
- Tabelas Swing sempre com **`AbstractTableModel`**.
- Herança no banco: "uma única tabela para toda a hierarquia" (coluna `pizza.forma` = CIRCULO/QUADRADO/TRIANGULO).
- Testes com **JUnit 5**.

## Estrutura

```
banco/pizzaria.sql                 cria o banco "pizzaria" do zero (tabelas + tipos + sabores iniciais)
src/main/resources/db.properties   url/usuário/senha do MySQL
src/main/java/br/ufpr/pizzaria/
  Pizzaria.java                    main: TelaPrincipal + Controllers; avisa se o banco não estiver acessível
  model/   Forma (abstrata), Circulo, Quadrado, Triangulo, TipoForma (enum-fábrica), Pizza, Pedido,
           EstadoPedido, Cliente, Sabor, TipoPizza
  model/dao/  ConnectionFactory, DaoFactory, *Dao (interfaces), *DaoSql (MySQL, Singleton)
  controller/ ClienteController, PedidoController, ControlePedidosController, SaborController,
              PrecoController, TratadorErros (exceção → mensagem), Entrada (texto → número, aceita vírgula)
  view/    TelaPrincipal, TelaBase (JDialog modal + mensagens), TelaClientes, TelaPedido, TelaControlePedidos,
           TelaSabores, TelaPrecos, *TableModel, Formato (moeda/números pt-BR), Formulario (layout)
src/test/java/   ModelTest (regras, roda sem banco), DaoTest (MySQL; pulado se o banco não estiver acessível)
docs/            diagramas de classes (Mermaid .mmd + .png)
```

Tabelas: `cliente` (telefone único), `tipo_pizza`, `sabor`, `pedido` (FK cliente ON DELETE CASCADE),
`pizza` (FK pedido CASCADE), `pizza_sabor` (FK sabor RESTRICT).
O preço de cada pizza é gravado no momento do pedido (mudar o preço do cm² não altera pedidos antigos).

## Comandos

```bash
mvn clean package                          # compila, roda os testes e gera target/Pizzaria-jar-with-dependencies.jar
java -jar target/Pizzaria-jar-with-dependencies.jar
mvn test -Dtest=ModelTest                  # só os testes do Model
```
Banco (PowerShell): `Get-Content banco\pizzaria.sql | & "<caminho>\mysql.exe" -u root -proot`
(ATENÇÃO: o script recria as tabelas e apaga os dados.)

## Git

- Repositório: https://github.com/Git-Mello/Pizzaria2.0 — branch `main`.
- O primeiro commit é o código do LPOO I (base); cada etapa da refatoração é um commit separado,
  com mensagem em português explicando o que mudou (isso ajuda na defesa).
