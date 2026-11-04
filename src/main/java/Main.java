import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        List<Employee> list = parseXML("data.xml");
        System.out.println("list=" + list);

        String json = listToJson(list);
        System.out.println(json);

        writeString(json);
    }

    public static List<Employee> parseXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(fileName));
        Node root = document.getDocumentElement();
        NodeList rootNodeList = root.getChildNodes();
        for (int i = 0; i < rootNodeList.getLength(); i++) {
            Node node = rootNodeList.item(i);
            NodeList nodeList = node.getChildNodes();
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Employee employee = new Employee();
                for (int j = 0; j < nodeList.getLength(); j++) {
                    Node node_ = nodeList.item(j);
                    switch (node_.getNodeName()) {
                        case "id" -> employee.id = Long.parseLong(node_.getTextContent());
                        case "firstName" -> employee.firstName = node_.getTextContent();
                        case "lastName" -> employee.lastName = node_.getTextContent();
                        case "country" -> employee.country = node_.getTextContent();
                        case "age" -> employee.age = Integer.parseInt(node_.getTextContent());
                    }
                }
                list.add(employee);
            }
        }
        return list;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    public static void writeString(String str) {
        try (FileWriter fileWriter = new FileWriter("data2.json")) {
            fileWriter.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
