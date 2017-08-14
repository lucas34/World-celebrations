package packi.day.network;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import packi.day.BuildConfig;

public final class Network {

    private Network() {
        // No instance
    }

    private static final OkHttpClient CLIENT = new OkHttpClient();

    public static void post(String description, String date, final Callback callback) {
        RequestBody body = new FormBody.Builder()
                .add("text", description)
                .add("date", date)
                .build();

        Request request = new Request.Builder()
                .url(BuildConfig.API_URL + "/post")
                .post(body)
                .build();

        CLIENT.newCall(request).enqueue(callback);
    }

}
