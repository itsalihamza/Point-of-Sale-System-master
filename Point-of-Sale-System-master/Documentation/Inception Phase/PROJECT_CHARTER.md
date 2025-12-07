# Inception Phase - Project Charter

## Project Information

**Project Name:** Point-of-Sale System  
**Organization:** SG Technologies  
**Project Duration:** 12 weeks  
**Team Size:** 3 members  
**Start Date:** September 2015  
**Status:** Completed

---

## Vision Statement

Develop a **desktop Point-of-Sale (POS) system** for small retail businesses to manage sales, inventory, rentals, and employee operations with an intuitive interface and robust transaction processing.

**Target Users:**
- Small retail stores (1-5 locations)
- Rental businesses
- Inventory-based businesses

**Key Differentiators:**
- Simple, no-training-required interface
- Rental management built-in
- Low cost (no database licensing)
- Standalone desktop application

---

## Business Objectives

| Objective | Success Criteria | Status |
|-----------|------------------|--------|
| Replace manual paper-based system | 100% digital transactions | ✅ Achieved |
| Reduce transaction time | <2 minutes per sale | ✅ Achieved (avg 90 sec) |
| Track inventory automatically | Real-time updates | ✅ Achieved |
| Support rental operations | 14-day rental tracking | ✅ Achieved |
| Enable basic reporting | Transaction history logs | ⚠️ Partial (no analytics) |
| Multi-user support | Admin + Cashiers | ✅ Achieved |

---

## Scope

### In Scope
✅ **Transaction Management**
- Process sales (cash/credit card)
- Process rentals (14-day period)
- Handle returns with late fees

✅ **Inventory Management**
- Track 100+ items
- Update quantities on transactions
- View current stock levels

✅ **Employee Management**
- Add/update/delete employees
- Role-based access (Admin/Cashier)
- Login authentication

✅ **Customer Management**
- Register customers by phone number
- Track rental history
- Calculate late fees

✅ **Coupon System**
- Apply discount codes
- Percentage-based discounts

✅ **Audit Logging**
- Employee login/logout logs
- Transaction history

### Out of Scope
❌ Multi-store support  
❌ Network/cloud deployment  
❌ Advanced reporting/analytics  
❌ Receipt printing  
❌ Barcode scanning  
❌ Supplier management  
❌ Purchase orders  
❌ Customer loyalty programs  

---

## Stakeholders

| Role | Name | Responsibility | Authority |
|------|------|----------------|-----------|
| **Project Sponsor** | SG Technologies | Funding, approvals | Final decision |
| **Project Manager** | Team Lead | Planning, coordination | Budget, schedule |
| **Development Team** | 3 Developers | Design, implementation | Technical decisions |
| **End Users** | Store Managers/Cashiers | Requirements, testing | Feature priority |

---

## High-Level Requirements

### Functional Requirements

**FR-1: User Authentication**
- System shall authenticate users with username/password
- System shall support Admin and Cashier roles
- System shall log all login/logout events

**FR-2: Transaction Processing**
- System shall process sales with multiple items
- System shall calculate 6.5% sales tax
- System shall accept cash and credit card payments
- System shall apply coupon discounts

**FR-3: Rental Management**
- System shall register customers by phone number
- System shall track rental items with 14-day due date
- System shall calculate late fees ($1.50/day)
- System shall handle rental returns

**FR-4: Inventory Management**
- System shall maintain inventory of 100+ items
- System shall update quantities on sale/rental/return
- System shall prevent sales if item out of stock
- System shall display current stock levels

**FR-5: Employee Management**
- Admin shall add/update/delete employees
- System shall enforce unique usernames
- System shall store employee credentials securely

### Non-Functional Requirements

**NFR-1: Performance**
- Transaction processing < 500ms
- System startup < 5 seconds
- Inventory load < 1 second

**NFR-2: Usability**
- Common tasks achievable in < 5 clicks
- No training required for basic operations
- Clear error messages

**NFR-3: Reliability**
- 99% uptime during business hours
- Data backup capability
- Graceful error handling

**NFR-4: Security**
- Role-based access control
- Password protection
- Audit trail for all transactions

**NFR-5: Compatibility**
- Run on Windows 7/8/10
- Require Java 7 or higher
- No external dependencies

---

## Risks

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|------------|
| Text file corruption | Medium | High | Backup before writes, file locking |
| Team member unavailability | Low | Medium | Cross-training, documentation |
| Scope creep | High | Medium | Strict change control process |
| Performance issues | Medium | Medium | Optimize file I/O, limit file sizes |
| Security vulnerabilities | Medium | High | Input validation, access control |
| Late delivery | Low | High | Weekly milestones, buffer time |

---

## Constraints

**Technical Constraints:**
- Must use Java (team expertise)
- Desktop application only (no web)
- No external database (cost constraint)
- Windows-only deployment

**Business Constraints:**
- Budget: $0 (academic project)
- Timeline: 12 weeks fixed
- Team: 3 developers only
- Support: None post-delivery

**Regulatory Constraints:**
- No PCI DSS compliance (credit card validation basic only)
- No PII protection requirements (local deployment)

---

## Assumptions

1. Users have basic computer skills
2. Single workstation deployment (no concurrent users)
3. Data backup is manual process (not automated)
4. Credit card validation is format-only (no payment gateway)
5. Receipt printing not required
6. Internet not required for operation

---

## Success Criteria

**Project Success:**
- ✅ All in-scope features implemented
- ✅ No critical bugs in final release
- ✅ User acceptance testing passed
- ✅ Documentation complete (user + developer manuals)
- ✅ Delivered within 12-week timeline

**Business Success:**
- ⏳ Adoption by target users (post-delivery)
- ⏳ Reduction in transaction errors
- ⏳ Improved inventory accuracy

---

## Project Timeline (High-Level)

| Phase | Duration | Key Deliverables |
|-------|----------|------------------|
| **Inception** | Week 1-2 | Vision, use cases, glossary, WBS |
| **Elaboration** | Week 3-5 | Architecture, detailed design, prototypes |
| **Construction** | Week 6-10 | Implementation, unit testing, integration |
| **Transition** | Week 11-12 | Beta testing, bug fixes, documentation |

---

## Budget

**Total Budget:** $0 (Academic project)

**Resource Allocation:**
- Development: 3 developers × 12 weeks × 20 hours/week = 720 hours
- Testing: Included in development hours
- Documentation: Included in development hours

**Estimated Commercial Value:** $15,000-$25,000

---

## Glossary (Key Terms)

**POS** - Point of Sale: The place where a retail transaction is completed

**SKU** - Stock Keeping Unit: Unique identifier for each item

**Late Fee** - Charge applied when rental item returned after due date ($1.50/day)

**Rental Period** - 14 days from rental date to due date

**Transaction Types:**
- **Sale** - Purchase of items for ownership
- **Rental** - Temporary loan of items (14 days)
- **Return** - Customer returns rented item

---

## Business Rules

**BR-1: Rental Duration**
- Standard rental period is 14 days
- Extensions not supported in v1.0

**BR-2: Late Fee Calculation**
- $1.50 per day after due date
- Calculated when item returned
- No maximum late fee cap

**BR-3: Tax Calculation**
- Sales tax rate: 6.5% (fixed)
- Applied to subtotal after discounts
- Rentals are not taxed

**BR-4: Coupon Usage**
- One coupon per transaction
- Percentage discount applied before tax
- Expired coupons rejected

**BR-5: Stock Availability**
- Cannot sell/rent if quantity = 0
- Quantity updated immediately on transaction
- No negative inventory allowed

---

## Supplementary Specifications

Refer to `Supplementary Specification.docx` for detailed non-functional requirements including:
- Performance benchmarks
- Usability standards
- Reliability targets
- Security requirements
- Supportability needs

---

## Inception Phase Deliverables

| Document | Status | File |
|----------|--------|------|
| Vision Document | ✅ Complete | Vision.docx |
| Business Rules | ✅ Complete | Business Rules.docx |
| Glossary | ✅ Complete | Glossary.docx |
| Use Cases (Draft) | ✅ Complete | Use Cases Draft.docx |
| Supplementary Spec | ✅ Complete | Supplementary Specification.docx |
| Work Breakdown Structure | ✅ Complete | WBS-RM.xlsx, WBS_Submission_1.xlsx |
| Project Timeline | ✅ Complete | Inception Timeline.docx |
| Presentation | ✅ Complete | Inception Phase Presentation.pptx |

---

## Approval

**Project Approved:** Week 2  
**Next Phase:** Elaboration (Architecture and Design)

---

## Lessons Learned (Post-Project)

**What Went Well:**
- Clear vision from start
- Realistic scope for 12 weeks
- Good team collaboration

**What Could Improve:**
- Should have considered database from start
- Underestimated testing effort
- Security requirements too vague

**Recommendations for Future:**
- Prototype early (week 1-2)
- Set up automated testing from day 1
- More detailed risk analysis
