package com.bpshparis.wsvc.app0;

import java.util.ArrayList;
import java.util.List;

public class NLU {

	String language;
	List<Entity> entities = new ArrayList<Entity>();
	List<Keyword> keywords = new ArrayList<Keyword>();
	List<Category> categories = new ArrayList<Category>();
	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public List<Entity> getEntities() {
		return entities;
	}
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	public List<Keyword> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	
}
