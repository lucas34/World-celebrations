package com.journeemondialelib;

import android.content.Context;
import android.text.TextUtils;

import org.joda.time.MonthDay;

import java.io.Closeable;
import java.util.HashSet;
import java.util.Set;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public final class WorldCelebration implements Closeable {

    private final Realm database;
    private final Celebration empty;

    public WorldCelebration(Context context) {
        empty = create(context);
        database = Realm.getDefaultInstance();

        if (database.isEmpty()) {
            database.beginTransaction();
            database.createOrUpdateAllFromJson(Celebration.class,
                    context.getResources().openRawResource(R.raw.celebration));
            database.commitTransaction();
        }
    }

    private Celebration create(Context context) {
        Celebration celebration = new Celebration();
        celebration.name = context.getString(R.string.no_celebration);
        return celebration;
    }

    public boolean hasCelebration(MonthDay date) {
        return find(date).count() != 0;
    }

    public Celebration getCelebration(MonthDay date) {
        Celebration first = find(date).findFirst();
        return first != null ? first : empty;
    }

    public MonthDay getRandomCelebrationDate() {
        MonthDay date = CalendarTools.getRandomDate();
        while (!hasCelebration(date)) {
            date = CalendarTools.getRandomDate();
        }

        return date;
    }

    public String getDrawableImage(Celebration celebration) {
        if (celebration != null && !TextUtils.isEmpty(celebration.image)) {
            return "file:///android_asset/" + celebration.image + ".png";
        } else {
            return "file:///android_asset/noimage.png";
        }
    }

    public RealmResults<Celebration> find(String nameConstais) {
        RealmQuery<Celebration> where = database.where(Celebration.class);
        if (!TextUtils.isEmpty(nameConstais)) {
            where = where.contains("name", nameConstais, Case.INSENSITIVE);
        }
        return where.findAll();
    }

    public Set<Integer> count(String criteria) {
        Set<Integer> result = new HashSet<>(12);
        int previous = 0;
        for (int month = 1; month <= 12; month++) {
            long current = database
                    .where(Celebration.class)
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

    private RealmQuery<Celebration> find(MonthDay date) {
        return database.where(Celebration.class)
                .equalTo("day", date.getDayOfMonth())
                .equalTo("month", date.getMonthOfYear());
    }

    @Override
    public void close() {
        database.close();
    }

}





