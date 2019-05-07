package com.research.chirag.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.research.chirag.beans.ComplexCriteria;
import com.research.chirag.beans.Criteria;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {

	@LocalServerPort
	private int port;
	
	@Autowired
	Gson gson;
	
	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();

	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testRetrieve() throws JSONException {

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/opportunity/list"),
				HttpMethod.GET, entity, String.class);

		System.out.println(response.getBody());
		assertNotNull(response.getBody());
	}
	
	@Test
	public void testSimpleCriteria() {
		Criteria criteria = new Criteria();
		criteria.setPropertyName("amount");
		criteria.setPropertyValue("10000");
		criteria.setOperator("equals");
		
		HttpEntity<Criteria> entity = new HttpEntity<Criteria>(criteria, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/criteria/search"),
				HttpMethod.POST, entity, String.class);
		
		String expectedVal = gson.toJson("[{\"name\":\"Opportunity 2\",\"amount\":10000,\"deadline\":\"2019-06-02T00:00:00.000+0000\"}]");
		assertNotNull(response.getBody());
		assertEquals(expectedVal, gson.toJson(response.getBody()));
	}
	
	@Test
	public void testComplexCriteria() {
		Criteria criteria1 = new Criteria();
		criteria1.setPropertyName("deadline");
		criteria1.setPropertyValue("2019-07-29T23:00:00.000+0000");
		criteria1.setOperator("lessThan");
		
		Criteria criteria2 = new Criteria();
		criteria2.setPropertyName("amount");
		criteria2.setPropertyValue("500000");
		criteria2.setOperator("greaterThan");
		
		List<Criteria> critList = new ArrayList<Criteria>();
		critList.add(criteria1);
		critList.add(criteria2);
		
		ComplexCriteria compCrit = new ComplexCriteria();
		
		compCrit.setCriterias(critList);
		compCrit.setLogicalOperator("and");
		
		HttpEntity<ComplexCriteria> entity = new HttpEntity<ComplexCriteria>(compCrit, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/criteria/complexSearch"),
				HttpMethod.POST, entity, String.class);
		
		String expectedVal = gson.toJson("[{\"name\":\"Opportunity 12\",\"amount\":1000000000,\"deadline\":\"2019-05-29T23:00:00.000+0000\"}]");
		
		assertNotNull(response.getBody());
		assertEquals(expectedVal, gson.toJson(response.getBody()));
	}
	
	@Test
	public void testComplexCriteria1() {
		Criteria criteria1 = new Criteria();
		criteria1.setPropertyName("deadline");
		criteria1.setPropertyValue("2019-12-31T23:00:00.000+0000");
		criteria1.setOperator("greaterThan");
		
		Criteria criteria2 = new Criteria();
		criteria2.setPropertyName("amount");
		criteria2.setPropertyValue("5000000");
		criteria2.setOperator("greaterThan");
		
		List<Criteria> critList = new ArrayList<Criteria>();
		critList.add(criteria1);
		critList.add(criteria2);
		
		ComplexCriteria compCrit = new ComplexCriteria();
		
		compCrit.setCriterias(critList);
		compCrit.setLogicalOperator("or");
		
		HttpEntity<ComplexCriteria> entity = new HttpEntity<ComplexCriteria>(compCrit, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/criteria/complexSearch"),
				HttpMethod.POST, entity, String.class);
		
		String expectedVal = gson.toJson("[{\"name\":\"Opportunity 3\",\"amount\":15000,\"deadline\":\"2020-05-31T00:00:00.000+0000\"},{\"name\":\"Opportunity 8\",\"amount\":10009999,\"deadline\":\"2020-01-30T00:00:00.000+0000\"},{\"name\":\"Opportunity 9\",\"amount\":9999000,\"deadline\":\"2019-07-29T23:00:00.000+0000\"},{\"name\":\"Opportunity 10\",\"amount\":11111000,\"deadline\":\"2019-08-22T23:00:00.000+0000\"},{\"name\":\"Opportunity 12\",\"amount\":1000000000,\"deadline\":\"2019-05-29T23:00:00.000+0000\"},{\"name\":\"Opportunity 14\",\"amount\":55323000,\"deadline\":\"2019-11-10T00:00:00.000+0000\"},{\"name\":\"Opportunity 17\",\"amount\":2799000,\"deadline\":\"2020-12-01T00:00:00.000+0000\"}]");
		
		assertNotNull(response.getBody());
		assertEquals(expectedVal, gson.toJson(response.getBody()));
	}
	
	@Test
	public void testSimpleCriteriaWithInvalidOperator() {
		Criteria criteria = new Criteria();
		criteria.setPropertyName("amount");
		criteria.setPropertyValue("10000");
		criteria.setOperator("equa");
		
		HttpEntity<Criteria> entity = new HttpEntity<Criteria>(criteria, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/criteria/search"),
				HttpMethod.POST, entity, String.class);
		
		assertNull(response.getBody());
	}
	
	@Test
	public void testSimpleCriteriaWithInvalidPropertyName() {
		Criteria criteria = new Criteria();
		criteria.setPropertyName("amount1");
		criteria.setPropertyValue("10000");
		criteria.setOperator("equals");
		
		HttpEntity<Criteria> entity = new HttpEntity<Criteria>(criteria, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/criteria/search"),
				HttpMethod.POST, entity, String.class);
		
		assertNull(response.getBody());
	}
	
	
	
	@Test
	public void testComplexCriteriaWithInvalidOperator() {
		Criteria criteria1 = new Criteria();
		criteria1.setPropertyName("deadline");
		criteria1.setPropertyValue("2019-07-29T23:00:00.000+0000");
		criteria1.setOperator("lessThan");
		
		Criteria criteria2 = new Criteria();
		criteria2.setPropertyName("amount");
		criteria2.setPropertyValue("500000");
		criteria2.setOperator("greater");
		
		List<Criteria> critList = new ArrayList<Criteria>();
		critList.add(criteria1);
		critList.add(criteria2);
		
		ComplexCriteria compCrit = new ComplexCriteria();
		
		compCrit.setCriterias(critList);
		compCrit.setLogicalOperator("and");
		
		HttpEntity<ComplexCriteria> entity = new HttpEntity<ComplexCriteria>(compCrit, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/criteria/complexSearch"),
				HttpMethod.POST, entity, String.class);
		
		assertNull(response.getBody());
	}
	
	@Test
	public void testComplexCriteriaWithInvalidLogicOperator() {
		Criteria criteria1 = new Criteria();
		criteria1.setPropertyName("deadline");
		criteria1.setPropertyValue("2019-07-29T23:00:00.000+0000");
		criteria1.setOperator("lessThan");
		
		Criteria criteria2 = new Criteria();
		criteria2.setPropertyName("amount");
		criteria2.setPropertyValue("500000");
		criteria2.setOperator("greaterThan");
		
		List<Criteria> critList = new ArrayList<Criteria>();
		critList.add(criteria1);
		critList.add(criteria2);
		
		ComplexCriteria compCrit = new ComplexCriteria();
		
		compCrit.setCriterias(critList);
		compCrit.setLogicalOperator("notand");
		
		HttpEntity<ComplexCriteria> entity = new HttpEntity<ComplexCriteria>(compCrit, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/criteria/complexSearch"),
				HttpMethod.POST, entity, String.class);
		
		assertNull(response.getBody());
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
