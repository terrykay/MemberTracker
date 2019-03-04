/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author tezk
 */
public class ProgressIndicator {

    private ProgressBar pb;
    private Stage stage;

    public ProgressBar getPb() {
        return pb;
    }

    public void setPb(ProgressBar pb) {
        this.pb = pb;
    }

    public void show() {
        stage.show();
    }

    public void hide() {
        stage.hide();
    }

    public ProgressIndicator() {
        stage = new Stage();
        Group root = new Group();
        Scene scene = new Scene(root, 300, 150);
        stage.setScene(scene);
        stage.setTitle("Printing in progress");

        final Label label = new Label();
        label.setText("progress : ");

        pb = new ProgressBar();
        pb.setProgress(0);

        final HBox hb = new HBox();
        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(label, pb);
        final VBox vb = new VBox();
        vb.setSpacing(5);
        vb.getChildren().addAll(hb);
        vb.setAlignment(Pos.CENTER);
        scene.setRoot(vb);
    }
}
