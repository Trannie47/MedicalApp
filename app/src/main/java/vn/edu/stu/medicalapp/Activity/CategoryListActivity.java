package vn.edu.stu.medicalapp.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import vn.edu.stu.medicalapp.Adapter.CategoryAdapter;
import vn.edu.stu.medicalapp.Data.DatabaseHelper;
import vn.edu.stu.medicalapp.R;
import vn.edu.stu.medicalapp.Class.Category;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class CategoryListActivity extends Fragment {

    private List<Category> categoryList = new ArrayList<>();
    private CategoryAdapter adapter;
    private DatabaseHelper dbhelper;
    private Button addCategoryButton;
    private ListView listView;
    private Category selectedCategory;
    private TextView selectedCategoryTextView;

    private TextInputLayout tilCategoryCode, tilCategoryName;
    private TextInputEditText edtCategoryCode, edtCategoryName;

    private Button saveCategoryButton,deleteCategoryButton;

    public CategoryListActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_category_list, container, false);
        // Vẫn dùng activity_category_list.xml → GIỮ NGUYÊN TÊN!
    }

    @SuppressLint("MissingInflatedId")
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

        // Ánh xạ view
        listView = view.findViewById(R.id.categoryListView);
        addCategoryButton = view.findViewById(R.id.btnAddCategory);
        selectedCategoryTextView = view.findViewById(R.id.selectedCategoryTextView);
        edtCategoryCode = view.findViewById(R.id.edtCategoryCode);
        edtCategoryName = view.findViewById(R.id.edtCategoryName);
        saveCategoryButton = view.findViewById(R.id.btnSaveCategory);
        deleteCategoryButton = view.findViewById(R.id.btnDeleteCategory);
        dbhelper = new DatabaseHelper(requireContext());

        // Nhận Category từ Bundle (thay vì Intent)
        if (getArguments() != null) {
            selectedCategory = getArguments().getParcelable("category");
            if (selectedCategory != null && selectedCategoryTextView != null) {
                selectedCategoryTextView.setText("Selected Category: " + selectedCategory.getName());
            }
        }

        // Load danh sách
        loadCategories();

        // Setup sự kiện
        setupEvents();
    }

    private void loadCategories() {
        categoryList.clear();
        categoryList.addAll(dbhelper.getAllCategories());
        adapter = new CategoryAdapter(requireContext(), categoryList);
        listView.setAdapter(adapter);
    }

    private void setupEvents() {
        // Click item → mở chi tiết
        listView.setOnItemClickListener((parent, v, position, id) -> {
            Category category = categoryList.get(position);
            edtCategoryCode.setText(category.getId());
            edtCategoryName.setText(category.getName());
        });

        // Long click → xóa
        listView.setOnItemLongClickListener((parent, v, position, id) -> {
            Category category = categoryList.get(position);
            dbhelper.deleteCategory(category.getId());

            new MaterialAlertDialogBuilder(requireContext()) // DÙNG requireContext() TRONG FRAGMENT
                    .setTitle("XÓA Loại Thuốc")
                    .setMessage("Bạn có chắc chắn muốn xóa loại thuốc \"" + category.getName() + "\" không?\n\nHành động này không thể hoàn tác!")
                    .setPositiveButton("XÓA NGAY", (dialog, which) -> {
                        // NGƯỜI DÙNG ĐỒNG Ý XÓA
                        dbhelper.deleteCategory(category.getId());
                        Toast.makeText(requireContext(), "Đã xóa thuốc thành công!", Toast.LENGTH_SHORT).show();
                        categoryList.remove(position);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("HỦY", (dialog, which) -> {
                        Toast.makeText(requireContext(), "Đã hủy xóa", Toast.LENGTH_SHORT).show();
                    })
                    .setCancelable(false)
                    .show();
            return true;
        });

        saveCategoryButton.setOnClickListener(view -> {
            String code = edtCategoryCode.getText().toString().trim().toUpperCase();
            String name = edtCategoryName.getText().toString().trim();
            boolean isValid = true;
            if (code.isEmpty()) {
                tilCategoryCode.setError("Bắt buộc nhập mã danh mục!");
                edtCategoryCode.requestFocus();
                isValid = false;
            }

            if (name.isEmpty()) {
                tilCategoryName.setError("Bắt buộc nhập tên danh mục!");
                if (isValid) edtCategoryName.requestFocus();
                isValid = false;
            }
            if (isValid) {
                // Lưu phân loại thốc vào cơ sở dữ liệu
                Category temp = new Category(code, name);
                Category found = null;
                    for (Category cat : categoryList) {
                        if (cat.getId().equals(code)) {
                            found = cat;
                            break;
                        }
                    }

                    if ( found == null ) {
                        long result = dbhelper.insertCategoryNew(code, name);
                        if (result != -1) {
                            Toast.makeText(requireContext(), "Category added successfully", Toast.LENGTH_SHORT).show();
                            categoryList.add(temp);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(requireContext(), "Failed to add category", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        // Cập nhật thuốc nếu drug đã tồn tại
                        int result = dbhelper.updateCategory(code, name);
                        if (result > 0) {
                            categoryList.set(categoryList.indexOf(found),temp);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(requireContext(), "Category updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Failed to update drug", Toast.LENGTH_SHORT).show();
                        }
                    }
            }
//            finish();
        });


        // Nút thêm
        addCategoryButton.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), CategoryDetailActivity.class));
        });

        //nút xoá
        deleteCategoryButton.setOnClickListener(v -> {
            String code = edtCategoryCode.getText().toString().trim().toUpperCase();
            String name = edtCategoryName.getText().toString().trim();

            if (code.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập mã loại thuốc!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tìm category trong danh sách hiện có
            Category found = null;
            for (Category cat : categoryList) {
                if (cat.getId().equals(code)) {
                    found = cat;
                    break;
                }
            }

            if (found != null) {
                Category categoryDel = found;
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("XÓA Loại Thuốc")
                        .setMessage("Bạn có chắc chắn muốn xóa loại thuốc \"" + found.getName() + "\" không?\n\nHành động này không thể hoàn tác!")
                        .setPositiveButton("XÓA NGAY", (dialog, which) -> {
                            dbhelper.deleteCategory(categoryDel.getId());
                            categoryList.remove(categoryDel);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(requireContext(), "Đã xóa loại thuốc thành công!", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("HỦY", (dialog, which) -> {
                            Toast.makeText(requireContext(), "Đã hủy xóa", Toast.LENGTH_SHORT).show();
                        })
                        .setCancelable(false)
                        .show();
            } else {
                Toast.makeText(requireContext(), "Không tìm thấy loại thuốc có mã " + code, Toast.LENGTH_SHORT).show();
            }
        });


    }

    // Tự động cập nhật khi quay lại từ CategoryDetailActivity
    @Override
    public void onResume() {
        super.onResume();
        loadCategories();
        if (selectedCategory != null && selectedCategoryTextView != null) {
            selectedCategoryTextView.setText("Selected Category: " + selectedCategory.getName());
        }
    }
}