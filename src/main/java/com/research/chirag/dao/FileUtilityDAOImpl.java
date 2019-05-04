package com.research.chirag.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.research.chirag.beans.Opportunity;

@Service
public class FileUtilityDAOImpl implements FileUtilityDAO {
	
	private static String FILE_LOCATION = "./test.txt";
	
	@Autowired
	private Gson gson;
	
	@Override
	public String writeToFile(String myData) {
		File file = new File(FILE_LOCATION);
		if (!file.exists()) {
			try {
				File directory = new File(file.getParent());
				if (!directory.exists()) {
					directory.mkdirs();
				}
				file.createNewFile();
			} catch (IOException e) {
				log("Excepton Occured: " + e.toString());
				return "Failure";
			}
		} else {
			try {
				file.delete();
				file.createNewFile();
			} catch (IOException e) {
				log("Excepton Occured: " + e.toString());
				return "Failure";
			}
		}
 
		try {
			FileWriter writer;
			writer = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bufferWriter = new BufferedWriter(writer);
			bufferWriter.write(myData.toString());
			bufferWriter.close();
 
			log("Data saved at file location: " + FILE_LOCATION + " Data: " + myData + "\n");
		} catch (IOException e) {
			log("Error while saving data to file " + e.toString());
			return "Failure";
		}
		
		return "Success";
	}
 
	@Override
	public List<Opportunity> readFromFile() {
		File file = new File(FILE_LOCATION);
		if (!file.exists())
			log("File doesn't exist");
 
		Type classType = new TypeToken<List<Opportunity>>() {}.getType();
		InputStreamReader isReader;
		try {
			isReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
			
			JsonReader myReader = new JsonReader(isReader);
			log("before file read");
			List<Opportunity> students = gson.fromJson(myReader, classType);
			return students;
		} catch (Exception e) {
			log("error load cache from file " + e.toString());
		}
		log("Data loaded successfully from file " + FILE_LOCATION);
		return null;
 
	}
	
	private void log(String string) {
		System.out.println(string);
	}
}
