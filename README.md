# 🚀 REST API com Spring Boot e Java

Este projeto é uma implementação de uma API RESTful completa, desenvolvida usando o framework **Spring Boot** e a linguagem **Java**. Ele serve como um repositório de aprendizado e prática dos conceitos essenciais do desenvolvimento backend moderno, abrangendo desde a configuração inicial até a persistência de dados e a implementação de padrões de arquitetura como HATEOAS.

O projeto é baseado nos guias de desenvolvimento e melhores práticas promovidos pela **Erudio Treinamentos**.

## 🌟 Destaques do Projeto

  * **RESTful CRUD Completo:** Implementação das quatro operações básicas (Criação, Leitura, Atualização e Exclusão) para recursos principais (ex: `Person`, `Book`, etc.).
  * **Spring Boot:** Utilização do Spring Boot para configuração rápida e execução autônoma da aplicação.
  * **Spring Data JPA:** Persistência de dados simplificada, utilizando Hibernate como provedor JPA.
  * **Content Negotiation:** Suporte para diferentes formatos de mídia (JSON, XML e/ou YAML).
  * **HATEOAS:** Implementação do princípio HATEOAS (Hypermedia as an Engine of Application State) para adicionar links de navegação às respostas da API.
  * **Tratamento de Exceções:** Implementação de um manipulador global de exceções para retornar códigos de *status* HTTP claros e mensagens de erro descritivas.
  * **Segurança (Opcional/A Ser Adicionada):** Base para implementação de segurança com Spring Security (autenticação JWT/OAuth2).
  * **Documentação (Opcional/A Ser Adicionada):** Integração com Swagger/OpenAPI para documentação interativa da API.

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão (Exemplo) | Descrição |
| :--- | :--- | :--- |
| **Java** | 17+ | Linguagem de programação principal. |
| **Spring Boot** | 3.x.x | Framework para simplificar o desenvolvimento de aplicações Spring. |
| **Maven** | 3.x.x | Gerenciador de dependências e construção do projeto. |
| **Spring Data JPA** | 3.x.x | Módulo para acesso a dados. |
| **HATEOAS** | 2.x.x | Implementação de hipermídia nas APIs. |
| **MySQL / PostgreSQL** | (Configurável) | Banco de dados relacional. |
| **Lombok** | 1.18.x | Biblioteca para reduzir código boilerplate (Getters, Setters, etc.). |

## ⚙️ Pré-requisitos

Antes de executar o projeto, você precisa ter instalado:

  * **Java Development Kit (JDK) 17 ou superior**
  * **Apache Maven 3.x.x**
  * **Um ambiente de desenvolvimento (IDE):** IntelliJ IDEA, Eclipse ou VS Code
  * **Um banco de dados** (Ex: MySQL)

## 🚀 Como Executar o Projeto

### 1\. Clonar o Repositório

```bash
git clone https://github.com/JzBreno/rest-spring-boot-java-erudio.git
cd rest-spring-boot-java-erudio
```

### 2\. Configuração do Banco de Dados

1.  Crie um novo banco de dados (Ex: `erudio_rest_db`).

2.  Edite o arquivo de propriedades da aplicação (`src/main/resources/application.properties` ou `application.yml`) e configure as credenciais do seu banco de dados:

    ```properties
    # Exemplo para MySQL
    spring.datasource.url=jdbc:mysql://localhost:3306/erudio_rest_db?useSSL=false&serverTimezone=UTC
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    spring.jpa.hibernate.ddl-auto=update
    ```

### 3\. Build e Execução

Use o Maven para compilar e iniciar a aplicação:

```bash
# Compila o projeto (cria o arquivo .jar)
mvn clean install

# Executa o projeto
mvn spring-boot:run
```

A API estará rodando em `http://localhost:8080` (porta padrão do Spring Boot).

## 🗺️ Endpoints Principais

A seguir, estão os principais *endpoints* disponíveis nesta API (substitua `[recurso]` pelo nome do recurso, ex: `person`):

| Método HTTP | Endpoint (Exemplo) | Descrição |
| :--- | :--- | :--- |
| `GET` | `/api/[recurso]/v1` | Retorna a lista completa de recursos. |
| `GET` | `/api/[recurso]/v1/{id}` | Retorna um recurso específico pelo ID. |
| `POST` | `/api/[recurso]/v1` | Cria um novo recurso. |
| `PUT` | `/api/[recurso]/v1/{id}` | Atualiza um recurso existente. |
| `DELETE` | `/api/[recurso]/v1/{id}` | Exclui um recurso específico. |

**Exemplo de uso com cURL (para um recurso `person`):**

```bash
# Criar um novo registro (POST)
curl -X POST http://localhost:8080/api/person/v1 \
-H "Content-Type: application/json" \
-d '{"firstName": "Breno", "lastName": "Souza", "address": "São Paulo", "gender": "Male"}'

# Buscar todos os registros (GET)
curl -X GET http://localhost:8080/api/person/v1
```

## 📝 Licença

Este projeto está sob a licença **MIT**. Veja o arquivo `LICENSE` para mais detalhes.

-----

**Desenvolvido por:** [Seu Nome / JzBreno]
