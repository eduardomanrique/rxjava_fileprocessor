package com.eduardomanrique.tsrd.util;

import java.util.Calendar;

/**
 * Created by emanrique on 19/06/17.
 */
public class DateUtil {
    public static final Calendar createDate(int year, int month, int day) {
        Calendar date = Calendar.getInstance();
        date.set(year, month, day, 0, 0, 0);
        return date;
    }
}
