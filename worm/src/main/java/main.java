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


        //System.out.println(result);


        while(foreignMatcher.find()){
            //System.out.println(foreignMatcher.group(0));
        }




        //这部分为过滤出来国内的状态
        Pattern ChinaCountry=Pattern.compile("provinceName(.*?)]");

        Matcher ChinaMatcher=ChinaCountry.matcher(result);



        ChinaMatcher.find();                            //此行的功能为为后面过滤掉非中国的地区


        while(ChinaMatcher.find()){

            String ProvinceSituation=ChinaMatcher.group(0);

            //System.out.println(ProvinceSituation);

            Pattern Province=Pattern.compile("Name:(.*?)create");

            Matcher ProvinceNameMatcher=Province.matcher(ProvinceSituation);

            String ProvinceName=ProvinceSituation.substring(0,ProvinceSituation.indexOf(','));

            System.out.println(ProvinceName);


            Pattern CitySituation=Pattern.compile("cityName(.*?)}");

            Matcher CitySituationMatcher=CitySituation.matcher(ProvinceSituation);


            if(CitySituationMatcher.find()) {
                while (CitySituationMatcher.find()) {
                    System.out.println(CitySituationMatcher.group(0));
                }
            }

            //System.out.println(ChinaMatcher.group(0));


            System.out.println("\n\n");
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
