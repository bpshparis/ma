package com.bpshparis.wsvc.app0;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.DeleteDocumentOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.ListCollectionsOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.ListCollectionsResponse;
import com.ibm.watson.developer_cloud.discovery.v1.model.ListEnvironmentsOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.ListEnvironmentsResponse;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;

/**
 * Application Lifecycle Listener implementation class ContextListener
 *
 */
@WebListener
public class ContextListener implements ServletContextListener {

	InitialContext ic;
	NaturalLanguageUnderstanding nlu;
	String vcap_services;
	String realPath;
	Properties props = new Properties();
	ToneAnalyzer ta;
	Discovery dsc;
	String dscEnvId;
	String dscCollId;
	VisualRecognition wvc;
	
    /**
     * Default constructor. 
     */
    public ContextListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
       	try {
       		
    			ic = new InitialContext();
    			arg0.getServletContext().setAttribute("ic", ic);
    			realPath = arg0.getServletContext().getRealPath("/"); 
    	    	props.load(new FileInputStream(realPath + "/res/conf.properties"));
    			arg0.getServletContext().setAttribute("props", props);
    	    	
    			System.out.println("Context has been initialized...");
    			
    			if(props.getProperty("MODE").equalsIgnoreCase("demo")){
        			System.out.println("Running in demo mode...");
    			}
    			else {
	    			initVCAP_SERVICES();
	    			System.out.println("VCAP_SERVICES has been initialized...");
	
	   				initNLU();
	    			System.out.println("NLU has been initialized...");
					arg0.getServletContext().setAttribute("nlu", nlu);
					String nlul = props.getProperty("NLU_LANGUAGE");
			    	if(nlul != null && !nlul.trim().isEmpty()){
						arg0.getServletContext().setAttribute("nlul", nlul);
			    	}
	
	    			initTA();
	    			System.out.println("TA has been initialized...");
					arg0.getServletContext().setAttribute("ta", ta);
					String tacl = props.getProperty("TA_CONTENT_LANGUAGE");
			    	if(tacl != null && !tacl.trim().isEmpty()){
						arg0.getServletContext().setAttribute("tacl", tacl);
			    	}
					
	    				
					initDSC();
	    			System.out.println("DSC has been initialized...");
					arg0.getServletContext().setAttribute("dsc", dsc);
					arg0.getServletContext().setAttribute("dscEnvId", dscEnvId);
					arg0.getServletContext().setAttribute("dscCollId", dscCollId);
					
	    			if(props.getProperty("CLEAN_DCOLL_AT_STARTUP").equalsIgnoreCase("true")){
	        			System.out.println("Cleaning Discovery collections...");
	    				cleanDColl();
	    			}
					
					initWVC();
	    			System.out.println("WVC has been initialized...");
					arg0.getServletContext().setAttribute("wvc", wvc);
					arg0.getServletContext().setAttribute("wvcUrl", props.getProperty("WVC_URL"));
					arg0.getServletContext().setAttribute("wvcClassify", props.getProperty("WVC_CLASSIFY"));
					arg0.getServletContext().setAttribute("wvcDetect_faces", props.getProperty("WVC_DETECT_FACES"));
					
    			}
    			
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}    	
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    	arg0.getServletContext().removeAttribute("ic");
		System.out.println("Context has been destroyed...");    	
    }
    
    public void initVCAP_SERVICES() throws FileNotFoundException, IOException{
    	
    	String value = props.getProperty("VCAP_SERVICES");
    	
    	if(value != null && !value.trim().isEmpty()){
			Path path = Paths.get(realPath + value);
			Charset charset = StandardCharsets.UTF_8;
			if(Files.exists(path)){
				vcap_services = new String(Files.readAllBytes(path), charset);
				System.out.println("VCAP_SERVICES read from " + value + ".");
			}
    	}
    	else{
    		vcap_services = System.getenv("VCAP_SERVICES");
			System.out.println("VCAP_SERVICES read from System ENV.");
    	}

		System.out.println("vcap_services=" + vcap_services);

    }
    
    @SuppressWarnings("unchecked")
	public void initNLU() throws JsonParseException, JsonMappingException, IOException{

    	String serviceName = props.getProperty("NLU_NAME");
    	
		ObjectMapper mapper = new ObjectMapper();
		
		String url = "";
		String username = "";
		String password = "";
		String version = props.getProperty("NLU_VERSION").split("=")[1];
		
		
		Map<String, Object> input = mapper.readValue(vcap_services, new TypeReference<Map<String, Object>>(){});
		
		List<Map<String, Object>> l0s = (List<Map<String, Object>>) input.get(serviceName);
		
		for(Map<String, Object> l0: l0s){
			for(Map.Entry<String, Object> e: l0.entrySet()){
				if(e.getKey().equalsIgnoreCase("credentials")){
					System.out.println(e.getKey() + "=" + e.getValue());
					Map<String, Object> credential = (Map<String, Object>) e.getValue();
					url = (String) credential.get("url");
//					username = (String) credential.get("username");
//					password = (String) credential.get("password");
					username = "apikey";
					password = (String) credential.get("apikey");
					
				}
			}
		}
		
//		nlu = new NaturalLanguageUnderstanding(NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27, username, password);
		nlu = new NaturalLanguageUnderstanding(version, username, password);
		nlu.setEndPoint(url);
		
		System.out.println(nlu.getName() + " " + nlu.getEndPoint());
		
		return;
    }
    
    @SuppressWarnings("unchecked")
	public void initTA() throws JsonParseException, JsonMappingException, IOException{

    	String serviceName = props.getProperty("TA_NAME");
    	
		ObjectMapper mapper = new ObjectMapper();
		
		String url = "";
		String username = "";
		String password = "";
		String version = props.getProperty("TA_VERSION").split("=")[1];
		
		Map<String, Object> input = mapper.readValue(vcap_services, new TypeReference<Map<String, Object>>(){});
		
		List<Map<String, Object>> l0s = (List<Map<String, Object>>) input.get(serviceName);
		
		for(Map<String, Object> l0: l0s){
			for(Map.Entry<String, Object> e: l0.entrySet()){
				if(e.getKey().equalsIgnoreCase("credentials")){
					System.out.println(e.getKey() + "=" + e.getValue());
					Map<String, Object> credential = (Map<String, Object>) e.getValue();
					url = (String) credential.get("url");
					username = (String) credential.get("username");
					password = (String) credential.get("password");
				}
			}
		}
		
		System.out.println("ta0 url=" + url);
		System.out.println("ta0 username=" + username);
		System.out.println("ta0 password=" + password);
		System.out.println("ta0 version=" + version);
		
		try {
		
			ta = new ToneAnalyzer(version, username, password);
			ta.setEndPoint(url);
			
			System.out.println(ta.getName() + " " + ta.getEndPoint());
			
		}
		catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return;
    }    

    @SuppressWarnings("unchecked")
	public void initDSC() throws JsonParseException, JsonMappingException, IOException{

    	String serviceName = props.getProperty("DSC_NAME");
    	
		ObjectMapper mapper = new ObjectMapper();
		
		String url = "";
		String username = "";
		String password = "";
		String version = props.getProperty("DSC_VERSION").split("=")[1];
		
		Map<String, Object> input = mapper.readValue(vcap_services, new TypeReference<Map<String, Object>>(){});
		
		List<Map<String, Object>> l0s = (List<Map<String, Object>>) input.get(serviceName);
		
		for(Map<String, Object> l0: l0s){
			for(Map.Entry<String, Object> e: l0.entrySet()){
				if(e.getKey().equalsIgnoreCase("credentials")){
					System.out.println(e.getKey() + "=" + e.getValue());
					Map<String, Object> credential = (Map<String, Object>) e.getValue();
					url = (String) credential.get("url");
//					username = (String) credential.get("username");
//					password = (String) credential.get("password");
					username = "apikey";
					password = (String) credential.get("apikey");
				}
			}
		}
		
		dsc = new Discovery(version);
		dsc.setEndPoint(url);
		dsc.setUsernameAndPassword(username, password);
		dsc.setEndPoint(url);
		
		ListEnvironmentsOptions envOptions = new ListEnvironmentsOptions.Builder().build();
		ListEnvironmentsResponse envResponse = dsc.listEnvironments(envOptions).execute();
		
		Map<String, Object> envMap = mapper.readValue(envResponse.toString(), new TypeReference<Map<String, Object>>(){});

		List<Map<String, String>> envs = (List<Map<String, String>>) envMap.get("environments");

		for(Map<String, String> e: envs){
			if(e.get("name").equalsIgnoreCase(props.getProperty("DSC_ENV_NAME"))){
				dscEnvId = (e.get("environment_id"));
			}
			if(e.get("name").equalsIgnoreCase("byod")){
				dscEnvId = (e.get("environment_id"));
			}
		}

		try {
			ListCollectionsOptions collOptions = new ListCollectionsOptions.Builder(dscEnvId).build();
			ListCollectionsResponse collResponse = dsc.listCollections(collOptions).execute();
	
			Map<String, Object> collMap = mapper.readValue(collResponse.toString(), new TypeReference<Map<String, Object>>(){});
	
			List<Map<String, String>> colls = (List<Map<String, String>>) collMap.get("collections");
	
			for(Map<String, String> e: colls){
				if(e.get("name").equalsIgnoreCase(props.getProperty("DSC_COLL_NAME"))){
					dscCollId = (e.get("collection_id"));
				}
			}
			System.out.println(dsc.getName() + " " + dsc.getEndPoint() + " " + dscEnvId + " " + dscCollId);
		}
		catch(IllegalArgumentException | NullPointerException e){
			System.out.println(dsc.getName() + " " + dsc.getEndPoint() + " !!! WARNING: no collection found !!!");
		}
		
		return;
    }

    @SuppressWarnings({ "unchecked", "unused" })
	public void initWVC() throws JsonParseException, JsonMappingException, IOException{
    	
    	String apiKey = System.getenv("WVC_APIKEY");    
        String serviceName = props.getProperty("WVC_NAME");
        String url = "";
        String version = props.getProperty("WVC_VERSION").split("=")[1];
    	
    	if(apiKey == null){
	        
	        ObjectMapper mapper = new ObjectMapper();
	
	        Map<String, Object> input = mapper.readValue(vcap_services, new TypeReference<Map<String, Object>>(){});
	
	        List<Map<String, Object>> l0s = (List<Map<String, Object>>) input.get(serviceName);
	
	        for(Map<String, Object> l0: l0s){
	                for(Map.Entry<String, Object> e: l0.entrySet()){
	                        if(e.getKey().equalsIgnoreCase("credentials")){
	                                Map<String, Object> credential = (Map<String, Object>) e.getValue();
	                                url = (String) credential.get("url");
	                                apiKey = (String) credential.get("apikey");
	                        }
	                }
	        }
    	}
    	
		IamOptions options = new IamOptions.Builder().apiKey(apiKey).build();

		wvc = new VisualRecognition(version, options);
		
		System.out.println(wvc.getName() + " " + wvc.getEndPoint());
		
		return;    	

    }
    
    @SuppressWarnings({ "unchecked" })
    public void cleanDColl() throws JsonParseException, JsonMappingException, IOException{
    
		List<String> fields = new ArrayList<String>();
		fields.add("extracted_metadata");
    	
//    	QueryRequest.Builder queryBuilder = new QueryRequest.Builder(dscEnvId, dscCollId)
//    			.count(100)
//    			.returnFields(fields);
//    	
//    	QueryResponse queryResponse = dsc.query(queryBuilder.build()).execute();
    	try{
	    	QueryOptions.Builder queryOptions = new QueryOptions.Builder(dscEnvId, dscCollId)
	    			.count(100)
	    			.returnFields(fields);
	    	QueryResponse queryResponse = dsc.query(queryOptions.build()).execute();    	
	    	
	    	System.out.println("queryResponse=" + queryResponse);
	    	
	    	ObjectMapper mapper = new ObjectMapper();
	    	
	    	Map<String, Object> docMap = mapper.readValue(queryResponse.toString(), new TypeReference<Map<String, Object>>(){});
	
			List<Map<String, String>> docs = (List<Map<String, String>>) docMap.get("results");
			
			List<String> docIds = new ArrayList<String>();
	
			for(Map<String, String> doc: docs){
				docIds.add(doc.get("id"));
			}
			
	    	System.out.println("docIds=" + docIds);
	    	
	    	for(String docId: docIds){
		    	DeleteDocumentOptions deleteOptions = new DeleteDocumentOptions.Builder(dscEnvId, dscCollId, docId).build();
		    	dsc.deleteDocument(deleteOptions).execute();
	    	}
    	}
    	catch(IllegalArgumentException | NullPointerException e){
    		System.out.println(dsc.getName() + " " + dsc.getEndPoint() + " !!! WARNING: no collection found !!!");
    	}
    	
    }
    
}
