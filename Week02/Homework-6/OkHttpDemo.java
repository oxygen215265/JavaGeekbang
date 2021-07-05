import okhttp3.*;



import java.io.IOException;

public class OkHttpDemo {

    static OkHttpClient myClient = new OkHttpClient();

    public static void main(String args[]) throws IOException {
        doGet("https://square.github.io/okhttp/");
    }

    public static void doGet(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = myClient.newCall(request).execute();
        if (response.isSuccessful()) {
            System.out.println(response.body().string());
        }else {
            System.out.println("Failed to get response");
        }
    }


    public static void doPost(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = myClient.newCall(request).execute();
        if (response.isSuccessful()) {
            System.out.println(response.body().string());
        }else {
            System.out.println("Failed to get response");
        }

    }
}
