package com.gdis.importer.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
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
	public HttpStatus importTestInDB(ObjectNode json, final String storyType) {
		
		//HttpStatus errorCode = null;
		ResponseEntity<String> response = null;
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		
		final String url = getDB_URL() + "/" + storyType + "/insert";
		
		HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);
			
		try {
			response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		} catch(HttpClientErrorException e) {
			
			//errorCode = e.getStatusCode();
			return e.getStatusCode();
			
		} catch (RestClientException re) {
			// No DB Connection
			//errorCode = 500;
			return HttpStatus.INTERNAL_SERVER_ERROR;
			
		}
		
		/*
		if(errorCode != 0) {
			return errorCode;
		}
		 return response.getStatusCodeValue();
		*/
	
		return response.getStatusCode();
	}

	public static String getDB_URL() {
		return DB_URL;
	}

	public static void setDB_URL(String dB_URL) {
		DB_URL = dB_URL;
	}
}
