package packi.day.main;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.joda.time.MonthDay;

import java.util.Locale;

import packi.day.common.AnalyticsTracker;
import packi.day.main.databinding.FragmentFocusCelebrationBinding;
import packi.day.store.InternationalDay;
import packi.day.store.StoreData;

public class FocusCelebrationFragment extends Fragment implements OnSwipe {

    private final ObservableField<InternationalDay> celebrationObservableField = new ObservableField<>();
    private StoreData storeData;

    @BindingAdapter({"bind:showText"})
    public static void loadText(TextView view, String text) {
        view.setText(text);
    }

    @BindingAdapter({"bind:showImage"})
    public static void loadImage(ImageView view, Uri uri) {
        Picasso.with(view.getContext()).load(uri).error(R.drawable.noimage).into(view);
    }

    @BindingAdapter({"bind:showDate"})
    public static void loadDate(TextView view, MonthDay celebrationDate) {
        if (celebrationDate.isEqual(MonthDay.now())) {
            view.setText(view.getContext().getString(R.string.today));
        } else {
            int date = celebrationDate.getDayOfMonth();
            String month = celebrationDate.monthOfYear().getAsText(Locale.getDefault());
            view.setText(view.getContext().getString(R.string.date_text_title, date, month));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StoreLocator locator = (StoreLocator) getActivity();
        storeData = locator.getStore();
        setHasOptionsMenu(false);

        celebrationObservableField.set(storeData.get(getDate(savedInstanceState, getArguments())));
    }

    @Override
    public void onResume() {
        super.onResume();
        AnalyticsTracker.getInstance(getActivity()).sendTracker("/launcher");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentFocusCelebrationBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_focus_celebration, container, false);
        binding.setCelebration(celebrationObservableField);
        binding.fab.setOnClickListener(v -> random());
        binding.randomButton.setOnClickListener(v -> random());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setOnTouchListener(new OnSwipeTouchListener(getActivity(), this));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("date", celebrationObservableField.get().getDate());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSwipeHorizontal(int direction) {
        MonthDay date = celebrationObservableField.get().getDate().plusDays(direction);
        celebrationObservableField.set(storeData.get(date));
    }

    @Override
    public void onSwipeVertical(int direction) {
        MonthDay date = celebrationObservableField.get().getDate().plusMonths(direction);
        celebrationObservableField.set(storeData.get(date));
    }

    public void random() {
        celebrationObservableField.set(storeData.getRandomCelebration());
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