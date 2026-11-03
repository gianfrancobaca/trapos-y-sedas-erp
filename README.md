# Trapos y Sedas ERP

Aplicación de escritorio en **Java Swing + MySQL (JDBC)** para gestionar una tienda de ropa:
inventario, clientes y ventas.

> **Contexto del proyecto:** nació como **proyecto final grupal del curso Programación Orientada
> a Objetos II (POO II) en la USIL**. La versión base fue desarrollada en equipo. Las mejoras
> descritas en [Mejoras posteriores](#mejoras-posteriores) las hice yo de forma individual después
> del curso. No es un sistema en producción ni se ha usado con una tienda real; los datos de
> `schema.sql` son ficticios.

## Módulos

| Módulo | Paquete | Qué cubre |
|---|---|---|
| 1. Inventario | `modulo1_inventario` | Productos y categorías, proveedores, órdenes de compra, movimientos de stock, ajustes, devoluciones, alertas de stock mínimo y reporte de inventario |
| 2. Gestión de clientes | `modulo2_gestionClientes` | Clientes y categorías, programa de fidelización, métodos de pago, pedidos, facturas, reclamos, notificaciones, reporte de clientes e inicio de sesión |
| 3. Ventas | `modulo3_ventas` | Registro de ventas con detalle, catálogo de productos, reservas, pagos, comprobantes y descuentos |

El módulo 4 (administración: usuarios, roles, historial de operaciones y reportes) está en la rama
`inventario` y **no está integrado** en esta rama.

## Arquitectura y patrones

Código organizado por módulo, cada uno con `models`, `views`, `controllers` y `repositories`
(más `core` y `database` compartidos).

| Patrón | Dónde | Estado |
|---|---|---|
| **MVC** | `core/Controller`, `Model`, `View` | En uso: las vistas Swing son observadoras de sus modelos |
| **Observer** | `Model.attach/notifyViews` (módulo 1) y `NotificadorCliente` (módulo 2) | En uso |
| **Repository** | Interfaces + implementaciones `*RepositorySQL` | En uso; el acceso a datos queda fuera de los controladores |
| **Singleton** | `DatabaseConnection`, `SesionUsuario` | En uso |
| **Factory** | `NotificacionFactory`, `CategoriaClienteFactory` | En uso en los controladores de reclamos y categorías |
| **Strategy** | `DescuentoStrategy` (porcentaje, monto fijo) | Implementado, **pero aún no conectado al flujo de venta** (la venta guarda descuento 0) |
| **Observer (ventas)** | `NotificadorVentas` | Implementado, **sin usar todavía** |

## Cómo ejecutarlo

Requisitos: **JDK 21+** y **MySQL 8+**. No hace falta instalar Maven (se incluye el wrapper).

1. **Crea la base de datos con datos de prueba**
   ```bash
   mysql -u root -p < schema.sql
   ```
2. **Configura las credenciales** (el archivo real lo ignora git)
   ```bash
   cp config.example.properties config.properties
   # edita config.properties con tu usuario y contraseña de MySQL
   ```
3. **Ejecuta**
   ```bash
   ./mvnw compile exec:java
   ```
4. **Inicia sesión** con el usuario de prueba `admin` / `Admin123!`.

Ejecútalo siempre desde la raíz del proyecto: `config.properties` se busca en el directorio actual.

En VS Code basta con instalar *Extension Pack for Java*, abrir la carpeta y usar la terminal integrada
con el comando de arriba, o el botón *Run* sobre `Main.java`.

## Mejoras posteriores

Hechas después del curso, un commit por cambio:

- **Seguridad del login:** `iniciarSesion` no comprobaba la contraseña (cualquier usuario existente
  entraba). Ahora se verifica con **BCrypt** (coste 12) y se compara contra un hash falso cuando el
  usuario no existe, para que el tiempo de respuesta no revele qué usuarios existen.
- **Credenciales fuera del código:** la conexión a la base lee `config.properties`
  (ignorado por git) con una plantilla `config.example.properties`.
- **Ventas atómicas:** `registrarVenta` guarda la venta y sus detalles en una transacción
  (`setAutoCommit(false)`, `commit`, `rollback`); si algo falla no queda nada a medias y la interfaz
  muestra el error.
- **`schema.sql`:** el repositorio no traía el esquema. Se dedujo de las consultas SQL de los
  repositorios e incluye datos de prueba.
- **Maven:** migración a estructura estándar `src/main/java` con `pom.xml` y Maven Wrapper.

## Limitaciones conocidas

- Los descuentos (Strategy) no se aplican en el flujo real de venta.
- Registrar una venta **no descuenta stock**.
- El módulo 1 y el módulo 3 mantienen tablas de producto separadas (`inv_productos` y `productos`).
- La mayoría de los repositorios solo imprimen los errores SQL en consola; únicamente el camino de
  registro de venta propaga las excepciones.
- El login no limita intentos fallidos ni bloquea cuentas.
- Una contraseña de base de datos usada en versiones anteriores sigue en el historial de git.
