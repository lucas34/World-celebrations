package packi.day;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import packi.day.common.AnalyticsTracker;

public class NotificationActivity extends Activity {

    public static final String EXTRA_TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Intent intent = getIntent();
        if (intent != null) {
            TextView text = findViewById(R.id.text_view);
            text.setText(intent.getStringExtra(EXTRA_TITLE));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsTracker.getInstance(getApplicationContext()).sendTracker("/wear/notification");
    }
}