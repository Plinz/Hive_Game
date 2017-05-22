/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.engine;

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
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public final class OptionManager {
    
    private static int resolution = 0;
    private static boolean fullscreen = false;
    private static boolean animationsEnable = false;
    private static boolean helpEnable = true;
    private static boolean gridEnable = true;
    
    public static void init() throws IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException, SAXException{
        File file = new File("Hive_init/init.xml");
        if(file.exists()){
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

    public static boolean isAnimationsEnable() {
        return animationsEnable;
    }

    public static void setAnimationsEnable(boolean animationsEnable) {
        OptionManager.animationsEnable = animationsEnable;
    }
    
    public static boolean isHelpEnable() {
		return helpEnable;
	}

	public static void setHelpEnable(boolean helpEnable) {
		OptionManager.helpEnable = helpEnable;
	}

	public static boolean isGridEnable() {
		return gridEnable;
	}

	public static void setGridEnable(boolean gridEnable) {
		OptionManager.gridEnable = gridEnable;
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
        
        res = (Element) racine.getElementsByTagName("animations").item(0); 
        animationsEnable = Integer.parseInt(res.getTextContent()) != 0;
        
        res = (Element) racine.getElementsByTagName("grid").item(0);
        gridEnable = Integer.parseInt(res.getTextContent()) != 0;

    }               


    
    private static void createAndInitializeFile(File file) throws IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException{
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
        
        Element animations = doc.createElement("animations");
        animations.appendChild(doc.createTextNode("0"));
        rootElement.appendChild(animations);
        
        Element grid = doc.createElement("grid");
        grid.appendChild(doc.createTextNode("1"));
        rootElement.appendChild(grid);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);		
        StreamResult result = new StreamResult(new File("Hive_init/init.xml"));
    
        transformer.transform(source, result);
    }
    
    public static void modifyOptions(int res, boolean fs, boolean help, boolean anim, boolean grid) throws SAXException, IOException, ParserConfigurationException, TransformerException{
        resolution = res;
        fullscreen = fs;
        helpEnable = help;
        animationsEnable = anim;
        gridEnable = grid;
        
        String filepath = "Hive_init/init.xml";
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(filepath);
        
        Node n = doc.getElementsByTagName("resolution").item(0);
        n.setTextContent(String.valueOf(res));
        
        n = doc.getElementsByTagName("fullscreen").item(0);
        if(fs)
            n.setTextContent("1");
        else
            n.setTextContent("0");
        
        n = doc.getElementsByTagName("help").item(0);
        if(help)
            n.setTextContent("1");
        else
            n.setTextContent("0");
        
        n = doc.getElementsByTagName("animations").item(0);
        if(anim)
            n.setTextContent("1");
        else
            n.setTextContent("0");
        
        n = doc.getElementsByTagName("grid").item(0);
        if(grid)
            n.setTextContent("1");
        else
            n.setTextContent("0");
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filepath));
        transformer.transform(source, result);
    }
 }
