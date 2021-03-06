package au.edu.unsw.soacourse.job.model;

public class Review {
	private String appId;
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	private String reviewId;
	private String reviewer;
	private String comment;
	private String recommend;
	
	public String getReviewId() {
		return reviewId;
	}
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	public String getReviewer() {
		return reviewer;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getRecommend() {
		return recommend;
	}
	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	public Review() {
	}
	public Review(String appId, String reviewId, String comment,
			String recommend) {
		this.appId = appId;
		this.reviewId = reviewId;
		this.comment = comment;
		this.recommend = recommend;
	}
	public Review(String reviewId, String comment, String recommend) {
		this.reviewId = reviewId;
		this.comment = comment;
		this.recommend = recommend;
	}
	
}
