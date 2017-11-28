package com.bpshparis.wsvc.app0;

import java.util.HashMap;
import java.util.Map;

public class Keyword {

	String content;
	String language;
	Float relevance;
	String text;
	Map<String, Float> emotion = new HashMap<String, Float>();
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
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
	public Map<String, Float> getEmotion() {
		return emotion;
	}
	public void setEmotion(Map<String, Float> emotion) {
		this.emotion = emotion;
	}
	
}
