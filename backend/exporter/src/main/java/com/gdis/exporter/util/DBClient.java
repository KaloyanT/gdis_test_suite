 package com.gdis.exporter.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.gdis.exporter.model.JSONResponse;

@Component
public class DBClient {
	
	private String DATABASEAPI_URL;
	
	@Autowired
	public DBClient(@Value("${DATABASEAPI_URL}") String DATABASEAPI_URL) {
		this.DATABASEAPI_URL = DATABASEAPI_URL;		
	}

	public String getDATABASEAPI_URL() {
		return DATABASEAPI_URL;
	}

	public void setDATABASEAPI_URL(String DATABASEAPI_URL) {
		this.DATABASEAPI_URL = DATABASEAPI_URL;
	}
	
	
	public List<JSONResponse> exportAllTestsFromDB(final String storyType) {
		
		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		List<JSONResponse> res = null;
		
		final String url = getDATABASEAPI_URL() + "/" + storyType;
		
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
		
		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		List<JSONResponse> res = null;
		
		final String url = getDATABASEAPI_URL() + "/" + storyType + "/get/by-story-name/" + storyName;
		
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
		
		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		List<JSONResponse> res = null;
		
		final String url = getDATABASEAPI_URL() + "/" + storyType + "/get/by-test-name/" + testName;
		
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
