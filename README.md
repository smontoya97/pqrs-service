# PQRS Service

Servicio backend desarrollado en Java 21 y Spring Boot bajo Arquitectura Hexagonal para la gestión de Peticiones, Quejas, Reclamos y Sugerencias (PQRS).

---

## 🚀 Requisitos e Instalación

* **JDK 21**
* **Docker** y **Docker Compose**
* **MySQL 8.0** (si se ejecuta manualmente)

### Opción 1: Despliegue con Docker Compose (Recomendado)
```bash
docker-compose up -d --build
```
*La aplicación estará disponible en `http://localhost:8080`.*

### Opción 2: Ejecución Manual
1. Iniciar una instancia de MySQL con la base de datos `pqrs_db`, usuario `pqrs_user` y contraseña `pqrs_password` en el puerto `3306` (o actualizar `application.yaml`).
2. Ejecutar la aplicación con Gradle:
```bash
./gradlew bootRun
```

---

## 📌 Endpoints

| Método | Endpoint | Acceso | Descripción |
| :--- | :--- | :--- | :--- |
| `POST` | `/requests` | Público | Registra una nueva PQRS en el sistema. |
| `GET` | `/requests` | Autenticado (`ROLE_OFFICIAL`) | Obtiene las PQRS asignadas a la dependencia del funcionario extraída del JWT. |

---

## 🧪 Pruebas Unitarias

Para ejecutar las pruebas automatizadas del proyecto:
```bash
./gradlew test
```

---

## ⚡ Estrategia de Escalabilidad (x100 de Volumen)

Si el volumen del sistema aumentara 100 veces, la solución escalaría mediante las siguientes acciones clave:

1. **Escalado Horizontal de Microservicios:** Desplegar múltiples réplicas del contenedor de `pqrs-service` detrás de un balanceador de carga (NGINX o AWS ALB) y gestionar la infraestructura con Kubernetes (HPA).
2. **Desacoplamiento de Eventos (Asincronía):** Sustituir la publicación síncrona de `RequestCreatedEvent` por un Message Broker distribuido (Apache Kafka o RabbitMQ) con procesamiento asíncrono en background mediante workers.
3. **Escalado de Base de Datos:** Implementar arquitectura de BD con nodo primario para escrituras (`POST`) y múltiples réplicas de lectura (`GET`), además de partitioning por fecha/dependencia e indexación adecuada.
4. **Capa de Caching Distribuido:** Integrar Redis para almacenar en caché las consultas de PQRS frecuentes por dependencia, reduciendo la carga sobre MySQL.
5. **Ajustes de Infraestructura:** Configurar pools de conexiones (HikariCP) y rate limiting en el API Gateway para proteger el sistema ante picos impredecibles de tráfico.