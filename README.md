# Insurance Management System

REST API untuk mengelola data asuransi (nasabah, polis, klaim, pembayaran) menggunakan Spring Boot 3.2 + JWT + H2 Database.

## Tech Stack

- **Java 17**, Spring Boot 3.2, Spring Security, Spring Data JPA
- **H2 Database** (in-memory)
- **JWT** (jjwt 0.12.3) untuk autentikasi

## Menjalankan Aplikasi

```bash
./mvnw spring-boot:run
```

Aplikasi berjalan di `http://localhost:8080`.

## Autentikasi

1. **Register** `POST /api/auth/register`
```json
{ "username": "admin", "password": "admin123", "email": "admin@test.com", "fullName": "Admin", "role": "ROLE_ADMIN" }
```

2. **Login** `POST /api/auth/login`
```json
{ "username": "admin", "password": "admin123" }
```
Response: `{ "token": "eyJhbGci..." }`

3. Gunakan token di header: `Authorization: Bearer eyJhbGci...`

## Roles & Akses

| Role | Akses |
|------|-------|
| `ROLE_ADMIN` | Semua endpoint, termasuk manajemen user |
| `ROLE_AGENT` | Customers, Policies, Claims, Payments, Dashboard |
| `ROLE_CUSTOMER` | Hanya profile sendiri |

## Endpoints

### Auth (publik)
| Method | Path | Body |
|--------|------|------|
| POST | `/api/auth/register` | `{ username, password, email, fullName, role? }` |
| POST | `/api/auth/login` | `{ username, password }` |

### Profile (user yang login)
| Method | Path | Body |
|--------|------|------|
| GET | `/api/profile` | - |
| PUT | `/api/profile` | `{ fullName, email }` |
| PUT | `/api/profile/password` | `{ currentPassword, newPassword }` |

### Users (ADMIN only)
| Method | Path | Body |
|--------|------|------|
| GET | `/api/users` | - |
| POST | `/api/users` | `{ username, password, email, fullName, role }` |
| GET | `/api/users/{id}` | - |
| PUT | `/api/users/{id}` | `{ username, password, email, fullName, role, enabled }` |
| DELETE | `/api/users/{id}` | - |

### Customers (ADMIN / AGENT)
| Method | Path | Body |
|--------|------|------|
| GET | `/api/customers` | - |
| GET | `/api/customers/search?q=keyword` | - |
| POST | `/api/customers` | `{ fullName, email, phone?, address?, dateOfBirth?, idCardNumber? }` |
| GET | `/api/customers/{id}` | - |
| PUT | `/api/customers/{id}` | `{ fullName, email, phone?, address?, dateOfBirth?, idCardNumber? }` |
| DELETE | `/api/customers/{id}` | - |

### Policies (ADMIN / AGENT)
| Method | Path | Body |
|--------|------|------|
| GET | `/api/policies` | - |
| GET | `/api/policies/search?q=keyword` | - |
| POST | `/api/policies` | `{ policyType, premiumAmount, coverageAmount, startDate, endDate, customerId }` |
| GET | `/api/policies/{id}` | - |
| PUT | `/api/policies/{id}` | `{ policyType, premiumAmount, coverageAmount, startDate, endDate, customerId }` |
| PATCH | `/api/policies/{id}/status?status=ACTIVE` | - |
| POST | `/api/policies/{id}/renew` | `{ startDate, endDate, premiumAmount }` |
| GET | `/api/policies/customer/{customerId}` | - |
| GET | `/api/policies/type/{type}` | - |
| DELETE | `/api/policies/{id}` | - |

**PolicyType:** `AUTO`, `HEALTH`, `LIFE`, `PROPERTY`, `TRAVEL`
**PolicyStatus:** `ACTIVE`, `EXPIRED`, `CANCELLED`, `PENDING`

### Claims (ADMIN / AGENT)
| Method | Path | Body |
|--------|------|------|
| GET | `/api/claims` | - |
| GET | `/api/claims/search?q=keyword` | - |
| POST | `/api/claims` | `{ incidentDate, claimAmount, description?, policyId }` |
| GET | `/api/claims/{id}` | - |
| PATCH | `/api/claims/{id}/status?status=APPROVED` | - |
| GET | `/api/claims/policy/{policyId}` | - |
| DELETE | `/api/claims/{id}` | - |

**ClaimStatus:** `SUBMITTED`, `UNDER_REVIEW`, `APPROVED`, `REJECTED`, `PAID`

### Payments (ADMIN / AGENT)
| Method | Path | Body |
|--------|------|------|
| GET | `/api/payments` | - |
| POST | `/api/payments` | `{ amount, paymentDate, paymentMethod, policyId }` |
| GET | `/api/payments/{id}` | - |
| PATCH | `/api/payments/{id}/status?status=PAID` | - |
| GET | `/api/payments/policy/{policyId}` | - |
| DELETE | `/api/payments/{id}` | - |

**PaymentMethod:** `BANK_TRANSFER`, `CREDIT_CARD`, `DEBIT_CARD`, `CASH`, `E_WALLET`
**PaymentStatus:** `PENDING`, `PAID`, `OVERDUE`, `CANCELLED`

### Dashboard
| Method | Path | Role |
|--------|------|------|
| GET | `/api/dashboard/stats` | ADMIN / AGENT |

### H2 Console
`http://localhost:8080/h2-console` — JDBC URL: `jdbc:h2:mem:insurance_db`, User: `sa`, Password: (kosong)

## Contoh Penggunaan

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"admin\",\"password\":\"admin123\"}"

# Simpan token, lalu:
curl http://localhost:8080/api/customers ^
  -H "Authorization: Bearer TOKEN"
```
