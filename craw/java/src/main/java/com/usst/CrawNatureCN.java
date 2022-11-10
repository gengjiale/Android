package com.usst;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrawNatureCN {
    public static void main(String[] args) throws IOException {
        crawNatureCN();
    }
    public static void crawNatureCN() throws IOException {
        String url = "http://www.naturechina.com/categories/sci";
        String link_url = "http://www.naturechina.com";
        Document document = Jsoup.connect(url).get();
        String superior_file_name = "NatureCN";
//        System.out.println(document.toString());
        List<Element> html_list;
        //该List存储具体文章链接
        List<String>  href_list = new ArrayList<>();
        //该List存储文章标题
        List<String> title_list = new ArrayList<>();
        html_list = document.getElementsByClass("itemlist");
        for(Element element : html_list){
            //向List中添加文章标题
            title_list.add(element.getElementsByClass("title").text());
//            System.out.println(element.getElementsByClass("title").text());
            //向List中添加文章链接
            href_list.add(link_url+element.select("a").attr("href"));
//            System.out.println(link_url+element.select("a").attr("href"));
        }
        Utils.creat_dic(superior_file_name);
        String path = Utils.get_now_path()+"\\"+superior_file_name+"\\"+superior_file_name+".json";
        Writer writer = new OutputStreamWriter(new FileOutputStream(path),"UTF-8");
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
        }catch (Exception e){
        }
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<>();
        for(int i=0;i<href_list.size();i++){
//        for(int i=0;i<3;i++){
            try {
                System.out.println("链接("+href_list.get(i)+")标题<"+title_list.get(i)+">正在爬取");
                Document doc = Jsoup.connect(href_list.get(i)).get();
                List<Element> html_list2;
                html_list2 = doc.getElementsByClass("doinum");
                for (Element element:html_list2){
//                    Utils.write_txt(superior_file_name,title_list.get(i),element.text());
                    JSONObject obj = new JSONObject();
                    obj.put("brief_introduction_url",href_list.get(i));
//                    System.out.println("brief_introduction_url"+href_list.get(i));
                    obj.put("title",title_list.get(i));
//                    System.out.println("title"+title_list.get(i));
                    obj.put("original_text_url",element.select("a").attr("href"));
//                    System.out.println("original_text_url"+element.select("a").attr("href"));
                    obj.put("content",doc.getElementsByClass("paragraph").text());
//                    System.out.println("content"+doc.getElementsByClass("paragraph").text());
//                    String jsonString = Utils.formatJson(obj.toJSONString());
                    jsonObjectArrayList.add(obj);
                }
            }
            catch (Exception e){
                System.out.println("链接("+href_list.get(i)+")标题<"+title_list.get(i)+">爬取失败");
                continue;
            }

        }
        writer.write(jsonObjectArrayList.toString());
        writer.flush();
        writer.close();
    }
}
