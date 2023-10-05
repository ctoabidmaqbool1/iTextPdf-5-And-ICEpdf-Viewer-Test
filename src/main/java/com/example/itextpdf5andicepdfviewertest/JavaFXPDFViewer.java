package com.example.itextpdf5andicepdfviewertest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.swing.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.icepdf.ri.common.MyAnnotationCallback;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

public class JavaFXPDFViewer extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Generate PDF and Open in Viewer");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // Step 1: Generate iTextPDF 5 "Hello World" report in memory
                ByteArrayOutputStream outputStream = generateHelloWorldReport();

                // Step 2: Open the generated PDF in the icepdf-viewer
                openPdfInViewer(outputStream);
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("JavaFX PDF Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Generate iTextPDF 5 "Hello World" report in memory
    private ByteArrayOutputStream generateHelloWorldReport() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(new Paragraph("Hello World"));
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream;
    }

    // Open the generated PDF in the icepdf-viewer
    private void openPdfInViewer(ByteArrayOutputStream outputStream) {
        // Create a SwingController
        SwingController controller = new SwingController();

        // Build a viewer component
        SwingViewBuilder factory = new SwingViewBuilder(controller);

        InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
        controller.openDocument(is, "", "");
        controller.setIsEmbeddedComponent(true);

        // Create the viewer as a JPanel
        controller.getDocumentViewController().setAnnotationCallback(
                new MyAnnotationCallback(controller.getDocumentViewController())
        );

        JPanel viewerComponentPanel = factory.buildViewerPanel();

        // Create JFrame to contain the viewer
        JFrame viewerFrame = new JFrame();
        viewerFrame.getContentPane().add(viewerComponentPanel);
        viewerFrame.pack();
        viewerFrame.setVisible(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
