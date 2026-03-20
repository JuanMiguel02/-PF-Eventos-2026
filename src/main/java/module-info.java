module lospolimorficos.boletopolis {

    requires javafx.controls;
    requires javafx.fxml;

    // Ikonli
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome6;
    requires org.kordamp.ikonli.antdesignicons;

    opens lospolimorficos.boletopolis to javafx.fxml;
    exports lospolimorficos.boletopolis;
}