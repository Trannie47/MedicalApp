package vn.edu.stu.medicalapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import vn.edu.stu.medicalapp.Adapter.DrugAdapter;
import vn.edu.stu.medicalapp.Class.Category;
import vn.edu.stu.medicalapp.Class.Drug;
import vn.edu.stu.medicalapp.Data.DatabaseHelper;
import vn.edu.stu.medicalapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Fragment {

    private BottomNavigationView bottomNavigationView;

    public HomeActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Vẫn dùng layout cũ: activity_home.xml
        return inflater.inflate(R.layout.activity_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Edge-to-edge (vẫn hoạt động trong Fragment)
        View mainView = view.findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

    }

    // Tự động reload khi quay lại Fragment (nếu cần)
    @Override
    public void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }
}