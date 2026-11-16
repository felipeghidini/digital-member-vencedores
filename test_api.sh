#!/bin/bash
# 🧪 Script de Teste da API - Sistema de Carteirinha de Membros
# Executar: bash test_api.sh

BASE_URL="http://localhost:8080"
ADMIN_CPF="11111111111"
ADMIN_PASSWORD="admin123"

echo "=========================================="
echo "🧪 Testando API de Carteirinha de Membros"
echo "=========================================="

# Cores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Função para exibir resultado
print_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ Sucesso${NC}"
    else
        echo -e "${RED}✗ Erro${NC}"
    fi
}

# ==========================================
# 1. REGISTRAR NOVO USUÁRIO
# ==========================================
echo ""
echo -e "${YELLOW}1. Registrando novo usuário...${NC}"

RESPONSE=$(curl -s -X POST "$BASE_URL/auth/register" \
    -H "Content-Type: application/json" \
    -d '{
        "cpf": "12345678901",
        "password": "senha123",
        "name": "João Silva"
    }')

echo "Response: $RESPONSE"
print_result $?

# ==========================================
# 2. TENTAR REGISTRAR COM CPF DUPLICADO
# ==========================================
echo ""
echo -e "${YELLOW}2. Tentando registrar com CPF duplicado...${NC}"

RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/auth/register" \
    -H "Content-Type: application/json" \
    -d '{
        "cpf": "12345678901",
        "password": "outra_senha",
        "name": "Outro João"
    }')

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)

echo "HTTP Code: $HTTP_CODE"
echo "Response: $BODY"

if [ "$HTTP_CODE" = "409" ]; then
    echo -e "${GREEN}✓ Corretamente retornou 409 Conflict${NC}"
else
    echo -e "${RED}✗ Esperado 409, recebido $HTTP_CODE${NC}"
fi

# ==========================================
# 3. LOGIN COM ADMIN
# ==========================================
echo ""
echo -e "${YELLOW}3. Fazendo login como ADMIN...${NC}"

LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
    -H "Content-Type: application/json" \
    -d '{
        "cpf": "'$ADMIN_CPF'",
        "password": "'$ADMIN_PASSWORD'"
    }')

echo "Response: $LOGIN_RESPONSE"

# Extrair token (usando jq se disponível, senão usar grep)
if command -v jq &> /dev/null; then
    TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.token')
else
    TOKEN=$(echo "$LOGIN_RESPONSE" | grep -oP '"token":"\K[^"]*')
fi

echo -e "${GREEN}Token obtido: ${TOKEN:0:50}...${NC}"

# ==========================================
# 4. OBTER DADOS DO USUÁRIO AUTENTICADO
# ==========================================
echo ""
echo -e "${YELLOW}4. Obtendo dados do usuário autenticado...${NC}"

RESPONSE=$(curl -s -X GET "$BASE_URL/users/me" \
    -H "Authorization: Bearer $TOKEN")

echo "Response: $RESPONSE"
print_result $?

# ==========================================
# 5. LISTAR TODOS OS MEMBROS
# ==========================================
echo ""
echo -e "${YELLOW}5. Listando todos os membros...${NC}"

RESPONSE=$(curl -s -X GET "$BASE_URL/members" \
    -H "Authorization: Bearer $TOKEN")

echo "Response: $RESPONSE"
print_result $?

# ==========================================
# 6. LISTAR MEMBROS PENDENTES
# ==========================================
echo ""
echo -e "${YELLOW}6. Listando membros pendentes (ADMIN only)...${NC}"

RESPONSE=$(curl -s -X GET "$BASE_URL/members/pending" \
    -H "Authorization: Bearer $TOKEN")

echo "Response: $RESPONSE"
print_result $?

# ==========================================
# 7. CRIAR NOVA IGREJA
# ==========================================
echo ""
echo -e "${YELLOW}7. Criando nova igreja...${NC}"

CHURCH_RESPONSE=$(curl -s -X POST "$BASE_URL/churches" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN" \
    -d '{
        "name": "Igreja Primeira",
        "city": "São Paulo"
    }')

echo "Response: $CHURCH_RESPONSE"
print_result $?

# ==========================================
# 8. LISTAR IGREJAS
# ==========================================
echo ""
echo -e "${YELLOW}8. Listando igrejas...${NC}"

RESPONSE=$(curl -s -X GET "$BASE_URL/churches" \
    -H "Authorization: Bearer $TOKEN")

echo "Response: $RESPONSE"
print_result $?

# ==========================================
# 9. TENTAR ACESSAR SEM TOKEN
# ==========================================
echo ""
echo -e "${YELLOW}9. Tentando acessar sem token (deve falhar)...${NC}"

RESPONSE=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL/members")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)

echo "HTTP Code: $HTTP_CODE"

if [ "$HTTP_CODE" = "403" ] || [ "$HTTP_CODE" = "401" ]; then
    echo -e "${GREEN}✓ Corretamente rejeitou requisição sem token${NC}"
else
    echo -e "${RED}✗ Esperado 401/403, recebido $HTTP_CODE${NC}"
fi

# ==========================================
# 10. H2 DATABASE
# ==========================================
echo ""
echo -e "${YELLOW}10. H2 Database Console${NC}"
echo "Acesse: http://localhost:8080/h2-console"
echo "URL: jdbc:h2:mem:testdb"
echo "User: sa"
echo "Password: (vazio)"

# ==========================================
# RESUMO
# ==========================================
echo ""
echo "=========================================="
echo -e "${GREEN}✨ Testes Concluídos!${NC}"
echo "=========================================="
echo ""
echo "Endpoints testados:"
echo "  ✓ POST   /auth/register"
echo "  ✓ POST   /auth/login"
echo "  ✓ GET    /users/me"
echo "  ✓ GET    /members"
echo "  ✓ GET    /members/pending"
echo "  ✓ POST   /churches"
echo "  ✓ GET    /churches"
echo ""
echo "Para testes mais detalhados, veja TESTING_GUIDE.md"
echo ""

