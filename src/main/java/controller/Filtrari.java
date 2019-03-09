package controller;

import domain.Nota;
import events.Event;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import service.Service;
import utils.FiltrareDupa;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Filtrari extends Controller {
    @FXML
    private TableView<Nota> tableView;

    @FXML
    private TableColumn<Nota, String> SID, Nume, Grupa, TID, Tema, PredataPe, Nota;

    @FXML
    private VBox vBox;
    //private BarChart<String, Number> barChart;

    private FiltrareDupa tip;
    private Service service;
    private List<String> params;

    @FXML
    public void initialize() {

    }

    private void initTableView() {
        tableView.getItems().clear();

        SID.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<domain.Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                StringProperty id = new SimpleStringProperty(param.getValue().getStudent().getID().toString());
                return id;
            }
        });
        Nume.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                StringProperty id = new SimpleStringProperty(param.getValue().getStudent().getNume());
                return id;
            }
        });
        Grupa.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                StringProperty id = new SimpleStringProperty(param.getValue().getStudent().getGrupa());
                return id;
            }
        });
        TID.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                StringProperty id = new SimpleStringProperty(param.getValue().getTema().getID().toString());
                return id;
            }
        });
        Tema.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                StringProperty id = new SimpleStringProperty(param.getValue().getTema().getDescriere());
                return id;
            }
        });
        PredataPe.setCellValueFactory(new PropertyValueFactory<Nota, String>("predataPe"));
        Nota.setCellValueFactory(new PropertyValueFactory<Nota, String>("valoare"));

        startFiltering();
    }

    private void startFiltering() {
        tableView.getItems().clear();

        switch (this.tip) {
            case TEMA:
                filterByTema();
                break;
            case STUDENT:
                filterByStudent();
                break;
            case GRUPA_TEMA:
                filterByGrupaAndTema();
                break;
            case PERIOADA:
                filterByPeriod();
                break;
            default:
                System.out.println("Nu se poate face filtrarea dupa parametrii dati!");
                break;
        }
    }

    private void filterByTema() {
        if(params.size() == 1) {
            Integer temaID = Integer.parseInt(params.get(0));
            Predicate<Nota> notSameID = t -> t.getTema().getID() != temaID;

            ObservableList<Nota> all = FXCollections.observableList(StreamSupport.stream(service.findAllNote().spliterator(), false).collect(Collectors.toList()));
            all.removeIf(notSameID);

            tableView.getItems().addAll(all);
            displayStudentsAndNotaChart(all);
        }
    }
    private void displayStudentsAndNotaChart(ObservableList<Nota> all) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setUpperBound(all.size());
        final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        xAxis.setLabel("Nota");
        yAxis.setLabel("Numar studenti");

        int[] date = new int[11];
        for(int i = 0; i <= 10; i++) date[i] = 0;
        all.forEach(nota -> {
            date[nota.getValoare()]++;
        });

        XYChart.Series series = new XYChart.Series();
        series.setName("Numar note");
        for(int i = 1; i <= 10; i++)
            series.getData().add(new XYChart.Data(String.valueOf(i), date[i]));

        barChart.getData().add(series);
        if(vBox.getChildren().size() == 2)
            vBox.getChildren().remove(1);
        vBox.getChildren().add(barChart);
    }

    private void filterByStudent() {
        if(params.size() == 1) {
            Integer studentID = Integer.parseInt(params.get(0));
            Predicate<Nota> notSameID = t -> t.getStudent().getID() != studentID;

            ObservableList<Nota> all = FXCollections.observableList(StreamSupport.stream(service.findAllNote().spliterator(), false).collect(Collectors.toList()));
            all.removeIf(notSameID);

            tableView.getItems().addAll(all);
            displayStudentBarChart(all);
        }
    }
    private void displayStudentBarChart(ObservableList<Nota> all) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setUpperBound(10);
        final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        xAxis.setLabel("ID Tema");
        yAxis.setLabel("Nota");

        Collections.sort(all, new Comparator<domain.Nota>() {
            @Override
            public int compare(domain.Nota o1, domain.Nota o2) {
                return o1.getTema().getID().compareTo(o2.getTema().getID());
            }
        });
        XYChart.Series series = new XYChart.Series();
        series.setName("Nota");
        all.forEach(nota -> {
            series.getData().add(new XYChart.Data(String.valueOf(nota.getTema().getID()), nota.getValoare()));
        });

        barChart.getData().add(series);
        if(vBox.getChildren().size() == 2)
            vBox.getChildren().remove(1);
        vBox.getChildren().add(barChart);
    }

    private void filterByGrupaAndTema() {
        System.out.println(params);
        if(params.size() == 2) {
            String grupa = params.get(0);
            Integer temaID = Integer.parseInt(params.get(1));

            Predicate<Nota> notTheSame = t -> !t.getTema().getID().equals(temaID) || !t.getStudent().getGrupa().equals(grupa);

            ObservableList<Nota> all = FXCollections.observableList(StreamSupport.stream(service.findAllNote().spliterator(), false).collect(Collectors.toList()));
            all.removeIf(notTheSame);

            tableView.getItems().addAll(all);
            displayStudentsAndNotaChart(all);
        }
    }

    private void filterByPeriod() {
        if(params.size() == 2) {
            Integer dePe = Integer.parseInt(params.get(0));
            Integer panaPe = Integer.parseInt(params.get(1));

            Predicate<Nota> notFit = t -> t.getPredataPe() < dePe || t.getPredataPe() > panaPe; // e in afara intervalului dat

            ObservableList<Nota> all = FXCollections.observableList(StreamSupport.stream(service.findAllNote().spliterator(), false).collect(Collectors.toList()));
            all.removeIf(notFit);

            tableView.getItems().addAll(all);
            displayStudentsAndNotaChart(all);
        }
    }

    public void setService(Service service, FiltrareDupa tip, List<String> params) {
        this.service = service;
        this.service.addObserver(this);
        this.tip = tip;
        this.params = params;
        initTableView();
    }
    @Override
    protected void setupGeneral() {
        //do nothing
    }
    @Override
    public void update(Event event) {
        startFiltering();
    }
}
