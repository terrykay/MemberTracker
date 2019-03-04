/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import UtilityClasses.MyDate;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javax.imageio.ImageIO;
import model.ImageRowItem;
import utility.IDPreferences;
import utility.Utility;

/**
 * FXML Controller class
 *
 * @author tezk
 */
public class FXMLImageViewController extends FXMLParentController implements Initializable {

    private final int EXPIRY = 5; // number of years until expiry
    private final String SAVE_IMAGE_PATH_KEY = "saveimagepath";
    private boolean noImage = false;
    
    static String fileChooserPath;
    final DoubleProperty zoomProperty = new SimpleDoubleProperty(200);

    private ImageRowItem ourImage;
    boolean updated = false;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private VBox buttonVBox;
    @FXML
    private ScrollPane imageScrollPane;
    @FXML
    private CheckBox expiresCheckBox;
    @FXML
    private Label scannedLabel;

    public boolean isUpdated() {
        return updated;
    }

    @FXML
    private ImageView imageView;
    @FXML
    private DatePicker scannedDatePicker;
    @FXML
    private DatePicker expiresDatePicker;
    @FXML
    private TextArea detailsTextArea;

    /**
     * Initializes the controller class.
     */
    public FXMLImageViewController() {
        FXMLPath = "FXMLImageView.fxml";
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // stage.setResizable(false);
        //  imageView.fitWidthProperty().bind(mainAnchorPane.widthProperty());
        //   imageView.fitHeightProperty().bind(mainAnchorPane.heightProperty().subtract(130));
        //buttonVBox.prefWidthProperty().bind(mainAnchorPane.widthProperty());
        // buttonVBox.prefHeightProperty().bind(mainAnchorPane.heightProperty());

        // User request 26/1/17 = updated expiry if date scanned change
        scannedDatePicker.setOnAction(v -> {
            expiresDatePicker.setValue(scannedDatePicker.getValue().plusYears(EXPIRY));
        });

        String property = IDPreferences.getInstance().getProperty(SAVE_IMAGE_PATH_KEY);
        if (property != null && property.length() > 0) {
            fileChooserPath = property;
        }

        zoomProperty.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable arg0) {
                imageView.setFitWidth(zoomProperty.get() * 4);
                imageView.setFitHeight(zoomProperty.get() * 3);
            }
        });

        imageScrollPane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() > 0) {
                    zoomProperty.set(zoomProperty.get() * 1.1);
                } else if (event.getDeltaY() < 0) {
                    zoomProperty.set(zoomProperty.get() / 1.1);
                }
            }
        });
        expiresCheckBox.setSelected(true);
        expiresCheckBox.setVisible(false);
    }

    public void setExpires(boolean flag) {
        expiresCheckBox.setVisible(true);
        if (flag != expiresCheckBox.isSelected()) {
            return;
        }

        expiresCheckBox.setSelected(flag);
        expiresDatePicker.setDisable(!flag);
        if (!flag) {
            expiresDatePicker.setValue(scannedDatePicker.getValue());
        }
    }
    
    public void setNoImage() {
        noImage = true;
        stage.setHeight(200);
        scannedLabel.setText("Seen");
        System.out.println("MyDate = "+MyDate.toLocalDate(MyDate.toXMLGregorianCalendar(new Date())));
        scannedDatePicker.setValue(MyDate.toLocalDate(MyDate.toXMLGregorianCalendar(new Date())));
        updated = true;
        hideImageView();
    }
    
    private void hideImageView() {
            imageView.setY(0);
            imageScrollPane.setHmax(0);
            imageScrollPane.setVisible(false);
    }

    public void setImage(ImageRowItem anImage) {
        ourImage = anImage;
        WritableImage newImage = null;
        imageScrollPane.managedProperty().bind(imageScrollPane.visibleProperty());
        System.out.println("image? noImage = "+noImage);
        if (!noImage) {
            newImage = SwingFXUtils.toFXImage(anImage.getTheImage(), newImage);
            imageView.setImage(newImage);
        } else {
            hideImageView();
        
        }
        if (anImage.getScanned() != null) {
        scannedDatePicker.setValue(MyDate.toLocalDate(anImage.getScanned()));
        expiresDatePicker.setValue(MyDate.toLocalDate(anImage.getExpires()));
        }
        detailsTextArea.setText(anImage.getDetails());
        double ih = imageView.getBoundsInParent().getHeight();
        double mph = mainAnchorPane.getPrefHeight();
        double vbh = buttonVBox.getPrefHeight();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        /*      System.out.println("mw = "+scene.getHeight());
        System.out.println("ih = "+newImage.getHeight());
        System.out.println("iv = "+imageView.getBoundsInParent().getHeight());
        System.out.println("ivh = "+imageView.getFitHeight());
        System.out.println("mp = "+mainAnchorPane.getPrefHeight());
        System.out.println("vb = "+buttonVBox.getPrefHeight());
        System.out.println("sh = "+primScreenBounds.getHeight());
         */ if (mph > (ih + vbh)) {
            mainAnchorPane.setPrefHeight(ih + vbh);
        }
        if (mph > primScreenBounds.getHeight() - 50) {
            mainAnchorPane.setPrefHeight(primScreenBounds.getHeight() - 50);
        }
    }

    public void resetExpiresDate() {
        expiresDatePicker.setValue(scannedDatePicker.getValue());
    }

    private void saveImage() {
        ourImage.setDetails(detailsTextArea.getText());
        ourImage.setScanned(MyDate.toXMLGregorianCalendar(scannedDatePicker.getValue()));
        ourImage.setExpires(MyDate.toXMLGregorianCalendar(expiresDatePicker.getValue()));
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        saveImage();
        updated = true;
        stage.hide();
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        updated = false;
        stage.hide();
    }

    @FXML
    private void handleExportButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        if (fileChooser == null) {
            System.err.println("Erro on Export - FileChooser() is null");
            return;
        }
        fileChooser.setTitle("Select file to save");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png")
        );

        if (fileChooserPath != null) {
            fileChooser.setInitialDirectory(new File(fileChooserPath));
        }

        fileChooser.setInitialFileName(ourImage.getUrl().replace("jpg", "png"));

        File selectedFile = null;
        try {
            selectedFile = fileChooser.showSaveDialog(getStage());
        } catch (Exception e) {
            // If initial folder has become invalid (drive ejected etc) we'll end up here
            fileChooser.setInitialDirectory(null);
            selectedFile = fileChooser.showSaveDialog(getStage());
        }
        if (selectedFile != null) {
            fileChooserPath = selectedFile.getParent();
            IDPreferences.getInstance().setProperty(SAVE_IMAGE_PATH_KEY, fileChooserPath);

            BufferedImage theImage = ourImage.getTheImage();

            try {
                ImageIO.write(theImage, "png", selectedFile);
                Utility.showAlert("Saved", "Image saved", "Image saved to " + selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                Logger.getLogger(FXMLImageViewController.class.getName()).log(Level.SEVERE, null, ex);
                Utility.showAlert("Error saving", "Could not save image", "Try again with different path / filename");
                return;
            }

        }

    }

    @FXML
    private void handleClickExpiresCheckBox(ActionEvent event) {
        setExpires(expiresCheckBox.isSelected());
    }
}
