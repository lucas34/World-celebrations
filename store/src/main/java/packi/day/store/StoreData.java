package packi.day.store;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.MonthDay;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
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
    public InternationalDay getRandomCelebration() {
        InternationalDay result;
        do {
            result = delegate.get(randomDate());
        } while (result == null);
        return result;
    }

    public boolean hasCelebration(MonthDay date) {
        return delegate.get(date) != null;
    }

    private MonthDay randomDate() {
        int minDay = 1;
        int maxDay;
        int minMonth = 1;
        int maxMonth = 12;
        int dayRandom;
        int monthRandom;

        Random random = new Random();
        monthRandom = random.nextInt(maxMonth - minMonth + 1) + minMonth;

        Calendar calendar = new GregorianCalendar(0, monthRandom - 1, 1);
        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        random = new Random();
        dayRandom = random.nextInt(maxDay - minDay + 1) + minDay;

        return new MonthDay(monthRandom, dayRandom);
    }

}
