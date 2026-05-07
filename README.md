# Spring Boot Event-Driven Saga (Kafka)

Studi kasus: `order`, `product`, `payment` dengan arsitektur event-driven dan Saga choreography + compensation transaction.

## Arsitektur
- `order-service` membuat order dan mengelola status saga.
- `product-service` reserve/release stok.
- `payment-service` proses payment dan refund.
- `common-event` menyimpan kontrak event bersama.
- Broker async job: Kafka.

## Alur Saga Choreography
1. `OrderCreatedEvent` dari `order-service`.
2. `product-service` reserve stok:
- sukses: `ProductReservedEvent`
- gagal: `ProductReservationFailedEvent`
3. `payment-service` konsumsi `ProductReservedEvent`:
- sukses: `PaymentProcessedEvent`
- gagal: `PaymentFailedEvent`
4. `order-service` finalisasi:
- sukses: status `COMPLETED`
- gagal: status `CANCELLED` + trigger compensation.

## Compensation Transaction
- Saat `PaymentFailedEvent`: `order-service` publish `ReleaseProductReservationEvent` agar stok dikembalikan.
- Saat demo forced-failure setelah payment sukses: `order-service` publish:
- `ReleaseProductReservationEvent`
- `RefundPaymentEvent`

## Jalankan
```bash
cd saga-ecommerce-kafka
docker compose up -d
mvn -pl common-event,order-service,product-service,payment-service clean package
mvn -pl order-service spring-boot:run
mvn -pl product-service spring-boot:run
mvn -pl payment-service spring-boot:run
```

## Uji Cepat
1. Cek produk:
```bash
curl localhost:8082/products
```
2. Buat order sukses:
```bash
curl -X POST localhost:8081/orders \
  -H 'Content-Type: application/json' \
  -d '{"productId":"SKU-1","quantity":1,"amount":500}'
```
3. Buat order gagal payment (trigger compensation stok):
```bash
curl -X POST localhost:8081/orders \
  -H 'Content-Type: application/json' \
  -d '{"productId":"SKU-1","quantity":1,"amount":15000}'
```
4. Demo compensation penuh (refund + release stock):
- Buat order nominal kecil, simpan `orderId`.
- Aktifkan forced failure:
```bash
curl -X PATCH localhost:8081/orders/{orderId}/force-fail-after-payment
```

## Catatan
- Implementasi ini berfokus pada konsep saga choreography dan compensation.
- Untuk production, tambahkan idempotency key, retry policy, DLQ, outbox pattern, observability, dan distributed tracing.
