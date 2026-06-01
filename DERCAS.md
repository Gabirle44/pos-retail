# DERCAS — POS Retail

**Documento de Especificación de Requerimientos para el Análisis de Sistemas**

---

## 1. Introducción

### 1.1 Propósito
Este documento describe los requerimientos funcionales y no funcionales del sistema **POS Retail**, un punto de venta para la comercialización de productos al contado y crédito, con manejo de compras a proveedores, devoluciones y control de inventario por Kardex.

### 1.2 Alcance del producto
El sistema permite a una empresa comercializadora gestionar de forma automatizada:
- Autenticación y control de acceso de usuarios.
- Compras a proveedores con sus devoluciones.
- Ventas a clientes con generación de comprobantes y registro de pagos.
- Control de inventario mediante Kardex de productos.

### 1.3 Definiciones y acrónimos
| Término | Definición |
|---|---|
| POS | Point of Sale (Punto de Venta). |
| Kardex | Registro detallado de movimientos de entrada y salida de cada producto. |
| BCrypt | Algoritmo de hashing usado para almacenar contraseñas. |
| NIT | Número de Identificación Tributaria. |
| ER | Entidad-Relación. |

---

## 2. Descripción general

### 2.1 Perspectiva del producto
Sistema web autónomo desarrollado en Java con Spring Boot, accesible desde cualquier navegador. Base de datos relacional MySQL. Arquitectura MVC en tres capas (Controlador → Servicio → Repositorio).

### 2.2 Funciones del producto
- Inicio de sesión seguro con bloqueo por intentos fallidos.
- Recuperación de contraseña por correo electrónico.
- Registro de compras al contado y crédito.
- Devolución de compras al proveedor.
- Registro de ventas al contado y crédito.
- Emisión de comprobante de venta y comprobante de pago.
- Mantenimiento de catálogo de productos, clientes y proveedores.
- Kardex automatizado por cada movimiento.
- Reportes de ventas, compras y existencias.

### 2.3 Características del usuario
| Tipo de usuario | Descripción |
|---|---|
| Administrador | Acceso total al sistema, incluye gestión de usuarios y catálogos. |
| Vendedor | Registra ventas y consulta productos. |
| Comprador | Registra compras, devoluciones y mantiene proveedores. |

### 2.4 Restricciones
- La contraseña debe tener entre 8 y 11 caracteres, incluyendo mayúsculas, minúsculas, números y un carácter especial.
- La contraseña se almacena cifrada con BCrypt; nunca en texto plano.
- Tras 3 intentos fallidos, el usuario queda bloqueado.
- La recuperación es por enlace enviado al correo registrado.

---

## 3. Requerimientos funcionales

### RF-01 — Módulo de Seguridad (Login)

| Código | Requerimiento |
|---|---|
| RF-01.1 | El sistema debe validar usuario y contraseña antes de permitir el acceso. |
| RF-01.2 | Tras 3 intentos fallidos consecutivos, el usuario queda bloqueado. |
| RF-01.3 | El sistema debe permitir recuperación de contraseña mediante un enlace enviado al correo del usuario. |
| RF-01.4 | La contraseña debe cumplir: 8 a 11 caracteres, al menos una mayúscula, una minúscula, un número y un carácter especial. |
| RF-01.5 | La contraseña debe almacenarse encriptada con BCrypt en la base de datos. |
| RF-01.6 | El sistema debe registrar cada intento de inicio de sesión (exitoso o fallido). |

### RF-02 — Módulo de Compras

| Código | Requerimiento |
|---|---|
| RF-02.1 | El sistema debe permitir registrar compras al contado y al crédito a proveedores. |
| RF-02.2 | El sistema debe permitir registrar devoluciones de compra a proveedores. |
| RF-02.3 | Cada compra debe generar movimiento de entrada en el Kardex. |
| RF-02.4 | Cada devolución debe generar movimiento de salida en el Kardex. |
| RF-02.5 | El sistema debe actualizar automáticamente el stock del producto. |
| RF-02.6 | El sistema debe permitir consultar el historial de compras por proveedor y por fecha. |

### RF-03 — Módulo de Ventas

| Código | Requerimiento |
|---|---|
| RF-03.1 | El sistema debe permitir registrar ventas al contado y al crédito. |
| RF-03.2 | El sistema debe generar un comprobante de venta por cada operación. |
| RF-03.3 | El sistema debe generar un comprobante de pago independiente al comprobante de venta. |
| RF-03.4 | Cada venta debe generar movimiento de salida en el Kardex. |
| RF-03.5 | El sistema debe validar que exista stock suficiente antes de confirmar la venta. |
| RF-03.6 | El sistema debe actualizar automáticamente el stock del producto. |
| RF-03.7 | El sistema debe permitir consultar el historial de ventas y emitir reportes. |

---

## 4. Requerimientos no funcionales

| Código | Requerimiento |
|---|---|
| RNF-01 | Tiempo de respuesta menor a 2 segundos en operaciones comunes. |
| RNF-02 | El sistema debe ser accesible desde cualquier navegador moderno (Chrome, Firefox, Edge). |
| RNF-03 | Las contraseñas deben almacenarse con hashing BCrypt (factor de costo 10). |
| RNF-04 | Las sesiones deben expirar tras 30 minutos de inactividad. |
| RNF-05 | El sistema debe registrar log de todas las operaciones críticas. |
| RNF-06 | La interfaz debe ser responsiva y operable en pantallas de al menos 1024 px. |

---

## 5. Casos de uso principales

### CU-01: Iniciar sesión
- **Actor:** Usuario.
- **Precondición:** El usuario está registrado y no está bloqueado.
- **Flujo principal:**
  1. El usuario ingresa username y contraseña.
  2. El sistema valida credenciales.
  3. El sistema redirige al panel principal.
- **Flujo alterno:** Si la contraseña es incorrecta, se incrementa el contador de intentos fallidos. Al llegar a 3, el usuario se bloquea.

### CU-02: Registrar venta
- **Actor:** Vendedor.
- **Precondición:** Usuario autenticado con stock disponible.
- **Flujo principal:**
  1. Selecciona cliente.
  2. Agrega productos al detalle.
  3. Elige tipo de pago (contado o crédito).
  4. Confirma la venta.
  5. El sistema genera comprobante de venta, comprobante de pago y actualiza Kardex.

### CU-03: Registrar compra
- **Actor:** Comprador.
- **Precondición:** Usuario autenticado.
- **Flujo principal:**
  1. Selecciona proveedor.
  2. Agrega productos al detalle.
  3. Elige tipo de pago.
  4. Confirma la compra.
  5. El sistema actualiza Kardex y stock.

### CU-04: Devolución de compra
- **Actor:** Comprador.
- **Precondición:** La compra debe existir.
- **Flujo principal:**
  1. Selecciona la compra a devolver.
  2. Indica productos y cantidades a devolver.
  3. Confirma la devolución.
  4. El sistema actualiza Kardex y stock.

---

## 6. Arquitectura general

```
┌─────────────────────────────────────────┐
│  Navegador (Chrome / Firefox / Edge)    │
└──────────────────┬──────────────────────┘
                   │ HTTPS
┌──────────────────▼──────────────────────┐
│  Capa de Presentación (Thymeleaf + Bootstrap)
│  - Controladores Spring MVC             │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│  Capa de Negocio (Servicios)            │
│  - LoginService, VentaService,          │
│    CompraService, KardexService         │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│  Capa de Persistencia (Spring Data JPA) │
│  - Repositorios                         │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│  Base de Datos (MySQL 8)                │
└─────────────────────────────────────────┘
```

---

## 7. Metodología
El proyecto se desarrolla bajo metodología ágil con seguimiento en **Trello**. Cada módulo (Seguridad, Compras, Ventas) se gestiona como un sprint con sus tareas correspondientes (análisis, diseño, codificación, pruebas).
