package packi.day.store.feature.realm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.joda.time.MonthDay;

import java.util.AbstractList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import packi.day.store.DayStore;
import packi.day.store.InternationalDay;

/**
 * Created by lucas34990 on 12/2/18.
 */

public class RealmDayStore implements DayStore {

    private final Realm database = Realm.getDefaultInstance();

    @Override
    public void loadData(Context context) {
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

    /**
     *
     * @param criteria
     * @return headers positions
     */
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

            if (current == 0) {
                // No result for this month
                continue;
            }

            result.add(previous);

            // 1 cell for the header
            previous += 1;

            // Number of match for the cells
            previous += current;
        }
        return result;
    }

    @Override
    public List<InternationalDay> find(String criteria) {
        RealmQuery<RealmInternationalDay> where = database.where(RealmInternationalDay.class);
        if (!TextUtils.isEmpty(criteria)) {
            where = where.contains("name", criteria, Case.INSENSITIVE);
        }

        final RealmResults<RealmInternationalDay> results = where.findAll();
        return new AbstractList<InternationalDay>() {
            @Override
            public int size() {
                return results.size();
            }

            @Override
            public InternationalDay get(int index) {
                return mapData(results.get(index));
            }
        };
    }

    @NonNull
    @Override
    public InternationalDay random() {
        RealmQuery<RealmInternationalDay> result = database.where(RealmInternationalDay.class);
        int count = (int) result.count();

        int value = new Random().nextInt(count);

        RealmInternationalDay realmInternationalDay = result.findAll().get(value);
        return mapData(realmInternationalDay);
    }

    private InternationalDay mapData(RealmInternationalDay data) {
        return new InternationalDay(data.id, data.name, data.day, data.month, data.image);
    }

}
