package com.bpshparis.wsvc.app0;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test1 {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		Path path = Paths.get("/opt/wks/ma/WebContent/res/tr.json");
//		Charset charset = StandardCharsets.UTF_8;
//		
//		String s = new String(Files.readAllBytes(path));
//		
//		System.out.println(s.replaceAll("[\\s]", ""));
		
		
//		String s = "\\\\\"test";    // the actual String is: \\"test
//		System.out.println(s);
//		System.out.println(s.replaceAll("\\\\\\\\\"", "\""));
		
		InputStream is  = new ByteArrayInputStream(Files.readAllBytes(path));
		
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        Map<String, Object> svc = mapper.readValue(br, new TypeReference<Map<String, Object>>(){});
		
		
		List<Map<String, Object>> images = ((List<Map<String, Object>>) svc.get("images")); 
		
		List<Word> words = new ArrayList<Word>();
		
		for(Map<String, Object> image: images){
			String text = ((String) image.get("text"));
			List<Map<String, Object>> ws = (List<Map<String, Object>>) image.get("words");
			for(Map<String, Object> w: ws){
				Word word = new Word();
				word.setText(text);
				word.setLine_number((Integer) w.get("line_number"));
				word.setWord((String) w.get("word"));
				word.setScore((Double) w.get("score"));
				Map<String, Integer> location = (Map<String, Integer>) w.get("location");
				word.setLocationHeight(location.get("height"));
				word.setLocationLeft(location.get("left"));
				word.setLocationTop(location.get("top"));
				word.setLocationWidth(location.get("width"));

				word.setTip("");
				words.add(word);
			}
		}
		
		TR tr = new TR();
		tr.setWords(words);
		
		System.out.println(mapper.writeValueAsString(tr));
		
	
//		
//		
//		List<Entity> entities = Arrays.asList(mapper.readValue(json, Entity[].class));
//		
//		for(Entity entity: entities){
//			System.out.println(mapper.writeValueAsString(entity));
//		}
//
//		json = mapper.writeValueAsString(enriched_text.get("categories"));
//		
//		System.out.println(json);
//		
//		
//		List<Category> categories = Arrays.asList(mapper.readValue(json, Category[].class));
//		
//		for(Category category: categories){
//			System.out.println(mapper.writeValueAsString(category));
//		}
//		
//		json = mapper.writeValueAsString(enriched_text.get("concepts"));
//		
//		System.out.println(json);
//		
//		
//		List<Concept> concepts = Arrays.asList(mapper.readValue(json, Concept[].class));
//		
//		for(Concept concept: concepts){
//			System.out.println(mapper.writeValueAsString(concept));
//		}
//		
//		Map<String, Object> sentiment = (Map<String, Object>) enriched_text.get("sentiment");
//		
//		System.out.println(mapper.writeValueAsString(sentiment));
//		
//		DSC dsc = new DSC();
//		dsc.setSentiment(sentiment);
//		dsc.setEntities(entities);
//		dsc.setCategories(categories);
//		dsc.setConcepts(concepts);
//		
//		System.out.println("dsc=" + mapper.writeValueAsString(dsc));
		
		
//		
//		System.out.println(ents.get(0).getRelevance());
		
//		System.out.println("ents=" + mapper.writeValueAsString(ents));
//		
//		List<Object> keywords = (List<Object>) svc.get("keywords");
//		json = mapper.writeValueAsString(keywords);
//		List<Keyword> kwords = Arrays.asList(mapper.readValue(json, Keyword[].class));
//
//		System.out.println("kwords=" + mapper.writeValueAsString(kwords));
//		
//		List<Object> categories = (List<Object>) svc.get("categories");
//		json = mapper.writeValueAsString(categories);
//		List<Category> cats = Arrays.asList(mapper.readValue(json, Category[].class));
//
//		System.out.println("cats=" + mapper.writeValueAsString(cats));
//		
//		String language = (String) svc.get("language");
//		
//		NLU nlu = new NLU();
//		nlu.setLanguage(language);
//		nlu.setEntities(ents);
//		nlu.setKeywords(kwords);
//		nlu.setCategories(cats);
//		
//		System.out.println("nlu=" + mapper.writeValueAsString(nlu));
//		
		
		
//		Map<String, Float> emotion = (Map<String, Float>) entities.get(0);

//		String jsonTones = mapper.writeValueAsString(tones);
//		
//		System.out.println("jsonTones=" + jsonTones);
//		
//		List<EmotionTone> etones = Arrays.asList(mapper.readValue(jsonTones, EmotionTone[].class));
//		
//		for(EmotionTone etone: etones){
//			System.out.println(etone.getTone_name());
//		}
		
	}
	
}
