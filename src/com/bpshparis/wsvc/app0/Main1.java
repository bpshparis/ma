package com.bpshparis.wsvc.app0;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.SentimentOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

public class Main1 {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		// TODO Auto-generated method stub
		
//		String text = "On en a gros ! On fait un cul de chouette ?";
		String text = "Team, I know that times are tough! Product sales have "
			    + "been disappointing for the past three quarters. We have a "
			    + "competitive product, but we need to do a better job of selling it!";
//		ToneAnalyzer ta = new ToneAnalyzer("2017-09-21", "678a9d54-0e6a-47e6-af04-8709a3e9650d", "8AdWSymOdAL8");
//		ToneAnalyzer ta = new ToneAnalyzer("2016-05-19", "1d570aa8-71cc-4580-9303-b3990d674d56", "7YKtO5OJZFyB");
//		ta.setEndPoint("https://gateway.watsonplatform.net/tone-analyzer/api");
		
//		ToneOptions toneOptions = new ToneOptions.Builder()
//				.contentLanguage("fr")
//				.acceptLanguage("fr")
//				.text(text)
//				.build();
//	    ToneAnalysis result = ta.tone(toneOptions).execute();
	    
//	    System.out.println(result.toString());
//	    
//		InputStream is = new ByteArrayInputStream(result.toString().getBytes(StandardCharsets.UTF_8.name()));
		
		
	    Path path = Paths.get("/opt/wks/ma/ta.resp.json");
		InputStream is = new ByteArrayInputStream(Files.readAllBytes(path));
		
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Map<String, Object> svc = mapper.readValue(br, new TypeReference<Map<String, Object>>(){});

		Map<String , Object> result = new HashMap<String, Object>();
		result.put("content", text);
        
		Map<String, Object> response = (Map<String, Object>) svc.get("document_tone");
		List<Map<String, Object>> docTones = (List<Map<String, Object>>) response.get("tones");
		
		List<Tone> docTonesList = Arrays.asList(mapper.readValue(Tools.toJSON(docTones), Tone[].class));

		Map<String, Double> emotion = new HashMap<String, Double>();
		Map<String, Double> language = new HashMap<String, Double>();
		
		for(Tone tone: docTonesList){
			switch(tone.getTone_id().toLowerCase()){
			
			case "joy": case"sadness": case "disgust": case "anger":  case "fear":
				emotion.put(tone.getTone_id(), tone.getScore());
				break;
			case  "analytical": case "confident": case "tentative":
				language.put(tone.getTone_id(), tone.getScore());
				break;
			}
		}
		
		result.put("emotion", emotion);
		result.put("language", language);
		
		System.out.println(Tools.toJSON(result));
		
//		List<Map<String, Object>> sentences = (List<Map<String, Object>>) svc.get("sentences_tone");
//
//		List<Tone> sentsTonesList = new ArrayList<Tone>();
//		
//		for(Map<String, Object> sentence: sentences){
//			String sentText = (String) sentence.get("text");
//			
//			List<Map<String, Object>> sentTones = (List<Map<String, Object>>) sentence.get("tones");
//			
//			List<Tone> sentTonesList = Arrays.asList(mapper.readValue(Tools.toJSON(sentTones), Tone[].class));
//			for(Tone tone: sentTonesList){
//				tone.setText(sentText);
//				sentsTonesList.add(tone);
//			}
//			
//			
//		}
//		
//		TAV3 tav3 = new TAV3();
//		tav3.setContent(text);
//		tav3.setDocument(docTonesList);
//		tav3.setSentences(sentsTonesList);
//		
//		Map<String, Object> output = new HashMap<String, Object>();
//		output.put("DOCUMENT", docTonesList);
//		output.put("SENTENCES", sentsTonesList);
		
		
	}

}
