package com.example.demo;

import net.sf.json.JSONObject;

import java.io.*;


public class LocalJson {
    public static JSONObject readJsonFile(InputStream is){
        String jsonStr="";
        try{
            //System.out.println(fileName);
            //File jsonFile=new File(fileName);

            //FileReader fileReader=new FileReader(is);
            //Reader reader=new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            //int ch=0;
            //StringBuffer sb=new StringBuffer();
            //while((ch=reader.read())!=-1){
             //   sb.append((char)ch);
            //}
            //fileReader.close();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            String s="";
            String sb="";
            while((s=br.readLine())!=null) {
                //System.out.println(s);
                sb=sb+s;
            }
            //System.out.println("zzzz"+sb);
            jsonStr=sb;
            jsonStr.replace(" ","");
            System.out.println(jsonStr);
            return JSONObject.fromObject(jsonStr);
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
   /* public static void main(String[] args){
        InputStream is=this.getClass().getResourceAsStream("/ChinaMap.json");
        JSONObject json1=readJsonFile(is);
        System.out.println(json1);
    }*/
}
