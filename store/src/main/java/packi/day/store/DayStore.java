package packi.day.store;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.MonthDay;

import java.util.List;
import java.util.Set;

/**
 * Created by lucas34990 on 12/2/18.
 */

public interface DayStore {

    void loadData(Context context);

    @Nullable
    InternationalDay get(MonthDay date);

    Set<Integer> count(String criteria);

    List<InternationalDay> find(String criteria);

    @NonNull
    InternationalDay random();

}
