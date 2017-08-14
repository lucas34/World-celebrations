package com.journeemondialelib;

import org.joda.time.MonthDay;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;


/* package */ final class CalendarTools {

    private CalendarTools() {
        // No instance
    }

    /* package */ static MonthDay getRandomDate() {
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
