package au.edu.unsw.soacourse.job.model;


public class Application {
	private String appId;
	private String candidate;
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
	public String getCandidate() {
		return candidate;
	}
	public void setCandidate(String candidate) {
		this.candidate = candidate;
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
