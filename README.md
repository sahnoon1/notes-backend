# Notes App Backend (Spring Boot)

## Overview
A Notes app backend built with Spring Boot, supporting user registration, login, JWT authentication, CRUD operations for notes, and public sharing via unique links. Designed to work with a React frontend hosted on Vercel. Backend can be deployed on Render, Railway, Fly.io, Heroku, or similar cloud platforms.

---

## 1. Deployment URLs
- **Frontend (React, Vercel):** `<your-frontend-url>`
- **Backend (Spring Boot, cloud):** `<your-backend-api-url>`

---

## 2. High-Level Design Specs
### Key Routes & Request/Response Shapes
- **Register:** `POST /auth/register`
  - Request: `{ "email": "...", "password": "..." }`
  - Response: `{ "id": "...", "email": "..." }`
- **Login:** `POST /auth/login`
  - Request: `{ "email": "...", "password": "..." }`
  - Response: `{ "accessToken": "...", "refreshToken": "...", ... }`
- **Refresh Token:** `POST /auth/refresh`
  - Request: `{ "refreshToken": "..." }`
  - Response: `{ "accessToken": "..." }`
- **Create Note:** `POST /api/notes`
  - Request: `{ "title": "...", "content": "..." }`
  - Response: Note JSON
- **Get Notes:** `GET /api/notes`
  - Response: List of notes
- **Get Note:** `GET /api/notes/{id}`
  - Response: Note JSON
- **Update Note:** `PUT /api/notes/{id}`
  - Request: `{ "title": "...", "content": "...", "version": 0 }`
  - Response: Updated note or 409 Conflict
- **Delete Note:** `DELETE /api/notes/{id}`
- **Share Note:** `POST /api/notes/{id}/share`
  - Request: `{ "isPublic": true }`
  - Response: `{ "publicSlug": "...", "shareUrl": "..." }`
- **Public Note:** `GET /api/public/notes/{slug}`
  - Response: Note JSON

### Authentication Approach
- **JWT (JSON Web Token):**
  - Access token (short-lived, 15 min)
  - Refresh token (long-lived, 7 days)
  - Used for stateless, secure authentication and authorization

### Database Schema
- **users**
  - `id` (UUID)
  - `email` (unique)
  - `password_hash` (hashed)
  - `created_at` (timestamp)
- **notes**
  - `id` (UUID)
  - `owner_id` (FK to users)
  - `title`, `content`
  - `is_public` (boolean)
  - `public_slug` (unique, nullable)
  - `version` (int, for optimistic locking)
  - `created_at`, `updated_at` (timestamps)

### System-Level Failure Mode & Mitigation
- **Auth failure (expired/invalid token):** Returns 401. Mitigation: refresh token endpoint and short-lived access tokens.

---

## 3. CRUD Specs
### Endpoints & JSON Contracts
- See above for all routes and request/response shapes.

### Database Schema
- See above.

### Authentication Choice & Justification
- **JWT:** Stateless, scalable, secure for APIs.

### CRUD-Specific Failure Mode & Mitigation
- **Race condition/conflict on update:** Optimistic locking with `version` field. Returns 409 on conflict.

---

## 4. Demo Instructions
- Log in/register.
- Create, read, update, delete notes.
- Share a note and open the share URL in incognito to verify public access.

---

## 5. RAG Pipeline (Optional)
- Not implemented, but could use chunking, embeddings, vector store, retriever, and prompt evaluation for semantic search over notes.

---

## 6. Pledge & Interview
- All code is original and reproducible in a live screen-share or repo walkthrough.

---

## 7. Environment Configuration
Add the following to your `application.properties`:
```properties
spring.application.name=note
jwt.secret=your_super_secret_jwt_key
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
Replace values as needed for your deployment.

---

## 8. Deployment
- Deploy backend to Render, Railway, Fly.io, Heroku, etc.
- Deploy frontend to Vercel.
- Set environment variables for secrets and database.
- Enable CORS for your frontend domain in backend config.

---

## 9. Contact
For questions or demo, contact the developer or schedule a repo walkthrough.




Here’s a complete prompt you can use to generate a Next.js frontend for your Notes App:

Prompt:

Build a Next.js frontend for a Notes App that connects to a Spring Boot backend API. The app should support:

User registration and login (with JWT authentication).
Viewing a list of notes for the logged-in user.
Creating, editing, and deleting notes.
Optimistic locking for note updates (handle 409 conflict errors).
Sharing notes via a public link (show share URL, allow toggling sharing).
Viewing a shared note via its public link (no login required).
Refreshing JWT access tokens using a refresh token.
Responsive, modern UI (use Material UI or Tailwind CSS).
Store access token in memory (or localStorage with care); refresh token in HttpOnly cookie if possible.
Show error messages for 401 (unauthorized), 409 (conflict), and other common errors.
Connect to backend endpoints as described in the README.
Backend endpoints:

POST /auth/register
POST /auth/login
POST /auth/refresh
GET /api/notes
POST /api/notes
GET /api/notes/{id}
PUT /api/notes/{id}
DELETE /api/notes/{id}
POST /api/notes/{id}/share
GET /api/public/notes/{slug}
Include:

Login/register pages
Notes list page
Note editor page
Share button and public link display
Public note view page
Error handling and loading states
You can use this prompt with any AI code generator, or as a checklist for building your Next.js frontend!