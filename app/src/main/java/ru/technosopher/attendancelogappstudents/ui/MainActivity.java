package ru.technosopher.attendancelogappstudents.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Map;

import ru.technosopher.attendancelogappstudents.R;
import ru.technosopher.attendancelogappstudents.data.source.CredentialsDataSource;
import ru.technosopher.attendancelogappstudents.ui.utils.NavigationBarChangeListener;
import ru.technosopher.attendancelogappstudents.ui.utils.UpdateSharedPreferences;

public class MainActivity extends AppCompatActivity implements NavigationBarChangeListener, UpdateSharedPreferences {

    private NavController navController;
    private ChipNavigationBar navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationBar = findViewById(R.id.bottom_navigation_bar);
        navigationBar.setItemSelected(R.id.scanner, true);
//        System.out.println(navigationBar.getSelectedItemId());
        navigationBar.setOnItemSelectedListener(destinationFragment -> {
            fragmentNavigation(0, destinationFragment);
        });
    }

    @Override
    public void hideNavigationBar() {
        if (navigationBar != null) navigationBar.setVisibility(View.GONE);
    }

    @Override
    public void showNavigationBar() {
        if (navigationBar != null) navigationBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void changeSelectedItem(int r) {
        navigationBar.setItemSelected(r, true);
    }


    private void fragmentNavigation(int previousFragment, int destinationFragment) {
        navController = Navigation.findNavController(MainActivity.this, R.id.fragmentContainerView);
        if (destinationFragment == R.id.scanner) {

            navController.navigate(R.id.scannerFragment);
            navigationBar.setItemSelected(R.id.scanner, true);
        }
        if (destinationFragment == R.id.group) {

            navController.navigate(R.id.groupFragment);
            navigationBar.setItemSelected(R.id.group, true);

        }
        if (destinationFragment == R.id.profile) {

            navController.navigate(R.id.profileFragment);
            navigationBar.setItemSelected(R.id.profile, true);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateLogin();
    }

    public void updateLogin() {
        CredentialsDataSource.getInstance().updateLogin(getPrefsLogin(), getPrefsPassword());
    }

    @Override
    public void updatePrefs(String id,
                            String login,
                            String password,
                            String name,
                            String surname,
                            String telegram,
                            String github,
                            String photo,
                            Boolean remember) {
        updateId(id);
        updateLogin(login);
        updatePassword(password);
        updateTelegram(telegram);
        updateGithub(github);
        updatePhoto(photo);
        updateName(name);
        updateSurname(surname);
        updateRemember(remember);
    }

    @Override
    public void profileUpdate(String name, String surname, String telegram, String github, String photo) {
        updateName(name);
        updateSurname(surname);
        updateTelegram(telegram);
        updateGithub(github);
        updatePhoto(photo);
    }

    @Override
    public void clearAll() {
        updateId("");
        updateLogin("");
        updatePassword("");
        updateTelegram("");
        updateGithub("");
        updatePhoto("");
        updateName("");
        updateSurname("");
        updateRemember(false);
    }


    @Override
    public Map<String, ?> getPrefs() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getAll();
    }

    @Override
    public String getPrefsId() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.SHARED_PREFS_ID), null);
    }

    @Override
    public String getPrefsLogin() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.SHARED_PREFS_LOGIN), null);
    }

    @Override
    public String getPrefsPassword() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.SHARED_PREFS_PASSWORD), null);
    }

    @Override
    public String getPrefsTelegram() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.SHARED_PREFS_TELEGRAM), null);
    }

    @Override
    public String getPrefsGithub() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.SHARED_PREFS_GITHUB), null);
    }

    @Override
    public String getPrefsPhotoUrl() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.SHARED_PREFS_PHOTO), null);
    }

    @Override
    public String getPrefsName() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.SHARED_PREFS_NAME), null);
    }

    @Override
    public String getPrefsSurname() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.SHARED_PREFS_SURNAME), null);
    }

    @Override
    public Boolean getRemember() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.SHARED_PREFS_CHECKED), "").equals("y");
    }

    @Override
    public void updateId(@NonNull String id) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.SHARED_PREFS_ID), id);
        editor.apply();
    }

    @Override
    public void updateLogin(@NonNull String login) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.SHARED_PREFS_LOGIN), login);
        editor.apply();
    }

    @Override
    public void updatePassword(@NonNull String password) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.SHARED_PREFS_PASSWORD), password);
        editor.apply();
    }

    @Override
    public void updateTelegram(String telegram) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.SHARED_PREFS_TELEGRAM), telegram);
        editor.apply();
    }

    @Override
    public void updateGithub(String github) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.SHARED_PREFS_GITHUB), github);
        editor.apply();
    }

    @Override
    public void updatePhoto(String photo) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.SHARED_PREFS_PHOTO), photo);
        editor.apply();
    }

    @Override
    public void updateName(String name) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.SHARED_PREFS_NAME), name);
        editor.apply();
    }

    @Override
    public void updateSurname(String surname) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.SHARED_PREFS_SURNAME), surname);
        editor.apply();
    }

    @Override
    public void updateRemember(Boolean b) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String s = b ? "y" : "n";
        editor.putString(getString(R.string.SHARED_PREFS_CHECKED), s);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}