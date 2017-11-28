package com.bpshparis.wsvc.app0;

public class Concept {

	String content;
	String attached;
	String docLabel;
	Float docScore;
	String text;
	Float relevance;
	String dbpedia_resource;

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
	public String getDocLabel() {
		return docLabel;
	}
	public void setDocLabel(String docLabel) {
		this.docLabel = docLabel;
	}
	public Float getDocScore() {
		return docScore;
	}
	public void setDocScore(Float docScore) {
		this.docScore = docScore;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Float getRelevance() {
		return relevance;
	}
	public void setRelevance(Float relevance) {
		this.relevance = relevance;
	}
	public String getDbpedia_resource() {
		return dbpedia_resource;
	}
	public void setDbpedia_resource(String dbpedia_resource) {
		this.dbpedia_resource = dbpedia_resource;
	}
	
}
