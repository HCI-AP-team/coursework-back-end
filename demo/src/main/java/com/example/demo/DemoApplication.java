package com.example.demo;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.sf.json.JSONObject;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
@RestController
@CrossOrigin
public class DemoApplication {

	public static void main(String[] args) {

			Timer tiemr=new Timer();

			tiemr.schedule(new dbConnection(),0,1000*60*60*24);

		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/Hel")
	@CrossOrigin
	public JSONObject get() {
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
			//System.out.println("zzzz");
			JSONObject json = JSONObject.fromObject(iterate.iterator().tryNext());
			//System.out.println(json);

			//System.out.println("wwww");
			//com.alibaba.fastjson.JSONObject json1 = com.alibaba.fastjson.JSONObject.parseObject((String) iterate.iterator().tryNext());
			//Output the query result to a file
            /*
            String text = json.toString();
            System.out.println(text);
            String filePath = "D:\\3rdYear\\data.json";
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(text.getBytes());
            fos.close();*/

			//System.out.println(json);
			return json;

			//return json;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
		//return null;
	}

	@GetMapping("/news")
	@CrossOrigin
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

		//System.out.println("以下为部分新闻:\n");

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
	@RequestMapping("/News")
	@CrossOrigin
	public com.alibaba.fastjson.JSONObject News() {
		String url = "https://ncov.dxy.cn/ncovh5/view/pneumonia";         //丁香园的网站
		Map map = new Map();

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

		String jsonObject = "{\n\"results\":[\n";

		//下面这部分为过滤出来国外的情况

		Pattern News = Pattern.compile("window.getTimelineService(.*?)}]");

		Matcher newsMatcher = News.matcher(result);

		newsMatcher.find();

		//System.out.println(newsMatcher.group(0));

		Pattern subNews = Pattern.compile("id(.*?)}");

		Matcher subNewsMatcher = subNews.matcher(newsMatcher.group(0));


		String news;
		while (subNewsMatcher.find()) {
			//System.out.println(newsMatcher.group(0));
			news = subNewsMatcher.group(0);

			String id = news.substring(3, news.indexOf(','));

			news = news.substring(news.indexOf("pubDate"));

			String pubDate = news.substring(8, news.indexOf(','));

			news = news.substring(news.indexOf("title"));

			String title = news.substring(6, news.indexOf(','));

			news = news.substring(news.indexOf("summary"));

			String summary = news.substring(8, news.indexOf(','));

			news = news.substring(news.indexOf("infoSource"));

			String infoSource = news.substring(11, news.indexOf(','));

			news = news.substring(news.indexOf("sourceUrl"));

			String sourceUrl = news.substring(10, news.indexOf(','));

			//System.out.println(infoSource);

			jsonObject = jsonObject +
					"{\n\"title\":\"" + title +
					"\",\n\"summary\":\"" + summary +
					"\",\n\"infoSource\":\"" + infoSource +
					"\",\n\"sourceUrl\":\"" + sourceUrl +
					"\",\n\"pubDate\":" + pubDate +
					",\n\"ProvinceName\":" + "\"null\"" + ",\n\"provinceId\":1" + "\n},\n";
		}
		//System.out.println(jsonObject);
		newsMatcher.find();

		//System.out.println(newsMatcher.group(0)+"zzz");


		subNewsMatcher = subNews.matcher(newsMatcher.group(0));


		while (subNewsMatcher.find()) {
			//System.out.println(newsMatcher.group(0));
			news = subNewsMatcher.group(0);

			String id = news.substring(3, news.indexOf(','));

			news = news.substring(news.indexOf("pubDate"));

			String pubDate = news.substring(8, news.indexOf(','));

			news = news.substring(news.indexOf("title"));

			String title = news.substring(6, news.indexOf(','));

			news = news.substring(news.indexOf("summary"));

			String summary = news.substring(8, news.indexOf(','));

			news = news.substring(news.indexOf("infoSource"));

			String infoSource = news.substring(11, news.indexOf(','));

			news = news.substring(news.indexOf("sourceUrl"));

			String sourceUrl = news.substring(10, news.indexOf(','));

			//System.out.println(infoSource);

			jsonObject = jsonObject +
					"{\n\"title\":\"" + title +
					"\",\n\"summary\":\"" + summary +
					"\",\n\"infoSource\":\"" + infoSource +
					"\",\n\"sourceUrl\":\"" + sourceUrl +
					"\",\n\"pubDate\":" + pubDate +
					",\n\"ProvinceName\":" + "\"null\"" + ",\n\"provinceId\":1" + "\n},\n";
		}
		jsonObject = jsonObject.substring(0, jsonObject.length() - 2);
		jsonObject = jsonObject + "\n],\n\"success\":true\n}";
		jsonObject = jsonObject.replace("\n", "");
		jsonObject = jsonObject.replace("\\n", "");
		jsonObject = jsonObject.replace("\\", "");

		//System.out.println(jsonObject);
		com.alibaba.fastjson.JSONObject json1 = com.alibaba.fastjson.JSONObject.parseObject(jsonObject);
		return json1;
	}


	@RequestMapping("/ChinaMap")
	@CrossOrigin
	public JSONObject chinamap(){
		InputStream is=this.getClass().getResourceAsStream("/ChinaMap.json");
		//System.out.println(location);
		JSONObject json1=LocalJson.readJsonFile(is);
		//System.out.println(json1);
		return json1;
	}
	@RequestMapping("/chinamapData")
	@CrossOrigin
	public JSONObject chinamapData(){
		InputStream is=this.getClass().getResourceAsStream("/chinamapData.json");
		//System.out.println(location);
		JSONObject json1=LocalJson.readJsonFile(is);
		//System.out.println(json1);
		return json1;
	}
	@RequestMapping("/tsv2json")
	@CrossOrigin
	public JSONObject tsv2json(){
		InputStream is=this.getClass().getResourceAsStream("/tsv2json.json");
		//System.out.println(location);
		JSONObject json1=LocalJson.readJsonFile(is);
		//System.out.println(json1);
		return json1;
	}
	@RequestMapping("/worldData")
	@CrossOrigin
	public JSONObject worldData(){
		InputStream is=this.getClass().getResourceAsStream("/worldData.json");
		//System.out.println(location);
		JSONObject json1=LocalJson.readJsonFile(is);
		//System.out.println(json1);
		return json1;
	}
	@RequestMapping("/2Dearth")
	@CrossOrigin
	public JSONObject earth(){
		InputStream is=this.getClass().getResourceAsStream("/2Dearth.json");
		//System.out.println(location);
		JSONObject json1=LocalJson.readJsonFile(is);
		//System.out.println(json1);
		return json1;
	}
	@RequestMapping("/metadata")
	@CrossOrigin
	public JSONObject metadata(){
		InputStream is=this.getClass().getResourceAsStream("/metadata.json");
		//System.out.println(location);
		JSONObject json1=LocalJson.readJsonFile(is);
		//System.out.println(json1);
		return json1;
	}
	@RequestMapping("/model")
	@CrossOrigin
	public JSONObject model(){
		InputStream is=this.getClass().getResourceAsStream("/model.json");
		//System.out.println(location);
		JSONObject json1=LocalJson.readJsonFile(is);
		System.out.println(json1);
		return json1;
	}
		@RequestMapping("/download")
		public String download(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
			String fileName = "";
			if (fileName != null){
				String realPath = "/Volumes/我的磁盘/图片/";
				File file = new File(realPath,fileName);
				fileName = new String(file.getName().getBytes("utf-8"));
				String suffixNmae = fileName.substring(fileName.lastIndexOf("."));
				String name = CommonUtils.generateUUID().toString();
				fileName = name + suffixNmae;
				if (file.exists()){
					response.setContentType("application/force-download");
					response.addHeader("Content-Disposition","attachment;fileName="+fileName);
					byte[] buffer = new byte[1024];
					FileInputStream fis = null;
					BufferedInputStream bis = null;
					try{
						fis = new FileInputStream(file);
						bis = new BufferedInputStream(fis);
						OutputStream os = response.getOutputStream();
						int i = bis.read(buffer);
						while(i != -1){
							os.write(buffer,0,i);
							i = bis.read(buffer);
						}
						System.out.println("success");
					}catch (Exception e){
						e.printStackTrace();
					}finally {
						if (bis != null){
							try{
								bis.close();
							}catch (IOException e){
								e.printStackTrace();
							}
						}
						if (fis != null){
							try{
								fis.close();
							}catch (IOException e){
								e.printStackTrace();
							}
						}
					}
				}
			}
			System.out.println(fileName);
			return fileName;

		}
	@RequestMapping(value = "/DownLoad/{fileName}/{fileType}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(HttpServletRequest request, @PathVariable String fileName, @PathVariable String fileType) throws IOException {
		File file = new File("D:\\Apks\\" + fileName + "." + fileType);
		byte[] body = null;
		InputStream is = this.getClass().getResourceAsStream("/"+fileName+"."+fileType);
		System.out.println(is);
		body = new byte[is.available()];
		is.read(body);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attchement;filename=" + file.getName());
		HttpStatus statusCode = HttpStatus.OK;
		ResponseEntity<byte[]> entity = new ResponseEntity<>(body, headers, statusCode);
		return entity;
	}

}
