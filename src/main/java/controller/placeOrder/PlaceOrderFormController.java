package controller.placeOrder;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import model.Item;
import model.Order;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlaceOrderFormController implements Initializable {

    public TableView tblItem;
    public JFXTextField TxtQtyWant;
    public JFXComboBox ComboBoxCustomerCode;
    public Label LblCustomerName;
    public Label TxtTotalPrice;
    @FXML
    private JFXComboBox ComboBoxItemCode;

    @FXML
    private JFXTextField TxtDescription;

    @FXML
    private JFXTextField TxtPacksize;

    @FXML
    private JFXTextField TxtQtyOnHand;

    @FXML
    private JFXTextField TxtUnitPrice;

    @FXML
    private TableColumn colDescription;

    @FXML
    private TableColumn colItemCode;

    @FXML
    private TableColumn colQTY;

    @FXML
    private TableColumn colTotal;

    @FXML
    private TableColumn colUnitPrice;

    private Double TotalPrice;

    @FXML
    void btnADDOnAction(ActionEvent event) {
        ObservableList items = tblItem.getItems();
        Double unitPrice = Double.parseDouble(TxtUnitPrice.getText());
        Integer qty = Integer.parseInt(TxtQtyWant.getText());
        Double total = unitPrice * qty;
        String itemCodeText = (String) ComboBoxItemCode.getValue();
        items.add(new Order(itemCodeText, TxtDescription.getText(), qty, unitPrice, total));
        TotalPrice = TotalPrice + total;
        tblItem.setItems(items);
        TxtTotalPrice.setText(TotalPrice+"");
    }

    @FXML
    void btnNewCustomerOnAction(ActionEvent event) {

        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../view/add_Customer_form.fxml"))));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TotalPrice=0d;
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQTY.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));


        ObservableList<Object> objects = FXCollections.observableArrayList();
        for (Item item : PlaceOrderController.getInstance().getAllItem()) {
            objects.add(item.getItemCode());
        }
        ComboBoxItemCode.setItems(objects);


        ComboBoxItemCode.valueProperty().addListener((obs, oldValue, newValue) -> {
            System.out.println("Selected Item: " + newValue);


            handleComboBoxSelectionItemCode((String) newValue);
        });
        ObservableList<Object> objects2 = FXCollections.observableArrayList();
        for (Customer customer : PlaceOrderController.getInstance().getAllCustomer()) {
            objects2.add(customer.getCustID());
        }
        ComboBoxCustomerCode.setItems(objects2);

        ComboBoxCustomerCode.valueProperty().addListener((obs, oldValue, newValue) -> {
            System.out.println("Selected Item: " + newValue);
            handleComboBoxSelectionCustomerCode((String) newValue);

        });
    }
    private void handleComboBoxSelectionCustomerCode(String selectedCustomerCode) {
        if (selectedCustomerCode != null) {

            for (Customer custoemrCode : PlaceOrderController.getInstance().getAllCustomer()) {
                if (custoemrCode.getCustID().equals(selectedCustomerCode)) {

                    LblCustomerName.setText(custoemrCode.getCustName());

                    break;
                }
            }
        } else {
            LblCustomerName.setText(null);

        }
    }

    private void handleComboBoxSelectionItemCode(String selectedItemCode) {
        if (selectedItemCode != null) {

            for (Item item : PlaceOrderController.getInstance().getAllItem()) {
                if (item.getItemCode().equals(selectedItemCode)) {

                    TxtDescription.setText(item.getDescription());
                    TxtPacksize.setText(item.getPackSize());
                    TxtQtyOnHand.setText(String.valueOf(item.getQtyOnHand()));
                    TxtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
                    break;
                }
            }
        } else {

            TxtDescription.clear();
            TxtPacksize.clear();
            TxtQtyOnHand.clear();
            TxtUnitPrice.clear();
        }
    }


    public void btnClearTablesOnAction(ActionEvent actionEvent) {
        tblItem.getItems().clear();
        TotalPrice=0d;

        TxtTotalPrice.setText(null);

    }
}