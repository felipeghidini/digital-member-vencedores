# ⚡ QUICK START - 5 MINUTOS

## 1️⃣ Executar a Aplicação

```bash
cd C:\Users\FELIPE\Projetos\digital-member-vencedores
mvn clean compile
mvn spring-boot:run
```

A aplicação estará disponível em: http://localhost:8080

---

## 2️⃣ Acessar H2 Database

URL: http://localhost:8080/h2-console

- **Driver:** org.h2.Driver
- **URL:** jdbc:h2:mem:testdb
- **User:** sa
- **Password:** (deixar em branco)

---

## 3️⃣ Registrar um Novo Usuário

**Requisição:**
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "cpf": "12345678901",
    "password": "senha123",
    "name": "João Silva"
  }'
```

**Resposta:**
```
Usuário cadastrado com sucesso. Aguarde aprovação do administrador.
```

---

## 4️⃣ Fazer Login com ADMIN

**Requisição:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "cpf": "11111111111",
    "password": "admin123"
  }'
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "userId": 1,
  "cpf": "11111111111",
  "role": "ADMIN"
}
```

**Guarde o token!** Você precisará para os próximos passos.

---

## 5️⃣ Aprovar Membro (com token do ADMIN)

**Requisição:**
```bash
curl -X PATCH http://localhost:8080/members/2/approve \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

**Resposta:**
```json
{
  "id": 2,
  "name": "João Silva",
  "status": "ACTIVE"
}
```

---

## 6️⃣ Fazer Login com Membro Aprovado

**Requisição:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "cpf": "12345678901",
    "password": "senha123"
  }'
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "userId": 2,
  "cpf": "12345678901",
  "role": "MEMBER"
}
```

---

## 📡 Outros Endpoints Rápidos

### Listar Membros
```bash
curl -X GET http://localhost:8080/members \
  -H "Authorization: Bearer SEU_TOKEN"
```

### Listar Igrejas
```bash
curl -X GET http://localhost:8080/churches \
  -H "Authorization: Bearer SEU_TOKEN"
```

### Criar Igreja (ADMIN)
```bash
curl -X POST http://localhost:8080/churches \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_ADMIN" \
  -d '{
    "name": "Igreja Primeira",
    "city": "São Paulo"
  }'
```

### Alterar Cargo (ADMIN)
```bash
curl -X PATCH "http://localhost:8080/members/2/cargo?cargo=PASTOR" \
  -H "Authorization: Bearer SEU_TOKEN_ADMIN"
```

---

## 🔑 Credenciais de Teste

### ADMIN
- CPF: `11111111111`
- Senha: `admin123`
- Role: ADMIN

### Novo Usuário (para registrar)
- CPF: `12345678901`
- Senha: `senha123`
- Nome: `João Silva`

---

## 📱 Usando Postman

1. Abra Postman
2. Configure a URL base: `http://localhost:8080`
3. Copie as requisições dos exemplos acima
4. Teste os endpoints
5. Ao fazer login, copie o token
6. Crie uma variável `token` com o valor do token
7. Use `{{token}}` no header Authorization

---

## ✅ Checklist Rápido

- [ ] Aplicação rodando em http://localhost:8080
- [ ] H2 Console acessível em http://localhost:8080/h2-console
- [ ] Conseguiu registrar um novo usuário
- [ ] Conseguiu fazer login como ADMIN
- [ ] Conseguiu aprovar um membro
- [ ] Conseguiu fazer login com membro aprovado
- [ ] Conseguiu listar membros
- [ ] Conseguiu criar uma igreja

---

## 📖 Próximos Passos

1. **Explorar Endpoints**
   - Veja: API_DOCUMENTATION.md

2. **Testes Completos**
   - Veja: TESTING_GUIDE.md

3. **Deploy**
   - Veja: DEPLOYMENT_GUIDE.md

4. **Implementação**
   - Veja: IMPLEMENTATION_SUMMARY.md

---

## 🆘 Problemas Comuns

### Porta 8080 em uso
```bash
# Mudar porta em application.properties
server.port=8081
```

### JWT Secret não configurado
```properties
# application.properties
app.jwtSecret=mySecretKeyForJWTTokenGenerationAndValidationPurposesOnly123456789012345
```

### Banco não inicializa
```bash
# Limpar e compilar novamente
mvn clean compile
```

---

## 🎉 Pronto!

Você tem um sistema completo de carteirinha de membros rodando!

Aproveite! 🚀

---

Tempo total: ⏱️ ~5 minutos
Dificuldade: ⭐ Fácil

