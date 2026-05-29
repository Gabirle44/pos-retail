# 🎥 Guion del video explicativo — POS Retail

**Duración objetivo:** 6 minutos (margen del rango 5-7 min)
**Herramienta sugerida:** OBS Studio (gratuito) o la herramienta de grabación de pantalla de Windows (Win + G).
**Resolución:** 1080p mínimo.

---

## 📐 Estructura general

| Tiempo | Sección |
|---|---|
| 0:00 - 0:30 | Introducción |
| 0:30 - 1:30 | Login y módulo de Seguridad |
| 1:30 - 3:00 | Módulo de Ventas |
| 3:00 - 4:30 | Módulo de Compras |
| 4:30 - 5:30 | Explicación del código |
| 5:30 - 6:00 | Explicación de la base de datos y cierre |

---

## 🎬 GUION DETALLADO

### 🎙️ 0:00 - 0:30 — Introducción

> "Hola, soy [tu nombre], estudiante de Análisis de Sistemas I. En este video voy a presentar el proyecto **POS Retail**, un sistema de punto de venta desarrollado en **Java con Spring Boot y MySQL**. El sistema cumple con los tres módulos solicitados: Seguridad, Compras y Ventas, con devoluciones a proveedores y Kardex de productos. Empezamos."

**🎬 Acción en pantalla:** mostrar el README en GitHub o el dashboard inicial.

---

### 🎙️ 0:30 - 1:30 — Login y Seguridad

> "Primero el módulo de Login. La pantalla pide usuario y contraseña. Voy a ingresar credenciales incorrectas para mostrar el bloqueo automático..."

**🎬 Acción:**
1. Ingresar usuario `admin` con contraseña incorrecta → mostrar mensaje "Intentos restantes: 2".
2. Repetir → "Intentos restantes: 1".
3. Repetir tercera vez → "Usuario bloqueado tras 3 intentos fallidos".

> "Como ven, el sistema bloqueó al usuario al tercer intento, exactamente como lo pide el enunciado."

**🎬 Acción:** clic en "Olvidé mi contraseña".

> "Aquí está la recuperación por correo. El sistema envía un enlace al correo registrado. Como estoy en local, el enlace se imprime también en la consola para pruebas."

> "Las contraseñas se guardan **encriptadas con BCrypt** en la base de datos, nunca en texto plano. La regla del enunciado es: entre 8 y 11 caracteres, con mayúscula, minúscula, número y carácter especial. Esto se valida tanto en el frontend como en el backend."

**🎬 Acción:** ingresar con `admin` / `Admin123!` → entrar al dashboard.

---

### 🎙️ 1:30 - 3:00 — Módulo de Ventas

> "Ahora el módulo de Ventas. Voy a registrar una nueva venta."

**🎬 Acción:** clic en "Nueva venta".

> "Selecciono el cliente, el tipo de pago (contado), el método (efectivo). Agrego dos productos: Arroz 1lb x5 y Coca Cola 600ml x3. Ven que el precio se carga automáticamente del producto y que el sistema calcula el subtotal, el IVA del 12% y el total en tiempo real."

**🎬 Acción:** llenar el formulario, ver cálculos automáticos, registrar.

> "Al confirmar, el sistema genera dos documentos: el **comprobante de venta** (factura con serie, número, detalle y totales) y el **comprobante de pago** independiente, que es uno de los requisitos del enunciado."

**🎬 Acción:** mostrar la pantalla del comprobante con ambos documentos.

> "Ahora vamos al stock para comprobar que las cantidades se descontaron."

**🎬 Acción:** menú → Stock → mostrar que el stock de los dos productos vendidos disminuyó.

> "Y en el Kardex queda registrado el movimiento de SALIDA con stock anterior, stock nuevo y referencia a la venta."

**🎬 Acción:** menú → Kardex → mostrar movimientos tipo SALIDA con referencia `VENTA-X`.

---

### 🎙️ 3:00 - 4:30 — Módulo de Compras

> "El módulo de Compras funciona de forma similar. Selecciono proveedor, tipo de pago y agrego productos. Esta vez los precios son los de compra."

**🎬 Acción:** registrar una compra con 2 productos.

> "Al confirmar, el stock aumenta y se genera un movimiento de ENTRADA en el Kardex."

**🎬 Acción:** ir a Stock → mostrar incremento.

> "Ahora la parte de devoluciones a proveedor, que es un requerimiento explícito del enunciado. Entro al detalle de la compra y clic en Devolución."

**🎬 Acción:** entrar a la compra, clic en devolver.

> "Selecciono los productos a devolver, la cantidad y el motivo. Confirmo la devolución."

**🎬 Acción:** marcar checkbox, ajustar cantidad, escribir motivo, confirmar.

> "El sistema descuenta nuevamente del stock y registra en el Kardex un movimiento tipo DEVOLUCION_COMPRA con la referencia correspondiente."

**🎬 Acción:** mostrar Kardex con el nuevo movimiento.

---

### 🎙️ 4:30 - 5:30 — Explicación del código

> "Brevemente sobre la arquitectura del código. El proyecto sigue el patrón **MVC en tres capas**: controladores, servicios y repositorios."

**🎬 Acción:** abrir el IDE (IntelliJ o VS Code), mostrar la estructura de carpetas.

> "En la carpeta **`model`** están las 16 entidades JPA que mapean cada tabla de la base de datos. En **`repository`** los repositorios Spring Data que dan acceso a la BD sin escribir SQL manual."

**🎬 Acción:** abrir `Usuario.java`, mostrar las anotaciones.

> "En **`service`** está la lógica de negocio. El `VentaService` por ejemplo valida el stock, calcula totales, genera el comprobante de venta y el comprobante de pago, y llama al `KardexService` para registrar el movimiento."

**🎬 Acción:** abrir `VentaService.java` y hacer scroll mostrando el método `registrarVenta`.

> "En **`security`** está el `PasswordValidator` que aplica la regla del enunciado, el `LoginEventHandlers` que cuenta intentos y bloquea al tercer fallo, y el `SecurityConfig` con BCrypt."

**🎬 Acción:** abrir `LoginEventHandlers.java`, señalar la constante `MAX_INTENTOS = 3`.

> "En **`controller`** están los controladores Spring MVC que reciben las peticiones HTTP. Las vistas son plantillas Thymeleaf con Bootstrap 5."

---

### 🎙️ 5:30 - 6:00 — Base de datos y cierre

> "La base de datos en MySQL tiene 16 tablas organizadas en cuatro bloques: seguridad (usuario, rol, intento_login), catálogos (producto, categoría, cliente, proveedor), compras (compra, detalle_compra, devolucion_compra, detalle_devolucion) y ventas (venta, detalle_venta, comprobante_venta, pago). La tabla `kardex` es transversal y registra todos los movimientos de inventario."

**🎬 Acción:** abrir MySQL Workbench, mostrar el diagrama EER o las tablas con `SHOW TABLES`.

> "Con esto cierro la presentación. El código completo está público en GitHub y el tablero de Trello documenta todo el avance por sprints. Gracias por su atención."

**🎬 Acción:** mostrar el enlace de GitHub y la pantalla del Trello.

---

## 🎤 Tips para grabar

1. **Habla pausado.** Si el video te queda corto, mejor; siempre puedes agregar pausas.
2. **Cierra notificaciones** del sistema y del navegador antes de empezar.
3. **Usa un solo monitor** o configura OBS para grabar solo uno.
4. **Prepara los datos antes**: ten al menos 5 productos cargados, 2 clientes y 2 proveedores listos en la BD para no tener que cargarlos en vivo.
5. **Practica una vez** sin grabar, para que la segunda toma sea fluida.
6. **Si te equivocas**, sigue grabando: en post-producción puedes recortar o regrabar solo ese trozo.

---

## 📤 Cómo subir el video a OneDrive

1. Sube el archivo a tu OneDrive personal (o el del Microsoft 365 estudiantil).
2. Clic derecho sobre el archivo → **Compartir** → **Cualquier persona con el enlace**.
3. Cambia el permiso a **solo lectura** (no edición).
4. Copia el enlace y pégalo en el PDF entregable.
5. **Verifica el enlace en modo incógnito** para confirmar que se puede ver sin estar logueado.

> ⚠️ Si el enlace no es accesible públicamente, pierdes los 2 puntos del video según el enunciado.

---

## ✅ Checklist final antes de grabar

- [ ] BD `pos_retail` cargada con datos.
- [ ] Aplicación corriendo en `http://localhost:8080`.
- [ ] Usuario `admin` con contraseña conocida.
- [ ] Al menos 5 productos con stock > 0.
- [ ] Al menos 2 clientes (uno consumidor final y uno con NIT).
- [ ] Al menos 2 proveedores.
- [ ] IDE abierto con el proyecto.
- [ ] MySQL Workbench listo con el diagrama EER.
- [ ] OBS o grabador de pantalla probado.
- [ ] Micrófono probado.
- [ ] Guion impreso o en pantalla secundaria.
