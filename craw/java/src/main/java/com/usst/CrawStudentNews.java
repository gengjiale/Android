package com.usst;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CrawStudentNews {
    public static void main(String[] args) throws IOException {
        crawStudentNews();
    }
    public static void crawStudentNews() throws IOException {
        //建立连接
        String url = "http://www.daxuejia.com/shsj/";
        String link_url = "http://www.daxuejia.com";
        Document document = Jsoup.connect(url).get();
        String superior_file_name = "studentNews";
        //该List存储html文档
        List<Element> html_list;
        //该List存储具体文章链接
        List<String>  href_list = new ArrayList<>();
        //该List存储文章标题
        List<String> title_list = new ArrayList<>();
        html_list = document.getElementsByClass("lbra");
        for(Element element : html_list){
            //向List中添加文章标题
            title_list.add(element.text());
            //向List中添加文章链接
            href_list.add(link_url+element.select("a").attr("href"));
        }
        //创建文件
        Utils.creat_dic(superior_file_name);
        //逐一读取文章链接和文章标题，以文章标题为txt文件的文件名，写入文章内容
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
                html_list2 = doc.getElementsByClass("showb");
                for (Element element:html_list2){
//                    Utils.write_txt(superior_file_name,title_list.get(i),element.text());
                    if (Objects.equals(element.text(), ""))
                    {
                        break;
                    }
                    else {
                        JSONObject obj = new JSONObject();
                        obj.put("url",href_list.get(i));
                        obj.put("title",title_list.get(i));
                        obj.put("content",element.text());
//                    String jsonString = Utils.formatJson(obj.toJSONString());
                        jsonObjectArrayList.add(obj);
                    }
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







