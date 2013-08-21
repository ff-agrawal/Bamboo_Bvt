package com.framework.updatedb;

import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

public class xmlParse {

    /**
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public HashMap<String,String> parseXml(String fileName) throws ParserConfigurationException, SAXException, IOException {
        File fXmlFile = new File(fileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();

        Element rootElement = doc.getDocumentElement();

        NodeList nodes = rootElement.getChildNodes();
        NodeList chNL;
        NodeList chNL2;
        NodeList chNL3;
        NodeList chNL4;
        NodeList chNL5;

        HashMap<String,String> tcList = new HashMap<String,String>();
        String tcDetails;
        String tcName;

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeName().equalsIgnoreCase("suite")) {
                chNL = node.getChildNodes();

                for (int j = 0; j < chNL.getLength(); j++) {
                    Node chNode = chNL.item(j);

                    if (chNode.getNodeName().equalsIgnoreCase("test")) {
                        chNL2 = chNode.getChildNodes();

                        for (int k = 0; k < chNL2.getLength(); k++) {
                            Node chNode2 = chNL2.item(k);

                            if (chNode2.getNodeName().equalsIgnoreCase("class")) {
                                chNL3 = chNode2.getChildNodes();

                                for (int m = 0; m < chNL3.getLength(); m++) {
                                    Node chNode3 = chNL3.item(m);

                                    if (chNode3.getNodeName().equalsIgnoreCase("test-method")) {
                                        if (!(chNode3.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("setup")) && !(chNode3.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("teardown"))) {
                                            tcName = chNode3.getAttributes().getNamedItem("name").getNodeValue();

                                            tcDetails = chNode3.getAttributes().getNamedItem("status").getNodeValue().toString() + "#" + chNode3.getAttributes().getNamedItem("duration-ms").getNodeValue().toString();

                                            if (chNode3.getAttributes().getNamedItem("status").getNodeValue().toString().equalsIgnoreCase("FAIL")) {
                                                chNL4 = chNode3.getChildNodes();

                                                for (int n = 0; n < chNL4.getLength(); n++) {
                                                    Node chNode4 = chNL4.item(n);

                                                    if (chNode4.getNodeName().equalsIgnoreCase("exception")) {

                                                        chNL5 = chNode4.getChildNodes();

                                                        for (int o = 0; o < chNL5.getLength(); o++) {
                                                            Node chNode5 = chNL5.item(o);

                                                            if (chNode5.getNodeName().equalsIgnoreCase("message"))
                                                                tcDetails = tcDetails + "#" + chNode5.getTextContent().trim();
                                                            if (chNode5.getNodeName().equalsIgnoreCase("full-stacktrace"))
                                                                tcDetails = tcDetails + "#" + chNode5.getTextContent().trim();

                                                        }

                                                    }

                                                }
                                            }
                                            tcList.put(tcName, tcDetails);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return tcList;
    }

    public String getBuildName(String fileName) throws ParserConfigurationException, IOException, SAXException {
        File fXmlFile = new File(fileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        Element rootElement = doc.getDocumentElement();

        String suiteName = "";

        NodeList nodes = rootElement.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeName().equalsIgnoreCase("suite")) {
                suiteName = node.getAttributes().getNamedItem("name").getNodeValue().toString();
            }
        }
    return suiteName;
    }
}