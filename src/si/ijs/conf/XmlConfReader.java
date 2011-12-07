package si.ijs.conf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlConfReader implements ConfReader {
	
	private String filename;
	
	public XmlConfReader(String filename)
	{
		this.filename = filename;
	}
	
	@Override
	public HashMap<String, String> read() throws ConfReaderException {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new ConfReaderException("Error creating new document builder", e);
		}
		
		org.w3c.dom.Document xmlDoc;
		try {
			xmlDoc = docBuilder.parse(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			throw new ConfReaderException("Error parsing", e);
		} catch (SAXException e) {
			throw new ConfReaderException("Error parsing", e);
		} catch (IOException e) {
			throw new ConfReaderException("Error parsing", e);
		}
		
		Element topElt = (Element)xmlDoc.getElementsByTagName("config").item(0);
		NodeList nodes = topElt.getChildNodes();
		for (int nodeN = 0; nodeN < nodes.getLength(); nodeN++) {
			if (nodes.item(nodeN) instanceof Element) {
				Element nodeElt = (Element)nodes.item(nodeN);
				resultMap.put(nodeElt.getTagName(), nodeElt.getTextContent());
			}
		}
		
		return resultMap;
	}

}
