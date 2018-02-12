package controller;

import Soap.CarTO;
import Soap.ChildTO;
import Soap.CustomerTO;
import Soap.ImageTO;
import Soap.AddressTO;
import Soap.ElectricitychargeTO;
import Soap.InvoiceTO;
import Soap.MembershipTO;
import Soap.NextOfKinTO;
import Soap.NotesTO;
import Soap.ReceiptTO;
import Soap.RefuseTO;
import Soap.VisitTO;
import UtilityClasses.MyDate;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import UtilityClasses.Name;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.PageRange;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import model.CarRowItem;
import model.ChildRowItem;
import model.ImageRowItem;
import model.InvoiceRowItem;
import model.SearchRowItem;
import model.SoapHandler;
import model.VisitRowItem;
import org.ghost4j.document.DocumentException;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.RendererException;
import org.ghost4j.renderer.SimpleRenderer;
import utility.PostFile;
import utility.ProgressIndicator;
import utility.SSLUtilities;
import utility.Utility;
import static utility.Utility.showConfirmationAlert;

public class FXMLCustomerController extends FXMLParentController implements Initializable {

    private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    @FXML
    private CheckBox giftAidCheckBox1;
    @FXML
    private Label dateJoinedLabel;
    @FXML
    private Label lastVisitLabel;
    @FXML
    private TextField lastVisitField;
    @FXML
    private Button addVisitButton;
    @FXML
    private TextField dateJoinedField;
    @FXML
    private TableView<InvoiceRowItem> invoiceTable;
    @FXML
    private TableColumn<InvoiceRowItem, String> invoiceTypeColumn;
    @FXML
    private TableColumn<InvoiceRowItem, String> invoiceAmountColumn;
    @FXML
    private TableColumn<InvoiceRowItem, String> invoicePaidColumn;

    // Override String from parent so Load() will work correctly
    //protected String FXMLPath="FXMLCustomer.fxml";
    public FXMLCustomerController() {
        FXMLPath = "FXMLCustomer.fxml";
    }

    private final static String ADDNEW = "Add new";
    private final int EXPIRY = 5; // Expiry date in years
    CustomerTO partner;
    private FXMLChildController childController = null; // Only show one child window at a time
    private FXMLCarController carController = null;
    private static String fileChooserPath = null;

    @FXML
    private CheckBox meberCheckBox;
    @FXML
    private Button refuseButton;
    @FXML
    private TextField occupationField;
    @FXML
    private TextField hobbiesField;
    @FXML
    private DatePicker dobDatePicker;
    @FXML
    private Button editMemberButton;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Button exportButton;
    @FXML
    private Button printButton;
    @FXML
    private CheckBox photographyCheckBox;
    @FXML
    private CheckBox giftAidCheckBox;
    @FXML
    private TextField nextOfKinField;
    @FXML
    private TextField contactNoField;
    @FXML
    private TextField relationShipField;
    @FXML
    private CheckBox naturistAwareCheckbox;
    @FXML
    private TextArea notesTextArea;
    @FXML
    private TextField emailField;
    @FXML

    private TableView<CarRowItem> carTable;
    @FXML
    private TableColumn<CarRowItem, String> carRegColumn;
    @FXML
    private TableColumn<CarRowItem, String> carMakeColumn;
    @FXML
    private TableColumn<CarRowItem, String> carModelColumn;
    @FXML
    private TableColumn<CarRowItem, String> carColourColumn;

    @FXML
    private TableView<ChildRowItem> childTable;
    @FXML
    private TableColumn<ChildRowItem, String> childNameColumn;
    @FXML
    private TableColumn<ChildRowItem, String> childSurnameColumn;
    @FXML
    private TableColumn<ChildRowItem, String> childAgeColumn;
    @FXML
    private TableColumn<ChildRowItem, String> childDOBColumn;

    @FXML
    private TableView<ImageRowItem> IDTable;
    @FXML
    private TableColumn<ImageRowItem, String> IDTypeColumn;
    @FXML
    private TableColumn<ImageRowItem, String> IDExpiryColumn;

    @FXML
    private TableView<ImageRowItem> documentTable;
    @FXML
    private TableColumn<ImageRowItem, String> documentTypeColumn;
    @FXML
    private TableColumn<ImageRowItem, String> documentDateColumn;
    @FXML
    private TextField partnerNameField;
    @FXML
    private Button findPartnerButton;

    public void setStage(Stage aStage) {
        super.setStage(aStage);
        aStage.setTitle("Customer details");
        aStage.setResizable(false);
    }

    private CustomerTO aCustomer = null;
    boolean updated = false;

    public boolean isUpdated() {
        return updated;
    }

    private Date getExpiry(Date fromDate) {
        // Add the constant EXPIRY (in years) to current date to get an expiry date
        Calendar cal = MyDate.getCalendar(fromDate);
        cal.add(Calendar.YEAR, EXPIRY);
        return cal.getTime();
    }

    @FXML
    private Button saveButton;
    @FXML
    private TextField forenameField;
    @FXML
    private TextField middlenameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField addressOneField;
    @FXML
    private TextField addressTwoField;
    @FXML
    private TextField townField;
    @FXML
    private TextField countyField;
    @FXML
    private TextField postcodeField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField telephoneOneField;
    @FXML
    private TextField telephoneTwoField;

    @FXML
    private Button cancelButton;

    ObservableList<CarRowItem> myCarList;
    ObservableList<ChildRowItem> myChildList;
    ObservableList<ImageRowItem> myDocumentList;
    ObservableList<ImageRowItem> myIDList;
    ObservableList<InvoiceRowItem> invoiceList;
    // Build lists of anything deleted so we can return the list, then they can be removed...
    List<CarTO> carDeleteList;
    List<ChildTO> childDeleteList;
    List<ImageTO> imageDeleteList;

    @Override

    public FXMLCustomerController load() {
        FXMLCustomerController controller = (FXMLCustomerController) super.load();
        controller.setExitBehaviour();
        return controller;
    }

    public void showRefused() {
        stage.setTitle("Individual should be refused entry");
        scene.setFill(Color.BEIGE);
        forenameField.setStyle("-fx-background-color: red;");
        middlenameField.setStyle("-fx-background-color: red;");
        surnameField.setStyle("-fx-background-color: red;");
        refuseButton.setVisible(false);
        refuseButton.setPrefWidth(0);
    }

    public void setExitBehaviour() {
        stage.setOnCloseRequest(v -> {
            if (updated) {
                Optional button = Utility.showConfirmationAlert("Changes have been made", "Changes have been made to record", "Click ok to save changes or cancel\nto discard");
                if (button.get() == ButtonType.OK) {
                    handleSaveButtonAction(null);
                    Utility.removeTempFiles();
                }
            } else {
                Utility.removeTempFiles();
                stage.close();
            }

        });
    }

    public void initialize(URL url, ResourceBundle rb) {
        partnerNameField.setEditable(false);
        partnerNameField.setContextMenu(new myPartnerContextMenu());
        carDeleteList = new ArrayList();
        childDeleteList = new ArrayList();
        imageDeleteList = new ArrayList();

        postcodeField.textProperty().addListener((e, oldv, newv) -> {
            if (newv != null) {
                postcodeField.setText(newv.toUpperCase());
            }
        });

        // Set up car table
        carTable.setContextMenu(new myContextMenu(carTable));
        carRegColumn.setCellValueFactory(cellData -> cellData.getValue().getRegNoProperty());
        carMakeColumn.setCellValueFactory(cellData -> cellData.getValue().getMakeProperty());
        carModelColumn.setCellValueFactory(cellData -> cellData.getValue().getModelProperty());
        carColourColumn.setCellValueFactory(cellData -> cellData.getValue().getColourProperty());

        myCarList = FXCollections.observableArrayList();
        //    myCarList.add(new CarRowItem(new CarTO(ADDNEW)));
        carTable.setItems(myCarList);

        // Set child table
        myChildList = FXCollections.observableArrayList();
        childNameColumn.setCellValueFactory(cellData -> cellData.getValue().getForenameProperty());
        childSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().getSurnameProperty());
        childDOBColumn.setCellValueFactory(cellData -> cellData.getValue().getDobProperty());
        childAgeColumn.setCellValueFactory(cellData -> {
            StringProperty sp = cellData.getValue().getAgeProperty();
            if (ADDNEW.equals(cellData.getValue().getForenameProperty())) {
                sp.setValue("");
            }
            return sp;
        });

        childTable.setContextMenu(new myContextMenu(childTable));
        //cellData.getValue().getAgeProperty());

        //ID Table
        documentTable.setContextMenu(new myContextMenu(documentTable));
        myIDList = FXCollections.observableArrayList();
        // addNewID();
        IDTable.setItems(myIDList);
        IDTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getDetailsProperty());
        IDExpiryColumn.setCellValueFactory(cellData -> cellData.getValue().getExpiredProperty());
        IDTable.setContextMenu(new myContextMenu(IDTable));

        /*  IDTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        IDTypeColumn.setOnEditCommit(t -> {
            ImageRowItem aDocument = t.getTableView().getItems().get(t.getTablePosition().getRow());
            aDocument.getDetailsProperty().set(t.getNewValue());
        });*/
        //Document table
        //documentTable.setEditable(true);
        documentTable.setContextMenu(new myContextMenu(documentTable));
        myDocumentList = FXCollections.observableArrayList();
        // newImageTO set in ID Table
        addNewDocument();
        documentTable.setItems(myDocumentList);

        documentTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getDetailsProperty());
        documentDateColumn.setCellValueFactory(cellData -> cellData.getValue().getScannedProperty());
        photographyCheckBox.setSelected(false);

        // Invoice column
        invoiceList = FXCollections.observableArrayList();
        invoiceTable.setContextMenu(new myInvoiceContextMenu(invoiceTable));
        invoiceTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getInvoiceTypeProperty());
        invoiceAmountColumn.setCellValueFactory(cellData -> cellData.getValue().getInvoiceAmountProperty());
        invoicePaidColumn.setCellValueFactory(cellData -> cellData.getValue().getInvoicePaidProperty());
        addNewInvoice();
        invoiceTable.setItems(invoiceList);

        meberCheckBox.setOnAction(value -> {
            if (meberCheckBox.isSelected()) {
                handleMemberEditButton(value);
                setForMember(true);
            } else {
                Optional choice = Utility.showConfirmationAlert("Are you sure?", "This will set member as a visitor", "Please confirm. Any membership details\nwill be lost.");
                if (choice.get() == ButtonType.OK) {
                    setVisits();
                    aCustomer.setMembership(null);
                    setForMember(false);
                } else {
                    setForMember(true);
                }
            }
        });
    }

    @FXML
    private void handlePartnerNameClicked(MouseEvent event) {
        if (partner == null) {
            return;
        }
        if (event == null || event.getButton() == MouseButton.PRIMARY) {
            FXMLCustomerController newController = new FXMLCustomerController();
            newController = (FXMLCustomerController) newController.load();
            newController.setCustomerDetails(partner);
            newController.getStage().showAndWait();
            if (newController.isUpdated()) {
                updated = true;
            }
        }
    }

    private void handleRemovePartner() {
        if (partner == null) {
            return;
        }
        Optional<ButtonType> showConfirmationAlert = Utility.showConfirmationAlert("Remove partner", "This action cannot be undone", "If you click OK, this partnership\nwill be removed and clicking\ncancel will not restore");
        if (showConfirmationAlert.get() == ButtonType.OK) {
            String result = SoapHandler.removePartner(aCustomer.getId());
            if ("annulled".equals(result)) {
                partner.setPartnerId(null);
                aCustomer.setPartnerId(null);
                partnerNameField.setText("");
                partner = null;
            } else {
                Utility.showAlert("An error occured", "The server was unable to complete", "The server was unable to cancel\nthe partnership. Try refreshing\nthe customer list and trying\nagain");
            }
        }
    }

    @FXML
    private void setUpdatedKey(KeyEvent event) {
        updated = true;
    }

    @FXML
    private void setUpdatedAction(ActionEvent event) {
        updated = true;
    }

    @FXML
    private void handleIDDragDrop(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasFiles()) {
            String filePath = null;
            for (File file : dragboard.getFiles()) {
                if (file.getName().toLowerCase().endsWith(".pdf")) {
                    //private void readPDFFromFile(File selectedFile, char imageType, ObservableList whichList) {
                    readPDFFromFile(file, 'i', myIDList);
                } else {
                    filePath = file.getAbsolutePath();
                    ImageRowItem iri = populateImageTO(file, 'i');
                    System.out.println(filePath);
                    if (iri != null) {
                        myIDList.add(0, iri);
                        updated = true;
                    }
                }
            }
        }
    }

    @FXML
    private void handleDocDragDropped(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasFiles()) {
            String filePath = null;
            for (File file : dragboard.getFiles()) {

                if (file.getName().toLowerCase().endsWith(".pdf")) {
                    //private void readPDFFromFile(File selectedFile, char imageType, ObservableList whichList) {
                    readPDFFromFile(file, 'd', myDocumentList);
                } else {
                    filePath = file.getAbsolutePath();

                    ImageRowItem iri = populateImageTO(file, 'd');
                    System.out.println(filePath);
                    if (iri != null) {
                        myDocumentList.add(0, iri);
                        updated = true;
                    }
                }
            }
        }
    }

    @FXML
    private void handleIDDragOver(DragEvent event) {
        // Also used by documant table
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    @FXML
    private void handleRefuseButton(ActionEvent event) {

        Optional<ButtonType> showConfirmationAlert = Utility.showConfirmationAlert("Please confirm", "Refuse this person entry?", "Are you sure you wish to refuse this\nperson entry? This cannot be\nrevoked once saved.");
        if (showConfirmationAlert.get() == ButtonType.OK) {
            RefuseTO refusal = new RefuseTO();
            refusal.setDate(MyDate.toXMLGregorianCalendar(new Date()));
            aCustomer.setRefuse(refusal);
            showRefused();
        }
    }

    @FXML
    private void handleAddVisitButton(ActionEvent event) {
        viewVisitDetails();
    }

    @FXML
    private void setMemberUpdatedAction(ActionEvent event) {
        viewMemberDetails();
    }

    @FXML
    private void setVisitUpdatedAction(ActionEvent event) {
        viewVisitDetails();
    }

    private class myPartnerContextMenu extends ContextMenu {

        public myPartnerContextMenu() {
            MenuItem view = new MenuItem("View");
            MenuItem delete = new MenuItem("Remove partner");

            getItems().addAll(view, delete);
            delete.setOnAction(event -> {
                handleRemovePartner();
            });
            view.setOnAction(event -> {
                handlePartnerNameClicked(null);
            });
        }
    }

    private class myContextMenu extends ContextMenu {

        public myContextMenu(TableView targetTable) {
            MenuItem view = new MenuItem("View");
            MenuItem delete = new MenuItem("Delete");

            getItems().addAll(view, delete);
            delete.setOnAction(event -> {
                handleDelete(event);
            });
            view.setOnAction(event -> {
                handleView(event);
            });
        }
    }

    private class myInvoiceContextMenu extends ContextMenu {

        public myInvoiceContextMenu(TableView targetTable) {
            MenuItem markPaid = new MenuItem("Mark paid");
            MenuItem addCharge = new MenuItem("Add a/c charge");

            getItems().addAll(markPaid, addCharge);
            markPaid.setOnAction(event -> {
                handleMarkPaid(event);
            });
            addCharge.setOnAction(event -> {
                handleAddAcCharge(event);
            });
        }
    }

    @FXML
    private void handleKeyPressOnTable(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (keyCode == KeyCode.DELETE) {
            handleDelete(null);
        }
    }

    private void viewVisitDetails() {
        FXMLVisitorHistoryController controller = new FXMLVisitorHistoryController();
        controller = (FXMLVisitorHistoryController) controller.load();
        controller.setItems(aCustomer.getVisitCollection());
        controller.getStage().showAndWait();
        if (controller.isUpdated()) {
            List<VisitRowItem> items = controller.getItems();
            aCustomer.getVisitCollection().clear();
            aCustomer.getVisitCollection().addAll(items);
            setVisits();
            aCustomer.getVisitDeleteCollection().addAll(controller.getDeleteList());
        }
    }

    private void viewMemberDetails() {
        FXMLMemberController controller = new FXMLMemberController();
        controller = (FXMLMemberController) controller.load();
        MembershipTO membership = aCustomer.getMembership();
        if (membership == null) {
            membership = new MembershipTO();
        }
        if (membership.getMembershipNo() == null || membership.getMembershipNo().isEmpty()) {
            // Add membership and set the membership number, just in case we want to save it    
            membership.setCustomer(aCustomer.getId());
            membership.setJoinedDate(MyDate.toXMLGregorianCalendar(new Date(System.currentTimeMillis())));
        }
        controller.setMembership(membership);
        controller.getStage().showAndWait();
        if (controller.isUpdated()) {
            if ("remove".equals(aCustomer.getMembership().getMembershipNo())) {
                aCustomer.setMembership(null);
                setVisits();
            } else if (aCustomer.getMembership() == null) {
                aCustomer.setMembership(controller.getMembership());
                setForMember(true);
                //   dateJoinedField.setText(controller.getMembership().getMembershipNo());
            }
            refreshCustomerDetails();
        }
    }

    @FXML
    private void handleMemberNoClicked(MouseEvent event) {
        viewMemberDetails();
    }

    @FXML
    private void handleFindPartnerButton(ActionEvent event) {
        FXMLSearchCustomerController controller = new FXMLSearchCustomerController();

        controller = (FXMLSearchCustomerController) controller.load();
        controller.setPickAndReturn(true);
        controller.loadCustomers();
        // Pass in who we're selecting for so we can use Child collection and address
        controller.setSelectFor(aCustomer);
        controller.getStage().showAndWait();
        // Check on address that was set for partner
        if (controller.getSelected() != null) {
            storeCustomerDetails();
            CustomerTO partner = controller.getSelected();
            aCustomer.setPartnerId(partner.getId());

            partnerNameField.setText(partner.getForename());
            if (partner.getAddressId().getAddressLineOne() == null && aCustomer.getAddressId().getAddressLineOne() != null) {
                // Partner has null address
                if (showConfirmationAlert("Partner address not set", "Use same address?", "Addresses will be linked").get() == ButtonType.OK) {
                    partner.setAddressId(aCustomer.getAddressId());
                }
            } else if (partner.getAddressId().getAddressLineOne() != null && aCustomer.getAddressId().getAddressLineOne() == null) {
                // Customer has null address, partner has an address
                if (showConfirmationAlert("Customer address not set", "Use partner address?", "Addresses will be linked").get() == ButtonType.OK) {
                    aCustomer.setAddressId(partner.getAddressId());
                }
            } else if (partner.getAddressId().getAddressLineOne() != null && aCustomer.getAddressId().getAddressLineOne() != null) {
                // The two addresses are set - are they separate but similar?
                if (!partner.getAddressId().equals(aCustomer.getAddressId().getId())
                        && partner.getAddressId().getAddressLineOne().equals(aCustomer.getAddressId().getAddressLineOne())) {
                    Optional<ButtonType> result = showConfirmationAlert("Similar addresses found", "The two customers have similar addresses", "Link the addresses?");
                    if (result.get() == ButtonType.OK) {
                        partner.setAddressId(aCustomer.getAddressId());
                    }
                }
            }
            updated = true;
            refreshCustomerDetails();
        }
    }

    @FXML
    private void handleMemberEditButton(ActionEvent event) {
        handleMemberNoClicked(null);
    }

    @FXML
    private void handleExportButton(ActionEvent event) {
        FXMLCopySheetController controller = new FXMLCopySheetController();
        controller = (FXMLCopySheetController) controller.load();
        String text = Utility.toString(aCustomer);
        controller.setText(text);
        controller.getStage().show();
    }

    public void handleView(Event event) {

        ActionEvent myEvent = (ActionEvent) event;
        if (carTable.isFocused()) {
            handleCarClicked(null);
        } else if (childTable.isFocused()) {
            handleChildClicked(null);
        } else if (documentTable.isFocused()) {
            handleDocumentClicked(null);
        } else if (IDTable.isFocused()) {
            handleIDClicked(null);
        } else {
            return;
        }
    }

    private void handleMarkPaid(ActionEvent event) {
        InvoiceRowItem whichInvoice = invoiceTable.getSelectionModel().getSelectedItem();

        ReceiptTO newReceipt = new ReceiptTO();
        newReceipt.setAmount(1);
        newReceipt.setDate(MyDate.toXMLGregorianCalendar(new Date()));
        newReceipt.setNotes("v1");
        whichInvoice.addReceipt(newReceipt);

        System.out.println("cc. handleMarkPaid");
        whichInvoice.getInvoicePaidProperty().setValue("Test");

        updated = true;
    }

    private void handleAddAcCharge(ActionEvent event) {
        ElectricitychargeTO charge = new ElectricitychargeTO();
        charge.setYear(Integer.toString(new Date().getYear()));
        InvoiceTO invoice = new InvoiceTO();
        invoice.setAmount(1);
        charge.getInvoiceList().add(invoice);

        aCustomer.getMembership().getElectricitychargeCollection().add(charge);

        InvoiceRowItem invoiceRowItem = new InvoiceRowItem(invoice);
        invoiceRowItem.setType("Electricity");

        ObservableList<InvoiceRowItem> list = FXCollections.observableArrayList(invoiceRowItem);
        list.addAll(invoiceList);

        invoiceList = list;
        invoiceTable.setItems(list);
    }

    private void handleDelete(ActionEvent event) {
        Object whichItem;
        ObservableList whichList;
        List deleteList;

        if (carTable.isFocused()) {
            whichItem = carTable.getSelectionModel().getSelectedItem();
            whichList = myCarList;
            deleteList = carDeleteList;
        } else if (childTable.isFocused()) {
            whichItem = childTable.getSelectionModel().getSelectedItem();
            whichList = myChildList;
            deleteList = childDeleteList;
        } else if (documentTable.isFocused()) {
            whichItem = documentTable.getSelectionModel().getSelectedItem();
            whichList = myDocumentList;
            deleteList = imageDeleteList;
        } else if (IDTable.isFocused()) {
            whichItem = IDTable.getSelectionModel().getSelectedItem();
            whichList = myIDList;
            deleteList = imageDeleteList;
        } else {
            return;
        }

        if (ADDNEW.equals(whichItem.toString())) {
            return;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Please confirm");
        alert.setHeaderText("If you click OK, this action cannot be reversed");
        alert.setContentText(whichItem + " will be removed. Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // ... user chose OK
            whichList.remove(whichItem);

            deleteList.add(whichItem);
            updated = true;
        }
    }

    public void setfocus() {
        forenameField.requestFocus();
    }

    private void addNewChild() {
        if (myChildList == null) {
            myChildList = FXCollections.observableArrayList();
        }
        ChildTO newChild = new ChildTO();
        newChild.setForename(ADDNEW);
        myChildList.add(new ChildRowItem(newChild));
    }

    private void addNewCar() {
        if (myCarList == null) {
            myCarList = FXCollections.observableArrayList();
        }
        CarTO newCar = new CarTO();
        newCar.setRegno(ADDNEW);
        myCarList.add(new CarRowItem(newCar));
    }

    private void addNewInvoice() {
        if (invoiceList == null) {
            invoiceList = FXCollections.observableArrayList();
        }
        InvoiceTO newInvoice = new InvoiceTO();
        newInvoice.setType(ADDNEW);
        invoiceList.add(new InvoiceRowItem(newInvoice));
    }

    private void addNewDocument() {
        if (myDocumentList == null) {
            myDocumentList = FXCollections.observableArrayList();
        }
        ImageTO newImage = new ImageTO();
        newImage.setDetails(ADDNEW);
        myDocumentList.add(new ImageRowItem(newImage));
    }

    private void addNewID() {
        if (myIDList == null) {
            myIDList = FXCollections.observableArrayList();
        }
        ImageTO newImage = new ImageTO();
        newImage.setDetails(ADDNEW);
        myIDList.add(new ImageRowItem(newImage));
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        if (surnameField.getText() == null || forenameField.getText() == null
                || "".equals(surnameField.getText()) || "".equals(forenameField.getText())) {
            new Alert(Alert.AlertType.ERROR, "Must enter fore and surnames at least!").showAndWait();
            return;
        }

        for (ImageRowItem eachDocument : myDocumentList) {
            if (!ADDNEW.equals(eachDocument.getDetails())) {
                if (eachDocument.getId() == null || eachDocument.getId() == 0) {
                    if (eachDocument.getFilename() == null) {
                        System.err.println("Problem with image filename, not saving");
                    } else {
                        eachDocument.setCustomerId(aCustomer.getId());
                        System.out.println("Saved : " + eachDocument.getFilename());
                        String result = PostFile.post(eachDocument);
                        System.out.println("Document saved = " + result);
                        if (result == null) {
                            Utility.showAlert("Problem encountered", "Encountered a problem saving the image", "Please try to save again. If unsuccessful after a few attempts, try removing the image from the record, saving the customer record and then re-opening the record and adding again.");
                            return;
                        } else {
                            System.out.println("Saved, url = " + result);
                            String imageUrl = result.split("=")[1];
                            if (!imageUrl.equals(eachDocument.getUrl())) {
                                eachDocument.setUrl(imageUrl);
                            }
                        }
                    }
                }
            }
        }
        for (ImageRowItem eachDocument : myIDList) {
            if (!ADDNEW.equals(eachDocument.getDetails())) {
                if (eachDocument.getId() == null || eachDocument.getId() == 0) {
                    if (eachDocument.getFilename() == null) {
                        System.err.println("Problem with image filename, not saving");
                    } else {
                        eachDocument.setCustomerId(aCustomer.getId());
                        String result = PostFile.post(eachDocument);
                        System.out.println("Document saved = " + result);
                        if (result == null) {
                            Utility.showAlert("Problem encountered", "Encountered a problem saving the image", "Please try to save again. If unsuccessful after\na few attempts, try removing the image from the\nrecord, saving the customer record and then\nre-opening the record and adding again.\nIf the image is over 10MB it will need resizing.");
                            return;
                        } else {
                            System.out.println("Saved document, url : " + result);
                            String imageUrl = result.split("=")[1];
                            if (!imageUrl.equals(eachDocument.getUrl())) {
                                eachDocument.setUrl(imageUrl);
                            }
                        }
                    }
                }
            }
        }

        storeCustomerDetails();
        String value = null;
        try {
            value = SoapHandler.saveCustomer(aCustomer);
        } catch (Exception e) {
            Utility.showAlert("Error saving customer!", "Fault received from server", e.getMessage());
            System.err.println("Error saving customer! SOAP fault received from server : " + e.getMessage());
            e.printStackTrace();
            //getScene().getWindow().hide();
            return;
        }
        // for a new customer, value should hold the ID
        try {
            aCustomer.setId(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            System.err.println("Problem parsing ID of customer (CustomerController) : " + e.getMessage());
            System.err.println("Value of value : " + value);
            // Not a valid id!
        }

        updated = true;
        getScene().getWindow().hide();
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        updated = false;
        getScene().getWindow().hide();
    }

    private void storeCustomerDetails() {
        if (aCustomer == null) {
            aCustomer = Utility.createCustomer();
        }

        aCustomer.setForename(Name.capitaliseName(forenameField.getText()));
        aCustomer.setMiddlenames(middlenameField.getText());
        aCustomer.setSurname(Name.capitaliseName(surnameField.getText()));
        AddressTO theAddress = aCustomer.getAddressId();
        if (theAddress == null) {
            theAddress = new AddressTO();
            aCustomer.setAddressId(theAddress);
        }
        theAddress.setAddressLineOne(addressOneField.getText());
        theAddress.setAddressLineTwo(addressTwoField.getText());
        theAddress.setTown(townField.getText());
        theAddress.setCounty(countyField.getText());
        theAddress.setPostcode(postcodeField.getText());
        theAddress.setCountry(countryField.getText());

        aCustomer.setEmail(emailField.getText());
        aCustomer.setTelephoneOne(telephoneOneField.getText());
        aCustomer.setTelephoneTwo(telephoneTwoField.getText());

        aCustomer.setOccupation(occupationField.getText());
        aCustomer.setHobbies(hobbiesField.getText());

        aCustomer.setDob(MyDate.toXMLGregorianCalendar(dobDatePicker.getValue()));

        aCustomer.setGiftAid(giftAidCheckBox.isSelected());
        aCustomer.setPhotography(photographyCheckBox.isSelected());

        if ((nextOfKinField.getText() != null && nextOfKinField.getText().length() > 0)
                || ((relationShipField.getText() != null && relationShipField.getText().length() > 0))) {
            if (aCustomer.getNextOfKin().isEmpty()) {
                aCustomer.getNextOfKin().add(new NextOfKinTO());
            }
            aCustomer.getNextOfKin().get(0).setCustomerId(aCustomer.getId());
            aCustomer.getNextOfKin().get(0).setName(nextOfKinField.getText());
            aCustomer.getNextOfKin().get(0).setContactNo(contactNoField.getText());
            aCustomer.getNextOfKin().get(0).setRelationship(relationShipField.getText());
            aCustomer.getNextOfKin().get(0).setAwareNaturist(naturistAwareCheckbox.isSelected());
        }

        //aCustomer.setNotes(notesTextArea.getText());
        if (notesTextArea.getText() != null && notesTextArea.getText().length() > 0) {
            if (aCustomer.getNotes().isEmpty()) {
                aCustomer.getNotes().add(new NotesTO());
            }
            aCustomer.getNotes().get(0).setNotes(notesTextArea.getText());
        }

        // Reset the collections and create again from table views
        aCustomer.getCarCollection().clear();
        for (CarTO eachCar : myCarList) {
            if (!ADDNEW.equals(eachCar.getRegno())) {
                aCustomer.getCarCollection().add(eachCar);
            }
        }

        aCustomer.getChildCollection().clear();
        for (ChildTO eachChild : myChildList) {
            if (!ADDNEW.equals(eachChild.getForename())) {
                aCustomer.getChildCollection().add(eachChild);
            }
        }

        aCustomer.getImageCollection().clear();
        for (ImageTO eachImage : myDocumentList) {
            if (!ADDNEW.equals(eachImage.getDetails())) {
                aCustomer.getImageCollection().add(eachImage);
            }
        }
        for (ImageTO eachImage : myIDList) {
            if (!ADDNEW.equals(eachImage.getDetails())) {
                aCustomer.getImageCollection().add(eachImage);
                System.out.println("Adding ID " + eachImage.getFilename());
            }
        }

        aCustomer.getCarDeleteCollection().addAll(carDeleteList);
        aCustomer.getChildDeleteCollection().addAll(childDeleteList);
        aCustomer.getImageDeleteCollection().addAll(imageDeleteList);
    }

    public void refreshCustomerDetails() {
        // Already set aCustomer, we've done something and need to refresh the display if 
        // the Customer is different or the display hasn't yet been set
        if ("".equals(forenameField.getText()) || !"".equals(aCustomer.getForename())) {
            forenameField.setText(aCustomer.getForename());
        }
        if ("".equals(middlenameField.getText()) || !"".equals(aCustomer.getMiddlenames())) {
            middlenameField.setText(aCustomer.getMiddlenames());
        }
        if ("".equals(surnameField.getText()) || !"".equals(aCustomer.getSurname())) {
            surnameField.setText(aCustomer.getSurname());
        }

        AddressTO theAddress = aCustomer.getAddressId();
        if ("".equals(addressOneField.getText()) || !"".equals(theAddress.getAddressLineOne())) {
            addressOneField.setText(theAddress.getAddressLineOne());
        }
        if ("".equals(addressTwoField.getText()) || !"".equals(theAddress.getAddressLineTwo())) {
            addressTwoField.setText(theAddress.getAddressLineTwo());
        }
        if ("".equals(townField.getText()) || !"".equals(theAddress.getTown())) {
            townField.setText(theAddress.getTown());
        }
        if ("".equals(countyField.getText()) || !"".equals(theAddress.getCounty())) {
            countyField.setText(theAddress.getCounty());
        }
        if ("".equals(postcodeField.getText()) || !"".equals(theAddress.getPostcode())) {
            postcodeField.setText(theAddress.getPostcode());
        }
        if ("".equals(countryField.getText()) || !"".equals(theAddress.getCountry())) {
            countryField.setText(theAddress.getCountry());
        }

        if ("".equals(telephoneOneField.getText()) || !"".equals(aCustomer.getTelephoneOne())) {
            telephoneOneField.setText(aCustomer.getTelephoneOne());
        }
        if ("".equals(telephoneOneField.getText()) || !"".equals(aCustomer.getTelephoneOne())) {
            telephoneTwoField.setText(aCustomer.getTelephoneTwo());
        }
        if ("".equals(emailField.getText()) || !"".equals(aCustomer.getEmail())) {
            emailField.setText(aCustomer.getEmail());
        }
        if ("".equals(occupationField.getText()) || !"".equals(aCustomer.getOccupation())) {
            occupationField.setText(aCustomer.getOccupation());
        }
        if ("".equals(hobbiesField.getText()) || !"".equals(aCustomer.getHobbies())) {
            hobbiesField.setText(aCustomer.getHobbies());
        }
        if ("".equals(notesTextArea.getText())) {
            if (aCustomer.getNotes() != null
                    && aCustomer.getNotes().size() > 0
                    && !"".equals(aCustomer.getNotes().get(0).getNotes())) {
                notesTextArea.setText(aCustomer.getNotes().get(0).getNotes());
            }
        }
        if (aCustomer.getNextOfKin() != null && aCustomer.getNextOfKin().size() > 0) {
            if ("".equals(nextOfKinField.getText()) || !"".equals(aCustomer.getNextOfKin().get(0).getName())) {
                nextOfKinField.setText(aCustomer.getNextOfKin().get(0).getName());
                contactNoField.setText(aCustomer.getNextOfKin().get(0).getContactNo());
                relationShipField.setText(aCustomer.getNextOfKin().get(0).getRelationship());
                naturistAwareCheckbox.setSelected(aCustomer.getNextOfKin().get(0).isAwareNaturist());
            }
        }

        if (aCustomer.getMembership() != null && aCustomer.getMembership().getJoinedDate() != null) {
            String date = df.format(MyDate.toDate(aCustomer.getMembership().getJoinedDate()));

            dateJoinedField.setText(date);
        } else {
            dateJoinedField.setText("n/a");
        }

        //dobDatePicker.setValue(MyDate.toLocalDate(myCustomer.getDob()));
    }

    public void setCustomerDetails(CustomerTO myCustomer) {

        aCustomer = myCustomer;
        if (aCustomer.getId() == null) {
            aCustomer.setId(0);
        }
        showCustomerDetails(aCustomer);

        /*      if (myCustomer == null || myCustomer.getId() == null) {
            aCustomer = new SearchRowItem();
            return;
        }
        if (aCustomer == null) {
            mainAnchorPane.setDisable(true);
        }
        Task task = new Task() {
            protected Integer call() throws Exception {
                try {
                    long start = System.currentTimeMillis();
                    aCustomer = SoapHandler.getCustomerByID(myCustomer.getId());
                    System.out.println("Request took " + ((System.currentTimeMillis() - start)) + " milliseconds");
                    if (aCustomer.getId() == null) {
                        aCustomer.setId(0);
                    }
                    Platform.runLater(() -> {
                        showCustomerDetails(aCustomer);
                        mainAnchorPane.setDisable(false);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();*/
    }

    private void showCustomerDetails(CustomerTO myCustomer) {
        forenameField.setText(myCustomer.getForename());
        middlenameField.setText(myCustomer.getMiddlenames());
        surnameField.setText(myCustomer.getSurname());

        AddressTO theAddress = myCustomer.getAddressId();
        addressOneField.setText(theAddress.getAddressLineOne());
        addressTwoField.setText(theAddress.getAddressLineTwo());
        townField.setText(theAddress.getTown());
        countyField.setText(theAddress.getCounty());
        postcodeField.setText(theAddress.getPostcode());
        countryField.setText(theAddress.getCountry());

        telephoneOneField.setText(myCustomer.getTelephoneOne());
        telephoneTwoField.setText(myCustomer.getTelephoneTwo());
        emailField.setText(myCustomer.getEmail());

        giftAidCheckBox.setSelected(myCustomer.isGiftAid() == null ? false : myCustomer.isGiftAid());
        if (myCustomer.isPhotography() != null) {
            photographyCheckBox.setSelected(myCustomer.isPhotography());
        }

        if (myCustomer.getMembership() != null && myCustomer.getMembership().getJoinedDate() != null) {
            String date = df.format(MyDate.toDate(myCustomer.getMembership().getJoinedDate()));
            setForMember(true);
            dateJoinedField.setText(date);
        }
        setVisits();

        if (myCustomer.getPartnerId() != null) {
            partner = SoapHandler.getCustomerByID(myCustomer.getPartnerId());

            if (partner != null) {
                partnerNameField.setText(partner.getForename());
            }
        }
        occupationField.setText(myCustomer.getOccupation());
        hobbiesField.setText(myCustomer.getHobbies());

        dobDatePicker.setValue(MyDate.toLocalDate(myCustomer.getDob()));

        if (myCustomer.getNotes() != null && myCustomer.getNotes().size() > 0 && myCustomer.getNotes().get(0).getNotes() != null) {
            notesTextArea.setText(myCustomer.getNotes().get(0).getNotes());
        } else {
            notesTextArea.setText("");
        }

        if (aCustomer.getNextOfKin() != null && aCustomer.getNextOfKin().size() > 0) {
            nextOfKinField.setText(aCustomer.getNextOfKin().get(0).getName());
            contactNoField.setText(aCustomer.getNextOfKin().get(0).getContactNo());
            relationShipField.setText(aCustomer.getNextOfKin().get(0).getRelationship());
            naturistAwareCheckbox.setSelected(aCustomer.getNextOfKin().get(0).isAwareNaturist());
        } else {
            nextOfKinField.setText("");
            contactNoField.setText("");
            relationShipField.setText("");
            naturistAwareCheckbox.setSelected(false);
        }

        myCarList = FXCollections.observableArrayList();
        for (CarTO eachCar : myCustomer.getCarCollection()) {
            myCarList.add(new CarRowItem(eachCar));
        }
        addNewCar();
        carTable.setItems(myCarList);

        myChildList = FXCollections.observableArrayList();
        for (ChildTO eachChild : myCustomer.getChildCollection()) {
            myChildList.add(new ChildRowItem(eachChild));
        }
        addNewChild();
        childTable.setItems(myChildList);

        myDocumentList = FXCollections.observableArrayList();
        myIDList = FXCollections.observableArrayList();
        for (ImageTO eachImage : myCustomer.getImageCollection()) {
            ImageRowItem eachRow = new ImageRowItem(eachImage);
            if (eachImage.getType() == 'd') {
                myDocumentList.add(eachRow);
            } else if (eachImage.getType() == 'i') {
                myIDList.add(eachRow);
            }
        }
        addNewDocument();
        addNewID();
        documentTable.setItems(myDocumentList);
        IDTable.setItems(myIDList);

        invoiceList = FXCollections.observableArrayList();
        if (myCustomer.getMembership() != null && myCustomer.getMembership().getElectricitychargeCollection() != null) {
            for (ElectricitychargeTO eachcharge : myCustomer.getMembership().getElectricitychargeCollection()) {
                for (InvoiceTO eachInvoice : eachcharge.getInvoiceList()) {
                    InvoiceRowItem invoiceRowItem = new InvoiceRowItem(eachInvoice);
                    invoiceRowItem.getInvoiceTypeProperty().setValue("Electricity " + eachcharge.getYear());
                    invoiceList.add(invoiceRowItem);
                }
            }
        }
        addNewInvoice();
        invoiceTable.setItems(invoiceList);

        if (myCustomer.getRefuse() != null && myCustomer.getRefuse().getDate() != null) {
            showRefused();
        }
        updated = false;
    }

    private boolean editCar(CarTO aCarTO) {
        if (carController == null) {
            carController = new FXMLCarController();
            carController = (FXMLCarController) carController.load();
            carController.setCarTO(aCarTO);
            carController.getStage().showAndWait();
            boolean returnValue = carController.isUpdated();
            carController = null;
            if (returnValue) {
                updated = true;
            }
            return returnValue;
        } else {
            carController.getStage().toFront();
            return false;
        }
    }

    @FXML
    private void handleCarClicked(MouseEvent event) {
        if (event == null || event.getButton().equals(MouseButton.PRIMARY)) {
            CarRowItem selection = (CarRowItem) carTable.getSelectionModel().getSelectedItem();
            if (ADDNEW.equals(selection.getRegno())) {
                CarTO newCar = new CarTO();
                if (editCar(newCar)) {
                    myCarList.add(0, new CarRowItem(newCar));
                }
            }
            if (event == null || event.getClickCount() >= 2) {
                editCar(selection);
            }
        }
    }

    private boolean editChild(ChildTO aChildTO) {
        if (childController == null) {
            childController = new FXMLChildController();
            childController = (FXMLChildController) childController.load();
            childController.setChildTO(aChildTO);
            childController.getStage().showAndWait();
            boolean returnValue = childController.isUpdated();
            if (returnValue) {
                updated = true;
            }
            childController = null;
            return returnValue;
        } else {
            childController.getStage().toFront();
            return false;
        }

    }

    @FXML
    private void handleChildClicked(MouseEvent event) {
        if (event == null || event.getButton().equals(MouseButton.PRIMARY)) {
            ChildRowItem selection = (ChildRowItem) childTable.getSelectionModel().getSelectedItem();
            if (ADDNEW.equals(selection.getForename())) {
                ChildTO newChild = new ChildTO();
                if (editChild(newChild)) {
                    myChildList.add(0, new ChildRowItem(newChild));
                }
            }

            if (event == null || event.getClickCount() >= 2) {
                editChild(selection);
            }
        }
    }

    private ImageRowItem populateImageTO(File aFile, Image anImage, char imageType) {
        // Create an ImageRowItem given a file (for its attributes) and an image
        BasicFileAttributes attr = null;
        try {
            Path path = Paths.get(aFile.getAbsolutePath());
            attr
                    = Files.readAttributes(path, BasicFileAttributes.class
                    );
        } catch (IOException e) {
            System.err.println("Error reading image file : " + e.getMessage());
            return null;
        }

        // Need to add image to table view
        ImageTO newImage = new ImageTO();

        // Populate the ImageTO
        newImage.setCustomerId(aCustomer.getId());
        newImage.setDetails(aFile.getName());

        long modified = attr.creationTime().toMillis();
        //long modified = selectedFile.lastModified();
        Date dateModified = modified == 0 ? new Date() : new Date(modified);
        newImage.setScanned(MyDate.toXMLGregorianCalendar(dateModified));
        newImage.setExpires(MyDate.toXMLGregorianCalendar(getExpiry(dateModified)));
        newImage.setType(imageType);
        newImage.setFilename(aFile.getAbsolutePath());

        try {
            newImage.setUrl(URLEncoder.encode(aFile.getName(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println("Should never get here! : " + e.getMessage());
        }
        // And set up the ImageRowItem
        ImageRowItem newImageRow = new ImageRowItem(newImage);
        newImageRow.setTheImage(Utility.imageToBufferedImage(anImage));

        updated = showImage(newImageRow);
        if (updated == false) {
            return null;
        }
        return newImageRow;
    }

    private ImageRowItem populateImageTO(File aFile, char imageType) {
        // create imageRowItem from a file containing an image
        BufferedImage img = null;
        BasicFileAttributes attr = null;
        try {
            img = ImageIO.read(new File(aFile.getAbsolutePath()));
        } catch (IOException e) {
            System.err.println("Error reading image : " + e.getMessage());
            return null;
        }
        if (img == null) {
            Utility.showAlert("Error reading image", "An error occured reading the image", "Please ensure the image type is supported");
            return null;
        }
        ImageRowItem newImageRow = populateImageTO(aFile, img, imageType);

        if (newImageRow == null || newImageRow.getTheImage() == null) {
            return null;
        }
        return newImageRow;
    }

    private void readPDFFromFile(File selectedFile, char imageType, ObservableList whichList) {
        PDFDocument document = new PDFDocument();
        try {
            document.load(selectedFile);

            SimpleRenderer renderer = new SimpleRenderer();

            // set resolution (in DPI)
            renderer.setResolution(300);

            List<Image> images = renderer.render(document);
            System.out.println("Images counted : " + images.size());
            String tempDir = System.getProperty("java.io.tmpdir");
            boolean check = true;
            if (images.size() > 1) {
                Optional<ButtonType> showConfirmationAlert = Utility.showConfirmationAlert("Multipage PDF",
                        "The selected PDF consists of " + images.size() + " pages that will\nbe processed individually.",
                        "Please confirm you wish to go ahead");
                check = (showConfirmationAlert.get() == ButtonType.OK);
            }
            if (check) {
                for (int i = 0; i < images.size(); i++) {
                    // Extract and display images
                    ImageRowItem newImageRow = populateImageTO(selectedFile, images.get(i), imageType);
                    if (newImageRow != null && newImageRow.getTheImage() != null) {
                        // If we're using, save a temporary file for upload
                        newImageRow.setUrl(i + "_" + newImageRow.getUrl().toLowerCase().replace(".pdf", ".png"));
                        System.out.println("Generated url : " + newImageRow.getUrl());
                        // URL is URL, Filename needs to be filename, so not URL encoded
                        File tempFile = new File(tempDir + File.separator + URLDecoder.decode(newImageRow.getUrl(), "UTF-8"));
                        ImageIO.write((RenderedImage) images.get(i), "png", tempFile);
                        newImageRow.setFilename(tempFile.getCanonicalPath());
                        whichList.add(0, newImageRow);
                        Utility.addFileToTempList(tempFile);
                    }
                }
            }
        } catch (DocumentException e) {
            System.err.println("Exception reading or rendering PDF : " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Exception reading PDF : " + e.getMessage());
            e.printStackTrace();
        } catch (RendererException e) {
            System.err.println("Exception rendering PDF : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleImageClicked(MouseEvent event, char imageType, TableView whichTable, ObservableList whichList) {
        // Deal with user clicking either on Document or ID table - both are stored in same table...
        // Ignore Right mouse, act on PRIMARY (Normally leftmouse)
        if (event == null || event.getButton().equals(MouseButton.PRIMARY)) {
            ImageRowItem selection = (ImageRowItem) whichTable.getSelectionModel().getSelectedItem();
            // Cover case of null selection
            if (selection == null) {
                return;
            }
            // Has user clicked "Add new"?
            if (ADDNEW.equals(selection.getDetailsProperty().getValue())) {
                // Add new image - bring up a file browser window to locate

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select image file");
                fileChooser.getExtensionFilters().addAll(
                        new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.PNG", "*.JPG", "*.pdf", "*.PDF"),
                        new ExtensionFilter("All Files", "*.*")
                );
                if (fileChooserPath != null) {
                    fileChooser.setInitialDirectory(new File(fileChooserPath));
                }
                File selectedFile = fileChooser.showOpenDialog(getStage());
                if (selectedFile != null) {
                    fileChooserPath = selectedFile.getParent();

                    System.out.println("path = " + fileChooserPath);
                    System.out.println("selectedFile = " + selectedFile);
                    if (selectedFile.getName().toLowerCase().endsWith(".pdf")) {
                        // Dealing with PDF
                        readPDFFromFile(selectedFile, imageType, whichList);

                    } else {
                        ImageRowItem newImageRow = populateImageTO(selectedFile, imageType);
                        if (newImageRow != null) {
                            whichList.add(0, newImageRow);
                        }
                    }
                }
            } else// Not adding new, so display image if double click
            {
                if (event == null || event.getClickCount() >= 2) {
                    if (selection.getTheImage() == null) {
                        // we need to pull the image from the server
                        try {
                            URL serviceUrl = new URL("https://idserver:8181/IDTrackerServer/ImageServlet?id=" + aCustomer.getId() + "&url=" + selection.getUrl() + "&sessionid=" + SoapHandler.getSessionId());
                            System.out.println("URL : " + serviceUrl);

                            SSLUtilities.trustAllHostnames();
                            SSLUtilities.trustAllHttpsCertificates();

                            selection.setTheImage(ImageIO.read(serviceUrl));
                        } catch (Exception e) {
                            System.err.println("Error retrieving image : " + e.getMessage());
                            Utility.showAlert("Error occured retrieving image", "Image could not be found on server", e.getMessage());
                            return;
                        }
                    }
                    showImage(selection);
                }
            }
        }
    }

    @FXML
    private void handleDocumentClicked(MouseEvent event) {
        handleImageClicked(event, 'd', documentTable, myDocumentList);
    }

    @FXML
    private void handleIDClicked(MouseEvent event) {
        handleImageClicked(event, 'i', IDTable, myIDList);
    }

    private boolean showImage(ImageRowItem anImage) {
        FXMLImageViewController controller = new FXMLImageViewController();

        controller = (FXMLImageViewController) controller.load();
        controller.getStage().show();
        controller.setImage(anImage);
        controller.getStage().hide();
        controller.getStage().showAndWait();
        if (controller.isUpdated()) {
            IDTable.refresh();
            documentTable.refresh();
        }
        return controller.isUpdated();
    }

    @FXML
    private void handlePrintButton(ActionEvent event) {
        FXMLCustomerController printController = new FXMLCustomerController();
        printController = (FXMLCustomerController) printController.load();
        printController.setUpPrint();
        if (printController.okToPrint()) {
            printController.getStage().show();
            // Details stored in form aren't same as aCustomer until we click save! 
            // Store current details and reset after
            CustomerTO tempCustomer = aCustomer;
            storeCustomerDetails();
            setCustomerDetails(aCustomer);
            printController.setCustomerDetails(aCustomer);
            printController.print();
            aCustomer = tempCustomer;
        }
        printController.endPrint();//finally end the printing job.
    }

    private Printer printer;
    private PageLayout pageLayout;
    private Stage dialogStage;
    private boolean showDialog;
    private PrinterJob job;
    private int noOfPages = 0;

    public void setUpPrint(int noOfPages) {
        this.noOfPages = noOfPages;
        setUpPrint();
    }

    public void setUpPrint() {
        printer = Printer.getDefaultPrinter();
        pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT); //create a pagelayout.  I used Paper.NA_LETTER for a standard 8.5 x 11 in page.
        dialogStage = new Stage(StageStyle.DECORATED);
        if (printer == null) {
            System.err.println("Error getting printer");
            return;
        }
        job = PrinterJob.createPrinterJob(printer);
        if (noOfPages > 1) {
            PageRange pageRange[] = new PageRange[1];
            pageRange[0] = new PageRange(1, noOfPages);
            job.getJobSettings().setPageRanges(pageRange);
        }
        if (job != null) {
            showDialog = job.showPrintDialog(dialogStage);
        }
    }

    public boolean okToPrint() {
        return showDialog;
    }

    public void print() {

        if (showDialog) {
            //stage.show();
            ProgressIndicator pi = new ProgressIndicator();
            pi.show();
            Paint originalColor = scene.getFill();
            mainAnchorPane.setStyle("-fx-background-color: white;");
            forenameField.setStyle("-fx-background-color: white;");
            surnameField.setStyle("-fx-background-color: white;");
            middlenameField.setStyle("-fx-background-color: white;");
            addressOneField.setStyle("-fx-background-color: white;");
            addressTwoField.setStyle("-fx-background-color: white;");
            townField.setStyle("-fx-background-color: white;");
            countyField.setStyle("-fx-background-color: white;");
            postcodeField.setStyle("-fx-background-color: white;");
            countryField.setStyle("-fx-background-color: white;");
            telephoneOneField.setStyle("-fx-background-color: white;");
            telephoneTwoField.setStyle("-fx-background-color: white;");
            emailField.setStyle("-fx-background-color: white;");
            occupationField.setStyle("-fx-background-color: white;");
            hobbiesField.setStyle("-fx-background-color: white;");
            notesTextArea.setStyle("-fx-background-color: white;");
            dobDatePicker.setStyle("-fx-background-color: white;");
            lastVisitField.setStyle("-fx-background-color: white;");
            dateJoinedField.setStyle("-fx-background-color: white;");
            partnerNameField.setStyle("-fx-background-color: white;");
            nextOfKinField.setStyle("-fx-background-color: white;");
            contactNoField.setStyle("-fx-background-color: white;");
            relationShipField.setStyle("-fx-background-color: white;");
            notesTextArea.setStyle("-fx-background-color: white;");

            saveButton.setVisible(false);
            printButton.setVisible(false);
            exportButton.setVisible(false);
            cancelButton.setVisible(false);
            editMemberButton.setVisible(false);
            findPartnerButton.setVisible(false);

            for (CarRowItem car : carTable.getItems()) {
                if (car.getRegno().equals(ADDNEW)) {
                    car.setRegno("");
                }
            }

            for (ChildRowItem child : childTable.getItems()) {
                if (child.getForename().equals(ADDNEW)) {
                    child.setForename("");
                }
            }

            for (ImageRowItem doc : documentTable.getItems()) {
                if (doc.getDetails().equals(ADDNEW)) {
                    doc.setDetails("");
                }
            }

            for (ImageRowItem doc : IDTable.getItems()) {
                if (doc.getDetails().equals(ADDNEW)) {
                    doc.setDetails("");
                }
            }

            double pagePrintableWidth = pageLayout.getPrintableWidth(); //this should be 8.5 inches for this page layout.
            double pagePrintableHeight = pageLayout.getPrintableHeight();// this should be 11 inches for this page layout.

            //  IntegerBinding tableHeight = Bindings.size(searchResultsTable.getItems()).multiply(28);
            //  searchResultsTable.prefHeightProperty().bind(tableHeight);// If your cells' rows are variable size you add the .multiply and play with the input value until your output is close to what you want. If your cells' rows are the same height, I think you can use .multiply(1). This changes the height of your tableView to show all rows in the table.
            //  searchResultsTable.minHeightProperty().bind(searchResultsTable.prefHeightProperty());//You can probably play with this to see if it's really needed.  Comment it out to find out.
            //searchResultsTable.maxHeightProperty().bind(searchResultsTable.prefHeightProperty());//You can probably play with this to see if it' really needed.  Comment it out to find out.
            double scaleX = pagePrintableWidth / mainAnchorPane.getBoundsInParent().getWidth();//scaling down so that the printing width fits within the paper's width bound.
            double scaleY = scaleX; //scaling the height using the same scale as the width. This allows the writing and the images to maintain their scale, or not look skewed.
            double localScale = scaleX; //not really needed since everything is scaled down at the same ratio. scaleX is used thoughout the program to scale the print out.

            Scale tableScale = new Scale(scaleX, scaleY);
            mainAnchorPane.getTransforms().add(tableScale);//scales the printing. Allowing the width to say within the papers width, and scales the height to do away with skewed letters and images.

            //Since the height of what needs to be printed is longer than the paper's heights we use gridTransfrom to only select the part to be printed for a given page.
            Translate gridTransform = new Translate();
            mainAnchorPane.getTransforms().add(gridTransform);

            //now we loop though the image that needs to be printed and we only print a subimage of the full image.
            //for example: In the first loop we only pint the printable image from the top down to the height of a standard piece of paper. Then we print starting from were the last printed page ended down to the height of the next page. This happens until all of the pages are printed. 
            // first page prints from 0 height to -11 inches height, Second page prints from -11 inches height to -22 inches height, etc. 
            ProgressBar pb = pi.getPb();

            job.printPage(pageLayout, mainAnchorPane);

            pb.setProgress(1);

            pi.hide();

        }
    }

    public void endPrint() {
        if (job != null) {
            job.endJob();
        }
        stage.hide();
        System.out.println("job done : " + job.getJobStatus());
    }

    private void setVisits() {
        if (aCustomer != null && aCustomer.getVisitCollection() != null && aCustomer.getVisitCollection().size() > 0) {
            List<VisitTO> visitCollection = aCustomer.getVisitCollection();
            visitCollection.sort((c, d) -> {
                return d.getStartDate().compare(c.getStartDate());
            });
            lastVisitField.setText(df.format(MyDate.toDate(visitCollection.get(0).getStartDate())));
        } else {
            lastVisitField.setText("No visits");
        }
    }

    private void setForMember(Boolean value) {
        meberCheckBox.setSelected(value);

        if (aCustomer != null && value) {
            String date = df.format(MyDate.toDate(aCustomer.getMembership().getJoinedDate()));
            dateJoinedField.setText(date);
        } else {
            dateJoinedField.setText("");
        }
    }
}
