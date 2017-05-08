package au.edu.unsw.soacourse.pollingservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import javax.ws.rs.core.Response;

import au.edu.unsw.soacourse.pollingservice.dao.PollsDao;
import au.edu.unsw.soacourse.pollingservice.dao.VotesDao;
import au.edu.unsw.soacourse.pollingservice.model.DBHandler;
import au.edu.unsw.soacourse.pollingservice.model.Poll;
import au.edu.unsw.soacourse.pollingservice.model.Vote;


@Path("/polls")
public class PollsResource {

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public Response newPoll(
			//@FormParam("id") String id,
			@FormParam("title") String title,
			@FormParam("description") String description,
			@FormParam("optiontype") String optionType,
			@FormParam("option") List<String> options,
			@FormParam("comments") String comments
			
	) throws IOException {
		
		Poll p = new Poll(title);
		
		if(description!=null&&!description.isEmpty()){
			p.setDescription(description);
		}else{
			description = " ";
			p.setDescription(description);
		}
		if(optionType!=null&&!optionType.isEmpty()){
			p.setOptionType(optionType);
		}else{
			optionType = " ";
			p.setOptionType(optionType);
		}		
		p.setOptions(options);
		if(comments!=null&&!comments.isEmpty()){
			p.setComments(comments);
		}else{
			comments = " ";
			p.setComments(comments);
		}
		p.setFinalChoice(" ");
		//System.out.println(comments);
		PollsDao pollsdao = new PollsDao();
		String id = pollsdao.createPoll(p);
		
		if(id.equals("0")){
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		}
		
		p.setId(id);
		
		return Response.status(Response.Status.CREATED).header("Location", "http://localhost:8080/Comp9322Ass2PollingService/polls/"+id).entity(id).build();
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getPolls() {
		
		List<Poll> ps = new ArrayList<>();
		
		PollsDao pollsdao = new PollsDao();
		
		ps = pollsdao.getPolls();
		
		if(ps!=null&&!ps.isEmpty()){
			return Response.ok(ps).build();
		}
		
		return Response.status(Response.Status.NOT_FOUND).build();
			
	}
	
	@GET
	@Path("{pid}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getPoll(@PathParam("pid") String id) {
		
		Poll p = null;
		List<Poll> ps = new ArrayList<Poll>();
		
		PollsDao pollsdao = new PollsDao();
		
		ps = pollsdao.getPolls();
		
		for(int i=0;i<ps.size();i++){
			if(ps.get(i).getId().equals(id)){
				p = ps.get(i);
				break;
			}
		}
		
		if(p==null){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		return Response.ok(p).build(); 
	}
	
	@GET
	@Path("{pid}/votes")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getVotesByPid(@PathParam("pid") String id) {
		
		List<Vote> vs = new ArrayList<Vote>();
		
		VotesDao votesdao = new VotesDao();
		
		vs = votesdao.getVotesByPid(id);
				
		if(vs==null||vs.isEmpty()){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		return Response.ok(vs).build(); 
	}
	
	@GET
	@Path("{pid}/votes/{vid}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getVoteByPid(
			@PathParam("pid") String pid,
			@PathParam("vid") String vid
	) {
		
		Vote v = new Vote();
		
		VotesDao votesdao = new VotesDao();
		
		v = votesdao.getVote(vid);
		
		if(v==null){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		return Response.ok(v).build(); 
	}
	
	@GET
	@Path("/search")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response searchPoll(@QueryParam("pid") String pid) {

		Poll p = null;
		List<Poll> ps = new ArrayList<Poll>();
		
		PollsDao pollsdao = new PollsDao();
		
		ps = pollsdao.getPolls();
		
		for(int i=0;i<ps.size();i++){
			if(ps.get(i).getId().equals(pid)){
				p = ps.get(i);
				break;
			}
		}
		
		if(p==null){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		return Response.ok(p).build();
	}
	
	@PUT
	@Path("{pid}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updatePoll(
			
			@PathParam("pid") String pid,
			@FormParam("title") String title,
			@FormParam("description") String description,
			@FormParam("optiontype") String optionType,
			@FormParam("option") List<String> options,
			@FormParam("comments") String comments,
			@FormParam("finalchoice") String finalChoice
	) {
		
		String id = null;
		
		PollsDao pollsdao = new PollsDao();
		
		Poll p = new Poll();
		
		p.setId(pid);
		p.setTitle(title);
		p.setDescription(description);
		p.setOptionType(optionType);
		p.setOptions(options);
		p.setComments(comments);
		p.setFinalChoice(finalChoice);
		
		id = pollsdao.updatePoll(p);
		
		if(id.equals("no pid")){
			return Response.status(Response.Status.NOT_FOUND).build();
		}else if(id.equals("exist votes")){
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}
		
		//return Response.status(Response.Status.OK).header("Location", "http://localhost:8080/Comp9322Ass2PollingService/polls/"+id).build();
		return Response.ok(p).build();
	}
	
	@DELETE
	@Path("{pid}")
	public Response deletePoll(@PathParam("pid") String id) {
		
		PollsDao pollsdao = new PollsDao();
		
		//System.out.println(title);
		String did = null;
		
		did = pollsdao.deletePoll(id);
		
		if(did.equals("no pid")){
			return Response.status(Response.Status.NOT_FOUND).build();

		}else if(did.equals("exist votes")){
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}
		
		return Response.status(Response.Status.NO_CONTENT).build();
		
	}
	
}
