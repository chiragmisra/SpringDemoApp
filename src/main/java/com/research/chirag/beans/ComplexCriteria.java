package com.research.chirag.beans;

import java.util.List;

public class ComplexCriteria {
	private List<Criteria> criterias;
	private String logicalOperator;
	
	public String getLogicalOperator() {
		return logicalOperator;
	}
	public void setLogicalOperator(String logicalOperator) {
		this.logicalOperator = logicalOperator;
	}
	public List<Criteria> getCriterias() {
		return criterias;
	}
	public void setCriterias(List<Criteria> criterias) {
		this.criterias = criterias;
	}
	
}
