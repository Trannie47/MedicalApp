package vn.edu.stu.medicalapp.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.stu.medicalapp.Data.DatabaseHelper;
import vn.edu.stu.medicalapp.R;
import vn.edu.stu.medicalapp.Class.Category;

public class CategoryDetailActivity extends AppCompatActivity {
    private Category currentCategory;
    private DatabaseHelper dbHelper;
    private EditText edtCategoryName;
    private Button btnSave;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbHelper=new DatabaseHelper(this);
        edtCategoryName = findViewById(R.id.edtCategoryName);
        btnSave = findViewById(R.id.btnSaveCategory);
        eVent();
        getBunde();
    }
    private void eVent(){
        btnSave.setOnClickListener(view -> {
            String categoryName = edtCategoryName.getText().toString();
            // Lưu phân loại thốc vào cơ sở dữ liệu
            if (currentCategory == null || currentCategory.getId() == null ) {
                long result = dbHelper.insertCategory(categoryName);
                if (result != -1) {
                    Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to add category", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                // Cập nhật thuốc nếu drug đã tồn tại
                int result = dbHelper.updateCategory(currentCategory.getId(), categoryName);
                if (result > 0) {
                    Toast.makeText(this, "Category updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to update drug", Toast.LENGTH_SHORT).show();
                }
            }
            finish();
        });
    }
    private void getBunde(){
        Intent intent = getIntent();
        currentCategory = intent.getParcelableExtra("category");
        if (currentCategory != null) {
            edtCategoryName.setText(currentCategory.getName());
        } else {
            currentCategory = null; // Clearly indicate that drug will be null if no drug is passed
        }
    }
}