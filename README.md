# SpendWise — Personal Expense Tracker
SpendWise is an **Expense Tracking Backend** built using **Java, Spring Boot, JWT Security, MySQL/H2, JPA, and Swagger**.

It provides secure authentication, transaction management, category-based spending control, summaries, and a clean REST API.

---

# Features

### Authentication
- User Signup
- Login with JWT Access Token
- Refresh Token (HttpOnly cookie)
- Logout (refresh token invalidation)
- BCrypt password hashing
- Fully stateless authentication

### User Profile
- Profile auto-created on signup
- 1:1 mapping with AuthUser
- Update and retrieve profile

### Transactions
- Create / Update / Delete
- Get by ID
- Pagination & Sorting
- Dynamic filtering (JPA Specification)
- Weekly and Monthly history
- Recent transactions (last 7 days)
- Secure per-user access

### Dashboard
- Total Income
- Total Expense
- Balance
- Recent transactions
- Uses optimized repository queries

### Category Limits
- Create & Update spending limits
- Get category limits
- Enable category wise spending limits

### Category Summary
- Monthly expense grouping
- Amount spent per category
- Percentage calculation

### Swagger UI
- Interactive REST API documentation
- JWT Authorize button
- Clean grouped endpoints

---

# Authentication Flow (JWT + Refresh Token)

1. User logs in → Access token + Refresh token cookie
2. User accesses secured endpoints with `Bearer <token>`
3. If token expires → frontend calls `/auth/refresh`
4. New access token is generated
5. Logout removes refresh token from DB

---

# Access REST API endpoints

Run the application and open:

``` http://<host_name>/swagger-ui/index.html ```

# Tech Stack

| Category | Technology |
|---------|------------|
| Language | Java 17 |
| Framework | Spring Boot 3 |
| Security | Spring Security 6, JWT, Refresh Tokens |
| Database | **MySQL (dev)** / **H2 In-Memory (prod)** |
| ORM | Hibernate, Spring Data JPA |
| Documentation | SpringDoc OpenAPI (Swagger UI) |
| Build Tool | Maven |