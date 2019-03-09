package controller;

import domain.Student;
import events.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.Window;
import utils.FiltrareDupa;
import utils.Pair;

import java.io.File;
import java.util.*;

public class ProfesorFilter extends Controller {
    @FXML
    private Button logoutButton, studentButton, temaButton, perioadaButton, grupaButton;

    @FXML
    private GridPane gridPane;

    @FXML
    private Circle circle;

    @FXML
    private ImageView imageView;
    private String filePath;

    private Button selectedButton;

    @FXML
    public void initialize(){
        gridPane.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            getClosestButton(event);
        });
        gridPane.addEventFilter(MouseEvent.MOUSE_EXITED, event -> clearButtons());

    }

    private void getClosestButton(MouseEvent event) {
        double mouseCenterX = event.getX();
        double mouseCenterY = event.getY();

        Bounds studentBounds = gridPane.sceneToLocal(studentButton.localToScene(studentButton.getBoundsInLocal()));
        double studentCenterX = (studentBounds.getMinX() + studentBounds.getMaxX())/2;
        double studentCenterY = (studentBounds.getMinY() + studentBounds.getMaxY())/2;

        double minDistance = getDistance(mouseCenterX, mouseCenterY, studentCenterX, studentCenterY);
        selectedButton = studentButton;
        filePath = "/img/student.png";

        Bounds temaBounds = gridPane.sceneToLocal(temaButton.localToScene(temaButton.getBoundsInLocal()));
        double temaCenterX = (temaBounds.getMinX() + temaBounds.getMaxX())/2;
        double temaCenterY = (temaBounds.getMinY() + temaBounds.getMaxY())/2;

        double temaDistance = getDistance(mouseCenterX, mouseCenterY, temaCenterX, temaCenterY);

        if (temaDistance < minDistance) {
            selectedButton = temaButton;
            minDistance = temaDistance;
            filePath = "/img/homework.png";
        }


        Bounds perioadaBounds = gridPane.sceneToLocal(perioadaButton.localToScene(perioadaButton.getBoundsInLocal()));
        double perioadaCenterX = (perioadaBounds.getMinX() + perioadaBounds.getMaxX())/2;
        double perioadaCenterY = (perioadaBounds.getMinY() + perioadaBounds.getMaxY())/2;

        double perioadaDistance = getDistance(mouseCenterX, mouseCenterY, perioadaCenterX, perioadaCenterY);

        if (perioadaDistance < minDistance) {
            selectedButton = perioadaButton;
            minDistance = perioadaDistance;
            filePath = "/img/calendar.png";
        }

        Bounds grupaBounds = gridPane.sceneToLocal(grupaButton.localToScene(grupaButton.getBoundsInLocal()));
        double grupaCenterX = (grupaBounds.getMinX() + grupaBounds.getMaxX())/2;
        double grupaCenterY = (grupaBounds.getMinY() + grupaBounds.getMaxY())/2;

        double grupaDistance = getDistance(mouseCenterX, mouseCenterY, grupaCenterX, grupaCenterY);

        if (grupaDistance < minDistance) {
            selectedButton = grupaButton;
            filePath = "/img/combined.png";
        }

        clearButtons();
        Image image = new Image(filePath);
        imageView.setImage(image);

    }

    private void clearButtons() {
        imageView.setImage(null);
    }

    private double getDistance(double aX, double aY, double bX, double bY) {
        return Math.sqrt(Math.pow(bX - aX, 2) + Math.pow(bY - aY, 2));
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
    private void setupAddNotaView() throws Exception {
        super.viewAssistant.changeScene(logoutButton.getScene(), service, viewAssistant, "../fxml/profesor_add_nota_view.fxml", 844, 662);

    }

    @FXML
    private void doLogout() throws Exception {
        viewAssistant.doLogout(logoutButton.getScene(), service);
    }


    private void launchFilter(FiltrareDupa tipFiltrare, List<String> args) throws Exception {
        Stage secondaryStage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/filtered_view.fxml"));
        Pane myPane = (Pane) loader.load();
        Filtrari ctrlFilter = loader.getController();

        ctrlFilter.setService(service, tipFiltrare, args);
        Scene myScene = new Scene(myPane, 600, 627);

        secondaryStage.setScene(myScene);
        secondaryStage.show();
    }

    @FXML
    private void filterByTema() throws Exception {
        List<String> teme = new ArrayList<>();
        service.findAllTeme().forEach(tema -> teme.add(tema.getID().toString() + ")" + tema.getDescriere()));

        ChoiceDialog<String> dialog = new ChoiceDialog<>(teme.get(0), teme);
        dialog.getDialogPane().getStyleClass().add("dialogus");
        dialog.setTitle("Selecteaza tema");
        dialog.setHeaderText(null);
        dialog.setContentText("Afisati toate notele de la tema ");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            Integer idSelectat = Integer.parseInt(result.get().split("\\)")[0]);

            ArrayList<String> params = new ArrayList<>();
            params.add(idSelectat.toString());

            launchFilter(FiltrareDupa.TEMA, params);
        }
    }

    @FXML
    private void filterByStudent() throws Exception {
        TableView<Student> tabel = new TableView<>();
        initStudentsTableView(tabel);

        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Filtreaza dupa student");
        dialog.setHeaderText("Selectati studentul dupa care se va efectua filtrarea");

        ButtonType loginButtonType = new ButtonType("Selecteaza", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(tabel);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return tabel.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        Optional<Student> result = dialog.showAndWait();

        if(result.isPresent()) {
            ArrayList<String> params = new ArrayList<>();
            params.add(result.get().getID().toString());

            launchFilter(FiltrareDupa.STUDENT, params);
        }
    }

    @FXML
    private void filterByGrupaAndTema() throws Exception {
        Set<String> listaGrupe = new HashSet<>();
        service.findAllStudents().forEach(student -> listaGrupe.add(student.getGrupa()));

        List<String> listaTeme = new ArrayList<>();
        service.findAllTeme().forEach(tema -> listaTeme.add(tema.getID().toString() + ")" + tema.getDescriere()));

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Filtreaza dupa grupa si tema");
        dialog.setHeaderText("Selectati grupa si tema dupa care se va efectua filtrarea");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<String> grupa = new ComboBox<>();
        grupa.getItems().addAll(listaGrupe);
        ComboBox<String> tema = new ComboBox<>();
        tema.getItems().addAll(listaTeme);

        grid.add(new Label("Grupa:"), 0, 0);
        grid.add(grupa, 1, 0);
        grid.add(new Label("Tema:"), 0, 1);
        grid.add(tema, 1, 1);

        ButtonType loginButtonType = new ButtonType("Filtreaza", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(grupa.getSelectionModel().getSelectedItem(), tema.getSelectionModel().getSelectedItem());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        if(result.isPresent()) {
            ArrayList<String> params = new ArrayList<>();

            Integer idSelectat = Integer.parseInt(result.get().getValue().split("\\)")[0]);

            params.add(result.get().getKey());
            params.add(idSelectat.toString());

            launchFilter(FiltrareDupa.GRUPA_TEMA, params);
        }
    }

    @FXML
    private void filterByPerioada() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Filtreaza dupa grupa si tema");
        dialog.setHeaderText("Selectati grupa si tema dupa care se va efectua filtrarea");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField saptStart = new TextField();
        TextField saptEnd = new TextField();

        grid.add(new Label("Saptamana start:"), 0, 0);
        grid.add(saptStart, 1, 0);
        grid.add(new Label("Saptamana sfarsit:"), 0, 1);
        grid.add(saptEnd, 1, 1);

        ButtonType loginButtonType = new ButtonType("Filtreaza", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(saptStart.getText(), saptEnd.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        if(result.isPresent()) {
            ArrayList<String> params = new ArrayList<>();

            params.add(result.get().getKey());
            params.add(result.get().getValue());

            try {
                launchFilter(FiltrareDupa.PERIOADA, params);
            } catch (Exception ex) {
                viewAssistant.createErrorBox(ex.getMessage());
            }
        }
    }

    private void initStudentsTableView(TableView<Student> studentTableView) {
        studentTableView.getItems().clear();

        super.viewAssistant.setupStudentTableView(studentTableView);

        for (Student st:service.findAllStudents())
            studentTableView.getItems().add(st);
    }


    protected void setupGeneral() {
        //do something
    }

    @Override
    public void update(Event event) {
        //do update
    }

    
}
