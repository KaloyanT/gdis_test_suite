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
	}
	
	
	public ResponseEntity<String> importStoryTestInDB(ObjectNode json) {
		
		if(getDATABASEAPI_URL() == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		//HttpStatus errorCode = null;
		ResponseEntity<String> response = null;
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
				
		final String url = getDATABASEAPI_URL() + "/storyTest/insert";
		
		HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);
			
		try {
			response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		} catch(HttpClientErrorException e) {
			
			//errorCode = e.getStatusCode();
			return new ResponseEntity<>(e.getStatusCode());
			
		} catch (RestClientException re) {
			// No DB Connection
			//errorCode = 500;
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		return response;
	}
	
	
	public ResponseEntity<String> updateStoryTestInDB(ObjectNode json, final String testName) {
		
		if(getDATABASEAPI_URL() == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		ResponseEntity<String> response = null;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		
		RestTemplate restTemplate = new RestTemplate();
		
		final String url = getDATABASEAPI_URL() + "/storyTest/update/by-test-name/" + testName;
		
		HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);
				
		try {
			response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
		} catch(HttpClientErrorException e) {
			
			//errorCode = e.getStatusCode();
			return new ResponseEntity<>(e.getStatusCode());
			
		} catch (RestClientException re) {
			// No DB Connection
			// HttpStatus = 500;
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		return response;
		
	}
	
	
	public ResponseEntity<String> deleteStoryTestFromDB(final String testName) {
		
		if(getDATABASEAPI_URL() == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		ResponseEntity<String> response = null;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		
		RestTemplate restTemplate = new RestTemplate();
		
		final String url = getDATABASEAPI_URL() + "/storyTest/delete/by-test-name/" + testName;
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
				
		try {
			response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
		} catch(HttpClientErrorException e) {
			
			//errorCode = e.getStatusCode();
			return new ResponseEntity<>(e.getStatusCode());
			
		} catch (RestClientException re) {
			// No DB Connection
			//errorCode = 500;
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		return response;
	}
	
	
	public ResponseEntity<String> importStoryInDB(ObjectNode json) {
		
		if(getDATABASEAPI_URL() == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		ResponseEntity<String> response = null;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
				
		final String url = getDATABASEAPI_URL() + "/story/insert";
		
		HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);
			
		try {
			response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		} catch(HttpClientErrorException e) {
			
			return new ResponseEntity<>(e.getStatusCode());
			
		} catch (RestClientException re) {
			// No DB Connection
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		return response;
	}
	
	
	public ResponseEntity<String> updateStoryInDB(final String storyName, ObjectNode json) {
		
		if(getDATABASEAPI_URL() == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		ResponseEntity<String> response = null;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
				
		final String url = getDATABASEAPI_URL() + "/story/update/by-story-name/" + storyName;
		
		HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);
			
		try {
			response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
		} catch(HttpClientErrorException e) {
			
			return new ResponseEntity<>(e.getStatusCode());
			
		} catch (RestClientException re) {
			// No DB Connection
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		return response;
	}
	
	
	public ResponseEntity<String> deleteStoryFromDB(final String storyName) {
		
		if(getDATABASEAPI_URL() == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		ResponseEntity<String> response = null;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
				
		final String url = getDATABASEAPI_URL() + "/story/delete/by-story-name/" + storyName;
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
			
		try {
			response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
		} catch(HttpClientErrorException e) {
			
			return new ResponseEntity<>(e.getStatusCode());
			
		} catch (RestClientException re) {
			// No DB Connection
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		return response;
	}
	
	
	public ResponseEntity<String> importTestEntityInDB(ObjectNode json) {
		
		if(getDATABASEAPI_URL() == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		ResponseEntity<String> response = null;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
				
		final String url = getDATABASEAPI_URL() + "/testEntity/insert";
		
		HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);
			
		try {
			response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		} catch(HttpClientErrorException e) {
			
			return new ResponseEntity<>(e.getStatusCode());
			
		} catch (RestClientException re) {
			// No DB Connection
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		return response;
	}
	
	
	public ResponseEntity<String> imporTestEntityAttributesInDB(final String entityName, ObjectNode json) {
		
		if(getDATABASEAPI_URL() == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		ResponseEntity<String> response = null;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
				
		final String url = getDATABASEAPI_URL() + "/testEntity/insert/attributes/" + entityName;
		
		HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);
			
		try {
			response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
		} catch(HttpClientErrorException e) {
			
			return new ResponseEntity<>(e.getStatusCode());
			
		} catch (RestClientException re) {
			// No DB Connection
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		return response;
	}
	
	
	public ResponseEntity<String> updateTestEntityInDB(final String entityName, ObjectNode json) {
		
		if(getDATABASEAPI_URL() == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		ResponseEntity<String> response = null;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
				
		final String url = getDATABASEAPI_URL() + "/testEntity/update/attributes/" + entityName;
		
		HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);
			
		try {
			response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
		} catch(HttpClientErrorException e) {
			
			return new ResponseEntity<>(e.getStatusCode());
			
		} catch (RestClientException re) {
			// No DB Connection
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		return response;
	}
	
	
	public ResponseEntity<String> deleteTestEntityFromDB(final String entityName) {
		
		if(getDATABASEAPI_URL() == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		ResponseEntity<String> response = null;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
				
		final String url = getDATABASEAPI_URL() + "/testEntity/delete/by-entity-name/" + entityName;
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
			
		try {
			response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
		} catch(HttpClientErrorException e) {
			
			return new ResponseEntity<>(e.getStatusCode());
			
		} catch (RestClientException re) {
			// No DB Connection
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		return response;
	}
	
	

	public String getDATABASEAPI_URL() {
		return DATABASEAPI_URL;
	}

	public void setDATABASEAPI_URL(String DATABASEAPI_URL) {
		this.DATABASEAPI_URL = DATABASEAPI_URL;
	}
}
