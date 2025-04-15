package com.gluonapplication;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.MyAnnotationCallback;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewControllerImpl;
import org.icepdf.ri.util.FontPropertiesManager;

import javax.print.attribute.standard.MediaSizeName;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GluonApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Load system fonts (just once)
        FontPropertiesManager.getInstance().loadOrReadSystemFonts();

        Button btn = new Button("Open PDF in Viewer");
        btn.setOnAction(event -> {
            try (InputStream inputStream = getClass().getResourceAsStream("/InvoiceDetailReport-new.pdf")) {
                if (inputStream == null) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "PDF file not found in resources.");
                    return;
                }
                openReport(inputStream);
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        });

        StackPane root = new StackPane(btn);
        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("JavaFX PDF Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Opens a PDF file in a PDF viewer.
     */
    private void openReport(InputStream inputStream) {
        SwingController controller = new SwingController();
        controller.getDocumentViewController().setAnnotationCallback(
                new MyAnnotationCallback(controller.getDocumentViewController())
        );

        controller.openDocument(inputStream, "", "Report.pdf");
        controller.setIsEmbeddedComponent(true);
        controller.setPrintDefaultMediaSizeName(MediaSizeName.ISO_A4);

        SwingViewBuilder factory = new SwingViewBuilder(
                controller,
                DocumentViewControllerImpl.ONE_COLUMN_VIEW,
                DocumentViewController.PAGE_FIT_WINDOW_HEIGHT
        );

        JPanel viewerPanel = factory.buildViewerPanel();
        ComponentKeyBinding.install(controller, viewerPanel);

        JFrame viewerFrame = new JFrame("Maqbool Solutions");
        viewerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewerFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                viewerFrame.dispose();
            }
        });

        viewerFrame.getContentPane().add(viewerPanel);
        viewerFrame.pack();
        viewerFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Optional: Load icon safely
        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.ico"));
            viewerFrame.setIconImage(icon);
        } catch (Exception e) {
            System.err.println("Icon not found.");
        }

        viewerFrame.setVisible(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
