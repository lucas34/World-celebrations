package packi.day.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;
import android.widget.TimePicker;

public class TimePickerPreferences extends DialogPreference {

    private int lastHour;
    private int lastMinute;
    private TimePicker picker;

    public TimePickerPreferences(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
    }

    public static int getHour(String time) {
        try {
            String[] pieces = time.split(":");
            return Integer.parseInt(pieces[0]);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getMinute(String time) {
        try {
            String[] pieces = time.split(":");
            return Integer.parseInt(pieces[1]);
        } catch (Exception e) {
            return 0;
        }
    }

//    @Override
//    protected View onCreateDialogView() {
//        picker = new TimePicker(getContext());
//        return picker;
//    }
//
//    @Override
//    protected void onBindDialogView(@NonNull View v) {
//        super.onBindDialogView(v);
//
//        picker.setCurrentHour(lastHour);
//        picker.setCurrentMinute(lastMinute);
//    }
//
//    @Override
//    protected void onDialogClosed(boolean positiveResult) {
//        super.onDialogClosed(positiveResult);
//
//        if (positiveResult) {
//            lastHour = picker.getCurrentHour();
//            lastMinute = picker.getCurrentMinute();
//
//            String time = lastHour + ":" + lastMinute;
//
//            persistString(time);
//            callChangeListener(time);
//        }
//    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String time;

        if (restoreValue) {
            if (defaultValue == null) {
                time = getPersistedString("00:00");
            } else {
                time = getPersistedString(defaultValue.toString());
            }
        } else {
            time = defaultValue.toString();
        }

        lastHour = getHour(time);
        lastMinute = getMinute(time);
    }
}
