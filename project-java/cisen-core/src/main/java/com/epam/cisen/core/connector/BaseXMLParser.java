package com.epam.cisen.core.connector;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BaseXMLParser {

    private final Document document;
    private final XPath xPath;

    public BaseXMLParser(String xmlStr) throws Exception {
        document = toDocument(xmlStr);
        xPath = XPathFactory.newInstance().newXPath();
    }

    private Document toDocument(String xmlString) throws Exception {
        if (xmlString == null || xmlString.isEmpty()) {
            throw new IllegalArgumentException("xml cannot be empty.");
        }
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        InputStream is = new ByteArrayInputStream(xmlString.getBytes());
        return documentBuilder.parse(is);
    }

    public String get(String path) throws XPathException {
        return xPath.compile(path).evaluate(document);
    }

    public Node getNode(String path) throws XPathException {
        return (Node) xPath.compile(path).evaluate(document, XPathConstants.NODE);
    }

    public String getAttribute(String path, String attribute) throws XPathException {
        Node node = getNode(path);
        return getAttribute(node, attribute);
    }

    private String getAttribute(Node node, String attribute) throws XPathException {
        return node.getAttributes().getNamedItem(attribute).getNodeValue();
    }

    public NodeList getNodeList(String path) throws XPathException {
        return (NodeList) xPath.compile(path).evaluate(document, XPathConstants.NODESET);
    }

    public List<String> getAttributeList(String path, String attribute) throws XPathException {
        NodeList nodeList = getNodeList(path);
        List<String> res = new ArrayList<>(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            res.add(getAttribute(node, attribute));
        }
        return res;
    }
}
