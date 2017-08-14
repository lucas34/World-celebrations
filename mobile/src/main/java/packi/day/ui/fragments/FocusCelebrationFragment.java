package packi.day.ui.fragments;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.journeemondialelib.WorldCelebration;
import com.journeemondialelib.share.AnalyticsTracker;
import com.squareup.picasso.Picasso;

import org.joda.time.MonthDay;

import java.util.Locale;

import packi.day.R;
import packi.day.WorldApplication;
import packi.day.databinding.FragmentFocusCelebrationBinding;
import packi.day.lib.OnSwipe;
import packi.day.lib.OnSwipeTouchListener;
import packi.day.ui.ActivityMain;

public class FocusCelebrationFragment extends Fragment implements OnSwipe {

    private final ObservableField<MonthDay> date = new ObservableField<>();
    private WorldCelebration celebrationHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        celebrationHelper = ((WorldApplication) getActivity().getApplication()).getCelebrationHelper();
        setHasOptionsMenu(false);

        date.set(getDate(savedInstanceState, getArguments()));
    }

    @Override
    public void onResume() {
        super.onResume();
        AnalyticsTracker.getInstance(getActivity()).sendTracker("/launcher");
        ((ActivityMain) getActivity()).setScreenTitle(R.string.app_name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentFocusCelebrationBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_focus_celebration, container, true);
        binding.setHandler(this);
        binding.setLoader(celebrationHelper);
        binding.setDate(date);
        binding.fab.setOnClickListener(v -> random());
        binding.randomButton.setOnClickListener(v -> random());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setOnTouchListener(new OnSwipeTouchListener(getActivity(), this));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("date", date.get());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSwipeHorizontal(int direction) {
        date.set(date.get().plusDays(direction));
    }

    @Override
    public void onSwipeVertical(int direction) {
        date.set(date.get().plusMonths(direction));
    }

    public void random() {
        date.set(celebrationHelper.getRandomCelebrationDate());
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            view.setImageResource(R.drawable.noimage);
        } else {
            Picasso.with(view.getContext()).load(imageUrl).error(R.drawable.noimage).into(view);
        }
    }

    @BindingAdapter({"bind:showDate"})
    public static void loadDate(TextView view, MonthDay currentDate) {
        if (currentDate.isEqual(MonthDay.now())) {
            view.setText(view.getContext().getString(R.string.today));
        } else {
            int date = currentDate.getDayOfMonth();
            String month = currentDate.monthOfYear().getAsText(Locale.getDefault());
            view.setText(view.getContext().getString(R.string.date_text_title, date, month));
        }
    }

    private MonthDay getDate(@Nullable Bundle savedInstanceState, @Nullable Bundle args) {
        if (savedInstanceState != null && savedInstanceState.containsKey("date")) {
            return (MonthDay) savedInstanceState.getSerializable("date");
        }

        if (args != null && args.containsKey("date")) {
            return (MonthDay) args.getSerializable("date");
        }

        return MonthDay.now();
    }

}