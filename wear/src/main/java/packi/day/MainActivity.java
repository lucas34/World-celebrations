package packi.day;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.CardScrollView;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.journeemondialelib.Celebration;
import com.journeemondialelib.WorldCelebration;
import com.journeemondialelib.share.AnalyticsTracker;
import com.squareup.picasso.Picasso;

import org.joda.time.MonthDay;

public class MainActivity extends Activity {

    private WorldCelebration worldCelebrationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null || worldCelebrationHelper == null) {
            worldCelebrationHelper = new WorldCelebration(this);
        }
        
        CardScrollView cardScrollView = (CardScrollView) findViewById(R.id.card_scroll_view);
        cardScrollView.setCardGravity(Gravity.BOTTOM);

        final MonthDay now = MonthDay.now();
        Celebration celebration = worldCelebrationHelper.getCelebration(now);

        ImageView image = (ImageView) findViewById(R.id.IllustrationJournee);
        Picasso.with(this).load(worldCelebrationHelper.getDrawableImage(celebration)).into(image);

        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(celebration.name);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsTracker.getInstance(getApplicationContext()).sendTracker("/wear/launcher");
    }

    @Override
    protected void onDestroy() {
        worldCelebrationHelper.close();
        super.onDestroy();
    }
}
