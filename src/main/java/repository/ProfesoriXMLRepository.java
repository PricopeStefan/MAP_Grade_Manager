package repository;

import domain.Profesor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import validators.Validator;


public class ProfesoriXMLRepository extends AbstractXMLRepository<Integer, Profesor> {

    public ProfesoriXMLRepository(String XMLFileName, Validator vali) {
        super(XMLFileName, vali);
    }

    @Override
    protected Profesor createRepoItemFromElement(Element ProfesorElement) {
        Integer ProfesorID = Integer.parseInt(ProfesorElement.getAttribute("id"));

        String nume = ProfesorElement.getElementsByTagName("nume").item(0).getTextContent();
        String email = ProfesorElement.getElementsByTagName("email").item(0).getTextContent();

        Profesor s = new Profesor(ProfesorID, nume, email);

        return s;
    }

    @Override
    protected Element createElementFromRepoItem(Document document, Profesor s) {
        Element el = document.createElement("Profesor");

        el.setAttribute("id", s.getID().toString());

        Element nume = document.createElement("nume");
        nume.setTextContent(s.getNume());

        Element email = document.createElement("email");
        email.setTextContent(s.getEmail());

        el.appendChild(nume);
        el.appendChild(email);

        return el;
    }

}
