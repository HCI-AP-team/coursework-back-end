package com.example.demo;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.sf.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/Hel")
	public Object get() {
		try {
			//Connect MongoDB, port number is 27017
			MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);

			//Connect specific database
			MongoDatabase mDatabase = mongoClient.getDatabase("test");  //"test" is optional
			//System.out.println("Connect to database successfully!");
			//System.out.println("MongoDatabase inof is : "+mDatabase.getName());

			//Switch to specific collection
			MongoCollection collection = mDatabase.getCollection("DXYArea");

			//Create query condition to get the latest data
			BasicDBObject filter_dbobject = new BasicDBObject();
			filter_dbobject.put("_id", -1);
			FindIterable iterate = collection.find().sort(filter_dbobject).limit(1);
			//Switch the output data into "json" type
			JSONObject json = JSONObject.fromObject(iterate.iterator().tryNext());

			//Output the query result to a file
            /*
            String text = json.toString();
            System.out.println(text);
            String filePath = "D:\\3rdYear\\data.json";
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(text.getBytes());
            fos.close();
            */

			return json;

			//return json;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
		//return null;
	}
}
