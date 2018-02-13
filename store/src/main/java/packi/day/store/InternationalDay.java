package packi.day.store;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.MonthDay;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucas34990 on 12/2/18.
 */

public class InternationalDay {

    public int id;

    @NonNull
    public final String name;

    private final int day;
    private final int month;

    @Nullable
    private final String image;

    /* package */ InternationalDay(MonthDay date, @NonNull String name) {
        this.day = date.getDayOfMonth();
        this.month = date.getMonthOfYear();
        this.name = name;
        this.image = null;
    }

    public InternationalDay(int id, @NonNull String name, int day, int month, @NonNull String image) {
        this.id = id;
        this.name = name;
        this.day = day;
        this.month = month;
        this.image = image;
    }

    public InternationalDay(int id, @NonNull String name, MonthDay date, @NonNull String image) {
        this.id = id;
        this.name = name;
        this.day = date.getDayOfMonth();
        this.month = date.getMonthOfYear();
        this.image = image;
    }

    public InternationalDay(JSONObject object) throws JSONException {
        id = object.getInt("id");
        name = object.getString("name");
        day = object.getInt("day");
        month = object.getInt("month");
        image = object.getString("image");
    }

    @NonNull
    public MonthDay getDate() {
        return new MonthDay(month, day);
    }

    @NonNull
    public Uri getDrawable() {
        if (image ==  null || image.length() == 0) {
            return Uri.parse("file:///android_asset/noimage.png");
        } else {
            String base = "https://raw.githubusercontent.com/lucas34/World-celebrations/modular/assets/images/";
            return Uri.parse(base + image + ".png");
        }
    }

}