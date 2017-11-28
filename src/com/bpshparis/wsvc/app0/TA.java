package com.bpshparis.wsvc.app0;

import java.util.HashMap;
import java.util.Map;

public class TA {

	String content;
	Map<String, Float> emotion = new HashMap<String, Float>();
	Map<String, Float> language = new HashMap<String, Float>();
	Map<String, Float> social = new HashMap<String, Float>();

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Map<String, Float> getEmotion() {
		return emotion;
	}
	public void setEmotion(Map<String, Float> emotion) {
		this.emotion = emotion;
	}
	public Map<String, Float> getLanguage() {
		return language;
	}
	public void setLanguage(Map<String, Float> language) {
		this.language = language;
	}
	public Map<String, Float> getSocial() {
		return social;
	}
	public void setSocial(Map<String, Float> social) {
		this.social = social;
	}
	
}
