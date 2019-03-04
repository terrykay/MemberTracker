/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Soap.CustomerTO;
import Soap.ImageTO;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import utility.CustomerListFilter;

/**
 * FXML Controller class
 *
 * @author tezk
 */
public class FXMLQueryController extends FXMLParentController implements Initializable {

    private final String SELECT = "Select";
    private final String DOCUMENTS = "Documents";
    private final String ID = "ID";
    private final String NOTES = "Notes";
    private final String DROP_OPTIONS[] = {SELECT, DOCUMENTS, ID, NOTES};

    private final int NO_PARAMS = 3;

    private boolean updated = false;

    private String queryString = "";
    
    public String getQueryString() { return queryString; }
    
    private String addToQueryString(String what) {
        if (queryString.length() > 0)
            queryString += ", ";
        queryString += what;
        return queryString;
    }
    
    public boolean isUpdated() {
        return updated;
    }

    private CustomerListFilter customerListFilter;

    public CustomerListFilter getCustomerListFilter() {
        return customerListFilter;
    }

    @FXML
    private ChoiceBox<String> choiceBoxOne;
    @FXML
    private TextField textFieldOne;
    @FXML
    private RadioButton containsOne;
    @FXML
    private ChoiceBox<String> choiceBoxTwo;
    @FXML
    private TextField textFieldTwo;
    @FXML
    private RadioButton containsTwo;
    @FXML
    private ChoiceBox<String> choiceBoxThree;
    @FXML
    private TextField textFieldThree;
    @FXML
    private RadioButton containsThree;
    @FXML
    private RadioButton notContainsThree;
    @FXML
    private RadioButton andRadioButton;
    @FXML
    private RadioButton orRadioButton;
    @FXML
    private RadioButton notContainsOne;
    @FXML
    private RadioButton notContainsTwo;

    private RadioButton contains[];
    private TextField textField[];
    private ChoiceBox<String> choiceBox[];

    public FXMLQueryController() {
        FXMLPath = "FXMLQuery.fxml";
    }

    @Override
    public FXMLQueryController load() {
        FXMLQueryController controller = (FXMLQueryController) super.load();
        controller.getStage().setTitle("Query builder");
        return controller;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        choiceBox = new ChoiceBox[]{choiceBoxOne, choiceBoxTwo, choiceBoxThree};
        contains = new RadioButton[]{containsOne, containsTwo, containsThree};
        textField = new TextField[]{textFieldOne, textFieldTwo, textFieldThree};

        ToggleGroup andOrToggleGroup = new ToggleGroup();
        orRadioButton.setToggleGroup(andOrToggleGroup);
        andRadioButton.setToggleGroup(andOrToggleGroup);

        ToggleGroup containsToggleGroup[] = new ToggleGroup[3];
        containsToggleGroup[0] = new ToggleGroup();
        containsOne.setToggleGroup(containsToggleGroup[0]);
        notContainsOne.setToggleGroup(containsToggleGroup[0]);

        containsToggleGroup[1] = new ToggleGroup();
        containsTwo.setToggleGroup(containsToggleGroup[1]);
        notContainsTwo.setToggleGroup(containsToggleGroup[1]);

        containsToggleGroup[2] = new ToggleGroup();
        containsThree.setToggleGroup(containsToggleGroup[2]);
        notContainsThree.setToggleGroup(containsToggleGroup[2]);

        choiceBoxOne.getItems().addAll(FXCollections.observableArrayList(DROP_OPTIONS));
        choiceBoxTwo.getItems().addAll(FXCollections.observableArrayList(DROP_OPTIONS));
        choiceBoxThree.getItems().addAll(FXCollections.observableArrayList(DROP_OPTIONS));

        /*   choiceBoxOne.getSelectionModel().selectedIndexProperty().addListener(
                (ov, oldv, newv) -> {
                    System.out.println("Selected : " + newv);
                }
        );*/
        clearSettings();
    }

    @FXML
    private void handleApplyButton(ActionEvent event) {
        /* Truth table
        ContainsFlag    contains()  result  not XOR
            T               T           T       T
            T               F           F       F
            F               T           F       F
            F               F           T       T
         */
        if (choiceBoxOne.getSelectionModel().getSelectedIndex() == 0) {
            handleCancelButton(event);
            return;
        }
        updated = true;
        stage.hide();

        customerListFilter = new CustomerListFilter();
        customerListFilter.setAnd(andRadioButton.isSelected());

        queryString="";
        for (int i = 0; i < NO_PARAMS; i++) {
            String filterWhat = choiceBox[i].getSelectionModel().getSelectedItem();
            final String tf = textField[i].getText().toLowerCase();
            final boolean containsFlag = contains[i].isSelected();

            switch (filterWhat) {
                case DOCUMENTS:
                    customerListFilter.addFilter(a -> {
                        boolean flag = false;
                        Collection<ImageTO> images = a.getImageCollection();
                        for (ImageTO eachImage : images) {
                            if (eachImage.getType() == 'd') {
                                String docNotes = eachImage.getDetails().toLowerCase();
                                boolean contains = docNotes.contains(tf);
                                if (contains) {
                                    flag = true;
                                }
                            }
                        }
                        // Not XOR
                        return !(flag ^ containsFlag);
                    });
                    queryString = addToQueryString("Documents");
                    break;
                case ID:
                    customerListFilter.addFilter(a -> {
                        boolean flag = false;
                        Collection<ImageTO> images = a.getImageCollection();
                        for (ImageTO eachImage : images) {
                            if (eachImage.getType() == 'i') {
                                String docNotes = eachImage.getDetails().toLowerCase();
                                boolean contains = docNotes.contains(tf);
                                if (contains) {
                                    flag = true;
                                }
                            }
                        }
                        return !(flag ^ containsFlag);
                    });
                    queryString = addToQueryString("ID");
                    break;

                case NOTES:
                    customerListFilter.addFilter(a -> {
                        boolean flag = false;
                        if (a.getNotes() != null && a.getNotes().size() > 0) {
                            flag = a.getNotes().get(0).getNotes().toLowerCase().contains(tf);
                        }
                        return !(flag ^ containsFlag);
                    });
                    queryString = addToQueryString("Notes");
                    break;
            }
        }

        return;
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        updated = false;
        stage.hide();
        return;
    }

    private void clearSettings() {
        for (int i = 0; i < NO_PARAMS; i++) {
            contains[i].setSelected(true);
            choiceBox[i].getSelectionModel().selectFirst();
            textField[i].setText("");
            
        }
        andRadioButton.setSelected(true);
        queryString = "";
    }

    @FXML
    private void handleResetButton(ActionEvent event) {
        clearSettings();
    }

    public void setMissingSwimOrSauna() {
        // Create recipe!
        // select where not contains swim or not contains sauna
        clearSettings();
        orRadioButton.setSelected(true);
        notContainsOne.setSelected(true);
        notContainsTwo.setSelected(true);
        choiceBoxOne.getSelectionModel().select(DOCUMENTS);
        choiceBoxTwo.getSelectionModel().select(DOCUMENTS);
        textFieldOne.setText("swim");
        textFieldTwo.setText("sauna");
        
        handleApplyButton(null);
    }
    
}
