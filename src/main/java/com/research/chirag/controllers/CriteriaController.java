package com.research.chirag.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.research.chirag.beans.ComplexCriteria;
import com.research.chirag.beans.Criteria;
import com.research.chirag.beans.Opportunity;
import com.research.chirag.services.BusinessLogicService;

@Controller
@RequestMapping(value="/criteria")
public class CriteriaController {
	
	@Autowired
	private BusinessLogicService service;
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value="/search" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Opportunity> search(@RequestBody Criteria criteria) {
		System.out.println(criteria.getPropertyName());
		return service.applySearchCriteria(criteria);
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value="/search" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Opportunity> complexSearch(@RequestBody ComplexCriteria criterias) {
		return service.applyComplexSearchCriteria(criterias);
	}

}
