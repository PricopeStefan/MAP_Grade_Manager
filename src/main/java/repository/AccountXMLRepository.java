package repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import utils.Pair;
import utils.PasswordHelper;
import utils.Types;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class AccountXMLRepository {
    //static String passwordPath = "C:\\Program Files\\MAP Manager\\pass.xml";
    private static String passwordPath = System.getProperty("user.dir") + "\\pass.xml";

    private static final AccountXMLRepository instance = new AccountXMLRepository();
    private AccountXMLRepository() {}
    
    public static AccountXMLRepository getInstance() {
        return instance;
    }
    
    public void createAccount(String email, String password1, String password2, Types type) throws Exception {

        if(email == null || password1 == null || password2 == null)
            return;

        //!!!TO DO:trebuie validare daca exista deja userul sau nu
        if (containsEmail(email))
            throw new Exception("User deja existent!");
        if (!password1.equals(password2))
            throw new Exception("Parolele nu se potrivesc!");

        String[] splitted = email.split("@");
        if(splitted.length != 2 || password1.length() < 5)
            throw new Exception("Date invalide/insecure!");

        Element root = getRootOfFile();

        if(root == null) {
            createRootOfFile();
            root = getRootOfFile();
        }

        Document document = root.getOwnerDocument();
        Element el = createElementFromUser(document, email, password1, type);
        root.appendChild(el);

        writeDocumentToFile(document);
    }
    public void removeAccount(String email) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document document = dbf.newDocumentBuilder().parse(passwordPath);

        Element root = document.getDocumentElement();
        NodeList accountsNodes = root.getChildNodes();

        for(int i = 0; i < accountsNodes.getLength(); i++) {
            Node accElement = accountsNodes.item(i);
            if (accElement.getNodeType() == Node.ELEMENT_NODE) {
                String emailNode = ((Element)accElement).getElementsByTagName("email").item(0).getTextContent();
                if(email.equals(emailNode)) {
                    root.removeChild(accElement);
                }
            }
        }
        writeDocumentToFile(document);
    }

    private void writeDocumentToFile(Document document) throws Exception {
        try {
            Transformer transformer = TransformerFactory
                    .newInstance()
                    .newTransformer();

            transformer.transform(new DOMSource(document), new StreamResult(passwordPath));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    private boolean containsEmail(String email) throws Exception {
        HashMap<String, Types> allAccounts = loadAccounts();

        if(allAccounts != null)
            return allAccounts.containsKey(email);

        return false;
    }
    private static HashMap<String, Types> loadAccounts() {
        HashMap<String, Types> accounts = new HashMap<>(); //hashmap; cheie-valoare = email-type
        try {
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(passwordPath);

            Element root = document.getDocumentElement();
            NodeList accountsNodes = root.getChildNodes();

            for(int i = 0; i < accountsNodes.getLength(); i++) {
                Node accElement = accountsNodes.item(i);
                if (accElement.getNodeType() == Node.ELEMENT_NODE) {
                    String email = ((Element)accElement).getElementsByTagName("email").item(0).getTextContent();
                    String typeStr = ((Element)accElement).getElementsByTagName("type").item(0).getTextContent();
                    Types type = Types.valueOf(typeStr);
                    accounts.put(email, type);
                }
            }
            return accounts;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static Pair<String, Types> findHashedPassword(String username) {
        try {
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(passwordPath);

            Element root = document.getDocumentElement();
            NodeList accountsNodes = root.getChildNodes();

            for(int i = 0; i < accountsNodes.getLength(); i++) {
                Node accElement = accountsNodes.item(i);
                if (accElement.getNodeType() == Node.ELEMENT_NODE) {
                    String email = ((Element)accElement).getElementsByTagName("email").item(0).getTextContent();
                    String password = ((Element)accElement).getElementsByTagName("password").item(0).getTextContent();
                    String typeStr = ((Element)accElement).getElementsByTagName("type").item(0).getTextContent();
                    Types type = Types.valueOf(typeStr);

                    if(username.equals(email)) {
                        return new Pair<>(password, type);
                    }
                }
            }
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    private static Element createElementFromUser(Document document, String user, String pass, Types type) {
        Element el = document.createElement("user");

        Element email = document.createElement("email");
        Element password = document.createElement("password");
        Element perms = document.createElement("type");

        String hashedPass = PasswordHelper.goHashThePassword(pass);
        System.out.println(hashedPass);

        email.setTextContent(user.toLowerCase());
        password.setTextContent(hashedPass);
        perms.setTextContent(type.toString());

        el.appendChild(email);
        el.appendChild(password);
        el.appendChild(perms);

        return el;
    }
    private static Element getRootOfFile() {
        try {
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(passwordPath);

            return document.getDocumentElement();
        } catch (Exception ex) {
            return null;
        }
    }
    private static void createRootOfFile() {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .newDocument();

            Element root = document.createElement("users");
            document.appendChild(root);

            Transformer transformer = TransformerFactory
                    .newInstance()
                    .newTransformer();

            transformer.transform(new DOMSource(document), new StreamResult(passwordPath));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
