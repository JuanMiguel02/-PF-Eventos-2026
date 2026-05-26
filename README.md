# 🎫 Boletópolis — Sistema de Gestión y Venta de Entradas

¡Bienvenido a **Boletópolis**! Una solución integral, robusta y moderna para la simulación, gestión e interacción de venta de entradas y control de aforo de eventos en recintos, diseñada bajo los más altos estándares de la ingeniería de software, buenas prácticas (principios SOLID) y desarrollada enteramente en **JavaFX**.

Este sistema fue desarrollado por el grupo **Los Polimórficos** para la asignatura de **Programación II** (Ingeniería de Sistemas y Computación - Universidad del Quindío).

---

## 🚀 Características Principales

- **Gestión Completa de Eventos:** Panel administrativo avanzado para crear, editar, asociar imágenes locales/remotas y actualizar dinámicamente los estados de los eventos.
- **Mapeo Interactivo de Recintos:** Renderizado dinámico del escenario y la matriz de asientos clasificados por zonas específicas con tarifas diferenciadas.
- **Arquitectura Robusta Basada en Patrones de Diseño:** Desacoplamiento total entre lógica de negocio y presentación visual (o eso intentamos).
- **Simulación Bancaria Integrada:** Gestión de saldos reales simulados y vinculación automática de pasarelas de pago virtuales (Nequi, Tarjetas de Crédito).

---

## 👥 Perfiles del Sistema y Funcionalidades

El sistema cuenta con dos roles claramente definidos e implementados mediante polimorfismo y herencia desde una clase base `Usuario`:

### 👤 1. Perfil Cliente / Usuario
Está orientado a la experiencia del consumidor final. Sus responsabilidades y acciones dentro del sistema incluyen:
- **Consulta de Cartelera:** Visualizar los eventos disponibles filtrados por pertinencia.
- **Compra de Entradas:** Selección interactiva de asientos en un mapa renderizado en tiempo real. El cliente puede dar clic sobre los puestos libres, ver el costo calculado según la zona, seleccionar su pasarela de pago y agregar servicios de valor añadido (snacks, parqueadero, etc.).
- **Gestión de Perfil:** Modificar sus datos personales y actualizar credenciales/contraseñas de manera segura mediante validación de contraseña actual.
- **Centro de Notificaciones:** Recibir alertas automáticas sobre cambios en los eventos que ha adquirido o de los cuales ha decidido ser observador.

### 🛡️ 2. Perfil Administrador
Encargado de la logística, control operativo y métricas financieras de la plataforma:
- **Control de Eventos:** Crear nuevos espectáculos, modificar descripciones, fechas, cargar posters promocionales y mutar el ciclo de vida del evento (`Borrador`, `Publicado`, `Cancelado`).
- **Configuración de Recintos:** Definir la distribución física de escenarios y la capacidad máximaizada de aforo desglosada por zonas.
- **Dashboard de Analítica:** Monitorear en tiempo real el comportamiento comercial del sistema mediante gráficos estadísticos (`BarChart`) de ventas consolidadas por meses.
- **Auditoría de Ventas y Clientes:** Consultar el listado global de usuarios y transacciones con motores de búsqueda rápidos e inteligentes.

---

## 🧩 Patrones de Diseño Implementados

Para garantizar que el sistema cumpla con los atributos de calidad de **mantenibilidad, escalabilidad y legibilidad**, se hizo uso de los patrones de diseño. Por mencionar algunos:

1. **Singleton (Instancia Única):**
   - *Dónde se usa:* En los repositorios de datos en memoria (`UsuarioRepositorio`, `CompraRepositorio`) y en componentes de estado global como `GestorSesion`.
   - *Por qué:* Previene la duplicación de colecciones de datos, asegurando que todas las pantallas y controladores de la interfaz de usuario consulten exactamente la misma fuente de verdad.

2. **Observer (Observador):**
   - *Dónde se usa:* En la relación entre `Evento` (Sujeto) y `Cliente` (Observador/Suscrito).
   - *Por qué:* Permite el desacoplamiento de alertas. Cuando un administrador cambia el estado de un evento (por ejemplo, a `CANCELADO`), el sistema notifica en cascada al listado de clientes compradores y ejecuta de manera autónoma las rutinas de devolución total de dinero a sus cuentas virtuales.

3. **Strategy (Estrategia):**
   - *Dónde se usa:* En el lienzo interactivo del mapa del recinto (`ServicioDibujoRecinto`), apoyado por interfaces como `CompraInteraccionStrategy` y `AdminEventoInteraccionStrategy`.
   - *Por qué:* Permite cambiar el comportamiento del clic y arrastre sobre el mapa de asientos en tiempo de ejecución. Mientras que el cliente selecciona puestos para comprar, el administrador usa el mismo lienzo para modificar configuraciones o verificar estados técnicos del aforo.

4. **Factory Method / Creacionales (Opcional):**
   - *Dónde se usa:* En la instanciación parametrizada de entradas, liquidación de costos de servicio y creación de perfiles.
   - *Por qué:* Centraliza la inicialización de objetos complejos evitando dispersar la palabra clave `new` por todos los controladores FXML.

---

## 👥 Credenciales de Acceso

Puedes explorar, auditar o evaluar los flujos de trabajo paralelos del sistema iniciando sesión directamente con los siguientes datos:

| Rol de Usuario | Correo Electrónico (User) | Contraseña (Password) | Detalles de la Cuenta / Saldo Inicial |
| :--- | :--- | :--- | :--- |
| **👤 Cliente / Usuario** | `casablancas@gmail.com` | `12345` | **Nombre:** Julian Casablancas <br>**Doc:** 12345 <br>**Plataforma:** NEQUI <br>**Saldo Semilla:** \$9,050,000.00 COP |
| **🛡️ Administrador** | `sancho@boletopolis.com` | `123456` | **Nombre:** Sancho Panza <br>**Doc:** 3123213 <br>**Privilegios:** Gestión total de eventos, estados, cancelaciones y carga de recursos. |

---

## 📂 Estructura Arquitectónica del Proyecto

El código fuente respeta una estricta separación de responsabilidades inspirada en la arquitectura **MVC (Modelo-Vista-Controlador)**:

```text
lospolimorficos.boletopolis
 ├── controller          # Controladores lógicos y de negocio (ClienteController, EventoController, CompraController)
 ├── models              # Clases de dominio puro (Cliente, Admin, Evento, Recinto, Asiento, Entrada, Compra)
 ├── plantillas           # Estas clases implementan el patrón de transferencia de datos mediante componentes inmutables (`records`) para precargar, estructurar y clonar configuraciones base de recintos y aforos sin alterar el estado real del sistema.
 ├── repositorios        # Capa de persistencia estructurada en memoria (Singletons)
 ├── services            # Componentes transversales autónomos (GestorSesion, ServicioDibujoRecinto, ServicioAlerta)
 └── viewController      # Controladores de Interfaz de Usuario ligados a las vistas JavaFX
      ├── viewControllersAdmin    # Flujos de pantalla exclusivos para el rol de administrador
      └── viewControllersUsuario  # Flujos de compra, mapa del recinto interactivo y edición de perfil del cliente
       └── viewControllersUsuario  # Flujos de compartidos entre los dos perfiles

 ## 🛠️ Requisitos Técnicos y Tecnologías
 Lenguaje Base: Java 21 LTS o superior.

 Framework Gráfico: JavaFX 21 (Componentes nativos como TableView, GridPane, PasswordField, e hilos gráficos mediante Platform.runLater).

 Composición de Interfaces: Archivos XML de diseño estructurado (.fxml) acoplados.

 Gestor de Construcción: Maven (archivo pom.xml con dependencias requeridas).

 ## 💻 Instrucciones para la Ejecución Local
 1. Clonar el repositorio
```bash
 git clone [https://github.com/JuanMiguel02/-PF-Eventos-2026.git](https://github.com/JuanMiguel02/-PF-Eventos-2026.git)
```

 2. Importar el Proyecto: Abre tu IDE (se recomienda IntelliJ IDEA) y selecciona "Open" apuntando al directorio raíz clonado.

 3. Configurar el Entorno: Asegúrate de que el proyecto apunte al SDK de Java 21 en la configuración de la estructura del proyecto (Project Structure).

 4. Cargar Dependencias: Permite que Maven descargue los módulos requeridos para los gráficos de JavaFX.

 5. Ejecutar: Localiza la clase encargada del punto de entrada de la aplicación (App.java), haz clic derecho y selecciona Run para inicializar el login.

 Desarrollado con rigurosidad académica por el grupo Los Polimórficos (Juan Miguel Henao, Jerónimo Delgado, Juan Camilo Agudelo). Universidad del Quindío, 2026.