package controller;

import domain.Nota;
import domain.Profesor;
import domain.Student;
import domain.Tema;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import repository.*;
import service.Service;
import utils.*;
import validators.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class Login {
    @FXML
    private TextField userField;

    @FXML
    private PasswordField passField;

    @FXML
    private Button loginButton;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        statusLabel.setManaged(false);
        statusLabel.setVisible(false);

        userField.textProperty().addListener(((observable, oldValue, newValue) -> clearError()));
        passField.textProperty().addListener(((observable, oldValue, newValue) -> clearError()));

    }

    @FXML
    public void checkLogin() throws Exception
    {
//        AccountXMLRepository repo = AccountXMLRepository.getInstance();
//        repo.createAccount("admin@admin", "admin", "admin", Types.ADMIN);
//        repo.createAccount("admin1@admin1", "admin1", "admin1", Types.ADMIN);
//        repo.createAccount("admin2@admin2", "admin2", "admin2", Types.ADMIN);
//        repo.createAccount("prof1@prof1", "123456", "123456", Types.PROFESOR);
//        repo.createAccount("prof2@prof2", "prof2", "prof2", Types.PROFESOR);
//        repo.createAccount("prof3@prof3", "prof3", "prof3", Types.PROFESOR);
//        repo.createAccount("student1@student1", "123456", "123456", Types.STUDENT);
//        repo.createAccount("student2@student2", "123456", "123456", Types.STUDENT);
//        repo.createAccount("student3@student3", "123456", "123456", Types.STUDENT);

        /*
        MODURI DE IMPLEMENTARE A USERILOR CU PERMISIUNI DIFERITE:
            - fisierul cu useri(studenti+profi) se afla pe partitia C undeva unde doar un admin de sistem poate scrie fisierele
            - fisierele cu parole sunt encriptate
            - userii se pot crea atunci cand programul este rulat cu permisiuni de admin,dar daca esti doar student
            se va putea citi doar din fisierele alea.
            - se va cauta userul(in fisiere se salveaza username, salt, parola encriptata si elevation_status(profesor/grup/whatever)
         */

//        ViewAssistant viewAssistant = new ViewAssistant("psir2384@scs.ubbcluj.ro", Types.STUDENT);
//
//        viewAssistant.changeScene(loginButton.getScene(), setupService(), viewAssistant, "../fxml/admin_main_view.fxml", 542, 417);

        if(userField.getText().length() > 0 && passField.getText().length() > 0) {
            Pair<String, Types> mainPair = getHashedPassword(userField.getText());


            if (mainPair != null && PasswordHelper.checkPassword(passField.getText(), mainPair.getKey())) {
                ViewAssistant viewAssistant = new ViewAssistant(userField.getText(), mainPair.getValue());
                doLogin(viewAssistant);
            }
            else
                loginError();
        }
    }

    private void clearError() {
        statusLabel.setVisible(false);
        statusLabel.setManaged(false);
    }
    private void loginError() {
        statusLabel.setText("Username/parola invalide!");
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
    }

    private Pair<String, Types> getHashedPassword(String username) throws Exception {
        return AccountXMLRepository.findHashedPassword(username);
    }

    private void doLogin(ViewAssistant viewAssistant) throws Exception
    {
        Service service = setupService();
        switch (viewAssistant.getType()) {
            case STUDENT:
                viewAssistant.changeScene(loginButton.getScene(), service, viewAssistant, "../fxml/student_main_view.fxml", 924,592);
                break;
            case PROFESOR:
                viewAssistant.changeScene(loginButton.getScene(), service, viewAssistant, "../fxml/profesor_main_view.fxml", 844,662);
                break;
            case ADMIN:
                viewAssistant.changeScene(loginButton.getScene(), service, viewAssistant, "../fxml/admin_main_view.fxml", 542, 417);
                break;
            default:
                break;
        }
    }

    private Service setupService() {
        Utils.readWeekFromFile();

        Validator<Student> v = new StudentValidator();
        AbstractXMLRepository<Integer, Student> repo = new StudentXMLRepository("studenti.xml", v);

        Validator<Tema> vTeme = new TemaValidator();
        AbstractXMLRepository<Integer, Tema> repoTeme = new TemeXMLRepository("teme.xml", vTeme);

        Validator<Profesor> vProfi = new ProfesorValidator();
        AbstractXMLRepository<Integer, Profesor> repoProfesori = new ProfesoriXMLRepository("profi.xml", vProfi);

        Validator<Nota> vNote = new NotaValidator();
        AbstractXMLRepository<Pair<Integer, Integer>, Nota> repoNote = new NoteXMLRepository("catalog.xml", vNote);

        Service service = new Service(repo, repoTeme, repoProfesori, repoNote);

        return service;
    }
}
