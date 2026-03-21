module lospolimorficos.boletopolis {

    requires javafx.controls;
    requires javafx.fxml;

    exports lospolimorficos.boletopolis;
    exports lospolimorficos.boletopolis.controller;
    exports lospolimorficos.boletopolis.models;
    exports lospolimorficos.boletopolis.repositorios;
    exports lospolimorficos.boletopolis.services;
    exports lospolimorficos.boletopolis.viewController;

    opens lospolimorficos.boletopolis.controller to javafx.fxml;
    opens lospolimorficos.boletopolis.viewController to javafx.fxml;
}