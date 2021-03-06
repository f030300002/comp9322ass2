package au.edu.unsw.soacourse.job.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

import au.edu.unsw.soacourse.job.model.Review;

public class ReviewsDao {
	static public String newReview(Review review) throws Exception {
		String appId = review.getAppId();
		String reviewer = review.getReviewer();
		String comment = review.getComment();
		String recommend = review.getRecommend();
		
		File file = new File("jobs.xml");
		if (! file.exists())
			return "no application found";
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
				if (jobChileNodeName.equals("applications")) {
					NodeList appNodesList = jobChildNode.getChildNodes();
					for (int k = 0; k < appNodesList.getLength(); k ++) {
						Node appNode = appNodesList.item(k);
						NodeList appChildNodesList = appNode.getChildNodes();
						for (int l = 0; l < appChildNodesList.getLength(); l ++) {
							Node appChildNode = appChildNodesList.item(l);
							if (appChildNode.getNodeName().equals("appId")) {
								foundAppId = appChildNode.getTextContent();
								break;
							}
						}
						if (foundAppId.equals(appId)) {
							// create a review under this application node
							for (int l = 0; l < appChildNodesList.getLength(); l ++) {
								Node appChildNode = appChildNodesList.item(l);
								if(appChildNode.getNodeName().equals("review1") || appChildNode.getNodeName().equals("review2")) {
									NodeList reviewChildNodesList = appChildNode.getChildNodes();
									if (reviewChildNodesList.getLength() == 0) {
										UUID uuid = UUID.randomUUID();
										String reviewId = uuid.toString();
										
										// set reviewIdNode
										Element reviewIdNode = doc.createElement("reviewId");
										reviewIdNode.appendChild(doc.createTextNode(reviewId));
										// set reviewerNode
										Element reviewerNode = doc.createElement("reviewer");
										if (reviewer == null)
											reviewerNode.appendChild(doc.createTextNode(""));
										else
											reviewerNode.appendChild(doc.createTextNode(reviewer));
										// set commentNode
										Element commentNode = doc.createElement("comment");
										if (comment == null)
											commentNode.appendChild(doc.createTextNode(""));
										else
											commentNode.appendChild(doc.createTextNode(comment));
										// set recommendNode
										Element recommendNode = doc.createElement("recommend");
										if (recommend == null)
											recommendNode.appendChild(doc.createTextNode(""));
										else
											recommendNode.appendChild(doc.createTextNode(recommend));
										
										// set review1Node or review2Node
										appChildNode.appendChild(reviewIdNode);
										appChildNode.appendChild(reviewerNode);
										appChildNode.appendChild(commentNode);
										appChildNode.appendChild(recommendNode);
										
										// write the content into xml file
										Transformer transformer = TransformerFactory.newInstance().newTransformer();
										transformer.transform(new DOMSource(doc), new StreamResult(file));
										return reviewId;
									}
								}
							}
							return "cannot create due to that there have been two reviews";
						}
					}
				}
			}
		}
		return "no application found";
	}
	
	static public String updateReview(Review review) throws Exception {
		String reviewId = review.getReviewId();
		String comment = review.getComment();
		String recommend = review.getRecommend();
		
		File file = new File("jobs.xml");
		if (! file.exists())
			return "no review found";
		String foundRevId = "";
		String jobStatues = "";
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(file);
		NodeList jobNodesList = doc.getDocumentElement().getChildNodes();
		for (int i = 0; i < jobNodesList.getLength(); i ++) {
			Node jobNode = jobNodesList.item(i);
			NodeList jobChildNodesList = jobNode.getChildNodes();
			for (int j = 0; j < jobChildNodesList.getLength(); j ++) {
				Node jobChildNode = jobChildNodesList.item(j);
				String jobChileNodeName = jobChildNode.getNodeName();
				if (jobChileNodeName.equals("status")) {
					jobStatues = jobChildNode.getTextContent();
					break;
				}
			}
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
							if(appChildNode.getNodeName().equals("review1") || appChildNode.getNodeName().equals("review2")) {
								NodeList reviewChildNodesList =  appChildNode.getChildNodes();
								for (int m = 0; m < reviewChildNodesList.getLength(); m ++) {
									Node reviewChildNode = reviewChildNodesList.item(m);
									String reviewChildNodeName = reviewChildNode.getNodeName();
									if (reviewChildNodeName.equals("reviewId")) {
										foundRevId = reviewChildNode.getTextContent();
										break;
									}
								}
								if (foundRevId.equals(reviewId)) {
									if (jobStatues.equals("in-review")) {
										for (int m = 0; m < reviewChildNodesList.getLength(); m ++) {
											Node reviewChildNode = reviewChildNodesList.item(m);
											String reviewChildNodeName = reviewChildNode.getNodeName();
											if (reviewChildNodeName.equals("comment") && comment != null)
												reviewChildNode.setTextContent(comment);
											else if (reviewChildNodeName.equals("recommend") && recommend != null)
												reviewChildNode.setTextContent(recommend);
										}
										// write the content into xml file
										Transformer transformer = TransformerFactory.newInstance().newTransformer();
										transformer.transform(new DOMSource(doc), new StreamResult(file));
										return reviewId;
									} else
										return "cannot update due to the job statues";
								}
							}
						}
					}
				}
			}
		}
		return "no review found";
	}
	
	static public List<Review> getReviews(String id) throws Exception {
		List<Review> results = new ArrayList<>();
		File file = new File("jobs.xml");
		if (! file.exists())
			return results;
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
							String appChildNodeName = appChildNode.getNodeName();
							if (appChildNodeName.equals("appId") && appChildNode.getTextContent().equals(id)) {
								// add all its reviews to results list
								for (int ll = 0; ll < appChildNodesList.getLength(); ll ++) {
									appChildNode = appChildNodesList.item(ll);
									appChildNodeName = appChildNode.getNodeName();
									if (appChildNodeName.equals("review1") || appChildNodeName.equals("review2")) {
										NodeList reviewChildNodesList =  appChildNode.getChildNodes();
										Review review = new Review();
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
										results.add(review);
									}
								}
								return results;
							} else if (appChildNodeName.equals("review1") || appChildNodeName.equals("review2")) {
								NodeList reviewChildNodesList =  appChildNode.getChildNodes();
								for (int m = 0; m < reviewChildNodesList.getLength(); m ++) {
									Node reviewChildNode = reviewChildNodesList.item(m);
									String reviewChildNodeName = reviewChildNode.getNodeName();
									if (reviewChildNodeName.equals("reviewId") && reviewChildNode.getTextContent().equals(id)) {
										// add this review to the list
										Review review = new Review();
										for (int mm = 0; mm < reviewChildNodesList.getLength(); mm ++) {
											reviewChildNode = reviewChildNodesList.item(mm);
											reviewChildNodeName = reviewChildNode.getNodeName();
											if (reviewChildNodeName.equals("reviewId"))
												review.setReviewId(reviewChildNode.getTextContent());
											else if (reviewChildNodeName.equals("reviewer"))
												review.setReviewer(reviewChildNode.getTextContent());
											else if (reviewChildNodeName.equals("comment"))
												review.setComment(reviewChildNode.getTextContent());
											else if (reviewChildNodeName.equals("recommend"))
												review.setRecommend(reviewChildNode.getTextContent());
										}
										results.add(review);
										return results;
									} else if (reviewChildNodeName.equals("reviewer") && reviewChildNode.getTextContent().equals(id)) {
										// add this review to the list and don't return now
										Review review = new Review();
										for (int mm = 0; mm < reviewChildNodesList.getLength(); mm ++) {
											reviewChildNode = reviewChildNodesList.item(mm);
											reviewChildNodeName = reviewChildNode.getNodeName();
											if (reviewChildNodeName.equals("reviewId"))
												review.setReviewId(reviewChildNode.getTextContent());
											else if (reviewChildNodeName.equals("reviewer"))
												review.setReviewer(reviewChildNode.getTextContent());
											else if (reviewChildNodeName.equals("comment"))
												review.setComment(reviewChildNode.getTextContent());
											else if (reviewChildNodeName.equals("recommend"))
												review.setRecommend(reviewChildNode.getTextContent());
										}
										results.add(review);
									}
								}
							}
						}
					}
				}
			}
		}
		return results;
	}
	
	static public List<Review> getAllReviews() throws Exception {
		List<Review> results = new ArrayList<>();
		File file = new File("jobs.xml");
		if (! file.exists())
			return results;
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
					NodeList appNodeList = jobChildNode.getChildNodes();
					for (int k = 0; k < appNodeList.getLength(); k ++) {
						Node appNode = appNodeList.item(k);
						NodeList appChildNodesList = appNode.getChildNodes();
						for (int l = 0; l < appChildNodesList.getLength(); l ++) {
							Node appChildNode = appChildNodesList.item(l);
							String appChildNodeName = appChildNode.getNodeName();
							if (appChildNodeName.equals("review1")) {
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
								results.add(review);
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
								results.add(review);
							}
						}
					}
				}
			}
		}
		return results;
	}
}
