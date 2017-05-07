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
import au.edu.unsw.soacourse.pollingservice.model.DBHandler;
import au.edu.unsw.soacourse.pollingservice.model.Poll;


@Path("/polls")
public class PollsResource {

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String newPoll(
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
		p.setId(id);
		
		return id;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<Poll> getPolls() {
		
		List<Poll> ps = new ArrayList<Poll>();
		
		PollsDao pollsdao = new PollsDao();
		
		ps = pollsdao.getPolls();
		
		return ps; 
	}
	
	@GET
	@Path("{pid}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Poll getPoll(@PathParam("pid") String id) {
		
		Poll p = null;
		List<Poll> ps = new ArrayList<Poll>();
		
		PollsDao pollsdao = new PollsDao();
		
		ps = pollsdao.getPolls();
		
		if(Integer.parseInt(id)<=ps.size()&&Integer.parseInt(id)>=0)
			p = ps.get(Integer.parseInt(id)-1);
		
		if(p==null){
			// should we need to handle this ???
		}
		
		return p; 
	}
	
	@GET
	@Path("/search")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Poll searchPollByTitle(@QueryParam("title") String title) {

		Poll p = null;
		List<Poll> ps = new ArrayList<Poll>();
		
		PollsDao pollsdao = new PollsDao();
		
		ps = pollsdao.getPolls();
		
		for(int i=0;i<ps.size();i++){
			if(ps.get(i).getTitle().equals(title)){
				p = ps.get(i);
				break;
			}
		}
		
		if(p==null){
			// should we need to handle this ???
		}
		
		return p; 
	}
	/*
	@PUT
	@Path("{title}")
	@Consumes(MediaType.APPLICATION_XML)
	public Response updatePoll(Poll p) {
		
	}*/
	
	@DELETE
	@Path("{title}")
	public void deletePollByTitle(@PathParam("title") String title) {
		
		PollsDao pollsdao = new PollsDao();
		
		//System.out.println(title);
		
		pollsdao.deletePollByTitle(title);
		
	}
	
}
