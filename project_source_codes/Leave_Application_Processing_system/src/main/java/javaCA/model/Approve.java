package javaCA.model;

public class Approve {
	private String decision;

	private String comment;

	public Approve() {
	}

	public Approve(String decision, String comment) {
		this.decision = decision;
		this.comment = comment;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
