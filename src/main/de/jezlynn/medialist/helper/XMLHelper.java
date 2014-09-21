package main.de.jezlynn.medialist.helper;

import main.de.jezlynn.medialist.exceptions.NoXMLFile;
import main.de.jezlynn.medialist.reference.Reference;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;

/**
 * Created by Michael on 24.08.2014.
 */
public class XMLHelper {

    File xmlFile;

    HashMap<String, String> data = new HashMap<String, String>(1024);

    public XMLHelper(String filePath) throws NoXMLFile {
        if (filePath.contains(".xml") || filePath.contains(".nfo")) {
            xmlFile = new File(filePath);
            this.readXML();
        } else throw new NoXMLFile();
    }

    private void readXML() {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document doc = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(xmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName(Reference.TAG_MOVIE);

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    LogHelper.debug("Description : " + eElement.getElementsByTagName(Reference.TAG_DESCRIPTION).item(0).getTextContent());
                    data.put(Reference.TAG_DESCRIPTION, eElement.getElementsByTagName(Reference.TAG_DESCRIPTION).item(0).getTextContent());
                    LogHelper.debug("FSK : " + eElement.getElementsByTagName(Reference.TAG_FSK).item(0).getTextContent());
                    data.put(Reference.TAG_FSK, eElement.getElementsByTagName(Reference.TAG_FSK).item(0).getTextContent());

                }
            }
        } catch (Exception e) {
            LogHelper.error(e);
        }
    }

    public HashMap<String, String> getData() {
        return data;
    }
}
