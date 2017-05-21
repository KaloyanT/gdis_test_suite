package com.gdis.database.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gdis.database.service.NewContractRepository;

@RestController
@RequestMapping("/db/newContract")
public class NewContractController {
	
	
	
	@Autowired
	private NewContractRepository newContractRepository;
	
	
	
}
