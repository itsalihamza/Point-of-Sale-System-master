# SG Technologies Point of Sale System

**Version:** Legacy Desktop Application  
**Last Updated:** December 6, 2025  
**Original Release:** December 9, 2015  
**Course:** CSE216 - Software Engineering  

---

## Overview

This is a desktop Point of Sale (POS) system built with Java Swing that supports sales transactions, rental management, item returns, employee management, and inventory tracking. The system uses plain text files for data storage and implements basic design patterns.

**Status:** Legacy system - Under reengineering for web-based deployment

---

## Table of Contents

1. [System Architecture](#system-architecture)
2. [Features](#features)
3. [Components](#components)
4. [Database Files](#database-files)
5. [Design Patterns](#design-patterns)
6. [Installation & Setup](#installation--setup)
7. [User Roles](#user-roles)
8. [Business Rules](#business-rules)
9. [Known Limitations](#known-limitations)
10. [Documentation](#documentation)

---

## System Architecture

### Architecture Overview

```
┌─────────────────────────────────────────────────────────┐
│                     Entry Point                         │
│                    Register.java                        │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│                   UI Layer (Swing)                      │
│  Login → Cashier/Admin → Transaction → Payment         │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│                 Business Logic Layer                    │
│  POSSystem, PointOfSale (POS/POR/POH), Management      │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│                 Data Access Layer                       │
│         Inventory (Singleton), File I/O                 │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│              Persistence Layer                          │
│            Plain Text Files (.txt)                      │
└─────────────────────────────────────────────────────────┘
```

**Layer Descriptions:**

- **UI Layer:** Java Swing interfaces for user interaction
- **Business Logic Layer:** Transaction processing, rental management, employee management
- **Data Access Layer:** File reading/writing operations
- **Persistence Layer:** Text-based storage (12 files)

---

## Features

### Core Functionality

#### For Cashiers:
- **Sales Transactions:** Process item purchases with automatic inventory updates
- **Rental Management:** Track item rentals with 14-day return period
- **Returns Processing:** 
  - Rented items return (with late fee calculation)
  - Unsatisfactory item returns (restocking)
- **Coupon Support:** Apply 10% discount coupons
- **Payment Processing:** Cash and credit card (16-digit validation)

#### For Administrators:
- **Employee Management:**
  - Add new employees (username, name, position, password)
  - Update employee information
  - View employee database
- **Access to all cashier functions**
- **Employee activity logging**

---

## Components

### Source Code Files (19 classes, ~2,954 LOC)

#### Entry Point
- **Register.java** (15 LOC) - Application entry point, launches login interface

#### Core Business Logic
- **POSSystem.java** (210 LOC) - Login authentication, employee database access, session management
- **PointOfSale.java** (246 LOC) - Abstract base class for transactions
  - **POS.java** (130 LOC) - Sale transaction implementation
  - **POR.java** (170 LOC) - Rental transaction implementation
  - **POH.java** (155 LOC) - Return transaction implementation
- **Management.java** (387 LOC) - Customer database management, rental tracking, late fee calculation
- **EmployeeManagement.java** (202 LOC) - Employee CRUD operations
- **Inventory.java** (120 LOC) - Singleton pattern for inventory management

#### Data Models
- **Employee.java** (25 LOC) - Employee data structure (username, name, position, password)
- **Item.java** (15 LOC) - Item data structure (ID, name, price, quantity)
- **ReturnItem.java** (15 LOC) - Return tracking structure (itemID, days since return)

#### User Interface (Swing)
- **Login_Interface.java** (95 LOC) - Authentication screen
- **Cashier_Interface.java** (120 LOC) - Cashier main menu
- **Admin_Interface.java** (110 LOC) - Admin main menu
- **Transaction_Interface.java** (250 LOC) - Transaction processing screen
- **EnterItem_Interface.java** (160 LOC) - Item entry dialog
- **Payment_Interface.java** (245 LOC) - Payment processing screen
- **AddEmployee_Interface.java** (150 LOC) - Employee creation dialog
- **UpdateEmployee_Interface.java** (135 LOC) - Employee update dialog

#### Tests
- **EmployeeTest.java** - Basic employee class unit test (minimal coverage <5%)

---

## Database Files

### Data Storage (12 plain text files)

| File | Format | Purpose | Sample Size |
|------|--------|---------|-------------|
| **employeeDatabase.txt** | Space-separated | Employee accounts | 12 employees |
| **itemDatabase.txt** | Space-separated | Inventory items | 102 items |
| **userDatabase.txt** | Complex nested | Customer rental history | 47+ customers |
| **couponNumber.txt** | Line-per-coupon | Valid coupon codes | 200 coupons |
| **employeeLogfile.txt** | Append-only | Login/logout activity | Growing |
| **saleInvoiceRecord.txt** | Append-only | Sales receipts | Growing |
| **returnSale.txt** | Append-only | Return records | Growing |
| **rentalDatabase.txt** | Space-separated | Active rentals | Variable |
| **temp.txt (1-3)** | Temporary | Transaction state recovery | Temporary |

### File Format Examples

**employeeDatabase.txt:**
```
username position firstName lastName password
110001 Admin Harry Larry 1
110002 Cashier Debra Cooper lehigh2016
```

**itemDatabase.txt:**
```
itemID itemName price quantity
1000 Potato 1.0 249
1002 SkirtSteak 15.0 1055
```

**userDatabase.txt:**
```
phoneNumber [itemID,rentDate,returned] [itemID,rentDate,returned] ...
6096515668 1000,6/30/09,true 1022,6/31/11,true
```

---

## Design Patterns

### 1. Singleton Pattern
**Class:** `Inventory.java`

**Purpose:** Ensures only one instance of Inventory exists throughout application lifecycle to prevent concurrent file access issues.

**Implementation:**
```java
public class Inventory {
    private static Inventory uniqueInstance = null;
    private Inventory() {}
    
    public static synchronized Inventory getInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new Inventory();
        return uniqueInstance;
    }
}
```

### 2. Abstract Factory Pattern (Partial Implementation)
**Classes:** `PointOfSale` (abstract) → `POS`, `POR`, `POH` (concrete)

**Purpose:** Provides common interface for different transaction types while allowing specialized behavior.

**Hierarchy:**
- `PointOfSale` defines template methods: `enterItem()`, `updateTotal()`, `coupon()`
- Abstract methods vary by type: `endPOS()`, `deleteTempItem()`, `retrieveTemp()`
- `POS` - Sale completion updates inventory
- `POR` - Rental completion creates customer rental record
- `POH` - Return completion calculates late fees and restocks

---

## Installation & Setup

### Prerequisites
- Java Development Kit (JDK) 7 or higher
- Apache Ant (for building with build.xml)
- NetBeans IDE (optional, project includes nbproject configuration)

### Build Instructions

**Using Ant:**
```bash
cd Point-of-Sale-System-master
ant compile
ant jar
```

**Using NetBeans:**
1. Open project in NetBeans
2. Clean and Build (Shift+F11)
3. Run Project (F6)

**Manual Compilation:**
```bash
cd src
javac -d ../bin *.java
cd ../bin
java Register
```

### Directory Structure
```
Point-of-Sale-System-master/
├── src/                    # Source code (.java files)
├── bin/                    # Compiled classes (.class files)
├── Database/               # Data files (.txt files)
├── Documentation/          # Project documentation
├── tests/                  # Unit tests
├── build.xml               # Ant build configuration
├── manifest.mf             # JAR manifest
└── README.md              # This file
```

### Initial Setup

**Default Admin Account:**
- Username: `110001`
- Password: `1`
- Position: Admin

**Default Cashier Account:**
- Username: `110002`
- Password: `lehigh2016`
- Position: Cashier

---

## User Roles

### Admin Role
**Permissions:**
- Full access to employee management (add, update, view)
- Access to all cashier functions
- Can process sales, rentals, and returns
- Activity is logged to `employeeLogfile.txt`

**Responsibilities:**
- Managing employee accounts
- System administration
- Employee oversight

### Cashier Role
**Permissions:**
- Process sales transactions
- Process rental transactions
- Process return transactions
- Apply coupon codes
- Customer lookup and management

**Restrictions:**
- Cannot access employee management
- Cannot modify employee data

---

## Business Rules

### Sales Transactions
1. Items must exist in `itemDatabase.txt`
2. Sufficient inventory quantity required
3. Tax rate: 6% (total multiplied by 1.06)
4. Coupon discount: 10% (before tax)
5. Payment methods: Cash or Credit Card (16 digits)
6. Inventory automatically decremented on sale completion
7. Sale recorded in `saleInvoiceRecord.txt`

### Rental Transactions
1. Customer phone number required (10 digits)
2. New customer created if not in database
3. Rental period: 14 days
4. Return date automatically calculated
5. Rental recorded in `userDatabase.txt`
6. Inventory decremented during rental period
7. Multiple items can be rented in one transaction

### Return Transactions

**Rented Item Return:**
1. Customer phone number lookup required
2. System calculates days late
3. Late fee: 10% of item price per day late
4. Item marked as returned in customer record
5. Inventory incremented on return
6. Return recorded in `returnSale.txt`

**Unsatisfactory Item Return:**
1. No customer lookup required
2. No late fees applied
3. Item restocked immediately
4. Return recorded in `returnSale.txt`

### Coupon Rules
1. 200 pre-generated coupon codes in `couponNumber.txt`
2. Single-use coupons (not enforced - current limitation)
3. 10% discount applied to subtotal before tax
4. One coupon per transaction
5. No coupon stacking

### Employee Management
1. Unique username required (application-enforced)
2. Position must be "Admin" or "Cashier"
3. Password stored in plain text (security vulnerability)
4. Employee name: first name + last name (no spaces in individual names)
5. Login/logout activity logged with timestamp

---

## Known Limitations

### Critical Issues
1. **Security:**
   - Plain text password storage (no encryption/hashing)
   - No input sanitization
   - No authentication timeout
   - No password complexity requirements

2. **Data Integrity:**
   - No primary key enforcement
   - No foreign key validation
   - Duplicate phone numbers possible
   - No transaction rollback capability
   - File corruption risk (no atomic writes)

3. **Concurrency:**
   - No file locking mechanism
   - Multi-user access not supported
   - Race conditions possible
   - No concurrent transaction handling

4. **Scalability:**
   - Sequential file scanning (O(n) lookups)
   - No indexing
   - Unbounded file growth
   - Single-user desktop application
   - Not web-accessible

### Functional Limitations
1. **Data Validation:**
   - No validation on file read operations
   - Invalid dates exist in data (e.g., 6/31/11)
   - No bounds checking on quantities/prices
   - Array index exceptions possible

2. **User Experience:**
   - No session recovery after crash
   - Temporary files not always cleaned up
   - Error messages printed to console (not visible in GUI)
   - Hard-coded UI dimensions (not responsive)
   - No internationalization support

3. **Business Logic:**
   - Coupon re-use not prevented
   - No reorder alerts for low inventory
   - No sales reporting/analytics
   - No backup/restore functionality
   - No audit trail (except login logs)

4. **Code Quality:**
   - Tight coupling between layers
   - Business logic mixed with UI code
   - Duplicate code (~300 lines)
   - Magic numbers throughout
   - Minimal test coverage (<5%)

---

## Workflows

### Sale Transaction Workflow
```
1. Cashier logs in
2. Clicks "Sale" button
3. System loads item database
4. Cashier enters item ID and quantity
5. System validates item exists and quantity available
6. System adds to transaction cart
7. System calculates running total
8. Repeat steps 4-7 for additional items
9. Cashier clicks "End Transaction"
10. System displays total with tax
11. (Optional) Apply coupon code
12. Cashier selects payment method
13. Cashier enters payment amount
14. System calculates change (if cash)
15. System updates inventory (decrement quantities)
16. System writes to saleInvoiceRecord.txt
17. System deletes temporary transaction file
18. Return to cashier menu
```

### Rental Transaction Workflow
```
1. Cashier logs in
2. Clicks "Rental" button
3. System prompts for customer phone number
4. System checks if customer exists in userDatabase.txt
5. If new customer, creates entry with phone number
6. System loads item database
7. Cashier enters item ID and quantity
8. System validates item exists
9. System adds to rental cart
10. System calculates rental total
11. Repeat steps 7-10 for additional items
12. Cashier clicks "End Transaction"
13. System displays total with tax
14. (Optional) Apply coupon code
15. Cashier processes payment
16. System calculates return date (today + 14 days)
17. System updates inventory (decrement)
18. System appends rental to customer record in userDatabase.txt
19. System writes to rentalDatabase.txt
20. Return to cashier menu
```

### Return (Rented Item) Workflow
```
1. Cashier logs in
2. Clicks "Return" button → "Rented Items"
3. System prompts for customer phone number
4. System looks up customer in userDatabase.txt
5. System retrieves all unreturned rentals
6. For each item:
   a. Calculate days since return date
   b. If late, calculate fee (10% × price × days)
   c. Add to return list
7. System displays items and late fees
8. Cashier confirms return
9. System marks items as returned in userDatabase.txt
10. System updates inventory (increment)
11. System records return in returnSale.txt
12. Return to cashier menu
```

---

## Configuration

### Hardcoded Values

| Configuration | Value | Location | Note |
|---------------|-------|----------|------|
| Tax Rate | 6% (1.06) | PointOfSale.java | Should be externalized |
| Coupon Discount | 10% (0.90) | PointOfSale.java | Should be externalized |
| Late Fee Rate | 10% per day | Management.java | Should be externalized |
| Rental Period | 14 days | POR.java | Should be externalized |
| Credit Card Length | 16 digits | Payment_Interface.java | Should support multiple formats |
| Phone Number Length | 10 digits | Various | Should be configurable |
| Database Paths | "Database/*.txt" | Various | Should use config file |

### File Paths
All database files expected in `Database/` subdirectory relative to execution location. No configuration file support.

---

## Error Handling

### Current Patterns
1. **File Not Found:** Prints to console, silent failure
2. **IO Exceptions:** Caught but often swallowed with empty catch blocks
3. **Parse Errors:** Some `NumberFormatException` handling, but incomplete
4. **Validation Errors:** Minimal validation, many unchecked assumptions

### Error Recovery
- **Temporary Files:** Used for transaction state (`temp.txt`)
- **Recovery Method:** `POSSystem.continueFromTemp()` attempts to reload interrupted transactions
- **Limitations:** Partial implementation, not always invoked

---

## Performance Characteristics

### Time Complexity
- **Employee Login:** O(n) - sequential scan of employee database
- **Item Lookup:** O(n) - sequential scan of item database
- **Customer Lookup:** O(n) - sequential scan of user database
- **Coupon Validation:** O(n) - sequential scan of coupon file
- **Inventory Update:** O(n) - read entire file, rewrite entire file

### Space Complexity
- **Item Database:** Loaded entirely into memory (List<Item>)
- **Employee Database:** Loaded entirely into memory (List<Employee>)
- **Transaction Cart:** Stored in memory during transaction
- **File I/O:** Buffered reading/writing

### Scalability Limits
- **Items:** Practical limit ~1,000 items (performance degradation)
- **Customers:** Practical limit ~500 customers (file parsing overhead)
- **Employees:** Practical limit ~100 employees
- **Transactions:** No limit, but files grow unbounded

---

## Testing

### Test Coverage
- **Unit Tests:** 1 test file (EmployeeTest.java)
- **Coverage:** <5% of codebase
- **Integration Tests:** None
- **UI Tests:** None
- **Performance Tests:** None

### Testing Gaps
1. No tests for transaction processing
2. No tests for file I/O operations
3. No tests for payment processing
4. No tests for inventory management
5. No tests for rental/return logic
6. No tests for UI components
7. No tests for error handling

---

## Documentation

### Available Documentation

**In Project:**
- This README.md
- Source code comments (minimal)
- REENGINEERING_REPORT.md (comprehensive technical analysis)

**In Documentation Folder:**
- Inception Phase documents
- Elaboration Phase documents
- Construction Phase documents
- Beta Release documentation
- Final Release documentation

### Documentation Gaps
- No API documentation (Javadoc)
- No user manual
- No administrator guide
- No troubleshooting guide
- No deployment guide
- No architecture documentation (now in REENGINEERING_REPORT.md)

---

## Development Guidelines

### Code Standards
- Java naming conventions
- File encoding: UTF-8
- Line endings: System-dependent
- Indentation: Mixed (2-4 spaces)
- No code formatter applied

### Design Principles
- Minimal use of design patterns
- Tight coupling between layers
- Limited separation of concerns
- Business logic in UI layer
- Data access scattered

---

## Maintenance

### Common Tasks

**Adding New Items:**
1. Edit `Database/itemDatabase.txt`
2. Add line: `itemID itemName price quantity`
3. Ensure itemID is unique
4. Restart application

**Adding New Employees:**
1. Use Admin Interface → Employee Management
2. Or manually edit `Database/employeeDatabase.txt`
3. Format: `username position firstName lastName password`

**Viewing Logs:**
1. Open `Database/employeeLogfile.txt`
2. Format: `name (username position) logs into POS System. Time: timestamp`

**Backup Data:**
1. Copy entire `Database/` folder
2. No automated backup
3. No point-in-time recovery

---

## Future Enhancements (Reengineering Roadmap)

### Phase 1: ✅ Complete
- Inventory analysis
- Document restructuring

### Phase 2: ✅ Complete
- Reverse engineering
- Code smell identification
- Data smell identification

### Phase 3: Planned (Code Restructuring)
- Refactor duplicate code
- Extract business logic from UI
- Implement proper layering
- Apply SOLID principles
- Remove code smells

### Phase 4: Planned (Data Restructuring)
- Design normalized database schema
- Migrate from text files to RDBMS
- Implement proper constraints
- Add indexing for performance

### Phase 5: Planned (Forward Engineering)
- Design web-based architecture
- Implement REST API
- Build modern web UI
- Add authentication/authorization
- Implement proper security

### Phase 6: Planned (Final Release)
- Comprehensive testing
- Security audit
- Performance optimization
- Production deployment
- User training materials

---

## Contact & Support

**Original Development Team:** SG Technologies  
**Course:** CSE216 - Software Engineering  
**Institution:** Lehigh University (implied)  
**Original Release Date:** December 9, 2015  

**Current Status:** Under reengineering for modern deployment  
**Last Documentation Update:** December 6, 2025  

---

## License

No license specified. Academic project.

---

## Appendix

### File Dependencies

**Register.java** depends on:
- Login_Interface.java

**Login_Interface.java** depends on:
- POSSystem.java
- Cashier_Interface.java
- Admin_Interface.java

**POSSystem.java** depends on:
- Employee.java
- employeeDatabase.txt
- employeeLogfile.txt

**Cashier_Interface.java** depends on:
- POSSystem.java
- Transaction_Interface.java

**Transaction_Interface.java** depends on:
- PointOfSale.java (and subclasses)
- Management.java
- EnterItem_Interface.java
- Payment_Interface.java

**PointOfSale.java** (and subclasses) depend on:
- Inventory.java
- Item.java
- itemDatabase.txt
- Various output files

**Management.java** depends on:
- ReturnItem.java
- userDatabase.txt

**EmployeeManagement.java** depends on:
- Employee.java
- employeeDatabase.txt

### Version History

| Version | Date | Notes |
|---------|------|-------|
| Alpha Release | Dec 9, 2015 | Initial release |
| Legacy System | 2015-2025 | No updates for 10 years |
| Reengineering | Dec 6, 2025 | Documentation updated, analysis complete |

---

**End of Documentation**
