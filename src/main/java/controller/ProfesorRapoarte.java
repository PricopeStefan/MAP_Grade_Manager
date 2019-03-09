package controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;

import domain.Nota;
import domain.Student;
import domain.Tema;
import events.Event;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static utils.Utils.computeMedie;

public class ProfesorRapoarte extends Controller {
    @FXML
    private Button logoutButton, savePDFButton, button1, button2, button3, button4;

    @FXML
    private GridPane gridPane;

    @FXML
    private void canEnter() {
        ObservableList<Student> students = FXCollections.observableList(StreamSupport.stream(service.findAllStudents().spliterator(), false).collect(Collectors.toList()));
        Predicate<Student> mediePreaMica = s -> computeMedie(s, service.findAllNote(), service.findAllTeme()) < 4;

        students.removeIf(mediePreaMica);
        setupStudentTable(students);
        savePDFButton.setOnAction(event -> saveStudentsPDF(students, "admisiExamen.pdf"));

        resetButtons();
        button2.setStyle("-fx-background-color: #6cbfce");
    }

    private void setupStudentTable(ObservableList<Student> students) {
        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 0);

        TableView<Student> studentTableView = new TableView<>();
        viewAssistant.setupStudentTableView(studentTableView);
        studentTableView.getStyleClass().add("tableView");

        studentTableView.setItems(students);
        gridPane.add(studentTableView, 0, 0, 2, 1);
    }

    private String getDestination(String filename) throws Exception {
        Stage stage = (Stage)logoutButton.getScene().getWindow();

        FileChooser directoryChooser = new FileChooser();
        File defaultDir = new File(System.getProperty("user.dir"));
        directoryChooser.setInitialFileName(filename);
        directoryChooser.setInitialDirectory(defaultDir);
        File selectedPath = directoryChooser.showSaveDialog(stage);


        if(selectedPath == null){
            throw new Exception("Nu a fost selectat niciun folder!");
        }else{
            System.out.println(selectedPath.getAbsolutePath());
        }
        return selectedPath.getAbsolutePath();
    }
    private void saveStudentsPDF(ObservableList<Student> students, String filename) {
        try {
            String dest = getDestination(filename);

            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);
            float [] pointColumnWidths = {150F, 300F, 150F, 150F};

            Table table = new Table(pointColumnWidths);

            table.addCell(new Cell().add("ID"));
            table.addCell(new Cell().add("Nume"));
            table.addCell(new Cell().add("Email"));
            table.addCell(new Cell().add("Grupa"));

            students.forEach(s -> {
                table.addCell(new Cell().add(s.getID().toString()));
                table.addCell(new Cell().add(s.getNume()));
                table.addCell(new Cell().add(s.getEmail()));
                table.addCell(new Cell().add(s.getGrupa()));
            });
            doc.add(table);
            doc.close();

            viewAssistant.createNotificationBox("Documentul a fost salvat cu succes!");
        } catch (Exception ex) {
            viewAssistant.createErrorBox(ex.getMessage());
        }
    }


    @FXML
    private void turnedOnTime() {
        ObservableList<Student> students = FXCollections.observableList(StreamSupport.stream(service.findAllStudents().spliterator(), false).collect(Collectors.toList()));
        Predicate<Student> nepredatLaTimp = s -> nuAPredatLaTimp(s);

        students.removeIf(nepredatLaTimp);

        setupStudentTable(students);
        savePDFButton.setOnAction(event -> saveStudentsPDF(students, "predatLaTimp.pdf"));

        resetButtons();
        button3.setStyle("-fx-background-color: #6cbfce");
    }

    private boolean nuAPredatLaTimp(Student s) {
        ObservableList<Nota> note = FXCollections.observableList(StreamSupport.stream(service.findAllNote().spliterator(), false).collect(Collectors.toList()));
        Predicate<Nota> nuEsteAStudentului = n -> !n.getStudent().equals(s);

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

        Predicate<Tema> incaNuAFostDeadline = t -> t.getDeadLine() >= Utils.getCurrentWeek();
        teme.removeIf(incaNuAFostDeadline);

        return teme.size() > 0; // > 0 == true, adica nu a predat la timp tot, false in caz contrar
    }

    @FXML
    private void labGrade() {
        ObservableList<Student> students = FXCollections.observableList(StreamSupport.stream(service.findAllStudents().spliterator(), false).collect(Collectors.toList()));

        TableView<Row> gradeStudentTableView = new TableView<>();

        makeColumns(gradeStudentTableView);

        students.forEach(s -> {
            Row r = new Row();

            r.getCells().add(new Cell2(s.getID().toString()));
            r.getCells().add(new Cell2(s.getNume()));
            r.getCells().add(new Cell2(s.getGrupa()));
            r.getCells().add(new Cell2(s.getEmail()));
            r.getCells().add(new Cell2(String.valueOf(computeMedie(s, service.findAllNote(), service.findAllTeme()))));

            gradeStudentTableView.getItems().add(r);
        });

        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 0);

        gridPane.add(gradeStudentTableView, 0, 0, 2, 1);

        savePDFButton.setOnAction(event -> saveGradesPDF(students, "grades.pdf"));
        resetButtons();
        button1.setStyle("-fx-background-color: #6cbfce");
    }
    private void makeColumns(TableView<Row> tableView) {
        tableView.getColumns().clear();
        tableView.getStyleClass().add("tableView");

        TableColumn<Row, String> idCol = new TableColumn<>("ID");
        TableColumn<Row, String> numeCol = new TableColumn<>("Nume");
        TableColumn<Row, String> grupaCol = new TableColumn<>("Grupa");
        TableColumn<Row, String> emailCol = new TableColumn<>("Email");
        TableColumn<Row, String> notaCol = new TableColumn<>("Nota");

        idCol.setCellValueFactory(param -> {
            List<Cell2> cells = param.getValue().getCells();
            return new SimpleStringProperty(cells.get(0).toString());
        });
        idCol.prefWidthProperty().bind(tableView.widthProperty().divide(6));
        numeCol.setCellValueFactory(param -> {
            List<Cell2> cells = param.getValue().getCells();
            return new SimpleStringProperty(cells.get(1).toString());
        });
        numeCol.prefWidthProperty().bind(tableView.widthProperty().divide(4));
        grupaCol.setCellValueFactory(param -> {
            List<Cell2> cells = param.getValue().getCells();
            return new SimpleStringProperty(cells.get(2).toString());
        });
        grupaCol.prefWidthProperty().bind(tableView.widthProperty().divide(6));
        emailCol.setCellValueFactory(param -> {
            List<Cell2> cells = param.getValue().getCells();
            return new SimpleStringProperty(cells.get(3).toString());
        });
        emailCol.prefWidthProperty().bind(tableView.widthProperty().divide(4));

        notaCol.setCellValueFactory(param -> {
            List<Cell2> cells = param.getValue().getCells();
            return new SimpleStringProperty(cells.get(4).toString());
        });
        notaCol.prefWidthProperty().bind(tableView.widthProperty().divide(6.4));

        tableView.getColumns().add(idCol);
        tableView.getColumns().add(numeCol);
        tableView.getColumns().add(grupaCol);
        tableView.getColumns().add(emailCol);
        tableView.getColumns().add(notaCol);
    }
    private void saveGradesPDF(ObservableList<Student> students, String filename) {
        try {
            String dest = getDestination(filename);

            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);
            float [] pointColumnWidths = {150F, 300F, 150F, 150F, 150F};

            Table table = new Table(pointColumnWidths);

            table.addCell(new Cell().add("ID"));
            table.addCell(new Cell().add("Nume"));
            table.addCell(new Cell().add("Email"));
            table.addCell(new Cell().add("Grupa"));
            table.addCell(new Cell().add("Media"));

            students.forEach(s -> {
                table.addCell(new Cell().add(s.getID().toString()));
                table.addCell(new Cell().add(s.getNume()));
                table.addCell(new Cell().add(s.getEmail()));
                table.addCell(new Cell().add(s.getGrupa()));
                table.addCell(new Cell().add(String.valueOf(computeMedie(s, service.findAllNote(), service.findAllTeme()))));
            });
            doc.add(table);
            doc.close();

            viewAssistant.createNotificationBox("Documentul a fost salvat cu succes!");
        } catch (Exception ex) {
            viewAssistant.createErrorBox(ex.getMessage());
        }
    }

    @FXML
    private void withMissingHomework() {
        ObservableList<Student> students = FXCollections.observableList(StreamSupport.stream(service.findAllStudents().spliterator(), false).collect(Collectors.toList()));
        Predicate<Student> predatLaTimp = s -> !nuAPredatLaTimp(s);

        students.removeIf(predatLaTimp);

        setupStudentTable(students);
        savePDFButton.setOnAction(event -> saveStudentsPDF(students, "temeLipsa.pdf"));

        resetButtons();
        button4.setStyle("-fx-background-color: #6cbfce");
    }

    private void resetButtons() {
        button1.setStyle("-fx-background-color: #EDDFED");
        button2.setStyle("-fx-background-color: #EDDFED");
        button3.setStyle("-fx-background-color: #EDDFED");
        button4.setStyle("-fx-background-color: #EDDFED");
    }

    @Override
    protected void setupGeneral() {

    }

    @FXML
    private void doLogout() throws Exception {
        viewAssistant.doLogout(logoutButton.getScene(), service);
    }

    @FXML
    private void setupTemeForm() throws Exception {
        super.viewAssistant.changeScene(logoutButton.getScene(), service, viewAssistant, "../fxml/profesor_teme_view.fxml", 844, 662);
    }

    @FXML
    private void setupNoteForm() throws Exception {
        super.viewAssistant.changeScene(logoutButton.getScene(), service, viewAssistant, "../fxml/profesor_view_note.fxml", 844, 662);
    }

    @Override
    public void update(Event event) {
        System.out.println("da");
    }


    static class Row
    {
        private final List<Cell2> list = new ArrayList<>();

        public List<Cell2> getCells()
        {
            return list;
        }
    }

    static class Cell2
    {
        private final String value;

        public Cell2(String value)
        {
            this.value = value;
        }

        @Override
        public String toString()
        {
            return value;
        }
    }
}
