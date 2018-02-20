package packi.day;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.CardScrollView;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.joda.time.MonthDay;

import packi.day.common.AnalyticsTracker;
import packi.day.store.InternationalDay;
import packi.day.store.StoreData;
import packi.day.store.feature.HashMapDayStore;

public class MainActivity extends Activity {

    private StoreData storeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null || storeData == null) {
            storeData = new StoreData(getApplicationContext(), new HashMapDayStore());
        }

        CardScrollView cardScrollView = findViewById(R.id.card_scroll_view);
        cardScrollView.setCardGravity(Gravity.BOTTOM);

        final MonthDay now = MonthDay.now();
        InternationalDay celebration = storeData.get(now);

        ImageView image = findViewById(R.id.IllustrationJournee);
        Picasso.with(this).load(celebration.getDrawable()).into(image);

        TextView textView = findViewById(R.id.text);
        textView.setText(celebration.name);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsTracker.getInstance(getApplicationContext()).sendTracker("/wear/launcher");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
