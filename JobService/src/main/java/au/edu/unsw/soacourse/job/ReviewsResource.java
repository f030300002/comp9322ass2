package au.edu.unsw.soacourse.job;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.job.dao.ReviewsDao;
import au.edu.unsw.soacourse.job.model.Review;

@Path("/revs")
public class ReviewsResource {
	@Context
	UriInfo uriInfo;
	
	// create a review
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public Response newReview(
			@FormParam("appId") String appId,
			@FormParam("reviewer") String reviewer,
			@FormParam("comment") String comment,
			@FormParam("recommend") String recommend
	) throws Exception {
		Response res;
		String reviewId = null;
		reviewId = ReviewsDao.newReview(new Review(appId, reviewer, comment, recommend));
		
		if (reviewId == null)
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		else if (reviewId.equals("no application found"))
			res = Response.status(Response.Status.NOT_FOUND).build();
		else if (reviewId.equals("cannot create due to that there have been two reviews"))
			res = Response.status(Response.Status.PRECONDITION_FAILED).build();
		else
			res = Response.created(new URI(uriInfo.getBaseUri() + "revs/" + reviewId)).entity(reviewId).build();
		
		return res;
	}
	
	// update a review
	@PUT
	@Path("/{reviewId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updateReview(
			@PathParam("reviewId") String reviewId,
			@FormParam("comment") String comment,
			@FormParam("recommend") String recommend
	) throws Exception {
		Response res;
		String wtf = null;
		wtf = ReviewsDao.updateReview(new Review(reviewId, comment, recommend));
		
		if (wtf == null)
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		else if (wtf.equals("no review found"))
			res = Response.status(Response.Status.NOT_FOUND).build();
		else if (wtf.equals("cannot update due to the job statues"))
			res = Response.status(Response.Status.PRECONDITION_FAILED).build();
		else
			res = Response.created(new URI(uriInfo.getBaseUri() + "revs/" + reviewId)).build();
		
		return res;
	}
	
	// get reviews
	@GET
	@Path("/{id}") // this id can be reviewId or appId or reviewer
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getReviews(
			@PathParam("id") String id
	) throws Exception {
		Response res;
		List<Review> revsList = null;
		revsList = ReviewsDao.getReviews(id);
		
		if (revsList == null)
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		else if (revsList.isEmpty())
			res = Response.status(Response.Status.NOT_FOUND).build();
		else
			res = Response.ok(revsList).build();
		
		return res;
	}
}
