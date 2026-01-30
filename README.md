# MAMA Ltd Online Store

A production-style microservices e-commerce platform built with Spring Boot and React, demonstrating modern cloud-native development and deployment practices.
This README provides a high-level overview.  
Detailed setup, deployment, API specs, and troubleshooting are available in the `/docs` folder.
---
## Project Overview

This project implements a full-stack e-commerce platform using microservices architecture, containerization with Docker, and orchestration with Kubernetes. It demonstrates best practices in software engineering, including service isolation, API design, database management, and cloud-native deployment.
---

## Key Features

### Backend Microservices
- **Product Service**: Manages product catalog and inventory with optimistic locking
- **Order Service**: Handles order placement, processing, and cancellation
- **RESTful APIs**: Comprehensive CRUD operations with Swagger documentation
- **Inter-Service Communication**: OpenFeign client for service-to-service calls
- **Database Per Service**: Independent MySQL databases for data isolation

### Frontend Application
- **Modern React UI**: Built with Vite for fast development
- **Responsive Design**: Tailwind CSS for mobile-first layouts
- **Real-time Updates**: Dynamic cart management and order tracking
- **User-Friendly**: Intuitive product browsing and checkout flow

### DevOps & Deployment
- **Containerization**: Docker multi-stage builds for optimized images
- **Kubernetes Orchestration**: 3 replicas per service for high availability
- **Load Balancing**: NGINX Ingress Controller for centralized routing
- **Persistent Storage**: PersistentVolumeClaims for database data
- **Health Monitoring**: Liveness and readiness probes
- **CORS Configuration**: Secure cross-origin resource sharing

---

##  Technology Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming language |
| Spring Boot | 3.4.1 | Application framework |
| Spring Data JPA | 3.4.1 | Data persistence |
| Hibernate | 6.6.4 | ORM implementation |
| Spring Cloud OpenFeign | 4.1.0 | Service-to-service communication |
| MySQL | 8.0 | Relational database |
| Lombok | Latest | Boilerplate reduction |
| SpringDoc OpenAPI | 2.5.0 | API documentation (Swagger UI) |

### Frontend
| Technology | Version | Purpose |
|------------|---------|---------|
| React | 18 | UI framework |
| Vite | 7.3 | Build tool & dev server |
| Tailwind CSS | Latest | Utility-first CSS |
| Lucide React | Latest | Icon library |
| JavaScript | ES6+ | Programming language |

### DevOps & Infrastructure
| Technology | Version | Purpose |
|------------|---------|---------|
| Docker | 20.10+ | Containerization |
| Kubernetes | 1.27+ | Container orchestration |
| Kind | Latest | Local Kubernetes cluster |
| NGINX Ingress | 1.8.1 | API Gateway & load balancer |
| Maven | 3.8+ | Build automation |

---

## Project Structure

```
mama-ltd-online-store/
│
├── .idea/                        # IntelliJ IDEA configuration
├── .vscode/                      # VS Code configuration
│
├── db-init/                      # Database initialization scripts
│   ├── 01_create_databases.sql
│   ├── 02_product_schema.sql
│   ├── 03_order_schema.sql
│   ├── 04_user_schema.sql
│   ├── 05_create_db_users.sql
│   └── 06_sample_products.sql
│
├── frontend/                     # React frontend application
│   ├── src/
│   │   ├── App.jsx              # Main application component
│   │   ├── main.jsx             # Application entry point
│   │   └── index.css            # Global styles (Tailwind)
│   ├── public/
│   ├── package.json
│   ├── vite.config.js
│   ├── tailwind.config.js
│   └── postcss.config.js
│
├── k8s/                          # Shared Kubernetes resources
│   └── ingress.yaml             # NGINX Ingress configuration
│
├── order-service/                # Order microservice
│   ├── src/
│   │   └── main/
│   │       ├── java/com/mamaltd/orderservice/
│   │       │   ├── client/      # Feign clients
│   │       │   ├── config/      # WebConfig (CORS)
│   │       │   ├── controller/  # REST controllers
│   │       │   ├── dto/         # Data Transfer Objects
│   │       │   ├── entity/      # JPA entities
│   │       │   ├── enums/       # Enumerations
│   │       │   ├── exception/   # Exception handlers
│   │       │   ├── repository/  # JPA repositories
│   │       │   ├── service/     # Business logic
│   │       │   └── OrderServiceApplication.java
│   │       └── resources/
│   │           └── application.yml
│   ├── k8s/                     # Kubernetes manifests
│   │   ├── deployment.yaml      # Service deployment
│   │   ├── mysql-orders.yaml    # MySQL deployment
│   │   └── order-service.yaml   # ConfigMap & Secret
│   ├── Dockerfile
│   └── pom.xml
│
├── product-service/              # Product microservice
│   ├── src/
│   │   └── main/
│   │       ├── java/com/mamaltd/productservice/
│   │       │   ├── config/      # WebConfig (CORS)
│   │       │   ├── controller/  # REST controllers
│   │       │   ├── dto/         # Data Transfer Objects
│   │       │   ├── entity/      # JPA entities
│   │       │   ├── exception/   # Exception handlers
│   │       │   ├── repository/  # JPA repositories
│   │       │   ├── service/     # Business logic
│   │       │   └── ProductServiceApplication.java
│   │       └── resources/
│   │           └── application.yml
│   ├── k8s/                     # Kubernetes manifests
│   │   ├── deployment.yaml      # Service deployment
│   │   ├── mysql-products.yaml  # MySQL deployment
│   │   └── product-service.yaml # ConfigMap & Secret
│   ├── Dockerfile
│   └── pom.xml
│
├── docker-compose.yml            # Docker Compose (legacy)
├── pom.xml                       # Parent Maven POM
└── README.md                     # This file
```

---

## Architecture

### System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    CLIENT LAYER                             │
│  ┌──────────────────────────────────────────────────────┐   │
│  │        React Frontend (Vite + Tailwind CSS)          │   │
│  │             http://localhost:5173                    │   │
│  └────────────────────────┬─────────────────────────────┘   │
└───────────────────────────┼─────────────────────────────────┘
                            │ HTTP/REST
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                  API GATEWAY LAYER                          │
│  ┌──────────────────────────────────────────────────────┐   │
│  │         NGINX Ingress Controller                     │   │
│  │         Routes: /api/products → Product Service      │   │
│  │                 /api/orders   → Order Service        │   │
│  └──────────┬─────────────────────┬─────────────────────┘   │
└─────────────┼─────────────────────┼─────────────────────────┘
              │                     │
              ▼                     ▼
┌─────────────────────────────────────────────────────────────┐
│              MICROSERVICES LAYER                            │
│              (Kubernetes Cluster)                           │
│  ┌───────────────────┐        ┌───────────────────┐         │
│  │ Product Service   │        │  Order Service    │         │
│  │ Spring Boot 3.4.1 │◄────── │  Spring Boot 3.4.1│         │
│  │ 3 Replicas        │ Feign  │  3 Replicas       │         │
│  │ Port: 8081        │        │  Port: 8082       │         │
│  └────────┬──────────┘        └────────┬──────────┘         │
│           │                            │                    │
└───────────┼────────────────────────────┼────────────────────┘
            │ JDBC                       │ JDBC
            ▼                            ▼
┌─────────────────────────────────────────────────────────────┐
│               PERSISTENCE LAYER                             │
│  ┌───────────────────┐        ┌───────────────────┐         │
│  │ MySQL Products    │        │  MySQL Orders     │         │
│  │ Database:productdb│       |  Database:orderdb │         │
│  │ PVC: 5Gi          │        │  PVC: 5Gi         │         │
│  └───────────────────┘        └───────────────────┘         │
└─────────────────────────────────────────────────────────────┘
```

### Key Design Patterns

- **Database Per Service**: Each microservice has its own database
- **API Gateway**: NGINX Ingress for centralized routing
- **Service Discovery**: Kubernetes DNS for inter-service communication
- **Circuit Breaker**: Feign client with resilience (future enhancement)
- **CQRS Ready**: Separate read/write models possible

---

## Getting Started (Summary)

This project runs locally using Docker and Kubernetes (Kind).

**High-level steps:**
1. Build backend services with Maven & Docker
2. Deploy services and databases to Kubernetes
3. Run React frontend with Vite

Full step-by-step instructions:
- `docs/deployment-documentation.md`

---
## Learning Outcomes

This project demonstrates proficiency in:

 **Microservices Architecture**: Designing independent, scalable services  
 **RESTful API Design**: Creating well-structured, documented APIs  
 **Database Design**: Implementing database-per-service pattern  
 **Containerization**: Building optimized Docker images  
 **Kubernetes**: Deploying and managing containerized applications  
 **Service Communication**: Inter-service REST calls with Feign  
 **Frontend Development**: Building responsive React applications  
 **DevOps Practices**: CI/CD readiness, infrastructure as code  

---

## Future Enhancements

- [ ] User authentication & authorization (Spring Security + JWT)
- [ ] API Gateway pattern with Spring Cloud Gateway
- [ ] Redis caching for frequently accessed data
- [ ] Monitoring with Prometheus & Grafana
- [ ] Distributed tracing with Zipkin/Jaeger
- [ ] Event-driven architecture with RabbitMQ/Kafka
- [ ] CI/CD pipeline with GitHub Actions
- [ ] Comprehensive unit & integration tests
- [ ] Horizontal Pod Autoscaling (HPA)
- [ ] Helm charts for easier deployment

---

## Author

**Mama Djalo**  
Student ID: 23010394  
Course: BSc Computer Science  
University: Liverpool Hope University  
Academic Year: 2025/26

### Contact
- Email: mamasadj@hotmail.com
- Phone: 07498 561299
- GitHub: https://github.com/mama-djalo
