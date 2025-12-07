# Elaboration Phase - Design Specifications

## Design Overview

**Phase:** Elaboration  
**Focus:** Architecture design, use case refinement, risk mitigation  
**Deliverables:** SAD, class diagrams, sequence diagrams, domain models  
**Status:** Completed

---

## System Architecture

### Layered Architecture (4 Layers)

```
┌─────────────────────────────────────┐
│     Presentation Layer (Swing)       │
│  Login, Admin, Cashier, Transaction  │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│      Business Logic Layer           │
│  POSSystem, Management, Inventory   │
│  PointOfSale, EmployeeManagement    │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│       Data Access Layer             │
│   File I/O scattered across classes │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│      Persistence Layer              │
│    12 text files (.txt format)      │
└─────────────────────────────────────┘
```

---

## Use Cases Designed

### UC-1: Process Sale
**Primary Actor:** Cashier  
**Preconditions:** User logged in, items in inventory  
**Main Flow:**
1. Cashier scans/enters item ID
2. System retrieves item details
3. System calculates subtotal
4. Cashier applies coupon (optional)
5. System calculates tax (6.5%)
6. System displays total
7. Customer provides payment
8. System processes payment
9. System updates inventory
10. System prints receipt

**Alternate Flows:**
- 2a. Item not found → Display error
- 4a. Invalid coupon → Reject and continue
- 7a. Insufficient payment → Request additional payment

**Postconditions:** Sale recorded, inventory updated

---

### UC-2: Process Rental
**Primary Actor:** Cashier  
**Preconditions:** Customer registered, item available  
**Main Flow:**
1. Cashier enters customer phone number
2. System retrieves customer history
3. Cashier selects rental item
4. System checks availability
5. System sets due date (14 days)
6. System processes rental fee
7. System updates rental database
8. System decrements inventory

**Postconditions:** Rental recorded, inventory reserved

---

### UC-3: Handle Return
**Primary Actor:** Cashier  
**Preconditions:** Active rental exists  
**Main Flow:**
1. Cashier enters customer phone
2. System displays active rentals
3. Cashier selects returned item
4. System calculates late fees (if applicable)
5. System processes late fee payment
6. System marks rental as returned
7. System increments inventory

**Late Fee Calculation:** $1.50/day after 14-day period

---

## Class Diagrams

### Core Classes

**PointOfSale (Abstract Factory)**
- `+ getTransaction(type: String): PointOfSale`
- `+ setStatus(status: boolean): void`
- Creates: POS, POR, POH instances

**Inventory (Singleton)**
- `- instance: Inventory`
- `+ getInstance(): Inventory`
- `+ accessInventory(): String[][]`
- `+ updateInventory(items: String[][]): void`
- `+ isInStock(itemId: int, qty: int): boolean`

**Management (God Class)**
- `+ getLatestReturnDate(phone: String): String`
- `+ addNewCustomer(phone: String, rental: String): void`
- `+ updateRentalDatabase(): void`
- `+ calculateLateFee(daysLate: int): double`
- *~150 lines of rental logic*

**Employee (Model)**
- `- username: String`
- `- position: String`
- `- firstName: String`
- `- lastName: String`
- `- password: String`

**Item (Model)**
- `- itemId: int`
- `- itemName: String`
- `- price: float`
- `- quantity: int`

---

## Sequence Diagrams

### Sale Transaction Sequence

```
Cashier → Transaction_Interface: enterItems()
Transaction_Interface → POSSystem: processTransaction()
POSSystem → PointOfSale: getTransaction("Sale")
PointOfSale → POS: new POS()
POS → Inventory: accessInventory()
Inventory → itemDatabase.txt: read()
Inventory → POS: items[][]
POS → Payment_Interface: processPayment(total)
Payment_Interface → POS: paymentConfirmed
POS → Inventory: updateInventory(items)
Inventory → itemDatabase.txt: write()
POS → saleInvoiceRecord.txt: recordSale()
POS → Transaction_Interface: success
```

---

### Rental Processing Sequence

```
Cashier → Cashier_Interface: startRental()
Cashier_Interface → Management: getLatestReturnDate(phone)
Management → userDatabase.txt: read()
Management → Cashier_Interface: rentalHistory
Cashier_Interface → POR: new POR()
POR → Inventory: checkAvailability(itemId)
Inventory → POR: available
POR → Management: addNewCustomer(phone, rental)
Management → userDatabase.txt: append()
Management → rentalDatabase.txt: append()
POR → Inventory: updateInventory()
POR → Cashier_Interface: rentalComplete
```

---

## Domain Models

### Sales Domain
- **Entities:** Item, Transaction, Payment, Receipt
- **Relationships:** 
  - Transaction contains 1+ Items
  - Transaction has 1 Payment
  - Transaction generates 1 Receipt

### Rental Domain
- **Entities:** Customer, Item, Rental, LateFee
- **Relationships:**
  - Customer has N Rentals
  - Rental references 1 Item
  - Rental may have 1 LateFee

### Employee Domain
- **Entities:** Employee, Role, AuditLog
- **Relationships:**
  - Employee has 1 Role (Admin/Cashier)
  - Employee generates N AuditLog entries

---

## Design Patterns Applied

| Pattern | Usage | Location | Status |
|---------|-------|----------|--------|
| **Singleton** | Single inventory instance | Inventory.java | ✅ Implemented |
| **Abstract Factory** | Transaction type creation | PointOfSale.java | ⚠️ Partial |
| **Template Method** | Transaction processing | - | ❌ Not implemented |
| **Strategy** | Payment methods | - | ❌ Not implemented |
| **Observer** | Inventory updates | - | ❌ Not implemented |
| **Repository** | Data access layer | - | ❌ Not implemented |

---

## Non-Functional Requirements

| Requirement | Target | Achieved | Status |
|-------------|--------|----------|--------|
| Response Time | <500ms | 150-800ms | ✅ Met |
| Concurrent Users | 5 | 1 | ❌ Not supported |
| Availability | 99% | N/A | - |
| Data Integrity | 100% | ~85% | ⚠️ Partial |
| Security | Encrypted passwords | Plain text | ❌ Not met |
| Usability | <5 clicks for common tasks | 3-6 clicks | ✅ Met |

---

## Risk Mitigation

| Risk | Impact | Mitigation Strategy | Result |
|------|--------|---------------------|--------|
| Text file corruption | High | Backup before write | ✅ Implemented |
| Invalid data entry | Medium | Input validation | ⚠️ Partial |
| Performance degradation | Medium | Limit file size | ❌ Not implemented |
| Security breach | High | Access control | ⚠️ Basic only |
| Data loss | Critical | Daily backups | ❌ Manual process |

---

## Deployment Model

```
Client Machine (Desktop)
├── Java Runtime Environment (JRE 8+)
├── POS Application (JAR)
└── Database Folder (12 .txt files)
    ├── Read/Write permissions required
    └── Local filesystem access
```

**No network required** - Standalone desktop application

---

## Component Dependencies

See Package Diagram (Package Diagram.png) for visual representation.

**Key Dependencies:**
- All UI → POSSystem (controller)
- POSSystem → Management, Inventory, EmployeeManagement
- Transaction classes → Inventory (stock updates)
- Management → 4 text files (data persistence)

**Circular Dependencies Identified:**
- Management ↔ Inventory (needs refactoring)

---

## Database Schema (Text File Format)

### employeeDatabase.txt
```
username position firstName lastName password
110001 Admin Harry Larry 1
220002 Cashier John Doe password123
```

### itemDatabase.txt
```
itemID itemName price quantity
1000 Potato 1.0 249
1001 Tomato 2.5 150
```

### userDatabase.txt (Nested Format)
```
phoneNumber [itemID,date,returned] [itemID,date,returned] ...
6096515668 1000,6/30/09,true 1022,6/31/11,true
```

**Issue:** Unnormalized data structure violates 1NF

---

## Design Decisions Log

**Decision 1:** Use text files instead of database  
**Date:** Week 1  
**Rationale:** Simplicity, rapid prototyping  
**Consequences:** Performance and scalability issues

**Decision 2:** Singleton pattern for Inventory  
**Date:** Week 2  
**Rationale:** Single source of truth  
**Consequences:** Global state, testing difficulties

**Decision 3:** Abstract factory for transactions  
**Date:** Week 3  
**Rationale:** Separate sale/rental/return logic  
**Consequences:** Incomplete implementation, code duplication

---

## Elaboration Phase Artifacts

| Document | Status | Location |
|----------|--------|----------|
| Software Architecture Document (SAD) | ✅ Complete | SAD.docx, SAD(2).docx, SAD(3).docx |
| Use Case Specifications | ✅ Complete | Multiple use cases designed |
| Class Diagrams | ✅ Complete | Sales, Rental, Return domains |
| Sequence Diagrams | ✅ Complete | Major workflows documented |
| Domain Models | ✅ Complete | Three domains modeled |
| Activity Diagrams | ✅ Complete | Process flows visualized |
| Deployment Diagram | ✅ Complete | Desktop deployment model |
| Package Diagram | ✅ Complete | Component dependencies |

---

## Next Phase (Construction)

**Ready to implement:**
- Core architecture defined
- All major use cases specified
- Design patterns selected
- Component interfaces established
- Data structures designed

**Risks carried forward:**
- Text file scalability concerns
- Circular dependency between Management/Inventory
- Missing validation layer
- No comprehensive error handling design
