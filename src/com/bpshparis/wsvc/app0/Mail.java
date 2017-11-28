package com.bpshparis.wsvc.app0;

public class Mail {

	private String subject;
	private String content;
	private String url;
	private String attached;
	private String picture;
	private String face;
	private String tip;
	private String dId;
	private Analysis analysis = new Analysis();
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAttached() {
		return attached;
	}
	public void setAttached(String attached) {
		this.attached = attached;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getFace() {
		return face;
	}
	public void setFace(String face) {
		this.face = face;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getdId() {
		return dId;
	}
	public void setdId(String dId) {
		this.dId = dId;
	}
	public Analysis getAnalysis() {
		return analysis;
	}
	public void setAnalysis(Analysis analysis) {
		this.analysis = analysis;
	}
	
}
