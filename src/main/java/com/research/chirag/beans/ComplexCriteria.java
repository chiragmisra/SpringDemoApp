package com.research.chirag.beans;

import java.util.List;

public class ComplexCriteria {
	private List<Criteria> criterias;
	private String logicOperator;
	
	public List<Criteria> getCriterias() {
		return criterias;
	}
	public void setCriterias(List<Criteria> criterias) {
		this.criterias = criterias;
	}
	public String getLogicOperator() {
		return logicOperator;
	}
	public void setLogicOperator(String logicOperator) {
		this.logicOperator = logicOperator;
	}
}
