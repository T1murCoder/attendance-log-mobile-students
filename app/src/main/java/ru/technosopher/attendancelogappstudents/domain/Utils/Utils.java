package ru.technosopher.attendancelogappstudents.domain.Utils;

import android.view.View;

public class Utils {

    public static int visibleOrGone(boolean isVisible){
        return isVisible ? View.VISIBLE : View.GONE;
    }
}
