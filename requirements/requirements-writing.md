# Requirements Writing
This document attempts to describe the program's progression towards completion and illustrate the intended User Experience. The goal will be to implement as many necessary features in as low a Version Number as possible, designated by the customer.

# Version 1
## New User Onboarding (TB-E1)
- **The User first visits the Webpage (TB-S1)**
    * **Priority:** Must Have
    * **Time Estimation:** No more than 1/2 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** When a New User first arrives to the Webpage, they may view the Storepage but must first Register before buying. They are redirected to the Registration Page by either clicking the "Sign-up" button at the top of the page or by clicking "Add to Cart" on a Storepage item.
- **The User creates an account (TB-S2)**
    * **Priority:** Must Have
    * **Time Estimation:** No more than 1/2 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** On the Registration Page, the New User creates an account and becomes a Registered User. They do this by inputting a Username, 6-digit (minimum) Password, and Email Address then clicking confirm. Their Login details are stored in our system.
- **The User finishes Registration and is sent to the Storepage (TB-S3)**
    * **Priority:** Must Have
    * **Time Estimation:** No more than 1/2 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** The Registered User is then redirected to the Storepage. If the User was originally brought to the Registration Page via clicking "Add to Cart" on an item, that item will be added to the cart at the end of Registration.

## The Storepage Experience (TB-E2)
- **The User first visits the Storepage (TB-S4)**
    * **Priority:** Must Have
    * **Time Estimation:** Around 4 days
    * **Functional or Nonfunctional:** Functional
    * **Description:** The User can start to see Art Pieces laid out in a grid pattern. They see each Art Piece has an Image, a Name, a Description, a Price, and an "Add to Cart" Button.
- **The User scrolls down to see more (TB-S5)**
    * **Priority:** Must Have
    * **Time Estimation:** No more than 1/4 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** The user notices they can scroll down the Storepage to view more Art Pieces. They can also scroll back up.
- **The User searches for a particular Art Piece (TB-S6)**
    * **Priority:** Must Have
    * **Time Estimation:** No more than 1/2 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** The User uses a Search Bar at the top of the screen to search for an Art Piece with a particular name, artist, or characteristic (description)
- **The User clicks the "Add to Cart" button (TB-S7)**
    * **Priority:** Must Have
    * **Time Estimation:** Around 1 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** The User clicks "Add to Cart" and the button turns from green to grey. The number next to the Cart icon at the top of the screen increments by +1 and the Checkout Button becomes clickable.
    

## The Checkout Experience (TB-E3)
- **The User tries checking out with an empty cart (TB-S8)**
    * **Priority:** Must Have
    * **Time Estimation:** No more than 1/4 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** The User gets an error message and cannot checkout.
- **The User clicks Checkout with an item(s) in the cart(TB-S9)**
    * **Priority:** Must Have
    * **Time Estimation:** Around 1 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** The User is shown a list of items in the cart and a subtotal in US Dollars. They can either click the "Pay Now" button, or return to the Storepage, or remove items from the cart. If the cart is emptied, they are returned to the Storepage.
- **The User clicks Pay Now (TB-SA)**
    * **Priority:** Must Have
    * **Time Estimation:** More than 1/2 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** Upon clicking Pay Now, the User is redirected to the Checkout Page. Here they see blank fields for Shipping Address, Phone Number, Shipping Speed, and Credit Card Number/Expiry/CCV. All fields are required before continuing
- **The User selects a Shipping Speed (TB-SB)**
    * **Priority:** Must Have
    * **Time Estimation:** Less than 1/4 day
    * **Functional or Nonfunctional:** Non-Functional
    * **Description:** 3 options are selectable for Shipping Speed:
    * Overnight - $29
    * 3-Day - $19
    * Ground - $0, free
- **The User clicks Confirm Order (TB-SC)**
    * **Priority:** Must Have
    * **Time Estimation:** Less than 1/4 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** Upon clicking Confirm Order, the User is presented with the Order Confirm Page. This page shows the list of items being bought (name and prices only), subtotal, tax (6% of the subtotal), Shipping Speed cost, and the Grand Total. They also see a button called Complete Order.
- **If the User does not click Complete Order (TB-SD)**
    * **Priority:** Must Have
    * **Time Estimation:** Less than 1/4 day
    * **Functional or Nonfunctional:** Non-Functional
    * **Description:** The items remain in the cart if the User backs out to the Checkout Page or the Storepage.
- **The User clicks Complete Order (TB-SE)**
    * **Priority:** Must Have
    * **Time Estimation:** More than 1/2 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** Upon clicking Complete Order, the items are removed from the inventory. The User is shown a receipt (it looks like the Confirm Order page but also containing the last 4 digits of their credit card as well as their address) on screen, and they receive a copy in their Email.
 - **The Items move from the Storepage to the Sales Report Page (TB-SF)**
    * **Priority:** Must Have
    * **Time Estimation:** More than 1/2 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** Now that the items are sold, they are removed from the Storepage and added to the Sales Report Page.

## Admin Powers (TB-E4)
- **Initial Admins are selected (TB-SF1)**
    * **Priority:** Must Have
    * **Time Estimation:** More than 1/2 day
    * **Functional or Nonfunctional:** Non-Functional
    * **Description:** The original Admins will be created when the system is first created. Admins have special access to the Admin Page, which Users cannot see.
- **An Admin visits the Admin Page (TB-SF2)**
    * **Priority:** Must Have
    * **Time Estimation:** About 1 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** An Admin visits the Admin Page and they see a page split into 3 columns: Run Sales Report, Add Item to Inventory, and Promote a New Admin.
- **The Admin selects Run Sales Report (TB-SF3)**
    * **Priority:** Must Have
    * **Time Estimation:** About 1 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** When the Admin runs a Sales Report, they see a history of all Items sold and a Grand Total. They can click individual items to see their receipts.
- **The Admin Adds a New Item to Inventory (TB-SF4)**
    * **Priority:** Must Have
    * **Time Estimation:** About 1/2 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** The Admin adds an Image, Title, Description, and Price of a new Art Piece. Then they can click a "Publish to Storepage" button, whereupon the Item shows up in the Storepage listings.
- **The Admin Promotes a New Admin (TB-SF5)**
    * **Priority:** Must Have
    * **Time Estimation:** Less than 1/4 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** The Admin inputs the Username of a Registered User and clicks the "Promote" button. That User is added to the Admin Log and gains the ability to view the Admin Page.

-----------------------------------------------------------

# Version 2
## New User Onboarding (TB-E1.1)
- **The User first visits the Webpage (TB-S1.1)**
    * **Priority:** Want
    * **Time Estimation:** Around 1 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** The New User can now add multiple items to cart before Registering an account. They will be redirected to the Registration Page upon clicking "Checkout"
- **The User creates an account (TB-S2.1)**
    * **Priority:** Need
    * **Time Estimation:** No more than 1/2 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** The Registration System will now send a Verification Email to the New User's Email before allowing them to complete Registration.
- **The User finishes Registration and is sent to the Storepage (TB-S3.1)**
    * **Priority:** Must Have
    * **Time Estimation:** No more than 1/4 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** If the User was directed to the Registration Page via clicking "Checkout", they will be redirected to the Checkout Page instead of the Storepage with their cart intact.

## The Storepage Experience (TB-E2.1)
- **The User searches for a particular Art Piece (TB-S6.1)**
    * **Priority:** Need
    * **Time Estimation:** Around 1 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** Art Pieces are now Sortable by Price, Artist, or Color Scheme.
- **The User clicks the "Add to Cart" button (TB-S7.1)**
    * **Priority:** Want
    * **Time Estimation:** Around 2 days
    * **Functional or Nonfunctional:** Non-Functional
    * **Description:** A small animation now plays where the art piece flies up to the Cart Icon and disappears from the Storepage.

## Admin Powers (TB-E4)
- **The Admin selects Run Sales Report (TB-SF3.1)**
    * **Priority:** Need
    * **Time Estimation:** Around 1 day
    * **Functional or Nonfunctional:** Functional
    * **Description:** The Sales Report is now capable of being exported to CSV.

---------------------------------------------------------------    
# Version 3
## New User Onboarding (TB-E1.2)
- **The User first visits the Storepage (TB-S4.1)**
    * **Priority:** Want
    * **Time Estimation:** Around 4 days
    * **Functional or Nonfunctional:** Non-Functional
    * **Description:** The art pieces now have multiple images. They can be viewed from multiple angles and seen framed on walls.
- **The User creates an account (TB-S2.2)**
    * **Priority:** Need
    * **Time Estimation:** Around 2 days
    * **Functional or Nonfunctional:** Functional
    * **Description:** The New User may now skip Registration and Sign-in via Trusted Account such as Google or X.

## The Storepage Experience (TB-E2.2)
- **The User searches for a particular Art Piece (TB-S6.2)**
    * **Priority:** Need
    * **Time Estimation:** Several Weeks
    * **Functional or Nonfunctional:** Functional
    * **Description:** The user may now interface with an AI which can answer Art-related questions and recommend Art Pieces for the user.