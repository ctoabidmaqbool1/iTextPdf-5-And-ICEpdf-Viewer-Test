module com.example.itextpdf5andicepdfviewertest {
    requires itextpdf;
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.icepdf.ri.viewer;

    opens com.example.itextpdf5andicepdfviewertest to javafx.fxml;
    exports com.example.itextpdf5andicepdfviewertest;
}