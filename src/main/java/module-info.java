module com.grpcstreamings.resturantapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.desktop;
    requires java.sql;
    requires jbcrypt;
    requires mysql.connector.java;

    opens com.grpcstreamings.resturantapp to javafx.fxml;
    exports com.grpcstreamings.resturantapp;
    exports com.grpcstreamings.resturantapp.controller;
    opens com.grpcstreamings.resturantapp.controller to javafx.fxml;
}