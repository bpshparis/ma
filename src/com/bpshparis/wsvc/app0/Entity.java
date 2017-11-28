package com.bpshparis.wsvc.app0;

import java.util.HashMap;
import java.util.Map;

public class Entity {

	String content;
	String attached;
	String docLabel;
	Float docScore;
	String language;
	String type;
	Float relevance;
	String text;
	Integer count;
	Map<String, Float> emotion = new HashMap<String, Float>();
	Map<String, Object> sentiment = new HashMap<String, Object>(); 
	Map<String, Object> disambiguation = new HashMap<String, Object>(); 
	
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
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Float getRelevance() {
		return relevance;
	}
	public void setRelevance(Float relevance) {
		this.relevance = relevance;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Map<String, Float> getEmotion() {
		return emotion;
	}
	public void setEmotion(Map<String, Float> emotion) {
		this.emotion = emotion;
	}
	public Map<String, Object> getSentiment() {
		return sentiment;
	}
	public void setSentiment(Map<String, Object> sentiment) {
		this.sentiment = sentiment;
	}
	public Map<String, Object> getDisambiguation() {
		return disambiguation;
	}
	public void setDisambiguation(Map<String, Object> disambiguation) {
		this.disambiguation = disambiguation;
	}
	
}
