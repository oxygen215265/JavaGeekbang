import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import sun.net.www.http.HttpClient;

import java.io.IOException;

public class OkHttpDemo {
    public static void main(String args[]) throws IOException {
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
        Request request = new Request.Builder().url("http://localhost:8001/").build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            System.out.println(response.body().string());
        }else {
            System.out.println("Failed to get response");
        }
    }

}
