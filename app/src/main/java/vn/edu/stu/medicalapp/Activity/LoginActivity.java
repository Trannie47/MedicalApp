package vn.edu.stu.medicalapp.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.stu.medicalapp.R;
import vn.edu.stu.medicalapp.utils.Constants;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button login;
    Switch swLang;
    String currentLang;


    String User = "admin",Pass="123";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        loadLocale();
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loadLocale();
        getBunde();
        AddEvent();

    }
    private void AddEvent() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals(User)){
                    if (password.getText().toString().equals(Pass)){
                        int a = 5;
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    else {
                        password.setText("");

                    }
                }
            }
        });
        swLang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                String newLang = b ? "vi" : "en";
                setLocale(newLang);
                SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
                prefs.edit().putString(Constants.PREF_KEY_LANG, newLang).apply();
                recreate();

            }
        });
    }
    private void getBunde() {
        username= findViewById(R.id.username_input);
        password=  findViewById(R.id.password_input);
        login= findViewById(R.id.login_btn);
        swLang= findViewById(R.id.switchLang);
        currentLang = Locale.getDefault().getLanguage();
        swLang.setChecked("vi".equals(currentLang));
    }
    private void loadLocale() {
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        String language = prefs.getString(Constants.PREF_KEY_LANG, "en");
        setLocale(language);
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

}