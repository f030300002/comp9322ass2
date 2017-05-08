package au.edu.unsw.soacourse.job;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.job.dao.JobsDao;
import au.edu.unsw.soacourse.job.model.Job;

@Path("/jobs")
public class JobsResource {
	@Context
	UriInfo uriInfo;
	
	// get a job by id
	@GET
	@Path("/{jobId}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getJob(
			@PathParam("jobId") String jobId,
			@HeaderParam("key") String key
	) throws Exception {
		Response res;
		Job job = null;
		job = JobsDao.getJob(jobId);
		
		if (job == null)
			res = Response.status(Response.Status.NOT_FOUND).build();
		else
			res = Response.ok(job).header("Location", uriInfo.getBaseUri() + "jobs/" + jobId).build();
		
		return res;
	}
	
	// get all jobs
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getJob(
			@HeaderParam("key") String key
	) throws Exception {
		Response res;
		
		// check the authority
		if (key == null || ! key.equals("admin")) {
			res = Response.status(Response.Status.UNAUTHORIZED).build();
			return res;
		}
		
		List<Job> jobsList = null;
		jobsList = JobsDao.getAllJobs();
		
		if (jobsList == null)
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		else if (jobsList.isEmpty())
			res = Response.ok().build();
		else
			res = Response.ok(jobsList).build();
		
		return res;
	}
	
	// create a new job
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public Response newJob(
			@FormParam("companyName") String companyName,
			@FormParam("salaryRate") String salaryRate,
			@FormParam("positionType") String positionType,
			@FormParam("location") String location,
			@FormParam("jobDescription") String jobDescription
	) throws Exception {
		Response res;
		String jobId = null;
		jobId = JobsDao.newJob(new Job(companyName, salaryRate, positionType, location, jobDescription));
		
		if (jobId == null)
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		else
			res = Response.created(new URI(uriInfo.getBaseUri() + "jobs/" + jobId)).entity(jobId).build();
		
		return res;
	}
	
	// update a job
	@PUT
	@Path("/{jobId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updateJob(
			@PathParam("jobId") String jobId,
			@FormParam("companyName") String companyName,
			@FormParam("salaryRate") String salaryRate,
			@FormParam("positionType") String positionType,
			@FormParam("location") String location,
			@FormParam("jobDescription") String jobDescription,
			@FormParam("status") String status
	) throws Exception {
		Response res;
		String wtf = null;
		wtf = JobsDao.updateJob(new Job(jobId, companyName, salaryRate, positionType, location, jobDescription, status));
		
		if (wtf == null)
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		else if (wtf.equals("no job found"))
			res = Response.status(Response.Status.NOT_FOUND).build();
		else if (wtf.equals("cannot update due to the existing applications"))
			res = Response.status(Response.Status.PRECONDITION_FAILED).build();
		else
			res = Response.created(new URI(uriInfo.getBaseUri() + "jobs/" + jobId)).build();
		
		return res;
	}
	
	// delete a job
	@DELETE
	@Path("/{jobId}")
	public Response deleteJob(@PathParam("jobId") String jobId) throws Exception {
		Response res;
		String wtf = null;
		wtf = JobsDao.deleteJob(jobId);
		
		if (wtf == null)
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		else if (wtf.equals("not found"))
			res = Response.status(Response.Status.NOT_FOUND).build();
		else
			res = Response.ok().build();
		
		return res;
	}
	
	// search jobs
	@GET
	@Path("/search")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response searchJob(
			@QueryParam ("companyName") String companyName,
			@QueryParam ("salaryRateFrom") String salaryRateFrom,
			@QueryParam ("salaryRateTo") String salaryRateTo,
			@QueryParam ("positionType") String positionType,
			@QueryParam ("location") String location
	) throws Exception {
		Response res;
		List<Job> jobsList = null;
		jobsList = JobsDao.searchJob(companyName, salaryRateFrom, salaryRateTo, positionType, location);
		
		if (jobsList == null)
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		else
			res = Response.ok(jobsList).build();
		
		return res;
	}
	
}
