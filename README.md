Repositório de estudo sobre Spring Security e OAuth2, implementando autenticação e autorização em uma aplicação web. Inclui exemplos práticos de endpoints para registro, login, gerenciamento de posts e controle de acesso baseado em roles.

## Funcionalidades

- **Autenticação JWT**: Usuários recebem um token JWT após login.
- **Controle de acesso baseado em roles**:
  - Roles disponíveis: `ADMIN`, `USER`.
  - Acesso restrito para ADMIN em rotas específicas.
- **Gestão de Posts**: Criar, visualizar e excluir posts.
  - Apenas criadores de posts ou ADMIN podem excluí-los.


