# 🖼️ Use Case Diagram – Online Art Store System

This use case diagram shows how different users and systems interact with the Online Art Store System.  
It includes the major features for all project versions (V1–V3), including AI art recommendations.

---

## Use Case Diagram
<img width="611" height="371" alt="use-case" src="https://github.com/user-attachments/assets/32302e11-e148-4ef9-9797-7e5e467efe85" />

```plantuml
@startuml
left to right direction
actor "New User" as NewUser
actor "Registered User" as RegUser
actor "Admin" as Admin
actor "Payment System" as Payment
actor "Email System" as Email
actor "AI Assistant" as AI

rectangle "Online Art Store System" {
    (Visit Webpage)
    (Register Account)
    (Login / Sign In)
    (Browse Art Pieces)
    (Search Art Pieces)
    (Add to Cart)
    (Checkout)
    (Enter Shipping & Payment Info)
    (Confirm Order)
    (Complete Order & Receive Receipt)
    (View Sales Report)
    (Add Item to Inventory)
    (Promote User to Admin)
    (Export Report to CSV)
    (Send Verification Email)
    (Recommend Art Pieces)
}

' Connections
NewUser --> (Visit Webpage)
NewUser --> (Register Account)
NewUser --> (Login / Sign In)

RegUser --> (Browse Art Pieces)
RegUser --> (Search Art Pieces)
RegUser --> (Add to Cart)
RegUser --> (Checkout)
RegUser --> (Enter Shipping & Payment Info)
RegUser --> (Confirm Order)
RegUser --> (Complete Order & Receive Receipt)

Admin --> (View Sales Report)
Admin --> (Add Item to Inventory)
Admin --> (Promote User to Admin)
Admin --> (Export Report to CSV)

Payment --> (Enter Shipping & Payment Info)
Payment --> (Complete Order & Receive Receipt)

Email --> (Send Verification Email)
Email --> (Complete Order & Receive Receipt)

AI --> (Recommend Art Pieces)
@enduml
