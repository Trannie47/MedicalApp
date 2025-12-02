package vn.edu.stu.medicalapp.Activity;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import vn.edu.stu.medicalapp.R;
import vn.edu.stu.medicalapp.utils.Constants;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.Locale;

public class AboutActivity extends Fragment implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private boolean isMapInitialized = false;
    private Switch swLangAbout;

    private String currentLang;
    private ImageButton btnCall,btnDiler;

    public AboutActivity() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadLocale();
        // Lấy Switch từ layout
        swLangAbout = view.findViewById(R.id.switchLangAbout);
        btnCall = view.findViewById(R.id.btn_call);
        btnDiler = view.findViewById(R.id.btn_dialer);

        // Load trạng thái ban đầu
        getBundle();

        // Gán sự kiện đổi ngôn ngữ
        addEvent();

        // --- Google Map ---
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map, mapFragment)
                    .commit();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (!isAdded()) return;

        mMap = googleMap;
        isMapInitialized = true;

        LatLng location = new LatLng(10.7380025, 106.6752572);
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title("Saigon Technology University"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMap = null;
        isMapInitialized = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isMapInitialized && mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    // Set trạng thái Switch khi load
    private void getBundle() {
        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        currentLang = prefs.getString(Constants.PREF_KEY_LANG, Locale.getDefault().getLanguage());
        swLangAbout.setChecked("vi".equals(currentLang));
    }

    // Gán sự kiện khi đổi Switch
    private void addEvent() {
        swLangAbout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                String newLang = b ? "vi" : "en";
                setLocale(newLang);

                SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
                prefs.edit().putString(Constants.PREF_KEY_LANG, newLang).apply();


                // Reload MainActivity
                if (getActivity() != null){
                    getActivity().recreate();
                }
//                getParentFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, new AboutActivity())
//                        .commit();
            }
//            View.OnClickListener( v->)
        });
        btnCall.setOnClickListener(v -> {
            String phoneNumber = "0123456789"; // thay bằng số muốn gọi
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // Yêu cầu cấp quyền CALL_PHONE nếu chưa có
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return;
            }
            startActivity(callIntent);
        });
        btnDiler.setOnClickListener(v -> {
            String phoneNumber = "0123456789"; // thay bằng số muốn gọi
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // Yêu cầu cấp quyền CALL_PHONE nếu chưa có
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return;
            }
            startActivity(callIntent);
        });
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
    private void loadLocale() {
        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        String language = prefs.getString(Constants.PREF_KEY_LANG, "en");
        setLocale(language);
    }
}
