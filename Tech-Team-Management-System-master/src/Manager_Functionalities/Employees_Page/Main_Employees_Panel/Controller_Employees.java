package Manager_Functionalities.Employees_Page.Main_Employees_Panel;


import Manager_Functionalities.Employees_Page.Edit_Employees_Panel.Controller_edit_Employee;
import Classes.Employee;
import Manager_Functionalities.Employees_Page.Edit_Employees_Panel.Controller_edit_Employee;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import static Login_Page.LoginPageController.manager;
public class Controller_Employees implements Initializable {

    @FXML
    private TextField search_field;

    @FXML
    private TableView<Employee> table_employees;

    @FXML
    private TableColumn<Employee, String> col_name;

    @FXML
    private TableColumn<Employee, String > col_position;

    @FXML
    private TableColumn<Employee, String> col_email;

    @FXML
    private TableColumn<Employee, String> col_birthdate;

    @FXML
    private TableColumn<Employee, String> col_phone;


    @FXML
    private Button edit;

    @FXML
    private Button delete;

    @FXML
    private Button reset_password;

    @FXML
    private Button performance;



    ObservableList<Employee> listM;
    ObservableList<Employee> dataList;

    public static Employee employee_selected;

    public void refresh(){
        updateTable();
    }


    public void updateTable(){
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_birthdate.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));


        listM = Employee.getDataEmployees();

        table_employees.setItems(listM);
        search_Name();
    }
    public void delete(){
        Alert alert =
                new Alert(Alert.AlertType.WARNING,
                        "Are you sure that you want to delete this Employee",
                        ButtonType.YES,
                        ButtonType.NO);
        alert.setTitle("Delete Employee warning");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get()==ButtonType.YES){
            manager.invokeDeleteEmployee(table_employees.getSelectionModel().getSelectedItem());
            updateTable();
        }
    }

    public void reset_Password(){
        Alert alert =
                new Alert(Alert.AlertType.WARNING,
                        "Are you sure that you want to reset this Employee's Password?",
                        ButtonType.YES,
                        ButtonType.NO);
        alert.setTitle("Reset Password warning");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get()==ButtonType.YES) {

            manager.resetEmployeePassword(table_employees.getSelectionModel().getSelectedItem());

        }
    }

    public void search_Name(){

        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_birthdate.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        dataList = Employee.getDataEmployees();
        table_employees.setItems(dataList);
        FilteredList<Employee> filteredData = new FilteredList<>(dataList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        search_field.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(Employee -> {
            // If filter text is empty, display all persons.

            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            // Compare first name and last name of every person with filter text.
            String lowerCaseFilter = newValue.toLowerCase();

            if (Employee.getName().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Filter matches first name.
            } else if (Employee.getPosition().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Filter matches last name.
            }
            else if (Employee.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Filter matches last name.
            }
            else if (String.valueOf(Employee.getBirthdate()).contains(lowerCaseFilter)){
                return true;
            }
            else if (Employee.getPhone().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Filter matches last name.
            }
            else {
                return false; // Does not match.
            }
        }));

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Employee> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(table_employees.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table_employees.setItems(sortedData);
    }

    @FXML
    public void open_add_employee_window() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Add_Employees_Panel/add_Employee.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Add New Employee");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void open_edit_employee_window() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Edit_Employees_Panel/edit_Employee.fxml"));
        Parent root = loader.load();
        Controller_edit_Employee controller = loader.getController();
        controller.setValues(table_employees.getSelectionModel().getSelectedItem());

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Edit Employee");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void showPerformanceStats() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager_Functionalities/Employees_Page/Performance_Stats/PerformanceStats.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Employee Performance");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }


    public void control_buttons(){
        edit.disableProperty().bind(Bindings.isEmpty(table_employees.getSelectionModel().getSelectedItems()));
        delete.disableProperty().bind(Bindings.isEmpty(table_employees.getSelectionModel().getSelectedItems()));
        reset_password.disableProperty().bind(Bindings.isEmpty(table_employees.getSelectionModel().getSelectedItems()));
    }

    @FXML
    void getSelected () {
        employee_selected = table_employees.getSelectionModel().getSelectedItem();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTable();
        search_Name();
        control_buttons();
    }
}
