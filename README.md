# Nemra Monorepo

This repository now contains two separate apps:

- `backend/` Spring Boot API
- `frontend/` Next.js web app

Run the full stack with Docker Compose:

```bash
docker compose up --build
```

Ports:

- Frontend: `http://localhost:3000`
- Backend API: `http://localhost:8080`
- Postgres: `localhost:5432`
- LiveKit: `localhost:7880`

Each app also has its own Dockerfile:

- `backend/Dockerfile`
- `frontend/Dockerfile`
