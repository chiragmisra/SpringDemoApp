package com.research.chirag.services;

import java.util.Arrays;
import java.util.List;

import com.research.chirag.beans.ComplexCriteria;
import com.research.chirag.beans.Criteria;
import com.research.chirag.beans.Opportunity;

public interface BusinessLogicService {
	public static final String NOT = "not";
	public static final String OR = "or";
	public static final String AND = "and";
	public static final String LESS_THAN = "lessThan";
	public static final String GREATER_THAN = "greaterThan";
	public static final String EQUALS = "equals";
	public static final String DEADLINE = "deadline";
	public static final String AMOUNT = "amount";
	public static final String NAME = "name";
	public static final List<String> AVAILABLE_PROPERTY_LIST = Arrays.asList(NAME, AMOUNT, DEADLINE);
	public static final List<String> AVAILABLE_COMPARISON_OPERATORS_LIST = Arrays.asList(EQUALS, GREATER_THAN, LESS_THAN);
	public static final List<String> AVAILABLE_LOGIC_OPERATORS_LIST = Arrays.asList(AND, OR, NOT);
 	
	
	public String registerOpportunities(List<Opportunity> opportunities);
	public List<Opportunity> getOpportunities();
	public List<Opportunity> applySearchCriteria(Criteria criteria);
	public List<Opportunity> applyComplexSearchCriteria(ComplexCriteria criterias);
	
}
