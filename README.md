#  Splittwise - Smart Expense Sharing Application

<div align="center">

![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![React](https://img.shields.io/badge/React-18-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)

</div>

---

##**About The Project**

**Splittwise** is a full-stack expense sharing application that helps groups of friends track shared expenses, split bills, and manage debts efficiently. Whether you're planning a trip, sharing rent with roommates, or splitting dinner bills, Splittwise makes it easy to keep track of who owes whom.

###**Key Features**

| Feature | Description |
|---------|-------------|
|**Secure Authentication** | JWT-based authentication with email OTP verification |
|**Group Management** | Create groups, add/remove members, leave groups |
|**Expense Tracking** | Support for EQUAL, PERCENTAGE, and EXACT split types |
|**Smart Balance Calculation** | Real-time balance tracking and debt simplification |
|**Debt Simplification** | Minimizes transactions to settle debts |
|**Responsive UI** | Modern dashboard with mobile-first design |
|**Email Notifications** | OTP verification and expense alerts |

---

##**System Architecture**

```
┌─────────────────────────────────────────────────────────────────────────┐
│                              CLIENT                                     │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │                    React Frontend (Vite)                        │   │
│  │  • Dashboard • Group Management • Expense Tracking • Settlements │   │
│  └─────────────────────────────────────────────────────────────────┘   │
│                                    │                                    │
│                                    │ HTTPS/REST                         │
│                                    ▼                                    │
├─────────────────────────────────────────────────────────────────────────┤
│                           API GATEWAY                                   │
│                    Spring Boot REST Controllers                         │
├─────────────────────────────────────────────────────────────────────────┤
│                           SERVICE LAYER                                 │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐   │
│  │ AuthService  │ │GroupService  │ │ExpenseService│ │BalanceService│   │
│  └──────────────┘ └──────────────┘ └──────────────┘ └──────────────┘   │
├─────────────────────────────────────────────────────────────────────────┤
│                         DATA ACCESS LAYER                               │
│                      Spring Data JPA Repositories                       │
├─────────────────────────────────────────────────────────────────────────┤
│                         DATABASE                                        │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │                         MySQL 8.0                                │   │
│  │  Users │ Groups │ Expenses │ ExpenseSplits │ Settlements        │   │
│  └─────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────┘
```

---

##**Tech Stack**

### **Backend**
| Technology |  Purpose |
|------------|-----------------|
| Java | Core programming language |
| Spring Boot | Application framework |
| Spring Security | Authentication & Authorization |
| Spring Data JPA | Database operations |
| JWT  | Token-based authentication |
| MySQL | Primary database |
| Hibernate | 6.x | ORM framework |
| Maven | Build automation |
| Lombok  | Boilerplate code reduction |
| JavaMail | - | Email service (SMTP) |

### **Frontend**
| Technology | Purpose |
|------------|---------|
| React | UI framework |
| React Router DOM | Client-side routing |
| Axios | HTTP client |
| Vite | Build tool |
| CSS3  | Styling |

---

## **Project Structure**

```
splittwise/
├── backend/                          # Spring Boot Application
│   ├── src/main/java/com/tushar/split/
│   │   ├── config/                   # Configuration classes
│   │   │   ├── SecurityConfig.java   # Security configuration
│   │   │   ├── JwtFilter.java        # JWT authentication filter
│   │   │   └── CorsConfig.java       # CORS configuration
│   │   ├── controller/               # REST API endpoints
│   │   │   ├── AuthController.java
│   │   │   ├── GroupController.java
│   │   │   ├── ExpenseController.java
│   │   │   └── BalanceController.java
│   │   ├── dto/                      # Data Transfer Objects
│   │   │   ├── UserSummary.java
│   │   │   ├── GroupResponse.java
│   │   │   ├── ExpenseRequest.java
│   │   │   └── SettlementResponse.java
│   │   ├── model/                    # JPA Entities
│   │   │   ├── Users.java
│   │   │   ├── SplitGroups.java
│   │   │   ├── Expense.java
│   │   │   └── ExpenseSplit.java
│   │   ├── repo/                     # Repository interfaces
│   │   │   ├── UserRepo.java
│   │   │   ├── GroupRepo.java
│   │   │   ├── ExpenseRepo.java
│   │   │   └── ExpenseSplitRepo.java
│   │   ├── service/                  # Business logic
│   │   │   ├── AuthService.java
│   │   │   ├── GroupService.java
│   │   │   ├── ExpenseService.java
│   │   │   └── BalanceService.java
│   │   ├── exception/                # Custom exceptions
│   │   └── util/                     # Utility classes
│   └── src/main/resources/
│       ├── application.yml           # Application configuration
│       └── db/migration/             # Flyway migrations
│
└── frontend/                         # React Application
    ├── src/
    │   ├── components/               # React components
    │   │   ├── auth/                 # Login, Signup, OTP pages
    │   │   ├── dashboard/            # Dashboard, Sidebar, GroupDetail
    │   │   └── common/               # Reusable components
    │   ├── services/                 # API service layer
    │   │   ├── api.js                # Axios configuration
    │   │   └── auth.js               # Authentication service
    │   ├── styles/                   # CSS files
    │   ├── App.jsx                   # Main app component
    │   └── main.jsx                  # Entry point
    ├── public/                       # Static assets
    └── package.json                  # Dependencies
```

---

## **Database Schema**

### **Entity Relationships**

```
┌─────────┐     ┌─────────────┐     ┌──────────┐
│  Users  │────<│    Groups   │>────│ Expenses │
└─────────┘     └─────────────┘     └──────────┘
      │               │                   │
      │               │                   │
      ▼               ▼                   ▼
┌─────────────┐ ┌─────────────┐ ┌──────────────┐
│GroupMembers │ │ExpenseSplits│ │  Settlements │
└─────────────┘ └─────────────┘ └──────────────┘
```

### **Key Tables**

| Table | Description |
|-------|-------------|
| `users` | User accounts and authentication |
| `split_groups` | Groups created by users |
| `group_members` | Junction table (many-to-many) |
| `expenses` | Expense records |
| `expense_splits` | Who owes what for each expense |
| `settlements` | Recorded payments between users |
| `refresh_tokens` | JWT refresh token storage |

---

##  **Getting Started**

### **Prerequisites**

```bash
Java 17 or higher
MySQL 8.0 or higher
Node.js 18+ and npm
Git
```

### **Backend Setup**

```bash
# 1. Clone the repository
git clone https://github.com/yourusername/splittwise.git
cd splittwise/backend

# 2. Configure MySQL
mysql -u root -p
CREATE DATABASE splitwise_db;
CREATE USER 'splitwise_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON splitwise_db.* TO 'splitwise_user'@'localhost';
FLUSH PRIVILEGES;

# 3. Configure application.yml
# Update datasource username/password and email settings

# 4. Build and run
mvn clean install
mvn spring-boot:run
```

### **Frontend Setup**

```bash
# 1. Navigate to frontend
cd ../frontend

# 2. Install dependencies
npm install

# 3. Configure environment
# Create .env file
echo "VITE_API_URL=http://localhost:8080" > .env

# 4. Run the app
npm run dev
```

##**Debt Simplification Algorithm**

The algorithm minimizes the number of transactions needed to settle debts:

```
Input: Net balances of all members
Process:
  1. Separate creditors (positive) and debtors (negative)
  2. Sort both lists by amount (largest first)
  3. Match largest debtor with largest creditor
  4. Create transaction for the minimum amount
  5. Update remaining balances
  6. Repeat until all balances are zero

Output: Minimal set of transactions
```

**Example:**
```
Balances: Alice: +90, Bob: -30, Charlie: -30, David: -30
Result: Bob → Alice: 30, Charlie → Alice: 30, David → Alice: 30
```

---

## **Security Features**

- **JWT Authentication** - Stateless token-based authentication
- **Password Encryption** - BCrypt hashing
- **Email Verification** - OTP validation for new accounts
- **Role-Based Access** - Creator privileges for group operations
- **CORS Configuration** - Restricted to frontend origin
- **SQL Injection Prevention** - JPA parameterized queries

---

<div align="center">
  
**Made with ❤️ for easier expense sharing**

</div>
