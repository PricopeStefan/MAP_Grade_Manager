package controller;

import domain.Tema;
import events.Event;
import events.EventType;
import events.TemaEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import utils.MailSender;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ProfesorTeme extends Controller {
    @FXML
    private Button logoutButton;

    @FXML
    private TextField idInput, descriereInput, primireInput, deadlineInput;

    @FXML
    private TableView<Tema> tableView;

    @FXML
    private TableColumn<Tema, String> idColoana, descriereColoana, primireColoana, deadlineColoana;

    private ObservableList<Tema> observableList;

    @Override
    protected void setupGeneral() {
        observableList = FXCollections.observableList(StreamSupport.stream(service.findAllTeme().spliterator(), false).collect(Collectors.toList()));

        initTableView();
    }

    private void initTableView() {
        tableView.getItems().clear();

        idColoana.setCellValueFactory(new PropertyValueFactory<>("ID"));
        descriereColoana.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        primireColoana.setCellValueFactory(new PropertyValueFactory<>("dataPrimire"));
        deadlineColoana.setCellValueFactory(new PropertyValueFactory<>("deadLine"));

        tableView.setRowFactory( tv -> {
            TableRow<Tema> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !(row.isEmpty())) {
                    launchEditBox(row.getItem());
                }
            });
            return row;
        });
        tableView.setItems(observableList);
    }

    private void launchEditBox(Tema tema) {
        if(tema != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Prelungire tema");
            dialog.setHeaderText("Prelungire deadline tema laborator \n" + tema.getDescriere());
            dialog.setContentText("Pana in ce saptamana doriti sa prelungiti tema?");

            Optional<String> result = dialog.showAndWait();

            try {
                result.ifPresent(saptamana -> service.updateTema(tema.getID(), Integer.parseInt(saptamana)));
            } catch (Exception ex) {
                viewAssistant.createErrorBox(ex.getMessage());
            }
        }
    }

    @FXML
    private void setupNoteForm() throws Exception {
        super.viewAssistant.changeScene(logoutButton.getScene(), service, viewAssistant, "../fxml/profesor_view_note.fxml", 844, 662);
    }

    @FXML
    private void setupRapoarteForm() throws Exception {
        super.viewAssistant.changeScene(logoutButton.getScene(), service, viewAssistant, "../fxml/profesor_rapoarte_view.fxml", 844, 662);
    }

    @FXML
    private void addTema() {
        String id = idInput.getText();
        String desc = descriereInput.getText();
        String primire = primireInput.getText();
        String deadline = deadlineInput.getText();

        if(!id.isEmpty() && !desc.isEmpty() && !primire.isEmpty() && !deadline.isEmpty()) {
            try {
                Tema st = new Tema(Integer.parseInt(id), desc, Integer.parseInt(deadline), Integer.parseInt(primire));
                service.save(st);
            } catch (Exception ex) {
                viewAssistant.createErrorBox(ex.getMessage());
            }
        }
        clearFields();
    }

    @FXML
    private void deleteTema() {
        Tema selected = tableView.getSelectionModel().getSelectedItem();
        service.deleteTema(selected.getID());
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void doLogout() throws Exception {
        viewAssistant.doLogout(logoutButton.getScene(), service);
    }

    private void clearFields() {
        idInput.clear();
        descriereInput.clear();
        primireInput.clear();
        deadlineInput.clear();
    }

    @Override
    public void update(Event event) {
        if(event instanceof TemaEvent) {
            try {
                TemaEvent se = (TemaEvent)event;
                if(se.getType() == EventType.ADD) {
                    handleAddEvent(se);
                }
                if(se.getType() == EventType.DELETE) {
                    int idTema = se.getOldData().getID();
                    Predicate<Tema> idEgal = x -> x.getID() == idTema;
                    observableList.removeIf(idEgal);
                }
                if(se.getType() == EventType.UPDATE) {
                    handleUpdateEvent(se);
                }
            } catch (Exception ex) {
                //again, do something
            }
        }
    }

    private void handleAddEvent(TemaEvent se) {
        observableList.add(se.getNewData());

        String from = viewAssistant.getUsername();
        String header = "O tema noua a fost adaugata";
        String content = "A fost adaugata tema " + se.getNewData().getDescriere() + " cu deadlineul in saptamana " + se.getNewData().getDeadLine();

        service.findAllStudents().forEach(student -> {
            MailSender mailSender = new MailSender(from, student.getEmail(), header, content);
            mailSender.start();
        });
    }

    private void handleUpdateEvent(TemaEvent se) {
        int idTema = se.getOldData().getID();
        Predicate<Tema> idEgal = x -> x.getID() == idTema;
        observableList.removeIf(idEgal);
        observableList.add(se.getNewData());

        String from = viewAssistant.getUsername();
        String header = "Deadline-ul unei teme a fost modificat!";
        String content = "Tema " + se.getNewData().getDescriere() + " si-a prelungit deadlineul pana in saptamana " + se.getNewData().getDeadLine();

        service.findAllStudents().forEach(student -> {
            MailSender mailSender = new MailSender(from, student.getEmail(), header, content);
            mailSender.start();
        });
    }
}
