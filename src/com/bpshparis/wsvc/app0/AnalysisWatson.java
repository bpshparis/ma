package com.bpshparis.wsvc.app0;

import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse;
//import com.ibm.watson.developer_cloud.discovery.v1.model.query.QueryResponse;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DetectedFaces;
//import com.ibm.watson.developer_cloud.visual_recognition.v3.model.RecognizedText;
//import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;

public class AnalysisWatson{

	private AnalysisResults nlu;
	private String ta;
	private QueryResponse d;
	private ClassifiedImages vr;
	private DetectedFaces fr;
	
	public AnalysisResults getNlu() {
		return nlu;
	}
	public void setNlu(AnalysisResults result) {
		this.nlu = result;
	}
	public String getTa() {
		return ta;
	}
	public void setTa(String result) {
		this.ta = result;
	}
	public QueryResponse getD() {
		return d;
	}
	public void setD(QueryResponse result) {
		this.d = result;
	}
	public ClassifiedImages getVr() {
		return vr;
	}
	public void setVr(ClassifiedImages result) {
		this.vr = result;
	}
	public DetectedFaces getFr() {
		return fr;
	}
	public void setFr(DetectedFaces result) {
		this.fr = result;
	}
	
}
