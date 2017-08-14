package packi.day.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlarmService extends Service {

    private final Alarm alarm = new Alarm();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarm.init(this);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}