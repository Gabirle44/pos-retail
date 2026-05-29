# POS Retail - Sistema de Punto de Venta

## Datos del estudiante

| | |
|---|---|
| **Nombre** | Eleazar Gabriel Francisco Juarez Ramazzini |
| **Carnet** | 5390-23-13757 |
| **Curso** | Analisis de Sistemas I |
| **Modalidad** | Virtual |
| **Anio** | 2026 |

---

## Descripcion del proyecto

Sistema web de Punto de Venta desarrollado en **Java + Spring Boot + MySQL** que automatiza compras al contado y credito a proveedores (con devoluciones) y ventas a clientes (con comprobantes), manteniendo control de inventario mediante Kardex.

---

## Requisitos previos

Antes de empezar, instala estos programas:

| Software | Version | Descarga |
|---|---|---|
| Java JDK | 17 | https://adoptium.net/temurin/releases/?version=17 |
| MySQL Server | 8.0+ | https://dev.mysql.com/downloads/installer/ |
| MySQL Workbench | (incluido con MySQL Installer) | igual que arriba |
| Apache NetBeans | 21 o superior | https://netbeans.apache.org/front/main/download/ |
| Git | (opcional, para clonar) | https://git-scm.com/download/win |

---

## Pasos para ejecutar el proyecto

### 1. Clonar el repositorio

**Con Git:**
```bash
git clone https://github.com/Gabirle44/pos-retail.git
cd pos-retail
```

**O sin Git:** descarga el ZIP desde GitHub (boton verde "Code -> Download ZIP") y descomprimelo.

---

### 2. Crear la base de datos en MySQL

1. Abre **MySQL Workbench**.
2. Conectate a tu servidor MySQL local.
3. Abre el archivo `sql/schema.sql` (menu **File -> Open SQL Script**).
4. Ejecutalo con el boton rayo amarillo (`Ctrl + Shift + Enter`).
5. Verifica en el panel SCHEMAS que aparezca la base **`pos_retail`** con 16 tablas.

---

### 3. Configurar la contrasena de MySQL

1. Abre el archivo `src/main/resources/application.properties`.
2. Busca esta linea:
```properties
spring.datasource.password=root
```
3. Cambiala por tu contrasena real de MySQL:
```properties
spring.datasource.password=TU_PASSWORD_DE_MYSQL
```
4. Guarda el archivo.

---

### 4. Crear el usuario admin en la base de datos

Ejecuta este SQL en MySQL Workbench:

```sql
USE pos_retail;

DELETE FROM intento_login WHERE id_usuario IN (SELECT id_usuario FROM usuario WHERE username = 'admin');
DELETE FROM usuario WHERE username = 'admin';

INSERT INTO usuario (id_rol, username, email, password_hash, intentos_fallidos, bloqueado)
VALUES (
    1,
    'admin',
    'admin@posretail.com',
    '$2a$10$WSxwCcnkh2VN7YAPCAWi.eVcjVQ2WngQyk3nV8L6aoWmRcTdDkKNC',
    0,
    false
);
```

Este SQL crea el usuario con la contrasena encriptada en BCrypt.

---

### 5. Abrir el proyecto en NetBeans

1. Abre **Apache NetBeans**.
2. Menu **File -> Open Project** (`Ctrl + Shift + O`).
3. Navega hasta la carpeta `pos-retail/` y seleccionala.
4. Espera a que NetBeans descargue todas las dependencias de Maven (5-10 minutos la primera vez).

---

### 6. Configurar JDK 17

1. Clic derecho sobre el proyecto **pos-retail** -> **Properties**.
2. Panel izquierdo: **Build**.
3. **Java Platform:** selecciona JDK 17. Si no aparece:
   - Clic en **Manage Java Platforms** -> **Add Platform**.
   - Selecciona la carpeta de tu JDK 17 instalado (normalmente en `C:\Program Files\Eclipse Adoptium\jdk-17...`).
4. Clic en **OK**.

---

### 7. Compilar el proyecto

Clic derecho sobre el proyecto -> **Clean and Build** (`Shift + F11`).

Espera a ver:
```
BUILD SUCCESS
```

---

### 8. Ejecutar el proyecto

Clic derecho sobre el proyecto -> **Run** (`F6`).

En la consola **Output** espera a ver:
```
Tomcat started on port 8080 (http)
Started PosRetailApplication in X.XXX seconds
```

---

### 9. Abrir en el navegador

Entra a:
```
http://localhost:8080
```

---

## Credenciales por defecto

| Usuario | Contrasena |
|---|---|
| `admin` | `Pos1234!` |

La contrasena cumple con las reglas del sistema:
- 8 caracteres (entre 8 y 11)
- Mayuscula: P
- Minusculas: o, s
- Numeros: 1, 2, 3, 4
- Caracter especial: !

---

## Flujo de prueba sugerido

1. **Login**: ingresa con `admin / Pos1234!`.
2. **Probar bloqueo**: cierra sesion y falla 3 veces el login -> usuario bloqueado.
3. **Recuperacion**: clic en "Olvidaste tu contrasena" -> el enlace aparece en la consola de NetBeans.
4. **Compra**: registra una compra a un proveedor con 2-3 productos -> verifica que el stock aumente.
5. **Devolucion**: en el detalle de la compra, registra una devolucion parcial.
6. **Venta**: registra una venta al contado -> ve el comprobante de venta y el comprobante de pago.
7. **Reportes**: revisa reportes de ventas, stock y Kardex.

---

## Estructura del proyecto

```
pos-retail/
├── docs/                          <- Documentacion
│   ├── DERCAS.md                  <- Documento de requerimientos
│   ├── PLAN_TRELLO.md             <- Plan de tablero Trello
│   └── GUION_VIDEO.md             <- Guion del video explicativo
├── sql/
│   └── schema.sql                 <- Script de la base de datos
├── src/main/java/com/posretail/
│   ├── config/                    <- Configuracion de Spring Security
│   ├── controller/                <- Controladores MVC
│   ├── dto/                       <- Objetos de transferencia
│   ├── model/                     <- Entidades JPA (16 tablas)
│   ├── repository/                <- Repositorios Spring Data
│   ├── security/                  <- BCrypt, validador, handlers
│   └── service/                   <- Logica de negocio
├── src/main/resources/
│   ├── application.properties     <- Configuracion de la app
│   └── templates/                 <- Vistas Thymeleaf
├── pom.xml                        <- Configuracion Maven
└── README.md                      <- Este archivo
```

---

## Modulos del sistema

### Modulo de Seguridad (Login)
- Bloqueo automatico tras 3 intentos fallidos
- Recuperacion de contrasena por correo electronico
- Validador de contrasena (8-11 caracteres, mayuscula, minuscula, numero, caracter especial)
- Contrasena encriptada con BCrypt en la base de datos

### Modulo de Compras
- Compras al contado y credito a proveedores
- Devoluciones a proveedores
- Kardex de productos (entradas y devoluciones)
- Calculo automatico de IVA (12%)

### Modulo de Ventas
- Ventas al contado y credito a clientes
- Comprobante de venta (factura)
- Comprobante de pago independiente
- Validacion de stock antes de confirmar la venta
- Kardex de productos (salidas)

---

## Tecnologias utilizadas

- **Java 17** + **Spring Boot 3.2.5**
- **Spring Security 6** con **BCrypt** para encriptacion de contrasenas
- **Spring Data JPA** + **Hibernate** para acceso a datos
- **Thymeleaf** + **Bootstrap 5** para las vistas
- **MySQL 8** como base de datos
- **Maven** para gestion de dependencias

---

## Problemas comunes

| Error | Solucion |
|---|---|
| `Access denied for user 'root'@'localhost'` | Mal la contrasena en `application.properties`. |
| `Unknown database 'pos_retail'` | No ejecutaste el `schema.sql` en MySQL. |
| `Port 8080 already in use` | Cierra la app anterior o cambia `server.port=8081` en properties. |
| `Failed to configure DataSource` | MySQL no esta corriendo. Inicia el servicio en `services.msc`. |
| `Could not find or load main class ${start-class}` | Ejecuta directamente `PosRetailApplication.java` con Run File. |
| `Credenciales incorrectas` al hacer login | Ejecuta el SQL del Paso 4 para crear el usuario admin. |

---

## Documentacion adicional

- **DERCAS:** ver `docs/DERCAS.md` con todos los requerimientos funcionales y no funcionales.
- **Modelo ER:** incluido en el documento entregable.
- **Trello:** ver enlace en el documento entregable.
- **Video explicativo:** ver enlace en el documento entregable.
