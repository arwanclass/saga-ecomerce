# Chat Context Handoff

Ringkasan konteks chat yang sudah dikerjakan:

- Dibuat proyek Java Spring Boot berbasis event-driven architecture.
- Studi kasus meliputi `order-service`, `product-service`, dan `payment-service`.
- Asynchronous job menggunakan Kafka.
- Diterapkan Saga pattern dengan pendekatan choreography.
- Ditambahkan compensation transaction untuk rollback:
  - payment gagal -> release reservasi stok.
  - order dibatalkan setelah payment sukses (forced demo) -> release stok + refund payment.
- Struktur proyek saat ini berada di folder ini (`saga-ecommerce-kafka`).

File acuan utama:
- `README.md`
- `docker-compose.yml`
- `common-event/`
- `order-service/`
- `product-service/`
- `payment-service/`
