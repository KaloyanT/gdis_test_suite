package com.gdis.importer.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
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
		
		setDB_URL(properties.getProperty("databaseAPI_URL"));
		
		inputStream.close();
		
	}
	
	//public void importChunksInDB(List<ObjectNode> chunks, String storyType) {
	public void importChunksInDB(ObjectNode json, final String storyType) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		
		final String url = getDB_URL() + "/" + storyType + "/insert";
		
		
		//for(ObjectNode i : chunks) {
			
			HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);
			//ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
			//ResponseEntity<String> response = null;
			
			try {
				restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			} catch(HttpClientErrorException e) {
				//System.out.println(e.getStatusCode());
			}
			
			
		//}
	}

	public static String getDB_URL() {
		return DB_URL;
	}

	public static void setDB_URL(String dB_URL) {
		DB_URL = dB_URL;
	}
}
