package com.bpshparis.wsvc.app0;

import java.util.ArrayList;
import java.util.List;

public class TAV3 {

	String content;
	List<Tone> document = new ArrayList<Tone>();
	List<Tone> sentences = new ArrayList<Tone>();

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<Tone> getDocument() {
		return document;
	}
	public void setDocument(List<Tone> document) {
		this.document = document;
	}
	public List<Tone> getSentences() {
		return sentences;
	}
	public void setSentences(List<Tone> sentences) {
		this.sentences = sentences;
	}	
	
}
