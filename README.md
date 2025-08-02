# 🎥 fanCollectorsMedia

Sistema completo para colecionadores de mídias físicas (Blu-ray, DVD, VHS, LP, CD...), permitindo cadastro detalhado para midia fśicas, salvando, editando, deletando e visualizando todos registro de cada usuário, integração com a API TMDB e outras que serão acresentadas com o desenvolvimento da aplicação web, autenticação de usuários, e muito mais!

---

## 🚀 Demonstração

Você pode acessar a aplicação em produção pelo link abaixo:

👉 [https://fan-collectors-frontend-app.vercel.app/](https://fan-collectors-frontend-app.vercel.app/)

[![Deploy](https://img.shields.io/badge/🔗%20Deploy-Vercel-blue?style=flat&logo=vercel)](https://fan-collectors-frontend-app.vercel.app/)

---

## 🚀 Tecnologias

### 🧠 Backend (Java + Spring Boot)

- Java 17+
- Spring Boot 3+
- Spring Security com JWT
- Spring Data JPA
- Flyway (migrações de banco)
- PostgreSQL
- Lombok
- Bean Validation
- ModelMapper (ou MapStruct - futuro)

---

## 🛠️ Como rodar localmente

### 📦 Backend

```bash
# Clone o projeto e navegue até a pasta backend
cd fanCollectorsMedia

# Configure o banco PostgreSQL
# Altere application.properties conforme necessário

# Rode o projeto com o Spring Tool Suite ou com:
./mvnw spring-boot:run
```

## 🔐 Variáveis de Ambiente (.env)

**Backend (application.properties)**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fanCollectorsMedia
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true

jwt.secret=seu_segredo_jwt
jwt.expiration=86400000
```

---

## 📌 Endpoints Principais

| Método | Endpoint              | Descrição                             |
|--------|-----------------------|----------------------------------------|
| POST   | `/auth/login`         | Login com e-mail e senha               |
| POST   | `/auth/register`      | Cadastro de novo usuário               |
| GET    | `/cadastros/perfil`   | Retorna dados do usuário autenticado   |
| GET    | `/cadastro-hobby`     | Lista hobbies do usuário autenticado   |
| POST   | `/midias`             | Cadastro de nova mídia                 |
| GET    | `/midias`             | Lista mídias do usuário                |

---

## 🧪 Testes

- Testes unitários no backend com JUnit e Mockito
- Testes de integração (usuário + autenticação)

---

## 👤 Autor

<p align="left">
  <img src="https://avatars.githubusercontent.com/u/52794588?v=1" width="120" alt="WagnerDf"/>
</p>

| **Wagner Andrade (WagnerDf)** |
</br>
| Desenvolvedor Fullstack Java & React |
</br>
| [LinkedIn](https://www.linkedin.com/in/wagner-andrade-876b6460) |
</br> 
| [GitHub](https://github.com/WagnerDf) |

---

## 📝 Licença

Este projeto está sob a licença MIT.  
Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 🤝 Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir uma _issue_, enviar _pull requests_ ou sugerir melhorias.

---

## 🌐 API TMDB

Este projeto usa a API pública do [TMDB](https://www.themoviedb.org/).  
Você precisa gerar uma **chave de API** gratuita para usar os recursos de busca automática de filmes e séries.

---

## 💡 Inspiração

Este sistema foi idealizado para organizar coleções pessoais de mídia física com praticidade, visual moderno e dados completos.

---

> Criado com 💙 por WagnerDf
