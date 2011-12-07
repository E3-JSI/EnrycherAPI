Enrycher API
=============

Example 
-------------

Taken from [Example.java][https://github.com/JozefStefanInstitute/EnrycherAPI/blob/master/src/si/ijs/enrycher/Example.java].

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
