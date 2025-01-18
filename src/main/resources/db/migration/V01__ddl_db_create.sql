-- Criação do esquema principal
CREATE SCHEMA IF NOT EXISTS gastrosphere;

-- Extensão para UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabela de endereços
CREATE TABLE gastrosphere.addresses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(), -- Identificador único do endereço
    country VARCHAR(100) NOT NULL, -- Nome do país
    state VARCHAR(100), -- Nome do estado
    city VARCHAR(100), -- Nome da cidade
    zip_code VARCHAR(20) NOT NULL, -- Código do CEP
    street VARCHAR(255) NOT NULL -- Nome da rua
);

-- Tabela de usuários
CREATE TABLE gastrosphere.users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(), -- Identificador único do usuário
    name VARCHAR(255) NOT NULL, -- Nome do usuário
    email VARCHAR(255) UNIQUE NOT NULL, -- Email único para login e contato
    login VARCHAR(100) UNIQUE NOT NULL, -- Login único para acesso
    password VARCHAR(255) NOT NULL, -- Senha do usuário (hash)
    user_type VARCHAR(50) NOT NULL CHECK (user_type IN ('Owner', 'Customer')), -- Tipo de usuário: Owner ou Customer
    document VARCHAR(20) UNIQUE NOT NULL, -- Documento do usuário
    address_id UUID NOT NULL REFERENCES gastrosphere.addresses(id) ON DELETE SET NULL, -- Relacionamento com endereço
    address_number VARCHAR(10) NOT NULL, -- Número do endereço
    address_complement VARCHAR(255), -- Complemento do endereço
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Data de criação do registro
    last_modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP -- Data da última alteração
);

-- Índices para otimização
CREATE INDEX idx_users_email ON gastrosphere.users(email);
CREATE INDEX idx_users_type ON gastrosphere.users(user_type);
CREATE INDEX idx_addresses_zip_code ON gastrosphere.addresses(zip_code);