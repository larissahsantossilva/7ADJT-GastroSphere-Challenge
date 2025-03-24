-- Tabela de Menus
CREATE TABLE gastrosphere.menus (
  id UUID PRIMARY KEY, -- Identificador único do menu
  restaurant_id UUID NOT NULL REFERENCES gastrosphere.restaurants(id) ON DELETE CASCADE, -- Relacionamento com restaurante
  name VARCHAR(255) NOT NULL, -- Nome do menu
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Data de criação do registro
  last_modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP -- Data da última alteração
);

-- Tabela de Itens do Menu
CREATE TABLE gastrosphere.menu_items (
  id UUID PRIMARY KEY, -- Identificador único do item do menu
  menu_id UUID NOT NULL REFERENCES gastrosphere.menus(id) ON DELETE CASCADE, -- Relacionamento com menu
  description TEXT NOT NULL, -- Descrição do item
  price DECIMAL(10,2) NOT NULL, -- Preço do item
  image VARCHAR(255), -- Caminho da imagem do item
  is_available BOOLEAN DEFAULT TRUE, -- Disponibilidade do item
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Data de criação do registro
  last_modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP -- Data da última alteração
);

-- Inserindo menus de exemplo
INSERT INTO gastrosphere.menus (id, restaurant_id, name, created_at, last_modified_at)
VALUES 
  ('00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000003', 'Menu Italiano', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000003', 'Menu Sobremesas', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserindo itens do menu de exemplo
INSERT INTO gastrosphere.menu_items (id, menu_id, description, price, is_available, image, created_at, last_modified_at)
VALUES 
  ('00000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000004', 'Lasanha à Bolonhesa', 45.90, TRUE, 'lasanha.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('00000000-0000-0000-0000-000000000007', '00000000-0000-0000-0000-000000000004', 'Pizza Margherita', 39.90, TRUE, 'pizza.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('00000000-0000-0000-0000-000000000008', '00000000-0000-0000-0000-000000000005', 'Tiramisu', 25.00, TRUE, 'tiramisu.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('00000000-0000-0000-0000-000000000009', '00000000-0000-0000-0000-000000000005', 'Gelato', 20.00, TRUE, 'gelato.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);