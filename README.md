## ⭐⭐⭐ **Nivel 3 - Proyecto API Blackjack Reactiva** 🍎

# API de Juego de Blackjack
## Persistencia Dual (MySQL & MongoDB)

Este proyecto consiste en una **API REST puramente reactiva** para gestionar partidas de **Blackjack**, desarrollada como parte del itinerario Java de **Itacademy**.  
La aplicación utiliza un enfoque de **programación reactiva con Spring WebFlux** y gestiona datos en dos entornos:

- **MySQL** → Jugadores
- **MongoDB** → Partidas

---

## 📖 Descripción del Proyecto

La API permite la gestión completa del ciclo de vida de un juego de Blackjack, desde la creación de jugadores y el seguimiento de estadísticas en una base de datos relacional (**MySQL vía R2DBC**), hasta la persistencia de manos y estados de las partidas en una base de datos **NoSQL (MongoDB)**.

---

## 🧩 Historias de Usuario Implementadas

- **Gestión de Jugadores**
    - Creación de perfiles
    - Actualización de nombres
    - Seguimiento de victorias, derrotas y tasa de éxito (*win rate*)

- **Lógica de Blackjack**
    - Creación de nuevas partidas
    - Gestión de jugadas (*Hit / Stand*)
    - Cálculo automático de resultados

- **Ranking Global**
    - Consulta de jugadores ordenados por rendimiento y puntuación

- **Limpieza de Datos**
    - Eliminación de partidas finalizadas o canceladas
    - Retorno **204 No Content**

---

## 🛠️ Tecnologías Utilizadas

- **Java 23 (LTS)**
- **Spring Boot 3.5.x**
- **Spring WebFlux** (Programación reactiva no bloqueante)
- **Spring Data R2DBC** (Persistencia reactiva para MySQL)
- **Spring Data MongoDB Reactive** (Persistencia reactiva para MongoDB)
- **Jakarta Validation** (Validación de datos de entrada)
- **JUnit 5 & Mockito** (Pruebas unitarias y de controladores)
- **Swagger / OpenAPI 3** (Documentación interactiva)

---

## ⚙️ Configuración e Instalación

### Requisitos Previos

- Tener instalado **Java 23**
- Instancias activas de:
    - **MySQL** (puerto `3306`)
    - **MongoDB** (puerto `27017`)  
      *(o usar Docker)*

### Pasos para Ejecutar

1. Clonar el repositorio
2. Configurar las conexiones en  
```
   `src/main/resources/application.properties`
   ```

properties
```

# Conexión MySQL (R2DBC)
spring.r2dbc.url=r2dbc:mysql://localhost:3306/blackjack_db
spring.r2dbc.username=root
spring.r2dbc.password=tu_password

# Conexión MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/blackjack_logs
Ejecutar la aplicación

mvn spring-boot:run
```
🌐 Endpoints de la API
```
Método	Endpoint	Descripción	Status OK	Status Error
POST	/blackjack/game/new	Crear una nueva partida	201 Created	400 Bad Request
GET	/blackjack/game/{id}	Detalles de una partida	200 OK	404 Not Found
POST	/blackjack/game/{id}/play	Realizar una jugada (Hit / Stand)	200 OK	400 Bad Request
DELETE	/blackjack/game/{id}/delete	Eliminar una partida	204 No Content	404 Not Found
GET	/blackjack/player/ranking	Listar ranking de jugadores	200 OK	—
PUT	/blackjack/player/{id}	Cambiar nombre del jugador	200 OK	400 / 404
```
🧪 Testing
El proyecto incluye una suite de pruebas para asegurar la calidad del código:

Unit Tests

Lógica de negocio en los servicios

Integration Tests

Uso de WebTestClient

Validación de controladores, rutas y GlobalExceptionHandler

Ejecutar los tests:
```
mvn test
```
📂 Estructura del Proyecto
```
src/main/java/com/example/JavaSprint5_1BlackJack/
├── controllers/   # Endpoints REST reactivos
├── DTO/           # Objetos de transferencia de datos (Records)
├── entities/      # Entidades MySQL y Documentos MongoDB
├── exception/     # Manejo de WebExchangeBindException y personalizadas
├── repository/    # Interfaces R2DBC y ReactiveMongoRepository
└── services/      # Lógica de negocio y reglas del Blackjack
```