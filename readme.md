# Aventura Submarina 🌊⚓️  
**API REST + Cliente JavaFX para un juego multijugador por turnos**  

> Proyecto desarrollado como parte de un trabajo académico para la asignatura de Enxeñaría de Servizos
> El objetivo es implementar un sistema cliente-servidor completo para gestionar usuarios, autenticación y partidas de un juego llamado **Aventura Submarina**, siguiendo buenas prácticas de diseño (REST, seguridad por roles, tokens, y orientación a hipermedia con HATEOAS).

---

## 📌 Índice

- [Descripción general](#-descripción-general)
- [Arquitectura](#-arquitectura)
- [Tecnologías y herramientas](#-tecnologías-y-herramientas)
- [Características principales](#-características-principales)
- [Modelo de seguridad y autenticación](#-modelo-de-seguridad-y-autenticación)
- [Persistencia y datos](#-persistencia-y-datos)
- [HATEOAS / Hipermedia](#-hateoas--hipermedia)
- [Endpoints de la API](#-endpoints-de-la-api)
  - [Autenticación](#autenticación)
  - [Usuarios](#usuarios)
  - [Partidas](#partidas)
- [Cómo ejecutar el proyecto](#-cómo-ejecutar-el-proyecto)
  - [Requisitos previos](#requisitos-previos)
  - [Configuración](#configuración)
  - [Arranque del servidor](#arranque-del-servidor)
  - [Arranque del cliente JavaFX](#arranque-del-cliente-javafx)
- [Uso rápido (ejemplos)](#-uso-rápido-ejemplos)
- [Notas de desarrollo](#-notas-de-desarrollo)
- [Roadmap / mejoras futuras](#-roadmap--mejoras-futuras)

---

## 🎮 Descripción general

**Aventura Submarina** es un sistema **cliente-servidor** para gestionar partidas multijugador de un juego por turnos.

El sistema se compone de:

- Un **cliente JavaFX** (interfaz de usuario) desde el que los jugadores:
  - se autentican
  - crean partidas
  - se unen/abandonan partidas
  - realizan acciones de juego
- Un **servidor Spring Boot** que expone una **API REST segura** y concentra toda la lógica de negocio:
  - reglas de partida
  - turnos
  - validación de acciones
  - control de permisos
- Un repositorio de datos basado en **MongoDB** para persistir usuarios/partidas/tokens.
- Apoyo de **Redis** para componentes que se benefician de cache/expiración (por ejemplo, elementos con TTL o timeouts).

---

## 🏗️ Arquitectura

La arquitectura es **tricapas** y sigue el flujo:

**Cliente (JavaFX) → Servidor (Spring Boot) → Persistencia (MongoDB)**

Esto permite desacoplar completamente:
- la interfaz (cliente),
- la lógica de negocio y seguridad (servidor),
- el almacenamiento (BD).

---

## 🧰 Tecnologías y herramientas

**Backend (Servidor)**
- Java + Spring Boot
- Spring Web (API REST)
- Spring Security (autorización por roles)
- JWT (tokens de acceso)
- Refresh tokens (renovación de sesión)
- OpenAPI/Swagger (documentación y prueba de endpoints)

**Persistencia**
- MongoDB (documentos)
- Redis (cache/TTL/timeouts)

**Frontend (Cliente)**
- JavaFX
- CSS para estilos

---

## ✨ Características principales

- ✅ API REST con endpoints semánticos y métodos HTTP bien definidos (GET/POST/PUT/PATCH/DELETE).
- ✅ Controladores separados por responsabilidad:
  - autenticación
  - usuarios
  - partidas
- ✅ Autenticación robusta:
  - acceso mediante token
  - renovación mediante refresh token
  - cierre de sesión e invalidación
- ✅ Autorización por roles:
  - `USER`
  - `ADMIN`
- ✅ Gestión completa del ciclo de vida de una partida:
  - crear
  - unirse
  - consultar estado
  - iniciar
  - ejecutar acciones
  - abandonar
- ✅ Enfoque HATEOAS (hipermedia): la API “sugiere” acciones posibles al cliente mediante enlaces.

---

## 🔐 Modelo de seguridad y autenticación

El sistema implementa un esquema moderno de autenticación:

1. **Login**: el usuario se autentica y recibe un **token de acceso (JWT)** para autorizar peticiones.
2. **Refresh**: cuando el JWT caduca, se obtiene uno nuevo mediante un **refresh token** (normalmente gestionado en cookie HttpOnly o mecanismo equivalente).
3. **Logout**: se invalidan tokens de forma explícita.

### Roles
- `USER`: rol estándar para jugar (crear partidas, unirse, actuar, etc.).
- `ADMIN`: rol reservado para tareas administrativas (si aplica).

---

## 🗄️ Persistencia y datos

El proyecto usa **MongoDB** como base de datos principal.

Colecciones típicas (orientativo según la implementación):
- `usuarios`
- `partidas`
- relación usuario-partidas (si se modela separado)
- tokens de refresco (si se almacenan server-side)

Además, se integra **Redis** para necesidades relacionadas con expiración/timeouts/caching (especialmente útil para tokens o estados transitorios).

---

## 🔗 HATEOAS / Hipermedia

El proyecto incorpora el enfoque **HATEOAS** (inspirado en HAL), es decir:

> El servidor no solo devuelve datos, sino también **las acciones disponibles** para el cliente en ese momento.

Ejemplo conceptual:
- Si la partida **no está empezada**, el servidor puede indicar un enlace `start`.
- Si el usuario está dentro, puede indicar un enlace `leave`.
- Siempre puede indicar `self` (autorreferencia al recurso).

> Nota: según la implementación, estos enlaces pueden devolverse en el cuerpo (estilo HAL) o en cabeceras HTTP (por ejemplo, cabecera `Link`). En ambos casos, el objetivo es el mismo: guiar al cliente sin que “adivine” URLs.

---

## 📡 Endpoints de la API

A continuación se listan los endpoints principales por controlador (resumen de la API).

> **Base URL** (ejemplo): `http://localhost:8082`

### Autenticación

- `POST /autenticacion/login`  
  Inicia sesión y obtiene token.

- `POST /autenticacion/refresh`  
  Renueva el token mediante refresh token.

- `POST /autenticacion/logout`  
  Cierra sesión e invalida tokens.

- `POST /autenticacion/register`  
  Registra un nuevo usuario.

---

### Usuarios

- `GET /usuarios`  
  Lista de usuarios (según permisos).

- `GET /usuarios/{id}`  
  Devuelve información de un usuario (normalmente el propio usuario autenticado).

- `DELETE /usuarios/{id}`  
  Elimina un usuario (según permisos).

- `GET /usuarios/{id}/partidasAcabadas`  
  Devuelve el histórico de partidas acabadas del usuario.

---

### Partidas

- `POST /partidas`  
  Crea una partida nueva.

- `GET /partidas/{id}`  
  Obtiene el estado actual de una partida.

- `PATCH /partidas/{id}`  
  Inicia la partida (si está permitido).

- `PATCH /partidas/{id}/jugadores`  
  Une al usuario autenticado a la partida.

- `PUT /partidas/{id}`  
  Envía una acción del jugador a la partida (movimiento/acción de juego).

- `DELETE /partidas/{idPartida}/jugadores/{idJugador}`  
  Elimina a un jugador de una partida (abandono / expulsión según reglas).

---

## 🚀 Cómo ejecutar el proyecto

### Requisitos previos

- Java (recomendado: 17+)
- Maven o Gradle (según build del proyecto)
- MongoDB en local **o** MongoDB Atlas (según configuración)
- Redis en local (si se usa en la ejecución)
- (Opcional) Docker / Docker Compose si lo usas para levantar servicios

---

## ⚙️ Configuración

La configuración se realiza en `application.properties` (o `application.yml`).

### MongoDB en local (recomendado para desarrollo)

Ejemplo:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/aventuraSubmarinaDB
