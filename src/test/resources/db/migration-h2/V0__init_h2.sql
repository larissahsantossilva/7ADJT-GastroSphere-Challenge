CREATE SCHEMA IF NOT EXISTS gastrosphere;

-- Tabela de endereços
CREATE TABLE gastrosphere.addresses (
  id UUID PRIMARY KEY,
  country VARCHAR(100) NOT NULL,
  state VARCHAR(100),
  city VARCHAR(100),
  zip_code VARCHAR(20) NOT NULL,
  street VARCHAR(255) NOT NULL,
  number VARCHAR(10) NOT NULL DEFAULT 'N/A',
  complement VARCHAR(255),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de tipos de usuário
CREATE TABLE gastrosphere.user_type (
  id UUID PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de usuários
CREATE TABLE gastrosphere.users (
  id UUID PRIMARY KEY,
  address_id UUID,
  name VARCHAR(100),
  email VARCHAR(100),
  login VARCHAR(100),
  password VARCHAR(100),
  document VARCHAR(20),
  user_type_id UUID,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de tipos de restaurante
CREATE TABLE gastrosphere.restaurant_type (
  id UUID PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de restaurantes
CREATE TABLE gastrosphere.restaurants (
  id UUID PRIMARY KEY,
  address_id UUID,
  name VARCHAR(100),
  user_id UUID,
  restaurant_type_id UUID,
  started_at TIMESTAMP,
  finished_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Inserções mínimas para testes

-- Endereço
INSERT INTO gastrosphere.addresses (id, country, zip_code, street, number, created_at, last_modified_at)
VALUES ('00000000-0000-0000-0000-000000000001', 'Brazil', '01000-000', 'Rua Teste', '123', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Tipo de usuário
INSERT INTO gastrosphere.user_type (id, name, created_at, last_modified_at)
VALUES ('00000000-0000-0000-0000-000000000010', 'Administrador', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Usuário
INSERT INTO gastrosphere.users (id, address_id, name, email, login, password, document, user_type_id, created_at, last_modified_at)
VALUES (
  '00000000-0000-0000-0000-000000000002',
  '00000000-0000-0000-0000-000000000001',
  'João',
  'joao@example.com',
  'joao123',
  'senha123',
  '12345678900',
  '00000000-0000-0000-0000-000000000010',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);

-- Tipo de restaurante
INSERT INTO gastrosphere.restaurant_type (id, name, created_at, last_modified_at)
VALUES (
  '00000000-0000-0000-0000-000000000020',
  'Italiana',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);

-- Restaurante
INSERT INTO gastrosphere.restaurants (id, name, address_id, user_id, restaurant_type_id, started_at, finished_at, created_at, last_modified_at)
VALUES (
  '00000000-0000-0000-0000-000000000003',
  'Spoleto',
  '00000000-0000-0000-0000-000000000001',
  '00000000-0000-0000-0000-000000000002',
  '00000000-0000-0000-0000-000000000020',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);
