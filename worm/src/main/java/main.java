import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main {
    public static void main(String[] args){


        //String url="https://view.inews.qq.com/g2/getOnsInfo?name=disease_h5";    //此处为腾讯的接口


        String url="https://ncov.dxy.cn/ncovh5/view/pneumonia";         //丁香园的网站


        String result="";
        BufferedReader in=null;
        try{
            URL realUrl=new URL(url);
            URLConnection connection=realUrl.openConnection();
            connection.connect();
            in=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line=in.readLine())!=null){
                result+=line+"\n";
            }

        }catch(Exception e){
            System.out.println("发送GET请求出现异常"+e);
            e.printStackTrace();
        }
        finally{
            try
            {
                if(in!=null){
                    in.close();
                }
            }catch(Exception e2){
                e2.printStackTrace();
            }
        }


        //这部分为可视化处理

        result=result.replace("]","]\n");
        //result=result.replace("{","\n{");
        //result=result.replace(",",",\n");
        result=result.replace("\"","");


        //下面这部分为过滤出来国外的情况

        Pattern foreignCountry =Pattern.compile("id:(.*?).json},");

        Matcher foreignMatcher=foreignCountry.matcher(result);


        System.out.println("以下为世界疫情汇总:\n");


        while(foreignMatcher.find()){

            String Foreign=foreignMatcher.group(0);

            Foreign=Foreign.substring(Foreign.indexOf("continents"));

            String Continent=Foreign.substring(11,Foreign.indexOf(','));

            Foreign=Foreign.substring(Foreign.indexOf("provinceName"));

            String CountryName=Foreign.substring(13,Foreign.indexOf(','));

            Foreign=Foreign.substring(Foreign.indexOf("currentConfirmedCount"));

            String CountryTodayConfirmed=Foreign.substring(22,Foreign.indexOf(','));

            Foreign=Foreign.substring(Foreign.indexOf("confirmedCount"));

            String CountryTotally=Foreign.substring(15,Foreign.indexOf(','));

            Foreign=Foreign.substring(Foreign.indexOf("suspectedCount"));

            String CountrySuspect=Foreign.substring(15,Foreign.indexOf(','));

            Foreign=Foreign.substring(Foreign.indexOf(',')+1);

            String CountryCure=Foreign.substring(11,Foreign.indexOf(','));

            Foreign=Foreign.substring(Foreign.indexOf(',')+1);

            String CountryDead=Foreign.substring(10,Foreign.indexOf(','));

            Foreign=Foreign.substring(Foreign.indexOf("countryShortCode"));

            String CountryCode=Foreign.substring(17,Foreign.indexOf(','));

            //System.out.println(Foreign);

            Foreign=Foreign.substring(Foreign.indexOf("countryFullName"));

            String CountryFullName=Foreign.substring(16,Foreign.indexOf(','));


            System.out.println("所属大陆:"+Continent+" \t国家名称:"+CountryName+" \t今日感染:"+CountryTodayConfirmed+" \t总共感染:"+CountryTotally+" \t疑似病例:"+CountrySuspect+" \t治愈病列:"+CountryCure
            +" \t死亡病例:"+CountryDead+" \t城市代码:"+CountryCode+" \t城市全称:"+CountryFullName);

            //System.out.println(foreignMatcher.group(0));
        }




        //这部分为过滤出来国内的状态
        Pattern ChinaCountry=Pattern.compile("provinceName(.*?)]");

        Matcher ChinaMatcher=ChinaCountry.matcher(result);



        ChinaMatcher.find();                            //此行的功能为为后面过滤掉非中国的地区

        ChinaMatcher.find();


        System.out.println("\n\n以下为国内各省份城市疫情汇总:\n");

        while(ChinaMatcher.find()){

            String ProvinceSituation=ChinaMatcher.group(0);

            //System.out.println(ProvinceSituation);

            Pattern Province=Pattern.compile("Name:(.*?)create");

            Matcher ProvinceNameMatcher=Province.matcher(ProvinceSituation);

            String ProvinceName=ProvinceSituation.substring(13,ProvinceSituation.indexOf(','));

            try {

                String ProvinceTotally = ProvinceSituation.substring(0, ProvinceSituation.indexOf("comment"));

                //System.out.println(ProvinceTotally);

                ProvinceTotally=ProvinceTotally.substring(ProvinceTotally.indexOf("currentConfirmedCount"));

                //System.out.println(ProvinceTotally);

                String ProvinceTodayConfirmed=ProvinceTotally.substring(22,ProvinceTotally.indexOf(','));

                ProvinceTotally=ProvinceTotally.substring(ProvinceTotally.indexOf(',')+1);

                String ProvinceTotallyConfirmed=ProvinceTotally.substring(15,ProvinceTotally.indexOf(','));

                ProvinceTotally=ProvinceTotally.substring(ProvinceTotally.indexOf(',')+1);

                String ProvinceSuspectCount=ProvinceTotally.substring(15,ProvinceTotally.indexOf(','));

                ProvinceTotally=ProvinceTotally.substring(ProvinceTotally.indexOf(',')+1);

                String ProvinceCured=ProvinceTotally.substring(11,ProvinceTotally.indexOf(','));

                ProvinceTotally=ProvinceTotally.substring(ProvinceTotally.indexOf(',')+1);

                String ProvinceDead=ProvinceTotally.substring(10,ProvinceTotally.indexOf(','));

                System.out.println("省份:"+ProvinceName+"\t 今日确诊:"+ProvinceTodayConfirmed+"\t 总共确诊:"
                        +ProvinceTotallyConfirmed+"\t 疑似病例:"+ProvinceSuspectCount+"\t 治愈病列:"+ProvinceCured+
                        "\t 死亡病例:"+ProvinceDead);

            }catch(Exception e){
                System.out.println();
            };
            //System.out.println(ProvinceName);


            Pattern CitySituation=Pattern.compile("cityName(.*?)}");

            Matcher CitySituationMatcher=CitySituation.matcher(ProvinceSituation);


                while (CitySituationMatcher.find()) {

                    String CityCondition=CitySituationMatcher.group(0);

                    String CityName=CityCondition.substring(9,CityCondition.indexOf(','));

                    CityCondition=CityCondition.substring(CityCondition.indexOf(',')+1);

                    //System.out.println(CityCondition);

                    String TodayConfirmed=CityCondition.substring(22,CityCondition.indexOf(','));

                    CityCondition=CityCondition.substring(CityCondition.indexOf(',')+1);

                    String TotallyConfirmed=CityCondition.substring(15,CityCondition.indexOf(','));

                    CityCondition=CityCondition.substring(CityCondition.indexOf(',')+1);

                    String suspectedCount=CityCondition.substring(15,CityCondition.indexOf(','));

                    CityCondition=CityCondition.substring(CityCondition.indexOf(',')+1);

                    String CuredCount=CityCondition.substring(11,CityCondition.indexOf(','));

                    CityCondition=CityCondition.substring(CityCondition.indexOf(',')+1);

                    String deadCount=CityCondition.substring(10,CityCondition.indexOf(','));


                    System.out.println("省份:"+ProvinceName+"\t 城市:"+CityName+"\t 今日确诊:"+TodayConfirmed+"\t 总共确诊:"
                            +TotallyConfirmed+"\t 疑似病例:"+suspectedCount+"\t 治愈病列:"+CuredCount+
                            "\t 死亡病例:"+deadCount);

                    //System.out.println("省份"+ProvinceName+" "+CitySituationMatcher.group(0));
                }


            //System.out.println(ChinaMatcher.group(0));


            System.out.println("\n");
        }


    }
  /*  public static String regexString (String targetStr,String patternStr){
        Pattern pattern =Pattern.compile(patternStr);

        Matcher matcher=pattern.matcher(targetStr);

        if(matcher.find()){
            return matcher.group(0);
        }
        return "Nothing";
    }*/
}
