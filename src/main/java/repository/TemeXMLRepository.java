package repository;

import domain.Profesor;
import domain.Tema;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import validators.Validator;

public class TemeXMLRepository extends AbstractXMLRepository<Integer, Tema> {

    public TemeXMLRepository(String XMLFileName, Validator vali) {
        super(XMLFileName, vali);
    }

    @Override
    protected Tema createRepoItemFromElement(Element TemaElement) {
        Integer TemaID = Integer.parseInt(TemaElement.getAttribute("id"));

        String descriere = TemaElement.getElementsByTagName("descriere").item(0).getTextContent();
        String saptPrimire = TemaElement.getElementsByTagName("saptPrimire").item(0).getTextContent();
        String deadline = TemaElement.getElementsByTagName("deadline").item(0).getTextContent();

        Tema t = new Tema(TemaID, descriere, Integer.valueOf(deadline), Integer.valueOf(saptPrimire));

        return t;
    }

    @Override
    protected Element createElementFromRepoItem(Document document, Tema t) {
        Element el = document.createElement("Tema");

        el.setAttribute("id", t.getID().toString());

        Element descriere = document.createElement("descriere");
        descriere.setTextContent(t.getDescriere());

        Element saptPrimire = document.createElement("saptPrimire");
        saptPrimire.setTextContent(t.getDataPrimire().toString());

        Element deadline = document.createElement("deadline");
        deadline.setTextContent(t.getDeadLine().toString());

        el.appendChild(descriere);
        el.appendChild(saptPrimire);
        el.appendChild(deadline);

        return el;
    }

}