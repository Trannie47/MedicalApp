package vn.edu.stu.medicalapp.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import vn.edu.stu.medicalapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Integer selectedNavItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Edge to edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        selectedNavItem =-1;
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Mở HomeFragment đầu tiên
        replaceFragment(new CategoryListActivity());
        bottomNavigationView.setSelectedItemId(R.id.nav_categories); // Highlight đúng
        //Fix lỗi nav
        ViewCompat.setOnApplyWindowInsetsListener(bottomNavigationView, (v, insets) -> {
            Insets navBarInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars());

            v.setPadding(0, 0, 0, 0);

            return insets;
        });

        if (savedInstanceState != null) {
            selectedNavItem = savedInstanceState.getInt("selectedNavItem", R.id.nav_home);
            replaceFragment(new AboutActivity());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            selectedNavItem = id;

            if (id == R.id.nav_home) {
                replaceFragment(new HomeActivity());
            } else if (id == R.id.nav_drug) {
                replaceFragment(new DrugListActivity());
            } else if (id == R.id.nav_categories) {
                replaceFragment(new CategoryListActivity());
            } else if (id == R.id.nav_about) {
                replaceFragment(new AboutActivity());
            } else if (id == R.id.nav_exit) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }


            return true;
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedNavItem", selectedNavItem);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}