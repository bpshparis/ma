package com.bpshparis.wsvc.app0;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.AddDocumentOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.DocumentAccepted;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.CategoriesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DetectFacesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DetectedFaces;

/**
 * Servlet implementation class AppendSelectionsServlet
 */
@WebServlet(name = "AnalyzeServlet", urlPatterns = { "/Analyze" })
public class AnalyzeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	NaturalLanguageUnderstanding nlu;
	String nlul;
	ToneAnalyzer ta;
	String tacl;
	Discovery dsc;
	VisualRecognition wvc;
	String wvcUrl;
	String wvcClassify;
	String wvcDetect_faces;
	String dscEnvId;
	String dscCollId;
	String mailsPath;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AnalyzeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		List<Mail> mails = new ArrayList<Mail>();

		Map<String, Object> reqParms = new HashMap<String, Object>();
		Map<String, Object> datas = new HashMap<String, Object>();

		try {

			datas.put("FROM", this.getServletName());
			mailsPath = getServletContext().getRealPath("/res/mails");

			dsc = (Discovery) request.getServletContext().getAttribute("dsc");
			dscEnvId = (String) request.getServletContext().getAttribute("dscEnvId");
			dscCollId = (String) request.getServletContext().getAttribute("dscCollId");
			wvcUrl = (String) request.getServletContext().getAttribute("wvcUrl");
			wvcClassify = (String) request.getServletContext().getAttribute("wvcClassify");
			wvcDetect_faces = (String) request.getServletContext().getAttribute("wvcDetect_faces");
			
			
			if(ServletFileUpload.isMultipartContent(request)){

				List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
				for (FileItem item : items) {
					if (!item.isFormField()) {
						// item is the file (and not a field)
						if(item.getName().equalsIgnoreCase("mailsFile.zip")){
							
							File dir = new File(mailsPath);
							
							for(File file: dir.listFiles()){ 
							    if (!file.isDirectory()){ 
							    	file.delete();
							    }
							}
							
							BufferedOutputStream dest = null;
							int BUFFER = Long.bitCount(item.getSize());
							ZipInputStream zis = new    ZipInputStream(new BufferedInputStream(item.getInputStream())); 
							ZipEntry entry;
							while((entry = zis.getNextEntry()) != null) {
								System.out.println("Extracting: " + entry);
					            int count;
					            byte data[] = new byte[BUFFER];
					            // write the files to the disk
					            FileOutputStream fos = new FileOutputStream(mailsPath + "/" + entry.getName());
					            dest = new BufferedOutputStream(fos, BUFFER);
					            while ((count = zis.read(data, 0, BUFFER)) 
					              != -1) {
					               dest.write(data, 0, count);
					            }
					            dest.flush();
					            dest.close();
					        }
							zis.close();

						}
						else {
							// Let see if mails.json exists in /res/mails and load it
							Path path = Paths.get(mailsPath + "/mails.json");
							if(Files.exists(path)){
								InputStream is = new ByteArrayInputStream(Files.readAllBytes(path));
								mails = Tools.MailsListFromJSON(is);
							}
							
							path = Paths.get(mailsPath + "/analysis.json");
							if(!Files.exists(path)){
								for(Mail mail: mails){
									if(mail.getAttached() != null && dsc != null){
										if(Files.exists(Paths.get(mailsPath + "/" + mail.getAttached()))){
											datas.put("UPLOAD_" + mail.getAttached() + "_RESPONSE", uploadAttached(mail));
										}
									}
								}
							}
							
						}
						
						request.getSession().setAttribute("mails", mails);
						datas.put("MAILS", mails);
						
					}
					if (item.isFormField() && item.getFieldName().equalsIgnoreCase("parms")) {
						item.getFieldName();
			            String value = item.getString();
			            reqParms = Tools.fromJSON(new ByteArrayInputStream(value.getBytes()));
			            datas.put("REQ_PARMS", reqParms);
					}
				}
			}
			else {

				reqParms = Tools.fromJSON(request.getInputStream());

				// Load analysis in /res/mails if exists
				Path path = Paths.get(mailsPath + "/analysis.json");
				if(Files.exists(path)){
					InputStream is  = new ByteArrayInputStream(Files.readAllBytes(path));
					mails = Tools.MailsListFromJSON(is);
				}
				
				else {
					mails = (List<Mail>) request.getSession().getAttribute("mails");
						
					for(Mail mail: mails){

						ta = (ToneAnalyzer) request.getServletContext().getAttribute("ta");
						tacl = (String) request.getServletContext().getAttribute("tacl");
						if(mail.getSubject() != null && ta != null){
							callTA(mail);
						}
						
						nlu = (NaturalLanguageUnderstanding) request.getServletContext().getAttribute("nlu");
						nlul = (String) request.getServletContext().getAttribute("nlul");
						if(mail.getContent() != null && nlu != null){
							callNLU(mail);
						}
	
						wvc = (VisualRecognition) request.getServletContext().getAttribute("wvc");

						if(mail.getPicture() != null && wvc != null){
							if(Files.exists(Paths.get(mailsPath + "/" + mail.getPicture()))){
								callVR(mail);
							}
						}
	
						if(mail.getFace() != null && wvc != null){
							if(Files.exists(Paths.get(mailsPath + "/" + mail.getFace()))){
								callFR(mail);
							}
						}
	
						if(mail.getdId() != null && dsc != null){
							if(Files.exists(Paths.get(mailsPath + "/" + mail.getAttached()))){
								callDSC(mail);
							}
						}
						
					}
				}
				datas.put("MAILS", mails);
			}
		}
		
		catch(JsonMappingException e){
			e.printStackTrace();

		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			datas.put("EXCEPTION", e.getClass().getName());
			datas.put("MESSAGE", e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			datas.put("STACKTRACE", sw.toString());
		}

		finally{
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(Tools.toJSON(datas));
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	protected DocumentAccepted uploadAttached(Mail mail) throws ArrayIndexOutOfBoundsException, JsonParseException, JsonMappingException, IOException{

		Path path = Paths.get(mailsPath + "/" + mail.getAttached());
		
		if(!Files.exists(path)){
			return null;
		}
		
		InputStream in = new BufferedInputStream(new FileInputStream(path.toFile()));
		new HashMap<String, String>();

		String mt = "";
		int i = mail.getAttached().split("\\.").length;
		String ext = mail.getAttached().split("\\.")[i - 1];

		switch(ext){

			case "doc":
				mt = HttpMediaType.APPLICATION_MS_WORD;
				break;

			case "docx":
				mt = HttpMediaType.APPLICATION_MS_WORD_DOCX;
				break;
				
			case "pdf":
				mt = HttpMediaType.APPLICATION_PDF;
				break;

			default:
				mt = HttpMediaType.TEXT_PLAIN;
				break;

		}
		
//		CreateDocumentRequest.Builder builder = new CreateDocumentOptions.Builder(dscEnvId, dscCollId)
//				.file(in, mt);
//		CreateDocumentResponse result = dsc.createDocument(builder.build()).execute();
		
		try{
			AddDocumentOptions.Builder builder = new AddDocumentOptions.Builder(dscEnvId, dscCollId)
					.file(in)
					.filename(mail.getAttached())
					.fileContentType(mt);
			DocumentAccepted result = dsc.addDocument(builder.build()).execute();
	
			ObjectMapper mapper = new ObjectMapper();
	
	    	Map<String, String> resultMap = mapper.readValue(result.toString(), new TypeReference<Map<String, String>>(){});
	
	    	mail.setdId(resultMap.get("document_id"));
	
	    	return result;
		}
		catch(IllegalArgumentException | NullPointerException e){
    		System.out.println(dsc.getName() + " " + dsc.getEndPoint() + " !!! WARNING: no collection found !!!");
		}
		
		return null;

	}

	@SuppressWarnings("unchecked")
	protected String listAttachedColl() throws JsonParseException, JsonMappingException, IOException{

		List<String> fields = new ArrayList<String>();
		fields.add("extracted_metadata");

    	QueryOptions.Builder queryBuilder = new QueryOptions.Builder(dscEnvId, dscCollId)
    			.count(100)
    			.returnFields(fields);

    	QueryResponse queryResponse = dsc.query(queryBuilder.build()).execute();

    	ObjectMapper mapper = new ObjectMapper();

    	Map<String, Object> docMap = mapper.readValue(queryResponse.toString(), new TypeReference<Map<String, Object>>(){});

		List<Map<String, String>> docs = (List<Map<String, String>>) docMap.get("results");

		List<String> docIds = new ArrayList<String>();

		for(Map<String, String> doc: docs){
			docIds.add(doc.get("id"));
		}

		System.out.println("docIds=" + docIds);
		
		return queryResponse.toString();

	}

	@SuppressWarnings("unchecked")
	protected void callVR(Mail mail) throws IOException{

		Path path = Paths.get(mailsPath + "/" + mail.getPicture());
		if(!Files.exists(path)){
			return;
		}
		
		ClassifyOptions classifyImagesOptions = new ClassifyOptions.Builder()
				.imagesFile(Files.newInputStream(path))
				.imagesFilename(mail.getPicture())
				.build();
		
		ClassifiedImages visualClassification = wvc.classify(classifyImagesOptions).execute();
		
		String result = visualClassification.toString();
		
//		RequestBody body = new MultipartBody.Builder()
//				.setType(MultipartBody.FORM)
//	            .addFormDataPart("File", "images_file", RequestBody.create(MediaType.parse("image/jpeg"), path.toFile()))
//	            .build();
//
//	    Request request = new Request.Builder()
//	    		.url(wvcUrl + wvcClassify)
//	    		.post(body)
//	    		.build();
//	    
//	    Response response = wvc.newCall(request).execute();
//	    String result = response.body().string();
	    
		InputStream is = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8.name()));
		
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Map<String, Object> svc = mapper.readValue(br, new TypeReference<Map<String, Object>>(){});
        
        List<Object> images = (List<Object>) svc.get("images");
		
		Map<String, Object> images0 = (Map<String, Object>) images.get(0);
		
		List<Object> classifiers = (List<Object>) images0.get("classifiers");
		
		if(classifiers != null){
		
			Map<String, Object> classifiers0 = (Map<String, Object>) classifiers.get(0);
			
			String json = mapper.writeValueAsString(classifiers0.get("classes"));
			
			List<VRClass> classes = Arrays.asList(mapper.readValue(json, VRClass[].class));
			
			for(VRClass vrClass: classes){
				vrClass.setPicture(mail.getPicture());
			}
			
			VR vr = new VR();
			vr.setClasses(classes);
			
			mail.getAnalysis().setVr(vr);
			
		}
		
		return;
	}

	@SuppressWarnings("unchecked")
	protected void callFR(Mail mail) throws IOException{

		Path path = Paths.get(mailsPath + "/" + mail.getFace());
		if(!Files.exists(path)){
			return;
		}

		DetectFacesOptions options = new DetectFacesOptions.Builder()
				.imagesFile(Files.newInputStream(path))
				.imagesFilename(mail.getFace())
				.build();

		DetectedFaces detectedFaces = wvc.detectFaces(options).execute();
		
		String result = detectedFaces.toString();

//		RequestBody body = new MultipartBody.Builder()
//				.setType(MultipartBody.FORM)
//	            .addFormDataPart("File", "images_file", RequestBody.create(MediaType.parse("image/jpeg"), path.toFile()))
//	            .build();
//
//	    Request request = new Request.Builder()
//	    		.url(wvcUrl + wvcDetect_faces)
//	    		.post(body)
//	    		.build();
//	    
//	    Response response = wvc.newCall(request).execute();
//	    String result = response.body().string();
		
		InputStream is = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8.name()));
		
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Map<String, Object> svc = mapper.readValue(br, new TypeReference<Map<String, Object>>(){});
		
		List<Object> images = (List<Object>) svc.get("images");
		
		Map<String, Object> image0 = (Map<String, Object>) images.get(0);
		
		List<Map<String, Object>> fs = (List<Map<String, Object>>) image0.get("faces");
		
		List<Face> faces= new ArrayList<Face>();
		
		for(Map<String, Object> f: fs){
			Face face = new Face();
			Map<String, Object> age = (Map<String, Object>) f.get("age");
			face.setAgeMax((Integer) age.get("max"));
			face.setAgeMin((Integer) age.get("min"));
			face.setAgeScore((Double) age.get("score"));
			Map<String, Object> gender = (Map<String, Object>) f.get("gender");
			face.setGender((String) gender.get("gender"));
			face.setGenderScore((Double) gender.get("score"));
			Map<String, Object> identity = (Map<String, Object>) f.get("identity");
			if(identity != null){
				face.setIdentityName((String) identity.get("name"));
				face.setIdentityScore((Double) identity.get("score"));
				face.setIdentityTypeHierarchy((String) identity.get("type_hierarchy"));
			}
			Map<String, Object> location = (Map<String, Object>) f.get("face_location");
			face.setLocationHeight((Double) location.get("height"));
			face.setLocationLeft((Double) location.get("left"));
			face.setLocationTop((Double) location.get("top"));
			face.setLocationWidth((Double) location.get("width"));
			
			face.setFace(mail.getFace());
			faces.add(face);
		}
		
		FR fr = new FR();
		fr.setFaces(faces);

		mail.getAnalysis().setFr(fr);

		return;
	}

	@SuppressWarnings("unchecked")
	protected QueryResponse callDSC(Mail mail) throws IOException{

		List<String> fields = new ArrayList<String>();
		fields.add("extracted_metadata");

    	QueryOptions.Builder queryBuilder = new QueryOptions.Builder(dscEnvId, dscCollId)
    			.aggregation("term(enriched_text)")
    			.filter("_id:" + mail.getdId());

    	QueryResponse queryResponse = dsc.query(queryBuilder.build()).execute();
    	
    	String result = queryResponse.toString();
    	
    	InputStream is = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8.name()));

		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
        Map<String, Object> svc = mapper.readValue(br, new TypeReference<Map<String, Object>>(){});
		
        List<Object> results = (List<Object>) svc.get("results");
        
        if(results.size() > 0){
		
	        Map<String, Object> result0 = (Map<String, Object>) results.get(0);
			
	        Map<String, Object> enriched_text = (Map<String, Object>) result0.get("enriched_text");
	        
	        String docLabel = null;
	        Float docScore = 0F;
	        
	        Map<String, Object> sentiment = (Map<String, Object>) enriched_text.get("sentiment");
	        Map<String, Object> document = (Map<String, Object>) sentiment.get("document");
	        
	        
	        docLabel = (String) document.get("label");
	        docScore = ((Double) document.get("score")).floatValue();
	        
			String json = mapper.writeValueAsString(enriched_text.get("entities"));
			
			List<Entity> entities = Arrays.asList(mapper.readValue(json, Entity[].class));
			
			for(Entity entity: entities){
				entity.setAttached(mail.getAttached());
				entity.setDocLabel(docLabel);
				entity.setDocScore(docScore);
			}
			
			json = mapper.writeValueAsString(enriched_text.get("categories"));
			
			List<Category> categories = Arrays.asList(mapper.readValue(json, Category[].class));
			
			for(Category category: categories){
				category.setAttached(mail.getAttached());
				category.setDocLabel(docLabel);
				category.setDocScore(docScore);
			}		
			
			json = mapper.writeValueAsString(enriched_text.get("concepts"));
			
			List<Concept> concepts = Arrays.asList(mapper.readValue(json, Concept[].class));
			
			for(Concept concept: concepts){
				concept.setAttached(mail.getAttached());
				concept.setDocLabel(docLabel);
				concept.setDocScore(docScore);
			}		
			
			DSC dsc = new DSC();
			dsc.setSentiment(sentiment);
			dsc.setEntities(entities);
			dsc.setCategories(categories);
			dsc.setConcepts(concepts);
	    	
	    	mail.getAnalysis().setDsc(dsc);
	    	
        }

		return queryResponse;
	}

	@SuppressWarnings("unchecked")
	protected ToneAnalysis callTA(Mail mail) throws IOException{
		
		if(mail.getSubject() == null){
			return null;
		}

		ToneOptions options = new ToneOptions.Builder()
				.text(mail.getSubject())
				.contentLanguage(tacl)
				.acceptLanguage("en")
				.build();

		ToneAnalysis toneAnalysis = ta.tone(options).execute();
		
		String result = toneAnalysis.toString();
		
		InputStream is = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8.name()));
		
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Map<String, Object> json = mapper.readValue(br, new TypeReference<Map<String, Object>>(){});

		Map<String, Object> document_tone = (Map<String, Object>) json.get("document_tone");
		
		List<Tone> documentTones = new ArrayList<Tone>();

		Map<String, Double> emotion = new HashMap<String, Double>();
		Map<String, Double> language = new HashMap<String, Double>();
		
		if(document_tone != null){
			List<Map<String, Object>> tonesForDocument = (List<Map<String, Object>>) document_tone.get("tones");
			
			if(tonesForDocument != null){
				
				documentTones = Arrays.asList(mapper.readValue(Tools.toJSON(tonesForDocument), Tone[].class));
				
				
				for(Tone documentTone: documentTones){
					switch(documentTone.getTone_id().toLowerCase()){
					
					case "joy": case"sadness": case "disgust": case "anger":  case "fear":
						emotion.put(documentTone.getTone_id(), documentTone.getScore());
						break;
					case  "analytical": case "confident": case "tentative":
						language.put(documentTone.getTone_id(), documentTone.getScore());
						break;
					}
				}
				
			}
		}
		
		TAV1 tav1 = new TAV1();
		tav1.setContent(mail.getSubject());
		tav1.setEmotion(emotion);
		tav1.setLanguage(language);

		mail.getAnalysis().setTav1(tav1);

		
//		List<Map<String, Object>> sentences_tone = (List<Map<String, Object>>) json.get("sentences_tone");
//
//		List<Tone> sentencesTones = new ArrayList<Tone>();
//		
//		if(sentences_tone != null){
//		
//			for(Map<String, Object> sentence: sentences_tone){
//				String sentenceText = (String) sentence.get("text");
//				
//				List<Map<String, Object>> tonesForOneSentence = (List<Map<String, Object>>) sentence.get("tones");
//				
//				if(tonesForOneSentence != null){
//					List<Tone> sentenceTones = Arrays.asList(mapper.readValue(Tools.toJSON(tonesForOneSentence), Tone[].class));
//				
//					System.out.println("++++++ sentenceTones = " + sentenceTones);
//					
//					for(Tone sentenceTone: sentenceTones){
//						sentenceTone.setText(sentenceText);
//						sentencesTones.add(sentenceTone);
//					}
//				}
//			}
//		}
		
//		TAV3 tav3 = new TAV3();
//		tav3.setContent(mail.getSubject());
//		tav3.setDocument(documentTones);
//		tav3.setSentences(sentencesTones);
		
//		mail.getAnalysis().setTav3(tav3);
		
		return toneAnalysis;
	}

	@SuppressWarnings("unchecked")
	protected AnalysisResults callNLU(Mail mail) throws IOException{

		if(mail.getContent() == null){
			return null;
		}
		
		EntitiesOptions entitiesOptions = new EntitiesOptions.Builder()
			.emotion(true)
			.sentiment(true)
			.limit(2)
			.build();

		KeywordsOptions keywordsOptions = new KeywordsOptions.Builder()
			.emotion(true)
			.sentiment(true)
			.limit(2)
			.build();

		CategoriesOptions categoriesOptions = new CategoriesOptions();

		Features features = new Features.Builder()
			.categories(categoriesOptions)
			.entities(entitiesOptions)
			.keywords(keywordsOptions)
			.build();

		AnalyzeOptions parameters = new AnalyzeOptions.Builder()
			.features(features)
			.language(nlul)
			.text(mail.getContent())
			.build();

		AnalysisResults analysisResults = nlu
			.analyze(parameters)
			.execute();
		
		String result = analysisResults.toString();
		
		InputStream is = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8.name()));
		
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Map<String, Object> svc = mapper.readValue(br, new TypeReference<Map<String, Object>>(){});

		String language = (String) svc.get("language");
		
		String json = mapper.writeValueAsString((List<Object>) svc.get("entities"));
		List<Entity> entities = Arrays.asList(mapper.readValue(json, Entity[].class));
		for(Entity entity: entities){
			entity.setContent(mail.getContent());
			entity.setLanguage(language);
		}
		
		json = mapper.writeValueAsString((List<Object>) svc.get("keywords"));
		List<Keyword> keywords = Arrays.asList(mapper.readValue(json, Keyword[].class));
		for(Keyword keyword: keywords){
			keyword.setContent(mail.getContent());
			keyword.setLanguage(language);
		}

		json = mapper.writeValueAsString((List<Object>) svc.get("categories"));
		List<Category> categories = Arrays.asList(mapper.readValue(json, Category[].class));
		for(Category category: categories){
			category.setContent(mail.getContent());
			category.setLanguage(language);
		}

		NLU nlu = new NLU();
		nlu.setLanguage(language);
		nlu.setEntities(entities);
		nlu.setKeywords(keywords);
		nlu.setCategories(categories);
		
		mail.getAnalysis().setNlu(nlu);

		return analysisResults;
	}

}
