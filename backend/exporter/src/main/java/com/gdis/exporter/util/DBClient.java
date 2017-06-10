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
import org.springframework.web.client.RestClientException;
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
	
	
	public List<JSONResponse> exportAllTestsFromDB(final String storyType) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		List<JSONResponse> res = null;
		
		
		final String url = getDB_URL() + "/" + storyType;
		
		ResponseEntity<JSONResponse[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, JSONResponse[].class);
		} catch(HttpClientErrorException e) {
			return null;
		} catch (RestClientException re) {
			// No DB Connection
			return null;
		}
		
		if(response != null) {
			//System.out.println(response.toString());
			res = new ArrayList<>(Arrays.asList(response.getBody()));
		}
		
		
		return res;
	}
	
	
	public List<JSONResponse> exportTestsFromDBByStoryName(final String storyType, final String storyName) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		List<JSONResponse> res = null;
		
		
		final String url = getDB_URL() + "/" + storyType + "/get/by-story-name/" + storyName;
		
		ResponseEntity<JSONResponse[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, JSONResponse[].class);
		} catch(HttpClientErrorException e) {
			return null;
		} catch (RestClientException re) {
			// No DB Connection
			return null;
		}
		
		if(response != null) {
			res = new ArrayList<>(Arrays.asList(response.getBody()));
		}
		
		
		return res;
	} 
	
	
	public List<JSONResponse> exportTestFromDBByTestName(final String storyType, final String testName) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		List<JSONResponse> res = null;
		
		final String url = getDB_URL() + "/" + storyType + "/get/by-test-name/" + testName;
		
		ResponseEntity<JSONResponse[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, JSONResponse[].class);
		} catch(HttpClientErrorException e) {
			return new ArrayList<JSONResponse>();
		} catch (RestClientException re) {
			// No DB Connection
			return null;
		}
		
		if(response != null) {
			res = new ArrayList<>(Arrays.asList(response.getBody()));
		
		} 
		
		
		return res;
	} 
	
	
}
