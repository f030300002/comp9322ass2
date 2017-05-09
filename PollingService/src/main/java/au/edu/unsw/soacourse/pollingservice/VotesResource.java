package au.edu.unsw.soacourse.pollingservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import au.edu.unsw.soacourse.pollingservice.dao.PollsDao;
import au.edu.unsw.soacourse.pollingservice.dao.VotesDao;
import au.edu.unsw.soacourse.pollingservice.model.Poll;
import au.edu.unsw.soacourse.pollingservice.model.Vote;

@Path("/votes")
public class VotesResource {
	
	@GET
	@Path("{vid}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getVote(@PathParam("vid") String id) {
		
		Vote v = null;
		
		VotesDao votesdao = new VotesDao();
		//System.out.println(id);
		v = votesdao.getVote(id);
		
		if(v==null){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		return Response.ok(v).build();
	}
	
	@GET
	@Path("/poll/{pid}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getVotesByPid(@PathParam("pid") String id) {
		
		List<Vote> vs = new ArrayList<Vote>();
		
		Vote v = null;
		
		VotesDao votesdao = new VotesDao();
		
		vs = votesdao.getVotesByPid(id);

		if(vs==null||vs.isEmpty()){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		return Response.ok(vs).build();
	}
	
	@POST
	@Path("{pid}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public Response newVote(
			//@FormParam("id") String id,
			@PathParam("pid") String pid,
			@FormParam("participantname") String participantName,
			@FormParam("chosenoption") String chosenOption
			
	) throws IOException {
		
		Vote v = new Vote();
		
		v.setPid(pid);
		v.setParticipantName(participantName);
		v.setChosenOption(chosenOption);

		VotesDao votesdao = new VotesDao();
		String id = votesdao.createVote(v);
		
		if(id.equals("0")){
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
		return Response.status(Response.Status.CREATED).header("Location", "http://localhost:8080/Comp9322Ass2PollingService/votes/"+id).entity(id).build();
		
	}
	
	@PUT
	@Path("{vid}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updateVote(
			@PathParam("vid") String vid,
			@FormParam("pid") String pid,
			@FormParam("participantname") String participantName,
			@FormParam("chosenoption") String chosenOption
	) {
		
		String id = null;
		
		VotesDao votesdao = new VotesDao();
		
		Vote v = new Vote();
		
		v.setId(vid);
		v.setPid(pid);
		v.setParticipantName(participantName);
		v.setChosenOption(chosenOption);
		
		id = votesdao.updateVote(v);
		
		if(id.equals("no vid")){
			return Response.status(Response.Status.NOT_FOUND).build();
		}else if(id.equals("has final choice")){
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}
		
		//return Response.status(Response.Status.OK).header("Location", "http://localhost:8080/Comp9322Ass2PollingService/polls/"+id).build();
		return Response.ok(v).build();
	}
	
}
