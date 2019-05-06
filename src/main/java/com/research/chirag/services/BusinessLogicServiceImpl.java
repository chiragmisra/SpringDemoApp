package com.research.chirag.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

			Predicate<Opportunity> predicate = getPredicate(criteria.getPropertyName(), criteria.getPropertyValue(), criteria.getOperator());

			if (predicate == null) {
				return null;
			}

			return getOpportunities().stream().filter(predicate).collect(Collectors.<Opportunity>toList());
		}
			
		return null;
	}
	
	@Override
	public List<Opportunity> applyComplexSearchCriteria(ComplexCriteria criterias) {
		if (validateComplexCriteria(criterias)) {
			List<Predicate<Opportunity>> predicates = getComplexPredicateList(criterias);
			Predicate<Opportunity> predicate = combineFilters(criterias.getLogicalOperator(), predicates);
			return getOpportunities().stream().filter(predicate).collect(Collectors.<Opportunity>toList());
		}	
		
		return null;
	}
	
	private Predicate<Opportunity> combineFilters(String operator, List<Predicate<Opportunity>> predicates) {
		Predicate<Opportunity> p = null;
		if(operator.equalsIgnoreCase(AND))
			p = predicates.stream().reduce(Predicate::and).orElse(x -> true);
		if(operator.equalsIgnoreCase(OR))
			p = predicates.stream().reduce(Predicate::or).orElse(x -> false);
		if(operator.equalsIgnoreCase(NOT))
			p = predicates.stream().reduce(Predicate::and).orElse(x-> true).negate();
        return p;
    }
	
	private List<Predicate<Opportunity>> getComplexPredicateList(ComplexCriteria criterias) {
		return criterias.getCriterias().stream().map(c -> getPredicate(c.getPropertyName(), c.getPropertyValue(), c.getOperator())).collect(Collectors.toList());
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
	
	private boolean validateComplexCriteria(ComplexCriteria criterias) {
		List<Criteria> criteriaList = criterias.getCriterias();
		boolean propName = true;
		boolean compOp = true;
		if (CollectionUtils.isNotEmpty(criteriaList) && !StringUtils.isEmpty(criterias.getLogicalOperator())) {
			if (criterias.getCriterias().size() >=2) {
					for (Criteria cr : criterias.getCriterias()) {
						if(!AVAILABLE_PROPERTY_LIST.contains(cr.getPropertyName())) {
							propName = false;
						}
					}
				
					for (Criteria cr : criterias.getCriterias()) {
						if(!AVAILABLE_COMPARISON_OPERATORS_LIST.contains(cr.getOperator())) {
							compOp = false;
						}
					}
				
				if(propName && compOp &&  AVAILABLE_LOGIC_OPERATORS_LIST.stream().anyMatch(x -> x.equalsIgnoreCase(criterias.getLogicalOperator()))) {
					return true;
				}
			}
		}
		return false;
	}
}
