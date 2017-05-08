package au.edu.unsw.soacourse.pollingservice.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Poll {

	private String id;
	private String title;
	private String description;
	private String optionType;
	private List<String> options;
	private String comments;	
	private String finalChoice;
	private List<Vote> votesInPoll;

	public Poll(){
		
	}
	
	public Poll(String title){
		this.title = title;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOptionType() {
		return optionType;
	}
	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getFinalChoice() {
		return finalChoice;
	}
	public void setFinalChoice(String finalChoice) {
		this.finalChoice = finalChoice;
	}
	public List<Vote> getVotesInPoll() {
		return votesInPoll;
	}

	public void setVotesInPoll(List<Vote> votesInPoll) {
		this.votesInPoll = votesInPoll;
	}
	
}
