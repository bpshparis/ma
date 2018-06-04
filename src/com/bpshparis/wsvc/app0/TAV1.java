package com.bpshparis.wsvc.app0;

import java.util.HashMap;
import java.util.Map;

public class TAV1 {

	String content;
	Map<String, Double> emotion = new HashMap<String, Double>();
	Map<String, Double> language = new HashMap<String, Double>();
	Map<String, Double> social = new HashMap<String, Double>();

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Map<String, Double> getEmotion() {
		return emotion;
	}
	public void setEmotion(Map<String, Double> emotion) {
		this.emotion = emotion;
	}
	public Map<String, Double> getLanguage() {
		return language;
	}
	public void setLanguage(Map<String, Double> language) {
		this.language = language;
	}
	public Map<String, Double> getSocial() {
		return social;
	}
	public void setSocial(Map<String, Double> social) {
		this.social = social;
	}
	
}
