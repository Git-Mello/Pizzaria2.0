-- =====================================================================
-- Pizzaria 2.0 - criação do banco de dados (MySQL)
-- ATENÇÃO: recria as tabelas do zero (apaga os dados existentes).
-- =====================================================================

CREATE DATABASE IF NOT EXISTS pizzaria;
USE pizzaria;

DROP TABLE IF EXISTS pizza_sabor;
DROP TABLE IF EXISTS pizza;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS sabor;
DROP TABLE IF EXISTS tipo_pizza;
DROP TABLE IF EXISTS cliente;

-- Clientes (enunciado, item 1). O telefone é único: é por ele que se busca o cliente no pedido.
CREATE TABLE cliente (
    id        INT AUTO_INCREMENT,
    nome      VARCHAR(45) NOT NULL,
    sobrenome VARCHAR(45) NOT NULL,
    telefone  VARCHAR(11) NOT NULL,
    CONSTRAINT pk_cliente PRIMARY KEY (id),
    CONSTRAINT uk_cliente_telefone UNIQUE (telefone)
);

-- Tipos de pizza e o preço do cm² de cada um (enunciado, item 3)
CREATE TABLE tipo_pizza (
    id        INT AUTO_INCREMENT,
    nome      VARCHAR(20) NOT NULL,
    preco_cm2 DECIMAL(10,4) NOT NULL,
    CONSTRAINT pk_tipo_pizza PRIMARY KEY (id),
    CONSTRAINT uk_tipo_pizza_nome UNIQUE (nome)
);

-- Sabores, cada um relacionado a um tipo (enunciado, item 4)
CREATE TABLE sabor (
    id      INT AUTO_INCREMENT,
    nome    VARCHAR(45) NOT NULL,
    id_tipo INT NOT NULL,
    CONSTRAINT pk_sabor PRIMARY KEY (id),
    CONSTRAINT uk_sabor_nome UNIQUE (nome),
    CONSTRAINT fk_sabor_tipo FOREIGN KEY (id_tipo) REFERENCES tipo_pizza(id)
);

-- Pedidos. Excluir o cliente exclui os pedidos dele (ON DELETE CASCADE, enunciado item 1.c)
CREATE TABLE pedido (
    id          INT AUTO_INCREMENT,
    id_cliente  INT NOT NULL,
    estado      VARCHAR(10) NOT NULL DEFAULT 'ABERTO',   -- ABERTO, A_CAMINHO ou ENTREGUE
    preco_total DECIMAL(10,2) NOT NULL DEFAULT 0,
    CONSTRAINT pk_pedido PRIMARY KEY (id),
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id) ON DELETE CASCADE
);

-- Pizzas de cada pedido. Herança Forma mapeada como "uma única tabela para toda a hierarquia":
-- a coluna forma diz qual subclasse é (CIRCULO, QUADRADO ou TRIANGULO).
CREATE TABLE pizza (
    id        INT AUTO_INCREMENT,
    id_pedido INT NOT NULL,
    forma     VARCHAR(10) NOT NULL,
    dimensao  DOUBLE NOT NULL,             -- raio (círculo) ou lado (quadrado e triângulo), em cm
    area      DOUBLE NOT NULL,             -- cm²
    preco     DECIMAL(10,2) NOT NULL,
    CONSTRAINT pk_pizza PRIMARY KEY (id),
    CONSTRAINT fk_pizza_pedido FOREIGN KEY (id_pedido) REFERENCES pedido(id) ON DELETE CASCADE
);

-- Sabores de cada pizza (1 ou 2). Um sabor usado em pizza não pode ser excluído (RESTRICT).
CREATE TABLE pizza_sabor (
    id_pizza INT NOT NULL,
    id_sabor INT NOT NULL,
    CONSTRAINT pk_pizza_sabor PRIMARY KEY (id_pizza, id_sabor),
    CONSTRAINT fk_pizza_sabor_pizza FOREIGN KEY (id_pizza) REFERENCES pizza(id) ON DELETE CASCADE,
    CONSTRAINT fk_pizza_sabor_sabor FOREIGN KEY (id_sabor) REFERENCES sabor(id) ON DELETE RESTRICT
);

-- Dados iniciais: os três tipos (preço em R$ por cm²) e alguns sabores
INSERT INTO tipo_pizza (nome, preco_cm2) VALUES ('Simples', 0.05), ('Especial', 0.07), ('Premium', 0.10);

INSERT INTO sabor (nome, id_tipo) VALUES
    ('Mussarela', 1), ('Calabresa', 1), ('Marguerita', 1),
    ('Frango com Catupiry', 2), ('Portuguesa', 2), ('Quatro Queijos', 2),
    ('Camarão', 3), ('Filé Mignon', 3);
