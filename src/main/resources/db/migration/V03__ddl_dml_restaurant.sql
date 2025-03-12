-- Tabela de tipos de restaurante
CREATE TABLE gastrosphere.restaurant_type (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(), -- Identificador único do tipo do restaurante
    name VARCHAR(100) NOT NULL, -- Tipo do restaurante
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Data de criação do registro
    last_modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP -- Data da última alteração
);

-- Inserir tipo de restaurante
INSERT INTO gastrosphere.restaurant_type (id, name, created_at, last_modified_at) VALUES
                                                                                    (uuid_generate_v4(), 'Japones', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                    (uuid_generate_v4(), 'Italiano', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                    (uuid_generate_v4(), 'Mexicano', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                    (uuid_generate_v4(), 'Nordestino', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                    (uuid_generate_v4(), 'Francês', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Tabela de restaurantes
CREATE TABLE gastrosphere.restaurants (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(), -- Identificador único do restaurante
    name VARCHAR(255) NOT NULL, -- Nome do restaurante
    address_id UUID NOT NULL REFERENCES gastrosphere.addresses(id) ON DELETE SET NULL, -- Relacionamento com endereço
    restaurant_type_id UUID NOT NULL REFERENCES gastrosphere.restaurant_type(id) ON DELETE SET NULL, -- Relacionamento com tipo do restaurante
    user_id UUID NOT NULL REFERENCES gastrosphere.users(id) ON DELETE SET NULL, -- Relacionamento com usuário dono do restaurante
    started_at TIME NOT NULL, -- Horário de abertura do restaurante
    finished_at TIME NOT NULL, -- Horário de fechamento do restaurante
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Data de criação do registro
    last_modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP -- Data da última alteração
);

CREATE INDEX idx_restaurants_name ON gastrosphere.restaurants(name);

-- Inserir restaurantes
INSERT INTO gastrosphere.restaurants (id, name, address_id, restaurant_type_id, user_id, started_at, finished_at, created_at, last_modified_at) VALUES
                                                                                                                              (uuid_generate_v4(), 'La Bella Pasta', (SELECT id FROM gastrosphere.addresses WHERE zip_code = '90001'), (SELECT id FROM gastrosphere.restaurant_type WHERE name = 'Italiano'),
                                                                                                                              (SELECT id FROM gastrosphere.users WHERE name = 'João Silva'), '10:00:00', '23:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                                                                                                                              (uuid_generate_v4(), 'Sushi do Mar', (SELECT id FROM gastrosphere.addresses WHERE zip_code = '20000-000'), (SELECT id FROM gastrosphere.restaurant_type WHERE name = 'Italiano'),
                                                                                                                              (SELECT id FROM gastrosphere.users WHERE name = 'João Silva'), '10:00:00', '20:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                                                                                                                              (uuid_generate_v4(), 'Churrasco Gaúcho', (SELECT id FROM gastrosphere.addresses WHERE zip_code = '01000-000'), (SELECT id FROM gastrosphere.restaurant_type WHERE name = 'Japones'),
                                                                                                                              (SELECT id FROM gastrosphere.users WHERE name = 'Ana Costa'), '12:00:00', '22:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                                                                                                                              (uuid_generate_v4(), 'Paris Café', (SELECT id FROM gastrosphere.addresses WHERE zip_code = 'M5H 2N2'), (SELECT id FROM gastrosphere.restaurant_type WHERE name = 'Nordestino'),
                                                                                                                              (SELECT id FROM gastrosphere.users WHERE name = 'Luiz Mendes'), '11:00:00', '23:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

                                                                                                                              (uuid_generate_v4(), 'Taco Loco', (SELECT id FROM gastrosphere.addresses WHERE zip_code = '01010'), (SELECT id FROM gastrosphere.restaurant_type WHERE name = 'Francês'),
                                                                                                                              (SELECT id FROM gastrosphere.users WHERE name = 'Luiz Mendes'), '12:00:00', '22:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);