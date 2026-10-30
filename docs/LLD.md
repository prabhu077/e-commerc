# LLD summary

## Main classes
- `User` (abstract): common identity fields; `Customer`, `Seller`, `Admin` inherit it.
- `Product`, `Cart`, `CartItem`, `Order`, `OrderItem`, `Shipment`, `Payment`, `Coupon`, `ReturnRequest`, `Review`.
- `PaymentMethod` interface with `CardPayment`, `UpiPayment`, `NetBankingPayment`, `CodPayment`.
- Services: `CatalogService`, `CartService`, `OrderService`, `PaymentService`, `CouponService`, `ReturnService`, `ReviewService`.
- `Store`: in-memory maps/lists for all runtime records.

## Important sequence: checkout
1. Validate customer cart and current stock.
2. Snapshot current product prices into `OrderItem`.
3. Calculate item subtotal, delivery fee, coupon discount and payable total.
4. Invoke selected `PaymentMethod`.
5. If online payment fails, stop; do not create order or decrement stock.
6. Create order and one shipment per seller.
7. Decrement stock and clear cart.

## Shipment state progression
`PLACED -> PACKED -> SHIPPED -> OUT_FOR_DELIVERY -> DELIVERED`.
Each shipment advances independently. Order becomes delivered only after all shipments are delivered.

## Diagrams (Mermaid)
### High-level module diagram
```mermaid
flowchart TD
 Customer --> Cart
 Cart --> Order
 Catalog --> Cart
 Order --> Payment
 Order --> Coupon
 Order --> Shipment
 Shipment --> Order
 Order --> Returns
 Returns --> Refund
 Order --> Reviews
 Admin --> Catalog
 Seller --> Catalog
 Seller --> Shipment
```

### Entity relationship sketch
```mermaid
erDiagram
 USER ||--o| CUSTOMER : is_a
 USER ||--o| SELLER : is_a
 USER ||--o| ADMIN : is_a
 CUSTOMER ||--o{ ADDRESS : has
 SELLER ||--o{ PRODUCT : lists
 CATEGORY ||--o{ PRODUCT : classifies
 CUSTOMER ||--o| CART : owns
 CART ||--o{ CART_ITEM : contains
 PRODUCT ||--o{ CART_ITEM : referenced_by
 CUSTOMER ||--o{ ORDER : places
 ORDER ||--|{ ORDER_ITEM : contains
 PRODUCT ||--o{ ORDER_ITEM : snapshot_of
 ORDER ||--o{ SHIPMENT : split_into
 ORDER ||--o| PAYMENT : paid_by
 COUPON ||--o{ ORDER : applied_to
 ORDER ||--o{ RETURN_REQUEST : may_have
 ORDER ||--o{ REVIEW : receives
 PRODUCT ||--o{ REVIEW : reviewed_in
```

The PDF asks for diagrams and three SQL exercises. The diagrams and SQL are included here as design references; the runnable console application stores data in memory as requested.
