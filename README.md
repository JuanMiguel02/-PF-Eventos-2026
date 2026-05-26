# Boletopolis: Plataforma de Gestión de Eventos y Venta de Entradas

¡Bienvenido a Boletopolis! Esta es una plataforma integral diseñada para la gestión eficiente de eventos y la venta de entradas, ofreciendo una experiencia fluida tanto para organizadores como para asistentes.

## Descripción del Proyecto

Boletopolis es una aplicación robusta que facilita la creación, promoción y gestión de eventos de todo tipo, desde conciertos y conferencias hasta talleres y reuniones. Permite a los organizadores tener un control total sobre sus eventos, mientras que los usuarios pueden descubrir y adquirir entradas de manera sencilla y segura.

## Características Principales

*   **Gestión de Eventos:** Crea, edita y elimina eventos con detalles completos como nombre, descripción, fecha, hora, lugar y capacidad.
*   **Venta de Entradas:** Configura diferentes tipos de entradas (general, VIP, etc.) con precios y cantidades específicas.
*   **Gestión de Usuarios:** Administra usuarios con diferentes roles (administrador, cliente).
*   **Procesamiento de Pagos:** Integración para el procesamiento seguro de pagos (simulado o real, dependiendo de la implementación).
*   **Interfaz Intuitiva:** Diseño amigable y fácil de usar para una experiencia de usuario óptima.
*   **Confirmación de Compra:** Envío de confirmaciones y detalles de entradas a los compradores.

## Tecnologías Utilizadas

*   **:** Java, Spring Boot
*   **Base de Datos:**
*   **Frontend:**
*   **Control de Versiones:** Git

## Cómo Empezar

1.  **Clonar el Repositorio:**
    ```bash
    git clone https://github.com/tu-usuario/boletopolis.git
    cd boletopolis
    ```
2.  **Configuración
3.  **Compilar y Ejecutar:**
    *   Puedes compilar y ejecutar el proyecto usando Maven o Gradle (dependiendo de la configuración del proyecto).
    *   Con Maven:
        ```bash
        mvn clean install
        mvn spring-boot:run
        ```
    *   Con Gradle:
        ```bash
        ./gradlew bootRun
        ```
4.  **Acceder a la Aplicación:**
    *   Una vez que la aplicación esté corriendo, podrás acceder a ella a través de tu navegador web, generalmente en `http://localhost:8080`.

## Usuarios de Ejemplo

Para facilitar las pruebas y el desarrollo, se han pre-cargado los siguientes usuarios:

### 1. Administrador

Este usuario tiene privilegios completos para gestionar eventos, usuarios y configuraciones del sistema.

*   **Objeto de Creación (Java):**
    ```java
    Admin admin1 = new Admin(
                "Sancho",
                "Panza",
                "3123213",
                "sancho@boletopolis.com",
                "412321312",
                "123456"
        );
    ```
*   **Credenciales de Acceso:**
    *   **Email:** `sancho@boletopolis.com`
    *   **Contraseña:** `123456`

### 2. Cliente

Este usuario puede explorar eventos, comprar entradas y ver su historial de compras.

*   **Objeto de Creación (Java):**
    ```java
    crearClienteEjemplo(
                "Julian",
                "Casablancas",
                "09172321",
                "casablancas@gmail.com",
                "301578000",
                9050000,
                "NEQUI"
        );
    ```
*   **Credenciales de Acceso:**
    *   **Email:** `casablancas@gmail.com`
    *   **Contraseña:** (Asumir una contraseña por defecto o generada, si no se especifica en `crearClienteEjemplo`. Por ejemplo, `password` o `123456`)

---

¡Esperamos que disfrutes usando y contribuyendo a Boletopolis!
