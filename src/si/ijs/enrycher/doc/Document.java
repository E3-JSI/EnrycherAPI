package si.ijs.enrycher.doc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A class representing one enrycher document. 
 * 
 * @author Blaz Fortuna
 */
public class Document {
	/** Document ID */
	protected final String id;
	/** Document's Attributes */
	protected final List<Attribute> attributeV; 
	/** Pipeline used to construct current document */
	protected final Pipeline pipeline;
	
	/** List of paragraph contained in the document */
	protected final List<Paragraph> paragraphV;
	protected final Map<Integer, Paragraph> sentIdToParagraphH;
	/** List of annotations */
	protected final Map<Integer, Annotation> annotationH;
	/** List of assertions */
	protected final Map<Integer, Assertion> assertionH;
	
	/** for unique sentence IDs */
	private int nextSentenceId;
	/** for unique everything-else IDs */
	private int nextId;
	
	public Document() {
		// create an empty document
		id = java.util.UUID.randomUUID().toString();
		attributeV = new ArrayList<Attribute>();
		pipeline = new Pipeline();
		paragraphV = new ArrayList<Paragraph>();
		sentIdToParagraphH = new HashMap<Integer, Paragraph>();
		annotationH = new HashMap<Integer, Annotation>();
		assertionH = new HashMap<Integer, Assertion>();
		
		// initialize ID counters
		nextSentenceId = 0;
		nextId = 0;
	}
	
	// meta-data 
	public List<Attribute> getAttributes() {
		return attributeV;		
	}
	
	public Pipeline getPipeline() {
		return pipeline;
	}
	
	// paragraphs
	public boolean hasParagraphs() {
		return (paragraphV.size() > 0);
	}
	
	public List<Paragraph> getParagraphs() {
		return paragraphV;
	}
	
	public Paragraph getParagraph(int sentenceId) {
		return sentIdToParagraphH.get(sentenceId);
	}

	public void addParagraph(Paragraph paragraph) {
		// remember the paragraph
		paragraphV.add(paragraph);
		// assign proper ids to sentences and tokens
		for (Sentence sentence : paragraph.getSentences()) {
			// handle the sentence id
			sentence.setId(nextSentenceId);
			sentIdToParagraphH.put(nextSentenceId, paragraph);
			nextSentenceId++;
			// handle the token ids
			final int tokens = sentence.getTokens().size();
			for (int tokenN = 0; tokenN < tokens; tokenN++) {
				sentence.getTokens().get(tokenN).setId(tokenN);
			}
		}
	}
	
	// annotations
	public boolean hasAnnotations() {
		return (annotationH.size() > 0); 
	}
	
	public Collection<Annotation> getAnnotations() {
		return annotationH.values();
	}
		
	public Annotation getAnnotationById(int annotationId) {
		return annotationH.get(new Integer(annotationId));
	}
	
	public Annotation addAnnotation(String displayName, String type) {
		// get id
		final int annId = nextId; nextId++;
		// create annotation
		Annotation ann = new Annotation(annId, displayName, type);
		// remember annotation
		annotationH.put(annId, ann);
		// return the new annotation
		return ann;
	}

	public void addAnnotationInstance(Annotation ann, Instance inst) {
		inst.setId(nextId); nextId++;
		ann.addInstance(inst);
	}
	
	public void addAnnotationAttribute(Annotation ann, Attribute attr) {
		ann.addAttribute(attr);
	}
	
	// assertions
	public boolean hasAssertions() {
		return (assertionH.size() > 0); 
	}

	public Collection<Assertion> getAssertions(){
		return assertionH.values();
	}
	
	public Assertion getAssertionById(int id){
		return assertionH.get(id);
	}
	
	public void addAssertion(AssertionNode subject, AssertionNode verb, AssertionNode object){
		final int assertId = nextId; nextId++;
		Assertion assertion = new Assertion(assertId, subject, verb, object);
		assertionH.put(assertion.getId(), assertion);
	}
	
	// serialization	
	public Document(File xmlFile) throws EnrycherException {
		// initialize
		attributeV = new ArrayList<Attribute>();
		pipeline = new Pipeline();
		paragraphV = new ArrayList<Paragraph>();
		sentIdToParagraphH = new HashMap<Integer, Paragraph>();
		annotationH = new HashMap<Integer, Annotation>();
		assertionH = new HashMap<Integer, Assertion>();
		// initialize ID counters
		nextSentenceId = 0;
		nextId = 0;
		
		// open XML file
		org.w3c.dom.Document xmlDoc = openXml(xmlFile);	
		// go over the document and load it into object model
		try {			
			// get document ID 
			Element topElt = (Element)xmlDoc.getElementsByTagName("item").item(0);
			id = topElt.getAttribute("id");
			// get rest of the document
			parseDoc(xmlDoc);		
		} catch (Exception e) {
			throw new EnrycherException("Error loading XML document " + xmlFile.getName(), e);
		}
	}
	
	public Document(InputStream xmlInputStream) throws EnrycherException {
		// initialize
		attributeV = new ArrayList<Attribute>();
		pipeline = new Pipeline();
		paragraphV = new ArrayList<Paragraph>();
		sentIdToParagraphH = new HashMap<Integer, Paragraph>();
		annotationH = new HashMap<Integer, Annotation>();
		assertionH = new HashMap<Integer, Assertion>();
		// initialize ID counters
		nextSentenceId = 0;
		nextId = 0;
		
		// open XML stream
		org.w3c.dom.Document xmlDoc = openXml(xmlInputStream);	
		// go over the document and load it into object model
		try {			
			// get document ID 
			Element topElt = (Element)xmlDoc.getElementsByTagName("item").item(0);
			id = topElt.getAttribute("id");
			// get rest of the document
			parseDoc(xmlDoc);		
		} catch (Exception e) {
			throw new EnrycherException("Error loading XML document.", e);
		}
	}
	
	public void updateDocument(InputStream xmlInputStream) throws EnrycherException {
		// reset current structures
		attributeV.clear();
		pipeline.clear();
		paragraphV.clear();
		sentIdToParagraphH.clear();
		annotationH.clear();
		assertionH.clear();
		
		// open XML stream
		org.w3c.dom.Document xmlDoc = openXml(xmlInputStream);	
		// go over the document and load it into object model
		try {			
			// get document ID 
			Element topElt = (Element)xmlDoc.getElementsByTagName("item").item(0);
			if (!id.equals(topElt.getAttribute("id"))) {
				throw new EnrycherException("Cannot overwrite document with different ID!");
			}
			// get rest of the document
			parseDoc(xmlDoc);		
		} catch (Exception e) {
			throw new EnrycherException("Error loading XML document.", e);
		}
	}
	
	private org.w3c.dom.Document openXml(File xmlFile) throws EnrycherException {
		// parse XML document
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new EnrycherException("Error creating DocumentBuilder.", e);
		}
		org.w3c.dom.Document xmlDoc;
		try {
			xmlDoc = docBuilder.parse(xmlFile);
		} catch (SAXException e) {
			throw new EnrycherException("Error parsing XML document " + xmlFile.getName(), e);
		} catch (IOException e) {
			throw new EnrycherException("Error reading XML document " + xmlFile.getName(), e);
		}
		return xmlDoc;
	}
	
	private org.w3c.dom.Document openXml(InputStream xmlInputStream) throws EnrycherException {
		// parse XML document
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new EnrycherException("Error creating DocumentBuilder.", e);
		}
		org.w3c.dom.Document xmlDoc;
		try {
			xmlDoc = docBuilder.parse(xmlInputStream);
		} catch (SAXException e) {
			throw new EnrycherException("Error parsing XML document.", e);
		} catch (IOException e) {
			throw new EnrycherException("Error reading XML document.", e);
		}
		return xmlDoc;
	}		

	private void parseDoc(org.w3c.dom.Document xmlDoc) throws ParseException, EnrycherException {
		Element topElt = (Element)xmlDoc.getElementsByTagName("item").item(0);
		// parse meta-data
		if (topElt.getElementsByTagName("metadata").getLength() > 0) {
			Element metadataElt = (Element)xmlDoc.getElementsByTagName("metadata").item(0);
			// load document level semantic attributes
			if (metadataElt.getElementsByTagName("semantics").getLength() > 0) {					
				Element semanticsElt = (Element)metadataElt.getElementsByTagName("semantics").item(0);
				parseSemantics(semanticsElt, attributeV);
			}
			// load pipeline information
			if (metadataElt.getElementsByTagName("pipeline").getLength() > 0) {
				Element pipelineElt = (Element)metadataElt.getElementsByTagName("pipeline").item(0);
				parsePipeline(pipelineElt);
			}
		}		
		// read text
		if (topElt.getElementsByTagName("text").getLength() > 0) {
			Element textElt = (Element)xmlDoc.getElementsByTagName("text").item(0);
			parseText(textElt);
		}								
		// read annotations
		if (topElt.getElementsByTagName("annotations").getLength() > 0) {
			Element annsElt = (Element)topElt.getElementsByTagName("annotations").item(0);
			parseAnnotations(annsElt, annotationH);
		}		
		// read assertion
		if (topElt.getElementsByTagName("assertions").getLength() > 0) {
			Element assertsElt = (Element)topElt.getElementsByTagName("assertions").item(0);
			parseAssertion(assertsElt);			
		}				
	}

	private void parseAssertion(Element assertsElt) {
		NodeList assertEltV = assertsElt.getElementsByTagName("assertion");
		for (int assertEltN = 0; assertEltN < assertEltV.getLength(); assertEltN++) {
			Element assertElt = (Element)assertEltV.item(assertEltN);
			final int assertId = Integer.parseInt(assertElt.getAttribute("id"));
			// parse assertion nodes
			Element subjectElt = (Element)assertElt.getElementsByTagName("subject").item(0);
			AssertionNode subjectNode = parseAssertionNode(subjectElt);
			Element verbElt = (Element)assertElt.getElementsByTagName("verb").item(0);
			AssertionNode verbNode = parseAssertionNode(verbElt);
			Element objectElt = (Element)assertElt.getElementsByTagName("object").item(0);
			AssertionNode objectNode = parseAssertionNode(objectElt);
			// store assertion
			Assertion assertion = new Assertion(assertId, subjectNode, verbNode, objectNode);
			assertionH.put(assertion.getId(), assertion);
		}
	}

	private AssertionNode parseAssertionNode(Element assertionNodeElt) {
		// get the head
		Annotation ann = getAnnotationById(Integer.parseInt(assertionNodeElt.getAttribute("annotationId")));
		Instance inst = ann.getInstanceById(Integer.parseInt(assertionNodeElt.getAttribute("instanceId")));
		// get modifiers		
		NodeList modifierEltV = assertionNodeElt.getElementsByTagName("modifier");
		List<Annotation> modifierAnnV = new ArrayList<Annotation>();
		List<Instance> modifierInstV = new ArrayList<Instance>();
		for (int modifierEltN = 0; modifierEltN < modifierEltV.getLength(); modifierEltN++) {
			Element modifierElt = (Element)modifierEltV.item(modifierEltN);
			Annotation annModifier = getAnnotationById(Integer.parseInt(modifierElt.getAttribute("annotationId")));
			modifierAnnV.add(annModifier);
			modifierInstV.add(annModifier.getInstanceById(Integer.parseInt(modifierElt.getAttribute("instanceId"))));
		}
		// create assertion node
		return new AssertionNode(ann, inst, modifierAnnV, modifierInstV);
	}

	private void parseAnnotations(Element annsElt, Map<Integer, Annotation> annH)
			throws EnrycherException {
		
		NodeList annEltV = annsElt.getElementsByTagName("annotation");
		for (int annEltN = 0; annEltN < annEltV.getLength(); annEltN++) {
			Element annElt = (Element)annEltV.item(annEltN);
			final int annId = Integer.parseInt(annElt.getAttribute("id"));
			final String annDisplayName = annElt.getAttribute("displayName");
			final String annType = annElt.getAttribute("type");
			Annotation ann = new Annotation(annId, annDisplayName, annType);					 
			// get instances
			if (annElt.getElementsByTagName("instances").getLength() > 0) {
				Element instsElt = (Element)annElt.getElementsByTagName("instances").item(0);				
				parseInstances(instsElt, ann.getInstances());
			}					 
			// get semantic attributes
			if (annElt.getElementsByTagName("semantics").getLength() > 0) {
				Element semanticsElt = (Element)annElt.getElementsByTagName("semantics").item(0);
				parseSemantics(semanticsElt, ann.getAttributes());
			}
			// remember annotation
			annH.put(new Integer(annId), ann);
		}
	}

	private void parseInstances(Element instsElt, List<Instance> instV)
			throws EnrycherException {
		
		NodeList instEltV = instsElt.getElementsByTagName("instance");
		for (int instEltN = 0; instEltN < instEltV.getLength(); instEltN++) {
			Element instElt = (Element)instEltV.item(instEltN);
			// basics
			final int instId = Integer.parseInt(instElt.getAttribute("id"));
			final String instDisplayName = instElt.getAttribute("words");
			// get tokens							 
			NodeList tokenEltV = instElt.getElementsByTagName("token");
			int[] tokenIdV = new int[tokenEltV.getLength()];
			int[] sentenceIdV = new int[tokenEltV.getLength()];
			for (int tokenEltN = 0; tokenEltN < tokenEltV.getLength(); tokenEltN++) {
				Element tokenElt = (Element)tokenEltV.item(tokenEltN);
				String tokenIdStr = tokenElt.getAttribute("id");
				String[] tokenIdStrV = tokenIdStr.split("\\.");
				if (tokenIdStrV.length != 2) {
					throw new EnrycherException("Bad token id " + tokenIdStr);
				}
				sentenceIdV[tokenEltN] = Integer.parseInt(tokenIdStrV[0]);
				tokenIdV[tokenEltN] = Integer.parseInt(tokenIdStrV[1]);								 
			}
			// remember the instance
			Instance inst = new Instance(instDisplayName, tokenIdV, sentenceIdV);
			inst.setId(instId);
			instV.add(inst);
		}
	}

	private void parseSemantics(Element semanticsElt, List<Attribute> attrV) {
		NodeList attrEltV = semanticsElt.getElementsByTagName("attribute");
		for (int attrEltN = 0; attrEltN < attrEltV.getLength(); attrEltN++) {
			Element attrElt = (Element)attrEltV.item(attrEltN);
			// default comment is an empty comment :-)
			String attrType = attrElt.getAttribute("type");
			String displayName = attrElt.getAttribute("displayName");
			// check if the attribute is resource or literal
			if (attrElt.hasAttribute("resource")) {
				attrV.add(Attribute.getResource(attrType,
					attrElt.getAttribute("resource"), displayName));
			} else {
				attrV.add(Attribute.getLiteral(attrType,
					attrElt.getTextContent(), displayName));					
			}
		}
	}

	private void parsePipeline(Element pipelineElt) throws ParseException {
		NodeList toolEltV = pipelineElt.getElementsByTagName("tool");
		for (int toolEltN = 0; toolEltN < toolEltV.getLength(); toolEltN++) {
			Element toolElt = (Element)toolEltV.item(toolEltN);
			String toolName = toolElt.getTextContent();
			String toolTimeStr = toolElt.getAttribute("time");
			Date toolTime = Tool.xsDateTime.parse(toolTimeStr); 
			String toolDurationStr = toolElt.getAttribute("durationTimeMillis");
			if (toolDurationStr.equals("")) { toolDurationStr = "0"; }
			pipeline.addTool(new Tool(toolName, toolTime, 
				Integer.parseInt(toolDurationStr)));
		}
	}

	private void parseText(Element textElt) throws EnrycherException {
		// get all paragraphs
		NodeList paraNodeV = textElt.getChildNodes();
		for (int paraNodeN = 0; paraNodeN < paraNodeV.getLength(); paraNodeN++) {
			Node paraNode = paraNodeV.item(paraNodeN);
			if (paraNode instanceof Element) {
				Element paraElt = (Element)paraNode;
				// parse paragraph
				Paragraph para = new Paragraph(paraElt.getTagName());
				NodeList sentEltV = paraElt.getElementsByTagName("sentence");
				for (int sentEltN = 0; sentEltN < sentEltV.getLength(); sentEltN++) {
					Element sentElt = (Element)sentEltV.item(sentEltN);
					final int sentId = Integer.parseInt(sentElt.getAttribute("id")); 
					String plainText =  sentElt.getElementsByTagName(
							"plainText").item(0).getTextContent();
					NodeList parseEltV = sentElt.getElementsByTagName("constituencyParse");
					String constituencyParse = null;
					if (parseEltV.getLength()>0) {
						constituencyParse =  parseEltV.item(0).getTextContent();
					}
					Sentence sent = new Sentence(sentId, plainText, constituencyParse);
					// get tokens, if any
					NodeList tokenEltV = sentElt.getElementsByTagName("token");
					for (int tokenEltN = 0; tokenEltN < tokenEltV.getLength(); tokenEltN++) {
						Element tokenElt = (Element)tokenEltV.item(tokenEltN);
						// get core of the token
						String tokenIdStr = tokenElt.getAttribute("id");
						String[] tokenIdStrV = tokenIdStr.split("\\.");
						if (tokenIdStrV.length != 2) {
							throw new EnrycherException("Bad token id " + tokenIdStr);
						}
						if (Integer.parseInt(tokenIdStrV[0]) != sentId) {
							throw new EnrycherException("Bad sentence id in token id " + tokenIdStr);
						}
						final int tokenId = Integer.parseInt(tokenIdStrV[1]);
						String tokenStr = tokenElt.getTextContent();
						Token token = new Token(tokenId, tokenStr);
						// get rest of the token
						NamedNodeMap attrMap = tokenElt.getAttributes();
						for (int attrMapN = 0; attrMapN < attrMap.getLength(); attrMapN++) {
							String key = attrMap.item(attrMapN).getNodeName();
							if (!key.equals("id")) {
								String val = attrMap.item(attrMapN).getNodeValue();
								token.addKeyVal(key, val);
							}
						}
						// remember the token
						sent.addToken(token);
						// update sentence id
						if (sentId >= nextSentenceId) {
							nextSentenceId =  sentId + 1;
						}
					}
					para.addSentence(sent);
					sentIdToParagraphH.put(sent.getId(), para);
				}
				paragraphV.add(para);
			}
		}
	}
	
	public void saveXml(OutputStream outputStream) throws EnrycherException {
		// create an empty XML document
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new EnrycherException(".", e);
		}
		org.w3c.dom.Document xmlDoc;
		xmlDoc = docBuilder.newDocument();		
		
		Element topElt = xmlDoc.createElement("item");
		topElt.setAttribute("id", id);
		
		// serialize meta-data
		Element metadataElt = xmlDoc.createElement("metadata");
		// save semantics
		Element semanticsElt = xmlDoc.createElement("semantics");
		saveAttributes(xmlDoc, semanticsElt, attributeV);
		metadataElt.appendChild(semanticsElt);
		// save pipeline
		Element pipelineElt = xmlDoc.createElement("pipeline");
		savePipeline(xmlDoc, pipelineElt, pipeline.getTools());
		metadataElt.appendChild(pipelineElt);
		// finish with meta-data
		topElt.appendChild(metadataElt);
		
		// serialize text
		Element textElt = xmlDoc.createElement("text");
		for (Paragraph para : paragraphV) {
			Element paraElt = xmlDoc.createElement(para.getTagName());
			saveParagraph(xmlDoc, para, paraElt);
			textElt.appendChild(paraElt);
		}
		topElt.appendChild(textElt);
		
		// serialize annotations
		Element annsElt = xmlDoc.createElement("annotations");
		for (Annotation ann : annotationH.values()) {
			Element annElt = xmlDoc.createElement("annotation");
			saveAnnotation(xmlDoc, ann, annElt);			
			annsElt.appendChild(annElt);
		}
		topElt.appendChild(annsElt);
		
		// serialize assertions
		Element assertionsElt = xmlDoc.createElement("assertions");
		for(Assertion assertion : assertionH.values()){
			Element assertionElt = xmlDoc.createElement("assertion");
			saveAssertion(xmlDoc, assertion, assertionElt);
			assertionsElt.appendChild(assertionElt);
		}
		topElt.appendChild(assertionsElt);
			
		// finish with the document
		xmlDoc.appendChild(topElt);
		
		// prepare serialization object
		TransformerFactory transFac = TransformerFactory.newInstance();
		// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6296446
		transFac.setAttribute("indent-number", new Integer(2));
		Transformer trans = null;
		try {
			trans = transFac.newTransformer();
		} catch (TransformerConfigurationException e) {
			throw new EnrycherException("Error creating Transformer.", e);
		}
		trans.setOutputProperty(OutputKeys.INDENT, "yes");
		// prepare input and output for serialization
	    DOMSource domSource = new DOMSource(xmlDoc);
		StreamResult streamResult = new StreamResult(outputStream);
		// do the serialization
		try {
			trans.transform(domSource, streamResult);
		} catch (TransformerException e) {
			throw new EnrycherException("Error serializing.", e);
		}
	}
	
	private void saveAssertion(org.w3c.dom.Document xmlDoc, Assertion assertion, Element assertionElt){
		assertionElt.setAttribute("id", Integer.toString(assertion.getId()));
		assertionElt.appendChild(saveAssertionNode(xmlDoc, assertion.getSubject(), "subject"));
		assertionElt.appendChild(saveAssertionNode(xmlDoc, assertion.getPredicate(), "verb"));
		assertionElt.appendChild(saveAssertionNode(xmlDoc, assertion.getObject(), "object"));
	}

	private Element saveAssertionNode(org.w3c.dom.Document xmlDoc,
			AssertionNode assertionNode, String eltName) {
		
		// serialize the head
		Element assertionNodeElt = xmlDoc.createElement(eltName);	
		assertionNodeElt.setAttribute("displayName", assertionNode.getInstance().getDisplayName());
		assertionNodeElt.setAttribute("instanceId", Integer.toString(assertionNode.getInstance().getId()));
		assertionNodeElt.setAttribute("annotationId", Integer.toString(assertionNode.getAnnotation().getId()));
		// serialize the modifiers
		for (int modifierN = 0; modifierN < assertionNode.getModifiers(); modifierN++) {
			// read modifier
			Instance modifierInst = assertionNode.getModifierInsts().get(modifierN);
			Annotation modifierAnn = assertionNode.getModifierAnns().get(modifierN);
			// write the modifier
			if (modifierInst != null && modifierAnn != null) {
				Element modifierElt = xmlDoc.createElement("modifier");
				modifierElt.setAttribute("displayName", modifierInst.getDisplayName());
				modifierElt.setAttribute("instanceId", Integer.toString(modifierInst.getId()));
				modifierElt.setAttribute("annotationId", Integer.toString(modifierAnn.getId()));
				assertionNodeElt.appendChild(modifierElt);
			}
		}
		return assertionNodeElt;
	}
	
	private void saveAnnotation(org.w3c.dom.Document xmlDoc, Annotation ann, Element annElt) {
		annElt.setAttribute("id", Integer.toString(ann.getId()));
		annElt.setAttribute("displayName", ann.getDisplayName());
		annElt.setAttribute("type", ann.getType());
		// get instances
		Element annInstancesElt = xmlDoc.createElement("instances");
		saveInstances(xmlDoc, annInstancesElt, ann.getInstances());
		annElt.appendChild(annInstancesElt);
		// get semantics
		Element annSemanticsElt = xmlDoc.createElement("semantics");
		saveAttributes(xmlDoc, annSemanticsElt, ann.getAttributes());
		annElt.appendChild(annSemanticsElt);
	}

	private void saveInstances(org.w3c.dom.Document xmlDoc,
			Element annInstancesElt, List<Instance> instV) {
		for (Instance inst : instV) {
			Element instElt = xmlDoc.createElement("instance");
			instElt.setAttribute("id", Integer.toString(inst.getId()));
			instElt.setAttribute("words", inst.getDisplayName());
			for (int tokenN = 0; tokenN < inst.getTokens(); tokenN++) {
				Element tokenElt = xmlDoc.createElement("token");
				tokenElt.setAttribute("id", 
					inst.getSentenceId(tokenN) + "." + inst.getTokenId(tokenN));
				instElt.appendChild(tokenElt);
			}				
			annInstancesElt.appendChild(instElt);
		}
	}

	private void saveParagraph(org.w3c.dom.Document xmlDoc, Paragraph para,	Element paraElt) {
		for (Sentence sent : para.getSentences()) {
			Element sentElt = xmlDoc.createElement("sentence");
			sentElt.setAttribute("id", Integer.toString(sent.getId()));
			// prepare plain text
			Element plainTextElt = xmlDoc.createElement("plainText");
			plainTextElt.setTextContent(sent.getPlainText());
			sentElt.appendChild(plainTextElt);
			// prepare constituency parse
			Element parseElt = xmlDoc.createElement("constituencyParse");
			parseElt.setTextContent(sent.getConstituencyParse());
			sentElt.appendChild(parseElt);
			// prepare tokens
			if (sent.isTokenized()) {
				Element tokensElt = xmlDoc.createElement("tokens");
				for (Token token : sent.getTokens()) {
					Element tokenElt = xmlDoc.createElement("token");
					tokenElt.setAttribute("id", sent.getId() + "." + token.getId());
					tokenElt.setTextContent(token.getStr());
					for (String key : token.getKeys()) {
						tokenElt.setAttribute(key, token.getKeyVal(key));
					}
					tokensElt.appendChild(tokenElt);
				}
				sentElt.appendChild(tokensElt);
			}
			paraElt.appendChild(sentElt);
		}
	}

	private void savePipeline(org.w3c.dom.Document xmlDoc, Element pipelineElt,	List<Tool> toolV) {
		for (Tool tool : toolV) {
			Element toolElt = xmlDoc.createElement("tool");
			toolElt.setAttribute("time", tool.getTimeString());
			toolElt.setAttribute("durationTimeMillis", Long.toString(tool.getDuration()));
			toolElt.setTextContent(tool.getName());
			pipelineElt.appendChild(toolElt);
		}
	}

	private void saveAttributes(org.w3c.dom.Document xmlDoc, Element attributesElt, List<Attribute> attrV) {
		for (Attribute attr : attrV) {
			// <attribute type="dmoz:topic" displayName="Topic on dmoz.org">Top/Kids_and_Teens/Sports_and_Hobbies/Sports/Soccer</attribute>
			// <attribute type="skos:subject" resource="http://dbpedia.org/resource/Category:MetroStars_players"></attribute>
			Element attrElt = xmlDoc.createElement("attribute");
			attrElt.setAttribute("type", attr.getType());
			if (attr.isDisplayName()) {
				attrElt.setAttribute("displayName", attr.getDisplayName());
			}
			if (attr.isLiteral()) {
				attrElt.setTextContent(attr.getLiteral());
			} else {
				attrElt.setAttribute("resource", attr.getResource());
			}
			attributesElt.appendChild(attrElt);
		}
	}
}
