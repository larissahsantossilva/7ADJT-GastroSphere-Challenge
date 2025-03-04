-- Tabela de restaurantes
CREATE TABLE gastrosphere.restaurants (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(), -- Identificador único do restaurante
    name VARCHAR(255) NOT NULL, -- Nome do restaurante
    address_id UUID NOT NULL REFERENCES gastrosphere.addresses(id) ON DELETE SET NULL, -- Relacionamento com endereço
    address_number VARCHAR(10) NOT NULL, -- Número do endereço
    address_complement VARCHAR(255), -- Complemento do endereço
    restaurant_type VARCHAR(100) NOT NULL, -- Tipo de cozinha
    opening_hours VARCHAR(100) DEFAULT 'Horário não informado', -- Horário de funcionamento
    owner_id UUID NOT NULL REFERENCES gastrosphere.users(id) ON DELETE SET NULL -- Relacionamento com usuário dono do restaurante
);

CREATE INDEX idx_restaurants_name ON gastrosphere.restaurants(name);

-- Inserir restaurantes
INSERT INTO gastrosphere.restaurants (id, name, address_id, address_number, address_complement, restaurant_type, opening_hours, owner_id) VALUES
                                                                                                                                      (uuid_generate_v4(), 'La Bella Pasta', (SELECT id FROM gastrosphere.addresses WHERE zip_code = '90001'), '12', NULL, 'Italiana', '10:00 - 22:00', (SELECT id FROM gastrosphere.users WHERE name = 'João Silva')),
                                                                                                                                      (uuid_generate_v4(), 'Sushi do Mar', (SELECT id FROM gastrosphere.addresses WHERE zip_code = '20000-000'), '452', 'Ao lado do shopping', 'Japonesa', '11:30 - 23:00', (SELECT id FROM gastrosphere.users WHERE name = 'João Silva')),
                                                                                                                                      (uuid_generate_v4(), 'Churrasco Gaúcho', (SELECT id FROM gastrosphere.addresses WHERE zip_code = '01000-000'), '101', 'Próximo ao estádio', 'Brasileira', '12:00 - 00:00', (SELECT id FROM gastrosphere.users WHERE name = 'Ana Costa')),
                                                                                                                                      (uuid_generate_v4(), 'Paris Café', (SELECT id FROM gastrosphere.addresses WHERE zip_code = 'M5H 2N2'), '5', NULL, 'Francesa', '08:00 - 20:00', (SELECT id FROM gastrosphere.users WHERE name = 'Luiz Mendes')),
                                                                                                                                      (uuid_generate_v4(), 'Taco Loco', (SELECT id FROM gastrosphere.addresses WHERE zip_code = '01010'), '77', NULL, 'Mexicana', '12:00 - 22:30', (SELECT id FROM gastrosphere.users WHERE name = 'Luiz Mendes'));