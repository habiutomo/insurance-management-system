# Insurance Management System — Bank Danamon

Sistem manajemen asuransi `bancassurance` untuk perbankan yang dibangun dengan Spring Boot 3.2, JWT, dan MySQL.

## Fitur

- **Manajemen Nasabah** lengkap dengan NIK, NPWP, agama, pekerjaan, ahli waris, dan histori kontak.
- **Polis Asuransi** dengan underwriting, auto-debit, nominee/beneficiary, auto-renewal, dan status lengkap.
- **Klaim Agunan** dengan workflow status: `SUBMITTED ? UNDER_REVIEW ? APPROVED/REJECTED ? PAID`.
- **Pembayaran Premi** via auto-debit, transfer, virtual account.
- **Agen Bancassurance** dengan kode agen, cabang, supervisor, dan status aktif/non-aktif.
- **Komisi Agen** dengan tracking per polis dan status pembayaran.
- **Dashboard Statistik** untuk total nasabah, polis, klaim, agen, komisi, dan ringkasan underwriting.
- **Role-based access**: `ADMIN`, `AGENT`, `CUSTOMER`.
- **Autentikasi JWT** stateless untuk API.

## Tech Stack

- Java 17
- Spring Boot 3.2
- Spring Security
- Spring Data JPA
- MySQL
- JWT dengan library `jjwt 0.12.3`

## Prasyarat

- JDK 17
- Git
- Koneksi internet untuk mengunduh dependencies Maven

## Instalasi & Persiapan

1. Clone repository:

```bash
git clone https://github.com/habiutomo/insurance-management-system.git
cd insurance-management-system
```

2. Jalankan aplikasi:

```bash
./mvnw spring-boot:run
```

3. Akses aplikasi di:

```
http://localhost:8080
```

4. Akses MySQL Console:

```
http://localhost:8080/MySQL-console
```

- JDBC URL: `jdbc:MySQL:mem:insurance_db`
- User: `sa`
- Password: kosong

## Data Seed Otomatis

Saat pertama kali dijalankan, aplikasi akan membuat data awal berikut:

| User | Username | Password | Role |
|------|----------|----------|------|
| Admin Danamon | `admin` | `admin123` | `ADMIN` |
| Agen | `agent1` | `agent123` | `AGENT` |
| Nasabah | `customer1` | `cust123` | `CUSTOMER` |

Selain itu, sistem menambahkan beberapa agen, nasabah, dan polis contoh.

## Panduan Pengguna (User Guide)

### 1. Login sebagai Admin

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

Response akan berisi token JWT:

```json
{
  "token": "eyJhbGci..."
}
```

Simpan token untuk header Authorization:

```
Authorization: Bearer <token>
```

### 2. Lihat Dashboard Statistik

```bash
curl http://localhost:8080/api/dashboard/stats \
  -H "Authorization: Bearer $TOKEN"
```

### 3. Kelola Nasabah

#### Buat nasabah baru

```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Ahmad Fauzi",
    "email": "ahmad@email.com",
    "phone": "08123456789",
    "address": "Jl. Sudirman No. 10",
    "dateOfBirth": "1985-05-15",
    "placeOfBirth": "Jakarta",
    "gender": "LAKI_LAKI",
    "religion": "ISLAM",
    "nik": "3174010101900001",
    "npwp": "01.234.567.8-901.000",
    "occupation": "PNS",
    "maritalStatus": "MENIKAH",
    "motherMaidenName": "Siti Aminah",
    "nationality": "WNI"
  }'
```

#### Cari nasabah

```bash
curl "http://localhost:8080/api/customers/search?q=Ahmad" \
  -H "Authorization: Bearer $TOKEN"
```

### 4. Kelola Polis

#### Buat polis baru

```bash
curl -X POST http://localhost:8080/api/policies \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "policyType": "HEALTH",
    "premiumAmount": 500000,
    "coverageAmount": 50000000,
    "startDate": "2024-01-01",
    "endDate": "2024-12-31",
    "customerId": 1,
    "insuredName": "Ahmad Fauzi",
    "beneficiaryName": "Siti Nurhaliza",
    "beneficiaryRelationship": "Istri",
    "bankAccountNumber": "1234567890",
    "bankAccountName": "Ahmad Fauzi",
    "bankName": "Bank Danamon",
    "premiumPaymentMethod": "AUTO_DEBIT",
    "premiumFrequency": "BULANAN",
    "autoRenew": true,
    "branchOffice": "Danamon KCU Thamrin"
  }'
```

#### Update status underwriting

```bash
curl -X PATCH "http://localhost:8080/api/policies/1/underwriting?status=APPROVED" \
  -H "Authorization: Bearer $TOKEN"
```

### 5. Kelola Klaim

#### Buat klaim baru

```bash
curl -X POST http://localhost:8080/api/claims \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "policyId": 1,
    "claimAmount": 1000000,
    "claimReason": "Kecelakaan ringan",
    "claimDate": "2024-03-15"
  }'
```

#### Update status klaim

```bash
curl -X PATCH "http://localhost:8080/api/claims/1/status?status=APPROVED" \
  -H "Authorization: Bearer $TOKEN"
```

### 6. Kelola Pembayaran

#### Catat pembayaran

```bash
curl -X POST http://localhost:8080/api/payments \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "policyId": 1,
    "paymentAmount": 500000,
    "paymentMethod": "BANK_TRANSFER"
  }'
```

#### Tandai pembayaran lunas

```bash
curl -X PATCH "http://localhost:8080/api/payments/1/status?status=PAID" \
  -H "Authorization: Bearer $TOKEN"
```

### 7. Kelola Agen

#### Tambah agen baru

```bash
curl -X POST http://localhost:8080/api/agents \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Rudi Hartono",
    "email": "rudi@danamon.co.id",
    "branchOffice": "Danamon KCU Thamrin"
  }'
```

#### Nonaktifkan agen

```bash
curl -X PATCH "http://localhost:8080/api/agents/1/toggle-status" \
  -H "Authorization: Bearer $TOKEN"
```

### 8. Kelola Komisi Agen

#### Buat komisi

```bash
curl -X POST http://localhost:8080/api/commissions \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "agentId": 1,
    "policyId": 1,
    "commissionAmount": 50000,
    "commissionRate": 10.0
  }'
```

## API Endpoint Utama

### Auth (Publik)

| Method | Path | Keterangan |
|--------|------|------------|
| POST | `/api/auth/register` | Register user baru |
| POST | `/api/auth/login` | Login dan dapat token JWT |

### Profile (Semua user login)

| Method | Path | Keterangan |
|--------|------|------------|
| GET | `/api/profile` | Lihat profil sendiri |
| PUT | `/api/profile` | Update nama & email |
| PUT | `/api/profile/password` | Ganti password |

### Users (ADMIN only)

| Method | Path | Keterangan |
|--------|------|------------|
| GET | `/api/users` | Lihat semua user |
| POST | `/api/users` | Buat user baru |
| GET/PUT/DELETE | `/api/users/{id}` | CRUD user |

### Customers (ADMIN / AGENT)

| Method | Path | Keterangan |
|--------|------|------------|
| GET | `/api/customers` | Semua nasabah |
| GET | `/api/customers/search?q=keyword` | Cari nasabah |
| POST | `/api/customers` | Tambah nasabah |
| GET | `/api/customers/{id}` | Detail nasabah |
| PUT | `/api/customers/{id}` | Update nasabah |
| DELETE | `/api/customers/{id}` | Hapus nasabah |

### Policies (ADMIN / AGENT)

| Method | Path | Keterangan |
|--------|------|------------|
| GET | `/api/policies` | Semua polis |
| GET | `/api/policies/search?q=keyword` | Cari polis |
| POST | `/api/policies` | Buat polis baru |
| GET | `/api/policies/{id}` | Detail polis |
| PUT | `/api/policies/{id}` | Update polis |
| PATCH | `/api/policies/{id}/status?status=ACTIVE` | Update status polis |
| PATCH | `/api/policies/{id}/underwriting?status=APPROVED` | Update status underwriting |
| POST | `/api/policies/{id}/renew` | Perpanjang polis |
| GET | `/api/policies/customer/{customerId}` | Polis by nasabah |
| GET | `/api/policies/type/{type}` | Polis by tipe |
| DELETE | `/api/policies/{id}` | Hapus polis |

### Claims (ADMIN / AGENT)

| Method | Path | Keterangan |
|--------|------|------------|
| GET | `/api/claims` | Semua klaim |
| GET | `/api/claims/search?q=keyword` | Cari klaim |
| POST | `/api/claims` | Buat klaim baru |
| GET | `/api/claims/{id}` | Detail klaim |
| PATCH | `/api/claims/{id}/status?status=APPROVED` | Update status klaim |
| GET | `/api/claims/policy/{policyId}` | Klaim by polis |
| DELETE | `/api/claims/{id}` | Hapus klaim |

### Payments (ADMIN / AGENT)

| Method | Path | Keterangan |
|--------|------|------------|
| GET | `/api/payments` | Semua pembayaran |
| POST | `/api/payments` | Catat pembayaran |
| GET | `/api/payments/{id}` | Detail pembayaran |
| PATCH | `/api/payments/{id}/status?status=PAID` | Update status bayar |
| GET | `/api/payments/policy/{policyId}` | Bayaran per polis |
| DELETE | `/api/payments/{id}` | Hapus pembayaran |

### Agents (ADMIN / AGENT)

| Method | Path | Keterangan |
|--------|------|------------|
| GET | `/api/agents` | Semua agen |
| GET | `/api/agents/active` | Agen aktif saja |
| GET | `/api/agents/{id}` | Detail agen |
| POST | `/api/agents` | Tambah agen baru |
| PUT | `/api/agents/{id}` | Update agen |
| PATCH | `/api/agents/{id}/toggle-status` | Aktif/non-aktifkan agen |
| DELETE | `/api/agents/{id}` | Hapus agen |

### Commissions (ADMIN / AGENT)

| Method | Path | Keterangan |
|--------|------|------------ |
| GET | `/api/commissions` | Semua komisi |
| GET | `/api/commissions/{id}` | Detail komisi |
| GET | `/api/commissions/agent/{agentId}` | Komisi per agen |
| GET | `/api/commissions/policy/{policyId}` | Komisi per polis |
| POST | `/api/commissions` | Buat komisi baru |
| PATCH | `/api/commissions/{id}/status?status=PAID` | Update status komisi |

## Build dan Deployment

### Package aplikasi

```bash
./mvnw clean package
```

### Deploy ke GitHub Packages

Workflow publish sudah ditambahkan di `.github/workflows/publish.yml`.

### Run tests

```bash
./mvnw test
```

## Catatan

- Jika upgrade Java ke versi lebih tinggi, perbarui properti `<java.version>` di `pom.xml`.
- Dokumen ini menggunakan endpoint dan fitur API yang tersedia di aplikasi saat ini.

## Lisensi

- (Tambahkan informasi lisensi di sini)

## Hubungi

- Repository: https://github.com/habiutomo/insurance-management-system


