package packi.day.store;

import android.support.annotation.NonNull;

import org.joda.time.MonthDay;

import java.util.List;
import java.util.Set;

/**
 * Created by lucas34990 on 12/2/18.
 */

public class StoreData implements DayStore {

    private final DayStore delegate;

    public StoreData(DayStore delegate) {
        this.delegate = delegate;
    }

    @NonNull
    @Override
    public InternationalDay get(MonthDay date) {
        InternationalDay internationalDay = delegate.get(date);
        if (internationalDay != null) {
            return internationalDay;
        } else {
            return new InternationalDay(date, "Default");
        }
    }

    @Override
    public Set<Integer> count(String criteria) {
        return delegate.count(criteria);
    }

    @Override
    public List<InternationalDay> find(String criteria) {
        return delegate.find(criteria);
    }

    @NonNull
    @Override
    public InternationalDay random() {
        return delegate.random();
    }

    public boolean hasCelebration(MonthDay date) {
        return delegate.get(date) != null;
    }

}
