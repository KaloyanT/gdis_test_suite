package com.gdis.importer.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gdis.importer.model.CSVAsJSON;

@RestController
@RequestMapping("/importer/import/csvasjson")
public class ImportCSVRequestControler {

	private CSVAsJSON csvToImport;
	
	private List<ObjectNode> csvRows;
	
	private ArrayNode rowsAsArray;
	
	private ObjectNode jsonToImport;
	
	
	@RequestMapping(method = RequestMethod.POST, consumes = 
		{ MediaType.APPLICATION_JSON_UTF8_VALUE})		
		public ResponseEntity<?> handleImportRequest(@RequestBody CSVAsJSON json) {	
		
		//ObjectMapper mapper = new ObjectMapper();
		//JsonNode response = mapper.createObjectNode();
		
		//JsonNode parsedJSON = null;
		
		/*try {
			parsedJSON = mapper.readTree(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//System.out.println(parsedJSON.toString());
		
		setCsvToImport(json);
		
		csvRows = new ArrayList<ObjectNode>();
		chunkJSONForImport(csvToImport);
		
		createJSONForImport(csvRows);
		
		printChunks(jsonToImport);
		
		//System.out.println(response.toString());
		
		return new ResponseEntity<>(json, HttpStatus.OK);
	}
	
	private void createJSONForImport(List<ObjectNode> jsonChunks) {
		
		ObjectMapper mapper = new ObjectMapper();
		rowsAsArray = mapper.createArrayNode();
		jsonToImport = mapper.createObjectNode();
		
		for(int i = 0; i < jsonChunks.size(); i++) {
			rowsAsArray.add(jsonChunks.get(i));
		}
		
		jsonToImport.put("csvFileName", csvToImport.getCsvFileName());
		
	}
	
	private void chunkJSONForImport(CSVAsJSON json) {
		
		ObjectMapper mapper = new ObjectMapper();
		
		for(int i = 0; i < json.getRows().size(); i++) {
			
			ObjectNode chunk = mapper.createObjectNode();
			
			for(int j = 0; j < json.getHeaders().size(); j++) {
				chunk.put(json.getHeaders().get(j), json.getRows().get(i).get(j));
			}
			
			csvRows.add(chunk);
		}
			
	}
	
	
	private void printChunks(ObjectNode json) {
	
		System.out.println(json.toString());
		
		/*System.out.println(jsonList.toString());
		for(ObjectNode i : jsonList) {
			System.out.println(i.toString());
		}*/
	}
	
	
	@ExceptionHandler({HttpMessageNotReadableException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<String> resolveException() {
		
		return new ResponseEntity<String>("Empty Body", HttpStatus.BAD_REQUEST);
	}

	public CSVAsJSON getCsvToImport() {
		return csvToImport;
	}

	public void setCsvToImport(CSVAsJSON csvToImport) {
		this.csvToImport = csvToImport;
	}
}
