# 🧪 Guia de Testes - Sistema de Carteirinha de Membros

## Como Testar a API com Postman ou cURL

---

## 📍 URL Base
```
http://localhost:8080
```

---

## 1️⃣ REGISTRAR NOVO USUÁRIO

### Requisição
```
POST /auth/register
Content-Type: application/json

{
  "cpf": "12345678901",
  "password": "senha123",
  "name": "João Silva"
}
```

### Resposta (201)
```
Usuário cadastrado com sucesso. Aguarde aprovação do administrador.
```

### Notas
- CPF deve ser único
- Status do Member será PENDING automaticamente
- Cargo será MEMBRO automaticamente

---

## 2️⃣ LOGIN (Usuário ADMIN - Para Teste)

### Requisição
```
POST /auth/login
Content-Type: application/json

{
  "cpf": "11111111111",
  "password": "admin123"
}
```

### Resposta (200)
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiY3BmIjoiMTExMTExMTExMTEiLCJyb2xlIjoiQURNSU4iLCJpYXQiOjE3MTI4ODAwMDAsImV4cCI6MTcxMjk2NjQwMH0.xyz...",
  "type": "Bearer",
  "userId": 1,
  "cpf": "11111111111",
  "role": "ADMIN"
}
```

### ⚠️ Importante
**Guarde o token!** Será necessário para os próximos passos.

---

## 3️⃣ OBTER DADOS DO USUÁRIO AUTENTICADO

### Requisição
```
GET /users/me
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

### Resposta (200)
```json
{
  "id": 1,
  "cpf": "11111111111",
  "password": "$2a$10$...",
  "role": "ADMIN"
}
```

---

## 4️⃣ LISTAR TODOS OS MEMBROS

### Requisição
```
GET /members
Authorization: Bearer {seu_token_aqui}
```

### Resposta (200)
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

## 5️⃣ OBTER DETALHES DE UM MEMBRO

### Requisição
```
GET /members/1
Authorization: Bearer {seu_token_aqui}
```

### Resposta (200)
Mesmo formato de um objeto da lista acima.

---

## 6️⃣ LISTAR MEMBROS PENDENTES (ADMIN)

### Requisição
```
GET /members/pending
Authorization: Bearer {seu_token_admin_aqui}
```

### Resposta (200)
```json
[
  {
    "id": 1,
    "name": "João Silva",
    "status": "PENDING",
    ...
  }
]
```

---

## 7️⃣ APROVAR MEMBRO (ADMIN)

### Requisição
```
PATCH /members/1/approve
Authorization: Bearer {seu_token_admin_aqui}
```

### Resposta (200)
```json
{
  "id": 1,
  "name": "João Silva",
  "status": "ACTIVE",
  ...
}
```

### Notas
- Apenas ADMIN pode aprovar
- Status muda de PENDING para ACTIVE
- Agora usuário pode fazer login

---

## 8️⃣ INATIVAR MEMBRO (ADMIN)

### Requisição
```
PATCH /members/1/inactive
Authorization: Bearer {seu_token_admin_aqui}
```

### Resposta (200)
```json
{
  "id": 1,
  "name": "João Silva",
  "status": "INACTIVE",
  ...
}
```

---

## 9️⃣ ALTERAR CARGO DO MEMBRO (ADMIN)

### Requisição
```
PATCH /members/1/cargo?cargo=PASTOR
Authorization: Bearer {seu_token_admin_aqui}
```

### Resposta (200)
```json
{
  "id": 1,
  "name": "João Silva",
  "cargo": "PASTOR",
  ...
}
```

### Cargos Disponíveis
- MEMBRO (padrão)
- DIACONO
- PRESBITERO
- PASTOR
- COOPERADOR
- EVANGELISTA
- OBREIRO

---

## 🔟 LISTAR IGREJAS

### Requisição
```
GET /churches
Authorization: Bearer {seu_token_aqui}
```

### Resposta (200)
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

## 1️⃣1️⃣ CRIAR NOVA IGREJA (ADMIN)

### Requisição
```
POST /churches
Content-Type: application/json
Authorization: Bearer {seu_token_admin_aqui}

{
  "name": "Igreja Segunda",
  "city": "Rio de Janeiro"
}
```

### Resposta (201)
```json
{
  "id": 2,
  "name": "Igreja Segunda",
  "city": "Rio de Janeiro"
}
```

---

## 1️⃣2️⃣ ATUALIZAR IGREJA (ADMIN)

### Requisição
```
PUT /churches/1
Content-Type: application/json
Authorization: Bearer {seu_token_admin_aqui}

{
  "name": "Igreja Primeira Atualizada",
  "city": "Campinas"
}
```

### Resposta (200)
Igreja atualizada.

---

## 1️⃣3️⃣ DELETAR IGREJA (ADMIN)

### Requisição
```
DELETE /churches/1
Authorization: Bearer {seu_token_admin_aqui}
```

### Resposta (204)
No content (sucesso).

---

## 🔴 TRATAMENTO DE ERROS

### CPF Duplicado
```
Status: 409 Conflict
{
  "status": 409,
  "message": "CPF já cadastrado",
  "timestamp": 1712880000000
}
```

### Usuário Não Ativo
```
Status: 403 Forbidden
{
  "status": 403,
  "message": "Usuário não ativo. Aguarde aprovação do admin",
  "timestamp": 1712880000000
}
```

### Recurso Não Encontrado
```
Status: 404 Not Found
{
  "status": 404,
  "message": "Member não encontrado",
  "timestamp": 1712880000000
}
```

### Token Inválido/Expirado
```
Status: 401 Unauthorized
Sem token no header Authorization
```

### Sem Permissão (Não é ADMIN)
```
Status: 403 Forbidden
Tentando acessar endpoint que requer role ADMIN
```

---

## 📝 Dicas de Teste

### 1. Salvar Token em Variável
```
// Postman: Após login, na aba Tests:
pm.environment.set("token", pm.response.json().token);

// Usar: {{token}}
```

### 2. Executar com cURL
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"cpf":"12345678901","password":"senha123","name":"João"}'
```

### 3. Usar Postman Collection
Importar arquivo JSON com todos os endpoints pré-configurados.

---

## ✅ Checklist de Testes

- [ ] Registrar novo usuário
- [ ] Tentar registrar com CPF duplicado (deve falhar)
- [ ] Login com usuário PENDING (deve falhar)
- [ ] Login com ADMIN
- [ ] Aprovar membro
- [ ] Login com membro aprovado
- [ ] Listar membros
- [ ] Alterar cargo do membro
- [ ] Inativar membro
- [ ] Criar igreja
- [ ] Atualizar igreja
- [ ] Deletar igreja
- [ ] Testar token expirado
- [ ] Testar token inválido
- [ ] Testar acesso sem token

---

## 🗄️ H2 Database Console

Acessar: `http://localhost:8080/h2-console`

**Conexão:**
- Driver: org.h2.Driver
- URL: jdbc:h2:mem:testdb
- User: sa
- Password: (vazio)

**Queries Úteis:**
```sql
-- Listar usuários
SELECT * FROM users;

-- Listar membros
SELECT * FROM members;

-- Listar igrejas
SELECT * FROM churches;

-- Contar membros por status
SELECT status, COUNT(*) FROM members GROUP BY status;
```

---

## 🎯 Cenário Completo de Teste

1. **Registrar 3 usuários diferentes**
   ```
   POST /auth/register (João, CPF: 111...)
   POST /auth/register (Maria, CPF: 222...)
   POST /auth/register (Pedro, CPF: 333...)
   ```

2. **Login com ADMIN**
   ```
   POST /auth/login (CPF: admin, Password: admin123)
   Guardar token
   ```

3. **Listar membros pendentes**
   ```
   GET /members/pending
   Deve retornar 3 membros com status PENDING
   ```

4. **Aprovar primeiro membro**
   ```
   PATCH /members/1/approve
   ```

5. **Tentar login com membro pendente**
   ```
   POST /auth/login (CPF: 222...)
   Deve falhar com "Usuário não ativo"
   ```

6. **Tentar login com membro aprovado**
   ```
   POST /auth/login (CPF: 111...)
   Deve retornar token
   ```

7. **Alterar cargo de membro**
   ```
   PATCH /members/1/cargo?cargo=PASTOR
   ```

8. **Inativar membro**
   ```
   PATCH /members/1/inactive
   ```

9. **Criar e deletar igreja**
   ```
   POST /churches
   DELETE /churches/1
   ```

---

Pronto para testar! 🚀

