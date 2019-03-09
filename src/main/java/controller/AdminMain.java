package controller;

import domain.Profesor;
import domain.Student;
import events.Event;
import events.ProfesorEvent;
import events.StudentEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import repository.AccountXMLRepository;

import utils.Types;

public class AdminMain extends Controller {
    @FXML
    private Button logoutButton, addButton, deleteButton;

    @FXML
    private Label Label1, Label2, Label3, Label4, Label5, Label6;

    @FXML
    private TextField textField1, textField2, textField3, textField4;

    @FXML
    private PasswordField passField1, passField2;

    private TableView<Student> studentTableView;
    private TableView<Profesor> profesorTableView;

    @FXML
    private BorderPane borderPane;

    private AccountXMLRepository accounts;

    @FXML
    public void initialize() {
        accounts = AccountXMLRepository.getInstance();

        borderPane.setVisible(false);
    }

    @Override
    protected void setupGeneral() {
        setupStudentTable();
        setupTeacherTable();
    }

    private void setupTeacherTable() {
        profesorTableView = new TableView<>();

        profesorTableView.setVisible(false);
        profesorTableView.setManaged(false);
        profesorTableView.prefWidthProperty().setValue(280);

        TableColumn<Profesor, String> colID = new TableColumn<>("ID");
        TableColumn<Profesor, String> colNume = new TableColumn<>("Nume");
        TableColumn<Profesor, String> colEmail = new TableColumn<>("Email");

        colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        colID.maxWidthProperty().bind(studentTableView.widthProperty().divide(4));
        colNume.maxWidthProperty().bind(studentTableView.widthProperty().divide(4));
        colEmail.maxWidthProperty().bind(studentTableView.widthProperty().divide(2));

        colID.prefWidthProperty().bind(studentTableView.widthProperty().divide(4));
        colNume.prefWidthProperty().bind(studentTableView.widthProperty().divide(4));
        colEmail.prefWidthProperty().bind(studentTableView.widthProperty().divide(2));

        profesorTableView.getColumns().add(colID);
        profesorTableView.getColumns().add(colNume);
        profesorTableView.getColumns().add(colEmail);
        profesorTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldvalue, newvalue) -> showDetails(newvalue)
        );
        profesorTableView.getStyleClass().add("tableView");
    }
    private void setupStudentTable() {
        studentTableView = new TableView<>();

        studentTableView.setVisible(false);
        studentTableView.setManaged(false);
        studentTableView.prefWidthProperty().setValue(280);

        TableColumn<Student, String> colID = new TableColumn<>("ID");
        TableColumn<Student, String> colNume = new TableColumn<>("Nume");
        TableColumn<Student, String> colEmail = new TableColumn<>("Email");

        colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        colID.maxWidthProperty().bind(studentTableView.widthProperty().divide(4));
        colNume.maxWidthProperty().bind(studentTableView.widthProperty().divide(4));
        colEmail.maxWidthProperty().bind(studentTableView.widthProperty().divide(2));

        colID.prefWidthProperty().bind(studentTableView.widthProperty().divide(4));
        colNume.prefWidthProperty().bind(studentTableView.widthProperty().divide(4));
        colEmail.prefWidthProperty().bind(studentTableView.widthProperty().divide(2));

        studentTableView.getColumns().add(colID);
        studentTableView.getColumns().add(colNume);
        studentTableView.getColumns().add(colEmail);

        studentTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldvalue, newvalue) -> showDetails(newvalue)
        );
        studentTableView.getStyleClass().add("tableView");
    }
    private void showDetails(Student selectat) {
        if (selectat != null) {
            textField1.setText(selectat.getID().toString());
            textField2.setText(selectat.getNume());
            textField3.setText(selectat.getEmail());
        }
    }
    private void showDetails(Profesor selectat) {
        if(selectat != null) {
            textField2.setText(selectat.getID().toString());
            textField3.setText(selectat.getNume());
            textField4.setText(selectat.getEmail());
        }
    }

    private void setupStudentForm() {
        Label1.setText("ID");
        Label2.setText("Nume");
        Label3.setText("Email");
        Label4.setText("Grupa");
        Label5.setText("Parola");
        Label6.setText("Repeta parola");
        clearInputs();

        Label1.setVisible(true);
        Label1.setManaged(true);
        textField1.setVisible(true);
        textField1.setManaged(true);
        profesorTableView.setVisible(false);
        profesorTableView.setManaged(false);
        borderPane.setVisible(true);

        refreshStudentTable();
    }
    private void refreshStudentTable() {
        studentTableView.getItems().clear();

        for (Student st:service.findAllStudents())
            studentTableView.getItems().add(st);

        borderPane.setRight(studentTableView);
        studentTableView.setVisible(true);
        studentTableView.setManaged(true);
    }
    private void setupTeacherForm() {
        Label2.setText("ID");
        Label3.setText("Nume");
        Label4.setText("Email");
        Label5.setText("Parola");
        clearInputs();

        Label1.setVisible(false);
        Label1.setManaged(false);
        textField1.setVisible(false);
        textField1.setManaged(false);
        studentTableView.setVisible(false);
        studentTableView.setManaged(false);
        borderPane.setVisible(true);

        refreshProfesorTable();
    }
    private void refreshProfesorTable() {
        profesorTableView.getItems().clear();

        for (Profesor pr:service.findAllProfesori())
            profesorTableView.getItems().add(pr);

        borderPane.setRight(profesorTableView);
        profesorTableView.setVisible(true);
        profesorTableView.setManaged(true);
    }

    @FXML
    private void showStudentForm() {
        setupStudentForm();
        addButton.setOnAction(event -> {
            addStudent(textField1.getText(), textField2.getText(), textField3.getText(), textField4.getText(), passField1.getText(), passField2.getText());
        });
        deleteButton.setOnAction(event -> {
            deleteStudent(textField1.getText(), textField3.getText());
        });
    }
    @FXML
    private void showTeacherForm() {
        setupTeacherForm();

        addButton.setOnAction(event -> {
            addProfesor(textField2.getText(), textField3.getText(), textField4.getText(), passField1.getText(), passField2.getText());
        });
        deleteButton.setOnAction(event -> {
            deleteProfesor(textField2.getText(), textField4.getText());
        });
    }

    private void addStudent(String id, String nume, String email, String grupa, String password1, String password2) {
        try {
            Student st = new Student(Integer.parseInt(id), nume, grupa, email);

            accounts.createAccount(email, password1, password2, Types.STUDENT); //o sa crape daca exista emailul
            service.save(st); // o sa crape daca exista deja id-ul asta
        } catch (Exception ex) {
            //service.deleteStudent(Integer.parseInt(id));
            viewAssistant.createErrorBox(ex.getMessage());
        }
        clearInputs();
    }
    private void deleteStudent(String idStr, String email) {
        try {
            accounts.removeAccount(email); //o sa caute in conturile de tipul student emailul dat si o sa il stearga sau arunca exceptie
            service.deleteStudent(Integer.parseInt(idStr));
        } catch (Exception ex) {
            viewAssistant.createErrorBox(ex.getMessage());
        }
        clearInputs();
    }
    private void addProfesor(String id, String nume, String email, String parola1, String parola2) {
        try {
            Profesor pr = new Profesor(Integer.parseInt(id), nume, email);

            accounts.createAccount(email, parola1, parola2, Types.PROFESOR); //o sa crape daca exista emailul
            service.save(pr); // o sa crape daca exista deja id-ul asta
        } catch (Exception ex) {
            //service.deleteProfesor(Integer.parseInt(id));
            viewAssistant.createErrorBox(ex.getMessage());
        }
        clearInputs();
    }
    private void deleteProfesor(String idStr, String email) {
        try {
            accounts.removeAccount(email); //o sa caute in conturile de tipul student emailul dat si o sa il stearga sau arunca exceptie
            service.deleteProfesor(Integer.parseInt(idStr));
        } catch (Exception ex) {
            viewAssistant.createErrorBox(ex.getMessage());
        }
        clearInputs();
    }

    @Override
    public void update(Event event) {
        if(event instanceof StudentEvent) {
            refreshStudentTable();
        }
        else if(event instanceof ProfesorEvent) {
            refreshProfesorTable();
        }
    }

    @FXML
    private void doLogout() throws Exception {
        viewAssistant.doLogout(logoutButton.getScene(), this.service);
    }

    private void clearInputs() {
        textField1.clear();
        textField2.clear();
        textField3.clear();
        textField4.clear();
        passField1.clear();
        passField2.clear();
    }
}
