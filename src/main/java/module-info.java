module com.example.itextpdf5andicepdfviewertest {
    requires javafx.controls;
    requires javafx.fxml;
    requires itextpdf;
    requires icepdf.viewer;
    requires java.desktop;

    opens com.example.itextpdf5andicepdfviewertest to javafx.fxml;
    exports com.example.itextpdf5andicepdfviewertest;
}