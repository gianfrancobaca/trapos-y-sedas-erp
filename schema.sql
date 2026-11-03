-- ============================================================================
-- Trapos y Sedas ERP — esquema de base de datos (MySQL 8+)
--
-- Deducido de las consultas SQL de los repositorios de los módulos 1, 2 y 3
-- (nombres de tablas y columnas, tipos de getters/setters y enums del código).
-- El módulo 4 (administración) vive en otra rama y no está incluido.
--
-- Uso:   mysql -u root -p < schema.sql
-- Es para una instalación nueva: si ya existe la base y quieres empezar de cero,
-- bórrala antes con  DROP DATABASE trapos_y_sedas;
--
-- Decisiones de diseño:
--  * Claves foráneas solo donde la relación es clara dentro de un módulo y entre
--    ventas y clientes. Las columnas usuario_id del módulo 1 no llevan FK porque
--    el código las recibe del llamador sin garantizar que exista el usuario.
--  * Los enums de Java se guardan como ENUM de MySQL (el código usa valueOf()).
--  * Los importes son DECIMAL(10,2); los repositorios los leen con getDouble().
--  * El módulo 1 (inv_productos) y el módulo 3 (productos) usan tablas de
--    producto separadas, tal como las consultan sus repositorios.
-- ============================================================================

CREATE DATABASE IF NOT EXISTS trapos_y_sedas
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE trapos_y_sedas;

-- ───────────────────────── Usuarios y credenciales ──────────────────────────

CREATE TABLE usuario (
    id_usuario  INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(80)  NOT NULL,
    apellido    VARCHAR(80)  NOT NULL,
    email       VARCHAR(120) NOT NULL UNIQUE,
    rol         VARCHAR(30)  NOT NULL
);

CREATE TABLE credencial (
    id_credencial INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(60)  NOT NULL UNIQUE,
    password      VARCHAR(100) NOT NULL,          -- hash BCrypt (60 caracteres)
    id_usuario    INT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

-- ───────────────────────── Módulo 2: gestión de clientes ────────────────────

CREATE TABLE categoria_cliente (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(60)  NOT NULL,
    descripcion  VARCHAR(200),
    descuento    DECIMAL(5,2) NOT NULL DEFAULT 0     -- porcentaje
);

CREATE TABLE cliente (
    id_cliente   INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(80)  NOT NULL,
    apellido     VARCHAR(80)  NOT NULL,
    email        VARCHAR(120),
    direccion    VARCHAR(200),
    id_categoria INT NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categoria_cliente(id_categoria)
);

CREATE TABLE programa_fidelizacion (
    id_programa       INT AUTO_INCREMENT PRIMARY KEY,
    nombre            VARCHAR(80) NOT NULL,
    puntos_acumulados INT NOT NULL DEFAULT 0,
    nivel             VARCHAR(30),
    id_cliente        INT NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente) ON DELETE CASCADE
);

CREATE TABLE metodo_pago (
    id_metodo_pago INT AUTO_INCREMENT PRIMARY KEY,
    tipo           VARCHAR(40) NOT NULL,
    descripcion    VARCHAR(200),
    id_cliente     INT NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente) ON DELETE CASCADE
);

CREATE TABLE pedido (
    id_pedido  INT AUTO_INCREMENT PRIMARY KEY,
    fecha      DATE NOT NULL,
    estado     VARCHAR(30) NOT NULL,
    id_cliente INT NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

CREATE TABLE factura (
    id_factura INT AUTO_INCREMENT PRIMARY KEY,
    fecha      DATE NOT NULL,
    total      DECIMAL(10,2) NOT NULL,
    estado     VARCHAR(30) NOT NULL,
    id_pedido  INT NOT NULL,
    id_cliente INT NOT NULL,
    FOREIGN KEY (id_pedido)  REFERENCES pedido(id_pedido),
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

CREATE TABLE notificacion (
    id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
    mensaje         VARCHAR(300) NOT NULL,
    tipo            VARCHAR(30)  NOT NULL,
    fecha           DATE NOT NULL,
    leida           BOOLEAN NOT NULL DEFAULT FALSE,
    id_cliente      INT NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente) ON DELETE CASCADE
);

CREATE TABLE reclamo (
    id_reclamo INT AUTO_INCREMENT PRIMARY KEY,
    motivo     VARCHAR(300) NOT NULL,
    estado     VARCHAR(30)  NOT NULL,
    fecha      DATE NOT NULL,
    id_cliente INT NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

-- ───────────────────────── Módulo 1: inventario ─────────────────────────────

CREATE TABLE inv_categorias (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(80) NOT NULL,
    descripcion VARCHAR(200),
    activo      BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE inv_proveedores (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    tipo_documento   ENUM('DNI','RUC','NINGUNO') DEFAULT 'NINGUNO',
    numero_documento VARCHAR(20),
    razon_social     VARCHAR(120) NOT NULL,
    contacto         VARCHAR(80),
    telefono         VARCHAR(30),
    correo           VARCHAR(120),
    direccion        VARCHAR(200),
    activo           BOOLEAN NOT NULL DEFAULT TRUE,
    INDEX idx_prov_documento (numero_documento)
);

CREATE TABLE inv_productos (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    codigo           VARCHAR(30) NOT NULL UNIQUE,
    nombre           VARCHAR(120) NOT NULL,
    descripcion      VARCHAR(300),
    categoria_id     INT,
    categoria_nombre VARCHAR(80),                  -- copia desnormalizada que usa el modelo
    precio_compra    DECIMAL(10,2) NOT NULL DEFAULT 0,
    precio_venta     DECIMAL(10,2) NOT NULL DEFAULT 0,
    stock_actual     INT NOT NULL DEFAULT 0,
    stock_minimo     INT NOT NULL DEFAULT 0,
    estado           ENUM('DISPONIBLE','RESERVADO','VENDIDO','DESCONTINUADO') NOT NULL DEFAULT 'DISPONIBLE',
    activo           BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (categoria_id) REFERENCES inv_categorias(id)
);

CREATE TABLE inv_movimientos (
    id                   INT AUTO_INCREMENT PRIMARY KEY,
    producto_id          INT NOT NULL,
    producto_nombre      VARCHAR(120),
    tipo                 ENUM('ENTRADA','SALIDA','AJUSTE','DEVOLUCION') NOT NULL,
    cantidad             INT NOT NULL,
    stock_anterior       INT NOT NULL,
    stock_resultante     INT NOT NULL,
    motivo               VARCHAR(200),
    referencia_documento VARCHAR(60),
    fecha_hora           DATETIME NOT NULL,
    usuario_id           INT,
    INDEX idx_mov_producto (producto_id),
    FOREIGN KEY (producto_id) REFERENCES inv_productos(id)
);

CREATE TABLE inv_ajustes (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    tipo       ENUM('CONTEO_FISICO','CORRECCION_ERROR','MERMA','OTRO') NOT NULL,
    motivo     VARCHAR(200),
    fecha_hora DATETIME NOT NULL,
    usuario_id INT
);

CREATE TABLE inv_detalle_ajuste (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    ajuste_id       INT NOT NULL,
    producto_id     INT NOT NULL,
    producto_nombre VARCHAR(120),
    stock_anterior  INT NOT NULL,
    stock_nuevo     INT NOT NULL,
    diferencia      INT NOT NULL,
    FOREIGN KEY (ajuste_id)   REFERENCES inv_ajustes(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES inv_productos(id)
);

CREATE TABLE inv_devoluciones (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    tipo          ENUM('CLIENTE','PROVEEDOR') NOT NULL,
    referencia_id INT,                             -- id del cliente o proveedor, según el tipo
    motivo        VARCHAR(200),
    estado        ENUM('PENDIENTE','APROBADA','RECHAZADA','COMPLETADA') NOT NULL DEFAULT 'PENDIENTE',
    fecha_hora    DATETIME NOT NULL,
    usuario_id    INT
);

CREATE TABLE inv_detalle_devolucion (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    devolucion_id   INT NOT NULL,
    producto_id     INT NOT NULL,
    producto_nombre VARCHAR(120),
    cantidad        INT NOT NULL,
    motivo_linea    VARCHAR(200),
    FOREIGN KEY (devolucion_id) REFERENCES inv_devoluciones(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id)   REFERENCES inv_productos(id)
);

CREATE TABLE inv_ordenes_compra (
    id                     INT AUTO_INCREMENT PRIMARY KEY,
    proveedor_id           INT NOT NULL,
    proveedor_nombre       VARCHAR(120),
    fecha_emision          DATE NOT NULL,
    fecha_entrega_estimada DATE,
    estado                 ENUM('PENDIENTE','APROBADA','RECIBIDA','CANCELADA') NOT NULL DEFAULT 'PENDIENTE',
    total                  DECIMAL(10,2) NOT NULL DEFAULT 0,
    observaciones          VARCHAR(300),
    FOREIGN KEY (proveedor_id) REFERENCES inv_proveedores(id)
);

CREATE TABLE inv_detalle_orden_compra (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    orden_id        INT NOT NULL,
    producto_id     INT NOT NULL,
    producto_nombre VARCHAR(120),
    cantidad        INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal        DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (orden_id)    REFERENCES inv_ordenes_compra(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES inv_productos(id)
);

-- ───────────────────────── Módulo 3: ventas ─────────────────────────────────

CREATE TABLE productos (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    codigo           VARCHAR(30) NOT NULL UNIQUE,
    nombre           VARCHAR(120) NOT NULL,
    descripcion      VARCHAR(300),
    categoria        ENUM('VESTIDO','BLUSA','PANTALON','FALDA','ACCESORIO','CALZADO','OTRO') NOT NULL,
    precio_venta     DECIMAL(10,2) NOT NULL,
    stock_disponible INT NOT NULL DEFAULT 0,
    activo           BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE ventas (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id    INT NOT NULL,
    vendedor_id   INT NOT NULL,
    fecha_hora    DATETIME NOT NULL,
    estado        ENUM('PENDIENTE','COMPLETADA','CANCELADA','DEVUELTA') NOT NULL,
    metodo_pago   ENUM('EFECTIVO','TARJETA_CREDITO','TARJETA_DEBITO','TRANSFERENCIA','YAPE','PLIN') NOT NULL,
    subtotal      DECIMAL(10,2) NOT NULL,
    descuento     DECIMAL(10,2) NOT NULL DEFAULT 0,
    igv           DECIMAL(10,2) NOT NULL,
    total         DECIMAL(10,2) NOT NULL,
    observaciones VARCHAR(300),
    INDEX idx_ventas_fecha (fecha_hora),
    FOREIGN KEY (cliente_id)  REFERENCES cliente(id_cliente),
    FOREIGN KEY (vendedor_id) REFERENCES usuario(id_usuario)
);

CREATE TABLE detalle_ventas (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    venta_id        INT NOT NULL,
    producto_id     INT NOT NULL,
    producto_nombre VARCHAR(120) NOT NULL,
    cantidad        INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    descuento_linea DECIMAL(10,2) NOT NULL DEFAULT 0,
    subtotal        DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (venta_id)    REFERENCES ventas(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);

CREATE TABLE pagos (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    venta_id   INT NOT NULL,
    monto      DECIMAL(10,2) NOT NULL,
    metodo     ENUM('EFECTIVO','TARJETA_CREDITO','TARJETA_DEBITO','TRANSFERENCIA','YAPE','PLIN') NOT NULL,
    estado     ENUM('PENDIENTE','APROBADO','RECHAZADO','REEMBOLSADO') NOT NULL,
    fecha_pago DATETIME NOT NULL,
    referencia VARCHAR(80),
    FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE
);

CREATE TABLE comprobantes_venta (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    venta_id      INT NOT NULL,
    tipo          ENUM('BOLETA','FACTURA','NOTA_CREDITO') NOT NULL,
    serie         VARCHAR(10) NOT NULL,
    numero        INT NOT NULL,
    fecha_emision DATETIME NOT NULL,
    monto_total   DECIMAL(10,2) NOT NULL,
    anulado       BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE KEY uq_comprobante (tipo, serie, numero),
    FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE
);

CREATE TABLE descuentos (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(80) NOT NULL,
    descripcion  VARCHAR(200),
    tipo         ENUM('PORCENTAJE','MONTO_FIJO','OFERTA_2X1','DESCUENTO_TEMPORADA') NOT NULL,
    valor        DECIMAL(10,2) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin    DATE NOT NULL,
    activo       BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE reservas (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id       INT NOT NULL,
    producto_id      INT NOT NULL,
    fecha_reserva    DATETIME NOT NULL,
    fecha_expiracion DATETIME NOT NULL,
    estado           ENUM('ACTIVA','CONFIRMADA','VENCIDA','CANCELADA') NOT NULL DEFAULT 'ACTIVA',
    observaciones    VARCHAR(300),
    FOREIGN KEY (cliente_id)  REFERENCES cliente(id_cliente),
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);

-- ============================================================================
-- DATOS DE PRUEBA (ficticios)
-- ============================================================================

-- Usuario de prueba para iniciar sesión:  admin / Admin123!   (hash BCrypt, coste 12)
INSERT INTO usuario (nombre, apellido, email, rol)
VALUES ('Demo', 'Admin', 'admin@traposysedas.test', 'ADMIN');

INSERT INTO credencial (username, password, id_usuario)
VALUES ('admin', '$2a$12$4B0pLK52mNzonwffnXbVJ.UounSJpl9/1TF.XJ/TfuFI1Be/7nEjy', 1);

INSERT INTO categoria_cliente (nombre, descripcion, descuento) VALUES
    ('Regular', 'Cliente sin beneficios especiales', 0),
    ('Frecuente', 'Más de 5 compras al año', 5),
    ('VIP', 'Clientes de alto consumo', 10);

INSERT INTO cliente (nombre, apellido, email, direccion, id_categoria) VALUES
    ('Lucía',  'Ramírez', 'lucia.ramirez@example.com', 'Av. Primavera 123, Lima', 2),
    ('Mateo',  'Quispe',  'mateo.quispe@example.com',  'Jr. Los Olivos 456, Lima', 1),
    ('Valeria','Torres',  'valeria.torres@example.com','Calle Las Flores 789, Lima', 3);

INSERT INTO programa_fidelizacion (nombre, puntos_acumulados, nivel, id_cliente) VALUES
    ('Club Sedas', 120, 'PLATA', 1),
    ('Club Sedas', 450, 'ORO', 3);

INSERT INTO metodo_pago (tipo, descripcion, id_cliente) VALUES
    ('TARJETA', 'Visa terminada en 4242 (ficticia)', 1),
    ('YAPE', 'Cuenta Yape del cliente', 2);

INSERT INTO pedido (fecha, estado, id_cliente) VALUES
    ('2026-06-10', 'ENTREGADO', 1),
    ('2026-06-15', 'PENDIENTE', 3);

INSERT INTO factura (fecha, total, estado, id_pedido, id_cliente) VALUES
    ('2026-06-10', 236.00, 'PAGADA', 1, 1);

INSERT INTO notificacion (mensaje, tipo, fecha, leida, id_cliente) VALUES
    ('Tu pedido fue entregado.', 'PEDIDO', '2026-06-11', FALSE, 1);

INSERT INTO reclamo (motivo, estado, fecha, id_cliente) VALUES
    ('Talla incorrecta en la blusa entregada.', 'ABIERTO', '2026-06-12', 2);

-- Módulo 1
INSERT INTO inv_categorias (nombre, descripcion) VALUES
    ('Vestidos', 'Vestidos de temporada'),
    ('Blusas', 'Blusas y camisas'),
    ('Accesorios', 'Bufandas, cinturones y bolsos');

INSERT INTO inv_proveedores (tipo_documento, numero_documento, razon_social, contacto, telefono, correo, direccion) VALUES
    ('RUC', '20123456789', 'Textiles Andinos S.A.C. (ficticio)', 'Carla Mendoza', '999000111', 'ventas@textiles-andinos.example', 'Av. Industrial 500, Lima'),
    ('RUC', '20987654321', 'Sedas del Sur E.I.R.L. (ficticio)',  'Jorge Paredes', '999000222', 'contacto@sedas-sur.example',     'Calle Comercio 20, Arequipa');

INSERT INTO inv_productos (codigo, nombre, descripcion, categoria_id, categoria_nombre, precio_compra, precio_venta, stock_actual, stock_minimo, estado) VALUES
    ('VES-001', 'Vestido seda floral', 'Vestido largo de seda estampada', 1, 'Vestidos',   120.00, 220.00, 15, 5, 'DISPONIBLE'),
    ('BLU-001', 'Blusa lino blanca',   'Blusa de lino manga corta',        2, 'Blusas',      40.00,  85.00,  3, 5, 'DISPONIBLE'),
    ('ACC-001', 'Bufanda de seda',     'Bufanda 90x90 cm',                 3, 'Accesorios',  25.00,  60.00, 30, 8, 'DISPONIBLE');

INSERT INTO inv_movimientos (producto_id, producto_nombre, tipo, cantidad, stock_anterior, stock_resultante, motivo, referencia_documento, fecha_hora, usuario_id) VALUES
    (1, 'Vestido seda floral', 'ENTRADA', 15, 0, 15, 'Stock inicial', 'OC-0001', '2026-06-01 09:00:00', 1),
    (2, 'Blusa lino blanca',   'SALIDA',   2, 5,  3, 'Venta',         NULL,      '2026-06-05 16:30:00', 1);

INSERT INTO inv_ajustes (tipo, motivo, fecha_hora, usuario_id)
VALUES ('CONTEO_FISICO', 'Conteo mensual', '2026-06-30 18:00:00', 1);
INSERT INTO inv_detalle_ajuste (ajuste_id, producto_id, producto_nombre, stock_anterior, stock_nuevo, diferencia)
VALUES (1, 3, 'Bufanda de seda', 31, 30, -1);

INSERT INTO inv_devoluciones (tipo, referencia_id, motivo, estado, fecha_hora, usuario_id)
VALUES ('CLIENTE', 2, 'Producto con defecto de costura', 'PENDIENTE', '2026-06-20 11:00:00', 1);
INSERT INTO inv_detalle_devolucion (devolucion_id, producto_id, producto_nombre, cantidad, motivo_linea)
VALUES (1, 2, 'Blusa lino blanca', 1, 'Costura abierta');

INSERT INTO inv_ordenes_compra (proveedor_id, proveedor_nombre, fecha_emision, fecha_entrega_estimada, estado, total, observaciones)
VALUES (1, 'Textiles Andinos S.A.C. (ficticio)', '2026-06-01', '2026-06-10', 'PENDIENTE', 400.00, 'Reposición de blusas');
INSERT INTO inv_detalle_orden_compra (orden_id, producto_id, producto_nombre, cantidad, precio_unitario, subtotal)
VALUES (1, 2, 'Blusa lino blanca', 10, 40.00, 400.00);

-- Módulo 3
INSERT INTO productos (codigo, nombre, descripcion, categoria, precio_venta, stock_disponible) VALUES
    ('VES-001', 'Vestido seda floral', 'Vestido largo de seda estampada', 'VESTIDO',   220.00, 15),
    ('BLU-001', 'Blusa lino blanca',   'Blusa de lino manga corta',        'BLUSA',      85.00, 10),
    ('ACC-001', 'Bufanda de seda',     'Bufanda 90x90 cm',                 'ACCESORIO',  60.00, 30);

INSERT INTO descuentos (nombre, descripcion, tipo, valor, fecha_inicio, fecha_fin) VALUES
    ('Temporada 10%', '10% en toda la tienda',  'PORCENTAJE', 10.00, '2026-06-01', '2026-12-31'),
    ('S/ 20 de regalo', 'Descuento fijo de S/ 20', 'MONTO_FIJO', 20.00, '2026-06-01', '2026-12-31');

-- Venta de ejemplo: 2 × 60.00 = 120.00 + IGV 18% (21.60) = 141.60
INSERT INTO ventas (cliente_id, vendedor_id, fecha_hora, estado, metodo_pago, subtotal, descuento, igv, total, observaciones)
VALUES (1, 1, '2026-06-10 15:20:00', 'COMPLETADA', 'TARJETA_CREDITO', 120.00, 0, 21.60, 141.60, 'Venta de ejemplo');
INSERT INTO detalle_ventas (venta_id, producto_id, producto_nombre, cantidad, precio_unitario, descuento_linea, subtotal)
VALUES (1, 3, 'Bufanda de seda', 2, 60.00, 0, 120.00);
INSERT INTO pagos (venta_id, monto, metodo, estado, fecha_pago, referencia)
VALUES (1, 141.60, 'TARJETA_CREDITO', 'APROBADO', '2026-06-10 15:21:00', 'REF-0001');
INSERT INTO comprobantes_venta (venta_id, tipo, serie, numero, fecha_emision, monto_total)
VALUES (1, 'BOLETA', 'B001', 1, '2026-06-10 15:22:00', 141.60);

INSERT INTO reservas (cliente_id, producto_id, fecha_reserva, fecha_expiracion, estado, observaciones)
VALUES (3, 1, '2026-06-15 10:00:00', '2026-12-31 23:59:59', 'ACTIVA', 'Reserva de ejemplo');
