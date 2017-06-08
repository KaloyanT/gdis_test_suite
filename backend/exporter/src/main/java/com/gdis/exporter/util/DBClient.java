package com.gdis.exporter.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.gdis.exporter.model.JSONResponse;

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

	public static String getDB_URL() {
		return DB_URL;
	}

	public static void setDB_URL(String dB_URL) {
		DB_URL = dB_URL;
	}
	
	
	public List<JSONResponse> exportAllTestsFromDB() {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		List<JSONResponse> res = null;
		
		
		final String url = getDB_URL() + "/" + "basicStoryTest";
		
		ResponseEntity<JSONResponse[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, JSONResponse[].class);
		} catch(HttpClientErrorException e) {
			//System.out.println(e.getStatusCode());
		}
		
		if(response != null) {
			System.out.println(response.toString());
			res = new ArrayList<>(Arrays.asList(response.getBody()));
		}
		
		
		return res;
	}
	
	
	public List<JSONResponse> exportTestsFromDBByStoryName(String storyName) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		List<JSONResponse> res = null;
		
		
		final String url = getDB_URL() + "/" + "basicStoryTest" + "/get/byStoryName/" + storyName;
		
		ResponseEntity<JSONResponse[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, JSONResponse[].class);
		} catch(HttpClientErrorException e) {
			//System.out.println(e.getStatusCode());
		}
		
		if(response != null) {
			System.out.println(response.toString());
			res = new ArrayList<>(Arrays.asList(response.getBody()));
		}
		
		
		return res;
	} 
	
	
	public JSONResponse exportCTestFromDBByTestName(String testName) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		JSONResponse res = null;
		
		final String url = getDB_URL() + "/" + "basicStoryTest" + "/get/byTestName/" + testName;
		
		ResponseEntity<JSONResponse> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, JSONResponse.class);
		} catch(HttpClientErrorException e) {
			System.out.println(e.getStatusCode());
		}
		
		if(response != null) {
			System.out.println(response.toString());
			res = response.getBody();
		}
		
		
		return res;
	} 
	
	
}
