package au.edu.unsw.soacourse.pollingservice.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class DBHandler {
	
	static DocumentBuilderFactory factory = null;
	static DocumentBuilder builder = null;
	static Document doc = null;
    
	static XPathFactory xpathFactory = null;
	static XPath xpath = null;
	
	public static void main(String[] args){
       
        		//preprocess("Polls.xml");
        		//getPolls();
			//deletePollByTitle("14 MAY Meeting");
			//votesInPoll("14 MAY Meeting");
		Poll p = new Poll();
		p.setId("3");
		p.setTitle("Hh");
		p.setDescription("blabla");
		p.setOptionType("lala");
		
		List<String> options = new ArrayList<String>();
		options.add("15 MAY");
		p.setOptions(options);
		p.setComments("fuck");
		p.setFinalChoice("lll");
		updatePoll(p);
		//createPoll(p);
	}
	
	
	public static void preprocess(String filename){
		
		factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        
        try {
			builder = factory.newDocumentBuilder();
			
			String path = System.getProperty("user.dir") + "/src/main/resources/";
			//String path = System.getProperty("catalina.home") + "/webapps/ROOT/";
			//System.out.println(path+filename);
			doc = builder.parse(path+filename);
			xpathFactory = XPathFactory.newInstance();
	        xpath = xpathFactory.newXPath();
		
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public List<Poll> getPolls(){
		
		List<Poll> ps = new ArrayList<Poll>();
		
		try {
			preprocess("Polls.xml");
			
			XPathExpression expr = xpath.compile("/Polls/Poll[1]/title/text()");
			
			NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			
			int pollnodeNum = Integer.parseInt(xpath.evaluate(
				      "count(/Polls/Poll)", doc, XPathConstants.STRING).toString());
			
			for (int i = 1; i <= pollnodeNum; i++) {
			    Poll p = new Poll();
			    
			    String id = (String) xpath.evaluate(
			            "/Polls/Poll[" + i + "]/id/text()",
			            doc, XPathConstants.STRING);
			    String title = (String) xpath.evaluate(
			            "/Polls/Poll[" + i + "]/title/text()",
			            doc, XPathConstants.STRING);
			    String description = (String) xpath.evaluate(
			            "/Polls/Poll[" + i + "]/description/text()",
			            doc, XPathConstants.STRING);
			    String optionType = (String) xpath.evaluate(
			            "/Polls/Poll[" + i + "]/optiontype/text()",
			            doc, XPathConstants.STRING);
			    int optionsNum = Integer.parseInt(xpath.evaluate(
					      "count(/Polls/Poll[" + i + "]/options/option)", doc, XPathConstants.STRING).toString());

			    List<String> options = new ArrayList<String>();
			    for(int j=1;j<=optionsNum;j++){
			    		String option = (String) xpath.evaluate(
					            "/Polls/Poll[" + i + "]/options/option["+ j + "]/text()",
					            doc, XPathConstants.STRING);
			    		options.add(option);
			    		
			    }			    
			    
			    String comments = (String) xpath.evaluate(
			            "/Polls/Poll[" + i + "]/comments/text()",
			            doc, XPathConstants.STRING);
			    String finalChoice = (String) xpath.evaluate(
			            "/Polls/Poll[" + i + "]/finalchoice/text()",
			            doc, XPathConstants.STRING);
			    p.setId(id);
			    p.setTitle(title);
			    p.setDescription(description);
			    p.setOptionType(optionType);
			    p.setOptions(options);
			    p.setComments(comments);
			    p.setFinalChoice(finalChoice);
			    
			    ps.add(p);
			}
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ps; 
	}
	
	public void deletePollByTitle(String title){
		
		if(!votesInPoll(title)){
			
			 try {
				 
				preprocess("Polls.xml");
				 
				int id = Integer.parseInt(xpath.evaluate(
						 "/Polls/Poll[title='"+ title + "']/id/text()", doc, XPathConstants.STRING).toString());
							

				NodeList nl=doc.getElementsByTagName("Poll"); 
				
		        Node nodeDel=(Element)nl.item(id-1); 
		        nodeDel.getParentNode().removeChild(nodeDel); 
		        
		        DOMSource source=new DOMSource(doc); 

		        String path = System.getProperty("catalina.home") + "/webapps/ROOT/";
	            StreamResult result=new StreamResult(new File(path+"Polls.xml")); 
	            
	            TransformerFactory tff = TransformerFactory.newInstance(); 

	            Transformer tf;
		
				tf = tff.newTransformer();
	            
				tf.transform(source, result);
							
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

		}
		
	}
	
	public static void updatePoll(Poll p){
		
		if(!votesInPoll(p.getTitle())){
			
			preprocess("Polls.xml");
			
			NodeList nl=doc.getElementsByTagName("Poll"); 
			
			Element element=(Element)nl.item(Integer.parseInt(p.getId())-1); 
			
			if(p.getTitle()!=null&&!p.getTitle().isEmpty()){
				Node nodeUpdate=element.getElementsByTagName("title").item(0);
	            nodeUpdate.getFirstChild().setNodeValue(p.getTitle()); 
			}
			if(p.getDescription()!=null&&!p.getDescription().isEmpty()){
				Node nodeUpdate=element.getElementsByTagName("description").item(0);
	            nodeUpdate.getFirstChild().setNodeValue(p.getDescription()); 
			}
			if(p.getOptionType()!=null&&!p.getOptionType().isEmpty()){
				Node nodeUpdate=element.getElementsByTagName("optiontype").item(0);
	            nodeUpdate.getFirstChild().setNodeValue(p.getTitle()); 
			}
			if(p.getOptions()!=null&&!p.getOptions().isEmpty()){

				for(int i=0;i<p.getOptions().size();i++){
					Node nodeUpdate=element.getElementsByTagName("option").item(i);
					nodeUpdate.getFirstChild().setNodeValue(p.getOptions().get(i));
				}
 
			}
			if(p.getComments()!=null&&!p.getComments().isEmpty()){
				Node nodeUpdate=element.getElementsByTagName("comments").item(0);
	            nodeUpdate.getFirstChild().setNodeValue(p.getComments()); 
			}
			if(p.getFinalChoice()!=null){
				Node nodeUpdate=element.getElementsByTagName("finalchoice").item(0);
	            nodeUpdate.getFirstChild().setNodeValue(p.getFinalChoice()); 
			}            
			
			DOMSource source=new DOMSource(doc); 

	        //String path = System.getProperty("catalina.home") + "/webapps/ROOT/";
			String path = System.getProperty("user.dir") + "/src/main/resources/";
            StreamResult result=new StreamResult(new File(path+"Polls.xml")); 
            
            TransformerFactory tff = TransformerFactory.newInstance(); 

            Transformer tf;
            
            try{
	    			tf = tff.newTransformer();
					
	    			tf.transform(source, result);
            } catch (TransformerConfigurationException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
    			} catch (TransformerException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
    			}
			
		} 
		
	}
	
	public static void createPoll(Poll p){
		
		preprocess("Polls.xml");
		
		Element eltRoot=doc.getDocumentElement(); 
		
		Element eltPoll=doc.createElement("Poll");
		
		Element eltId=doc.createElement("id");
		Text txtId=doc.createTextNode(p.getId()); 
		eltId.appendChild(txtId); 
		eltPoll.appendChild(eltId);
		Element eltTitle=doc.createElement("title");
		Text txtTitle=doc.createTextNode(p.getTitle()); 
		eltTitle.appendChild(txtTitle); 
		eltPoll.appendChild(eltTitle);
		Element eltDes=doc.createElement("description");
		Text txtDes=doc.createTextNode(p.getDescription()); 
		eltDes.appendChild(txtDes); 
		eltPoll.appendChild(eltDes);
		Element eltOptType=doc.createElement("optiontype");
		Text txtOptType=doc.createTextNode(p.getOptionType());
		eltOptType.appendChild(txtOptType); 
		eltPoll.appendChild(eltOptType);
		Element eltOpts=doc.createElement("options");
		if(p.getOptions()!=null&&!p.getOptions().isEmpty()){
			for(int i=0;i<p.getOptions().size();i++){
				Element eltOpt=doc.createElement("option");
				Text txtOpt=doc.createTextNode(p.getOptions().get(i));
				eltOpt.appendChild(txtOpt);
				eltOpts.appendChild(eltOpt);
			}
		}
		eltPoll.appendChild(eltOpts);
		Element eltComml=doc.createElement("comments");
		Text txtComm=doc.createTextNode(p.getComments()); 
		eltComml.appendChild(txtComm); 
		eltPoll.appendChild(eltComml);
		Element eltFinc=doc.createElement("finalchoice");
		Text txtFinc=doc.createTextNode(p.getFinalChoice()); 
		eltFinc.appendChild(txtFinc); 
		eltPoll.appendChild(eltFinc);

		eltRoot.appendChild(eltPoll);
		
		DOMSource source=new DOMSource(doc); 

        //String path = System.getProperty("catalina.home") + "/webapps/ROOT/";
		String path = System.getProperty("user.dir") + "/src/main/resources/";
        StreamResult result=new StreamResult(new File(path+"Polls.xml")); 
        
        TransformerFactory tff = TransformerFactory.newInstance(); 

        Transformer tf;
        
        try{
    			tf = tff.newTransformer();
				
    			tf.transform(source, result);
        } catch (TransformerConfigurationException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
		} catch (TransformerException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
		}
		
	}
	
	public static boolean votesInPoll(String title){
		
		boolean in = false;
		
		try {
			preprocess("Polls.xml");
			
			String id = (String) xpath.evaluate(
			        "/Polls/Poll[title='"+ title + "']/id/text()",
			        doc, XPathConstants.STRING);
			
			if(id!=null&&!id.isEmpty()){
				preprocess("Votes.xml");
				
				int votesNum = Integer.parseInt(xpath.evaluate(
					      "count(/Votes/Vote[pid='"+id+"']/id)", doc, XPathConstants.STRING).toString());
				
				if(votesNum!=0) in = true;

			}
			

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return in;
	}
	
}
