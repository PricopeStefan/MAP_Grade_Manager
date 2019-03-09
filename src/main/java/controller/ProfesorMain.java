package controller;

import events.Event;
import events.ProfesorEvent;
import events.StudentEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class ProfesorMain extends Controller {
    @FXML
    private Button logoutButton;

    @Override
    protected void setupGeneral() {
        //no setup necessary
    }

    @FXML
    private void setupTemeForm() throws Exception {
        super.viewAssistant.changeScene(logoutButton.getScene(), service, viewAssistant, "../fxml/profesor_teme_view.fxml", 844, 662);
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
    private void doLogout() throws Exception {
        super.viewAssistant.doLogout(logoutButton.getScene(), this.service);
    }

    @Override
    public void update(Event event) {
        if(event instanceof StudentEvent) {
            System.out.println("Student event");
        }
        else if(event instanceof ProfesorEvent) {
            System.out.println("Profesor event,this shouldnt happen");
        }
    }
}
