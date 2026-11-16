## 📖 Documentação - Sistema de Carteirinha de Membros da Igreja

### 🚀 Visão Geral

Backend completo para gerenciar membros de uma Igreja com autenticação JWT, desenvolvido em Java 21 com Spring Boot.

---

## 🏗️ Estrutura do Projeto

```
com.app.carteirinha.vencedores
├── controllers/      # REST Controllers
├── services/         # Lógica de negócio
├── repositories/     # Acesso a dados (JPA)
├── entities/         # Modelos JPA
├── dtos/            # Data Transfer Objects
├── enums/           # Enumerações (Role, Status, Cargo)
├── security/        # JWT e autenticação
├── exceptions/      # Exceções customizadas
└── config/          # Configurações Spring
```

---

## 🔑 Autenticação JWT

Todos os endpoints (exceto `/auth/**` e `/h2-console/**`) requerem autenticação via JWT.

### Fluxo de Autenticação:

1. **Registrar** → `POST /auth/register`
2. **Login** → `POST /auth/login`
3. **Usar Token** → Incluir no header: `Authorization: Bearer {token}`

---

## 📡 Endpoints da API

### 🔐 Autenticação (`/auth`)

#### POST `/auth/register`
Registra um novo usuário e cria um Member com status PENDING.

**Body:**
```json
{
  "cpf": "12345678901",
  "password": "senha123",
  "name": "João Silva"
}
```

**Response (201):**
```
Usuário cadastrado com sucesso. Aguarde aprovação do administrador.
```

---

#### POST `/auth/login`
Realiza login e retorna JWT token.

**Body:**
```json
{
  "cpf": "12345678901",
  "password": "senha123"
}
```

**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "userId": 1,
  "cpf": "12345678901",
  "role": "MEMBER"
}
```

---

### 👤 Usuário (`/users`)

#### GET `/users/me`
Retorna dados do usuário autenticado.

**Headers:**
```
Authorization: Bearer {token}
```

**Response (200):**
```json
{
  "id": 1,
  "cpf": "12345678901",
  "password": "$2a$10...",
  "role": "MEMBER"
}
```

---

### 🙋 Membros (`/members`)

#### GET `/members`
Lista todos os membros.

**Response (200):**
```json
[
  {
    "id": 1,
    "name": "João Silva",
    "birthDate": "14/04/1989",
    "photoUrl": "foto.jpg",
    "rg": "123456789",
    "cargo": "MEMBRO",
    "baptismDate": "12/05/2011",
    "fatherName": "Marco Antonio",
    "motherName": "Maria Ivamar",
    "createdAt": "11/04/2026",
    "updatedAt": "11/04/2026",
    "status": "PENDING"
  }
]
```

---

#### GET `/members/{id}`
Retorna um membro específico.

**Response (200):**
Mesmo formato da lista acima (um único objeto).

---

#### GET `/members/pending` ⚙️ ADMIN
Lista apenas membros pendentes de aprovação.

**Response (200):**
Array de membros com `status: PENDING`

---

#### PATCH `/members/{id}/approve` ⚙️ ADMIN
Aprova um membro (altera status para ACTIVE).

**Response (200):**
```json
{
  "id": 1,
  "status": "ACTIVE",
  ...
}
```

---

#### PATCH `/members/{id}/inactive` ⚙️ ADMIN
Inativa um membro (altera status para INACTIVE).

**Response (200):**
```json
{
  "id": 1,
  "status": "INACTIVE",
  ...
}
```

---

#### PATCH `/members/{id}/cargo` ⚙️ ADMIN
Altera o cargo de um membro.

**Query Parameters:**
```
cargo=PASTOR
```

**Response (200):**
```json
{
  "id": 1,
  "cargo": "PASTOR",
  ...
}
```

---

### ⛪ Igrejas (`/churches`)

#### GET `/churches`
Lista todas as igrejas.

**Response (200):**
```json
[
  {
    "id": 1,
    "name": "Igreja Primeira",
    "city": "São Paulo"
  }
]
```

---

#### GET `/churches/{id}`
Retorna uma igreja específica.

**Response (200):**
Mesmo formato da lista acima (um único objeto).

---

#### POST `/churches` ⚙️ ADMIN
Cria uma nova igreja.

**Body:**
```json
{
  "name": "Igreja Segunda",
  "city": "Rio de Janeiro"
}
```

**Response (201):**
```json
{
  "id": 2,
  "name": "Igreja Segunda",
  "city": "Rio de Janeiro"
}
```

---

#### PUT `/churches/{id}` ⚙️ ADMIN
Atualiza uma igreja.

**Body:**
```json
{
  "name": "Igreja Segunda Atualizada",
  "city": "Belo Horizonte"
}
```

**Response (200):**
Igreja atualizada.

---

#### DELETE `/churches/{id}` ⚙️ ADMIN
Deleta uma igreja.

**Response (204):**
No content.

---

## 🗄️ Enums

### Role (Papel do Usuário)
- `ADMIN` - Administrador (acesso total)
- `MEMBER` - Membro (acesso limitado)

### Status (Status do Membro)
- `PENDING` - Aguardando aprovação
- `ACTIVE` - Ativo
- `INACTIVE` - Inativo

### Cargo (Função do Membro)
- `MEMBRO`
- `DIACONO`
- `PRESBITERO`
- `PASTOR`
- `COOPERADOR`
- `EVANGELISTA`
- `OBREIRO`

---

## ⚙️ Configuração

### application.properties
```properties
# JWT
app.jwtSecret=mySecretKeyForJWTTokenGenerationAndValidationPurposesOnly123456789012345
app.jwtExpirationInMs=86400000  # 24 horas

# Banco de dados
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
```

---

## 🧪 Teste no Postman

### 1. Registrar Usuário
```
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "cpf": "12345678901",
  "password": "senha123",
  "name": "João Silva"
}
```

### 2. Login
```
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "cpf": "12345678901",
  "password": "senha123"
}
```

### 3. Usar Token
```
GET http://localhost:8080/users/me
Authorization: Bearer {token_obtido_no_login}
```

### 4. Listar Membros
```
GET http://localhost:8080/members
Authorization: Bearer {token}
```

---

## 🔒 Regras de Segurança

1. **CPF único** - Não permite CPF duplicado
2. **Senha criptografada** - Usa BCrypt
3. **Status PENDING** - Novo membro inicia como PENDING
4. **Login bloqueado** - Não pode fazer login se status != ACTIVE (exceto ADMIN)
5. **Rotas ADMIN** - Protegidas apenas para role ADMIN

---

## 🛠️ Tecnologias

- **Java 21**
- **Spring Boot 4.0.5**
- **Spring Security**
- **JWT (jjwt)**
- **JPA/Hibernate**
- **H2 Database** (desenvolvimento)
- **PostgreSQL** (produção)

---

## 📝 Boas Práticas Implementadas

✅ Sem Lombok (Java puro com getters/setters)
✅ Separação de responsabilidades (Controller → Service → Repository)
✅ DTOs para entrada/saída
✅ Exceções customizadas
✅ Exception Handler global
✅ JWT para autenticação
✅ BCrypt para criptografia
✅ Logs básicos
✅ Código limpo e legível
✅ Endpoints REST seguindo padrões

---

## 🚀 Deploy

Para produção, configure:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/carteirinha
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=validate
app.jwtSecret=CHAVE_SECRETA_FORTE_AQUI
```

---

## 📞 Contato / Suporte

Para dúvidas sobre a API, consulte esta documentação ou verifique os controllers correspondentes.

