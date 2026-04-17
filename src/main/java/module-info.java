module lospolimorficos.boletopolis {

    requires javafx.controls;
    requires javafx.fxml;

    // Ikonli
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome6;
    requires org.kordamp.ikonli.antdesignicons;
    requires javafx.base;
    requires java.desktop;
    requires org.apache.pdfbox;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires javafx.swing;

    exports lospolimorficos.boletopolis;
    exports lospolimorficos.boletopolis.controller;
    exports lospolimorficos.boletopolis.models;
    exports lospolimorficos.boletopolis.repositorios;
    exports lospolimorficos.boletopolis.services;
    exports lospolimorficos.boletopolis.viewController.viewControllersAdmin;

    opens lospolimorficos.boletopolis.controller to javafx.fxml;
    opens lospolimorficos.boletopolis.viewController.viewControllersAdmin to javafx.fxml;

}