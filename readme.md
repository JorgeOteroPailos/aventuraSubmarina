# Aventura Submarina 🌊⚓️  
**API REST + Cliente JavaFX para un juego multijugador por turnos**  

> Proyecto desarrollado como parte de un trabajo académico para la asignatura de Enxeñaría de Servizos
> El objetivo es implementar un sistema cliente-servidor completo para gestionar usuarios, autenticación y partidas de un juego llamado **Aventura Submarina**, realizando la comunicación mediante una API REST.

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

**Cliente (JavaFX) → Servidor (Spring Boot) → Persistencia (MongoDB, Redis)**

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

**Persistencia**
- MongoDB (documentos)
- Redis (tokens de refresco, partidas activas)

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
- ✅ Enfoque HATEOAS (cabeceras link): la API “sugiere” acciones posibles al cliente mediante enlaces.

---

## 🔐 Modelo de seguridad y autenticación

El sistema implementa un esquema moderno de autenticación:

1. **Login**: el usuario se autentica y recibe un **token de acceso (JWT)** para autorizar peticiones.
2. **Refresh**: cuando el JWT caduca, se obtiene uno nuevo mediante un **refresh token** (token opaco almacenado en una cookie).
3. **Logout**: se invalidan tokens de forma explícita.

### Roles
- `USER`: rol estándar para jugar (crear partidas, unirse, actuar, etc.).
- `ADMIN`: rol reservado para tareas administrativas (de momento, solo listar todos los usuarios).

---

## 🗄️ Persistencia y datos

El proyecto usa **MongoDB** como base de datos principal, para almacenar usuarios y partidas ya finalizadas.

Además, en **Redis** se almacenan objetos con un corto tiempo de vida o que esperan un acceso frecuente: los tokens de refresco y las partidas activas.

El proyecto está pensado para ejecutar MongoDB en un servidor web y Redis en un contenedor local, pero el diseño modular permitiría cambiarlo muy fácilmente solo mediante archivos de configuración.

---

## 🔗 HATEOAS / Hipermedia

El proyecto incorpora el enfoque **HATEOAS** mediante cabeceras link, es decir:

> El servidor no solo devuelve datos, sino también **las acciones disponibles** para el cliente en ese momento.

Ejemplo conceptual:
- Si la partida **no está empezada**, el servidor indica un enlace `start`.
- Si el usuario está dentro, indica un enlace `leave`.


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
- Gradle
- MongoDB
- Redis 

---

## ⚙️ Configuración

La configuración se realiza en `application.properties`

