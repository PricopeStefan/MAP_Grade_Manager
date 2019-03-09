package repository;

import domain.Student;
import validators.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class StudentXMLRepository extends AbstractXMLRepository<Integer, Student> {
    public StudentXMLRepository(String XMLFileName, Validator vali) {
        super(XMLFileName, vali);
    }

    @Override
    protected Student createRepoItemFromElement(Element studentElement) {
        Integer studentID = Integer.parseInt(studentElement.getAttribute("id"));

        String nume = studentElement.getElementsByTagName("nume").item(0).getTextContent();
        String grupa = studentElement.getElementsByTagName("grupa").item(0).getTextContent();
        String email = studentElement.getElementsByTagName("email").item(0).getTextContent();

        Student s = new Student(studentID, nume, grupa, email);

        return s;
    }

    @Override
    protected Element createElementFromRepoItem(Document document, Student s) {
        Element el = document.createElement("student");

        el.setAttribute("id", s.getID().toString());

        Element nume = document.createElement("nume");
        nume.setTextContent(s.getNume());

        Element email = document.createElement("email");
        email.setTextContent(s.getEmail());

        Element grupa = document.createElement("grupa");
        grupa.setTextContent(s.getGrupa());


        el.appendChild(nume);
        el.appendChild(email);
        el.appendChild(grupa);

        return el;
    }

}
