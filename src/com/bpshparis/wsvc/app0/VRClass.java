package com.bpshparis.wsvc.app0;

//import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class VRClass {

	String picture;
//	@JsonAlias({"vrClass", "class"})
	String vrClass;
	Float score;
	String type_hierarchy;

	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	@JsonGetter("class")
	public String getVrClass() {
		return vrClass;
	}
	@JsonSetter("class")
	public void setVrClass(String vrClass) {
		this.vrClass = vrClass;
	}
	public Float getScore() {
		return score;
	}
	public void setScore(Float score) {
		this.score = score;
	}
	public String getType_hierarchy() {
		return type_hierarchy;
	}
	public void setType_hierarchy(String type_hierarchy) {
		this.type_hierarchy = type_hierarchy;
	}	
	
}
