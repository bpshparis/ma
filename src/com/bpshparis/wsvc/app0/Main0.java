package com.bpshparis.wsvc.app0;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Main0 {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, KeyManagementException {
		// TODO Auto-generated method stub
		
		@SuppressWarnings("unused")
		String version = "2018-03-19";
//		String password = "iL7mtf3ebj1DI7-DPFYZ6CXaWcq726dINi68Dw8JUz1o";
//		String password = "oaKhuzPMVbhZuKv6IQx5ZHliR27SM0kI1bec7SCK1kVB";
		String username = "apikey";
		String password = "YydUT91iEMdi7hQ8I5EVpWt8sMrwhAbMGJfvihTWITAq";
//		String url = "http://172.16.186.244/dma/TestDBConnection";
//		String url = "http://gateway.watsonplatform.net:443/visual-recognition/api/v3/classify?version=2018-03-19";
		String url = "https://gateway.watsonplatform.net/visual-recognition/api";
		@SuppressWarnings("unused")
		String query = "/visual-recognition/api/v3/classify?version=2018-03-19";
		Path path = Paths.get("/opt/wks/ma/WebContent/res/mails/pic0.jpg");
		
//	    OkHttpClient client = new UnsafeOkHttpClient().getUnsafeOkHttpClient();
//	    
//		client = client.newBuilder()
//	    	.addInterceptor(new BasicAuthInterceptor(username, password))
//	    	.build();		
//	    
//		RequestBody body = new MultipartBody.Builder()
//				.setType(MultipartBody.FORM)
//	            .addFormDataPart("File", "images_file", RequestBody.create(MediaType.parse("image/jpeg"),file))
//	            .build();
//
//	    Request request = new Request.Builder()
//	    		.url(url)
//	    		.post(body)
//	    		.build();
//	    
//	    Response response = client.newCall(request).execute();
//	    String result = response.body().string();
//	    System.out.println("result=" + result);
//	    
//	    
//	    InputStream is = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8.name()));
//		
//		InputStreamReader isr = new InputStreamReader(is);
//		BufferedReader br = new BufferedReader(isr);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        Map<String, Object> svc = mapper.readValue(br, new TypeReference<Map<String, Object>>(){});
//        
//        List<Object> images = (List<Object>) svc.get("images");
//		
//		Map<String, Object> images0 = (Map<String, Object>) images.get(0);
//		
//		List<Object> classifiers = (List<Object>) images0.get("classifiers");
//		
//		if(classifiers != null){
//		
//			Map<String, Object> classifiers0 = (Map<String, Object>) classifiers.get(0);
//			
//			String json = mapper.writeValueAsString(classifiers0.get("classes"));
//			
//			List<VRClass> classes = Arrays.asList(mapper.readValue(json, VRClass[].class));
//			
//			for(VRClass vrClass: classes){
//				System.out.println(vrClass.getVrClass() + " -> " + vrClass.getScore());
//			}
//			
//			
//		}	    
		    
			VisualRecognition wvc = new VisualRecognition(version);
			wvc.setEndPoint(url);
			wvc.setApiKey(password);
			
//			IamOptions options = new IamOptions.Builder()
//					.apiKey(password)
//					.build();
//			wvc.setIamCredentials(options);			
			
			ClassifyOptions classifyImagesOptions = new ClassifyOptions.Builder()
					.imagesFile(Files.newInputStream(path))
					.imagesFilename("pic0.jpg")
					.build();
			
			ClassifiedImages visualClassification = wvc.classify(classifyImagesOptions).execute();
			
			String result = visualClassification.toString();
			
			System.out.println(result);
		
	}

}
