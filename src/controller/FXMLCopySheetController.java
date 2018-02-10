/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import utility.IDPreferences;
import utility.Utility;

/**
 * FXML Controller class
 *
 * @author tezk
 */
public class FXMLCopySheetController extends FXMLParentController implements Initializable {

    private final String SAVE_CSV_PATH_KEY = "savecsvpath";

    static String fileChooserPath;

    @FXML
    private TextArea textArea;

    public FXMLCopySheetController() {
        FXMLPath = "FXMLCopySheet.fxml";
    }

    public FXMLCopySheetController load() {
        FXMLCopySheetController controller = (FXMLCopySheetController) super.load();
        controller.getStage().setTitle("Output");
        return controller;
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String property = IDPreferences.getInstance().getProperty(SAVE_CSV_PATH_KEY);
        if (property != null && property.length() > 0) {
            fileChooserPath = property;
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file to save");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        if (fileChooserPath != null) {
            fileChooser.setInitialDirectory(new File(fileChooserPath));
        }
        Date date = new Date(System.currentTimeMillis());
        String dateString = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss").format(date);
        fileChooser.setInitialFileName(dateString + "-customers.csv");

        File selectedFile = fileChooser.showSaveDialog(getStage());
        if (selectedFile != null) {
            fileChooserPath = selectedFile.getParent();
            IDPreferences.getInstance().setProperty(SAVE_CSV_PATH_KEY, fileChooserPath);

            try {
                PrintWriter printWriter = new PrintWriter(selectedFile);
                printWriter.println(textArea.getText());
                printWriter.close();
                Utility.showAlert("Saved", "CSV saved", "CSV saved to " + selectedFile.getAbsolutePath());

                return;

            } catch (FileNotFoundException ex) {
                Logger.getLogger(FXMLCopySheetController.class.getName()).log(Level.SEVERE, null, ex);
                Utility.showAlert("Error saving", "Could not save CSV", "Try again with different path / filename");
            }
        }

    }
}
