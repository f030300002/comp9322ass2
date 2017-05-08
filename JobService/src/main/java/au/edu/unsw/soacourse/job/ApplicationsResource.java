package au.edu.unsw.soacourse.job;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.job.dao.ApplicationsDao;
import au.edu.unsw.soacourse.job.model.Application;

@Path("/apps")
public class ApplicationsResource {
	@Context
	UriInfo uriInfo;
	
	// create an application
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public Response newApplication(
			@FormParam("jobId") String jobId,
			@FormParam("candidateId") String candidateId,
			@FormParam("coverLetter") String coverLetter
	) throws Exception {
		Response res;
		String appId = null;
		appId = ApplicationsDao.newApplication(new Application(jobId, candidateId, coverLetter));
		
		if (appId == null)
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		else if (appId.equals("no job found"))
			res = Response.status(Response.Status.NOT_FOUND).build();
		else
			res = Response.created(new URI(uriInfo.getBaseUri() + "apps/" + appId)).entity(appId).build();
		
		return res;
	}
	
	// update an application
	@PUT
	@Path("/{appId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updateApplication(
			@PathParam("appId") String appId,
			@FormParam("coverLetter") String coverLetter
	) throws Exception {
		Response res;
		String wtf = null;
		wtf = ApplicationsDao.updateApplication(new Application(appId, coverLetter));
		
		if (wtf == null)
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		else if (wtf.equals("no application found"))
			res = Response.status(Response.Status.NOT_FOUND).build();
		else if (wtf.equals("cannot update due to the current status"))
			res = Response.status(Response.Status.PRECONDITION_FAILED).build();
		else
			res = Response.created(new URI(uriInfo.getBaseUri() + "apps/" + appId)).build();
		
		return res;
	}
	
	// get a single application and all applications per job
	@GET
	@Path("/{id}") // this id can be appId or jobId
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getApplication(
			@PathParam("id") String id,
			@HeaderParam("key") String key
	) throws Exception {
		Response res;
		List<Application> appsList = null;
		appsList = ApplicationsDao.getApplication(id);
		
		if (appsList == null)
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		else if (appsList.isEmpty())
			res = Response.status(Response.Status.NOT_FOUND).build();
		else
			res = Response.ok(appsList).build();
		
		return res;
	}
	
	// get all applications
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getApplication(
			@HeaderParam("key") String key
	) throws Exception {
		Response res;
		
		// check the authority
		if (key == null || ! key.equals("admin")) {
			res = Response.status(Response.Status.UNAUTHORIZED).build();
			return res;
		}
		
		List<Application> appsList = null;
		appsList = ApplicationsDao.getAllApplication();
		
		if (appsList == null)
			res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		else if (appsList.isEmpty())
			res = Response.status(Response.Status.NOT_FOUND).build();
		else
			res = Response.ok(appsList).build();
		
		return res;
	}
}
