package au.edu.unsw.soacourse.job.model;

import java.util.ArrayList;
import java.util.List;

public class Job {
	private String jobId;
	private String companyName;
	private String salaryRate;
	private String positionType;
	private String location;
	private String jobDescription;
	private String status;
	private List<Application> applications;
	
	public List<Application> getApplications() {
		return applications;
	}
	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getSalaryRate() {
		return salaryRate;
	}
	public void setSalaryRate(String salaryRate) {
		this.salaryRate = salaryRate;
	}
	public String getPositionType() {
		return positionType;
	}
	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void addApplication(Application application) {
		applications.add(application);
	}
	public Job() {
		applications = new ArrayList<>();
	}
	
	public Job(String companyName, String salaryRate, String positionType,
			String location, String jobDescription) {
		this.companyName = companyName;
		this.salaryRate = salaryRate;
		this.positionType = positionType;
		this.location = location;
		this.jobDescription = jobDescription;
	}
	public Job(String jobId, String companyName, String salaryRate,
			String positionType, String location, String jobDescription,
			String status) {
		this.jobId = jobId;
		this.companyName = companyName;
		this.salaryRate = salaryRate;
		this.positionType = positionType;
		this.location = location;
		this.jobDescription = jobDescription;
		this.status = status;
	}
	
	
}
