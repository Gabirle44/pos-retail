# 📋 Plan de Trello — POS Retail

Este es el tablero que debes crear en **trello.com** para cumplir con el entregable de Trello (2 puntos).

---

## 🌐 Paso 1: Crear el tablero

1. Entra a https://trello.com y crea una cuenta gratuita (o usa la de Google).
2. Clic en **"Crear nuevo tablero"**.
3. Nombre del tablero: **POS Retail — Análisis de Sistemas I**
4. Visibilidad: **Workspace** o **Público** (importante: el catedrático debe poder verlo).
5. Una vez creado, ve a *Compartir → Cambiar visibilidad → Público* y copia el enlace.

---

## 📑 Paso 2: Crear las listas (columnas)

Crea exactamente estas 5 listas, en este orden:

1. **📌 Backlog** (todo lo pendiente)
2. **🔄 En progreso** (lo que estás trabajando ahora)
3. **🧪 Pruebas** (terminado pero falta probar)
4. **✅ Completado** (lo que ya funciona)
5. **📚 Documentación** (entregables del documento)

---

## 🃏 Paso 3: Crear las tarjetas

### Sprint 1 — Análisis y diseño (mover a "Completado")

| # | Tarjeta | Etiqueta |
|---|---|---|
| 1 | Identificar requerimientos funcionales del POS | 🔵 Análisis |
| 2 | Elaborar documento DERCAS | 🔵 Análisis |
| 3 | Diseñar Modelo Entidad-Relación | 🟣 Diseño |
| 4 | Definir arquitectura del sistema (Spring Boot + MySQL) | 🟣 Diseño |
| 5 | Crear estructura del proyecto Maven | 🟣 Diseño |

### Sprint 2 — Módulo de Seguridad (Login)

| # | Tarjeta | Etiqueta |
|---|---|---|
| 6 | Diseñar tabla `usuario` y `rol` | 🟢 Login |
| 7 | Implementar entidad `Usuario` con JPA | 🟢 Login |
| 8 | Configurar Spring Security con BCrypt | 🟢 Login |
| 9 | Implementar validador de contraseña (8-11, mayúscula, minúscula, número, especial) | 🟢 Login |
| 10 | Implementar bloqueo tras 3 intentos fallidos | 🟢 Login |
| 11 | Implementar recuperación de contraseña por correo | 🟢 Login |
| 12 | Crear vista de login | 🟢 Login |
| 13 | Crear vista de recuperación | 🟢 Login |
| 14 | Probar bloqueo con 3 intentos | 🟢 Login |

### Sprint 3 — Módulo de Compras

| # | Tarjeta | Etiqueta |
|---|---|---|
| 15 | Diseñar tablas de compras y proveedores | 🟡 Compras |
| 16 | Implementar entidades `Compra`, `DetalleCompra`, `Proveedor` | 🟡 Compras |
| 17 | Implementar `CompraService` con cálculo de IVA y total | 🟡 Compras |
| 18 | Implementar `KardexService` con movimientos de entrada | 🟡 Compras |
| 19 | Crear vista de nueva compra (formulario dinámico) | 🟡 Compras |
| 20 | Crear vista de lista de compras | 🟡 Compras |
| 21 | Implementar devolución a proveedor | 🟡 Compras |
| 22 | Crear vista de devolución | 🟡 Compras |
| 23 | Probar flujo completo compra + devolución + kardex | 🟡 Compras |

### Sprint 4 — Módulo de Ventas

| # | Tarjeta | Etiqueta |
|---|---|---|
| 24 | Diseñar tablas de ventas, clientes, comprobantes y pagos | 🟠 Ventas |
| 25 | Implementar entidades `Venta`, `DetalleVenta`, `Cliente` | 🟠 Ventas |
| 26 | Implementar entidades `ComprobanteVenta` y `Pago` | 🟠 Ventas |
| 27 | Implementar `VentaService` con generación de comprobantes | 🟠 Ventas |
| 28 | Validar stock antes de confirmar venta | 🟠 Ventas |
| 29 | Generar movimiento de salida en Kardex al vender | 🟠 Ventas |
| 30 | Crear vista de nueva venta | 🟠 Ventas |
| 31 | Crear vista del comprobante de venta + pago | 🟠 Ventas |
| 32 | Crear reporte de ventas | 🟠 Ventas |

### Sprint 5 — Entregables

| # | Tarjeta | Etiqueta |
|---|---|---|
| 33 | Subir código a GitHub (público) | 🔴 Entregable |
| 34 | Grabar video explicativo (5-7 min) | 🔴 Entregable |
| 35 | Subir video a OneDrive y compartir enlace | 🔴 Entregable |
| 36 | Elaborar PDF final con todos los enlaces | 🔴 Entregable |

---

## 🎨 Etiquetas a crear

En Trello → tablero → menú → Etiquetas, crea estas con colores:

- 🔵 **Análisis** (azul)
- 🟣 **Diseño** (morado)
- 🟢 **Login** (verde)
- 🟡 **Compras** (amarillo)
- 🟠 **Ventas** (naranja)
- 🔴 **Entregable** (rojo)

---

## 📸 Captura para el documento

Una vez que tengas todas las tarjetas creadas y distribuidas por columnas (la mayoría en "Completado" al momento de la entrega), saca una captura del tablero completo y pégala en el PDF entregable. También incluye el **enlace público al tablero**.

### Distribución sugerida al momento de entregar:

- **Backlog:** vacío
- **En progreso:** vacío
- **Pruebas:** 1-2 tarjetas (las del video por ejemplo)
- **Completado:** todas las del código (28 tarjetas)
- **Documentación:** las 4 tarjetas del Sprint 5

Esto demuestra que el proyecto avanzó por sprints y está completo.

---

## ✅ Cómo verificar que el catedrático puede ver tu tablero

1. Abre el tablero en **modo incógnito** del navegador (sin estar logueado).
2. Si lo ves, está público y bien configurado.
3. Si te pide login, regresa a *Compartir → Visibilidad → Público*.
