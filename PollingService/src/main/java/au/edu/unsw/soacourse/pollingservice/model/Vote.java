package au.edu.unsw.soacourse.pollingservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Vote {
	
	private String id;
	private String pid;
	private String participantName;
	private String chosenOption;
	
	public Vote(){
		
	}
	
	public Vote(String chosenOption){
		this.chosenOption = chosenOption;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getChosenOption() {
		return chosenOption;
	}
	public void setChosenOption(String chosenOption) {
		this.chosenOption = chosenOption;
	}
	
}
