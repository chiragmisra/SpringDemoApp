package com.research.chirag.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.research.chirag.beans.Opportunity;
import com.research.chirag.beans.OpportunityWrapper;
import com.research.chirag.services.BusinessLogicService;

@Controller
@RequestMapping(value="/opportunity")
public class OpportunityController {
	
	@Autowired
	private BusinessLogicService service;
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value="/register" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String registerOpportunity(@RequestBody OpportunityWrapper opportunities) {
		System.out.println("In register"+opportunities.getOpportunities());
		return service.registerOpportunities(opportunities.getOpportunities());
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value="/list" ,produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Opportunity> getOpportunities() {
		System.out.println("In list");
		return service.getOpportunities();
	}
}
