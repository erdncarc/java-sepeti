<div align="center">

# 🛒 Java Sepeti

### A Multi-Role Food Delivery Platform — Spring Boot API + Native Android App

*browse, basket, order, deliver — one platform for customers, restaurants, and couriers*

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6db33f?logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-24-007396?logo=openjdk&logoColor=white)
![Android](https://img.shields.io/badge/Android-Java-3ddc84?logo=android&logoColor=white)
![JWT](https://img.shields.io/badge/Auth-JWT-000000?logo=jsonwebtokens&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479a1?logo=mysql&logoColor=white)

</div>

---

## Overview

**Java Sepeti** is a full-stack food-ordering and delivery platform. A **Spring Boot** REST API handles the domain, persistence, and security, while a **native Android** client (written in Java) provides a separate, role-aware experience for each kind of user.

The system models a real delivery marketplace with four roles — **Customer**, **Restaurant**, **Courier**, and **Admin** — each with its own flows, screens, and permissions. Customers discover restaurants and place orders, restaurants manage menus and incoming orders, couriers handle assignments and deliveries, and admins oversee the platform. Authentication is stateless: the API issues a JWT on login that the Android client attaches to every request.

---

## Architecture

```text
        Android client (Java)                  Spring Boot API                MySQL
   ┌───────────────────────────┐          ┌──────────────────────┐       ┌──────────┐
   │ app module                │  HTTP +  │ Controllers          │  JPA  │          │
   │  · role-based Activities  │  JSON    │ Services             │ ────▶ │ Hibernate│
   │  · Fragments / Adapters   │ ───────▶ │ Repositories         │       │  schema  │
   │                           │  + JWT   │ Spring Security + JWT│       │          │
   │ core module               │ ◀─────── │ DTOs · ModelMapper   │ ◀─── │          │
   │  · Retrofit ApiClient     │          │ Validation           │       │          │
   └───────────────────────────┘          └──────────────────────┘       └──────────┘
```

The backend follows a classic layered design — **Controller → Service → Repository → Entity** — with role-specific DTOs mapped via ModelMapper. The Android app is split into two modules: **`app`** for the UI (Activities, Fragments, Adapters) and **`core`** for shared infrastructure (Retrofit networking, models, DTOs, local store). The emulator reaches the local backend through `http://10.0.2.2:8080/api/`.

---

## Features

### Customer
- Multi-step sign-up, login, and profile editing
- Browse and search restaurants (with filtering and sorting), available even anonymously
- View menus and product details, build a basket, and confirm orders
- Saved addresses with a **map-based address picker**, plus saved payment cards
- Rate and review restaurants

### Restaurant
- Dedicated sign-up flow and home dashboard
- Create and edit menu items
- Receive and manage incoming orders

### Courier
- Dedicated sign-up flow and main screen
- Handle order assignments and deliveries with status tracking

### Platform
- JWT-secured, role-based authorization across all endpoints
- Geolocation-based distance calculation for restaurant/courier matching
- Refunds, ratings, reviews, allergen and nutrition data on the domain model
- Interactive **Swagger UI** for the entire API

---

## Tech Stack

### Backend

| Concern | Technology |
|---------|------------|
| Framework | Spring Boot 3.2 (Java 24, Maven) |
| Persistence | Spring Data JPA, Hibernate, MySQL |
| Security | Spring Security, JWT (jjwt) |
| Mapping / Boilerplate | ModelMapper, Lombok |
| Validation | Hibernate Validator / Jakarta Validation |
| API Docs | springdoc-openapi (Swagger UI) |

### Mobile

| Concern | Technology |
|---------|------------|
| Platform | Native Android (Java) |
| Modules | `app` (UI) + `core` (shared infrastructure) |
| Networking | Retrofit |
| Maps | Google Maps (address picker) |
