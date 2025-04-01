package com.example.itextpdf5andicepdfviewertest;

import java.io.*;
import javax.swing.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.MyAnnotationCallback;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.util.FontPropertiesManager;

public class JavaFXPDFViewer extends Application {

    @Override
    public void start(Stage primaryStage) {
        // read/store the font cache, just needs to be called once when your app loads.
        FontPropertiesManager.getInstance().loadOrReadSystemFonts();

        Button btn = new Button();
        btn.setText("Generate PDF and Open in Viewer");
        btn.setOnAction(event -> {
            // Step 1: Generate iTextPDF 5 "Hello World" report in memory
            ByteArrayOutputStream outputStream = generateHelloWorldReport();

            // Step 2: Open the generated PDF in the icepdf-viewer
            openPdfInViewer(outputStream);
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

            Paragraph p = new Paragraph("Hello World");
            p.setPaddingTop(200);

            for (int i = 0; i < 1000; i++) {
                document.add(p);
            }

            document.close();

            // Create a reader
            PdfReader reader = new PdfReader(outputStream.toByteArray());

            // Create a stamper with a new OutputStream
            outputStream = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, outputStream);

            // Loop over the pages and add a header to each page
            int n = reader.getNumberOfPages();
            for (int i = 1; i <= n; i++) {
                getHeaderTable(i, n).writeSelectedRows(
                        0, -1, 34, 803, stamper.getOverContent(i)
                );
            }

            // Close the stamper
            stamper.close();
            reader.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream;
    }

    public static PdfPTable getHeaderTable(int x, int y) {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(527);
        table.setLockedWidth(true);
        table.getDefaultCell().setFixedHeight(20);
        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
        table.addCell("FOOBAR FILMFESTIVAL");
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(String.format("Page %d of %d", x, y));
        return table;
    }

    // Open the generated PDF in the icepdf-viewer
    private void openPdfInViewer(ByteArrayOutputStream outputStream) {
        // Create a SwingController
        SwingController controller = new SwingController();

        // Create the viewer as a JPanel
        controller.getDocumentViewController().setAnnotationCallback(
                new MyAnnotationCallback(controller.getDocumentViewController())
        );

        InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
        controller.openDocument(is, "", "\\test.pdf");
        controller.setIsEmbeddedComponent(true);

        // Build a viewer component
        SwingViewBuilder factory = new SwingViewBuilder(controller);

        JPanel viewerComponentPanel = factory.buildViewerPanel();

        ComponentKeyBinding.install(controller, viewerComponentPanel);

        // add copy keyboard command
        ComponentKeyBinding.install(controller, viewerComponentPanel);

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
