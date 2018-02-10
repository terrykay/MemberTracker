/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import UtilityClasses.Hash;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.SoapHandler;
import utility.Utility;

/**
 * FXML Controller class
 *
 * @author tezk
 */
public class FXMLLoginController extends FXMLParentController implements Initializable {

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public FXMLLoginController() {
        FXMLPath = "FXMLLogin.fxml";
    }

    public FXMLLoginController load() {
        FXMLLoginController controller = (FXMLLoginController) super.load();
        controller.getStage().setTitle("Please login");
        return controller;
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        doLogin();
    }

    private void doLogin() {
        setOpacity(0.2);

        Thread t = new Thread() {
            @Override
            public void run() {
                String password = Hash.sha256(passwordTextField.getText());

                String username = usernameTextField.getText();
                if (username == null || "".equals(username) || password == null || "".equals(password)) {
                    Platform.runLater(() -> {
                        Utility.showAlert("Wrong user name or password",
                                "You must supply a user name and\na password",
                                "Please correct and try again");
                        setOpacity(1.0);
                    });
                    return;
                }
                try {

                    String sessionId = SoapHandler.login(username, password);
                    if (sessionId != null && !"".equals(sessionId)) {
                        // Success!
                        Platform.runLater(() -> {
                            FXMLSearchCustomerController newController = new FXMLSearchCustomerController();
                            newController = (FXMLSearchCustomerController) newController.load();
                            newController.loadCustomers();
                            newController.getStage().show();
                            stage.hide();
                        });
                        return;
                    }
                    // Failure! Reset password
                    Platform.runLater(() -> {
                        Utility.showAlert("Wrong user name or password",
                                "Either the supplied username or\npassword was incorrect",
                                "Please correct and try again");
                        passwordTextField.setText("");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        Utility.showAlert("Error occured processing login",
                                "An error occured when trying to\nprocess the login details",
                                "Details of error : " + e.getMessage());
                    });
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    setOpacity(1.0);
                });
            }
        };

        t.start();
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handlePasswordAction(ActionEvent event) {
        doLogin();
    }

    private void setOpacity(Double value) {
        passwordTextField.setOpacity(value);
        usernameTextField.setOpacity(value);
        loginButton.setOpacity(value);
        cancelButton.setOpacity(value);
        usernameLabel.setOpacity(value);
        passwordLabel.setOpacity(value);
    }
}
