-- Tabela de tipos de usuário
CREATE TABLE gastrosphere.user_type (
                                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(), -- Identificador único do tipo do usuário
                                        name VARCHAR(100) NOT NULL UNIQUE, -- Tipo do usuário
                                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Data de criação do registro
                                        last_modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP -- Data da última alteração
);

-- Inserir tipo de usuário
INSERT INTO gastrosphere.user_type (id, name, created_at, last_modified_at) VALUES
                                                                                (uuid_generate_v4(), 'Proprietário', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                (uuid_generate_v4(), 'Cliente', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Alteração da tabela users
ALTER TABLE gastrosphere.users DROP COLUMN user_type;
ALTER TABLE gastrosphere.users ADD COLUMN user_type_id UUID REFERENCES gastrosphere.user_type(id) ON DELETE SET NULL;

-- Atualizar os registros existentes com um tipo de usuário padrão
UPDATE gastrosphere.users
SET user_type_id = (SELECT id FROM gastrosphere.user_type WHERE name = 'Cliente' LIMIT 1)
WHERE user_type_id IS NULL;

-- Tornar a coluna user_type_id NOT NULL
ALTER TABLE gastrosphere.users ALTER COLUMN user_type_id SET NOT NULL;

-- Remover índice antigo
DROP INDEX IF EXISTS idx_users_type;