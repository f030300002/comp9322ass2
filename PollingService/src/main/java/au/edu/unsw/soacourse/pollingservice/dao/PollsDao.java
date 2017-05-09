package au.edu.unsw.soacourse.pollingservice.dao;

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

import au.edu.unsw.soacourse.pollingservice.model.Poll;

public class PollsDao {
	DocumentBuilderFactory factory = null;
	DocumentBuilder builder = null;
	Document doc = null;
    
	XPathFactory xpathFactory = null;
	XPath xpath = null;
	
	public void preprocess(String filename){
		
		factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        
        try {
			builder = factory.newDocumentBuilder();
			
			//String path = System.getProperty("user.dir") + "/src/main/resources/";
			String path = System.getProperty("catalina.home") + "/webapps/ROOT/";
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
			    
			    VotesDao votesdao = new VotesDao();
			    
			    p.setVotesInPoll(votesdao.getVotesByPid(id));
			    
			    ps.add(p);
			}
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ps; 
	}
	
	public String deletePoll(String pid){
		
		int id = -1;
		
		if(!votesInPoll(pid)){
			
			 try {
				 
				preprocess("Polls.xml");
				 
				int totalNum = Integer.parseInt(xpath.evaluate(
					      "count(/Polls/Poll/id)", doc, XPathConstants.STRING).toString());
				
				for(int i=1;i<=totalNum;i++){
					String tempid = (String) xpath.evaluate(
				            "/Polls/Poll[" + i + "]/id/text()",
				            doc, XPathConstants.STRING);		
					if(tempid.equals(pid)){
						id = i-1;
						break;
					}
			    }
				
				if(id==-1) return "no pid";
				
				NodeList nl=doc.getElementsByTagName("Poll"); 
				
		        Node nodeDel=(Element)nl.item(id); 
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

		}else{
			return "exist votes";
		}
		
		return id+"";
	}
	
	public String updatePoll(Poll p){
		
		int id = -1;
		
		if(!votesInPoll(p.getId())){
			
			preprocess("Polls.xml");
			
			NodeList nl=doc.getElementsByTagName("Poll"); 
			
			int totalNum = 0;
			try {
				totalNum = Integer.parseInt(xpath.evaluate(
					      "count(/Polls/Poll/id)", doc, XPathConstants.STRING).toString());
				
				for(int i=1;i<=totalNum;i++){
					String tempid = (String) xpath.evaluate(
				            "/Polls/Poll[" + i + "]/id/text()",
				            doc, XPathConstants.STRING);		
					if(tempid.equals(p.getId())){
						id = i-1;
						break;
					}
			    }
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (XPathExpressionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(id==-1) return "no pid";
			
			Element element=(Element)nl.item(id); 
			
			if(p.getTitle()!=null){
				Node nodeUpdate=element.getElementsByTagName("title").item(0);
	            nodeUpdate.getFirstChild().setNodeValue(p.getTitle()); 
			}
			if(p.getDescription()!=null){
				Node nodeUpdate=element.getElementsByTagName("description").item(0);
	            nodeUpdate.getFirstChild().setNodeValue(p.getDescription()); 
			}
			if(p.getOptionType()!=null){
				Node nodeUpdate=element.getElementsByTagName("optiontype").item(0);
	            nodeUpdate.getFirstChild().setNodeValue(p.getOptionType()); 
			}
			if(p.getOptions()!=null){

				for(int i=0;i<p.getOptions().size();i++){
					Node nodeUpdate=element.getElementsByTagName("option").item(i);
					nodeUpdate.getFirstChild().setNodeValue(p.getOptions().get(i));
				}
 
			}
			if(p.getComments()!=null){
				Node nodeUpdate=element.getElementsByTagName("comments").item(0);
	            nodeUpdate.getFirstChild().setNodeValue(p.getComments()); 
			}
			if(p.getFinalChoice()!=null){
				Node nodeUpdate=element.getElementsByTagName("finalchoice").item(0);
	            nodeUpdate.getFirstChild().setNodeValue(p.getFinalChoice()); 
			}            
			
			DOMSource source=new DOMSource(doc); 

	        String path = System.getProperty("catalina.home") + "/webapps/ROOT/";
			//String path = System.getProperty("user.dir") + "/src/main/resources/";
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
			
		}else{
			return "exist votes";
		}
		
		return id+"";
		
	}
	
	public String createPoll(Poll p){
		
		int id = 0;
		
		try {
			
			preprocess("Polls.xml");
			
			Element eltRoot=doc.getDocumentElement(); 
			
			Element eltPoll=doc.createElement("Poll");
			
			 id = Integer.parseInt(xpath.evaluate(
			        "/Polls/Poll[last()]/id/text()",
			        doc, XPathConstants.STRING).toString())+1;
			
			Element eltId=doc.createElement("id");
			Text txtId=doc.createTextNode(id+""); 
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

	        String path = System.getProperty("catalina.home") + "/webapps/ROOT/";
			//String path = System.getProperty("user.dir") + "/src/main/resources/";
	        StreamResult result=new StreamResult(new File(path+"Polls.xml")); 
	        
	        TransformerFactory tff = TransformerFactory.newInstance(); 

	        Transformer tf;
	        
	        tf = tff.newTransformer();
			
			tf.transform(source, result);
			
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return id+"";
		
	}
	
	public boolean votesInPoll(String id){
		
		boolean in = false;
		
		try {
			preprocess("Polls.xml");
			
			
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
