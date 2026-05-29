-- =====================================================
-- POS Retail — Script de Base de Datos
-- MySQL 8.0+
-- =====================================================

DROP DATABASE IF EXISTS pos_retail;
CREATE DATABASE pos_retail CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pos_retail;

-- =========== SEGURIDAD ===========

CREATE TABLE rol (
    id_rol       INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(50) NOT NULL UNIQUE,
    descripcion  VARCHAR(150)
);

CREATE TABLE usuario (
    id_usuario          INT AUTO_INCREMENT PRIMARY KEY,
    id_rol              INT NOT NULL,
    username            VARCHAR(50) NOT NULL UNIQUE,
    email               VARCHAR(120) NOT NULL UNIQUE,
    password_hash       VARCHAR(255) NOT NULL,
    intentos_fallidos   INT NOT NULL DEFAULT 0,
    bloqueado           BOOLEAN NOT NULL DEFAULT FALSE,
    token_recuperacion  VARCHAR(100),
    token_expira        DATETIME,
    fecha_creacion      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);

CREATE TABLE intento_login (
    id_intento  INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario  INT,
    username    VARCHAR(50),
    fecha       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    exitoso     BOOLEAN NOT NULL,
    ip          VARCHAR(45),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- =========== CATÁLOGOS ===========

CREATE TABLE cliente (
    id_cliente  INT AUTO_INCREMENT PRIMARY KEY,
    nit         VARCHAR(20) NOT NULL UNIQUE,
    nombre      VARCHAR(120) NOT NULL,
    direccion   VARCHAR(200),
    telefono    VARCHAR(20),
    email       VARCHAR(120)
);

CREATE TABLE proveedor (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    nit          VARCHAR(20) NOT NULL UNIQUE,
    nombre       VARCHAR(120) NOT NULL,
    contacto     VARCHAR(120),
    telefono     VARCHAR(20),
    email        VARCHAR(120)
);

CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(80) NOT NULL UNIQUE,
    descripcion  VARCHAR(200)
);

CREATE TABLE producto (
    id_producto    INT AUTO_INCREMENT PRIMARY KEY,
    id_categoria   INT NOT NULL,
    codigo         VARCHAR(30) NOT NULL UNIQUE,
    nombre         VARCHAR(150) NOT NULL,
    precio_compra  DECIMAL(10,2) NOT NULL DEFAULT 0,
    precio_venta   DECIMAL(10,2) NOT NULL DEFAULT 0,
    stock_actual   INT NOT NULL DEFAULT 0,
    stock_minimo   INT NOT NULL DEFAULT 0,
    activo         BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);

-- =========== KARDEX ===========

CREATE TABLE kardex (
    id_kardex       INT AUTO_INCREMENT PRIMARY KEY,
    id_producto     INT NOT NULL,
    fecha           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento VARCHAR(20) NOT NULL, -- ENTRADA, SALIDA, DEVOLUCION_COMPRA
    cantidad        INT NOT NULL,
    costo_unitario  DECIMAL(10,2) NOT NULL,
    stock_anterior  INT NOT NULL,
    stock_nuevo     INT NOT NULL,
    referencia      VARCHAR(50), -- e.g. COMPRA-1, VENTA-3, DEVOL-2
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

-- =========== COMPRAS ===========

CREATE TABLE compra (
    id_compra    INT AUTO_INCREMENT PRIMARY KEY,
    id_proveedor INT NOT NULL,
    id_usuario   INT NOT NULL,
    fecha        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_pago    VARCHAR(20) NOT NULL, -- CONTADO, CREDITO
    subtotal     DECIMAL(10,2) NOT NULL,
    iva          DECIMAL(10,2) NOT NULL,
    total        DECIMAL(10,2) NOT NULL,
    estado       VARCHAR(20) NOT NULL DEFAULT 'ACTIVA', -- ACTIVA, ANULADA
    FOREIGN KEY (id_proveedor) REFERENCES proveedor(id_proveedor),
    FOREIGN KEY (id_usuario)   REFERENCES usuario(id_usuario)
);

CREATE TABLE detalle_compra (
    id_detalle      INT AUTO_INCREMENT PRIMARY KEY,
    id_compra       INT NOT NULL,
    id_producto     INT NOT NULL,
    cantidad        INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal        DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_compra)   REFERENCES compra(id_compra),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

CREATE TABLE devolucion_compra (
    id_devolucion INT AUTO_INCREMENT PRIMARY KEY,
    id_compra     INT NOT NULL,
    fecha         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    motivo        VARCHAR(200),
    total         DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_compra) REFERENCES compra(id_compra)
);

CREATE TABLE detalle_devolucion (
    id_detalle_dev  INT AUTO_INCREMENT PRIMARY KEY,
    id_devolucion   INT NOT NULL,
    id_producto     INT NOT NULL,
    cantidad        INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_devolucion) REFERENCES devolucion_compra(id_devolucion),
    FOREIGN KEY (id_producto)   REFERENCES producto(id_producto)
);

-- =========== VENTAS ===========

CREATE TABLE venta (
    id_venta   INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_usuario INT NOT NULL,
    fecha      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_pago  VARCHAR(20) NOT NULL, -- CONTADO, CREDITO
    subtotal   DECIMAL(10,2) NOT NULL,
    iva        DECIMAL(10,2) NOT NULL,
    total      DECIMAL(10,2) NOT NULL,
    estado     VARCHAR(20) NOT NULL DEFAULT 'ACTIVA',
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE detalle_venta (
    id_detalle      INT AUTO_INCREMENT PRIMARY KEY,
    id_venta        INT NOT NULL,
    id_producto     INT NOT NULL,
    cantidad        INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal        DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_venta)    REFERENCES venta(id_venta),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

CREATE TABLE comprobante_venta (
    id_comprobante INT AUTO_INCREMENT PRIMARY KEY,
    id_venta       INT NOT NULL UNIQUE,
    serie          VARCHAR(10) NOT NULL,
    numero         VARCHAR(20) NOT NULL,
    fecha_emision  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo           VARCHAR(20) NOT NULL DEFAULT 'FACTURA',
    FOREIGN KEY (id_venta) REFERENCES venta(id_venta)
);

CREATE TABLE pago (
    id_pago    INT AUTO_INCREMENT PRIMARY KEY,
    id_venta   INT NOT NULL,
    fecha      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    monto      DECIMAL(10,2) NOT NULL,
    metodo     VARCHAR(30) NOT NULL, -- EFECTIVO, TARJETA, TRANSFERENCIA
    referencia VARCHAR(60),
    FOREIGN KEY (id_venta) REFERENCES venta(id_venta)
);

-- =========== DATOS INICIALES ===========

INSERT INTO rol (nombre, descripcion) VALUES
('ADMIN', 'Administrador del sistema'),
('VENDEDOR', 'Encargado de ventas'),
('COMPRADOR', 'Encargado de compras');

-- Usuario admin: usuario=admin, contraseña=Admin123! (BCrypt cost 10)
INSERT INTO usuario (id_rol, username, email, password_hash) VALUES
(1, 'admin', 'admin@posretail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
-- Nota: cambia este hash usando BCryptPasswordEncoder en el primer arranque,
-- o registra el admin desde la app con la contraseña real.

INSERT INTO categoria (nombre, descripcion) VALUES
('Abarrotes', 'Productos de consumo básico'),
('Bebidas', 'Bebidas frías y calientes'),
('Limpieza', 'Productos de limpieza del hogar');

INSERT INTO proveedor (nit, nombre, contacto, telefono, email) VALUES
('1234567-8', 'Distribuidora Central S.A.', 'Juan Pérez', '2222-3333', 'ventas@distcentral.com'),
('9876543-2', 'Mayorista del Sur',          'Ana López',  '4444-5555', 'pedidos@maysur.com');

INSERT INTO cliente (nit, nombre, direccion, telefono, email) VALUES
('CF', 'Consumidor Final', 'Ciudad', '', ''),
('1122334-5', 'Tienda La Esquina', 'Zona 1', '5555-6666', 'tienda@esquina.com');

INSERT INTO producto (id_categoria, codigo, nombre, precio_compra, precio_venta, stock_actual, stock_minimo) VALUES
(1, 'P001', 'Arroz 1lb',           5.00, 7.50,  100, 20),
(1, 'P002', 'Frijol 1lb',          7.00, 10.00, 80,  15),
(2, 'P003', 'Coca Cola 600ml',     6.00, 9.00,  150, 30),
(2, 'P004', 'Agua Pura 600ml',     2.50, 4.00,  200, 40),
(3, 'P005', 'Detergente 1kg',     15.00, 22.00, 50,  10);
