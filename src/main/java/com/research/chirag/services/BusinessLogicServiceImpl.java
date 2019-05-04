package com.research.chirag.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.research.chirag.beans.ComplexCriteria;
import com.research.chirag.beans.Criteria;
import com.research.chirag.beans.Opportunity;
import com.research.chirag.dao.FileUtilityDAO;

@Service
public class BusinessLogicServiceImpl implements BusinessLogicService {
	
	@Autowired
	private FileUtilityDAO utility;
	
	@Autowired
	private Gson gson;
	
	@Override
	public String registerOpportunities(List<Opportunity> opportunities) {
		return utility.writeToFile(gson.toJson(opportunities).toString());
	}

	@Override
	public List<Opportunity> getOpportunities() {
		return utility.readFromFile();
	}

	@Override
	public List<Opportunity> applySearchCriteria(Criteria criteria) {
		if (AVAILABLE_PROPERTY_LIST.stream().anyMatch(x -> x.equalsIgnoreCase(criteria.getPropertyName()))
				&& AVAILABLE_COMPARISON_OPERATORS_LIST.stream().anyMatch(x -> x.equalsIgnoreCase(criteria.getOperator()))) {

			Predicate<Opportunity> predicate = getPredicate(criteria.getPropertyName(), criteria.getPropertyValue(),
					criteria.getOperator());

			if (predicate == null) {
				return null;
			}

			return getOpportunities().stream().filter(predicate).collect(Collectors.<Opportunity>toList());
		}
			
		return null;
	}
	
	@Override
	public List<Opportunity> applyComplexSearchCriteria(ComplexCriteria criterias) {
		
		return null;
	}
	
	private Predicate<Opportunity> getPredicate(String name, String value, String operator) {
		switch(name) {
			case NAME : 
				return getNamePredicate(value, operator);
			case AMOUNT :
				return getAmountPredicate(value, operator);
			case DEADLINE :
				try {
					return getDeadlinePredicate(value, operator);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			default : 
				return null;
		}		
	}
	
	private Predicate<Opportunity> getNamePredicate(String value, String operator) {
		switch (operator) {
			case EQUALS : 
				return p -> p.getName().equalsIgnoreCase(value);
		
			default :
				return null;			
		}
	}
	
	private Predicate<Opportunity> getAmountPredicate(String value, String operator) {
		switch (operator) {
			case EQUALS : 
				return p -> p.getAmount().equals(Integer.valueOf(value));
		
			case GREATER_THAN :
				return p -> p.getAmount() > Integer.valueOf(value);
				
			case LESS_THAN :
				return p -> p.getAmount() < Integer.valueOf(value);
				
			default :
				return null;			
		}
	}
	
	private Predicate<Opportunity> getDeadlinePredicate(String value, String operator) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = formatter.parse(value);
		switch (operator) {
			case EQUALS : 
				return p -> p.getDeadline().compareTo(date) == 0;
		
			case GREATER_THAN :
				return p -> p.getDeadline().after(date);
				
			case LESS_THAN :
				return p -> p.getDeadline().before(date);
				
			default :
				return null;			
		}
	}
}
