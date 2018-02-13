package packi.day.common;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;


public final class SomeTools {

    private SomeTools() {
        // No instance
    }

    public static void hideKeyboard(Activity activity) {

        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            IBinder windowToken = view.getWindowToken();
            inputManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void sendToWear(Context context, String message) {
        GoogleApiClient apiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();

        if (!apiClient.isConnected()) {
            apiClient.blockingConnect();

            if (apiClient.isConnected()) {
                sendMessage(apiClient, message);
            }
        } else {
            sendMessage(apiClient, message);
        }
    }

    private static void sendMessage(GoogleApiClient googleApiClient, String message) {
        if (googleApiClient.isConnected()) {

            PutDataMapRequest datas = PutDataMapRequest.create(Constants.PATH_NOTIFICATION);
            datas.getDataMap().putString(Constants.KEY_TITLE, message);
            PutDataRequest request = datas.asPutDataRequest();

            Wearable.DataApi.putDataItem(googleApiClient, request);
        }
    }
}
