package com.example.demo;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import net.sf.json.JSONObject;
import java.io.FileOutputStream;

public class dataExport {


    public static void main(String[] args) {
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

            System.out.println(json);

            //return json;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        //return null;
    }
}
