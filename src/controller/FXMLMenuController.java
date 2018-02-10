/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import utility.IDPreferences;

/**
 * FXML Controller class
 *
 * @author tezk
 */
public class FXMLMenuController extends FXMLParentController implements Initializable {

    private final String TITLE = "BH ID tracker";

    private FXMLSearchCustomerController searchCustomerController = null;
    private FXMLEventListController eventListController = null;

    public FXMLMenuController() {
        FXMLPath = "FXMLMenu.fxml";
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //scene.xProperty().addListener(e -> { System.out.println(stage.getX()); });
        // TODO
    }

    public FXMLParentController load() {
        FXMLParentController controller = super.load();
        stage.setTitle(TITLE);
        scene.xProperty().addListener(e -> {
            System.out.println(stage.getX());
        });
        System.out.println(stage.getX());
        try {
            stage.setX(Double.parseDouble(IDPreferences.getInstance().getProperty("menux")));
            stage.setY(Double.parseDouble(IDPreferences.getInstance().getProperty("menuy")));
        } catch (NumberFormatException e) {
            stage.setX(100);
            stage.setY(100);
        }
        if (stage.getX() < 0) {
            stage.setX(100);
        }
        if (stage.getY() < 0) {
            stage.setY(100);
        }
        stage.setOnCloseRequest(e -> {
            IDPreferences.getInstance().setProperty("menux", Double.toString(stage.getX()));
            IDPreferences.getInstance().setProperty("menuy", Double.toString(stage.getY()));
            System.out.println(stage.getX() + ", " + stage.getY());
        });
        new Task() {
            @Override
            protected Object call() throws Exception {
                if (searchCustomerController
                        == null) {
                    searchCustomerController = new FXMLSearchCustomerController();
                    searchCustomerController = (FXMLSearchCustomerController) searchCustomerController.load();
                }
                searchCustomerController.loadCustomers();
                return null;
            }
        }.run();
        return controller;
    }

    @FXML
    private void handleCustomerListButton(ActionEvent event) {
        if (searchCustomerController == null) {
            searchCustomerController = new FXMLSearchCustomerController();
            searchCustomerController = (FXMLSearchCustomerController) searchCustomerController.load();
        }
        searchCustomerController.getStage().show();
        searchCustomerController.loadCustomers();
    }

    @FXML
    private void handleEventListButton(ActionEvent event) {
        if (eventListController == null) {
            eventListController = new FXMLEventListController();
            eventListController = (FXMLEventListController) eventListController.load();
        }
        eventListController.loadEvents();
        eventListController.getStage().show();
    }

    @FXML
    private void handleOutstandingInvoicesButton(ActionEvent event) {
    }

    @FXML
    private void handleExpiredIDsButton(ActionEvent event) {
    }

    @FXML
    private void handleOtherStuffButton(ActionEvent event) {
    }

}
