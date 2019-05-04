package com.research.chirag.dao;

import java.util.List;

import com.research.chirag.beans.Opportunity;

public interface FileUtilityDAO {
	public String writeToFile(String myData);
	public List<Opportunity> readFromFile();
}
