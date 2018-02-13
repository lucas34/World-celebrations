package packi.day.store.feature.realm;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.joda.time.MonthDay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import packi.day.store.DayStore;
import packi.day.store.InternationalDay;

/**
 * Created by lucas34990 on 12/2/18.
 */

public class RealmDayStore implements DayStore {

    private final Context context;
    private final Realm database;

    public RealmDayStore(Context context) {
        this.context = context;
        this.database = Realm.getDefaultInstance();

        if (database.isEmpty()) {
            database.beginTransaction();
            database.createOrUpdateAllFromJson(RealmInternationalDay.class,
                    context.getResources().openRawResource(packi.day.store.feature.R.raw.celebration));
            database.commitTransaction();
        }
    }

    @Nullable
    @Override
    public InternationalDay get(MonthDay date) {
        RealmInternationalDay first = database.where(RealmInternationalDay.class)
                .equalTo("day", date.getDayOfMonth())
                .equalTo("month", date.getMonthOfYear())
                .findFirst();

        return first != null ? mapData(first) : null;
    }

    @Override
    public Set<Integer> count(String criteria) {
        Set<Integer> result = new HashSet<>(12);
        int previous = 0;
        for (int month = 1; month <= 12; month++) {
            long current = database
                    .where(RealmInternationalDay.class)
                    .contains("name", criteria, Case.INSENSITIVE)
                    .equalTo("month", month)
                    .count();

            if (current > 0) {
                if (result.isEmpty()) {
                    result.add(0);
                    previous += current + 1;
                } else {
                    result.add(previous);
                    previous += current + 1;
                }
            }
        }
        return result;
    }

    @Override
    public List<InternationalDay> find(String criteria) {
        RealmQuery<RealmInternationalDay> where = database.where(RealmInternationalDay.class);
        if (!TextUtils.isEmpty(criteria)) {
            where = where.contains("name", criteria, Case.INSENSITIVE);
        }
        List<InternationalDay> result = new ArrayList<>((int) where.count());
        for (RealmInternationalDay data : where.findAll()) {
            result.add(mapData(data));
        }
        return result;
    }

    private InternationalDay mapData(RealmInternationalDay data) {
        return new InternationalDay(data.id, data.name, data.day, data.month, data.image);
    }


}
