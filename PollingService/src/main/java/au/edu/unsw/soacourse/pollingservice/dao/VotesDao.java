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
import au.edu.unsw.soacourse.pollingservice.model.Vote;

public class VotesDao {
	
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
	
	public Vote getVote(String id){
	
		Vote v = new Vote();
		
		try {
			preprocess("Votes.xml");
			
			String pid = (String) xpath.evaluate(
		            "/Votes/Vote[id='"+id+"']/pid/text()",
		            doc, XPathConstants.STRING);
			
			String participantName = (String) xpath.evaluate(
		            "/Votes/Vote[id='"+id+"']/participantname/text()",
		            doc, XPathConstants.STRING);
			
			String chosenOption = (String) xpath.evaluate(
		            "/Votes/Vote[id='"+id+"']/chosenoption/text()",
		            doc, XPathConstants.STRING);
			
			v.setId(id);
			v.setPid(pid);
			v.setParticipantName(participantName);
			v.setChosenOption(chosenOption);
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return v; 
	}
	
	public List<Vote> getVotesByPid(String id){
		
		List<Vote> vs = new ArrayList<Vote>();
		
		try {
			preprocess("Votes.xml");
			
			int totalNum = Integer.parseInt(xpath.evaluate(
				      "count(/Votes/Vote/id)", doc, XPathConstants.STRING).toString());
			
			int votesNum = Integer.parseInt(xpath.evaluate(
				      "count(/Votes/Vote[pid='"+id+"']/id)", doc, XPathConstants.STRING).toString());
			
			for(int i=1;i<=totalNum;i++){
				
				String pid = (String) xpath.evaluate(
						"/Votes/Vote[" + i + "]/pid/text()",
			            doc, XPathConstants.STRING);
				
				if(pid.equals(id)){
					Vote v = new Vote();
					
					String vid = (String) xpath.evaluate(
							"/Votes/Vote[" + i + "]/id/text()",
				            doc, XPathConstants.STRING);
					
					String participantName = (String) xpath.evaluate(
				            "/Votes/Vote[" + i + "]/participantname/text()",
				            doc, XPathConstants.STRING);
					
					String chosenOption = (String) xpath.evaluate(
				            "/Votes/Vote[" + i + "]/chosenoption/text()",
				            doc, XPathConstants.STRING);
					
					v.setId(vid);
					v.setPid(pid);
					v.setParticipantName(participantName);
					v.setChosenOption(chosenOption);
					
					vs.add(v);
				}
				
			}
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return vs; 
	}
	
	public String updateVote(Vote v){
		
		//String id = null;
		int id = 0;
		
		if(!finalChoiceInPoll(v.getPid())){
			
			//id = v.getId(); // is it possible that given wrong id????
			
			preprocess("Votes.xml");
			
			NodeList nl=doc.getElementsByTagName("Vote"); 
			
			int totalNum = 0;
			try {
				totalNum = Integer.parseInt(xpath.evaluate(
					      "count(/Votes/Vote/id)", doc, XPathConstants.STRING).toString());
				
				for(int i=1;i<=totalNum;i++){
					String tempid = (String) xpath.evaluate(
				            "/Votes/Vote[" + i + "]/id/text()",
				            doc, XPathConstants.STRING);		
					if(tempid.equals(v.getId())){
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
			
			if(id==0) return "no vid";
			
			Element element=(Element)nl.item(id); 
			
			if(v.getParticipantName()!=null){
				Node nodeUpdate=element.getElementsByTagName("participantname").item(0);
	            nodeUpdate.getFirstChild().setNodeValue(v.getParticipantName()); 
			}
			if(v.getChosenOption()!=null){
				Node nodeUpdate=element.getElementsByTagName("chosenoption").item(0);
	            nodeUpdate.getFirstChild().setNodeValue(v.getChosenOption()); 
			}
			
			DOMSource source=new DOMSource(doc); 

	        String path = System.getProperty("catalina.home") + "/webapps/ROOT/";
			//String path = System.getProperty("user.dir") + "/src/main/resources/";
            StreamResult result=new StreamResult(new File(path+"Votes.xml")); 
            
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
			return "has final choice";
		}
		
		return id+"";
	}
	
	public String createVote(Vote v){
		
		int id = 0;
		
		try {
			
			preprocess("Votes.xml");
			
			Element eltRoot=doc.getDocumentElement(); 
			
			Element eltVote=doc.createElement("Vote");
			
			 id = Integer.parseInt(xpath.evaluate(
			        "/Votes/Vote[last()]/id/text()",
			        doc, XPathConstants.STRING).toString())+1;
			
			Element eltId=doc.createElement("id");
			Text txtId=doc.createTextNode(id+""); 
			eltId.appendChild(txtId); 
			eltVote.appendChild(eltId);
			Element eltPid=doc.createElement("pid");
			Text txtPid=doc.createTextNode(v.getPid()); 
			eltPid.appendChild(txtPid); 
			eltVote.appendChild(eltPid);
			Element eltPart=doc.createElement("participantname");
			Text txtPart=doc.createTextNode(v.getParticipantName()); 
			eltPart.appendChild(txtPart); 
			eltVote.appendChild(eltPart);
			Element eltCho=doc.createElement("chosenoption");
			Text txtCho=doc.createTextNode(v.getChosenOption());
			eltCho.appendChild(txtCho); 
			eltVote.appendChild(eltCho);

			eltRoot.appendChild(eltVote);
			
			DOMSource source=new DOMSource(doc); 

	        String path = System.getProperty("catalina.home") + "/webapps/ROOT/";
			//String path = System.getProperty("user.dir") + "/src/main/resources/";
	        StreamResult result=new StreamResult(new File(path+"Votes.xml")); 
	        
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
	
	public boolean finalChoiceInPoll(String pid){
		
		boolean in = false;
		
		preprocess("Polls.xml");
		
		try {
			String finalchoice = (String) xpath.evaluate(
			        "/Polls/Poll[pid='"+ pid + "']/finalchoice/text()",
			        doc, XPathConstants.STRING);
			
			if(!finalchoice.equals(" ")) in = true;
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return in;
		
	}
	
}
