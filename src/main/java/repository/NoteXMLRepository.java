package repository;

import domain.Nota;
import domain.Profesor;
import domain.Student;
import domain.Tema;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import utils.Pair;
import validators.Validator;

public class NoteXMLRepository extends AbstractXMLRepository<Pair<Integer, Integer>, Nota> {

    public NoteXMLRepository(String XMLFileName, Validator vali) {
        super(XMLFileName, vali);
    }


    protected Nota createRepoItemFromElement(Element notaElement) {
        String[] splitted = notaElement.getAttribute("id").split(",");
        Pair<Integer, Integer> ids = new Pair<>();
        if(splitted.length == 2) {
            ids.setKey(Integer.valueOf(splitted[0]));
            ids.setValue(Integer.valueOf(splitted[1]));
        }


        Student s = getStudent(ids, notaElement);
        Tema t = getTema(ids, notaElement);
        Profesor p = getProfesor(notaElement);

        String valoare = notaElement.getElementsByTagName("valoare").item(0).getTextContent();
        String predataPe = notaElement.getElementsByTagName("predataPe").item(0).getTextContent();

        return new Nota(s, t, Integer.valueOf(valoare), Integer.valueOf(predataPe), p);
    }

    protected Element createElementFromRepoItem(Document document, Nota nota) {
        String ids = nota.getID().toString();

        Element el = document.createElement("nota");
        el.setAttribute("id", ids);

        Element numeStudent = document.createElement("numeStudent");
        numeStudent.setTextContent(nota.getStudent().getNume());

        Element grupaStudent = document.createElement("grupaStudent");
        grupaStudent.setTextContent(nota.getStudent().getGrupa());

        Element emailStudent = document.createElement("emailStudent");
        emailStudent.setTextContent(nota.getStudent().getEmail());


        Element descriere = document.createElement("descriere");
        descriere.setTextContent(nota.getTema().getDescriere());

        Element saptPrimire = document.createElement("saptPrimire");
        saptPrimire.setTextContent(nota.getTema().getDataPrimire().toString());

        Element deadline = document.createElement("deadline");
        deadline.setTextContent(nota.getTema().getDeadLine().toString());


        Element idProfesor = document.createElement("idProfesor");
        idProfesor.setTextContent(nota.getProfesor().getID().toString());

        Element numeProfesor = document.createElement("numeProfesor");
        numeProfesor.setTextContent(nota.getProfesor().getNume());

        Element emailProfesor = document.createElement("emailProfesor");
        emailProfesor.setTextContent(nota.getProfesor().getEmail());


        Element valoare = document.createElement("valoare");
        valoare.setTextContent(nota.getValoare().toString());

        Element predataPe = document.createElement("predataPe");
        predataPe.setTextContent(nota.getPredataPe().toString());

        el.appendChild(numeStudent);
        el.appendChild(grupaStudent);
        el.appendChild(emailStudent);

        el.appendChild(descriere);
        el.appendChild(deadline);
        el.appendChild(saptPrimire);

        el.appendChild(idProfesor);
        el.appendChild(numeProfesor);
        el.appendChild(emailProfesor);

        el.appendChild(valoare);
        el.appendChild(predataPe);

        return el;
    }

    private Student getStudent(Pair<Integer, Integer> ids, Element notaElement) {
        Integer studentID = ids.getKey();

        String numeStudent = notaElement.getElementsByTagName("numeStudent").item(0).getTextContent();
        String grupaStudent = notaElement.getElementsByTagName("grupaStudent").item(0).getTextContent();
        String emailStudent = notaElement.getElementsByTagName("emailStudent").item(0).getTextContent();

        return new Student(studentID, numeStudent, grupaStudent, emailStudent);
    }

    private Tema getTema(Pair<Integer, Integer> ids, Element notaElement) {
        Integer temaID = ids.getValue();

        String descriere = notaElement.getElementsByTagName("descriere").item(0).getTextContent();
        String saptPrimire = notaElement.getElementsByTagName("saptPrimire").item(0).getTextContent();
        String deadline = notaElement.getElementsByTagName("deadline").item(0).getTextContent();

        return new Tema(temaID, descriere, Integer.valueOf(deadline), Integer.valueOf(saptPrimire));
    }

    private Profesor getProfesor(Element notaElement) {
        String idProfesor = notaElement.getElementsByTagName("idProfesor").item(0).getTextContent();
        String numeProfesor = notaElement.getElementsByTagName("numeProfesor").item(0).getTextContent();
        String emailProfesor = notaElement.getElementsByTagName("emailProfesor").item(0).getTextContent();

        return new Profesor(Integer.valueOf(idProfesor), numeProfesor, emailProfesor);
    }

}
