# SalonCitas — Sistema de Gestión de Citas

Proyecto de escritorio en Java Swing para registrar personas y agendar citas en un salón de belleza. Conecta con SQL Server mediante JDBC.

---

## 🗂 Estructura del proyecto

```
SalonCitas/
├── conexion/
│   └── Conexion.java        # Configuración de la base de datos
├── modelo/
│   ├── Persona.java
│   ├── Estado.java
│   └── Cita.java
├── dao/
│   ├── PersonaDAO.java      # CRUD de personas
│   ├── EstadoDAO.java       # Carga combos de estado
│   ├── ComboDAO.java        # Carga combos de cliente/empleado
│   └── CitaDAO.java         # CRUD de citas
└── vista/
    ├── FormPersona.java     # Formulario de personas
    └── FormCita.java        # Formulario de citas
```

---

## ⚙️ Configuración

### 1. Base de datos

Ejecuta el script `db/CitasSalonBD.sql` en SQL Server. Crea las tablas e inserta los estados necesarios.

### 2. Conexión

Edita `conexion/Conexion.java` con tus datos:

```java
private static final String SERVIDOR  = "localhost";
private static final String PUERTO    = "1433";
private static final String BD        = "CitasSalonBD";
private static final String USUARIO   = "sa";
private static final String PASSWORD  = "tu_password";
```

## 📋 Orden de uso

Antes de usar los formularios, la base de datos necesita tener datos en este orden:

1. `Estado` — ya incluido en el script SQL
2. `Persona` — se registra desde **FormPersona**
3. `Cliente` o `Empleado` — se inserta manualmente o con formulario propio
4. `Cita` — se agenda desde **FormCita**

---

## 📝 Formularios

### FormPersona
- Registra, edita y elimina personas
- Haz clic en una fila de la tabla para cargar los datos en los campos
- No elimines una persona que tenga un cliente o empleado asociado

### FormCita
- Agenda citas seleccionando cliente, empleado, fecha, hora y estado
- Fecha en formato `yyyy-MM-dd` — ejemplo: `2025-06-15`
- Hora en formato `HH:mm` — ejemplo: `14:30`
- Los combos solo muestran clientes y empleados con estado **Activo**

---

## 🛠 Tecnologías

- IntelliJ IDEA
- JDBC — Microsoft SQL Server Driver
- SQL Server
- Patrón de capas: Modelo / DAO / Vista