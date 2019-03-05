package controller;

import IDTrackerTO.EventTO;
import Soap.CustomerTO;
import Soap.ElectricitychargeTO;
import UtilityClasses.MyDate;
import static controller.FXMLMemberController.LARGE_VAN_PITCH;
import static controller.FXMLMemberController.TENT_PITCH;
import static controller.FXMLMemberController.VAN_PITCH;
import filter.CustomerToTextFilter;
import filter.DisplayFilter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.PageRange;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.xml.datatype.DatatypeConstants;
import model.SearchRowItem;
import model.SoapHandler;
import utility.CustomerFilter;
import utility.CustomerListFilter;
import utility.IDPreferences;
import utility.MailHandler;
import utility.ProgressIndicator;
import utility.Utility;

/**
 * FXML Controller class
 *
 * @author tezk
 */
public class FXMLSearchCustomerController extends FXMLParentController implements Initializable {

    private static final String SELECT_TITLE = "Selecting";
    private static final String SEARCH_TITLE = "BH ID tracker customer list";
    private static final String EVENT_SEARCH_TITLE = "Attendees for ";
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    private ContextMenu contextMenu;
    // If we're selecting for an Event
    private EventTO forEvent = null;

    // ObservableList<SearchRowItem> searchRowItemList; // Points to current used list
    ObservableList<SearchRowItem> filteredRowItemList;
    List<CustomerTO> fullCustomerList = null;         // Full list of customers
    ObservableList<SearchRowItem> fullCustomerRowItemList;
    List<CustomerTO> eventAttendeesList = null;       // Event attendees
    ObservableList<SearchRowItem> eventCustomerRowItemList;
    // Set up a set so we can access by userId when getting partner details etc
    Map<Integer, SearchRowItem> customerSet;

    CustomerListFilter queryFilter = null;
    CustomerListFilter searchFilter = null;
    CustomerListFilter attendeesFilter = null;
    CustomerListFilter showOnlyFilter = null; // Show only
    FXMLQueryController queryController = null;
    List<CustomerListFilter> filterList = new ArrayList();

    @FXML
    private Button addNewButton;
    @FXML
    private CheckBox attendeesOnlyCheckbox;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private VBox mainVBox;
    @FXML
    private AnchorPane tableAnchorPane;
    @FXML
    private AnchorPane subAnchorPane;
    @FXML
    private ChoiceBox<String> filterSelection;
    @FXML
    private Label customerCountStatus;
    @FXML
    private HBox customersDisplayedHBox;
    @FXML
    private Label queryStatus;
    @FXML
    private HBox queryHBox;

    // Override String from parent so Load() will work correctly
    //protected String FXMLPath="FXMLSearchCustomer.fxml";
    public FXMLSearchCustomerController() {
        FXMLPath = "FXMLSearchCustomer.fxml";
    }

    @Override
    public FXMLParentController load() {
        controller = (FXMLSearchCustomerController) super.load();

        controller.getStage().setTitle(SEARCH_TITLE);
        return controller;
    }

    // Is the search being used to pick someone and return the value?
    // If so, double click = select instead of edit
    // Right click = "select" rather than "view"/"delete"
    // Allow "Add new" button either way
    private boolean pickAndReturn = false;

    public boolean isPickAndReturn() {
        return pickAndReturn;
    }

    public void setPickAndReturn(boolean pickAndReturn) {
        this.pickAndReturn = pickAndReturn;
        if (pickAndReturn) {
            MenuItem select = new MenuItem("Select");
            select.setOnAction(e -> {
                setSelected();
                stage.hide();
            });
            contextMenu.getItems().add(0, select);
            getStage().setTitle(SELECT_TITLE);
            // Don't allow to add new if we're picking and returning! So much easier...

        }
    }

    //Who are we selecting for, in the case if pickAndReturn? Use their kids and address
    private CustomerTO selectFor = null;

    public CustomerTO getSelectFor() {
        return selectFor;
    }

    public void setSelectFor(CustomerTO selectFor) {
        this.selectFor = selectFor;
    }

    //Are we finding for an event?
    /*   public void showForEvent(EventTO anEvent) {
        if (anEvent == null) {
            return;
        }
        addNewButton.setDisable(true);
        forEvent = anEvent;

        stage.setTitle(EVENT_SEARCH_TITLE + anEvent.getEventTitle());
        eventAttendeesList = SoapHandler.getCustomersForEvent(anEvent.getId());
        eventCustomerRowItemList = FXCollections.observableArrayList();
        for (CustomerTO eachCustomer : eventAttendeesList) {
            eventCustomerRowItemList.add(new SearchRowItem(eachCustomer));
        }
        filteredRowItemList = eventCustomerRowItemList;
        searchResultsTable.setItems(eventCustomerRowItemList);
        attendeesOnlyCheckbox.setSelected(true);
        attendeesOnlyCheckbox.setVisible(true);
        if (forEvent != null) {
            MenuItem addToEvent = new MenuItem("Add to event");
            contextMenu.getItems().add(0, addToEvent);
            addToEvent.setOnAction(new handleAddToEvent());
        }
        // Customers in event list will be different objects to main list, though will be equal()
        loadAllCustomers();
    }*/
    private CustomerTO selected = null;

    public CustomerTO getSelected() {
        return selected;
    }

    private static final String MATCHANY = "Match any";
    private static final String FORENAME = "Forename";
    private static final String SURNAME = "Surname";
    private static final String CHILD = "Child";
    private static final String CARREG = "Car reg";
    private static final String SOCIETY = "Society";

    private static final String[] menuItems = {MATCHANY, SURNAME, FORENAME, CHILD, CARREG, SOCIETY};

    private static final String SHOWONLY = "Show only";
    private static final String SHOWALL = "All";
    private static final String EXPIRED = "Expired";
    private static final String MEMBERSONLY = "Members";
    private static final String NONEMEMBERS = "Non-members";
    private static final String REFUSED = "Refuse entry";
    private static final String NORTHERN = "Northern";
    private static final String SOUTHERN = "Southern";
    private static final String BROMLEY = "Bromley";
    private static final String KENT = "Kent";
    private static final String ELECTRIC_HOOKUP = "Members with electricity";
    private static final String NO_ELECTRIC_HOOKUP = "Members with pitch, no elec";
    private static final String[] filterItems = {SHOWONLY, SHOWALL, EXPIRED, MEMBERSONLY, NONEMEMBERS, REFUSED, NORTHERN, SOUTHERN, BROMLEY, KENT};

    @FXML
    private TextField searchField;
    @FXML
    private ChoiceBox<String> searchSelection;
    @FXML
    private TableView<SearchRowItem> searchResultsTable;
    @FXML
    private TableColumn<SearchRowItem, String> nameColumn;
    @FXML
    private TableColumn<SearchRowItem, String> surnameColumn;
    @FXML
    private TableColumn<SearchRowItem, String> partnerNameColumn;
    @FXML
    private TableColumn<SearchRowItem, String> carRegColumn;
    @FXML
    private TableColumn<SearchRowItem, String> childrenColumn;
    @FXML
    private TableColumn<SearchRowItem, String> memberNoColumn;
    @FXML
    private TableColumn<SearchRowItem, String> societyColumn;
    @FXML
    private TableColumn<SearchRowItem, String> idValidColumn;

    public ObservableList<SearchRowItem> getFilteredList() {
        return filteredRowItemList;
    }

    public void setFilteredList(ObservableList<SearchRowItem> filteredList) {
        this.filteredRowItemList = filteredList;
        searchResultsTable.setItems(filteredList);
    }

    public List<CustomerTO> getFullCustomerList() {
        return fullCustomerList;
    }

    public void setFullCustomerList(List<CustomerTO> fullCustomerList) {
        this.fullCustomerList = fullCustomerList;
    }

    public TableView<SearchRowItem> getSearchResultsTable() {
        return searchResultsTable;
    }

    /*public void setSearchResultsTable(TableView<SearchRowItem> searchResultsTable) {
        this.searchResultsTable = searchResultsTable;
    }*/
    private void setSelected() {
        // Used to show who we're selecting another customer for, handy for pre filling details
        // of partners etc if we click "Add new"
        selected = searchResultsTable.getSelectionModel().getSelectedItem();
        // Make sure we persist the address and return the ID for it, to ensure partners can share
        if (selected.getAddressId() == null || selected.getAddressId().getId() == null || selected.getAddressId().getId() == 0) {
            Integer addressId = SoapHandler.saveAddress(selected.getAddressId());
            selected.getAddressId().setId(addressId);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialise context menu for tables
        contextMenu = new ContextMenu();

        MenuItem delete = new MenuItem("Delete");
        MenuItem view = new MenuItem("View/edit");
        MenuItem emailMenu = new MenuItem("Send email");

        contextMenu.getItems().addAll(view, delete, emailMenu);
        delete.setOnAction(new handleDelete());
        view.setOnAction(new handleView());
        emailMenu.setOnAction(v -> handleSendEmailSingle());

        // Set up car table
        searchResultsTable.setContextMenu(contextMenu);
        // Set search options and select first
        searchSelection.getItems().addAll(FXCollections.observableArrayList(menuItems));
        searchSelection.getSelectionModel().select(0);
        searchSelection.getSelectionModel().selectedIndexProperty().addListener(
                (ov, oldv, newv) -> {
                    setSearchItems(newv);
                }
        );
        filterSelection.getItems().addAll(FXCollections.observableArrayList(filterItems));
        filterSelection.getSelectionModel().select(0);
        filterSelection.getSelectionModel().selectedIndexProperty().addListener(
                (ov, oldv, newv) -> {
                    setFilterItems(newv);
                }
        );

        nameColumn.setCellValueFactory(t -> t.getValue().getNameProperty());
        surnameColumn.setCellValueFactory(t -> t.getValue().getSurnameProperty());
        partnerNameColumn.setCellValueFactory(t -> t.getValue().getPartnerProperty());
        carRegColumn.setCellValueFactory(t -> t.getValue().getCarRegProperty());
        childrenColumn.setCellValueFactory(t -> t.getValue().getChildrenProperty());
        memberNoColumn.setCellValueFactory(cellData -> {

            StringProperty value = cellData.getValue().getMemberNoProperty();
            if (cellData.getValue().getRefuse() != null && cellData.getValue().getRefuse().getDate() != null) {
                value.setValue("refuse entry");
            }
            if (value.getValue() == null) {
                value.setValue("n/a");
            }
            return value;
        });

        // Comparator to ensure "Sort by member joined date" sorts by date, not String
        Comparator comparator = new Comparator<String>() {

            public int compare(String a, String b) {
                Date startDate;
                Date endDate;
                try {
                    startDate = df.parse(a);
                    endDate = df.parse(b);
                    return startDate.compareTo(endDate);
                } catch (Exception e) {
                    return (a.compareTo(b));
                }
            }
        };
        memberNoColumn.setComparator(comparator);

        societyColumn.setCellValueFactory(t -> t.getValue().getSocietyProperty());
        idValidColumn.setCellValueFactory(cellData -> {
            return Utility.checkValidID(cellData.getValue().getImageCollection());
        });

        // Make sure everything scales when we resize window
        //mainAnchorPane.prefHeightProperty().bind(scene.heightProperty());
        //mainAnchorPane.prefWidthProperty().bind(scene.widthProperty());
        mainVBox.prefWidthProperty().bind(mainAnchorPane.widthProperty());
        mainVBox.prefHeightProperty().bind(mainAnchorPane.heightProperty());

        subAnchorPane.prefWidthProperty().bind(mainAnchorPane.widthProperty());
        subAnchorPane.prefHeightProperty().bind(mainAnchorPane.heightProperty());
        tableAnchorPane.prefWidthProperty().bind(subAnchorPane.widthProperty());
        tableAnchorPane.prefHeightProperty().bind(subAnchorPane.heightProperty());

        filteredRowItemList = FXCollections.observableArrayList();
        searchResultsTable.setItems(filteredRowItemList);
        customerCountStatus.setText(Integer.toString(filteredRowItemList.size()));

        customersDisplayedHBox.setStyle("-fx-background-color: white");
        queryHBox.setStyle("-fx-background-color: white ");
        setupMenu();

    }

    private void setupMenu() {
        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(mainAnchorPane.widthProperty());

        Menu menuFile = new Menu("File");
        MenuItem menuPrintList = new MenuItem("Print list");
        menuPrintList.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
        menuPrintList.setOnAction(v -> {
            printList();
        });
        MenuItem menuPrintRecords = new MenuItem("Print all records");
        menuPrintRecords.setOnAction(v -> {
            printAll();
        });
        MenuItem menuExportRecords = new MenuItem("Export as CSV");
        menuExportRecords.setOnAction(v -> {
            handleExportButton(null);
        });
        MenuItem menuExportInsuranceRecords = new MenuItem("Export as CSV (Insurance)");
        menuExportInsuranceRecords.setOnAction(v -> {
            handleExportInsuranceButton(null);
        });
        MenuItem menuCustomExport = new MenuItem("Export - select fields");
        menuCustomExport.setOnAction(v
                -> {
            handleCustomExportButton(null);
        }
        );
        MenuItem menuTableView = new MenuItem("View in table");
        menuTableView.setOnAction(v -> {
            tableView();
        });
        MenuItem menuRefresh = new MenuItem("Reload & Reset");
        menuRefresh.setAccelerator(KeyCombination.keyCombination("Ctrl+F5"));
        menuRefresh.setOnAction(v -> handleRefreshButton(null));
        Menu menuEmail = new Menu("Email");
        MenuItem menuEmailAll = new MenuItem("Email filtered");
        MenuItem menuEmailSelected = new MenuItem("Email selected");
        menuEmailAll.setOnAction(v -> {
            handleSendEmailList();
        });
        menuEmailSelected.setOnAction(v -> {
            handleSendEmailSingle();
        });
        menuEmail.getItems().addAll(menuEmailAll, menuEmailSelected);
        MenuItem menuExit = new MenuItem("Exit");
        menuExit.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        menuExit.setOnAction(v -> {
            System.exit(0);
        });

        Menu menuQuery = new Menu("Query");
        MenuItem menuQueryView = new MenuItem("View");
        menuQueryView.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        menuQueryView.setOnAction(v -> {
            handleQueryButton(null);
        });
        MenuItem menuQueryClear = new MenuItem("Clear");
        menuQueryClear.setOnAction(v -> {
            if (queryFilter != null) {
                filterList.remove(queryFilter);
                queryFilter = null;
            }
            queryController = null;
            queryStatus.setText("None");
            applyFilters();
        });
        MenuItem menuQueryFindMissingDocuments = new MenuItem("Find missing swim or sauna");
        menuQueryFindMissingDocuments.setOnAction(v -> queryBuilderMissingSwimOrSauna());
        MenuItem menuQueryBirthday = new MenuItem("Birthday today");
        menuQueryBirthday.setOnAction(v -> queryBuilderBirthday());
        MenuItem idWillExpire = new MenuItem("IDs set to expire within next week");
        idWillExpire.setOnAction(v -> queryBuilderIDExpiring());
        MenuItem customersMissingEmail = new MenuItem("Customers missing email address");
        customersMissingEmail.setOnAction(v -> queryBuilderMissingEmail());
        //   Menu menuEvents = new Menu("Events");
        MenuItem customersWithHookup = new MenuItem("Pitch members with hookup");
        customersWithHookup.setOnAction(v -> queryBuilderElectricHookupTemplate());
        MenuItem pitchMemberNoHookup = new MenuItem("Pitch members without hookup");
        pitchMemberNoHookup.setOnAction(v -> queryBuilderPitchNoElectricHookupTemplate());
        MenuItem unpaidInvoices = new MenuItem("Outstanding invoices");
        unpaidInvoices.setOnAction(v -> queryBuilderOutstandingInvoices());
        MenuItem vanMembers = new MenuItem("Van members");
        vanMembers.setOnAction(v -> queryBuilderVanMembers());
        MenuItem vanMembersNoInsurance = new MenuItem("Van members (no insurance)");
        vanMembersNoInsurance.setOnAction(v -> queryBuilderVanMembersNotInsured());
        MenuItem membersWithInsurance = new MenuItem("Members with insurance");
        membersWithInsurance.setOnAction(v -> queryBuilderMembersInsured());

        /*     Menu menuEvents = new Menu("Events");
        MenuItem menuEventsView = new MenuItem("View");
        menuEventsView.setOnAction(v -> {
            showEvents();
        });*/
        Menu menuTools = new Menu("Tools");
        MenuItem menuCreateInvoice = new MenuItem("Create invoices");
        menuCreateInvoice.setOnAction(v -> createInvoices());

        menuFile.getItems().addAll(
                menuPrintList,
                menuPrintRecords,
                menuExportRecords,
                menuExportInsuranceRecords,
                menuCustomExport,
                menuTableView,
                new SeparatorMenuItem(),
                menuRefresh,
                new SeparatorMenuItem(),
                menuEmail,
                new SeparatorMenuItem(),
                menuExit);
        menuQuery.getItems().addAll(
                menuQueryView,
                menuQueryClear,
                new SeparatorMenuItem(),
                unpaidInvoices,
                customersWithHookup,
                pitchMemberNoHookup,
                menuQueryFindMissingDocuments,
                menuQueryBirthday,
                idWillExpire,
                customersMissingEmail,
                vanMembers,
                vanMembersNoInsurance,
                membersWithInsurance
        );
        menuTools.getItems().addAll(menuCreateInvoice);
        //       menuEvents.getItems().addAll(
        //               menuEventsView
        //       );

        menuBar.getMenus().addAll(menuFile, menuQuery, menuTools);

        mainAnchorPane.getChildren().add(menuBar);
    }

    private Task loadAllCustomers() {
        // Get list of all customers from server - reduces network access to one call
        fullCustomerRowItemList = FXCollections.observableArrayList();
        Task task = new Task() {
            protected Integer call() throws Exception {

                try {
                    long start = System.currentTimeMillis();
                    fullCustomerList = SoapHandler.getDisplayCustomers();
                    System.out.println("Request took " + ((System.currentTimeMillis() - start)) + " milliseconds");
                    //  if (fullCustomerList == null || fullCustomerList.size() == 0) {
                    // No customers! - set place holder for blank searches as well...
                    Platform.runLater(() -> {
                        Node node = new Label("Processing customers");
                        searchResultsTable.setPlaceholder(node);
                    });

                    Collections.sort(fullCustomerList, (a, b) -> {
                        int d = a.getSurname().compareTo(b.getSurname());
                        if (d == 0) {
                            d = a.getForename().compareTo(b.getForename());
                        }
                        return d;
                    });
                    customerSet = new HashMap();

                    Iterator<CustomerTO> i = fullCustomerList.iterator();
                    while (i.hasNext()) {
                        CustomerTO eachCustomer = i.next();
                        SearchRowItem eachRow = new SearchRowItem(eachCustomer);
                        fullCustomerRowItemList.add(eachRow);
                        // populate customerSet Map, allows easy indexing as Soap TO objects don't override equals
                        customerSet.put(eachRow.getId(), eachRow);
                    }

                    // Now we can easily pull partner details
                    new Thread() {
                        public void run() {
                            for (SearchRowItem eachCustomer : fullCustomerRowItemList) {
                                if (eachCustomer.getPartnerId() != null && eachCustomer.getPartnerId() != 0) {
                                    CustomerTO partner = customerSet.get(eachCustomer.getPartnerId());
                                    eachCustomer.setPartnerProperty(new SimpleStringProperty(partner.getForename()));
                                }
                            }
                        }
                    }.start();
                    //filteredList = searchRowItemList = fullCustomerRowItemList;
                    filteredRowItemList = fullCustomerRowItemList;

                    //if (fullCustomerList == null || fullCustomerList.size() == 0) {
                    // No customers! - set place holder for blank searches as well...
                    Platform.runLater(() -> {
                        Node node = new Label("No customers found");
                        searchResultsTable.setPlaceholder(node);
                        customerCountStatus.setText(Integer.toString(filteredRowItemList.size()));
                    });
                    //}
                } catch (Exception e) {
                    // If no connection to server, we'll get a ConnectException
                    Platform.runLater(() -> {
                        Node node = new Label("Cannot contact server");
                        searchResultsTable.setPlaceholder(node);
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Error contacting server");
                        alert.setContentText("Cannot connect to the database server. \nPlease ensure the server is running and \ntry again.");
                        alert.show();
                        System.err.println("Error contacting server : " + e.getMessage());
                    });

                }
                //
                return 0;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

        return task;
    }

    public void loadCustomers() {
        //searchResultsTable.setItems(fullCustomerRowItemList);       
        Node node = new Label("Loading customers");
        searchResultsTable.setPlaceholder(node);
        Task task = loadAllCustomers();
        filteredRowItemList = fullCustomerRowItemList;

        task.setOnSucceeded(value -> {
            Platform.runLater(() -> {
                searchResultsTable.setItems(fullCustomerRowItemList);
                applyFilters();
            });
        });
    }

    private void setFilterItems(Number index) {
        String filterSelected;
        if (index == null) {
            filterSelected = filterSelection.getSelectionModel().getSelectedItem();

        } else {
            filterSelected = filterItems[index.intValue()];
        }

        // Don't apply filtering in these cases
        if (SHOWONLY.equals(filterSelected) || SHOWALL.equals(filterSelected)) {
            //filteredList = fullCustomerRowItemList;
            //searchResultsTable.setItems(filteredRowItemList);
            if (showOnlyFilter != null) {
                filterList.remove(showOnlyFilter);
                showOnlyFilter = null;
            }
            applyFilters();
            return;
        }

        // Create filtered list
        // TODO : fix to use filterList
        if (showOnlyFilter == null) {
            showOnlyFilter = new CustomerListFilter();
            filterList.add(showOnlyFilter);
        } else {
            showOnlyFilter.getFilterList().clear();
        }

        if (EXPIRED.equals(filterSelected)) {
            showOnlyFilter.addFilter(a -> {
                int days = Utility.getIDDaysLeft(a.getImageCollection());
                return days <= 0;
            });
        } else if (MEMBERSONLY.equals(filterSelected)) {
            showOnlyFilter.addFilter(a -> {
                return (a.getMembership() != null
                        && a.getMembership().getMembershipNo() != null);
            });
        } else if (NONEMEMBERS.equals(filterSelected)) {
            showOnlyFilter.addFilter(a -> {
                return (a.getMembership() == null || a.getMembership().getMembershipNo() == null);
            });
        } else if (REFUSED.equals(filterSelected)) {
            showOnlyFilter.addFilter(a -> {
                return (a.getRefuse() != null && a.getRefuse().getDate() != null);
            });
        } else {
            // Searching for one of societies, all that is left
            showOnlyFilter.addFilter(a -> {
                return (a.getMembership() != null && filterSelected.equals(a.getMembership().getSociety()));
            });
        }
        applyFilters();
    }

    private void showEvents() {
        FXMLEventListController newController = new FXMLEventListController();

        newController = (FXMLEventListController) newController.load();
        newController.getStage().show();
    }

    private void createInvoices() {
        FXMLCreateInvoicesController newController = new FXMLCreateInvoicesController();

        newController = (FXMLCreateInvoicesController) newController.load();
        newController.setCustomerList(fullCustomerRowItemList);
        newController.getStage().show();
    }

    private void applyFilters() {
        // Apply the filters to the full list to get our filtered list for display
        Task task = new Task() {
            @Override
            public Object call() {
                long start = System.currentTimeMillis();
                System.out.println("Applying filters! " + filterList.size());
                filteredRowItemList = fullCustomerRowItemList;
                for (CustomerListFilter eachFilter : filterList) {
                    eachFilter.setCustomerList(filteredRowItemList);
                    filteredRowItemList = eachFilter.run();
                }
                System.out.println("Filtering took : " + (System.currentTimeMillis() - start) + " ms");
                searchResultsTable.setItems(filteredRowItemList);
                customerCountStatus.setText(Integer.toString(filteredRowItemList.size()));
                return null;
            }
        };
        task.run();
    }

    private void setSearchItems(Number index) {
        // We either arrive from
        // user typing into the search box, where getSelectedItem() is valid, or user changing
        // search value, where that handler receives newly selected item as Number, though
        // getSelectedItem() hasn't been updated yet
        String searchSelected;
        if (index == null) {
            searchSelected = searchSelection.getSelectionModel().getSelectedItem();
        } else {
            searchSelected = menuItems[index.intValue()];
        }

        final String searchText = searchField.getText().trim().toUpperCase();
        if (searchText == null || "".equals(searchText)) {
            // Search field is blank, remove filters if active
            if (searchFilter != null) {
                filterList.remove(searchFilter);
                searchFilter = null;
            }
            applyFilters();
            return;
        }

        boolean matchAny = MATCHANY.equals(searchSelected);

        if (searchFilter == null) {
            searchFilter = new CustomerListFilter();
            searchFilter.setOr(true);
            filterList.add(searchFilter);
        } else {
            searchFilter.getFilterList().clear();
        }
        if ((matchAny || SURNAME.equals(searchSelected))) {
            searchFilter.addFilter(a -> {
                String aSurname = a.getSurname().toUpperCase();
                return (aSurname.contains(searchText));
            });
        }
        if ((matchAny || FORENAME.equals(searchSelected))) {
            searchFilter.addFilter(a -> {
                String aForename = a.getForename().toUpperCase();
                return (aForename.contains(searchText));
            });
        }
        if (matchAny || CHILD.equals(searchSelected)) {
            searchFilter.addFilter(a -> {
                String aChildname = a.getChildrenProperty().getValue().toUpperCase();
                return (aChildname.contains(searchText));
            });
        }
        if ((matchAny || CARREG.equals(searchSelected))) {
            searchFilter.addFilter(a -> {
                String aCars = a.getCarRegProperty().getValue().toUpperCase();
                return (aCars.contains(searchText));
            });
        }
        // No filter to apply?
        if (searchFilter.getFilterList().size() == 0) {
            filterList.remove(searchFilter);
            searchFilter = null;
        }
        applyFilters();
    }

    @FXML
    private void handleSearchKeyReleased(KeyEvent event) {
        setSearchItems(null);
    }

    @FXML
    private void handleAddNewButton(ActionEvent event) {
        FXMLCustomerController newController = new FXMLCustomerController();

        newController = (FXMLCustomerController) newController.load();
        SearchRowItem aCustomer = new SearchRowItem(Utility.createCustomer());
        // Is this a case of "PickAndSelect"? If so, we  might be adding a partner...
        //       System.err.println("addressId = "+selectFor.getAddressId());
        if (pickAndReturn && selectFor != null) {
            // If address isn't persisted, persist now to be able to share between two custoemrs
            if (selectFor.getAddressId().getId() == null || selectFor.getAddressId().getId() == 0) {
                selectFor.getAddressId().setId(SoapHandler.saveAddress(selectFor.getAddressId()));
            }
            aCustomer.setAddressId(selectFor.getAddressId());
            aCustomer.getChildCollection().addAll(selectFor.getChildCollection());
        }
        newController.setCustomerDetails(aCustomer);
        newController.getStage().showAndWait();

        if (newController.isUpdated()) {
            fullCustomerList.add(aCustomer);
            filteredRowItemList.add(0, new SearchRowItem(aCustomer));
        }
    }

    @FXML
    private void handleMouseClickOnTable(MouseEvent event) {
        int clicks = event.getClickCount();
        MouseButton button = event.getButton();
        if (clicks < 2) {
            return;
        }
        if (!button.equals(MouseButton.PRIMARY)) {
            return;
        }
        if (pickAndReturn) {
            // Double click means select and return!
            setSelected();
            stage.hide();
        } else {
            // Double click means edit! Deal with as if user clicked view from context menu
            new handleView().handle(event);
        }
    }

    @FXML
    private void handleRefreshButton(ActionEvent event) {
        //searchRowItemList.clear();
        loadCustomers();
        searchResultsTable.refresh();
        searchSelection.getSelectionModel().select(0);
        filterSelection.getSelectionModel().select(0);
        searchField.setText("");
        // clear Query and Search filters
        if (queryFilter != null) {
            if (queryController != null) {
                queryController = null;
            }
            filterList.remove(queryFilter);
            queryFilter = null;
        }
        if (searchFilter != null) {
            filterList.remove(searchFilter);
            searchFilter = null;
        }
        applyFilters();
        // Note, we do not clear eventAttendee filter
    }

    @FXML
    private void handleKeyPressedOnTable(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (keyCode == KeyCode.DELETE) {
            new handleDelete().handle(null);
        }
    }

    @FXML
    private void handleAttendeesOnlyCheckbox(ActionEvent event) {
        // TODO : Not tested since updating to filterList 17/1/17
        if (attendeesOnlyCheckbox.isSelected()) {
            // Attendees only
            //searchRowItemList = eventCustomerRowItemList;
            //searchResultsTable.setItems(eventCustomerRowItemList);
            attendeesFilter = new CustomerListFilter();
            attendeesFilter.addFilter(a -> {
                if (eventCustomerRowItemList.contains(a)) {
                    return true;
                }
                return false;
            }
            );
            filterList.add(attendeesFilter);
        } else {
            // Show all
            //searchRowItemList = fullCustomerRowItemList;
            //searchResultsTable.setItems(fullCustomerRowItemList);
            filterList.remove(attendeesFilter);
            attendeesFilter = null;
        }

        applyFilters();
    }

    private void handleCustomExportButton(ActionEvent event) {
        FXMLDisplayFilterController controller = new FXMLDisplayFilterController();
        controller = (FXMLDisplayFilterController) controller.load();

        IDPreferences prefs = IDPreferences.getInstance();
        String keyString = SoapHandler.getUserId() + "customExport";
        byte[] byteArrayProperty = prefs.getByteArrayProperty(keyString);
        if (byteArrayProperty.length > 0) {
            DisplayFilter filter = new DisplayFilter();
            filter.fromByteArray(byteArrayProperty);
            controller.setDisplayFilter(filter);
        }

        controller.getStage().showAndWait();

        if (!controller.isDoneSelected()) {
            return;
        }

        prefs.setProperty(keyString, controller.getDisplayFilter().asByteArray());

        CustomerToTextFilter filter = new CustomerToTextFilter();
        String string = filter.toString(searchResultsTable.getItems(), controller.getDisplayFilter());

        FXMLCopySheetController copyController = new FXMLCopySheetController();
        copyController = (FXMLCopySheetController) copyController.load();
        copyController.setText(string);
        copyController.getStage().show();
    }

    private void handleExportButton(ActionEvent event) {
        FXMLCopySheetController controller = new FXMLCopySheetController();
        controller = (FXMLCopySheetController) controller.load();
        controller.setText(Utility.toString(searchResultsTable.getItems()));
        controller.getStage().show();
    }

    private void handleExportInsuranceButton(ActionEvent event) {
        FXMLCopySheetController controller = new FXMLCopySheetController();
        controller = (FXMLCopySheetController) controller.load();
        controller.setText(Utility.toStringNameAndInsurance(searchResultsTable.getItems()));
        controller.getStage().show();
    }

    private void tableView() {
        FXMLDisplayFilterController controller = new FXMLDisplayFilterController();
        controller = (FXMLDisplayFilterController) controller.load();

        IDPreferences prefs = IDPreferences.getInstance();
        String keyString = SoapHandler.getUserId() + "tableView";
        byte[] byteArrayProperty = prefs.getByteArrayProperty(keyString);
        if (byteArrayProperty.length > 0) {
            DisplayFilter filter = new DisplayFilter();
            filter.fromByteArray(byteArrayProperty);
            controller.setDisplayFilter(filter);
        }

        controller.getStage().showAndWait();

        if (!controller.isDoneSelected()) {
            return;
        }

        DisplayFilter displayFilter = controller.getDisplayFilter();
        prefs.setProperty(keyString, displayFilter.asByteArray());
        
        FXMLFilteredCustomerListViewController ncontroller = new FXMLFilteredCustomerListViewController();
        ncontroller = (FXMLFilteredCustomerListViewController) ncontroller.load();
        for (int i=0; i<DisplayFilter.fieldNames.length; i++) {
            if (displayFilter.getValue(DisplayFilter.fieldNames[i]))
                ncontroller.addColumn(DisplayFilter.fieldNames[i]);
        }
        ncontroller.setItems(searchResultsTable.getItems());
        ncontroller.getStage().show();
    }

    private void printList() {
        FXMLSearchCustomerController printController = new FXMLSearchCustomerController();
        printController = (FXMLSearchCustomerController) printController.load();
        printController.setFilteredList(filteredRowItemList);
        TableColumn[] tc = {nameColumn,
            surnameColumn,
            partnerNameColumn,
            carRegColumn,
            childrenColumn,
            memberNoColumn,
            societyColumn,
            idValidColumn};
        printController.copyWidths(mainAnchorPane, tc);
        printController.print();
    }

    private void handlePrint(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm printing action");
        alert.setHeaderText("Please confirm details to print");
        alert.setContentText("Do you require this list or\neach individual record printing?\n(total of " + filteredRowItemList.size() + " pages to print)");

        ButtonType buttonList = new ButtonType("List");
        ButtonType buttonIndividual = new ButtonType("Individual");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonList, buttonIndividual, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonList) {
            printList();
        } else if (result.get() == buttonIndividual) {
            printAll();
        }
    }

    public void backPrint() {

        Task t = new Task() {
            protected Integer call() {
                print();
                return 0;
            }
        };

        Thread th = new Thread(t);
        th.setDaemon(true);
        th.start();

    }

    public void copyWidths(AnchorPane ap, TableColumn cols[]) {
        mainAnchorPane.prefWidthProperty().bind(ap.widthProperty());
        nameColumn.prefWidthProperty().bind(cols[0].widthProperty());
        surnameColumn.prefWidthProperty().bind(cols[1].widthProperty());
        partnerNameColumn.prefWidthProperty().bind(cols[2].widthProperty());
        carRegColumn.prefWidthProperty().bind(cols[3].widthProperty());
        childrenColumn.prefWidthProperty().bind(cols[4].widthProperty());
        memberNoColumn.prefWidthProperty().bind(cols[5].widthProperty());
        societyColumn.prefWidthProperty().bind(cols[6].widthProperty());
        idValidColumn.prefWidthProperty().bind(cols[7].widthProperty());
    }

    public void print() {
        float tableRowHeight = 24;
        float tableHeaderHeight = 30;

        Printer printer = Printer.getDefaultPrinter();
        if (printer == null) {
            System.err.println("Error getting printer");
            return;
        }
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT); //create a pagelayout.  I used Paper.NA_LETTER for a standard 8.5 x 11 in page.
        Stage dialogStage = new Stage(StageStyle.DECORATED);
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if (job != null) {
            // Show stage to work out number of pages for dialog box
            stage.show();
            double pagePrintableWidth = pageLayout.getPrintableWidth(); //this should be 210mm for this page layout.
            double pagePrintableHeight = pageLayout.getPrintableHeight();// this should be 297mm inches for this page layout.

            System.out.println("ppw = " + pagePrintableWidth);
            System.out.println("pph = " + pagePrintableHeight);
            System.out.println("srt = " + searchResultsTable.getBoundsInParent().getWidth());

            // Fix row height and adjust tableView to show whole table
            searchResultsTable.setFixedCellSize(tableRowHeight);
            IntegerBinding tableHeight = Bindings.size(searchResultsTable.getItems()).multiply((int) tableRowHeight).add((int) tableHeaderHeight);
            searchResultsTable.prefHeightProperty().bind(tableHeight);// If your cells' rows are variable size you add the .multiply and play with the input value until your output is close to what you want. If your cells' rows are the same height, I think you can use .multiply(1). This changes the height of your tableView to show all rows in the table.
            searchResultsTable.minHeightProperty().bind(searchResultsTable.prefHeightProperty());//You can probably play with this to see if it's really needed.  Comment it out to find out.
            //searchResultsTable.maxHeightProperty().bind(searchResultsTable.prefHeightProperty());//You can probably play with this to see if it' really needed.  Comment it out to find out.

            // Calculate scaling
            double scaleX = pagePrintableWidth / searchResultsTable.getBoundsInParent().getWidth();//scaling down so that the printing width fits within the paper's width bound.
            double scaleY = scaleX; //scaling the height using the same scale as the width. This allows the writing and the images to maintain their scale, or not look skewed.
            double localScale = scaleX; //not really needed since everything is scaled down at the same ratio. scaleX is used thoughout the program to scale the print out.

            System.out.println("scale = " + localScale);

            double numberOfPages = Math.ceil((searchResultsTable.getPrefHeight() * localScale) / pagePrintableHeight);//used to figure out the number of pages that will be printed.
            if (numberOfPages > 1) {
                PageRange pageRange[] = new PageRange[1];
                pageRange[0] = new PageRange(1, (int) numberOfPages);
                job.getJobSettings().setPageRanges(pageRange);
            }

            stage.hide();
            boolean showDialog = job.showPrintDialog(dialogStage);

            if (showDialog) {
                // Refetch width and height, in case they've changed
                pagePrintableWidth = pageLayout.getPrintableWidth(); //this should be 210mm for this page layout.
                pagePrintableHeight = pageLayout.getPrintableHeight();// this should be 297mm inches for this page layout.
                // Calculate scaling
                scaleX = pagePrintableWidth / searchResultsTable.getBoundsInParent().getWidth();//scaling down so that the printing width fits within the paper's width bound.
                scaleY = scaleX; //scaling the height using the same scale as the width. This allows the writing and the images to maintain their scale, or not look skewed.
                localScale = scaleX; //not really needed since everything is scaled down at the same ratio. scaleX is used thoughout the program to scale the print out.

                stage.show();
                ProgressIndicator pi = new ProgressIndicator();
                pi.show();

                Scale tableScale = new Scale(scaleX, scaleY);
                searchResultsTable.getTransforms().add(tableScale);//scales the printing. Allowing the width to say within the papers width, and scales the height to do away with skewed letters and images.
                Translate tableTranslate = new Translate(0, 0);
                searchResultsTable.getTransforms().add(tableTranslate);// starts the first print at the top left corner of the image that needs to be printed

                //Since the height of what needs to be printed is longer than the paper's heights we use gridTransfrom to only select the part to be printed for a given page.
                //Transforms and no. of rows relative to scaled table = print view
                double scaledTableRowHeight = (double) tableRowHeight * localScale;
                double scaledTableHeaderHeight = (double) tableHeaderHeight * localScale;
                Translate gridTransform = new Translate();
                searchResultsTable.getTransforms().add(gridTransform);
                int rowsOnFirstPage = (int) ((pagePrintableHeight - scaledTableHeaderHeight) / scaledTableRowHeight);
                int rowsOnPages = (int) (pagePrintableHeight / scaledTableRowHeight);

                //Clips all relative to tableView, no scaling applied
                double firstPageClip = rowsOnFirstPage * tableRowHeight + tableHeaderHeight;
                double pageClip = rowsOnPages * tableRowHeight;
                double dClip = firstPageClip - pageClip; // = should be diff between rows and header
                Rectangle clip = new Rectangle(pagePrintableWidth / localScale, firstPageClip);
                searchResultsTable.setClip(clip);
                clip.setLayoutY(0);
                System.out.println("rowsOnFirstPage = " + rowsOnFirstPage);
                System.out.println("rowsOnPages = " + rowsOnPages + ", " + (pagePrintableHeight / scaledTableRowHeight));
                System.out.println("dClip = " + dClip);
                System.out.println("pageOneClip = " + firstPageClip);
                System.out.println("pageClip = " + pageClip);

                double scaledDClip = dClip / localScale;
                double scaledPageClip = pageClip / localScale;
                System.out.println("scaledPageClip = " + scaledPageClip);

                //now we loop though the image that needs to be printed and we only print a subimage of the full image.
                //for example: In the first loop we only pint the printable image from the top down to the height of a standard piece of paper. Then we print starting from were the last printed page ended down to the height of the next page. This happens until all of the pages are printed. 
                // first page prints from 0 height to -11 inches height, Second page prints from -11 inches height to -22 inches height, etc. 
                ProgressBar pb = pi.getPb();
                System.out.println("Printing page : " + 1);
                pb.setProgress(1 / numberOfPages);
                job.printPage(pageLayout, searchResultsTable);

                clip.setHeight(pageClip + 1);
                for (int i = 1; i < numberOfPages; i++) {
                    //gridTransform.setY(-i * (pagePrintableHeight / localScale));/
                    gridTransform.setY(((-i * pageClip) - dClip));
                    //clip.setLayoutY(i * (pagePrintableHeight / localScale));
                    clip.setLayoutY(i * pageClip);
                    job.printPage(pageLayout, searchResultsTable);
                    System.out.println("Printing page : " + i);
                    pb.setProgress(i / numberOfPages);
                    System.out.println("transform = " + (-i * pageClip));
                }

                job.endJob();//finally end the printing job.
                pi.hide();
                stage.hide();
            }
        } else {
            System.err.println("Error printing");
        }
    }

    private void printAll() {
        // Print each customers details
        int numberToPrint = searchResultsTable.getItems().size();

        FXMLCustomerController printController = new FXMLCustomerController();
        printController = (FXMLCustomerController) printController.load();
        printController.setUpPrint(numberToPrint);

        if (printController.okToPrint()) {
            System.out.println("Printing");
            printController.getStage().show();
            for (SearchRowItem eachItem : searchResultsTable.getItems()) {
                printController.setCustomerDetails(eachItem);
                printController.print();
                System.out.println("Printed : " + eachItem.toString());
            }
            printController.endPrint();
            //printController.getStage().hide();
        }
    }

    @FXML
    private void handleQueryButton(ActionEvent event) {
        if (queryController == null) {
            queryController = new FXMLQueryController();
            queryController = queryController.load();
        }
        //TODO : handle cancel button correctly - currently clears
        queryController.getStage().showAndWait();
        if (queryController.isUpdated()) {
            if (queryFilter != null) {
                filterList.remove(queryFilter);
            }
            queryFilter = queryController.getCustomerListFilter();
            filterList.add(queryFilter);
        } else {
            // Currently wrong! Cancelled so remove query?
            filterList.remove(queryFilter);
            queryFilter = null;
        }
        applyFilters();
        queryStatus.setText(queryFilter == null ? "None" : queryController.getQueryString());
    }

    private void queryBuilderMissingSwimOrSauna() {
        if (queryController == null) {
            queryController = new FXMLQueryController();
            queryController = queryController.load();
        }
        queryController.setMissingSwimOrSauna();
        filterList.remove(queryFilter);
        queryFilter = queryController.getCustomerListFilter();
        filterList.add(queryFilter);
        queryStatus.setText("Displaying customers missing Swim or Sauna agreements");
        applyFilters();
    }

    private void queryBuilderBirthday() {
        filterList.remove(queryFilter);
        queryFilter = new CustomerListFilter();
        queryFilter.addFilter(a -> {
            Date dob = MyDate.toDate(a.getDob());
            if (dob == null) {
                return false;
            }
            int day = dob.getDate();
            int month = dob.getMonth();
            Date today = new Date();
            return (day == today.getDate() && month == today.getMonth());
        });
        filterList.add(queryFilter);
        queryStatus.setText("Displaying customers with birthday today");
        applyFilters();
    }

    private void queryBuilderTemplate() {
        filterList.remove(queryFilter);
        queryFilter = new CustomerListFilter();
        queryFilter.addFilter(a -> {
            return true;
        });
        filterList.add(queryFilter);
        queryStatus.setText("");
        applyFilters();
    }

    private void queryBuilderOutstandingInvoices() {
        filterList.remove(queryFilter);
        queryFilter = new CustomerListFilter();
        queryFilter.addFilter(new CustomerFilter() {
            @Override
            public boolean include(SearchRowItem a) {
                if (a.getMembership() == null) {
                    return false;
                }
                if (a.getMembership().getElectricitychargeCollection() == null) {
                    return false;
                }
                if (!a.getMembership().getElectricitychargeCollection().isEmpty()) {
                    boolean flag = false;
                    for (ElectricitychargeTO action : a.getMembership().getElectricitychargeCollection()) {
                        //    a.getMembership().getElectricitychargeCollection().stream().forEach((ElectricitychargeTO action) -> {
                        if (action.getInvoiceList() != null && action.getInvoiceList().size() > 0) {
                            // Only check for receipt of fist invoice for now
                            if (action.getInvoiceList().get(0).getReceiptCollection() == null || action.getInvoiceList().get(0).getReceiptCollection().size() == 0) {
                                flag = true;
                            }
                        }
                    }
                    return flag;

                }
                return false;
            }
        });
        filterList.add(queryFilter);
        queryStatus.setText("Outstanding invoices");
        applyFilters();
    }

    private void queryBuilderElectricHookupTemplate() {
        filterList.remove(queryFilter);
        queryFilter = new CustomerListFilter();
        queryFilter.addFilter(a -> {
            return (a.getMembership() != null
                    && (a.getMembership().isElectricityHookup() != null && a.getMembership().isElectricityHookup()));
        });
        filterList.add(queryFilter);
        queryStatus.setText("Pitch members with hookup");
        applyFilters();
    }

    private void queryBuilderPitchNoElectricHookupTemplate() {
        filterList.remove(queryFilter);
        queryFilter = new CustomerListFilter();
        queryFilter.addFilter(a -> {
            return ((a.getMembership() != null)
                    && (a.getMembership().getType() != null && ((a.getMembership().getType().equals(LARGE_VAN_PITCH) || a.getMembership().getType().equals(TENT_PITCH) || a.getMembership().getType().equals(VAN_PITCH))))
                    && (a.getMembership().isElectricityHookup() == null || !a.getMembership().isElectricityHookup()));
        });
        filterList.add(queryFilter);
        queryStatus.setText("Pitch members with no hookup");
        applyFilters();
    }

    private void queryBuilderVanMembers() {
        filterList.remove(queryFilter);
        queryFilter = new CustomerListFilter();
        queryFilter.addFilter(a -> {
            return ((a.getMembership() != null) && a.getMembership().getType() != null
                    && (a.getMembership().getType().equals(LARGE_VAN_PITCH) || a.getMembership().getType().equals(VAN_PITCH)));
        });
        filterList.add(queryFilter);
        queryStatus.setText("Van members");
        applyFilters();
    }

    private void queryBuilderVanMembersNotInsured() {
        filterList.remove(queryFilter);
        queryFilter = new CustomerListFilter();
        queryFilter.addFilter(a -> {
            return ((a.getMembership() != null) && a.getMembership().getType() != null
                    && (a.getMembership().getType().equals(LARGE_VAN_PITCH) || a.getMembership().getType().equals(VAN_PITCH))
                    && (a.getMembership().getInsuranceExpiry() == null
                    || a.getMembership().getInsuranceExpiry().compare(MyDate.toXMLGregorianCalendar(new Date())) == DatatypeConstants.LESSER));
        });
        filterList.add(queryFilter);
        queryStatus.setText("Van members with no insurance");
        applyFilters();
    }

    private void queryBuilderMembersInsured() {
        filterList.remove(queryFilter);
        queryFilter = new CustomerListFilter();
        queryFilter.addFilter(a -> {
            return ((a.getMembership() != null) && a.getMembership().getType() != null
                    && (a.getMembership().getInsuranceExpiry() != null));
        });
        filterList.add(queryFilter);
        queryStatus.setText("Members with insurance");
        applyFilters();
    }

    private void queryBuilderIDExpiring() {
        filterList.remove(queryFilter);
        queryFilter = new CustomerListFilter();
        queryFilter.addFilter(a -> {
            int days = Utility.getIDDaysLeft(a.getImageCollection());
            return (days > 0 && days < 8);
        });
        filterList.add(queryFilter);
        queryStatus.setText("Customers with ID expiring within 7 days");
        applyFilters();
    }

    private void queryBuilderMissingEmail() {
        filterList.remove(queryFilter);
        queryFilter = new CustomerListFilter();
        queryFilter.addFilter(a -> {
            String email = a.getEmail();
            return (email == null || email.length() <= 0);
        });
        filterList.add(queryFilter);
        queryStatus.setText("Customers with no Email address");
        applyFilters();

    }

    private class handleDelete implements EventHandler {

        // private class to handle delete menu or delete key press
        @Override
        public void handle(Event event) {
            ActionEvent myEvent = (ActionEvent) event;

            SearchRowItem whichItem;
            ObservableList<SearchRowItem> whichList;
            List<CustomerTO> theCustomerList = fullCustomerList;

            whichItem = searchResultsTable.getSelectionModel().getSelectedItem();
            whichList = filteredRowItemList;

            Optional<ButtonType> result = Utility.showConfirmationAlert("Please confirm",
                    "If you click OK, this action cannot be reversed",
                    whichItem + " will be removed. Are you sure?");

            if (result.get() == ButtonType.OK) {
                // ... user chose OK
                whichList.remove(whichItem);
                theCustomerList.remove(whichItem);
                SoapHandler.deleteCustomer(whichItem);
            }
        }

    }

    private class handleView implements EventHandler {

        // private class to handle delete menu or delete key press
        @Override
        public void handle(Event event) {
            FXMLCustomerController controller = new FXMLCustomerController();
            controller = (FXMLCustomerController) controller.load();

            SearchRowItem whichCustomer = searchResultsTable.getSelectionModel().getSelectedItem();
            controller.setCustomerDetails(whichCustomer);

            controller.getStage().showAndWait();
            if (controller.isUpdated()) {
                // refresh Customer against server
                //       CustomerTO updatedCustomer = SoapHandler.getCustomerByID(whichCustomer.getCustomerId());
                //       whichCustomer.setCustomerTO(updatedCustomer);
                // if there's a partner, set the name
                CustomerTO updatedCustomer = controller.getCustomer();
                Integer partnerId = whichCustomer.getPartnerId();
                if (partnerId != null && partnerId != 0) {
                    if (whichCustomer.getPartnerProperty() == null) {
                        whichCustomer.setPartnerProperty(new SimpleStringProperty());
                    }
                    SimpleStringProperty partnerName = (SimpleStringProperty) whichCustomer.getPartnerProperty();
                    SearchRowItem partner = customerSet.get(partnerId);
                    partnerName.setValue(partner.getForename());
                    if (partner.getPartnerProperty() != null) {
                        partner.getPartnerProperty().setValue(whichCustomer.getForename());
                    } else {
                        partner.setPartnerProperty(new SimpleStringProperty(whichCustomer.getForename()));
                    }
                } else if (whichCustomer.getPartnerProperty() != null) {
                    whichCustomer.getPartnerProperty().setValue("");
                }
                whichCustomer.setCustomer(updatedCustomer);
                whichCustomer.refresh(updatedCustomer);
                //  SearchRowItem customer = customerSet.get(whichCustomer.getCustomerId());
                //  customer.setCustomerTO(whichCustomer);

                searchResultsTable.refresh();
            }
        }
    }

    private class handleAddToEvent implements EventHandler {

        @Override
        public void handle(Event event) {
            /*       SearchRowItem selected = searchResultsTable.getSelectionModel().getSelectedItem();

            forEvent.setAttendees(forEvent.getAttendees() == null ? 1 : forEvent.getAttendees() + 1);
            System.out.println("Attendees = " + forEvent.getAttendees());

            CustomerIsAttendingEventTO attendance = new CustomerIsAttendingEventTO();
            attendance.setEventId(forEvent.getId());
            attendance.setCustomerId(selected.getCustomerId());
            attendance.setOrderDate(MyDate.toXMLGregorianCalendar(new Date(System.currentTimeMillis())));
            // TODO: Deal with pricing
            attendance.setEventPrice(forEvent.getEventPriceCollection().get(0));
            EventPriceTO evp = forEvent.getEventPriceCollection().get(0);
            System.out.println("Event price = " + evp.getId());
            InvoiceTO invoice = new InvoiceTO();
            invoice.setAmount(0);
            invoice.setNotes("Default, testing purposes only");
            attendance.setInvoicenumber(invoice);

            SoapHandler.saveCustomerIsAttendingEvent(attendance);

            eventCustomerRowItemList.add(selected);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Added to event");
            alert.setHeaderText(selected.getForename() + " is attending " + forEvent.getEventTitle());
            alert.setContentText("");
            alert.showAndWait();
             */
        }
    }

    private void handleSendEmailList() {
        FXMLEmailController emailController = new FXMLEmailController();
        emailController = (FXMLEmailController) emailController.load();
        emailController.setTo(filteredRowItemList);
        emailController.getStage().showAndWait();
        if (emailController.isUpdated()) {
            MailHandler mh = MailHandler.getInstance();
            mh.setSubject(emailController.getSubject());
            mh.setEmailBody(emailController.getBody());
            mh.setFrom(emailController.getFrom());
            mh.setRecipients(filteredRowItemList);
            if (mh.send()) {
                Utility.showAlert("Email send", "Email has been sent", "Email has been sent to the server");
            } else {
                Optional<ButtonType> showOkOrYesAlert = Utility.showOkOrYesAlert("Error occured", "An error was returned", "An error occurred sending the emails.\nClick Log to view the error report\nor OK to close.", "Log");
                System.out.println("Button type = " + showOkOrYesAlert.get());
                if (showOkOrYesAlert.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
                    FXMLCopySheetController controller = new FXMLCopySheetController();
                    controller = (FXMLCopySheetController) controller.load();
                    controller.setText(mh.getErrorMessage());
                    controller.getStage().showAndWait();
                }
            }

        }
    }

    private void handleSendEmailSingle() {
        FXMLEmailController emailController = new FXMLEmailController();
        emailController = (FXMLEmailController) emailController.load();
        if (searchResultsTable.getSelectionModel().getSelectedItem() == null) {
            Utility.showAlert("Please select a record", "Nothing selected", "Please select a record and try again");
            return;
        }
        String toEmail = searchResultsTable.getSelectionModel().getSelectedItem().getEmail();
        if (toEmail == null || toEmail.length() == 0) {
            Utility.showAlert("Problem with record", "EMail address unavailable",
                    searchResultsTable.getSelectionModel().getSelectedItem().getForename() + " has not got an email address set");
            return;
        }
        emailController.setTo(toEmail);
        emailController.getStage().showAndWait();
        if (emailController.isUpdated()) {
            MailHandler mh = MailHandler.getInstance();
            mh.setSubject(emailController.getSubject());
            mh.setEmailBody(emailController.getBody());
            mh.setFrom(emailController.getFrom());

            mh.setRecipient(toEmail);
            if (mh.send()) {
                Utility.showAlert("Email sent", "Email has been sent", "Email has been sent to the server");
            } else {
                Utility.showAlert("Error occured", "An error was returned", "An error occurred sending\nthe email\n\n" + mh.getErrorMessage());
            }
        }
    }
}
