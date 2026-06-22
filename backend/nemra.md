# 📒 ServiFind — Backend README
> Modern service provider directory platform — Spring Boot + PostgreSQL

---

## 📌 Project Overview

ServiFind is a modern take on the classic Yellow Pages — a platform where users can find verified local service providers (plumbers, carpenters, electricians, etc.), view their portfolio, read detailed reviews, and eventually communicate directly within the platform.

**Starting market:** Morocco 🇲🇦  
**Future:** Worldwide expansion  
**Architecture:** Modular Monolith (microservices-ready)

---

## 🧱 Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3.x |
| Database | PostgreSQL |
| Migrations | Flyway |
| Authentication | JWT |
| Image Storage | Cloudinary (MVP) → Cloudflare R2 (scale) |
| Build Tool | Maven |

---

## 📁 Project Folder Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/servifind/
│   │   │   │
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── JwtConfig.java
│   │   │   │   ├── CloudinaryConfig.java
│   │   │   │   └── CorsConfig.java
│   │   │   │
│   │   │   ├── modules/
│   │   │   │   │
│   │   │   │   ├── auth/
│   │   │   │   │   ├── AuthController.java
│   │   │   │   │   ├── AuthService.java
│   │   │   │   │   ├── AuthRepository.java
│   │   │   │   │   └── dto/
│   │   │   │   │       ├── LoginRequest.java
│   │   │   │   │       ├── RegisterRequest.java
│   │   │   │   │       └── AuthResponse.java
│   │   │   │   │
│   │   │   │   ├── users/
│   │   │   │   │   ├── UserController.java
│   │   │   │   │   ├── UserService.java
│   │   │   │   │   ├── UserRepository.java
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── User.java
│   │   │   │   │   │   ├── ClientProfile.java
│   │   │   │   │   │   └── Role.java (enum)
│   │   │   │   │   └── dto/
│   │   │   │   │       ├── UserDTO.java
│   │   │   │   │       └── UpdateProfileRequest.java
│   │   │   │   │
│   │   │   │   ├── providers/
│   │   │   │   │   ├── ProviderController.java
│   │   │   │   │   ├── ProviderService.java
│   │   │   │   │   ├── ProviderRepository.java
│   │   │   │   │   ├── model/
│   │   │   │   │   │   └── ProviderProfile.java
│   │   │   │   │   └── dto/
│   │   │   │   │       ├── ProviderDTO.java
│   │   │   │   │       └── ProviderSummaryDTO.java
│   │   │   │   │
│   │   │   │   ├── categories/
│   │   │   │   │   ├── CategoryController.java
│   │   │   │   │   ├── CategoryService.java
│   │   │   │   │   ├── CategoryRepository.java
│   │   │   │   │   └── model/
│   │   │   │   │       └── Category.java
│   │   │   │   │
│   │   │   │   ├── portfolio/
│   │   │   │   │   ├── PortfolioController.java
│   │   │   │   │   ├── PortfolioService.java
│   │   │   │   │   ├── PortfolioRepository.java
│   │   │   │   │   └── model/
│   │   │   │   │       └── PortfolioItem.java
│   │   │   │   │
│   │   │   │   ├── jobs/
│   │   │   │   │   ├── JobController.java
│   │   │   │   │   ├── JobService.java
│   │   │   │   │   ├── JobRepository.java
│   │   │   │   │   └── model/
│   │   │   │   │       ├── Job.java
│   │   │   │   │       └── JobStatus.java (enum)
│   │   │   │   │
│   │   │   │   ├── reviews/
│   │   │   │   │   ├── ReviewController.java
│   │   │   │   │   ├── ReviewService.java
│   │   │   │   │   ├── ReviewRepository.java
│   │   │   │   │   └── model/
│   │   │   │   │       └── Review.java
│   │   │   │   │
│   │   │   │   ├── search/
│   │   │   │   │   ├── SearchController.java
│   │   │   │   │   ├── SearchService.java
│   │   │   │   │   └── dto/
│   │   │   │   │       └── SearchFilterDTO.java
│   │   │   │   │
│   │   │   │   └── notifications/
│   │   │   │       ├── NotificationController.java
│   │   │   │       ├── NotificationService.java
│   │   │   │       ├── NotificationRepository.java
│   │   │   │       └── model/
│   │   │   │           └── Notification.java
│   │   │   │
│   │   │   ├── shared/
│   │   │   │   ├── exception/
│   │   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   │   └── UnauthorizedException.java
│   │   │   │   ├── response/
│   │   │   │   │   └── ApiResponse.java
│   │   │   │   └── utils/
│   │   │   │       ├── JwtUtils.java
│   │   │   │       └── FileUploadUtils.java
│   │   │   │
│   │   │   └── Application.java
│   │   │
│   │   └── resources/
│   │       ├── db/
│   │       │   └── migration/
│   │       │       ├── V1__create_users.sql
│   │       │       ├── V2__create_client_profiles.sql
│   │       │       ├── V3__create_provider_profiles.sql
│   │       │       ├── V4__create_categories.sql
│   │       │       ├── V5__create_portfolio.sql
│   │       │       ├── V6__create_jobs.sql
│   │       │       ├── V7__create_reviews.sql
│   │       │       └── V8__create_notifications.sql
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   │
│   └── test/
│       └── java/com/servifind/
│           ├── auth/
│           ├── providers/
│           ├── reviews/
│           └── search/
│
├── pom.xml
└── README.md
```

---

## 🗄️ Database Schema

### V1 — Users
```sql
CREATE TABLE users (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email         VARCHAR(255) UNIQUE NOT NULL,
    phone         VARCHAR(20) UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(20) NOT NULL CHECK (role IN ('CLIENT', 'PROVIDER', 'ADMIN')),
    is_active     BOOLEAN DEFAULT TRUE,
    created_at    TIMESTAMP DEFAULT NOW(),
    updated_at    TIMESTAMP DEFAULT NOW()
);
```

### V2 — Client Profiles
```sql
CREATE TABLE client_profiles (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    full_name   VARCHAR(255) NOT NULL,
    avatar_url  VARCHAR(500),
    city        VARCHAR(100),
    created_at  TIMESTAMP DEFAULT NOW()
);
```

### V3 — Provider Profiles
```sql
CREATE TABLE provider_profiles (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id           UUID UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    business_name     VARCHAR(255) NOT NULL,
    category_id       UUID REFERENCES categories(id),
    bio               TEXT,
    years_experience  INT,
    city              VARCHAR(100),
    avatar_url        VARCHAR(500),
    is_verified       BOOLEAN DEFAULT FALSE,
    avg_rating        DECIMAL(3,2) DEFAULT 0.0,
    total_reviews     INT DEFAULT 0,
    created_at        TIMESTAMP DEFAULT NOW()
);
```

### V4 — Categories
```sql
CREATE TABLE categories (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name_en     VARCHAR(100) NOT NULL,
    name_fr     VARCHAR(100),
    name_ar     VARCHAR(100),
    icon_url    VARCHAR(500),
    is_active   BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT NOW()
);
```

### V5 — Portfolio
```sql
CREATE TABLE portfolio_items (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    provider_id  UUID REFERENCES provider_profiles(id) ON DELETE CASCADE,
    image_url    VARCHAR(500) NOT NULL,
    description  TEXT,
    created_at   TIMESTAMP DEFAULT NOW()
);
```

### V6 — Jobs
```sql
CREATE TABLE jobs (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id     UUID REFERENCES users(id),
    provider_id   UUID REFERENCES users(id),
    description   TEXT,
    status        VARCHAR(20) DEFAULT 'PENDING'
                  CHECK (status IN ('PENDING','ACCEPTED','COMPLETED','CANCELLED')),
    created_at    TIMESTAMP DEFAULT NOW(),
    completed_at  TIMESTAMP
);
```

### V7 — Reviews
```sql
CREATE TABLE reviews (
    id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_id                UUID UNIQUE REFERENCES jobs(id),
    reviewer_id           UUID REFERENCES users(id),
    reviewed_id           UUID REFERENCES users(id),
    reviewer_type         VARCHAR(20) CHECK (reviewer_type IN ('CLIENT', 'PROVIDER')),

    -- Overall score
    rating_overall        SMALLINT CHECK (rating_overall BETWEEN 1 AND 5),

    -- Provider-specific sub-ratings (filled when CLIENT reviews PROVIDER)
    rating_quality        SMALLINT CHECK (rating_quality BETWEEN 1 AND 5),
    rating_punctuality    SMALLINT CHECK (rating_punctuality BETWEEN 1 AND 5),
    rating_communication  SMALLINT CHECK (rating_communication BETWEEN 1 AND 5),
    rating_price_fairness SMALLINT CHECK (rating_price_fairness BETWEEN 1 AND 5),

    -- Client-specific sub-ratings (filled when PROVIDER reviews CLIENT)
    rating_payment        SMALLINT CHECK (rating_payment BETWEEN 1 AND 5),
    rating_respect        SMALLINT CHECK (rating_respect BETWEEN 1 AND 5),

    comment               TEXT,
    created_at            TIMESTAMP DEFAULT NOW()
);
```

### V8 — Notifications
```sql
CREATE TABLE notifications (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    UUID REFERENCES users(id) ON DELETE CASCADE,
    type       VARCHAR(50) NOT NULL,
    message    TEXT NOT NULL,
    is_read    BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT NOW()
);
```

---

## 🔌 API Endpoints (MVP)

### 🔐 Auth — `/api/v1/auth`

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/register/client` | Register as client | ❌ |
| POST | `/register/provider` | Register as provider | ❌ |
| POST | `/login` | Login and get JWT token | ❌ |
| POST | `/logout` | Logout (invalidate token) | ✅ |
| POST | `/refresh-token` | Refresh JWT token | ✅ |

---

### 👤 Users — `/api/v1/users`

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| GET | `/me` | Get current user profile | ✅ |
| PUT | `/me` | Update current user profile | ✅ |
| PUT | `/me/avatar` | Upload/update avatar | ✅ |
| DELETE | `/me` | Delete account | ✅ |

---

### 🔧 Providers — `/api/v1/providers`

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| GET | `/` | List all providers (paginated) | ❌ |
| GET | `/{id}` | Get provider details | ❌ |
| PUT | `/me` | Update provider profile | ✅ PROVIDER |
| GET | `/{id}/reviews` | Get provider reviews | ❌ |
| GET | `/{id}/portfolio` | Get provider portfolio | ❌ |

---

### 📂 Categories — `/api/v1/categories`

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| GET | `/` | List all active categories | ❌ |
| POST | `/` | Create category | ✅ ADMIN |
| PUT | `/{id}` | Update category | ✅ ADMIN |
| DELETE | `/{id}` | Deactivate category | ✅ ADMIN |

---

### 🖼️ Portfolio — `/api/v1/portfolio`

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/` | Add portfolio item (upload image) | ✅ PROVIDER |
| DELETE | `/{id}` | Delete portfolio item | ✅ PROVIDER |

---

### 💼 Jobs — `/api/v1/jobs`

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/` | Create a job request | ✅ CLIENT |
| GET | `/my` | Get my jobs (client or provider) | ✅ |
| GET | `/{id}` | Get job details | ✅ |
| PATCH | `/{id}/accept` | Provider accepts job | ✅ PROVIDER |
| PATCH | `/{id}/complete` | Mark job as completed | ✅ PROVIDER |
| PATCH | `/{id}/cancel` | Cancel a job | ✅ |

---

### ⭐ Reviews — `/api/v1/reviews`

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/` | Submit a review for a completed job | ✅ |
| GET | `/provider/{id}` | Get all reviews for a provider | ❌ |
| GET | `/client/{id}` | Get all reviews for a client | ✅ |

---

### 🔍 Search — `/api/v1/search`

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| GET | `/providers` | Search providers with filters | ❌ |

**Query params:**
```
?keyword=plumber
&category=uuid
&city=Casablanca
&minRating=4
&page=0
&size=20
&sortBy=rating
```

---

### 🔔 Notifications — `/api/v1/notifications`

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| GET | `/` | Get my notifications | ✅ |
| PATCH | `/{id}/read` | Mark notification as read | ✅ |
| PATCH | `/read-all` | Mark all as read | ✅ |

---

## ⚙️ application.properties Configuration

### application-dev.properties
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/servifind_dev
spring.datasource.username=postgres
spring.datasource.password=yourpassword

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true

# JWT
jwt.secret=your_secret_key_here
jwt.expiration=86400000

# Cloudinary
cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret
```

### application-prod.properties
```properties
# Database (use environment variables in prod — never hardcode)
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USER}
spring.datasource.password=${DATABASE_PASSWORD}

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Flyway
spring.flyway.enabled=true

# JWT
jwt.secret=${JWT_SECRET}
jwt.expiration=86400000

# Cloudinary
cloudinary.cloud-name=${CLOUDINARY_CLOUD_NAME}
cloudinary.api-key=${CLOUDINARY_API_KEY}
cloudinary.api-secret=${CLOUDINARY_API_SECRET}
```

---

## 📦 pom.xml Dependencies

```xml
<dependencies>

    <!-- Spring Boot -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- PostgreSQL -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>

    <!-- Flyway -->
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-core</artifactId>
    </dependency>

    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
    </dependency>

    <!-- Cloudinary -->
    <dependency>
        <groupId>com.cloudinary</groupId>
        <artifactId>cloudinary-http44</artifactId>
        <version>1.36.0</version>
    </dependency>

    <!-- Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

</dependencies>
```

---

## 🚦 MVP Checklist

### Phase 1 — Core (Start here)
- [ ] Project setup + Flyway config
- [ ] Users table + Auth module (register/login/JWT)
- [ ] Categories (seed initial data)
- [ ] Provider profiles
- [ ] Client profiles
- [ ] Search & filter providers

### Phase 2 — Engagement
- [ ] Portfolio (image upload via Cloudinary)
- [ ] Jobs (request, accept, complete)
- [ ] Double-sided reviews with sub-ratings
- [ ] Notifications (job status changes, new review)

### Phase 3 — Polish
- [ ] Admin endpoints (verify providers, manage categories)
- [ ] Pagination & sorting on all list endpoints
- [ ] Input validation & error handling
- [ ] API documentation (Swagger/OpenAPI)
- [ ] Unit & integration tests

---

## 🔮 Future Features (Post-MVP)

- [ ] In-platform voice/video calls (WebRTC)
- [ ] Mobile app (React Native consuming same API)
- [ ] OAuth login (Google, Facebook)
- [ ] Elasticsearch for advanced search
- [ ] Real-time notifications (WebSocket)
- [ ] Migrate image storage to Cloudflare R2
- [ ] Microservices extraction (search, calls, notifications)
- [ ] Multi-country / multi-language support

---

## 👥 Team

| Role | Responsibility |
|---|---|
| Backend Developer | Spring Boot API, PostgreSQL, Flyway, Auth |
| Frontend Developer | Next.js, consuming REST API |
| Business | Monetization strategy, market research |

---

*README generated during project planning — update as the project evolves.*
