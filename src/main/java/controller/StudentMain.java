package controller;

import domain.Nota;
import domain.Student;
import domain.Tema;
import events.Event;
import events.NotaEvent;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import utils.Utils;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StudentMain extends Controller{
    @FXML
    private Button logoutButton;

    @FXML
    private TableView<Tema> tableTeme; //tabel cu temele in curs/curent

    @FXML
    private TableColumn<Tema, String> temaIDColumn, temaDescColumn, temaSaptPrimireColumn, temaDeadlineColumn;

    @FXML
    private TableView<Nota> tableNote; //tabel cu notele mele

    @FXML
    private TableColumn<Nota, String> notaTemaIDColumn, notaTemaDescColumn, notaTemaPredataColumn, notaProfesorColumn, notaValoareColumn;

    @FXML
    private Label medieCurenta; //un label simplu ce afiseaza media curenta

    @FXML
    private LineChart<Number, Number> evolutionLineChart;

    private Student loggedInStudent;
    //un line graph ce prezinta evolutia pe parcursul semestrului(pe X ai IDTema, pe Y ai nota obtinuta)

    @Override
    protected void setupGeneral() {
        //do nothing so far
        displayMedie();
        setupNoteTabel();
        setupTemeTabel();
        setupLineChart();
    }

    private void setupNoteTabel() {
        notaTemaIDColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                StringProperty id = new SimpleStringProperty(param.getValue().getTema().getID().toString());
                return id;
            }
        });
        notaTemaDescColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                StringProperty id = new SimpleStringProperty(param.getValue().getTema().getDescriere());
                return id;
            }
        });
        notaProfesorColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                StringProperty id = new SimpleStringProperty(param.getValue().getProfesor().getNume());
                return id;
            }
        });

        notaTemaPredataColumn.setCellValueFactory(new PropertyValueFactory<>("predataPe"));
        notaValoareColumn.setCellValueFactory(new PropertyValueFactory<>("valoare"));


        tableNote.setItems(getNote());

    }

    private ObservableList<Nota> getNote() {
        ObservableList<Nota> note = FXCollections.observableList(StreamSupport.stream(service.findAllNote().spliterator(), false).collect(Collectors.toList()));
        Predicate<Nota> nuEsteAStudentului = n -> !n.getStudent().equals(loggedInStudent);

        note.removeIf(nuEsteAStudentului);
        Collections.sort(note, (Nota n1, Nota n2) -> n1.getTema().getDeadLine().compareTo(n2.getTema().getDeadLine()));
        return note;
    }

    private void setupTemeTabel() {
        ObservableList<Tema> temeNefacute = getTemeNefacute();

        temaIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        temaDescColumn.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        temaSaptPrimireColumn.setCellValueFactory(new PropertyValueFactory<>("dataPrimire"));
        temaDeadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadLine"));

        tableTeme.setItems(temeNefacute);
    }

    private ObservableList<Tema> getTemeNefacute() {
        ObservableList<Nota> note = FXCollections.observableList(StreamSupport.stream(service.findAllNote().spliterator(), false).collect(Collectors.toList()));
        Predicate<Nota> nuEsteAStudentului = n -> !n.getStudent().equals(loggedInStudent);

        note.removeIf(nuEsteAStudentului);

        ObservableList<Tema> teme = FXCollections.observableList(StreamSupport.stream(service.findAllTeme().spliterator(), false).collect(Collectors.toList()));
        Predicate<Tema> aPredatTemaLaTimp = t -> {
            for(int i = 0; i < note.size(); i++) {
                if(note.get(i).getTema().equals(t) && note.get(i).getPredataPe() <= note.get(i).getTema().getDeadLine())
                    return true;
            }
            return false;
        };

        teme.removeIf(aPredatTemaLaTimp);

        return teme;
    }

    private void setupLineChart() {
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Evolutie note");
        //populating the series with data
        ObservableList<Nota> note = getNote();
        note.forEach(n -> {
            series.getData().add(new XYChart.Data(n.getTema().getDescriere(), n.getValoare()));
        });
        //ORDONEAZA NOTELE DUPA DEADLINE
        evolutionLineChart.getData().add(series);
    }

    @Override
    public void update(Event event) {
        //notify stuff
    }

    @FXML
    private void doLogout() throws Exception {
        viewAssistant.doLogout(logoutButton.getScene(), service);
    }

    private void displayMedie() {
        getLoggedInStudent();

        double medie = Utils.computeMedie(loggedInStudent, service.findAllNote(), service.findAllTeme());

        medieCurenta.setText("Media ta curenta la laborator este " + medie);
    }

    private void getLoggedInStudent() {
        String emailProf = viewAssistant.getUsername();

        ObservableList<Student> students = FXCollections.observableList(StreamSupport.stream(service.findAllStudents().spliterator(), false).collect(Collectors.toList()));
        for(int i = 0; i < students.size(); i++)
            if (students.get(i).getEmail().equals(emailProf))
                loggedInStudent = students.get(i);
    }
}
