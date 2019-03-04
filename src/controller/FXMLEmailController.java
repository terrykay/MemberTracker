/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import model.SearchRowItem;

/**
 * FXML Controller class
 *
 * @author tezk
 */
public class FXMLEmailController extends FXMLParentController implements Initializable {

    @FXML
    private TextField subjectTextField;
    @FXML
    private TextArea messageBodyTextArea;
    @FXML
    private TextField fromTextField;
    @FXML
    private TextArea toTextArea;
    @FXML
    private CheckBox bccFromButton;

    public FXMLEmailController() {
        FXMLPath = "FXMLEmail.fxml";
    }

    @Override
    public FXMLEmailController load() {
        FXMLEmailController controller = (FXMLEmailController) super.load();
        controller.getStage().setTitle("Compose email");
        return controller;
    }

    private boolean updated = false;

    public boolean isUpdated() {
        return updated;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setFrom("natfound@hotmail.co.uk");
        toTextArea.setEditable(false);
        toTextArea.setTooltip(new Tooltip("To edit an email, open the customers record from the Customer list screen"));
        toTextArea.setWrapText(true);
        bccFromButton.setTooltip(new Tooltip("Selecting this will add the From address as a BCC: recipient of any emails sent"));
    }

    public void setTo(String newTo) {
        toTextArea.setText(newTo);
    }

    public void setTo(List<SearchRowItem> emailList) {
        StringBuilder newTo = new StringBuilder();
        Iterator<SearchRowItem> i = emailList.iterator();
        SearchRowItem eachEmail = i.next();
        String email = eachEmail.getEmail();
        while (i.hasNext()) {
            if (email != null && email.length() > 0) {
                newTo.append(eachEmail.getEmail());
            }
            if (i.hasNext()) {
                eachEmail = i.next();
                email = eachEmail.getEmail();
                if (email != null && email.length() > 0 && newTo.length()>0) {
                    newTo.append(", ");
                }
            }
        }
        toTextArea.setText(newTo.toString());
    }

    public void setSubject(String subjectText) {
        this.subjectTextField.setText(subjectText);
    }

    public void setMessageBody(String messageBodyText) {
        this.messageBodyTextArea.setText(messageBodyText);
    }

    public void setFrom(String fromText) {
        this.fromTextField.setText(fromText);
    }

    public String getSubject() {
        return subjectTextField.getText();
    }

    public String getBody() {
        return messageBodyTextArea.getText();
    }

    public void setBody(String newBody) {
        messageBodyTextArea.setText(newBody);
    }

    public String getFrom() {
        return fromTextField.getText();
    }
    
    public boolean getBccFrom() {
        return bccFromButton.selectedProperty().getValue();
    }

    @FXML
    private void handleSendButton(ActionEvent event) {
        updated = true;
        stage.hide();
        return;
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        updated = false;
        stage.hide();
        return;
    }

}
