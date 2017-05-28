package com.gdis.importer.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DBClient {
	
	private static String DB_URL;
	
	public DBClient() {
		
		try {
			readAndSetDBUrl();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readAndSetDBUrl() throws IOException, FileNotFoundException {
		
		// Read customProperties file
		Properties properties = new Properties();
		InputStream inputStream = null;
		
		inputStream = getClass().getClassLoader().getResourceAsStream("customProperties.properties");
		
		if(inputStream != null) {
			properties.load(inputStream);
		} else {
			throw new FileNotFoundException("Custom Properties File not found!");
		}
		
		DB_URL = properties.getProperty("databaseAPI_URL");
		
		inputStream.close();
		
	}
	
	public void importChunksInDB(List<ObjectNode> chunks, String storyType) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		
		// Debug DB POST Request Simulator
		// final String url = "http://localhost:8083/importer/database/" + storyType;
		final String url = DB_URL + "/" + storyType + "/insert";
		
		
		for(ObjectNode i : chunks) {
			
			HttpEntity<String> entity = new HttpEntity<String>(i.toString(), headers);
			//ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			System.out.println(response.getStatusCodeValue());
		}
	}
}
