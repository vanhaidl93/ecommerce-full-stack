# 🛍️ Ecommerce

## Introduction:
🚀 This project is a full-featured e-commerce platform built with modern technologies.

🎯 We'll have all most fully functional e-commerce platform that's described on three main domains: product, order and user.
Core features include:
- 👤 User authentication (login, signup) with Kinde.
- 🛠️ Admin panel for products, categories and orders .
- 🛒 Users can browse and filter products, add them to their cart, and review before checkout. 
- 💳 Secure payments using Stripe
- 📦 Order management & tracking

## 🛠️ Tech Stack
- **Spring Boot 3** for our backend development.
- **Angular 17+** for the frontend.
- **Tailwind v3** with **Daisy v4** for sleek and responsive UI.
- Database management will be handled by **PostgreSQL** _(Docker approach)_, migrating with **Liquibase** Change Types.
- Secure authentication by **Spring security 6** and **Kinde**.
- Using **Nx monorepo** workspace for ecommerce full stack.
- **Hexagonal architecture** _(Domain-Driven Design)_ in backend.

## 🧪 How to Run

### Prerequisites
- Resource need to be cloned from this GitHub repository.
- IntelliJ IDE - VS Code
- JDK 21.
- Docker-Desktop.
- NodeJS, Angular CLI.
- Register Stripe Account, Kinde Account.
- Install **Stripe Local** available for Webhook with Stripe Account, payment service.
- Update environment variable for both backend and frontend before run command line, related client_key and secret_key.
```sh

    1. ecom-backend:
    KINDE_CLIENT_ID=${<client-id>}
    KINDE_CLIENT_SECRET=${<client-secret>}
    STRIPE_API_KEY=${<stripe-api-secret-key>}
    STRIPE_WEBHOOK_SECRET=${<stripe-webhook-secret>}
    
    issuer-uri: <kinde-domain>
    jwk-set-uri: <kinde-certification> 
    audiences: <audience-verify-jwt-frontend>
    
    2. ecom-frontend:
    environment = {
      kinde: {
        authority: <kinde-domain>,
        redirectUrl: 'http://localhost:4200',
        postLogoutRedirectUri: 'http://localhost:4200',
        clientId: <client-key>,
        audience: <audience-related-custom-api-for-frontend-application>,
      },
      apiUrl: 'http://localhost:8080/api',
      stripePublishableKey: <publishable_key>,
    }
```

### Running resource - _Windows (PowerShell)_

1. Clone the repository:
```sh
  git clone https://github.com/vanhaidl93/ecommerce-full-stack
  cd ecommerce-full-stack
```

2. Initialize libraries for project
```sh
  npm install
```
3. Create schema: ecommerce
```sh
  docker exec -it postgres-db bash
  psql -U root -d ecommerce
  \c ecommerce
  create schema ecommerce;
```
4. Create a stripe webhook in local
```sh
    stripe login 
    stripe listen --forward-to localhost:8080/api/orders/webhook
```
- _Saving <stripe-webhook-secret>_
5. Run Ecom-frontend:
- _Updating environment variable_
```sh
  npx nx run ecom-frontend:serve:development
```
6. Run Ecom-backend
```sh
  $env:KINDE_CLIENT_ID="<client-id>"
  $env:KINDE_CLIENT_SECRET="<client-secret>"
  $env:STRIPE_API_KEY="<stripe-api-secret-key>"
  $env:STRIPE_WEBHOOK_SECRET="<stripe-webhook-secret>"
  
  npx nx run ecom-backend:serve
```



