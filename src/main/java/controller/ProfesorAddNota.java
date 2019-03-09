package controller;

import domain.Nota;
import domain.Profesor;
import domain.Student;
import domain.Tema;
import events.Event;
import events.NotaEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import utils.MailSender;
import utils.Utils;
import validators.ValidationException;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ProfesorAddNota extends Controller {
    @FXML
    private Button notaAddButton, logoutButton;

    @FXML
    private TextArea feedbackTextArea;

    @FXML
    private ComboBox<Tema> temaComboBox;

    @FXML
    private CheckBox intarziereCheckBox;

    @FXML
    private TextField studentTextField, notaTextField;

    @FXML
    private TableView<Student> studentTableView;

    @FXML
    private Label currentWeekLabel;

    private Student selectedStudent;
    private Tema selectedTema;
    private Profesor loggedInProfesor;
    private Integer nota, notaDiminuata, predataPe;


    @FXML
    public void initialize() {
        studentTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldvalue, newvalue) -> showDetails(newvalue)
        );

        notaTextField.textProperty().addListener(((observable, oldValue, newValue) -> validateNota(newValue)));
        currentWeekLabel.setText("Suntem in saptamana " + Utils.getCurrentWeek());

    }
    private void initTemeComboBox(ComboBox<Tema> temaComboBox) {
        temaComboBox.getItems().clear();

        Callback<ListView<Tema>, ListCell<Tema>> factory = lv -> new ListCell<Tema>() {
            @Override
            protected void updateItem(Tema item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getID() + ")" + item.getDescriere() + ", deadline:" + item.getDeadLine());
            }

        };

        temaComboBox.setCellFactory(factory);
        temaComboBox.setButtonCell(factory.call(null));

        ObservableList<Tema> teme = FXCollections.observableList(StreamSupport.stream(service.findAllTeme().spliterator(), false).collect(Collectors.toList()));
        Predicate<Tema> esteCurenta = t -> t.getDeadLine() == Utils.getCurrentWeek();

        Tema actuala = teme.filtered(esteCurenta).isEmpty() ? null : teme.filtered(esteCurenta).get(0);
        if(actuala != null) {
            temaComboBox.getItems().add(actuala);
            teme.removeIf(esteCurenta);
        }

        temaComboBox.getItems().addAll(teme);
        temaComboBox.getSelectionModel().selectFirst();

        temaComboBox.setOnAction(event -> {
            studentTextField.clear();
            notaTextField.clear();
            feedbackTextArea.clear();
        });
    }

    @FXML
    private void computeFeedback() {
        feedbackTextArea.clear();

        selectedTema = temaComboBox.getValue();
        notaAddButton.setStyle("-fx-background-color: lightgray");
        notaAddButton.setDisable(false);
        predataPe = Utils.getCurrentWeek();
        notaDiminuata = nota;

        if (Utils.getCurrentWeek() > selectedTema.getDeadLine() + 2 && !intarziereCheckBox.isSelected()) {
            feedbackTextArea.clear();
            feedbackTextArea.appendText("Nu se poate acorda nicio nota!\n(A ratat deadlineul nemotivat)");
            notaAddButton.setStyle("-fx-background-color: red");
            notaAddButton.setDisable(true);
        }
        else if(Utils.getCurrentWeek() > selectedTema.getDeadLine() && intarziereCheckBox.isSelected()) {
            feedbackTextArea.clear();
            feedbackTextArea.appendText("Intarziere motivata, nu se vor scadea puncte!\n");
            predataPe = selectedTema.getDeadLine();
        }
        else if(Utils.getCurrentWeek() > selectedTema.getDeadLine() &&
                Utils.getCurrentWeek() <= selectedTema.getDeadLine() + 2 &&
                !intarziereCheckBox.isSelected()) {
            feedbackTextArea.clear();
            feedbackTextArea.appendText("Nota a fost diminuata cu " + 3 * (Utils.getCurrentWeek() - selectedTema.getDeadLine()) + " puncte!\n");
            notaDiminuata = nota - 3 * (Utils.getCurrentWeek() - selectedTema.getDeadLine());
        }
    }

    private void showDetails(Student n) {
        if(n != null) {
            studentTextField.setText(n.getNume());
            selectedStudent = n;
        }
    }

    private void refreshTable(String newData) {
        if(newData != null) {
            Predicate<Student> notNumeContine = x -> !x.getNume().toLowerCase().contains(newData.toLowerCase()) && !x.getGrupa().toLowerCase().contains(newData.toLowerCase());
            studentTableView.getItems().removeIf(notNumeContine);

            if(newData.equals(""))
                initStudentsTableView(this.studentTableView);
        }
    }

    private void initStudentsTableView(TableView<Student> studentTableView) {
        studentTableView.getItems().clear();

        super.viewAssistant.setupStudentTableView(studentTableView);

        for (Student st:service.findAllStudents())
            studentTableView.getItems().add(st);
    }

    private void validateNota(String newValue) {
        try {
            Integer i = Integer.parseInt(newValue);
            if(i < 1 || i > 10) {
                throw new Exception("xd");
            }
            nota = i;
            computeFeedback();
        } catch (Exception ex) {
            Platform.runLater(() -> {
                notaTextField.clear();
            });
        }
    }

    protected void setupGeneral() {
        studentTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldvalue, newvalue) -> showDetails(newvalue)
        );

        studentTextField.textProperty().addListener((observable, oldData, newData) -> {
            refreshTable(newData);
        });

        initTemeComboBox(temaComboBox);
        initStudentsTableView(studentTableView);

        getLoggedInProfesor();
    }

    @Override
    public void update(Event event) {
        //do update
    }

    private void sendMail(NotaEvent event) {
        String from = viewAssistant.getUsername();
        String to = event.getNewData().getStudent().getEmail();
        String title = "Ai primit o nota noua";
        String content = "Ai primit nota " + event.getNewData().getValoare() + " la tema " + event.getNewData().getTema().getDescriere();

        MailSender mailSender = new MailSender(from, to, title, content);
        mailSender.start();
    }


    @FXML
    private void setupTemeForm() throws Exception {
        super.viewAssistant.changeScene(logoutButton.getScene(), service, viewAssistant, "../fxml/profesor_teme_view.fxml", 844, 662);
    }

    @FXML
    private void setupRapoarteForm() throws Exception {
        super.viewAssistant.changeScene(logoutButton.getScene(), service, viewAssistant, "../fxml/profesor_rapoarte_view.fxml", 844, 662);
    }

    @FXML
    private void setupNoteView() throws Exception {
        super.viewAssistant.changeScene(logoutButton.getScene(), service, viewAssistant, "../fxml/profesor_view_note.fxml", 844, 662);
    }

    @FXML
    private void setupFilterView() throws Exception {
        super.viewAssistant.changeScene(logoutButton.getScene(), service, viewAssistant, "../fxml/profesor_filter_view.fxml", 844, 662);
    }
    @FXML
    private void doLogout() throws Exception {
        viewAssistant.doLogout(logoutButton.getScene(), service);
    }

    private Alert createAlertBox() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        String textDeAdaugat = "Sigur doriti sa adaugati urmatoarea nota?\n";
        textDeAdaugat += "Student: ";
        textDeAdaugat += selectedStudent.getID() + ", " + selectedStudent.getNume() + "\n";
        textDeAdaugat += "Tema: ";
        textDeAdaugat += selectedTema.getID() + ", " + selectedTema.getDescriere() + "\n";
        textDeAdaugat += "Nota: ";
        textDeAdaugat += notaDiminuata + "\n";
        alert.setContentText(textDeAdaugat);

        return alert;
    }
    private Alert createSuccessBox() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Nota adaugata cu succes!");
        alert.show();

        return alert;
    }


    @FXML
    private void addNotaHandler() {
        Alert alert = createAlertBox();

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get().getText().equals("OK")) {
            try {

                Nota returnata = service.saveNota(selectedStudent.getID(), selectedTema.getID(), Integer.parseInt(notaTextField.getText()), Utils.getCurrentWeek(), loggedInProfesor, feedbackTextArea.getText());

                if (returnata != null) {
                    throw new ValidationException("Nota existenta deja!");
                }

                Alert success = createSuccessBox();
                success.show();
            }
            catch (Exception ex) {
                viewAssistant.createErrorBox(ex.getMessage());
            }
        }
    }


    private void getLoggedInProfesor() {
        String emailProf = viewAssistant.getUsername();

        ObservableList<Profesor> profesors = FXCollections.observableList(StreamSupport.stream(service.findAllProfesori().spliterator(), false).collect(Collectors.toList()));
        for(int i = 0; i < profesors.size(); i++)
            if (profesors.get(i).getEmail().equals(emailProf))
                loggedInProfesor = profesors.get(i);
    }
}
