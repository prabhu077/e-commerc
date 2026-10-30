-- Query 1: category product count, units sold and delivered revenue.
-- Assumed tables: products(id, category), order_items(order_id, product_id, quantity, unit_price),
-- orders(id, status). Revenue uses snapshot unit_price * quantity.
SELECT p.category,
       COUNT(DISTINCT p.id) AS total_products,
       COALESCE(SUM(oi.quantity), 0) AS total_units_sold,
       COALESCE(SUM(oi.quantity * oi.unit_price), 0) AS total_revenue
FROM products p
LEFT JOIN order_items oi ON oi.product_id = p.id
LEFT JOIN orders o ON o.id = oi.order_id AND o.status = 'DELIVERED'
GROUP BY p.category
HAVING COUNT(DISTINCT p.id) > 2
ORDER BY total_revenue DESC;

-- Query 2: seller orders, delivered revenue and returned order count.
-- Assumed seller_id on products; returns(order_id, product_id, status).
SELECT p.seller_id,
       COUNT(DISTINCT o.id) AS total_orders,
       COALESCE(SUM(CASE WHEN o.status = 'DELIVERED'
                         THEN oi.quantity * oi.unit_price ELSE 0 END), 0) AS delivered_revenue,
       COUNT(DISTINCT CASE WHEN r.status = 'APPROVED' THEN r.order_id END) AS returned_orders
FROM products p
JOIN order_items oi ON oi.product_id = p.id
JOIN orders o ON o.id = oi.order_id
LEFT JOIN returns r ON r.order_id = o.id AND r.product_id = p.id
GROUP BY p.seller_id
HAVING COUNT(DISTINCT o.id) >= 3;

-- Query 3: customers with purchases in more than 2 distinct categories.
SELECT o.customer_id,
       COUNT(DISTINCT o.id) AS total_orders,
       COALESCE(SUM(CASE WHEN o.status = 'DELIVERED'
                         THEN oi.quantity * oi.unit_price ELSE 0 END), 0) AS delivered_spend,
       COUNT(DISTINCT CASE WHEN o.status = 'DELIVERED' THEN p.category END) AS categories_bought
FROM orders o
JOIN order_items oi ON oi.order_id = o.id
JOIN products p ON p.id = oi.product_id
GROUP BY o.customer_id
HAVING COUNT(DISTINCT CASE WHEN o.status = 'DELIVERED' THEN p.category END) > 2
ORDER BY total_orders DESC;
