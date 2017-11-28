package com.bpshparis.wsvc.app0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DSC {

	Map<String, Object> sentiment = new HashMap<String, Object>();
	List<Entity> entities = new ArrayList<Entity>();
	List<Concept> concepts = new ArrayList<Concept>();
	List<Category> categories = new ArrayList<Category>();

	public Map<String, Object> getSentiment() {
		return sentiment;
	}
	public void setSentiment(Map<String, Object> sentiment) {
		this.sentiment = sentiment;
	}
	public List<Entity> getEntities() {
		return entities;
	}
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	public List<Concept> getConcepts() {
		return concepts;
	}
	public void setConcepts(List<Concept> concepts) {
		this.concepts = concepts;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	
}
