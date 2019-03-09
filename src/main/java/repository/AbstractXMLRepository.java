package repository;

import domain.HasID;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import validators.Validator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public abstract class AbstractXMLRepository<ID, E extends HasID<ID>> extends ConcreteCrudRepository<ID, E> {
    private String XMLFileName;

    public AbstractXMLRepository(String XMLFileName, Validator v) {
        super(v);
        this.XMLFileName = XMLFileName;
        loadData();
    }

    private void loadData() {
        try {
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(this.XMLFileName);

            Element root = document.getDocumentElement();
            NodeList Es = root.getChildNodes();

            for(int i = 0; i < Es.getLength(); i++) {
                Node EElement = Es.item(i);
                if (EElement.getNodeType() == Node.ELEMENT_NODE) {
                    E s = createRepoItemFromElement((Element)EElement);
                    super.save(s);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void writeToFile() {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .newDocument();

            Element root = document.createElement("items");
            document.appendChild(root);

            findAll().forEach(E -> {
                Element EElement = createElementFromRepoItem(document ,E);
                root.appendChild(EElement);
            });

            Transformer transformer = TransformerFactory
                    .newInstance()
                    .newTransformer();

            transformer.transform(new DOMSource(document), new StreamResult(this.XMLFileName));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    protected abstract E createRepoItemFromElement(Element EElement);
    protected abstract Element createElementFromRepoItem(Document document, E s);




    @Override
    public E save(E s) {
        E deReturnat = super.save(s);
        writeToFile();
        return deReturnat;
    }

    @Override
    public E delete(ID id) {
        E deReturnat = super.delete(id);
        writeToFile();
        return deReturnat;
    }

    @Override
    public E update(E s) {
        E deReturnat = super.update(s);
        writeToFile();
        return deReturnat;
    }
}
