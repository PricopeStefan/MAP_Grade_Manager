package controller;

import domain.Nota;
import events.Event;
import events.EventType;
import events.NotaEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import utils.MailSender;
import utils.Utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ProfesorViewNote extends Controller {
    @FXML
    private Button logoutButton;

    @FXML
    private TableColumn<Nota, String> colStudentID, colStudentNume, colTemaID, colDescID, colPredata, colNota, colProfesor;

    @FXML
    private TableView<Nota> noteTableView;

    @FXML
    private Label currentWeekLabel;

    @FXML
    private TextField studentTextField;


    private ObservableList<Nota> listaNote;

    @FXML
    private void setupTemeForm() throws Exception {
        super.viewAssistant.changeScene(logoutButton.getScene(), service, viewAssistant, "../fxml/profesor_teme_view.fxml", 844, 662);
    }

    @FXML
    private void setupRapoarteForm() throws Exception {
        super.viewAssistant.changeScene(logoutButton.getScene(), service, viewAssistant, "../fxml/profesor_rapoarte_view.fxml", 844, 662);
    }

    @FXML
    private void setupAddNotaView() throws Exception {
        super.viewAssistant.changeScene(logoutButton.getScene(), service, viewAssistant, "../fxml/profesor_add_nota_view.fxml", 844, 662);

    }

    @FXML
    private void setupFilterView() throws Exception {
        super.viewAssistant.changeScene(logoutButton.getScene(), service, viewAssistant, "../fxml/profesor_filter_view.fxml", 844, 662);
    }

    @FXML
    private void doLogout() throws Exception {
        viewAssistant.doLogout(logoutButton.getScene(), service);
    }

    @FXML
    public void initialize() {
        currentWeekLabel.setText("Suntem in saptamana " + Utils.getCurrentWeek());

        studentTextField.textProperty().addListener(((observable, oldValue, newValue) -> refreshNoteWithFilter(newValue)));


    }


    private void setupNoteTableView() {
        colStudentID.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                StringProperty id = new SimpleStringProperty(param.getValue().getStudent().getID().toString());
                return id;
            }
        });
        colStudentNume.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                StringProperty id = new SimpleStringProperty(param.getValue().getStudent().getNume());
                return id;
            }
        });
        colTemaID.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                StringProperty id = new SimpleStringProperty(param.getValue().getTema().getID().toString());
                return id;
            }
        });
        colDescID.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                StringProperty id = new SimpleStringProperty(param.getValue().getTema().getDescriere());
                return id;
            }
        });
        colProfesor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                StringProperty id = new SimpleStringProperty(param.getValue().getProfesor().getNume());
                return id;
            }
        });

        colPredata.setCellValueFactory(new PropertyValueFactory<Nota, String>("predataPe"));
        colNota.setCellValueFactory(new PropertyValueFactory<Nota, String>("valoare"));

        noteTableView.setRowFactory(tv -> {
            TableRow<Nota> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !(row.isEmpty())) {
                    launchEditBox(row.getItem());
                }
            });
            return row;
        });

        initNoteTableView();
    }

    private void initNoteTableView() {
        listaNote = FXCollections.observableList(StreamSupport.stream(service.findAllNote().spliterator(), false).collect(Collectors.toList()));

        Collections.sort(listaNote, new Comparator<Nota>() {
            @Override
            public int compare(domain.Nota o1, domain.Nota o2) {
                return o1.getStudent().getID().compareTo(o2.getStudent().getID());
            }
        });

        noteTableView.setItems(listaNote);
    }

    private void refreshNoteWithFilter(String value) {
        if(value.length() > 0) {
            listaNote = FXCollections.observableList(StreamSupport.stream(service.findAllNote().spliterator(), false).collect(Collectors.toList()));

            Predicate<Nota> nuContineStringul = n -> !n.getStudent().getNume().toLowerCase().contains(value.toLowerCase()) && !n.getStudent().getEmail().toLowerCase().contains(value.toLowerCase()) && !n.getStudent().getGrupa().toLowerCase().contains(value.toLowerCase());

            listaNote.removeIf(nuContineStringul);
            noteTableView.setItems(listaNote);
        }
        else
            initNoteTableView();
    }

    private void launchEditBox(Nota nota) {
        if(nota != null) {
            Dialog<Integer> dialog = new Dialog<>();
            dialog.setTitle("Edit nota");
            dialog.setHeaderText(null);

            VBox vBox = new VBox();

            TextField newNotaInput = new TextField();

            vBox.getChildren().add(new Label("Introduceti noua nota"));
            vBox.getChildren().add(newNotaInput);

            ButtonType loginButtonType = new ButtonType("Modifica", ButtonBar.ButtonData.OK_DONE);
            ButtonType deleteButtonType = new ButtonType("Sterge nota", ButtonBar.ButtonData.APPLY);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, deleteButtonType, ButtonType.CANCEL);
            dialog.getDialogPane().setContent(vBox);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == loginButtonType) {
                    return Integer.valueOf(newNotaInput.getText());
                }
                if (dialogButton == deleteButtonType) {
                    return -1;
                }
                return null;
            });

            Optional<Integer> result = dialog.showAndWait();

            try {
                if(result.isPresent()) {
                    if(result.get() == -1) {
                        service.deleteNota(nota.getID());
                    }
                    else
                        service.updateNota(nota.getID(), result.get());
                }
            } catch (Exception ex) {
                viewAssistant.createErrorBox(ex.getMessage());
            }
        }
    }


    protected void setupGeneral() {
        setupNoteTableView();

    }

    @Override
    public void update(Event event) {
      if(event instanceof NotaEvent) {
            initNoteTableView();

            if(event.getType().equals(EventType.ADD)) {
                sendMail((NotaEvent)event);
            }
        }
    }

    private void sendMail(NotaEvent event) {
        String from = viewAssistant.getUsername();
        String to = event.getNewData().getStudent().getEmail();
        String title = "Ai primit o nota noua";
        String content = "Ai primit nota " + event.getNewData().getValoare() + " la tema " + event.getNewData().getTema().getDescriere();

        MailSender mailSender = new MailSender(from, to, title, content);
        mailSender.start();
    }

}
