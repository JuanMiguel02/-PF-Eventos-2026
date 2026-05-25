package lospolimorficos.boletopolis.services;

import javafx.scene.control.Alert;

/**
 * Servicio para mostrar alertas y mensajes al usuario en la interfaz de usuario de JavaFX.
 * Proporciona métodos estáticos para mostrar diferentes tipos de alertas.
 */
public class ServicioAlerta {

    /**
     * Muestra una alerta genérica con un título, mensaje y tipo de alerta especificados.
     *
     * @param titulo El título de la ventana de alerta.
     * @param mensaje El contenido del mensaje que se mostrará en la alerta.
     * @param tipo El {@link Alert.AlertType} que define el icono y el comportamiento de la alerta (e.g., ERROR, INFORMATION).
     */
    public static void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        // Paso 1: Crear una nueva instancia de Alert con el tipo especificado.
        Alert alerta = new Alert(tipo);
        // Paso 2: Establecer el título de la ventana de alerta.
        alerta.setTitle(titulo);
        // Paso 3: Eliminar el texto del encabezado (header text) para un mensaje más limpio.
        alerta.setHeaderText(null);
        // Paso 4: Establecer el contenido principal del mensaje de la alerta.
        alerta.setContentText(mensaje);
        // Paso 5: Mostrar la alerta y esperar a que el usuario la cierre.
        alerta.showAndWait();
    }

    /**
     * Muestra una alerta de error con un mensaje específico.
     * El título de la alerta se establece automáticamente como "ERROR".
     *
     * @param mensaje El contenido del mensaje de error que se mostrará.
     */
    public static void mostrarAlertaError(String mensaje) {
        // Paso 1: Crear una nueva instancia de Alert de tipo ERROR.
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        // Paso 2: Establecer el título de la ventana de alerta como "ERROR".
        alerta.setTitle("ERROR");
        // Paso 3: Eliminar el texto del encabezado (header text) para un mensaje más limpio.
        alerta.setHeaderText(null);
        // Paso 4: Establecer el contenido principal del mensaje de error.
        alerta.setContentText(mensaje);
        // Paso 5: Mostrar la alerta y esperar a que el usuario la cierre.
        alerta.showAndWait();
    }
}
