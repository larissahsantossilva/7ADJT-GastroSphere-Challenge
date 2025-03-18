-- Tabela de Menus
CREATE TABLE gastrosphere.menus (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(), -- Identificador único do menu
    restaurant_id UUID NOT NULL REFERENCES gastrosphere.restaurants(id) ON DELETE CASCADE, -- Relacionamento com restaurante
    name VARCHAR(255) NOT NULL, -- Nome do menu
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Data de criação do registro
    last_modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP -- Data da última alteração
);

-- Índice para acelerar consultas por restaurante no menu
CREATE INDEX idx_menus_restaurant_id ON gastrosphere.menus(restaurant_id);
CREATE INDEX idx_menus_name ON gastrosphere.menus(name);

-- Tabela de Itens do Menu
CREATE TABLE gastrosphere.menu_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(), -- Identificador único do item do menu
    menu_id UUID NOT NULL REFERENCES gastrosphere.menus(id) ON DELETE CASCADE, -- Relacionamento com menu
    description TEXT NOT NULL, -- Descrição do item
    price DECIMAL(10,2) NOT NULL, -- Preço do item
    image VARCHAR(255), -- Caminho da imagem do item
    is_available BOOLEAN NOT NULL DEFAULT TRUE, -- Disponibilidade do item
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Data de criação do registro
    last_modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP -- Data da última alteração
);

-- Índices para melhorar consultas
CREATE INDEX idx_menu_items_menu_id ON gastrosphere.menu_items(menu_id);
CREATE INDEX idx_menu_items_is_available ON gastrosphere.menu_items(is_available);
CREATE INDEX idx_menu_items_price ON gastrosphere.menu_items(price);

-- Inserindo menus
INSERT INTO gastrosphere.menus (id, restaurant_id, name) VALUES
														    (uuid_generate_v4(), (SELECT id FROM gastrosphere.restaurants WHERE name = 'La Bella Pasta'), 'Menu Italiano'),
														    (uuid_generate_v4(), (SELECT id FROM gastrosphere.restaurants WHERE name = 'Sushi do Mar'), 'Menu Japonês'),
													        (uuid_generate_v4(), (SELECT id FROM gastrosphere.restaurants WHERE name = 'Taco Loco'), 'Menu Mexicano');

-- Inserindo itens nos menus
INSERT INTO gastrosphere.menu_items (id, menu_id, description, price, image, is_available) VALUES
																								(uuid_generate_v4(), (SELECT id FROM gastrosphere.menus WHERE name = 'Menu Italiano'), 'Lasanha à Bolonhesa', 45.90, 'lasanha.jpg', TRUE),
																								(uuid_generate_v4(), (SELECT id FROM gastrosphere.menus WHERE name = 'Menu Italiano'), 'Pizza Margherita', 39.90, 'pizza.jpg', TRUE),
																								(uuid_generate_v4(), (SELECT id FROM gastrosphere.menus WHERE name = 'Menu Japonês'), 'Sashimi de Salmão', 55.00, 'sashimi.jpg', TRUE),
																								(uuid_generate_v4(), (SELECT id FROM gastrosphere.menus WHERE name = 'Menu Mexicano'), 'Burrito de Carne', 29.90, 'burrito.jpg', TRUE),
																								(uuid_generate_v4(), (SELECT id FROM gastrosphere.menus WHERE name = 'Menu Mexicano'), 'Tacos de Frango', 25.00, 'tacos.jpg', TRUE);

