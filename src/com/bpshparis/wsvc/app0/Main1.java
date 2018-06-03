package com.bpshparis.wsvc.app0;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

public class Main1 {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub

		IamOptions options = new IamOptions.Builder().apiKey("MXxX8UPgku3CaO7_w9WXYVBSw-ikyOQkSZzssXWt8OZY").build();

		VisualRecognition service = new VisualRecognition("2018-03-19", options);

		InputStream imagesStream = new FileInputStream("/opt/wks/ma/WebContent/res/mails/pic0.jpg");
		ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
		  .imagesFile(imagesStream)
		  .imagesFilename("pic0.jpg")
//		  .threshold((float) 0.6)
		  .build();
		ClassifiedImages result = service.classify(classifyOptions).execute();
		System.out.println(result);
				
	}

}
