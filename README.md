# POS Retail — Sistema de Punto de Venta

Proyecto de curso virtual — **Análisis de Sistemas I**.

Sistema web de Punto de Venta desarrollado en **Java + Spring Boot + MySQL** que automatiza compras al contado y crédito a proveedores (con devoluciones) y ventas a clientes (con comprobantes), manteniendo control de inventario mediante Kardex.

---

## 📑 Contenido del repositorio

```
pos-retail/
├── docs/
│   └── DERCAS.md             ← Documento de requerimientos
├── sql/
│   └── schema.sql            ← Script MySQL con datos de ejemplo
├── src/main/java/com/posretail/
│   ├── config/               ← Spring Security, inicializador
│   ├── controller/           ← Controladores MVC
│   ├── dto/                  ← Objetos de transferencia
│   ├── model/                ← Entidades JPA
│   ├── repository/           ← Repositorios Spring Data
│   ├── security/             ← BCrypt, validador, handlers de login
│   └── service/              ← Lógica de negocio
├── src/main/resources/
│   ├── application.properties
│   └── templates/            ← Vistas Thymeleaf
├── pom.xml
└── README.md
```

---

## ✅ Requerimientos del proyecto cubiertos

### Módulo de Seguridad
- ✅ Bloqueo automático tras **3 intentos fallidos**.
- ✅ Recuperación de contraseña por **correo electrónico**.
- ✅ Contraseña con **8 a 11 caracteres** (más de 7 y menos de 12 según el enunciado), con mayúscula, minúscula, número y carácter especial.
- ✅ Contraseña **encriptada con BCrypt** en la base de datos.

### Módulo de Compras
- ✅ Compras al contado y crédito.
- ✅ **Devoluciones a proveedores**.
- ✅ **Kardex de productos** (entradas y devoluciones).

### Módulo de Ventas
- ✅ Ventas al contado y crédito.
- ✅ **Comprobante de venta** (factura).
- ✅ **Comprobante de pago** independiente.
- ✅ **Kardex de productos** (salidas).

---

## ⚙️ Requisitos previos

| Software | Versión |
|---|---|
| Java | 17 o superior |
| Maven | 3.8+ (o usar Maven wrapper) |
| MySQL | 8.0+ |
| Git | Cualquier versión |

---

## 🚀 Instalación paso a paso

### 1. Clonar el repositorio
```bash
git clone https://github.com/TU_USUARIO/pos-retail.git
cd pos-retail
```

### 2. Crear la base de datos
Abrir MySQL Workbench o la terminal de MySQL y ejecutar:
```bash
mysql -u root -p < sql/schema.sql
```
El script crea la base `pos_retail` con sus 16 tablas y datos iniciales (categorías, productos, proveedores, clientes).

### 3. Configurar credenciales de la BD
Editar `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pos_retail?...
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
```

### 4. (Opcional) Configurar correo para recuperación
En el mismo archivo:
```properties
spring.mail.username=tu_correo@gmail.com
spring.mail.password=tu_app_password
```
> Para Gmail: activa verificación en dos pasos y crea una "contraseña de aplicación".
> Si no configuras el correo, el enlace de recuperación se imprime en la consola.

### 5. Ejecutar la aplicación
```bash
./mvnw spring-boot:run
```
o en Windows:
```bash
mvnw.cmd spring-boot:run
```

### 6. Abrir el navegador
```
http://localhost:8080
```

---

## 🔑 Credenciales por defecto

| Usuario | Contraseña |
|---|---|
| `admin` | `Admin123!` |

El sistema crea automáticamente este usuario al primer arranque (con contraseña encriptada usando BCrypt).

---

## 🧪 Flujo de prueba sugerido

1. **Login**: ingresar con `admin` / `Admin123!`.
2. **Probar el bloqueo**: cerrar sesión y fallar 3 veces el login → usuario bloqueado.
3. **Recuperar**: clic en "Olvidé mi contraseña" → recibir enlace por correo o ver en consola.
4. **Compra**: registrar una compra a un proveedor con 2-3 productos → verificar que el stock aumente.
5. **Devolución**: en el detalle de la compra, registrar una devolución parcial → verificar Kardex.
6. **Venta**: registrar una venta al contado → ver el comprobante de venta y el comprobante de pago.
7. **Reportes**: revisar reporte de ventas, stock y Kardex.

---

## 🏗️ Arquitectura

```
Navegador
    ↓ HTTPS
Capa Web (Thymeleaf + Bootstrap 5)
    ↓
Controladores Spring MVC
    ↓
Servicios (Lógica de negocio)
    ↓
Repositorios JPA
    ↓
MySQL 8
```

---

## 📚 Documentación

- **DERCAS:** `docs/DERCAS.md`
- **Modelo ER:** incluido en el documento entregable.
- **Trello:** ver enlace en el documento entregable.
- **Video explicativo:** ver enlace en el documento entregable.

---

## 👤 Autor

Estudiante de Análisis de Sistemas I — 2026.
