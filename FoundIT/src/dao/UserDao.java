package dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import model.User;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UserDao {

	public static boolean hasUser(String userId) {
		File file = new File("users.xml");
		if (! file.exists())
			return false;
		
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(file);
			NodeList userNodesList = doc.getDocumentElement().getChildNodes();
			
			for (int i = 0; i < userNodesList.getLength(); i ++) {
				Node userNode = userNodesList.item(i);
				NodeList userChildNodesList = userNode.getChildNodes();
				for (int j = 0; j < userChildNodesList.getLength(); j ++) {
					Node userChildNode = userChildNodesList.item(j);
					if (userChildNode.getNodeName().equals("userId")
							|| userChildNode.getTextContent().equals(userId))
						return true;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public static Boolean registerEE(User user) {
		String userId = user.getUserId();
		String password = user.getPassword();
		String realName = user.getRealName();
		String phoneNum = user.getPhoneNum();
		String address = user.getAddress();
		
		try {
			File file = new File("users.xml");
			if (! file.exists()) {
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
				bufferedWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><users></users>");
				bufferedWriter.close();
			}
	
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(file);
			Node users = doc.getDocumentElement();
			
			// set userIdNode
			Element userIdNode = doc.createElement("userId");
			userIdNode.appendChild(doc.createTextNode(userId));
			// set passwordNode
			Element passwordNode = doc.createElement("password");
			passwordNode.appendChild(doc.createTextNode(password));
			// set realNameNode
			Element realNameNode = doc.createElement("realName");
			if (realName == null || realName.trim().equals(""))
				realNameNode.appendChild(doc.createTextNode(""));
			else
				realNameNode.appendChild(doc.createTextNode(realName));
			// set phoneNumNode
			Element phoneNumNode = doc.createElement("phoneNum");
			if (phoneNum == null || phoneNum.trim().equals(""))
				phoneNumNode.appendChild(doc.createTextNode(""));
			else
				phoneNumNode.appendChild(doc.createTextNode(phoneNum));
			// set addressNode
			Element addressNode = doc.createElement("phoneNum");
			if (address == null || address.trim().equals(""))
				addressNode.appendChild(doc.createTextNode(""));
			else
				addressNode.appendChild(doc.createTextNode(address));
			// set userNode
			Element userNode = doc.createElement("user");
			users.appendChild(userNode);
			userNode.appendChild(userIdNode);
			userNode.appendChild(passwordNode);
			userNode.appendChild(realNameNode);
			userNode.appendChild(phoneNumNode);
			userNode.appendChild(addressNode);
			
			// write the content into xml file
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(new DOMSource(doc), new StreamResult(file));
		
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
