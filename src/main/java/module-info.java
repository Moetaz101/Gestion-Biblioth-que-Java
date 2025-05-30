module org.example.gestionbibliotheque {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;

    // Export the main package
    exports org.example.gestionbibliotheque;
    
    // Export the controller package to JavaFX FXML
    exports org.example.gestionbibliotheque.controller to javafx.fxml;
    
    // Export the model package
    exports org.example.gestionbibliotheque.model;
    
    // Export the dao package
    exports org.example.gestionbibliotheque.dao;

    // Open the controller package to JavaFX FXML for reflection
    opens org.example.gestionbibliotheque.controller to javafx.fxml;
    
    // Open the model package to JavaFX base for property bindings
    opens org.example.gestionbibliotheque.model to javafx.base;
}