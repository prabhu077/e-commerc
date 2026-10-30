# E-commerce Shopping App — Java LLD

Console application based on `05_Ecommerce_Shopping_App_Problem_Statement.pdf`.
Uses Java 17 and in-memory collections only (no database).

## Run in VS Code
1. Install JDK 17+ and the VS Code **Extension Pack for Java**.
2. Open this folder in VS Code.
3. In Terminal:
   ```bash
   mvn clean package
   java -cp target/classes com.ecommerce.cli.Main
   ```
   If Maven is not installed, compile with:
   ```bash
   javac -d out $(find src/main/java -name "*.java")
   java -cp out com.ecommerce.cli.Main
   ```
   On Windows PowerShell, use:
   ```powershell
   Get-ChildItem -Recurse src/main/java -Filter *.java | ForEach-Object { $_.FullName } | Set-Content sources.txt
   javac -d out @sources.txt
   java -cp out com.ecommerce.cli.Main
   ```

## Seed login identities
- Customer: `CUST1`
- Seller: `SELLER1`
- Seller: `SELLER2`
- Admin: `ADMIN1`

The CLI is a demonstrator; select actions from its menu. In-memory data resets when the app exits.

## Implemented business rules
- Admin/Seller product creation; categories, positive price, non-negative stock.
- Product out-of-stock visibility; cart max 20 distinct products, max 10 units/product, stock validation.
- Cart prices refresh from current catalog at checkout; cart does not reserve stock.
- Checkout checks all stock before any decrement; immutable order-item price snapshots.
- Rs.50 delivery charge below Rs.500; free at/above Rs.500.
- COD maximum Rs.5,000; online payment simulated before stock deduction/order confirmation.
- Coupons: percentage/flat, max discount, minimum order, expiry, one coupon/order and one use/customer.
- Seller-specific shipments; strict shipment status progression; order delivered when all shipments delivered.
- Cancellation before shipped restores stock; prepaid cancellation refund excludes? Per source: prepaid order cancellation before shipping is refunded in full.
- Returns within 7 days after delivery; Grocery excluded; seller/admin approval restores stock and refunds item amount only (delivery fee excluded).
- Reviews only after delivered; one per product/order; rating 1–5; low-rating flag after at least 10 reviews and average <2.0.

## LLD note
This is an educational in-memory console implementation, not production payment software. Payment processors simulate gateway responses. Do not collect/store real card numbers, CVV, UPI PIN, or bank credentials.
