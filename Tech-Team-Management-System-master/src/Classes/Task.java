
package Classes;

import Exceptions.EmptyInputException;
import Exceptions.InvalidDateException;
import Exceptions.TaskAlreadyVerifiedException;
import MySQL.MySQL_Connector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;


public class Task {
    private  int task_id,employee_id;
    private  String task_name,task_description;
    private  String deadline_date;

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }

    public void setDeadline_date(String deadline_date) {
        this.deadline_date = deadline_date;
    }

    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    private String task_status;
    private int project_id;


    public Task(int task_id, int employee_id, int project_id,String task_name, String task_description,
                String deadline_date, String task_status) {
        this.project_id=project_id;
        this.task_id = task_id;
        this.employee_id = employee_id;
        this.task_name = task_name;
        this.task_description = task_description;
        this.deadline_date = deadline_date;
        this.task_status = task_status;
    }
    public Task(int employee_id, int project_id,String task_name, String task_description,
                String deadline_date, String task_status) {
        this.project_id=project_id;
        this.employee_id = employee_id;
        this.task_name = task_name;
        this.task_description = task_description;
        this.deadline_date = deadline_date;
        this.task_status = task_status;
    }
    public int getTask_id() {
        return task_id;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public String getTask_description() {
        return task_description;
    }

    public String getDeadline_date() {
        return deadline_date;
    }

    public String getTask_status() {
        return task_status;
    }



    private static final ObservableList<String> Tasks_Status = FXCollections.observableArrayList("Not Done","In Progress", "Done");

    public static ObservableList<String> getTasks_Status() {
        return Tasks_Status;
    }

    public static ObservableList<Task> getDataTasks(){
        Connection conn = MySQL_Connector.ConnectDB();
        ObservableList<Task> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = Objects.requireNonNull(conn).prepareStatement("SELECT * FROM Tasks");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                list.add(new Task(Integer.parseInt(rs.getString("task_id")),Integer.parseInt(rs.getString("employee_id")), Integer.parseInt(rs.getString("project_id")),rs.getString("task_name"),rs.getString("task_description"),rs.getString("deadline_date"),rs.getString("task_status")));

            }
        } catch (Exception e) {
            System.out.println("couldn't get tasks data");
        }
        return list;
    }



    public  void Add_Task()  {

        Connection conn = MySQL_Connector.ConnectDB();
        PreparedStatement pst;
        String sql = "INSERT INTO Tasks (employee_id,project_id,task_name,task_description,deadline_date,task_status) VALUES (?,?,?,?,?,?)";
        try {
            pst = Objects.requireNonNull(conn).prepareStatement(sql);
            pst.setString(1, String.valueOf(this.employee_id));
            pst.setString(2,String.valueOf(this.project_id));
            pst.setString(3,this.task_name);
            pst.setString(4,this.task_description);
            pst.setString(5, this.deadline_date);
            pst.setString(6, "not complete");
            pst.execute();
            Popup_Window.confirmation("Task Added Successfully","Add Task");
        } catch (Exception e) {
            Popup_Window.error("Cannot add Task");
        }
    }

    public void delete_Task() {
        Connection conn = MySQL_Connector.ConnectDB();
        String sql = "DELETE FROM Tasks WHERE task_id = '"+this.getTask_id()+"'";
        try {
            PreparedStatement pst = Objects.requireNonNull(conn).prepareStatement(sql);

            pst.execute();
            Popup_Window.confirmation("Task Deleted Successfully","Delete Task");
        } catch (Exception e) {
            Popup_Window.error("Cannot Delete Task");
        }
    }

    public void editTask()  {
        try {
            Connection conn = MySQL_Connector.ConnectDB();
            String sql = "UPDATE Tasks SET employee_id = ' "+this.getEmployee_id()+"', task_name = '"+this.task_name+"', " +
                    "task_description = '"+this.task_description+"', deadline_date = '"+this.deadline_date+"' WHERE task_id = '"+this.getTask_id()+"' ";

            PreparedStatement pst = Objects.requireNonNull(conn).prepareCall(sql);
            pst.execute();
            Popup_Window.confirmation("Task Updated Successfully","Update Task");
        }
        catch( Exception e){
            Popup_Window.error("Cannot Update Task");
        }
    }

    public void update_Task_status(String new_status){
        try {
            Connection conn = MySQL_Connector.ConnectDB();
            String sql = "UPDATE Tasks SET task_status = '"+new_status+"' WHERE task_id = '"+this.getTask_id()+"' ";

            PreparedStatement pst = Objects.requireNonNull(conn).prepareCall(sql);
            pst.execute();
            Popup_Window.confirmation("Task Status Updated Successfully","Task Status");
        }
        catch( Exception e){
            Popup_Window.error("Cannot Update Task Status");
        }
    }
    public void verify(Employee e) throws TaskAlreadyVerifiedException {
        if(this.task_status.equals("Verified")){
            throw new TaskAlreadyVerifiedException("Task Already Verified");
        }
        this.update_Task_status("Verified");
        e.task_verified();
    }

}
