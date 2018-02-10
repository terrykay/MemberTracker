/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Soap.CaravanTO;
import Soap.MembershipTO;
import UtilityClasses.MyBoolean;
import UtilityClasses.MyDate;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import utility.Utility;
import static utility.Utility.showConfirmationAlert;

/**
 * FXML Controller class
 *
 * @author tezk
 */
public class FXMLMemberController extends FXMLParentController implements Initializable {

    private final String SELECT = "Select";
    private final String SOCIETIES[] = {SELECT, "Northern", "Southern", "Bromley", "Kent"};
    private final String REGIONS[] = {"N/A"};
    private final String VAN_PITCH = "Van pitch";
    private final String LARGE_VAN_PITCH = "Large van pitch";
    private final String MEMBERSHIP_TYPE[] = {SELECT, "Member", "Tent pitch", VAN_PITCH, LARGE_VAN_PITCH, "Resident"};
    private final String TITLE = "Member details";

    private MembershipTO aMembership;

    private boolean updated = false;
    private double hBoxHeights;
    @FXML
    private HBox caravanMakeHBox;
    @FXML
    private TextField caravanMakeTextField;
    @FXML
    private HBox caravanModelHBox;
    @FXML
    private TextField caravanModelTextField;
    @FXML
    private HBox caravanCrisHBox;
    @FXML
    private TextField caravanCrisTextField;
    @FXML
    private HBox caravanLengthHBox;
    @FXML
    private TextField caravanLengthTextField;

    public boolean isUpdated() {
        return updated;
    }

    @FXML
    private TextField membershipNumberTextField;
    @FXML
    private DatePicker memberSinceDatePicker;
    @FXML
    private ChoiceBox<String> societyChoiceBox;
    @FXML
    private ChoiceBox<String> districtChoiceBox;
    @FXML
    private ChoiceBox<String> membershipTypeChoiceBox;
    @FXML
    private TextField swipeCardIDTextField;
    @FXML
    private TextField lockerNumberTextField;
    @FXML
    private TextField plotIDTextField;
    @FXML
    private CheckBox parkingSpaceCheckBox;
    @FXML
    private CheckBox electricityHookupCheckBox;
    @FXML
    private CheckBox winterStorageCheckBox;

    private Integer caravanId=0; // Used to store caravanId, so we can tell if caravan field is newly set

    public FXMLMemberController() {
        FXMLPath = "FXMLMember.fxml";
    }

    /*
            searchSelection.getItems().addAll(FXCollections.observableArrayList(menuItems));
        searchSelection.getSelectionModel().select(0);
        searchSelection.getSelectionModel().selectedIndexProperty().addListener(
                (ov, oldv, newv) -> {
                    setSearchItems(newv);
                }
        );
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        societyChoiceBox.getItems().addAll(FXCollections.observableArrayList(SOCIETIES));
        societyChoiceBox.getSelectionModel().select(SOCIETIES[0]);
        districtChoiceBox.getItems().addAll(FXCollections.observableArrayList(REGIONS));
        districtChoiceBox.getSelectionModel().select(0);
        membershipTypeChoiceBox.getItems().addAll(FXCollections.observableArrayList(MEMBERSHIP_TYPE));
        membershipTypeChoiceBox.getSelectionModel().select(MEMBERSHIP_TYPE[0]);
        caravanLengthTextField.textProperty().addListener((e, oldv, newv) -> {
            if (newv.length() > 0) {
                try {
                    Integer.parseInt(newv);
                } catch (NumberFormatException ex) {
                    newv = oldv;
                }
            }
            caravanLengthTextField.setText(newv);
        });
 /*       membershipTypeChoiceBox.setOnAction(v -> {
            if (!VAN_PITCH.equals(membershipTypeChoiceBox.getValue())
                    && !LARGE_VAN_PITCH.equals(membershipTypeChoiceBox.getValue())) {
                caravanId = -1;
                disableCaravanFields();
            } else {
                enableCaravanFields();
            }
        });*/
    }

    public FXMLParentController load() {
        FXMLParentController controller = super.load();
        stage.setTitle(TITLE);
        return controller;
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        if (saveMember()) {
            updated = true;
            stage.hide();
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        updated = false;
        stage.hide();
    }

    public void setMembership(MembershipTO aMembership) {
   //     disableCaravanFields();
        if (aMembership == null) {
            return;
        }
        this.aMembership = aMembership;
        if (aMembership.getMembershipNo() == null || aMembership.getMembershipNo().isEmpty()) {
            aMembership.setMembershipNo(String.valueOf(aMembership.getCustomer()));
        }
                
        membershipNumberTextField.setText(aMembership.getMembershipNo());
        memberSinceDatePicker.setValue(MyDate.toLocalDate(aMembership.getJoinedDate()));
        societyChoiceBox.getSelectionModel().select(aMembership.getSociety());
        //districtChoiceBox
        if (aMembership.getType() != null && !aMembership.getType().isEmpty()) {
            membershipTypeChoiceBox.getSelectionModel().select(aMembership.getType());
    /*        if (aMembership.getType().equals(VAN_PITCH) || aMembership.getType().equals(LARGE_VAN_PITCH)) {
                enableCaravanFields();
            }*/
        }
        swipeCardIDTextField.setText(aMembership.getSwipeCardId());
        lockerNumberTextField.setText(aMembership.getLockerId());
        plotIDTextField.setText(aMembership.getPlotId());

        parkingSpaceCheckBox.setSelected(MyBoolean.MyBoolean(aMembership.isParkingSpace()));
        electricityHookupCheckBox.setSelected(MyBoolean.MyBoolean(aMembership.isElectricityHookup()));
        winterStorageCheckBox.setSelected(MyBoolean.MyBoolean(aMembership.isWinterStorage()));
        if (aMembership.getCaravanCollection().size() > 0) {
            //add van details
            CaravanTO van = aMembership.getCaravanCollection().get(0);
            caravanId = van.getIdcaravan();
            caravanMakeTextField.setText(van.getMake());
            caravanModelTextField.setText(van.getModel());
            caravanCrisTextField.setText(van.getCris());
            if (van.getLength() != null) {
                caravanLengthTextField.setText(van.getLength().toString());
            }
        } 
    }

    public MembershipTO getMembership() {
        return aMembership;
    }

    private boolean saveMember() {
        if (aMembership == null) {
            aMembership = new MembershipTO();
        }

        aMembership.setMembershipNo(membershipNumberTextField.getText());
        aMembership.setJoinedDate(MyDate.toXMLGregorianCalendar(memberSinceDatePicker.getValue()));
        if (!SELECT.equals(societyChoiceBox.getValue())) {
            aMembership.setSociety(societyChoiceBox.getValue());
        }
        //districtChoiceBox
        if (!SELECT.equals(membershipTypeChoiceBox.getValue())) {
            aMembership.setType(membershipTypeChoiceBox.getValue());
            CaravanTO van = null;
            if (!caravanMakeTextField.getText().isEmpty() || !caravanModelTextField.getText().isEmpty() ||
                    !caravanCrisTextField.getText().isEmpty() || ! caravanLengthTextField.getText().isEmpty() ) {
                // Dealing with a caravan

                if (aMembership.getCaravanCollection().size() == 0) {
                    aMembership.getCaravanCollection().add(van = new CaravanTO());
                } else {
                    van = aMembership.getCaravanCollection().get(0);
                }
                van.setMake(caravanMakeTextField.getText());
                van.setModel(caravanModelTextField.getText());
                van.setCris(caravanCrisTextField.getText());
                if (caravanLengthTextField.getText().length() > 0) {
                    van.setLength(Integer.parseInt(caravanLengthTextField.getText()));
                }
                van.setMembershipId(aMembership.getId());
                caravanId = 1;
            }
            // If memberAttribute caravanId == -1, we need to remove, so set customerId to -1 so server removes
            if (caravanId == -1) {
                if (aMembership.getCaravanCollection().size() > 0) {
                    aMembership.getCaravanCollection().get(0).setMembershipId(-1);
                }
            }

            aMembership.setSwipeCardId(swipeCardIDTextField.getText());
            aMembership.setLockerId(lockerNumberTextField.getText());
            aMembership.setPlotId(plotIDTextField.getText());
            aMembership.setParkingSpace(parkingSpaceCheckBox.isSelected());
            aMembership.setParkingSpace(parkingSpaceCheckBox.isSelected());
            aMembership.setElectricityHookup(electricityHookupCheckBox.isSelected());
            aMembership.setWinterStorage(winterStorageCheckBox.isSelected());
            return true;
        } else {
            Utility.showAlert("Please set a membership type", "Please update membership type\nto save details", "");
            return false;
        }
    }

    @FXML
    private void handleRemoveButton(ActionEvent event) {
        Optional result = showConfirmationAlert("Please confirm", "Set as none member?", "Remove membership status\nof this customer?");
        if (result.get() == ButtonType.OK) {
            aMembership.setMembershipNo("remove");
            aMembership.setJoinedDate(null);
           
            stage.hide();
            updated = true;
        }
    }
    
    private void disableCaravanFields() {
        System.out.println("Disabling fields : "+caravanMakeTextField);
        if (caravanMakeTextField == null)
                return;
        caravanMakeTextField.setDisable(true);
        caravanModelTextField.setDisable(true);
        caravanCrisTextField.setDisable(true);
        caravanLengthTextField.setDisable(true);
        caravanMakeTextField.setPromptText("Select Van membership");
        caravanModelTextField.setPromptText("type to enable");
    }
    
    private void enableCaravanFields() {
        System.out.println("Enabling fields");
        if (caravanMakeTextField == null)
                return;
        caravanMakeTextField.setDisable(false);
        caravanModelTextField.setDisable(false);
        caravanCrisTextField.setDisable(false);
        caravanLengthTextField.setDisable(false);
        caravanMakeTextField.setPromptText("");
        caravanModelTextField.setPromptText("");
    }
}
