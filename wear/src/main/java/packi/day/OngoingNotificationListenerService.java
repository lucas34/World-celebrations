package packi.day;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import packi.day.common.Constants;

public class OngoingNotificationListenerService extends WearableListenerService {

    private static final String TAG = OngoingNotificationListenerService.class.getSimpleName();

    private int notificationId = 100;

    private GoogleApiClient apiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        apiClient.connect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.release();

        if (!apiClient.isConnected()) {
            ConnectionResult connectionResult = apiClient
                    .blockingConnect(30, TimeUnit.SECONDS);
            if (!connectionResult.isSuccess()) {
                Log.e(TAG, "Service failed to connect to GoogleApiClient.");
                return;
            }
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager == null) return;

        Intent notificationIntent = new Intent(this, NotificationActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.WearableExtender extender = new Notification.WearableExtender()
                .setDisplayIntent(pendingIntent);

        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .extend(extender);

        for (DataEvent event : events) {
            if (event.getType() != DataEvent.TYPE_CHANGED) {
                continue;
            }

            String path = event.getDataItem().getUri().getPath();
            if (!Constants.PATH_NOTIFICATION.equals(path)) {
                Log.d(TAG, "Unrecognized path: " + path);
                continue;
            }

            // Get the data out of the event
            DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
            final String title = dataMapItem.getDataMap().getString(Constants.KEY_TITLE);

            notificationIntent.putExtra(NotificationActivity.EXTRA_TITLE, title);

            notificationManager.notify(++notificationId, notificationBuilder.build());
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(Constants.PATH_DISMISS)) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.cancel(notificationId);
        }
    }
}