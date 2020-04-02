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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	@GetMapping("/news")
	public Object news() {
		String url = "https://ncov.dxy.cn/ncovh5/view/pneumonia";         //丁香园的网站


		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			URLConnection connection = realUrl.openConnection();
			connection.connect();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line + "\n";
			}

		} catch (Exception e) {
			System.out.println("发送GET请求出现异常" + e);
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		//这部分为可视化处理

		result = result.replace("]", "]\n");
		//result=result.replace("{","\n{");
		//result=result.replace(",",",\n");
		result = result.replace("\"", "");

		String jsonObject = "{\n\"result\":[\n";

		//下面这部分为过滤出来国外的情况

		//System.out.println(result);

		//result=result.replace("]","]\n");
		//result=result.replace("{","\n{");
		//result=result.replace(",",",\n");
		//result=result.replace("\"","");

		Pattern SimpleNews =Pattern.compile("abroadRemark(.*?)}]");

		Matcher SimpleNewsMatcher=SimpleNews.matcher(result);

		System.out.println("以下为部分新闻:\n");

		String context = null;

		while(SimpleNewsMatcher.find()){

			context=SimpleNewsMatcher.group(0);


		}

		context=context.substring(22);


		context=context.replace("id","\"id\"");

		context=context.replace("marqueeLabel","\"marqueeLabel\"");

		context=context.replace("marqueeLink","\"marqueeLink\"");

		context=context.replace(":",":\"");

		context=context.replace(",","\",");

		context=context.replace("}","\"}");

		context=context.replace("https:\"//","https://");

		context=context.replace("}\",{","},{");

		System.out.println(context);

		return context;
	}
}
