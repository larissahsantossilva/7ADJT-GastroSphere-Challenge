# GastroSphere 🍽️

Bem-vindo ao repositório do projeto **GastroSphere**, desenvolvido como parte do trabalho de pós-graduação do curso de Arquitetura e Desenvolvimento Java da FIAP. Este projeto consiste em um sistema de gestão personalizado para um conjunto de restaurantes, focado em oferecer uma solução eficiente e compartilhada de gestão.

## Descrição do Projeto 📝

O GastroSphere foi criado para atender à necessidade de restaurantes da nossa região que buscavam uma alternativa econômica aos sistemas de gestão individuais. A solução permite que os clientes escolham restaurantes com base em seus cardápios, enquanto o backend oferece funcionalidades robustas de gerenciamento.

### Objetivo 🎯

Desenvolver um backend completo para gerenciamento de usuários, restaurantes, cardápio e endereços.

## Tecnologias Utilizadas ‍💻

- **Java 21**: Linguagem principal do projeto. ☕
- **Spring Boot**: Framework para construção do backend. 🌱
- **PostgreSQL**: Banco de dados relacional. 🗄️
- **Flyway**: Gerenciamento de migrações do banco de dados. 🦋
- **Docker**: Containerização da aplicação e dependências. 🐳
- **Docker Compose**: Orquestração dos serviços. 🎶
- **Maven**: Gerenciamento de dependências e build. 🛠️
- **Postman**: Testes manuais dos endpoints da API. 📬
- **Swagger**: Documentação da API. 📚

## Arquitetura do Sistema 🏛️

O projeto segue uma arquitetura em camadas, garantindo separação de responsabilidades:
1. **Controller**: Recebe requisições e retorna respostas.
2. **Service**: Centraliza as regras de negócio.
3. **Repository**: Interage com o banco de dados.
4. **Model**: Representa as entidades do sistema.

A comunicação é baseada em **HTTP/REST**, e o banco de dados é gerenciado com **Flyway** para versionamento das migrações.

## Endpoints da API 🌐

Abaixo estão descritos os endpoints disponíveis:

| Endpoint                        | Método | Descrição                                                 |
|---------------------------------|--------|-----------------------------------------------------------|
| `/api/v1/users`                 | GET    | Retorna uma lista paginada de usuários                    |
| `/api/v1/users/{id}`            | GET    | Retorna os detalhes de um usuário pelo ID                 |
| `/api/v1/users`                 | POST   | Cria um novo usuário                                      |
| `/api/v1/users/{id}`            | PUT    | Atualiza completamente os dados de um usuário             |
| `/api/v1/users/{id}`            | DELETE | Deleta um usuário específico pelo ID                      |
| `/api/v1/users/{id}/password`   | PUT    | Atualiza a senha de um usuário                            |
| `/api/v1/addresses`             | GET    | Retorna uma lista paginada de endereços                   |
| `/api/v1/addresses/{id}`        | GET    | Retorna os detalhes de um endereço pelo ID                |
| `/api/v1/addresses`             | POST   | Cria um novo endereço                                     |
| `/api/v1/addresses/{id}`        | PUT    | Atualiza completamente os dados de um endereço            |
| `/api/v1/addresses/{id}`        | DELETE | Deleta um endereço específico pelo ID                     |
| `/api/v1/user-types`            | GET    | Retorna uma lista paginada de tipos de usuário            |
| `/api/v1/user-types/{id}`       | GET    | Retorna os detalhes de um tipo de usuário pelo ID         |
| `/api/v1/user-types`            | POST   | Cria um novo tipo de usuário                              |
| `/api/v1/user-types/{id}`       | PUT    | Atualiza completamente os dados de um tipo de usuário     |
| `/api/v1/user-types/{id}`       | DELETE | Deleta um tipo de usuário específico pelo ID              |
| `/api/v1/restaurant-types`      | GET    | Retorna uma lista paginada de tipos de restaurante        |
| `/api/v1/restaurant-types/{id}` | GET    | Retorna os detalhes de um tipo de restaurante pelo ID     |
| `/api/v1/restaurant-types`      | POST   | Cria um novo tipo de restaurante                          |
| `/api/v1/restaurant-types/{id}` | PUT    | Atualiza completamente os dados de um tipo de restaurante |
| `/api/v1/restaurant-types/{id}` | DELETE | Deleta um tipo de restaurante específico pelo ID          |
| `/api/v1/restaurants`           | GET    | Retorna uma lista paginada de restaurantes                |
| `/api/v1/restaurants/{id}`      | GET    | Retorna os detalhes de um restaurante pelo ID             |
| `/api/v1/restaurants`           | POST   | Cria um novo restaurante                                  |
| `/api/v1/restaurants/{id}`      | PUT    | Atualiza completamente os dados de um restaurante         |
| `/api/v1/restaurants/{id}`      | DELETE | Deleta um restaurante específico pelo ID                  |
| `/api/v1/menus`                 | GET    | Retorna uma lista paginada de menus                       |
| `/api/v1/menus/{id}`            | GET    | Retorna os detalhes de um menu pelo ID                    |
| `/api/v1/menus`                 | POST   | Cria um novo menu                                         |
| `/api/v1/menus/{id}`            | PUT    | Atualiza completamente os dados de um menu                |
| `/api/v1/menus/{id}`            | DELETE | Deleta um menu específico pelo ID                         |
| `/api/v1/menu/items`            | GET    | Retorna uma lista paginada de itens de menu               |
| `/api/v1/menu/{id}/items`       | GET    | Retorna os detalhes de item de menu pelo ID               |
| `/api/v1/menu/items`            | POST   | Cria um novo item de menu                                 |
| `/api/v1/menu/{id}/items`       | PUT    | Atualiza completamente os dados de um item de menu        |
| `/api/v1/menu/{id}/items`       | DELETE | Deleta um item de menu específico pelo ID                 |

A documentação completa da API está disponível via Swagger em: `http://localhost:8080/swagger-ui/index.html`.

## Pré-requisitos ✅

- **Docker** e **Docker Compose** instalados.
- **Git** para clonar o repositório.

## Instruções de Execução Local 📋

1. Clone o repositório: ```git clone https://github.com/larissahsantossilva/7ADJT-GastroSphere-Challenge.git```
2. Navegue até o diretório raiz do projeto: ```cd 7ADJT-GastroSphere-Challenge```
3. Inicie os containers com Docker Compose: ```docker-compose up```
4. Verifique o status dos containers: ```docker-compose ps```
5. Para encerrar o ambiente: ```docker-compose down```

A aplicação estará disponível em `http://localhost:8080`. 🌍

## Testes 🧪

Uma coleção do Postman foi criada para testes manuais dos endpoints. Acesse o link abaixo para importá-la:
- [Coleção do Postman](https://speeding-station-415931.postman.co/workspace/GastroSphere~d52dd68e-4865-4739-92da-bc6c1068d122/collection/40855990-153f9978-0b30-4fea-becd-30a8b5ef5d67?action=share&creator=40855990)

## Qualidade do Código 🏅

O projeto segue os princípios **SOLID** e boas práticas do Spring, como:
- Separação de responsabilidades por camadas.
- Injeção de dependência via construtor.
- Uso de `ResponseEntity` para respostas HTTP consistentes.

## Equipe 👥

- Daniel Souza dos Santos (RM360340)
- Deyvid Kalbiak Martim (RM359078)
- Felipe Eduardo Gomes (RM360584)
- Guilherme Correia Martins (RM360403)
- Larissah Santos da Silva (RM360669)

