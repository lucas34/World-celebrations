package packi.day.notification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.journeemondialelib.Celebration;
import com.journeemondialelib.WorldCelebration;
import com.journeemondialelib.share.AnalyticsTracker;
import com.journeemondialelib.share.SomeTools;

import org.joda.time.MonthDay;

import packi.day.R;
import packi.day.WorldApplication;
import packi.day.ui.ActivityMain;


public class NotificationExecutor extends IntentService {

    private static final MonthDay TODAY = MonthDay.now();

    public NotificationExecutor() {
        super("NotificationExecutor");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        WorldCelebration celebrationHelper = WorldApplication.with(getApplicationContext());
        if (celebrationHelper.hasCelebration(TODAY)) {
            Celebration celebration = celebrationHelper.getCelebration(TODAY);

            showNotification(celebration.name);
            SomeTools.sendToWear(this, celebration.name);
        }
    }

    private void showNotification(String celebration) {
        Intent intent = new Intent(this, ActivityMain.class);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
                .setContentTitle(getString(R.string.today))
                .setContentText(celebration)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notification);
        AnalyticsTracker.getInstance(this).sendTracker("/notification/publish");
    }
}
