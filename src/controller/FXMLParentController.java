/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import bhmembertracker.BHMemberTracker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author tezk
 */
public class FXMLParentController {
    private static Stage mainStage;
    // References to Stage and Controller for "newProject" stage & controller, saves creating them over and over
    protected Stage stage;
    protected Scene scene;
    protected FXMLParentController controller;
    // Reference to the DBAccess methods
    protected String FXMLPath="old";

    protected boolean updated = false;
    public boolean isUpdated() { return updated; }

    public FXMLParentController load() {
        // Load the FXML  - we need to set FXMLPath in constructor to link to path to the page
        Parent root = null;
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(BHMemberTracker.class.getResource("/view/"+FXMLPath));
        //System.out.println(ProjectLog.class.getResource("/view/main.fxml"));
        try {
            root = (Parent)fxmlLoader.load();
        }
        catch (Exception e) {
            System.err.println("Exception loading FXML resources! "+e.getMessage());
            System.exit(0);
        }
        scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        controller = fxmlLoader.getController();
        controller.setScene(scene);
        controller.setStage(stage);
        return controller;
    }
    
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    public Scene getScene()
    {
        return scene;
    }

    public void setScene(Scene scene)
    {
        this.scene = scene;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public FXMLParentController getController() {
        return controller;
    }

    public void setController(FXMLParentController controller) {
        this.controller = controller;
    }  

    public Stage getStage() {
        return stage;
    }
    
    
}
