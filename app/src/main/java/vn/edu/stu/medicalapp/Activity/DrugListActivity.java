package vn.edu.stu.medicalapp.Activity;

import static vn.edu.stu.medicalapp.Activity.DrugDetailActivity.PICK_IMAGE_REQUEST;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import vn.edu.stu.medicalapp.Adapter.DrugAdapter;
import vn.edu.stu.medicalapp.R;
import vn.edu.stu.medicalapp.Data.DatabaseHelper;
import vn.edu.stu.medicalapp.Class.Drug;

import java.util.ArrayList;
import java.util.List;

public class DrugListActivity extends Fragment {

    private DatabaseHelper dbHelper;
    private List<Drug> drugList = new ArrayList<>();
    private DrugAdapter drugAdapter;
    private ListView listView;
    private Button addButton;
    private ImageView drugImageView;

    public DrugListActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_drug_list, container, false);
        // Vẫn dùng activity_drug_list.xml → GIỮ NGUYÊN TÊN!
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Edge-to-edge
        View mainView = view.findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Khởi tạo
        dbHelper = new DatabaseHelper(requireContext());
        listView = view.findViewById(R.id.drugListView);
        addButton = view.findViewById(R.id.addButton);

        loadDrugList();
        setupEvents();
    }

    private void setupEvents() {
        // Click item → mở chi tiết
        listView.setOnItemClickListener((parent, v, position, id) -> {
            Drug selectedDrug = drugList.get(position);
            Intent intent = new Intent(requireContext(), DrugDetailActivity.class);
            intent.putExtra("drug", selectedDrug);
            startActivity(intent);
        });

        // Nút thêm thuốc
        addButton.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), DrugDetailActivity.class));
        });
    }

    private void loadDrugList() {
        drugList.clear();
        drugList.addAll(dbHelper.getAllDrug());
        drugAdapter = new DrugAdapter(requireContext(), R.layout.item_drug, drugList);
        listView.setAdapter(drugAdapter);
    }

    // Tự động reload khi quay lại từ DrugDetailActivity
    @Override
    public void onResume() {
        super.onResume();
        loadDrugList();
    }
}