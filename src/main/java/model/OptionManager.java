/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.model;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author gontardb
 */

public final class OptionManager {
    
    private static int resolution = 0;
    private static boolean fullscreen = false;
    private static boolean helpEnable = true;
    
    public static void init() throws IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException, SAXException{
        File file = new File("init.xml");
        if(file.exists()){
            System.out.println("Il existe !");
            initByFile(file);
        }
        else{
            createAndInitializeFile(file);
        }
    }

    public static int getResolution() {
        return resolution;
    }

    public static void setResolution(int res) {
        resolution = res;
    }

    public static boolean isFullscreen() {
        return fullscreen;
    }

    public static void setFullscreen(boolean fs) {
        fullscreen = fs;
    }

    public static boolean isHelpEnabled() {
        return helpEnable;
    }

    public static void setHelp(boolean h) {
        helpEnable = h;
    } 
    
    private static void initByFile(File file) throws ParserConfigurationException, SAXException, IOException{
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document document= builder.parse(file);
        final Element racine = document.getDocumentElement();
        
        Element res = (Element) racine.getElementsByTagName("resolution").item(0);        
        resolution = Integer.parseInt(res.getTextContent()); 
        
        res = (Element) racine.getElementsByTagName("fullscreen").item(0);
        fullscreen = Integer.parseInt(res.getTextContent()) != 0;
        
        res = (Element) racine.getElementsByTagName("help").item(0);
        helpEnable = Integer.parseInt(res.getTextContent()) != 0;

        System.out.println("Resolution = " + resolution + " \n Fullscreen =" + fullscreen + " \n Help =" + helpEnable);
    }               


    
    private static void createAndInitializeFile(File file) throws IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException{
        System.out.println("Création du fichier d'options");
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("options");
	doc.appendChild(rootElement);
        
        Element res = doc.createElement("resolution");
        res.appendChild(doc.createTextNode("0"));
        rootElement.appendChild(res);
        
        Element fs = doc.createElement("fullscreen");
        fs.appendChild(doc.createTextNode("0"));
        rootElement.appendChild(fs);
        
        Element help = doc.createElement("help");
        help.appendChild(doc.createTextNode("1"));
        rootElement.appendChild(help);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	Transformer transformer = transformerFactory.newTransformer();
	DOMSource source = new DOMSource(doc);		
        StreamResult result = new StreamResult(new File("init.xml"));
    
        transformer.transform(source, result);
    }
}
