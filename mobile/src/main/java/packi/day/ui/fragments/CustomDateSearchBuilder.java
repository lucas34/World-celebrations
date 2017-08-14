package packi.day.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.samsistemas.calendarview.widget.CalendarView;

import org.joda.time.MonthDay;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import packi.day.R;
import packi.day.ui.ActivityMain;

public class CustomDateSearchBuilder extends AlertDialog
        implements DialogInterface.OnDismissListener, CalendarView.OnDateSelectedListener {

    private final Handler handler = new Handler();
    private final Runnable runnable = () -> {
        if (isShowing()) {
            dismiss();
        }
    };

    private final ActivityMain activity;

    public CustomDateSearchBuilder(final ActivityMain activityMain) {
        super(activityMain, R.style.MyAlertDialogStyle);
        activity = activityMain;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);

        final CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);

        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        calendarView.setIsOverflowDateVisible(true);
        calendarView.setDateAsSelected(new Date(System.currentTimeMillis()));
        calendarView.refreshCalendar(Calendar.getInstance(Locale.getDefault()));
        calendarView.setNextButtonColor(R.color.accent);
        calendarView.setBackButtonColor(R.color.accent);
        calendarView.setOnDateSelectedListener(this);

        setView(calendarView);
        setOnDismissListener(this);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onDateSelected(@NonNull Date date) {
        activity.showFocus(MonthDay.fromDateFields(date));
        handler.postDelayed(runnable, 200);
    }

}
