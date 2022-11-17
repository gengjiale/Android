package httpunit;

import com.example.weatherdemo.bean.Newsbean;
import com.example.weatherdemo.bean.StudentNews;
import com.google.gson.Gson;
import com.xuexiang.xhttp2.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Httpunit {
    String url="http://139.196.91.214:8080";
    OkHttpClient okHttpClient = new OkHttpClient();
    public List<StudentNews> get_NatureNews() throws IOException {
        url=url+"/getStudentNews";
        Request request = new Request.Builder().get().url(url).build();
        Response response = okHttpClient.newCall(request).execute();
        String result = Objects.requireNonNull(response.body()).string();
        Gson gson = new Gson();
        List<StudentNews> arrayList = gson.fromJson(result,new TypeToken<ArrayList<StudentNews>>(){}.getType());
        return arrayList;

    }

}
