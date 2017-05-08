package au.edu.unsw.soacourse.job;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
import au.edu.unsw.soacourse.job.model.Job;
import au.edu.unsw.soacourse.job.model.Review;

@Path("/jobs")
public class JobsResource {
	// get a job by id
	@GET
	@Path("/{jobId}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Job getJob(
			@PathParam("jobId") String jobId
	) throws Exception {
		File file = new File("jobs.xml");
		if (! file.exists())
			return null;
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
			if (foundJobId.equals(jobId)) {
				Job job = new Job();
				for (int j = 0; j < jobChildNodesList.getLength(); j ++) {
					Node jobChildNode = jobChildNodesList.item(j);
					String jobChileNodeName = jobChildNode.getNodeName();
					if (jobChileNodeName.equals("jobId"))
						job.setJobId(jobChildNode.getTextContent());
					else if (jobChileNodeName.equals("companyName"))
						job.setCompanyName(jobChildNode.getTextContent());
					else if (jobChileNodeName.equals("salaryRate"))
						job.setSalaryRate(jobChildNode.getTextContent());
					else if (jobChileNodeName.equals("positionType"))
						job.setPositionType(jobChildNode.getTextContent());
					else if (jobChileNodeName.equals("location"))
						job.setLocation(jobChildNode.getTextContent());
					else if (jobChileNodeName.equals("jobDescription"))
						job.setJobDescription(jobChildNode.getTextContent());
					else if (jobChileNodeName.equals("status"))
						job.setStatus(jobChildNode.getTextContent());
					else if (jobChileNodeName.equals("applications")) {
						List<Application> appList = new ArrayList<>();
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
							appList.add(app);
						}
						job.setApplications(appList);
					}
				}
				return job;
			}
		}	
		return null;
	}
	
	// create a new job
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String newJob(
			@FormParam("companyName") String companyName,
			@FormParam("salaryRate") String salaryRate,
			@FormParam("positionType") String positionType,
			@FormParam("location") String location,
			@FormParam("jobDescription") String jobDescription
	) throws Exception {
		File file = new File("jobs.xml");
		if (! file.exists()) {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><jobs></jobs>");
			bufferedWriter.close();
		}
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(file);
		Node jobs = doc.getDocumentElement();
		
		// set jobIdNode
		UUID uuid = UUID.randomUUID();
		String jobId = uuid.toString();
		Element jobIdNode = doc.createElement("jobId");
		jobIdNode.appendChild(doc.createTextNode(jobId));
		// set companyNameNode
		Element companyNameNode = doc.createElement("companyName");
		if (companyName == null)
			companyNameNode.appendChild(doc.createTextNode(""));
		else
			companyNameNode.appendChild(doc.createTextNode(companyName));
		// set salaryRateNode
		Element salaryRateNode = doc.createElement("salaryRate");
		if (salaryRate == null)
			salaryRateNode.appendChild(doc.createTextNode(""));
		else
			salaryRateNode.appendChild(doc.createTextNode(salaryRate));
		// set positionTypeNode
		Element positionTypeNode = doc.createElement("positionType");
		if (positionType == null)
			positionTypeNode.appendChild(doc.createTextNode(""));
		else
			positionTypeNode.appendChild(doc.createTextNode(positionType));
		// set locationNode
		Element locationNode = doc.createElement("location");
		if (location == null)
			locationNode.appendChild(doc.createTextNode(""));
		else
			locationNode.appendChild(doc.createTextNode(location));
		// set jobDescriptionNode
		Element jobDescriptionNode = doc.createElement("jobDescription");
		if (jobDescription == null)
			jobDescriptionNode.appendChild(doc.createTextNode(""));
		else
			jobDescriptionNode.appendChild(doc.createTextNode(jobDescription));
		// set statusNode
		Element statusNode = doc.createElement("status");
		statusNode.appendChild(doc.createTextNode("created"));
		// set applicationsNode
		Element applicationsNode = doc.createElement("applications");
		
		// set jobNode
		Element jobNode = doc.createElement("job");
		jobs.appendChild(jobNode);
		jobNode.appendChild(jobIdNode);
		jobNode.appendChild(companyNameNode);
		jobNode.appendChild(salaryRateNode);
		jobNode.appendChild(positionTypeNode);
		jobNode.appendChild(locationNode);
		jobNode.appendChild(jobDescriptionNode);
		jobNode.appendChild(statusNode);
		jobNode.appendChild(applicationsNode);
		
		// write the content into xml file
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(new DOMSource(doc), new StreamResult(file));
		
		return "/jobs/" + jobId;
	}
	
	// update a job
	@PUT
	@Path("/{jobId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String updateApplication(
			@PathParam("jobId") String jobId,
			@FormParam("companyName") String companyName,
			@FormParam("salaryRate") String salaryRate,
			@FormParam("positionType") String positionType,
			@FormParam("location") String location,
			@FormParam("jobDescription") String jobDescription,
			@FormParam("status") String status
	) throws Exception {
		File file = new File("jobs.xml");
		if (! file.exists())
			return "no job found";
		String foundJobId = "";
		int applicationNum = 0;
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
				else if (jobChileNodeName.equals("applications"))
					applicationNum = jobChildNode.getChildNodes().getLength();
			}
			if (foundJobId.equals(jobId)) {
				if (applicationNum == 0) {
					for (int j = 0; j < jobChildNodesList.getLength(); j ++) {
						Node jobChildNode = jobChildNodesList.item(j);
						String jobChileNodeName = jobChildNode.getNodeName();
						if (jobChileNodeName.equals("companyName") && companyName != null)
							jobChildNode.setTextContent(companyName);
						else if (jobChileNodeName.equals("salaryRate") && salaryRate != null)
							jobChildNode.setTextContent(salaryRate);
						else if (jobChileNodeName.equals("positionType") && positionType != null)
							jobChildNode.setTextContent(positionType);
						else if (jobChileNodeName.equals("location") && location != null)
							jobChildNode.setTextContent(location);
						else if (jobChileNodeName.equals("jobDescription") && jobDescription != null)
							jobChildNode.setTextContent(jobDescription);
						else if (jobChileNodeName.equals("status") && status != null)
							jobChildNode.setTextContent(status);
					}
					// write the content into xml file
					Transformer transformer = TransformerFactory.newInstance().newTransformer();
					transformer.transform(new DOMSource(doc), new StreamResult(file));
					return "/jobs/" + jobId;
				} else
					return "cannot update due to the existing applications";
			}
		}
		return "no job found";
	}
	
	// delete a job
	@DELETE
	@Path("/{jobId}")
	public void deleteBook(@PathParam("jobId") String jobId) throws Exception {
		File file = new File("jobs.xml");
		if (! file.exists())
			return;
		String foundJobId = "";
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(file);
		Node jobs = doc.getDocumentElement();
		NodeList jobNodesList = jobs.getChildNodes();
		for (int i = 0; i < jobNodesList.getLength(); i ++) {
			Node job = jobNodesList.item(i);
			NodeList jobChildNodesList = job.getChildNodes();
			for (int j = 0; j < jobChildNodesList.getLength(); j ++) {
				Node jobChildNode = jobChildNodesList.item(j);
				String jobChileNodeName = jobChildNode.getNodeName();
				if (jobChileNodeName.equals("jobId")) {
					foundJobId = jobChildNode.getTextContent();
					break;
				}
			}
			if (foundJobId.equals(jobId)) {
				jobs.removeChild(job);
				// write the content into xml file
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.transform(new DOMSource(doc), new StreamResult(file));
				return;
			}	
		}
	}
	
	// search jobs
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<Job> searchJob(
			@QueryParam ("companyName") String companyName,
			@QueryParam ("salaryRateFrom") String salaryRateFrom,
			@QueryParam ("salaryRateTo") String salaryRateTo,
			@QueryParam ("positionType") String positionType,
			@QueryParam ("location") String location
	) throws Exception {
		List<Job> results = new ArrayList<>();
		File file = new File("jobs.xml");
		if (! file.exists())
			return results;
		boolean match;
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(file);
		NodeList jobNodesList = doc.getDocumentElement().getChildNodes();
		for (int i = 0; i < jobNodesList.getLength(); i ++) {
			Node jobNode = jobNodesList.item(i);
			NodeList jobChildNodesList = jobNode.getChildNodes();
			Job job = new Job();
			match = true;
			for (int j = 0; j < jobChildNodesList.getLength(); j ++) {
				Node jobChildNode = jobChildNodesList.item(j);
				String jobChildNodeName = jobChildNode.getNodeName();
				String jobChildNodeCont = jobChildNode.getTextContent();
				if (jobChildNodeName.equals("jobId"))
					job.setJobId(jobChildNodeCont);
				else if (jobChildNodeName.equals("companyName")) {
					job.setCompanyName(jobChildNodeCont);
					if (companyName != null && ! companyName.equals(jobChildNodeCont)) {
						match = false;
						break;
					}
				} else if (jobChildNodeName.equals("positionType")) {
					job.setPositionType(jobChildNodeCont);
					if (positionType != null && ! positionType.equals(jobChildNodeCont)) {
						match = false;
						break;
					}
				} else if (jobChildNodeName.equals("location")) {
					job.setLocation(jobChildNodeCont);
					if (location != null && ! location.equals(jobChildNodeCont)) {
						match = false;
						break;
					}
				} else if (jobChildNodeName.equals("jobDescription"))
					job.setJobDescription(jobChildNodeCont);
				else if (jobChildNodeName.equals("status")) {
					job.setStatus(jobChildNodeCont);
					if (jobChildNodeCont != null && ! jobChildNodeCont.equals("open")) {
						match = false;
						break;
					}
				} else if (jobChildNodeName.equals("salaryRate")) {
					job.setSalaryRate(jobChildNodeCont);
					if (jobChildNodeCont != null && (salaryRateFrom != null || salaryRateTo != null)) {
						Double from, to, value;
						if (salaryRateFrom != null)
							from = Double.parseDouble(salaryRateFrom);
						else
							from = -1.0;
						if (salaryRateTo != null)
							to = Double.parseDouble(salaryRateTo);
						else
							to = Double.MAX_VALUE;
						value = Double.parseDouble(jobChildNodeCont);
						if (from > value || value > to) {
							match = false;
							break;
						}
					}
				} 
			}
			if (match) results.add(job);
		}	
		return results;
	}
	
}
