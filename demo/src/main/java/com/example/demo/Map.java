package com.example.demo;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Map {
    InputStream is;
    JSONObject json;
    String x1;
    public Map(){
        is = this.getClass().getResourceAsStream("/githubApi.json");
        //System.out.println(location);
        json = LocalJson.readJsonFile(is);

        //System.out.println(json);
        x1 = json.getString("results");
        //System.out.println(x1);
        x1 = x1.substring(1, x1.length() - 1);
    }

    public String countryEnglishName(String name) {
        //System.out.println(x1);
        Pattern foreignCountry = Pattern.compile("\\{(.*?)},");

        Matcher foreignMatcher = foreignCountry.matcher(x1);

        String contientName = null;

        while (foreignMatcher.find()) {
            //System.out.println(foreignMatcher.group(0));
            contientName = foreignMatcher.group(0);
            Pattern contient = Pattern.compile("countryName(.*?),");

            Matcher contientMatcher = contient.matcher(contientName);


            Pattern contientEnglish = Pattern.compile("countryEnglishName(.*?),");

            Matcher contientEngMatcher = contientEnglish.matcher(contientName);
            while (contientMatcher.find() && contientEngMatcher.find()) {
                String chin = contientMatcher.group(0);
                chin = chin.substring(14, chin.indexOf(',') - 1);

                String eng = contientEngMatcher.group(0);
                eng = eng.substring(21, eng.indexOf(',') - 1);
                if (chin.equals(name)) {
                    //System.out.println(eng);
                    return eng;
                }
            }
        }
        return null;
    }

    public String continentName(String name) {
        //System.out.println(x1);
        Pattern foreignCountry = Pattern.compile("\\{(.*?)},");

        Matcher foreignMatcher = foreignCountry.matcher(x1);

        String contientName = null;

        while (foreignMatcher.find()) {
            //System.out.println(foreignMatcher.group(0));
            contientName = foreignMatcher.group(0);
            Pattern contient = Pattern.compile("continentName(.*?),");

            Matcher contientMatcher = contient.matcher(contientName);


            Pattern contientEnglish = Pattern.compile("continentEnglishName(.*?),");

            Matcher contientEngMatcher = contientEnglish.matcher(contientName);
            while (contientMatcher.find() && contientEngMatcher.find()) {
                String chin = contientMatcher.group(0);
                chin = chin.substring(16, chin.indexOf(',') - 1);

                String eng = contientEngMatcher.group(0);
                eng = eng.substring(23, eng.indexOf(',') - 1);
                //System.out.println(chin + " " + eng);
                if (chin.equals(name)) {
                    //System.out.println(eng);
                    return eng;
                }
            }
        }
        return null;
    }

    public String provinceName(String name) {
        //System.out.println(x1);
        Pattern foreignCountry = Pattern.compile("\\{(.*?)},");

        Matcher foreignMatcher = foreignCountry.matcher(x1);

        String contientName = null;

        while (foreignMatcher.find()) {
            //System.out.println(foreignMatcher.group(0));
            contientName = foreignMatcher.group(0);
            Pattern contient = Pattern.compile("provinceName(.*?),");

            Matcher contientMatcher = contient.matcher(contientName);


            Pattern contientEnglish = Pattern.compile("provinceEnglishName(.*?),");

            Matcher contientEngMatcher = contientEnglish.matcher(contientName);
            while (contientMatcher.find() && contientEngMatcher.find()) {
                String chin = contientMatcher.group(0);
                chin = chin.substring(15, chin.indexOf(',') - 1);

                String eng = contientEngMatcher.group(0);
                eng = eng.substring(22, eng.indexOf(',') - 1);
                //System.out.println(chin + " " + eng);
                if (chin.equals(name)) {
                    //System.out.println(eng);
                    return eng;
                }
            }
        }
        return null;
    }
    public String cityName(String name) {
        //System.out.println(x1);
        Pattern foreignCountry = Pattern.compile("\\{(.*?)},");

        Matcher foreignMatcher = foreignCountry.matcher(x1);

        String contientName = null;

        while (foreignMatcher.find()) {
            //System.out.println(foreignMatcher.group(0));
            contientName = foreignMatcher.group(0);
            Pattern contient = Pattern.compile("cityName(.*?),");

            Matcher contientMatcher = contient.matcher(contientName);


            Pattern contientEnglish = Pattern.compile("cityEnglishName(.*?),");

            Matcher contientEngMatcher = contientEnglish.matcher(contientName);
            while (contientMatcher.find() && contientEngMatcher.find()) {
                String chin = contientMatcher.group(0);
                chin = chin.substring(11, chin.indexOf(',') - 1);

                String eng = contientEngMatcher.group(0);
                if(eng.indexOf('}')!=-1) {
                    eng = eng.substring(18,eng.indexOf('}')-1);
                }
                else{
                    eng=eng.substring(18,eng.indexOf(',')-1);
                }
                //System.out.println(chin + " " + eng);
                if (chin.equals(name)) {
                    //System.out.println(eng);
                    return eng;
                }
            }
        }
        return null;
    }
}
