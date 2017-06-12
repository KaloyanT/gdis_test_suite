package com.gdis.importer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class DBClient {
	
	private String DATABASEAPI_URL;
	
	@Autowired
	public DBClient(@Value("${DATABASEAPI_URL}") String DATABASEAPI_URL) {
		
		this.DATABASEAPI_URL = DATABASEAPI_URL;
		
		/*try {
			readAndSetDBUrlIfNull();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
	}
	
	/*
	private void readAndSetDBUrlIfNull() throws IOException, FileNotFoundException {
		
		if(getDB_URL() != null) {
			return;
		}
		
		// Read customProperties file
		Properties properties = new Properties();
		InputStream inputStream = null;
		
		inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
		
		if(inputStream != null) {
			properties.load(inputStream);
		} else {
			throw new FileNotFoundException("Application Properties File not found!");
		}
		
		setDB_URL(properties.getProperty("databaseAPI_URL"));
		
		inputStream.close();
		
	}*/
	
	
	public HttpStatus importTestInDB(ObjectNode json, final String storyType) {
		
		//HttpStatus errorCode = null;
		ResponseEntity<String> response = null;
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		
		if(getDATABASEAPI_URL() == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		final String url = getDATABASEAPI_URL() + "/" + storyType + "/insert";
		
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

	public String getDATABASEAPI_URL() {
		return DATABASEAPI_URL;
	}

	public void setDATABASEAPI_URL(String DATABASEAPI_URL) {
		this.DATABASEAPI_URL = DATABASEAPI_URL;
	}
}
