# Nemra Backend

Spring Boot REST API for a service marketplace platform. The codebase is organized as a modular monolith with authentication, users, providers, categories, jobs, reviews, calls, and shared infrastructure.

## Tech Stack

- Java 21
- Spring Boot 3.3.x
- Spring Web
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- JWT (jjwt 0.12.6)
- Cloudflare R2 via AWS SDK S3 client
- Apache Tika
- LiveKit Server SDK (livekit-server 0.13.0)
- Lombok

## Architecture

The project follows a layered backend structure:

- `controller` handles HTTP routes.
- `service` contains business logic.
- `repository` handles persistence.
- `model` contains JPA entities.
- `dto` contains request/response payloads.
- `shared` contains utilities, wrappers, and exceptions.

`MapperToDTO.java` is the shared mapping layer used to convert entities into API DTOs.

## File Tree

```text
src/main/java/backend/nemra/
├── NemraApplication.java
├── config/
│   ├── CorsConfig.java
│   ├── JwtConfig.java
│   ├── R2Config.java
│   └── SecurityConfig.java
├── modules/
│   ├── auth/
│   │   ├── AuthController.java
│   │   ├── AuthRepository.java
│   │   ├── AuthService.java
│   │   ├── dto/
│   │   │   ├── AuthResponse.java
│   │   │   ├── LoginRequest.java
│   │   │   ├── RegisterClient.java
│   │   │   ├── RegisterProvider.java
│   │   │   └── RegisterRequest.java
│   │   └── model/
│   │       └── RefreshToken.java
│   ├── calls/
│   │   ├── CallController.java
│   │   ├── CallRepository.java
│   │   ├── CallService.java
│   │   ├── dto/
│   │   │   ├── CallRequest.java
│   │   │   └── CallResponse.java
│   │   └── model/
│   │       ├── Call.java
│   │       └── CallStatus.java
│   ├── categories/
│   │   ├── CategoryController.java
│   │   ├── CategoryRepository.java
│   │   ├── CategoryService.java
│   │   ├── dto/
│   │   │   ├── CategoryDTO.java
│   │   │   ├── CreateCategoryRequest.java
│   │   │   └── UpdateCategory.java
│   │   └── model/
│   │       └── Category.java
│   ├── jobs/
│   │   ├── JobController.java
│   │   ├── JobRepository.java
│   │   ├── JobService.java
│   │   ├── dto/
│   │   │   ├── CreateJobRequest.java
│   │   │   ├── JobCompletedDTO.java
│   │   │   ├── JobNotCompletedDTO.java
│   │   │   └── JobPendingDTO.java
│   │   └── model/
│   │       ├── Job.java
│   │       └── JobStatus.java
│   ├── media/
│   │   └── MediaService.java
│   ├── reviews/
│   │   ├── ReviewController.java
│   │   ├── ReviewRepository.java
│   │   ├── ReviewService.java
│   │   ├── dto/
│   │   │   ├── ClientReviewDTO.java
│   │   │   ├── CreateReviewDTO.java
│   │   │   ├── ProviderReviewDTO.java
│   │   │   └── ReviewResponseDTO.java
│   │   └── model/
│   │       ├── Review.java
│   │       └── ReviewerType.java
│   └── users/
│       ├── UserController.java
│       ├── UserRepository.java
│       ├── UserService.java
│       ├── dto/
│       │   ├── UpdateProfileRequest.java
│       │   └── UserDTO.java
│       ├── model/
│       │   ├── Role.java
│       │   └── User.java
│       ├── clients/
│       │   ├── ClientController.java
│       │   ├── ClientRepository.java
│       │   ├── ClientService.java
│       │   ├── dto/
│       │   │   └── ClientProfileDTO.java
│       │   └── model/
│       │       └── ClientProfile.java
│       └── providers/
│           ├── ProviderController.java
│           ├── ProviderRepository.java
│           ├── ProviderService.java
│           ├── dto/
│           │   ├── ProviderDTO.java
│           │   └── ProviderSummaryDTO.java
│           └── model/
│               └── ProviderProfile.java
├── shared/
│   ├── exception/
│   │   ├── ExceptionsHandler.java
│   │   └── MyBadRequest.java
│   ├── response/
│   │   └── ApiResponse.java
│   └── utils/
│       ├── MapperToDTO.java
│       └── jwtUtils.java
src/main/resources/db/migration/
├── V1__create_users.sql
├── V2__init_admin.sql
├── V3__create_category_table.sql
├── V4__create_categories.sql
├── V5__create_clients_table.sql
├── V6__create_providers_table.sql
├── V7__create_refresh_tokens.sql
├── V8__create_jobs_table.sql
├── V9__create_reviews_table.sql
├── V10__change_the_relations_from_users_to_clients_and_providers_table.sql
├── V11__make_the_ratings_between_0and5.sql
└── V12__create_calls_table.sql
```

## Security Model

`SecurityConfig` uses stateless JWT authentication.

- Public:
  - `POST /api/auth/register/**`
  - `POST /api/auth/login`
  - `POST /api/calls/webhook` (LiveKit webhook, verified by signature)
- Admin-restricted:
  - `GET|POST|PUT|DELETE /api/admin/**` require `ADMIN`
- Role-restricted (method-level `@PreAuthorize`):
  - category writes (`POST`, `PUT`, `DELETE /api/categories/**`) require `ADMIN`
  - job creation (`POST /api/jobs`) requires `CLIENT`
  - job acceptance and completion (`PATCH /api/jobs/{id}/accept`, `PATCH /api/jobs/{id}/complete`) require `PROVIDER`
  - provider profile update (`PUT /api/providers/me`) requires `PROVIDER`
- Authenticated:
  - all other routes

## Database Schema

The schema below matches the Flyway migrations in `src/main/resources/db/migration`.

### `users`

| Column | Type | Notes |
|---|---|---|
| `id` | `UUID` | Primary key, generated by `gen_random_uuid()` |
| `full_name` | `VARCHAR(255)` | Required |
| `phone` | `VARCHAR(20)` | Unique |
| `password_hash` | `VARCHAR(255)` | Required |
| `city` | `VARCHAR(255)` | Required |
| `role` | `VARCHAR(20)` | `CLIENT`, `PROVIDER`, or `ADMIN` |
| `is_active` | `BOOLEAN` | Default `true` |
| `created_at` | `TIMESTAMPTZ` | Default `NOW()` |
| `updated_at` | `TIMESTAMPTZ` | Default `NOW()` |

### `categories`

| Column | Type | Notes |
|---|---|---|
| `id` | `UUID` | Primary key |
| `name_en` | `VARCHAR(100)` | Nullable in schema |
| `name_fr` | `VARCHAR(100)` | Nullable in schema |
| `name_ar` | `VARCHAR(100)` | Required |
| `is_active` | `BOOLEAN` | Default `true` |
| `created_at` | `TIMESTAMP` | Default `NOW()` |

### `client_profiles`

| Column | Type | Notes |
|---|---|---|
| `id` | `UUID` | Primary key |
| `user_id` | `UUID` | Unique FK to `users(id)`, cascade delete |
| `full_name` | `VARCHAR(255)` | Required |
| `avatar_url` | `VARCHAR(500)` | Optional |
| `city` | `VARCHAR(100)` | Optional |
| `created_at` | `TIMESTAMP` | Default `NOW()` |

### `provider_profiles`

| Column | Type | Notes |
|---|---|---|
| `id` | `UUID` | Primary key |
| `user_id` | `UUID` | Unique FK to `users(id)`, cascade delete |
| `business_name` | `VARCHAR(255)` | Required |
| `category_id` | `UUID` | FK to `categories(id)` |
| `bio` | `TEXT` | Optional |
| `years_experience` | `INT` | Optional |
| `city` | `VARCHAR(100)` | Optional |
| `avatar_url` | `VARCHAR(500)` | Optional |
| `is_verified` | `BOOLEAN` | Default `false` |
| `avg_rating` | `DECIMAL(3,2)` | Default `0.0` |
| `total_reviews` | `INT` | Default `0` |
| `created_at` | `TIMESTAMP` | Default `NOW()` |

### `refresh_tokens`

| Column | Type | Notes |
|---|---|---|
| `id` | `UUID` | Primary key |
| `user_id` | `UUID` | FK to `users(id)`, cascade delete |
| `token` | `TEXT` | Unique |
| `created_at` | `TIMESTAMPTZ` | Default `NOW()` |

### `jobs`

| Column | Type | Notes |
|---|---|---|
| `id` | `UUID` | Primary key |
| `client_id` | `UUID` | FK to `client_profiles(id)` after migration `V10` |
| `provider_id` | `UUID` | FK to `provider_profiles(id)` after migration `V10` |
| `description` | `TEXT` | Optional in schema, validated in request DTO |
| `status` | `VARCHAR(20)` | `PENDING`, `ACCEPTED`, `COMPLETED`, or `CANCELLED` |
| `created_at` | `TIMESTAMP` | Default `NOW()` |
| `completed_at` | `TIMESTAMP` | Set when completed |

### `reviews`

| Column | Type | Notes |
|---|---|---|
| `id` | `UUID` | Primary key |
| `job_id` | `UUID` | FK to `jobs(id)` |
| `reviewer_id` | `UUID` | FK to `users(id)` |
| `reviewed_id` | `UUID` | FK to `users(id)` |
| `reviewer_type` | `VARCHAR(20)` | `CLIENT` or `PROVIDER` |
| `rating_overall` | `SMALLINT` | Constrained to `0..5` after migration `V11` |
| `rating_quality` | `SMALLINT` | `0..5`, provider review only |
| `rating_punctuality` | `SMALLINT` | `0..5`, provider review only |
| `rating_communication` | `SMALLINT` | `0..5`, provider review only |
| `rating_price_fairness` | `SMALLINT` | `0..5`, provider review only |
| `rating_payment` | `SMALLINT` | `0..5`, client review only |
| `rating_respect` | `SMALLINT` | `0..5`, client review only |
| `comment` | `TEXT` | Required |
| `created_at` | `TIMESTAMP` | Default `NOW()` |

### `calls`

| Column | Type | Notes |
|---|---|---|
| `id` | `UUID` | Primary key, generated by `gen_random_uuid()` |
| `caller_id` | `UUID` | FK to `client_profiles(id)` |
| `called_id` | `UUID` | FK to `provider_profiles(id)` |
| `room_name` | `TEXT` | Required; composed as `call-{clientUserId}-{providerUserId}` |
| `status` | `VARCHAR(20)` | `RINGING`, `ACCEPTED`, `FINISHED`, or `REFUSED`. Default `RINGING` |
| `created_at` | `TIMESTAMP` | Default `NOW()` |
| `finished_at` | `TIMESTAMP` | Set when the room finishes |

## API Endpoints

All non-list responses are wrapped in `ApiResponse` with the shape:

```json
{
  "message": "string",
  "data": {},
  "success": true
}
```

### Auth

| Method | Route | Auth | Request DTO | Response DTO |
|---|---|---|---|---|
| `POST` | `/api/auth/register/client` | Public | `RegisterClient` | `AuthResponse` |
| `POST` | `/api/auth/register/provider` | Public | `RegisterProvider` (`multipart/form-data`) | `AuthResponse` |
| `POST` | `/api/auth/login` | Public | `LoginRequest` | `AuthResponse` |
| `POST` | `/api/auth/refresh-token` | Bearer refresh token | No body | `AuthResponse` |
| `POST` | `/api/auth/logout` | Bearer refresh token | No body | No `data` payload |

### Users

| Method | Route | Auth | Request DTO | Response DTO |
|---|---|---|---|---|
| `GET` | `/api/users/me` | Authenticated | None | `UserDTO` |
| `PUT` | `/api/users/me` | Authenticated | `RegisterRequest` with `fullName`, `phoneNumber`, `password`, `city` | `UserDTO` |

### Providers

| Method | Route | Auth | Request DTO | Response DTO |
|---|---|---|---|---|
| `GET` | `/api/providers` | Authenticated | None | `ProviderSummaryDTO` list |
| `GET` | `/api/providers/{user_id}` | Authenticated | None | `ProviderDTO` |
| `PUT` | `/api/providers/me` | `PROVIDER` | `RegisterProvider` (`application/json`) | `ProviderDTO` |
| `GET` | `/api/providers/{provider_id}/reviews` | Authenticated | None | `ReviewResponseDTO` list |

### Categories

| Method | Route | Auth | Request DTO | Response DTO |
|---|---|---|---|---|
| `GET` | `/api/categories` | Authenticated | None | `CategoryDTO` list (raw, not wrapped in `ApiResponse`) |
| `POST` | `/api/categories` | `ADMIN` | `CreateCategoryRequest` | No `data` payload |
| `PUT` | `/api/categories/{category_id}` | `ADMIN` | `CreateCategoryRequest` | No `data` payload |
| `DELETE` | `/api/categories/{category_id}` | `ADMIN` | None | No `data` payload |

### Jobs

| Method | Route | Auth | Request DTO | Response DTO |
|---|---|---|---|---|
| `POST` | `/api/jobs` | `CLIENT` | `CreateJobRequest` | `JobPendingDTO` |
| `GET` | `/api/jobs/my` | Authenticated | None | Job DTO list (variant depends on status) |
| `GET` | `/api/jobs/{job_id}` | Authenticated | None | Job DTO (variant depends on status) |
| `PATCH` | `/api/jobs/{job_id}/accept` | `PROVIDER` | None | `JobNotCompletedDTO` |
| `PATCH` | `/api/jobs/{job_id}/complete` | `PROVIDER` | None | `JobCompletedDTO` |
| `PATCH` | `/api/jobs/{job_id}/cancel` | Authenticated | None | `JobPendingDTO` or `JobNotCompletedDTO` depending on state |

### Reviews

| Method | Route | Auth | Request DTO | Response DTO |
|---|---|---|---|---|
| `POST` | `/api/reviews` | Authenticated | `CreateReviewDTO` | `ReviewResponseDTO` |
| `GET` | `/api/reviews/provider/{id}` | Authenticated | None | `ProviderReviewDTO` list |
| `GET` | `/api/reviews/client/{id}` | Authenticated | None | `ClientReviewDTO` list |

### Calls

| Method | Route | Auth | Request DTO | Response DTO |
|---|---|---|---|---|
| `POST` | `/api/calls` | Authenticated | `CallRequest` | `CallResponse` |
| `POST` | `/api/calls/webhook` | Public (LiveKit signature) | Raw body + `Authorization` header | No body (`200 OK`) |

The `POST /api/calls` endpoint generates a LiveKit room token. The room name is derived as `call-{clientUserId}-{providerUserId}`. When the caller (CLIENT) joins, a 30-second timer starts; if the called (PROVIDER) does not join within that window, the call status transitions to `REFUSED`. The webhook endpoint listens for LiveKit room events (`room_finished`, `participant_joined`) and updates the `calls` record accordingly.

## DTO Reference

### Request DTOs

#### `RegisterRequest`

| Field | Type | Required | Notes |
|---|---|---:|---|
| `fullName` | `String` | Yes | User full name |
| `phoneNumber` | `String` | Yes | Phone used for login |
| `password` | `String` | Yes | Plain password sent by frontend |
| `city` | `String` | Yes | User city |

#### `RegisterClient`

Extends `RegisterRequest` with no additional fields. Used for `POST /api/auth/register/client`.

| Field | Type | Required | Notes |
|---|---|---:|---|
| `fullName` | `String` | Yes | Inherited from `RegisterRequest` |
| `phoneNumber` | `String` | Yes | Inherited from `RegisterRequest` |
| `password` | `String` | Yes | Inherited from `RegisterRequest` |
| `city` | `String` | Yes | Inherited from `RegisterRequest` |

#### `RegisterProvider`

Extends `RegisterRequest`. Used for `POST /api/auth/register/provider` (`multipart/form-data`) and `PUT /api/providers/me` (`application/json`).

| Field | Type | Required | Notes |
|---|---|---:|---|
| `fullName` | `String` | Yes | Inherited from `RegisterRequest` |
| `phoneNumber` | `String` | Yes | Inherited from `RegisterRequest` |
| `password` | `String` | Yes | Inherited from `RegisterRequest` |
| `city` | `String` | Yes | Inherited from `RegisterRequest` |
| `business_name` | `String` | Yes | Provider business name |
| `category` | `String` | Yes | Category name, matched case-insensitively |
| `bio` | `String` | No | Provider description |
| `years_of_experience` | `int` | No | Years of experience |
| `avatar` | `MultipartFile` | No | Multipart file upload (registration only) |

#### `LoginRequest`

| Field | Type | Required | Notes |
|---|---|---:|---|
| `number` | `String` | Yes | Phone number, length 10 to 13 |
| `password` | `String` | Yes | Plain password |

#### `CreateCategoryRequest`

| Field | Type | Required | Notes |
|---|---|---:|---|
| `nameAr` | `String` | Yes | Arabic category name |
| `nameFr` | `String` | Yes | French category name |
| `nameEn` | `String` | Yes | English category name |

#### `UpdateCategory`

Extends `CreateCategoryRequest`.

| Field | Type | Required | Notes |
|---|---|---:|---|
| `categoryId` | `String` | No | Optional extra field in DTO |
| `nameAr` | `String` | Yes | Inherited from `CreateCategoryRequest` |
| `nameFr` | `String` | Yes | Inherited from `CreateCategoryRequest` |
| `nameEn` | `String` | Yes | Inherited from `CreateCategoryRequest` |

#### `CreateJobRequest`

| Field | Type | Required | Notes |
|---|---|---:|---|
| `description` | `String` | Yes | 10 to 1000 characters |

#### `CreateReviewDTO`

| Field | Type | Required | Notes |
|---|---|---:|---|
| `jobId` | `UUID` | Yes | Target job id |
| `reviewedId` | `UUID` | Yes | User being reviewed |
| `reviewerType` | `String` | Yes | `CLIENT` or `PROVIDER` |
| `comment` | `String` | Yes | Review text |
| `ratingQuality` | `int` | No | Provider review only |
| `ratingPunctuality` | `int` | No | Provider review only |
| `ratingCommunication` | `int` | No | Provider review only |
| `ratingPriceFairness` | `int` | No | Provider review only |
| `ratingPayment` | `int` | No | Client review only |
| `ratingRespect` | `int` | No | Client review only |

#### `UpdateProfileRequest`

Defined in `users/dto/` but currently unused by controllers. `PUT /api/users/me` accepts `RegisterRequest` directly.

| Field | Type | Required | Notes |
|---|---|---:|---|
| `fullName` | `String` | No | Optional profile update field |
| `phoneNumber` | `String` | No | Optional profile update field |
| `password` | `String` | No | Optional password update field |
| `city` | `String` | No | Optional profile update field |

#### `CallRequest`

| Field | Type | Required | Notes |
|---|---|---:|---|
| `clientId` | `UUID` | Yes | Client profile id |
| `providerId` | `UUID` | Yes | Provider profile id |

### Response DTOs

#### `AuthResponse`

| Field | Type | Notes |
|---|---|---|
| `accessToken` | `String` | JWT access token |
| `refreshToken` | `String` | Refresh token |
| `user` | `UserDTO` | Logged-in/registered user payload |

#### `UserDTO`

| Field | Type | Notes |
|---|---|---|
| `user_id` | `UUID` | User id |
| `fullName` | `String` | Full name |
| `phone` | `String` | Phone number |
| `role` | `Role` | `ADMIN`, `CLIENT`, or `PROVIDER` |
| `city` | `String` | City |
| `createdAt` | `LocalDateTime` | Creation timestamp |

#### `ClientProfileDTO`

Extends `UserDTO`.

| Field | Type | Notes |
|---|---|---|
| `user_id` | `UUID` | Parent user id |
| `fullName` | `String` | Inherited from `UserDTO` |
| `phone` | `String` | Inherited from `UserDTO` |
| `role` | `Role` | Inherited from `UserDTO` |
| `city` | `String` | Inherited from `UserDTO` |
| `createdAt` | `LocalDateTime` | Inherited from `UserDTO` |
| `client_profile_id` | `UUID` | Client profile id |

#### `ProviderDTO`

| Field | Type | Notes |
|---|---|---|
| `user_id` | `UUID` | Parent user id |
| `fullName` | `String` | Provider full name |
| `phone` | `String` | Phone number |
| `role` | `Role` | Always `PROVIDER` |
| `city` | `String` | City |
| `createdAt` | `LocalDateTime` | Creation timestamp |
| `providerId` | `UUID` | Provider profile id |
| `businessName` | `String` | Provider business name |
| `category` | `CategoryDTO` | Nested category |
| `bio` | `String` | Provider bio |
| `yearsOfExperience` | `int` | Experience years |
| `isVerified` | `boolean` | Verification flag |
| `averageRating` | `double` | Average rating |
| `totalReviews` | `int` | Number of reviews |
| `avatarUrl` | `String` | Avatar path/url |

#### `ProviderSummaryDTO`

| Field | Type | Notes |
|---|---|---|
| `id` | `UUID` | Provider profile id |
| `fullName` | `String` | Provider full name |
| `businessName` | `String` | Business name |
| `category` | `CategoryDTO` | Nested category |
| `averageRating` | `double` | Average rating |
| `isVerified` | `boolean` | Verification flag |
| `avatarUrl` | `String` | Public avatar URL (prefixed with R2 public base URL) |

#### `CategoryDTO`

| Field | Type | Notes |
|---|---|---|
| `id` | `UUID` | Category id |
| `nameEn` | `String` | English name |
| `nameFr` | `String` | French name |
| `nameAr` | `String` | Arabic name |
| `isActive` | `boolean` | Active flag |
| `createdAt` | `LocalDateTime` | Creation timestamp |

#### `JobPendingDTO`

| Field | Type | Notes |
|---|---|---|
| `id` | `UUID` | Job id |
| `clientId` | `UUID` | Client profile id |
| `clientName` | `String` | Client full name |
| `description` | `String` | Job description |
| `status` | `JobStatus` | `PENDING`, `ACCEPTED`, `COMPLETED`, `CANCELLED` |
| `createdAt` | `LocalDateTime` | Creation timestamp |

#### `JobNotCompletedDTO`

Extends `JobPendingDTO`. Returned for `ACCEPTED` jobs and for `CANCELLED` jobs that already had a provider.

| Field | Type | Notes |
|---|---|---|
| `id` | `UUID` | Inherited from `JobPendingDTO` |
| `clientId` | `UUID` | Inherited from `JobPendingDTO` |
| `clientName` | `String` | Inherited from `JobPendingDTO` |
| `description` | `String` | Inherited from `JobPendingDTO` |
| `status` | `JobStatus` | Inherited from `JobPendingDTO` |
| `createdAt` | `LocalDateTime` | Inherited from `JobPendingDTO` |
| `providerId` | `UUID` | Provider profile id |
| `providerName` | `String` | Provider full name |

#### `JobCompletedDTO`

Extends `JobNotCompletedDTO`. Returned for `COMPLETED` jobs.

| Field | Type | Notes |
|---|---|---|
| `id` | `UUID` | Inherited from `JobNotCompletedDTO` |
| `clientId` | `UUID` | Inherited from `JobNotCompletedDTO` |
| `clientName` | `String` | Inherited from `JobNotCompletedDTO` |
| `description` | `String` | Inherited from `JobNotCompletedDTO` |
| `status` | `JobStatus` | Inherited from `JobNotCompletedDTO` |
| `createdAt` | `LocalDateTime` | Inherited from `JobNotCompletedDTO` |
| `providerId` | `UUID` | Inherited from `JobNotCompletedDTO` |
| `providerName` | `String` | Inherited from `JobNotCompletedDTO` |
| `completedAt` | `LocalDateTime` | Completion timestamp |

#### `ReviewResponseDTO`

| Field | Type | Notes |
|---|---|---|
| `id` | `UUID` | Review id |
| `reviewerName` | `String` | Reviewer full name |
| `reviewedName` | `String` | Reviewed user full name |
| `ratingOverall` | `int` | Overall rating |
| `comment` | `String` | Review comment |
| `createdAt` | `LocalDateTime` | Creation timestamp |

#### `ClientReviewDTO`

Extends `ReviewResponseDTO`.

| Field | Type | Notes |
|---|---|---|
| `id` | `UUID` | Inherited from `ReviewResponseDTO` |
| `reviewerName` | `String` | Inherited from `ReviewResponseDTO` |
| `reviewedName` | `String` | Inherited from `ReviewResponseDTO` |
| `ratingOverall` | `int` | Inherited from `ReviewResponseDTO` |
| `comment` | `String` | Inherited from `ReviewResponseDTO` |
| `createdAt` | `LocalDateTime` | Inherited from `ReviewResponseDTO` |
| `ratingPayment` | `int` | Payment rating |
| `ratingRespect` | `int` | Respect rating |

#### `ProviderReviewDTO`

Extends `ReviewResponseDTO`.

| Field | Type | Notes |
|---|---|---|
| `id` | `UUID` | Inherited from `ReviewResponseDTO` |
| `reviewerName` | `String` | Inherited from `ReviewResponseDTO` |
| `reviewedName` | `String` | Inherited from `ReviewResponseDTO` |
| `ratingOverall` | `int` | Inherited from `ReviewResponseDTO` |
| `comment` | `String` | Inherited from `ReviewResponseDTO` |
| `createdAt` | `LocalDateTime` | Inherited from `ReviewResponseDTO` |
| `ratingQuality` | `int` | Quality rating |
| `ratingPunctuality` | `int` | Punctuality rating |
| `ratingCommunication` | `int` | Communication rating |
| `ratingPriceFairness` | `int` | Price fairness rating |

#### `CallResponse`

| Field | Type | Notes |
|---|---|---|
| `token` | `String` | LiveKit JWT access token (TTL: 2 hours) |
| `url` | `String` | LiveKit server URL from config |

## MapperToDTO.java

`src/main/java/backend/nemra/shared/utils/MapperToDTO.java` centralizes entity-to-DTO conversion.

Current mappings:

- `ProviderProfile` -> `ProviderDTO`
- `ProviderProfile` -> `ProviderSummaryDTO` (requires public R2 URL for avatar)
- `ClientProfile` -> `ClientProfileDTO`
- `User` -> `UserDTO`
- `Category` -> `CategoryDTO`
- `Review` -> `ReviewResponseDTO`
- `Review` -> `ClientReviewDTO`
- `Review` -> `ProviderReviewDTO`
- `Job` -> `JobPendingDTO`, `JobNotCompletedDTO`, or `JobCompletedDTO` depending on status

Call entities are not mapped through `MapperToDTO`; the `CallResponse` is built inline in `CallService`.

## Notes

- `ClientController.java` currently has no endpoints.
- `SecurityConfig` permits `/api/uploads/**`, but there is no controller for it in the current tree.
- `PUT /api/users/me` accepts `RegisterRequest` (not `UpdateProfileRequest`). `UpdateProfileRequest` exists in `users/dto/` but is not currently wired to any controller.
- The `PUT /api/providers/me` update endpoint accepts `RegisterProvider` as `application/json`, not `multipart/form-data` (avatar upload only applies at registration).
- `CallService` uses a `ScheduledExecutorService` (10-thread pool, defined as a bean in `SecurityConfig`) to auto-refuse calls when the provider does not join within 30 seconds.
- LiveKit configuration (`livekit.api-key`, `livekit.api-secret`, `livekit.url`) is supplied via `application.properties` / environment variables.
