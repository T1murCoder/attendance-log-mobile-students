package ru.technosopher.attendancelogappstudents.ui.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

public class Utils {

    public static int visibleOrGone(boolean isVisible){
        return isVisible ? View.VISIBLE : View.GONE;
    }

    public static void setClipboard(String text, Context context) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Скопировано!", Toast.LENGTH_SHORT).show();
    }
}
