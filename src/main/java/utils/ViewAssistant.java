package utils;

import controller.Controller;
import controller.Login;
import domain.Student;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import service.Service;

public class ViewAssistant {
    private String username;
    private Types type;

    public ViewAssistant(String username, Types type) {
        this.username = username;
        this.type = type;
    }

    public String getUsername() {
        return this.username;
    }

    public Types getType() { return  this.type; }

    public void doLogout(Scene currentScene, Service currentService) throws Exception {
        Stage stage = (Stage)currentScene.getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/login_view.fxml"));
        Pane myPane = (Pane) loader.load();
        Login ctrlLogin = loader.getController();

        Scene myScene = new Scene(myPane, 323, 285);
        stage.setScene(myScene);
        stage.show();
    }

    public void changeScene(Scene currentScene, Service currentService, ViewAssistant currentViewAssistant, String fxmlPath, int width, int height) throws Exception {
        Stage stage = (Stage)currentScene.getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Pane myPane = (Pane) loader.load();
        Controller controller = loader.getController();

        controller.setService(currentService);
        controller.setLogin(currentViewAssistant);

        Scene myScene = new Scene(myPane, width, height);
        stage.setScene(myScene);
    }

    public Alert createErrorBox(String eroare) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Eroare");
        alert.setContentText(eroare);
        alert.show();

        return alert;
    }

    public Alert createNotificationBox(String mesaj) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.show();

        return alert;
    }

    public void setupStudentTableView(TableView<Student> studentTableView) {
        studentTableView.getColumns().clear();

        studentTableView.getColumns().add(new TableColumn<>("ID"));
        studentTableView.getColumns().add(new TableColumn<>("Nume"));
        studentTableView.getColumns().add(new TableColumn<>("Grupa"));

        studentTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("ID"));
        studentTableView.getColumns().get(0).prefWidthProperty().bind(studentTableView.widthProperty().divide(5));

        studentTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nume"));
        studentTableView.getColumns().get(1).prefWidthProperty().bind(studentTableView.widthProperty().divide(1.825));

        studentTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("grupa"));
        studentTableView.getColumns().get(2).prefWidthProperty().bind(studentTableView.widthProperty().divide(4));
    }
}
