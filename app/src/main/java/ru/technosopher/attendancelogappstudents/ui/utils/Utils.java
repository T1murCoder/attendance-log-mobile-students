package ru.technosopher.attendancelogappstudents.ui.utils;

import android.view.View;

import java.util.Calendar;
import java.util.TimeZone;

public class Utils {

    public static int visibleOrGone(boolean isVisible){
        return isVisible ? View.VISIBLE : View.GONE;
    }
}
