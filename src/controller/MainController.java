package controller;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
//import org.controlsfx.control.Notifications;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
//import library.AlertBox;
import javafx.util.Callback;
import library.Registers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.time.LocalDate;
//import java.awt.event.ActionEvent;
// # onaction ın çözümü bu
//import java.awt.event.MouseEvent;
import java.sql.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


import static java.time.LocalDateTime.now;
import static java.time.Month.MARCH;

public class MainController  implements Initializable {

    private  ObjectProperty<Date> date;

    @FXML
    public TextField departmentField;
    @FXML
    private TableColumn<Registers, Date> dateColumn2;
    @FXML
    public ImageView image1;
    @FXML
    public Circle mycircle;
    @FXML
    public DatePicker datePicker2;

    @FXML
    private Label uyarılabel;

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private TextArea textarea;

    @FXML
    private TextField mailField;

    @FXML
    private DatePicker datePicker;


    @FXML
    private TextField filteredField;

    @FXML
    private Button insertButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button button;

    @FXML
    private TableView<Registers> TableView;

    @FXML
    private TableColumn<Registers, Integer> idColumn;

    @FXML
    private TableColumn<Registers, String> nameColumn;

    @FXML
    private TableColumn<Registers, String> surnameColumn;

    @FXML
    private TableColumn<Registers, String> departmentColumn;

    @FXML
    private TableColumn<Registers, String> mailColumn;

    @FXML
    private TableColumn<Registers, Date> dateColumn;
    @FXML
    private TableColumn<Registers, Boolean> emptyColumn;

    private Date cellData;

    public ObjectProperty<Date> dateObjectProperty(){
        return date;
    }
    private Service<Void> backgroundThread;

    @FXML private void showAlert(){
        backgroundThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return null;
            }
        };
        for(int i=0;i<=10;i++){
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run()
                {

                    //alertDate();
                   // uyarılabel.setText("kayıt bulundu");

                    //alert2();
                }

            }, 5000 * i); // 5 second intervals


            //alertDate();
            //alert2();




        }


    }


    @FXML
    private void insertButton() {
        insert();
        showRegisters();
        Sorting();
        giveAlert();
    }


    @FXML
    private void updateButton() {
        String query = "UPDATE registers SET Name='" + nameField.getText() + "',Surname='" + surnameField.getText() + "',Department='" + departmentField.getText() + "',Mail='" + mailField.getText() + "',Date='" + datePicker.getValue() + "',Date2='" + datePicker2.getValue() + "' WHERE Number='" + idField.getText() + "'";
        executeQuery(query);
        showRegisters();
        Sorting();
        giveAlert();
    }

    @FXML
    private void deleteButton() {
        String query = "DELETE FROM registers WHERE Number='" + idField.getText() + "'";
        executeQuery(query);
        idField.clear();
        nameField.clear();
        surnameField.clear();
        departmentField.clear();
        mailField.clear();
        datePicker.getEditor().clear();
        datePicker2.getEditor().clear();
        showRegisters();
        Sorting();
        giveAlert();
    }
    public void insert(){
        String query = "insert into registers values('" + idField.getText() + "','" + nameField.getText() + "','" + surnameField.getText() + "','" + departmentField.getText() + "','" + mailField.getText() + "','" + datePicker.getValue() + "','" + datePicker2.getValue() + "')";
        executeQuery(query);
        giveAlert();
    }

    public void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        giveAlert();
        showAlert();
        idField.addEventFilter(KeyEvent.KEY_TYPED,numfilter());
       // nameField.addEventFilter(KeyEvent.KEY_TYPED,stringFilter());
     //   surnameField.addEventFilter(KeyEvent.KEY_TYPED,stringFilter());
    //    departmentField.addEventFilter(KeyEvent.KEY_TYPED,stringFilter());
        showRegisters();
        Sorting();

        mycircle.setStroke(Color.SEAGREEN);
         Image im = new Image("images/indir.png", false);
         mycircle.setFill(new ImagePattern(im));
         mycircle.setEffect((new DropShadow(+25d, +0d, +2d, Color.DARKCYAN)));

    }


    public Connection getConnection() {
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/proje_sgk?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "1794eliz");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<Registers> getRegistersList() {
        ObservableList<Registers> registerList = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String query = "SELECT * FROM registers ";
        Statement st;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            Registers registers;
            while (rs.next()) {
                registers = new Registers(rs.getInt("Number"), rs.getString("Name"), rs.getString("Surname"), rs.getString("Department"), rs.getString("Mail"), rs.getDate("Date"), rs.getDate("Date2"));
                registerList.add(registers);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registerList;
    }

    // I had to change ArrayList to ObservableList I didn't find another option to do this but this works :)
    public void showRegisters() {
        ObservableList<Registers> list = getRegistersList();

        idColumn.setCellValueFactory(new PropertyValueFactory<Registers, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Registers, String>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<Registers, String>("surname"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<Registers, String>("department"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<Registers, String>("mail"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Registers, Date>("date"));
        dateColumn2.setCellValueFactory(new PropertyValueFactory<Registers, Date>("date2"));
        TableView.setItems(list);

//*************
        TableView.setEditable(true);

        TableColumn<Object, String> idColumn = new TableColumn<>("Number"); // Kolon Başlığı
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idColumn")); // Kolona yüklenecek veri alanı adı
        idColumn.setCellFactory(TextFieldTableCell.forTableColumn()); // Edit Yapacaksak alanı bir textfield olarak tanımladık


        TableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }


    @FXML
    public void clickItem(MouseEvent event) {
        if (event.getClickCount() == 2) //Checking double click
        {
            idField.setText(String.valueOf(TableView.getSelectionModel().getSelectedItem().getId()));
            nameField.setText(TableView.getSelectionModel().getSelectedItem().getName());
            surnameField.setText(TableView.getSelectionModel().getSelectedItem().getSurname());
            departmentField.setText(TableView.getSelectionModel().getSelectedItem().getDepartment());
            mailField.setText(TableView.getSelectionModel().getSelectedItem().getMail());
            datePicker.setValue(TableView.getSelectionModel().getSelectedItem().getDate().toLocalDate());
            datePicker2.setValue(TableView.getSelectionModel().getSelectedItem().getDate2().toLocalDate());


        }
    }

    public void Completedclick(MouseEvent event){
        if(event.getButton()== MouseButton.SECONDARY){
            dateColumn.setCellFactory(column -> new TableCell<Registers, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        dateColumn.setCellValueFactory(new PropertyValueFactory<Registers, Date>("date"));
                        setText(null);
                        setStyle("");
                    } else {
                        setStyle("-fx-background-color: green");

                    }
                }
            });

        }
    }

    public void ClearButton() {
        idField.clear();
        nameField.clear();
        surnameField.clear();
        departmentField.clear();
        mailField.clear();
        datePicker.getEditor().clear();
        datePicker2.getEditor().clear();
    }

    public void Filtering() {
        ObservableList<Registers> list = getRegistersList();
        FilteredList<Registers> filteredList = new FilteredList<>(list, p -> true);
        filteredField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(p -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (p.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (p.getSurname().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        SortedList<Registers> sortedData = new SortedList<>(filteredList);
        sortedData.comparatorProperty().bind(TableView.comparatorProperty());
        TableView.setItems(sortedData);

    }

    public void Sorting() {
        ObservableList<Registers> list = getRegistersList();
        SortedList<Registers> sortedList = new SortedList<>(list,
                (Registers date1, Registers date2) -> {
                    if (date1.getDate().toLocalDate().isBefore(date2.getDate().toLocalDate())) {
                        return -1;
                    } else if (date1.getDate().toLocalDate().isAfter(date2.getDate().toLocalDate())) {
                        return 1;
                    } else {
                        return 0;
                    }
                });


        TableView.setItems(sortedList);
    }

    public void giveAlert() {
        ObservableList<Registers> list = getRegistersList();
        SortedList<Registers> alertList = new SortedList<Registers>(list,
                (Registers date, Registers date2) -> {//Sürekli kontrol edip saatte 1 tekrar uygulama açılsın.
                    if (date.getDate().toLocalDate().isEqual(LocalDate.now().plusDays(1))) {
                        uyarılabel.textProperty().setValue("\tStaj tarihleri yaklaşan kayıtlar bulundu! \n\t" + date.getDate().toString());
                        deneme();
                        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                        dateColumn.setCellFactory(column -> new TableCell<Registers, Date>() {
                            @Override
                            protected void updateItem(Date item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    dateColumn.setCellValueFactory(new PropertyValueFactory<Registers, Date>("date"));
                                    setText(null);
                                    setStyle("");
                                } else {
                                    setText(item.toString());
                                    // Style all dates in March with a different color.
                                    if (item.equals(date.getDate())) {
                                        setTextFill(Color.BLACK);
                                        setStyle("-fx-background-color: red");

                                    } else {
                                        setTextFill(Color.BLACK);
                                        setStyle("");
                                    }
                                }
                            }
                        });
                    }else{
                        uyarılabel.setText("Yaklaşan staj kaydı bulunmamaktadır.");

                    }
                    return 0;
                });

    }

    public static EventHandler<KeyEvent> numfilter(){
        EventHandler<KeyEvent> aux=new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(!"0123456789".contains(keyEvent.getCharacter())){
                    keyEvent.consume();
                }
            }
        };
        return aux;

    }
    public static EventHandler<KeyEvent> stringFilter() {

        EventHandler<KeyEvent> aux = new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                if (!keyEvent.getCharacter().matches("[A-Za-z]")) {
                    keyEvent.consume();

                }
            }

        };
        return aux;
    }

    public void deneme() {
        for (int i = 0; i <= 0; i++) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                               public void run() {
                                   Platform.runLater(() -> {

                                       Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                       alert.setTitle("Information dialog");
                                       alert.setHeaderText("Staj kayıtları");

                                       String contxt = null;

                                       alert.setContentText(contxt);
                                       alert.showAndWait();
                                   });

                               }


                           }, 10000
            );
        }


    }
}








































