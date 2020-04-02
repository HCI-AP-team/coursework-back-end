package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsWarm {
    public static void main(String[] args) {


        //String url="https://view.inews.qq.com/g2/getOnsInfo?name=disease_h5";    //此处为腾讯的接口


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
    }
}