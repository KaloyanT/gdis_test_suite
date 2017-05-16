package com.gdis.importer.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/importer/database")
public class DatabaseSimulator {

	@RequestMapping(value = "/{storyType}", method = RequestMethod.POST, consumes = 
		{ MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
		
		public ResponseEntity<String> simulateDatabase(@RequestBody String json) {	
			
			return new ResponseEntity<String>(json, HttpStatus.OK);
		}
}
