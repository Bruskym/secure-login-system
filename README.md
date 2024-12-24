# API de Estudo: Spring Security com OAuth2

Repositório para fins de estudo do **Spring Security** e **OAuth2**, aplicando autenticação e autorização para proteger uma aplicação web. É um exemplo prático para estudo, contendo endpoints para registro, login, gerenciamento de posts e controle de acesso baseado em roles.

---

## Funcionalidades

- **Autenticação JWT**: Usuários recebem um token JWT após login.
- **Controle de acesso baseado em roles**:
  - Roles disponíveis: `ADMIN`, `USER`.
  - Acesso restrito para ADMIN em rotas específicas.
- **Gestão de Posts**: Criar, visualizar e excluir posts.
  - Apenas criadores de posts ou ADMIN podem excluí-los.

---

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.x**
- **Spring Security com OAuth2**
- **JWT (JSON Web Tokens)**
- **JPA com Hibernate**
- **PostgreSQL**
- **Maven**

---

## Endpoints e Exemplos de Uso

### **1. Registro e Autenticação**

#### Registro de Usuário
- **POST** `/register`
- **Request:**
  ```json
  {
      "username": "user5",
      "password": "123"
  }
  ```
- **Response:**
  ```json
  {
      "userId": "9afaddbe-9f79-4ebc-b466-dceb31c2e835",
      "message": "Usuario salvo com sucesso!"
  }
  ```

#### Login
- **POST** `/login`
- **Request:**
  ```json
  {
      "username": "user5",
      "password": "123"
  }
  ```
- **Response:**
  ```json
  {
      "accessToken": "eyJhbGciOiJSUzI1NiJ9...",
      "expiresIn": 300
  }
  ```

### **2. Rotas Protegidas**

#### Visualizar Todos os Usuários (ADMIN apenas)
- **GET** `/user/listAll`
- **Headers:**
  ```
  Authorization: Bearer <token>
  ```
- **Response:**
  ```json
  [
      {
          "userId": "7063bff5-7a1c-4990-9616-3b2b2501fb11",
          "username": "user1",
          "roles": [
              {
                  "roleId": 2,
                  "roleName": "USER"
              }
          ]
      },
      {
          "userId": "b4e5d2a5-8a71-4b5b-8b53-bbe111f473e5",
          "username": "admin",
          "roles": [
              {
                  "roleId": 1,
                  "roleName": "ADMIN"
              }
          ]
      }
  ]
  ```

#### Criar um Post
- **POST** `/post/create`
- **Headers:**
  ```
  Authorization: Bearer <token>
  ```
- **Request:**
  ```json
  {
      "content": "postagem usuario adm"
  }
  ```
- **Response:**
  Apenas `HTTP 200 OK`.

#### Excluir um Post
- **DELETE** `/post/delete/{id}`
- **Headers:**
  ```
  Authorization: Bearer <token>
  ```
- **Response:**
  Apenas `HTTP 200 OK`.

#### Visualizar Posts (Paginado)
- **GET** `/post?page=0`
- **Headers:**
  ```
  Authorization: Bearer <token>
  ```
- **Response:**
  ```json
  [
      {
          "content": "postagem usuario adm",
          "username": "admin",
          "creationDate": "2024-12-24T22:22:03.467171Z"
      },
      {
          "content": "postagem usuario 4",
          "username": "user4",
          "creationDate": "2024-12-24T03:34:52.606866Z"
      }
  ]
  ```

---

## Como Rodar o Projeto

### Requisitos
- **Java 17+**
- **Maven**

### Passos
1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/spring-security-oauth2-study.git
```

2. Navegue para o diretório do projeto:
```bash
  cd spring-security-oauth2-study
```

3. Configuração do application.yaml 

Após clonar o repositório, copie o arquivo application-example.yml para application.yml e substitua os placeholders pelos valores reais:

```bash
cp src/main/resources/application-example.yml src/main/resources/application.yml
```

Configure os seguintes campos:

Banco de Dados: Substitua <DATABASE_HOST>, <DATABASE_PORT>, <DATABASE_NAME>, <DATABASE_USERNAME>, e <DATABASE_PASSWORD> com as informações do seu banco de dados.
JWT Keys: Certifique-se de apontar as chaves públicas e privadas para o local correto no campo jwt.public.key e jwt.private.key.

4. Variáveis de ambiente

Crie o arquivo .env na pasta raiz do projeto para o docker-compose conseguir usar as variáveis de ambiente e subir o container com o PostgreSQL

```bash
cp .env.example .env
```

5. Execute o projeto:
```bash
mvn spring-boot:run
```

6. Acesse:
   - **Endpoints da API:** [http://localhost:8080](http://localhost:8080)

---

## Melhorias Futuras
- Implementar testes automatizados.
- Documentar a API com Swagger.
- Adicionar suporte para roles dinâmicas (CRUD de roles).
- Refinar o tratamento de erros e validação.

---


