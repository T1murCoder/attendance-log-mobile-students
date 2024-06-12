package ru.technosopher.attendancelogappstudents.ui.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateFormatter {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
    public static String getDateStringFromDate(GregorianCalendar date, String pat) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(pat);
        df.setTimeZone(TimeZone.getDefault());
        return df.format(date.getTime());
    }

    public static String getFullTimeStringFromDate(GregorianCalendar start, GregorianCalendar end, String pat) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(pat);
        df.setTimeZone(TimeZone.getDefault());
        return df.format(start.getTime()) + " - " + df.format(end.getTime());
    }

    //    public static int getYearFromDate(GregorianCalendar date) {
//        return date.get(Calendar.YEAR);
//    }
//
//    public static int getMonthFromDate(GregorianCalendar date) {
//        return date.get(Calendar.MONTH);
//    }
//
//    public static int getDayFromDate(GregorianCalendar date) {
//        return date.get(Calendar.DAY_OF_MONTH);
//    }
//    public static String getTimeFromDate(GregorianCalendar date) {
//        return date.get(Calendar.HOUR) + ":" + date.get(Calendar.MINUTE);
//    }
    public static int getActualYear() {
        return LocalDate.now().getYear();
    }

    public static int getActualMonth() {
        return LocalDate.now().getMonthValue() - 1;
    }

    public static int getActualDay() {
        return LocalDate.now().getDayOfMonth();
    }
}
