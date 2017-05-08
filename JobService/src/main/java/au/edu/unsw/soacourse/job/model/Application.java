package au.edu.unsw.soacourse.job.model;


public class Application {
	private String jobId;
	
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public Application(String appId, String coverLetter) {
		this.appId = appId;
		this.coverLetter = coverLetter;
	}
	public Application(String jobId, String candidateId, String coverLetter) {
		this.jobId = jobId;
		this.candidateId = candidateId;
		this.coverLetter = coverLetter;
	}
	private String appId;
	private String candidateId;
	private String coverLetter;
	private String status;
	private Review review1;
	private Review review2;
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getCandidateId() {
		return candidateId;
	}
	public void setCandidateId(String candidate) {
		this.candidateId = candidate;
	}
	public String getCoverLetter() {
		return coverLetter;
	}
	public void setCoverLetter(String coverLetter) {
		this.coverLetter = coverLetter;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Review getReview1() {
		return review1;
	}
	public void setReview1(Review review1) {
		this.review1 = review1;
	}
	public Review getReview2() {
		return review2;
	}
	public void setReview2(Review review2) {
		this.review2 = review2;
	}
	public Application() {
	}
	
}
