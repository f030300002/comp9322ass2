package au.edu.unsw.soacourse.job;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import au.edu.unsw.soacourse.job.model.Application;
import au.edu.unsw.soacourse.job.model.Review;

@Path("/apps")
public class ApplicationsResource {
	// create an application
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String newApplication(
			@FormParam("jobId") String jobId,
			@FormParam("candidateId") String candidateId,
			@FormParam("coverLetter") String coverLetter
	) throws Exception {
		File file = new File("jobs.xml");
		if (! file.exists())
			return "no job found";
		String foundJobId = "";
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(file);
		NodeList jobNodesList = doc.getDocumentElement().getChildNodes();
		for (int i = 0; i < jobNodesList.getLength(); i ++) {
			Node jobNode = jobNodesList.item(i);
			NodeList jobChildNodesList = jobNode.getChildNodes();
			for (int j = 0; j < jobChildNodesList.getLength(); j ++) {
				Node jobChildNode = jobChildNodesList.item(j);
				String jobChileNodeName = jobChildNode.getNodeName();
				if (jobChileNodeName.equals("jobId")) {
					foundJobId = jobChildNode.getTextContent();
					break;
				}
			}
			if (jobId != null && foundJobId.equals(jobId)) {
				for (int j = 0; j < jobChildNodesList.getLength(); j ++) {
					Node applications = jobChildNodesList.item(j);
					String jobChileNodeName = applications.getNodeName();
					if (jobChileNodeName.equals("applications")) {
						
						// set appIdNode
						UUID uuid = UUID.randomUUID();
						String appId = uuid.toString();
						Element appIdNode = doc.createElement("appId");
						appIdNode.appendChild(doc.createTextNode(appId));
						// set candidateIdNode
						Element candidateIdNode = doc.createElement("candidateId");
						if (candidateId == null)
							candidateIdNode.appendChild(doc.createTextNode(""));
						else
							candidateIdNode.appendChild(doc.createTextNode(candidateId));
						// set coverLetterNode
						Element coverLetterNode = doc.createElement("coverLetter");
						if (coverLetter == null)
							coverLetterNode.appendChild(doc.createTextNode(""));
						else
							coverLetterNode.appendChild(doc.createTextNode(coverLetter));
						// set statusNode
						Element statusNode = doc.createElement("status");
						statusNode.appendChild(doc.createTextNode("received"));
						// set reviewNode
						Element review1Node = doc.createElement("review1");
						Element review2Node = doc.createElement("review2");
						
						// set applicationNode
						Element applicationNode = doc.createElement("application");
						applications.appendChild(applicationNode);
						applicationNode.appendChild(appIdNode);
						applicationNode.appendChild(candidateIdNode);
						applicationNode.appendChild(coverLetterNode);
						applicationNode.appendChild(statusNode);
						applicationNode.appendChild(review1Node);
						applicationNode.appendChild(review2Node);
						
						// write the content into xml file
						Transformer transformer = TransformerFactory.newInstance().newTransformer();
						transformer.transform(new DOMSource(doc), new StreamResult(file));
						
						return "/apps/" + appId;
					}
				}
			}
		}
		return "no job found";
	}
	
	// update an application
	@PUT
	@Path("/{appId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String updateApplication(
			@PathParam("appId") String appId,
			@FormParam("coverLetter") String coverLetter
	) throws Exception {
		File file = new File("jobs.xml");
		if (! file.exists())
			return "no application found";
		String foundAppId = "";
		String status = "";
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(file);
		NodeList jobNodesList = doc.getDocumentElement().getChildNodes();
		for (int i = 0; i < jobNodesList.getLength(); i ++) {
			Node jobNode = jobNodesList.item(i);
			NodeList jobChildNodesList = jobNode.getChildNodes();
			for (int j = 0; j < jobChildNodesList.getLength(); j ++) {
				Node jobChildNode = jobChildNodesList.item(j);
				String jobChileNodeName = jobChildNode.getNodeName();
				if (jobChileNodeName.equals("applications")) {
					NodeList appNodesList = jobChildNode.getChildNodes();
					for (int k = 0; k < appNodesList.getLength(); k ++) {
						Node appNode = appNodesList.item(k);
						NodeList appChildNodesList = appNode.getChildNodes();
						for (int l = 0; l < appChildNodesList.getLength(); l ++) {
							Node appChildNode = appChildNodesList.item(l);
							if (appChildNode.getNodeName().equals("appId"))
								foundAppId = appChildNode.getTextContent();
							else if (appChildNode.getNodeName().equals("status"))
								status = appChildNode.getTextContent();
						}
						if (foundAppId.equals(appId)) {
							if (status!= null && status.equals("received")) {
								for (int l = 0; l < appChildNodesList.getLength(); l ++) {
									Node appChildNode = appChildNodesList.item(l);
									if (appChildNode.getNodeName().equals("coverLetter") && coverLetter != null)
										appChildNode.setTextContent(coverLetter);
								}
								// write the content into xml file
								Transformer transformer = TransformerFactory.newInstance().newTransformer();
								transformer.transform(new DOMSource(doc), new StreamResult(file));
								return "/apps/" + appId;
							} else
								return "cannot update due to the current status";
						}
					}
				}
			}
		}
		return "no application found";
	}
	
	// get a single application and all applications per job
	@GET
	@Path("/{id}") // this id can be appId or jobId
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<Application> getApplication(
			@PathParam("id") String id
	) throws Exception {
		List<Application> results = new ArrayList<>();
		File file = new File("jobs.xml");
		if (! file.exists())
			return results;
		String foundJobId = "";
		String foundAppId = "";
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(file);
		NodeList jobNodesList = doc.getDocumentElement().getChildNodes();
		for (int i = 0; i < jobNodesList.getLength(); i ++) {
			Node jobNode = jobNodesList.item(i);
			NodeList jobChildNodesList = jobNode.getChildNodes();
			for (int j = 0; j < jobChildNodesList.getLength(); j ++) {
				Node jobChildNode = jobChildNodesList.item(j);
				String jobChileNodeName = jobChildNode.getNodeName();
				if (jobChileNodeName.equals("jobId"))
					foundJobId = jobChildNode.getTextContent();
				else if (jobChileNodeName.equals("applications")) {
					NodeList appNodeList = jobChildNode.getChildNodes();
					for (int k = 0; k < appNodeList.getLength(); k ++) {
						Node appNode = appNodeList.item(k);
						NodeList appChildNodesList = appNode.getChildNodes();
						for (int l = 0; l < appChildNodesList.getLength(); l ++) {
							Node appChildNode = appChildNodesList.item(l);
							if (appChildNode.getNodeName().equals("appId"))
								foundAppId = appChildNode.getTextContent();
						}
						if (foundAppId.equals(id)) {
							// return this single application
							Application app = new Application();
							for (int l = 0; l < appChildNodesList.getLength(); l ++) {
								Node appChildNode = appChildNodesList.item(l);
								String appChildNodeName = appChildNode.getNodeName();
								if (appChildNodeName.equals("appId"))
									app.setAppId(appChildNode.getTextContent());		
								else if (appChildNodeName.equals("candidateId"))
									app.setCandidateId(appChildNode.getTextContent());
								else if (appChildNodeName.equals("coverLetter"))
									app.setCoverLetter(appChildNode.getTextContent());
								else if (appChildNodeName.equals("status"))
									app.setStatus(appChildNode.getTextContent());
								else if (appChildNodeName.equals("review1")) {
									Review review = new Review();
									NodeList reviewChildNodesList = appChildNode.getChildNodes();
									for (int m = 0; m < reviewChildNodesList.getLength(); m ++) {
										Node reviewChildNode = reviewChildNodesList.item(m);
										String reviewChildNodeName = reviewChildNode.getNodeName();
										if (reviewChildNodeName.equals("reviewId"))
											review.setReviewId(reviewChildNode.getTextContent());
										else if (reviewChildNodeName.equals("reviewer"))
											review.setReviewer(reviewChildNode.getTextContent());
										else if (reviewChildNodeName.equals("comment"))
											review.setComment(reviewChildNode.getTextContent());
										else if (reviewChildNodeName.equals("recommend"))
											review.setRecommend(reviewChildNode.getTextContent());
									}
									app.setReview1(review);
								} else if (appChildNodeName.equals("review2")) {
									Review review = new Review();
									NodeList reviewChildNodesList = appChildNode.getChildNodes();
									for (int m = 0; m < reviewChildNodesList.getLength(); m ++) {
										Node reviewChildNode = reviewChildNodesList.item(m);
										String reviewChildNodeName = reviewChildNode.getNodeName();
										if (reviewChildNodeName.equals("reviewId"))
											review.setReviewId(reviewChildNode.getTextContent());
										else if (reviewChildNodeName.equals("reviewer"))
											review.setReviewer(reviewChildNode.getTextContent());
										else if (reviewChildNodeName.equals("comment"))
											review.setComment(reviewChildNode.getTextContent());
										else if (reviewChildNodeName.equals("recommend"))
											review.setRecommend(reviewChildNode.getTextContent());
									}
									app.setReview2(review);
								}
							}
							results.add(app);
							return results;
						}
					}
				}
			}
			if (foundJobId.equals(id)) {
				// return all the applications under this job
				for (int j = 0; j < jobChildNodesList.getLength(); j ++) {
					Node jobChildNode = jobChildNodesList.item(j);
					String jobChileNodeName = jobChildNode.getNodeName();
					if (jobChileNodeName.equals("applications")) {
						NodeList appNodesList = jobChildNode.getChildNodes();
						for (int k = 0; k < appNodesList.getLength(); k ++) {
							Node appNode = appNodesList.item(k);
							Application app = new Application();
							NodeList appChildNodesList = appNode.getChildNodes();
							for (int l = 0; l < appChildNodesList.getLength(); l ++) {
								Node appChildNode = appChildNodesList.item(l);
								String appChildNodeName = appChildNode.getNodeName();
								if (appChildNodeName.equals("appId"))
									app.setAppId(appChildNode.getTextContent());		
								else if (appChildNodeName.equals("candidateId"))
									app.setCandidateId(appChildNode.getTextContent());
								else if (appChildNodeName.equals("coverLetter"))
									app.setCoverLetter(appChildNode.getTextContent());
								else if (appChildNodeName.equals("status"))
									app.setStatus(appChildNode.getTextContent());
								else if (appChildNodeName.equals("review1")) {
									Review review = new Review();
									NodeList reviewChildNodesList = appChildNode.getChildNodes();
									for (int m = 0; m < reviewChildNodesList.getLength(); m ++) {
										Node reviewChildNode = reviewChildNodesList.item(m);
										String reviewChildNodeName = reviewChildNode.getNodeName();
										if (reviewChildNodeName.equals("reviewId"))
											review.setReviewId(reviewChildNode.getTextContent());
										else if (reviewChildNodeName.equals("reviewer"))
											review.setReviewer(reviewChildNode.getTextContent());
										else if (reviewChildNodeName.equals("comment"))
											review.setComment(reviewChildNode.getTextContent());
										else if (reviewChildNodeName.equals("recommend"))
											review.setRecommend(reviewChildNode.getTextContent());
									}
									app.setReview1(review);
								} else if (appChildNodeName.equals("review2")) {
									Review review = new Review();
									NodeList reviewChildNodesList = appChildNode.getChildNodes();
									for (int m = 0; m < reviewChildNodesList.getLength(); m ++) {
										Node reviewChildNode = reviewChildNodesList.item(m);
										String reviewChildNodeName = reviewChildNode.getNodeName();
										if (reviewChildNodeName.equals("reviewId"))
											review.setReviewId(reviewChildNode.getTextContent());
										else if (reviewChildNodeName.equals("reviewer"))
											review.setReviewer(reviewChildNode.getTextContent());
										else if (reviewChildNodeName.equals("comment"))
											review.setComment(reviewChildNode.getTextContent());
										else if (reviewChildNodeName.equals("recommend"))
											review.setRecommend(reviewChildNode.getTextContent());
									}
									app.setReview2(review);
								}
							}
							results.add(app);
						}
						return results;
					}
				}
			}
		}
		return results;
	}
}
