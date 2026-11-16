# 🚀 GUIA DE DEPLOY - Sistema de Carteirinha de Membros

## Ambiente de Desenvolvimento

### Pré-requisitos
- Java 21+
- Maven 3.8+
- H2 Database (automático)

### Executar Localmente
```bash
mvn clean install
mvn spring-boot:run
```

URL: http://localhost:8080

---

## Ambiente de Staging

### Pré-requisitos
- Java 21+
- PostgreSQL 12+
- Servidor Linux/Unix

### Configuração do Banco
```sql
CREATE DATABASE carteirinha;
CREATE USER carteirinha WITH PASSWORD 'your_secure_password';
GRANT ALL PRIVILEGES ON DATABASE carteirinha TO carteirinha;
```

### application-staging.properties
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/carteirinha
spring.datasource.username=carteirinha
spring.datasource.password=your_secure_password
spring.jpa.hibernate.ddl-auto=validate

app.jwtSecret=YOUR_LONG_SECRET_KEY_HERE_MINIMUM_64_CHARACTERS
app.jwtExpirationInMs=86400000

logging.level.root=WARN
logging.level.com.app.carteirinha.vencedores=INFO
```

### Executar
```bash
mvn clean package -Pstaging
java -jar target/vencedores.jar --spring.profiles.active=staging
```

---

## Ambiente de Produção

### Pré-requisitos
- Java 21+
- PostgreSQL 12+
- Servidor com SSL/TLS
- Nginx/Apache reverse proxy

### Configuração do Banco
```sql
CREATE DATABASE carteirinha_prod;
CREATE USER carteirinha_prod WITH PASSWORD 'very_secure_password_here';
GRANT ALL PRIVILEGES ON DATABASE carteirinha_prod TO carteirinha_prod;
```

### application-production.properties
```properties
# Database
spring.datasource.url=jdbc:postgresql://prod-db-server:5432/carteirinha_prod
spring.datasource.username=carteirinha_prod
spring.datasource.password=${DB_PASSWORD}
spring.datasource.hikari.maximum-pool-size=10
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# JWT Security
app.jwtSecret=${JWT_SECRET}
app.jwtExpirationInMs=86400000

# Server Configuration
server.port=8080
server.servlet.context-path=/api
server.compression.enabled=true
server.compression.min-response-size=1024

# Logging
logging.level.root=WARN
logging.level.com.app.carteirinha.vencedores=INFO
logging.file.name=/var/log/carteirinha/app.log

# Actuator (Health checks)
management.endpoints.web.exposure.include=health,metrics
management.endpoint.health.show-details=when-authorized
```

### Variáveis de Ambiente
```bash
export DB_PASSWORD="very_secure_password_here"
export JWT_SECRET="your_very_long_secret_key_minimum_64_characters_long_here"
```

### Build para Produção
```bash
mvn clean package -Pprod -DskipTests
```

### Executar
```bash
java -jar target/vencedores.jar --spring.profiles.active=production
```

---

## Docker (Recomendado)

### Dockerfile
```dockerfile
FROM openjdk:21-slim
WORKDIR /app
COPY target/vencedores.jar app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=production
CMD ["java", "-jar", "app.jar"]
```

### Build Docker Image
```bash
mvn clean package -Pprod
docker build -t carteirinha:1.0.0 .
```

### docker-compose.yml
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: carteirinha_prod
      POSTGRES_USER: carteirinha_prod
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

  api:
    build: .
    environment:
      SPRING_PROFILES_ACTIVE: production
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/carteirinha_prod
      SPRING_DATASOURCE_USERNAME: carteirinha_prod
      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
      JWT_SECRET: ${JWT_SECRET}
    ports:
      - "8080:8080"
    depends_on:
      - postgres

volumes:
  postgres_data:
```

### Executar com Docker Compose
```bash
export DB_PASSWORD="your_password"
export JWT_SECRET="your_secret_key"
docker-compose up -d
```

---

## Nginx Reverse Proxy

### /etc/nginx/sites-available/carteirinha
```nginx
upstream carteirinha {
    server localhost:8080;
}

server {
    listen 80;
    server_name api.seu-dominio.com;
    
    # Redirect HTTP to HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name api.seu-dominio.com;
    
    ssl_certificate /etc/ssl/certs/seu-certificado.crt;
    ssl_certificate_key /etc/ssl/private/sua-chave-privada.key;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    
    client_max_body_size 10M;
    
    location / {
        proxy_pass http://carteirinha;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # Timeouts
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }
}
```

### Habilitar
```bash
sudo ln -s /etc/nginx/sites-available/carteirinha /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

---

## Systemd Service (Linux)

### /etc/systemd/system/carteirinha.service
```ini
[Unit]
Description=Carteirinha de Membros API
After=network.target postgresql.service

[Service]
Type=simple
User=carteirinha
WorkingDirectory=/opt/carteirinha
ExecStart=/usr/bin/java -jar /opt/carteirinha/vencedores.jar
Restart=on-failure
RestartSec=10

Environment="SPRING_PROFILES_ACTIVE=production"
Environment="DB_PASSWORD=sua_senha"
Environment="JWT_SECRET=sua_chave_secreta"

[Install]
WantedBy=multi-user.target
```

### Iniciar Serviço
```bash
sudo useradd -m -s /bin/bash carteirinha
sudo cp /opt/carteirinha/vencedores.jar /opt/carteirinha/
sudo chown -R carteirinha:carteirinha /opt/carteirinha

sudo systemctl daemon-reload
sudo systemctl enable carteirinha
sudo systemctl start carteirinha
sudo systemctl status carteirinha
```

---

## Monitoramento

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Metrics
```bash
curl http://localhost:8080/actuator/metrics
```

### Logs
```bash
# Arquivo
tail -f /var/log/carteirinha/app.log

# Systemd
journalctl -u carteirinha -f
```

---

## Backup do Banco

### PostgreSQL Backup
```bash
pg_dump -U carteirinha_prod -h localhost carteirinha_prod > backup.sql

# Com data
pg_dump -U carteirinha_prod -h localhost carteirinha_prod > backup_$(date +%Y%m%d_%H%M%S).sql
```

### Restaurar
```bash
psql -U carteirinha_prod -h localhost carteirinha_prod < backup.sql
```

### Backup Automático (Cron)
```bash
0 2 * * * pg_dump -U carteirinha_prod -h localhost carteirinha_prod | gzip > /backups/carteirinha_$(date +\%Y\%m\%d_\%H\%M\%S).sql.gz
```

---

## Checklist de Deploy

### Antes do Deploy
- [ ] Testar em staging
- [ ] Validar variáveis de ambiente
- [ ] Backup do banco de dados
- [ ] Certificado SSL válido
- [ ] DNS apontando para servidor

### Deploy
- [ ] Build do projeto
- [ ] Upload dos arquivos
- [ ] Configurar banco de dados
- [ ] Iniciar serviço
- [ ] Validar saúde da API

### Depois do Deploy
- [ ] Testar endpoints críticos
- [ ] Monitorar logs
- [ ] Verificar métricas
- [ ] Validar backup automático
- [ ] Documentar versão deployada

---

## Troubleshooting

### Erro de Conexão com Banco
```
Error: Failed to initialize database
```
Solução: Verificar credenciais de banco e URL de conexão

### Erro de JWT Secret
```
Error: JWT secret must be at least 256 bits
```
Solução: Usar chave secreta com mínimo 64 caracteres

### Porta em Uso
```
Error: Address already in use :8080
```
Solução: Mudar porta em application.properties ou matar processo

### OutOfMemory
```
Error: java.lang.OutOfMemoryError
```
Solução: Aumentar heap memory: `java -Xmx1g -jar vencedores.jar`

---

## Performance

### Tuning JVM
```bash
java -Xms512m -Xmx1g \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -jar vencedores.jar
```

### Connection Pool
```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
```

### Cache
```properties
spring.cache.type=caffeine
spring.cache.caffeine.spec=maximumSize=1000,expireAfterWrite=10m
```

---

## Rollback

Se algo der errado:

```bash
# Parar serviço
sudo systemctl stop carteirinha

# Restaurar versão anterior
sudo cp /opt/carteirinha/backups/vencedores-1.0.0.jar /opt/carteirinha/vencedores.jar

# Restaurar banco (se necessário)
psql -U carteirinha_prod < /backups/carteirinha_backup.sql

# Iniciar serviço
sudo systemctl start carteirinha
```

---

## Suporte

Para dúvidas sobre deploy:
1. Verifique logs: `journalctl -u carteirinha -f`
2. Teste saúde: `curl http://localhost:8080/actuator/health`
3. Valide banco: `psql -U carteirinha_prod -l`

---

Desenvolvido: 11 de Abril de 2026
Versão: 1.0.0

