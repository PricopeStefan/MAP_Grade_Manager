package controller;

import events.*;
import events.Event;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import domain.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import service.*;
import utils.FiltrareDupa;
import utils.MailSender;
import utils.Pair;
import utils.Utils;
import validators.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class ProfesorNote extends Controller {
    @FXML
    private Button logoutButton;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label currentWeekLabel;

    private Student selectedStudent;
    private Tema selectedTema;
    private Profesor loggedInProfesor;
    private Integer nota, notaDiminuata, predataPe;


    @FXML
    public void initialize() {

//        studentTableView.getSelectionModel().selectedItemProperty().addListener(
//                (observable, oldvalue, newvalue) -> showDetails(newvalue)
//        );

        //notaTextField.textProperty().addListener(((observable, oldValue, newValue) -> validateNota(newValue)));

    }

    @FXML
    private void setupTemeForm() throws Exception {
        super.viewAssistant.changeScene(logoutButton.getScene(), service, viewAssistant, "../fxml/profesor_teme_view.fxml", 844, 662);
    }

    @FXML
    private void setupRapoarteForm() throws Exception {
        super.viewAssistant.changeScene(logoutButton.getScene(), service, viewAssistant, "../fxml/profesor_rapoarte_view.fxml", 844, 662);
    }









    @Override
    public void update(Event event) {
//        if(event instanceof StudentEvent) {
//            initStudentsTableView(this.studentTableView);
//        }
//        else if(event instanceof TemaEvent) {
//            initTemeComboBox(temaComboBox);
//        }
//        else if(event instanceof NotaEvent) {
//            initNoteTableView();
//
//            if(event.getType().equals(EventType.ADD)) {
//                sendMail((NotaEvent)event);
//            }
//        }
    }



//    private void initTemeComboBox(ComboBox<Tema> temaComboBox) {
//        temaComboBox.getItems().clear();
//
//        Callback<ListView<Tema>, ListCell<Tema>> factory = lv -> new ListCell<Tema>() {
//            @Override
//            protected void updateItem(Tema item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty ? "" : item.getID() + ")" + item.getDescriere() + ", deadline:" + item.getDeadLine());
//            }
//
//        };
//
//        temaComboBox.setCellFactory(factory);
//        temaComboBox.setButtonCell(factory.call(null));
//
//        ObservableList<Tema> teme = FXCollections.observableList(StreamSupport.stream(service.findAllTeme().spliterator(), false).collect(Collectors.toList()));
//        Predicate<Tema> esteCurenta = t -> t.getDeadLine() == Utils.getCurrentWeek();
//
//        Tema actuala = teme.filtered(esteCurenta).isEmpty() ? null : teme.filtered(esteCurenta).get(0);
//        if(actuala != null) {
//            temaComboBox.getItems().add(actuala);
//            teme.removeIf(esteCurenta);
//        }
//
//        temaComboBox.getItems().addAll(teme);
//        temaComboBox.getSelectionModel().selectFirst();
//
////        temaComboBox.setOnAction(event -> {
////            studentTextField.clear();
////            notaTextField.clear();
////            feedbackTextArea.clear();
////        });
//    }





    @FXML
    private void setupAddNotaView() throws Exception {
        super.viewAssistant.changeScene(logoutButton.getScene(), service, viewAssistant, "../fxml/profesor_add_nota_view.fxml", 844, 662);

    }

    @FXML
    private void setupNoteView() throws Exception {
        super.viewAssistant.changeScene(logoutButton.getScene(), service, viewAssistant, "../fxml/profesor_view_note.fxml", 844, 662);
    }

    @Override
    protected void setupGeneral() {
        //initTemeComboBox(temaComboBox);
        //initStudentsTableView(this.studentTableView);
    }



    @FXML
    private void doLogout() throws Exception {
        viewAssistant.doLogout(logoutButton.getScene(), service);
    }


}
