package si.ijs.enrycher;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import si.ijs.enrycher.doc.Annotation;
import si.ijs.enrycher.doc.Attribute;
import si.ijs.enrycher.doc.Document;
import si.ijs.enrycher.doc.EnrycherException;
import si.ijs.enrycher.doc.Instance;
import si.ijs.enrycher.doc.Paragraph;
import si.ijs.enrycher.doc.Sentence;
import si.ijs.enrycher.exe.EnrycherWebExecuter;

public class Example {

	public static void main(String[] args) throws EnrycherException, MalformedURLException {
		// input document
		String docString = "Tiger Woods emerged from a traffic jam of his " +
			"own making to thrill thousands of fans with a six-under 66 at " + 
			"the $1.4 million Australian Masters on Thursday.";
		// URL of Enrycher web service
		URL pipelineUrl = new URL("http://enrycher.ijs.si/run");
		// convert input document to input stream
		InputStream docStream = new ByteArrayInputStream(docString.getBytes());
		// call Enrycher web service
		Document doc = EnrycherWebExecuter.processSync(pipelineUrl, docStream);
		// iterate over all the annotations
		for (Annotation ann : doc.getAnnotations()) {
			// list all persons
			if (ann.isPerson()) {
				System.out.println("Person: " + ann.getDisplayName());
				// get sentences in which it occurs
				for (Instance inst : ann.getInstances()) {
					int sentenceId = inst.getSentenceId(0);
					Paragraph paragraph = doc.getParagraph(sentenceId);
					Sentence sentence = paragraph.getSentence(sentenceId);
					System.out.println(inst.getDisplayName() + ": '" + sentence.getPlainText() + "'");
				}
				// list all attributes 
				for (Attribute attr : ann.getAttributes()) {
					if (attr.isLiteral()) {
						System.out.println(" - " + attr.getType() + " : " + attr.getLiteral());
					} else if (attr.isResource()){
						System.out.println(" - " + attr.getType() + " : " + attr.getResource());					
					}
				}
			}
		}
	}

}
