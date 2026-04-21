module lospolimorficos.boletopolis {

    requires javafx.controls;
    requires javafx.fxml;

    // Ikonli
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome6;
    requires org.kordamp.ikonli.antdesignicons;
    requires javafx.base;
    requires java.xml;

    exports lospolimorficos.boletopolis;
    exports lospolimorficos.boletopolis.controller;
    exports lospolimorficos.boletopolis.models;
    exports lospolimorficos.boletopolis.repositorios;
    exports lospolimorficos.boletopolis.services;
    exports lospolimorficos.boletopolis.viewController;

    opens lospolimorficos.boletopolis.controller to javafx.fxml;
    opens lospolimorficos.boletopolis.viewController to javafx.fxml;
}