package packi.day.store;

import android.content.Context;
import android.support.annotation.NonNull;

import org.joda.time.MonthDay;

import java.util.List;
import java.util.Set;

import packi.day.store.feature.R;

/**
 * Created by lucas34990 on 12/2/18.
 */

public class StoreData {

    private final Context context;
    private final DayStore delegate;

    public StoreData(Context context, DayStore delegate) {
        this.context = context;
        this.delegate = delegate;

        this.delegate.loadData(context);
    }

    @NonNull
    public InternationalDay get(MonthDay date) {
        InternationalDay internationalDay = delegate.get(date);
        if (internationalDay != null) {
            return internationalDay;
        } else {
            return new InternationalDay(date, context.getString(R.string.no_celebration));
        }
    }

    public Set<Integer> count(String criteria) {
        return delegate.count(criteria);
    }

    public List<InternationalDay> find(String criteria) {
        return delegate.find(criteria);
    }

    @NonNull
    public InternationalDay random() {
        return delegate.random();
    }

    public boolean hasCelebration(MonthDay date) {
        return delegate.get(date) != null;
    }

}
