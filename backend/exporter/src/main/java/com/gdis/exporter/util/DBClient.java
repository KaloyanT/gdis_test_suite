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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gdis.exporter.model.EntityExportModel;
import com.gdis.exporter.model.StoryExportModel;
import com.gdis.exporter.model.StoryTestExportModel;

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
	
	
	public List<StoryTestExportModel> exportAllStoryTestsFromDB() {

		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		List<StoryTestExportModel> res = null;
		
		final String url = getDATABASEAPI_URL() + "/storyTest";
		
		ResponseEntity<StoryTestExportModel[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, StoryTestExportModel[].class);
		} catch(HttpClientErrorException e) {
			return new ArrayList<StoryTestExportModel>();
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
	
	
	public List<String> exportTestNamesOfAllStoryTestsFromDB() {

		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		List<String> res = null;
		
		final String url = getDATABASEAPI_URL() + "/storyTest/testNames";
		
		ResponseEntity<String[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, String[].class);
		} catch(HttpClientErrorException e) {
			return new ArrayList<String>();
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
	
	
	public List<ObjectNode> exportAllStoryTestsFromDBForExport() {

		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		List<ObjectNode> res = null;
		
		final String url = getDATABASEAPI_URL() + "/storyTest/export";
		
		ResponseEntity<ObjectNode[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, ObjectNode[].class);
		} catch(HttpClientErrorException e) {
			return new ArrayList<ObjectNode>();
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
	
	
	public List<StoryTestExportModel> exportStoryTestsFromDBByStoryName(final String storyName) {
		
		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		List<StoryTestExportModel> res = null;
		
		final String url = getDATABASEAPI_URL() + "/storyTest/get/by-story-name/" + storyName;
		
		ResponseEntity<StoryTestExportModel[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, StoryTestExportModel[].class);
		} catch(HttpClientErrorException e) {
			return new ArrayList<StoryTestExportModel>();
		} catch (RestClientException re) {
			// No DB Connection
			return null;
		}
		
		if(response != null) {
			res = new ArrayList<>(Arrays.asList(response.getBody()));
		}
		
		return res;
	} 
	
	
	public List<ObjectNode> exportStoryTestsFromDBByStoryNameForExport(final String storyName) {
		
		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		List<ObjectNode> res = null;
		
		final String url = getDATABASEAPI_URL() + "/storyTest/get/by-story-name/" + storyName + "/export";
		
		ResponseEntity<ObjectNode[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, ObjectNode[].class);
		} catch(HttpClientErrorException e) {
			return new ArrayList<ObjectNode>();
		} catch (RestClientException re) {
			// No DB Connection
			return null;
		}
		
		if(response != null) {
			res = new ArrayList<>(Arrays.asList(response.getBody()));
		}
		
		return res;
	} 
	
	
	public List<StoryTestExportModel> exportStoryTestFromDBByTestName(final String testName) {
		
		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		List<StoryTestExportModel> res = null;
		
		final String url = getDATABASEAPI_URL() + "/storyTest/get/by-test-name/" + testName;
		
		ResponseEntity<StoryTestExportModel[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, StoryTestExportModel[].class);
		} catch(HttpClientErrorException e) {
			return new ArrayList<StoryTestExportModel>();
		} catch (RestClientException re) {
			// No DB Connection
			return null;
		}
		
		if(response != null) {
			res = new ArrayList<>(Arrays.asList(response.getBody()));
		
		} 
		
		return res;
	} 
	
	
	public List<ObjectNode> exportStoryTestFromDBByTestNameForExport(final String testName) {
		
		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		List<ObjectNode> res = null;
		
		final String url = getDATABASEAPI_URL() + "/storyTest/get/by-test-name/" + testName + "/export";
		
		ResponseEntity<ObjectNode[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, ObjectNode[].class);
		} catch(HttpClientErrorException e) {
			return new ArrayList<ObjectNode>();
		} catch (RestClientException re) {
			// No DB Connection
			return null;
		}
		
		if(response != null) {
			res = new ArrayList<>(Arrays.asList(response.getBody()));
		
		} 
		
		return res;
	} 
	
	
	public List<String> exportEntityAttributesForStoryTestFromDBByTestName(final String testName) {
		
		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		List<String> res = null;
		
		final String url = getDATABASEAPI_URL() + "/storyTest/get/entity-attributes-for-story-test/" + testName;
		
		ResponseEntity<String[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, String[].class);
		} catch(HttpClientErrorException e) {
			return new ArrayList<String>();
		} catch (RestClientException re) {
			// No DB Connection
			return null;
		}
		
		if(response != null) {
			res = new ArrayList<>(Arrays.asList(response.getBody()));
		
		} 
		
		return res;
	} 
	
	
	public List<ObjectNode> exportStoryTestFromDBByEntityName(final String entityName) {
		
		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
		List<ObjectNode> res = null;
		
		final String url = getDATABASEAPI_URL() + "/testEntity/get/story-tests-containing-entity/" + entityName;
		
		ResponseEntity<ObjectNode[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, ObjectNode[].class);
		} catch(HttpClientErrorException e) {
			return new ArrayList<ObjectNode>();
		} catch (RestClientException re) {
			// No DB Connection
			return null;
		}
		
		if(response != null) {
			res = new ArrayList<>(Arrays.asList(response.getBody()));
		
		} 
		
		return res;
	} 
	
	
	public List<EntityExportModel> exportAllTestEntitiesFromDB() {
		
		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
	
		List<EntityExportModel> res = null;
	
		final String url = getDATABASEAPI_URL() + "/testEntity";
	
		ResponseEntity<EntityExportModel[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, EntityExportModel[].class);
		} catch(HttpClientErrorException e) {
			return new ArrayList<EntityExportModel>();
		} catch (RestClientException re) {
			// No DB Connection
			return null;
		}
		
		if(response != null) {
			res = new ArrayList<>(Arrays.asList(response.getBody()));
		} 
		
		return res;		
	}
	
	
	public List<EntityExportModel> exportTestEntityByNameFromDB(final String entityName) {
		
		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
	
		List<EntityExportModel> res = null;
	
		final String url = getDATABASEAPI_URL() + "/testEntity/get/by-entity-name/" + entityName;
	
		ResponseEntity<EntityExportModel[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, EntityExportModel[].class);
		} catch(HttpClientErrorException e) {
			return new ArrayList<EntityExportModel>();
		} catch (RestClientException re) {
			// No DB Connection
			return null;
		}
		
		if(response != null) {
			res = new ArrayList<>(Arrays.asList(response.getBody()));
		} 
		
		return res;		
	}
	
	
	public List<String> exportValuesForEntityAttributeFromDB(final String entityName, final String attribute) {
		
		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
	
		List<String> res = null;
	
		final String url = getDATABASEAPI_URL() + "/testEntity/get/values-for-attribute/" + entityName + "/" + attribute; 
	
		ResponseEntity<String[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, String[].class);
		} catch(HttpClientErrorException e) {
			return new ArrayList<String>();
		} catch (RestClientException re) {
			// No DB Connection
			return null;
		}
		
		if(response != null) {
			res = new ArrayList<>(Arrays.asList(response.getBody()));
		} 
		
		return res;		
	}
	
	
	
	public List<ObjectNode> exportAllTestObjectsFromDB() {
		
		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
	
		List<ObjectNode> res = null;
	
		final String url = getDATABASEAPI_URL() + "/" + "testObject" + "/reduced";
	
		ResponseEntity<ObjectNode[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, ObjectNode[].class);
		} catch(HttpClientErrorException e) {
			return new ArrayList<ObjectNode>();
		} catch (RestClientException re) {
			// No DB Connection
			return null;
		}
		
		if(response != null) {
			res = new ArrayList<>(Arrays.asList(response.getBody()));
		
		} 
		
		return res;		
	}
	
	
	public List<ObjectNode> exportTestObjectsFromDBByEntityType(final String entityType) {
		
		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
	
		List<ObjectNode> res = null;
	
		final String url = getDATABASEAPI_URL() + "/testObject/get/objects-for-entity/" + entityType + "/reduced";
	
		ResponseEntity<ObjectNode[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, ObjectNode[].class);
		} catch(HttpClientErrorException e) {
			return new ArrayList<ObjectNode>();
		} catch (RestClientException re) {
			// No DB Connection
			return null;
		}
		
		if(response != null) {
			res = new ArrayList<>(Arrays.asList(response.getBody()));
		
		} 
		
		return res;		
	}
	
	
	public List<StoryExportModel> exportAllStoriesFromDB() {
		
		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
	
		List<StoryExportModel> res = null;
	
		final String url = getDATABASEAPI_URL() + "/story/reduced";
	
		ResponseEntity<StoryExportModel[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, StoryExportModel[].class);
		} catch(HttpClientErrorException e) {
			return new ArrayList<StoryExportModel>();
		} catch (RestClientException re) {
			// No DB Connection
			return null;
		}
		
		if(response != null) {
			res = new ArrayList<>(Arrays.asList(response.getBody()));
		
		} 
		
		return res;		
	}
	
	
	public List<String> exportListOfStoryNamesFromDB() {
		
		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
	
		List<String> res = null;
	
		final String url = getDATABASEAPI_URL() + "/story/story-name-list";
	
		ResponseEntity<String[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, String[].class);
		} catch(HttpClientErrorException e) {
			return new ArrayList<String>();
		} catch (RestClientException re) {
			// No DB Connection
			return null;
		}
		
		if(response != null) {
			res = new ArrayList<>(Arrays.asList(response.getBody()));
		
		} 
		
		return res;		
	}
	
	public List<StoryExportModel> exportStoryFromDBByStoryName(final String storyName) {
		
		if(getDATABASEAPI_URL() == null) {
			return null;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		RestTemplate restTemplate = new RestTemplate();
	
		List<StoryExportModel> res = null;
	
		final String url = getDATABASEAPI_URL() + "/story/get/by-story-name/" + storyName + "/reduced";
	
		ResponseEntity<StoryExportModel[]> response = null; 
		
		try {
			response = restTemplate.getForEntity(url, StoryExportModel[].class);
		} catch(HttpClientErrorException e) {
			return new ArrayList<StoryExportModel>();
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
