package com.bpshparis.wsvc.app0;

import java.util.ArrayList;
import java.util.List;

public class TR {
	
	String text;
	List<Word> words = new ArrayList<Word>();
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<Word> getWords() {
		return words;
	}
	public void setWords(List<Word> words) {
		this.words = words;
	}
	
}
