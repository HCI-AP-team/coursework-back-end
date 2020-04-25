package com.example.demo;

import com.alibaba.fastjson.parser.Feature;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;
import org.bson.Document;

public class dbConnection extends TimerTask {
    @Override
    public void run() {
        boolean contain = true;
        while (contain) {
            try {

//String url="https://view.inews.qq.com/g2/getOnsInfo?name=disease_h5";    //此处为腾讯的接口


                String url = "https://ncov.dxy.cn/ncovh5/view/pneumonia";         //丁香园的网站
                Map map=new Map();

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

                Pattern foreignCountry = Pattern.compile("id:(.*?)showRank");

                Matcher foreignMatcher = foreignCountry.matcher(result);

                //System.out.println(result);


                //System.out.println("以下为世界疫情汇总:\n");


                while (foreignMatcher.find()) {


                    String Foreign = foreignMatcher.group(0);


                    //System.out.println(Foreign);

                    String ContinentId = Foreign.substring(3, Foreign.indexOf(','));

                    Foreign = Foreign.substring(Foreign.indexOf("createTime"));

                    String createTime = Foreign.substring(11, Foreign.indexOf(','));

                    Foreign = Foreign.substring(Foreign.indexOf("continents"));

                    String Continent = Foreign.substring(11, Foreign.indexOf(','));

                    Foreign = Foreign.substring(Foreign.indexOf("provinceName"));

                    String CountryName = Foreign.substring(13, Foreign.indexOf(','));

                    Foreign = Foreign.substring(Foreign.indexOf("currentConfirmedCount"));

                    String CountryTodayConfirmed = Foreign.substring(22, Foreign.indexOf(','));

                    Foreign = Foreign.substring(Foreign.indexOf("confirmedCount"));

                    String CountryTotally = Foreign.substring(15, Foreign.indexOf(','));

                    Foreign = Foreign.substring(Foreign.indexOf("suspectedCount"));

                    String CountrySuspect = Foreign.substring(15, Foreign.indexOf(','));

                    Foreign = Foreign.substring(Foreign.indexOf(',') + 1);

                    String CountryCure = Foreign.substring(11, Foreign.indexOf(','));

                    Foreign = Foreign.substring(Foreign.indexOf(',') + 1);

                    String CountryDead = Foreign.substring(10, Foreign.indexOf(','));

                    Foreign = Foreign.substring(Foreign.indexOf("countryShortCode"));

                    String CountryCode = Foreign.substring(17, Foreign.indexOf(','));


                    Foreign = Foreign.substring(Foreign.indexOf("countryFullName"));

                    String CountryFullName = Foreign.substring(16, Foreign.indexOf(','));


                    //System.out.println("所属大陆:"+Continent+" \t国家名称:"+CountryName+" \t今日感染:"+CountryTodayConfirmed+" \t总共感染:"+CountryTotally+" \t疑似病例:"+CountrySuspect+" \t治愈病列:"+CountryCure
                    //+" \t死亡病例:"+CountryDead+" \t城市代码:"+CountryCode+" \t城市全称:"+CountryFullName);

                    jsonObject = jsonObject + "{\n\"locationId\":" + ContinentId +
                            ",\n\"continentName\":\"" + Continent +
                            "\",\n\"continentEnglishName\":\"" + map.continentName(Continent) +
                            "\",\n\"countryName\":\"" + CountryName +
                            "\",\n\"countryEnglishName\":\"" + map.countryEnglishName(CountryName) +
                            "\",\n\"provinceName\":\"" + CountryName +
                            "\",\n\"provinceEnglishName\":\"" + map.countryEnglishName(CountryName) +
                            "\",\n\"provinceShortName\":\"" + CountryName +
                            "\",\n\"currentConfirmedCount\":" + CountryTodayConfirmed +
                            ",\n\"confirmedCount\":" + CountryTotally +
                            ",\n\"suspectedCount\":" + CountrySuspect +
                            ",\n\"curedCount\":" + CountryCure +
                            ",\n\"deadCount\":" + CountryDead +
                            ",\n\"updateTime\":" + createTime +
                            ",\n\"cities\":" + "\"\"" + ",\n\"comment\":\"\"" + "\n},\n";

                    //System.out.println(foreignMatcher.group(0));
                }

                Pattern China = Pattern.compile("provinceId:999(.*?)showRank");

                Matcher ChMatcher = China.matcher(result);

                ChMatcher.find(0);
                //System.out.println(ChMatcher.group(0));

                String china=ChMatcher.group(0);

                china=china.substring(china.indexOf("currentConfirmedCount"));
                String current=china.substring(22,china.indexOf(','));

                china=china.substring(china.indexOf("confirmedCount"));
                String confirmed=china.substring(15,china.indexOf(','));

                china=china.substring(china.indexOf("suspectedCount"));
                String suspect=china.substring(15,china.indexOf(','));

                china=china.substring(china.indexOf("curedCount"));
                String cure=china.substring(11,china.indexOf(','));

                china=china.substring(china.indexOf("deadCount"));
                String dead=china.substring(10,china.indexOf(','));

                china=china.substring(china.indexOf("locationId"));
                String id=china.substring(11,china.indexOf(','));

                jsonObject = jsonObject + "{\n\"locationId\":" + id +
                        ",\n\"continentName\":\"" + "亚洲" +
                        "\",\n\"continentEnglishName\":\"" + map.continentName("亚洲") +
                        "\",\n\"countryName\":\"" + "中国" +
                        "\",\n\"countryEnglishName\":\"" + map.countryEnglishName("中国") +
                        "\",\n\"provinceName\":\"" + "中国" +
                        "\",\n\"provinceEnglishName\":\"" + map.countryEnglishName("中国") +
                        "\",\n\"provinceShortName\":\"" + "中国" +
                        "\",\n\"currentConfirmedCount\":" + current +
                        ",\n\"confirmedCount\":" + confirmed +
                        ",\n\"suspectedCount\":" + suspect +
                        ",\n\"curedCount\":" + cure +
                        ",\n\"deadCount\":" + dead +
                        ",\n\"updateTime\":" + "\"\"" +
                        ",\n\"cities\":" + "\"\"" + ",\n\"comment\":\"\"" + "\n},\n";
                //System.out.println(jsonObject);


                //这部分为过滤出来国内的状态
                Pattern ChinaCountry = Pattern.compile("provinceName(.*?)]");

                Matcher ChinaMatcher = ChinaCountry.matcher(result);


                ChinaMatcher.find();                            //此行的功能为为后面过滤掉非中国的地区

                //ChinaMatcher.find();


                //System.out.println("\n\n以下为国内各省份城市疫情汇总:\n");

                while (ChinaMatcher.find()) {

                    String ProvinceSituation = ChinaMatcher.group(0);

                    //System.out.println(ProvinceSituation);

                    Pattern Province = Pattern.compile("Name:(.*?)create");

                    Matcher ProvinceNameMatcher = Province.matcher(ProvinceSituation);

                    String ProvinceName = ProvinceSituation.substring(13, ProvinceSituation.indexOf(','));


                    String ProvinceTotally = ProvinceSituation.substring(0, ProvinceSituation.indexOf("statisticsData"));

                    //System.out.println(ProvinceTotally);

                    ProvinceTotally = ProvinceTotally.substring(ProvinceTotally.indexOf("currentConfirmedCount"));

                    //System.out.println(ProvinceTotally);

                    String ProvinceTodayConfirmed = ProvinceTotally.substring(22, ProvinceTotally.indexOf(','));

                    ProvinceTotally = ProvinceTotally.substring(ProvinceTotally.indexOf(',') + 1);

                    String ProvinceTotallyConfirmed = ProvinceTotally.substring(15, ProvinceTotally.indexOf(','));

                    ProvinceTotally = ProvinceTotally.substring(ProvinceTotally.indexOf(',') + 1);

                    String ProvinceSuspectCount = ProvinceTotally.substring(15, ProvinceTotally.indexOf(','));

                    ProvinceTotally = ProvinceTotally.substring(ProvinceTotally.indexOf(',') + 1);

                    String ProvinceCured = ProvinceTotally.substring(11, ProvinceTotally.indexOf(','));

                    ProvinceTotally = ProvinceTotally.substring(ProvinceTotally.indexOf(',') + 1);

                    String ProvinceDead = ProvinceTotally.substring(10, ProvinceTotally.indexOf(','));

                    ProvinceTotally = ProvinceTotally.substring(ProvinceTotally.indexOf(',') + 1);

                    String ProvinceComment = ProvinceTotally.substring(8, ProvinceTotally.indexOf(','));

                    ProvinceTotally = ProvinceTotally.substring(ProvinceTotally.indexOf(',') + 1);

                    String ProvinceId = ProvinceTotally.substring(11, ProvinceTotally.indexOf(','));

                    //System.out.println(ProvinceComment+" "+ProvinceId);

                    //ProvinceTotally=ProvinceTotally.substring(ProvinceTotally.indexOf(',')+1);


                    //System.out.println("省份:"+ProvinceName+"\t 今日确诊:"+ProvinceTodayConfirmed+"\t 总共确诊:"
                    // +ProvinceTotallyConfirmed+"\t 疑似病例:"+ProvinceSuspectCount+"\t 治愈病列:"+ProvinceCured+
                    // "\t 死亡病例:"+ProvinceDead);

                    jsonObject = jsonObject + "{\n" + "\"locationId\":" + ProvinceId +
                            ",\n\"continentName\":\"亚洲\",\n" +
                            "\"continentEnglishName\":\""+map.continentName("亚洲")+"\",\n" +
                            "\"countryName\":\"中国\",\n" +
                            "\"countryEnglishName\":\""+map.countryEnglishName("中国")+"\",\n" +
                            "\"provinceName\":\"" + ProvinceName +
                            "\",\n\"provinceEnglishName\":\"" + map.provinceName(ProvinceName) +
                            "\",\n\"currentConfirmedCount\":" + ProvinceTodayConfirmed +
                            ",\n\"confirmedCount\":" + ProvinceTotallyConfirmed +
                            ",\n\"suspectedCount\":" + ProvinceSuspectCount +
                            ",\n\"curedCount\":" + ProvinceCured +
                            ",\n\"deadCount\":" + ProvinceDead +
                            ",\n\"comment\":\"" + ProvinceComment +
                            "\",\n\"cities\":[";


                    //System.out.println(ProvinceName);


                    Pattern CitySituation = Pattern.compile("cityName(.*?)}");

                    Matcher CitySituationMatcher = CitySituation.matcher(ProvinceSituation);


                    while (CitySituationMatcher.find()) {

                        String CityCondition = CitySituationMatcher.group(0);

                        String CityName = CityCondition.substring(9, CityCondition.indexOf(','));

                        CityCondition = CityCondition.substring(CityCondition.indexOf(',') + 1);

                        //System.out.println(CityCondition);

                        String TodayConfirmed = CityCondition.substring(22, CityCondition.indexOf(','));

                        CityCondition = CityCondition.substring(CityCondition.indexOf(',') + 1);

                        String TotallyConfirmed = CityCondition.substring(15, CityCondition.indexOf(','));

                        CityCondition = CityCondition.substring(CityCondition.indexOf(',') + 1);

                        String suspectedCount = CityCondition.substring(15, CityCondition.indexOf(','));

                        CityCondition = CityCondition.substring(CityCondition.indexOf(',') + 1);

                        String CuredCount = CityCondition.substring(11, CityCondition.indexOf(','));

                        CityCondition = CityCondition.substring(CityCondition.indexOf(',') + 1);

                        String deadCount = CityCondition.substring(10, CityCondition.indexOf(','));

                        CityCondition = CityCondition.substring(CityCondition.indexOf(',') + 1);

                        String locationId = CityCondition.substring(11, CityCondition.indexOf('}'));

                        //System.out.println(locationId);

                        // System.out.println("省份:"+ProvinceName+"\t 城市:"+CityName+"\t 今日确诊:"+TodayConfirmed+"\t 总共确诊:"
                        //+TotallyConfirmed+"\t 疑似病例:"+suspectedCount+"\t 治愈病列:"+CuredCount+
                        //"\t 死亡病例:"+deadCount);

                        jsonObject = jsonObject + "\n{\n" + "\"cityName\":\"" + CityName +
                                "\",\n\"currentConfirmedCount\":" + TodayConfirmed +
                                ",\n\"confirmedCount\":" + TotallyConfirmed +
                                ",\n\"suspectedCount\":" + suspectedCount +
                                ",\n\"curedCount\":" + CuredCount +
                                ",\n\"deadCount\":" + deadCount +
                                ",\n\"cityEnglishName\":\"" + map.cityName(CityName) +
                                "\",\n\"locationId\":" + locationId + "\n},";

                        //System.out.println("省份"+ProvinceName+" "+CitySituationMatcher.group(0));
                    }

                    if (ProvinceName.equals("香港") != true && ProvinceName.equals("台湾") != true && ProvinceName.equals("澳门") != true) {
                        jsonObject = jsonObject.substring(0, jsonObject.length() - 1);
                    }

                    jsonObject = jsonObject + "\n]\n},\n";

                    //System.out.println(ChinaMatcher.group(0));


                    //System.out.println("\n");
                }

                jsonObject = jsonObject.substring(0, jsonObject.length() - 2);

                jsonObject = jsonObject + "\n],\n\"success\":true\n}";

                jsonObject = jsonObject.replace("\n", "");

                //.out.println(jsonObject);


                net.sf.json.JSONObject json1 = net.sf.json.JSONObject.fromObject(jsonObject);
                System.out.println(json1);

                //System.out.println(json1);

                //Connect MongoDB, port number is 27017
                MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);

                //Connect specific database
                MongoDatabase mDatabase = mongoClient.getDatabase("test"); //"test" is optional
                //System.out.println("Connect to database successfully!");
                //System.out.println("Current MongoDatabase is : "+mDatabase.getName());

                //Switch to specific collection
                MongoCollection collection = mDatabase.getCollection("DXYArea");

                //Read the data file from previous crawler running result
                //BufferedReader br = new BufferedReader(new FileReader("D:/3rdYear/DXYArea.json")); //Need to switch to your own file path
                //String s = null;
                //String text = "";
                //while((s = br.readLine()) != null){
                //System.out.println(s);
                //text = text + s + "\n";
                //}
                //System.out.println(text);

                //Change the content into the type that suits MongoDB
                Document document = new Document(json1);

                //Import the latest data
                collection.insertOne(document);
                contain=false;
                //System.out.println("Successfully insert!");
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                contain=true;
            }
        }
    }
}