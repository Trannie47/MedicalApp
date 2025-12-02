package vn.edu.stu.medicalapp.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.stu.medicalapp.Adapter.CategoryAdapter;
import vn.edu.stu.medicalapp.Class.Category;
import vn.edu.stu.medicalapp.Class.Drug;
import vn.edu.stu.medicalapp.Data.DatabaseHelper;
import vn.edu.stu.medicalapp.R;
import com.google.android.material.appbar.MaterialToolbar;


import android.Manifest;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;


public class DrugDetailActivity extends AppCompatActivity {
    public static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100; // Bạn có thể thay đổi số này nếu cần

    private DatabaseHelper dbHelper;
    private EditText nameInput, categoryInput, priceInput, quantityInput;
    private Spinner categorySpinner;

    List<Category> Categories;
    Category category;
    CategoryAdapter adapter;
    private ImageView drugImage;
    private Uri imageUri;
    private Button saveBtn, deleteBtn,  chooseImageBtn;
    MaterialToolbar topAppBar;
    private Drug drug;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_drug_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            openImagePicker();
        }

        dbHelper=new DatabaseHelper(this);
        nameInput=findViewById(R.id.nameInput);
//        categoryInput=findViewById(R.id.categoryInput);
        categorySpinner= (Spinner) findViewById(R.id.categorySpinner);
        drugImage=findViewById(R.id.drugImageView);
        priceInput=findViewById(R.id.priceInput);
        quantityInput=findViewById(R.id.quantityInput);
        saveBtn=findViewById(R.id.saveBtn);
        deleteBtn=findViewById(R.id.deleteBtn);
        chooseImageBtn = findViewById(R.id.chooseImageBtn);
        topAppBar = findViewById(R.id.topAppBar);
        Categories = getArrayCategory();
        adapter = new CategoryAdapter(this, Categories);
        categorySpinner.setAdapter(adapter);
        getBunde();
        addEvent();
    }

    private List<Category> getArrayCategory() {
        return dbHelper.getAllCategories();
    }

    private void addEvent(){
        chooseImageBtn.setOnClickListener(view ->
                openImagePicker()
                );

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (Category) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        saveBtn.setOnClickListener(view -> {
            String name = nameInput.getText().toString();
            String categoryId = category.getId();
            double price = Double.parseDouble(priceInput.getText().toString());
            int quantity = Integer.parseInt(quantityInput.getText().toString());

            byte[] imageBytes = drug != null ? drug.getImage() : null; // Sử dụng mảng byte cho hình ảnh

            if (drug == null || drug.getId() == null) {
                long result = dbHelper.insertDrug(name, categoryId, imageBytes, price, quantity);
                if (result != -1) {
                    Toast.makeText(this, "Drug added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to add drug", Toast.LENGTH_SHORT).show();
                }
            } else {
                int result = dbHelper.updateDrug(drug.getId(), name, categoryId, imageBytes, price, quantity);
                if (result > 0) {
                    Toast.makeText(this, "Drug updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to update drug", Toast.LENGTH_SHORT).show();
                }
            }

            finish();
        });

        deleteBtn.setOnClickListener(view -> {
          if(drug!=null){
              dbHelper.deleteDrug(drug.getId());
              Toast.makeText(this, "Drug delete successfully", Toast.LENGTH_SHORT).show();
              finish();
          }
        });
        topAppBar.setNavigationOnClickListener(v -> {
            this.onBackPressed(); // hoặc dùng NavController nếu có
        });

    }
    //test
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*"); // Change to suit your file type, e.g. "*/*" for all files.
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false); // Allow multiple selections if needed.
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void getBunde() {
        Intent intent = getIntent();
        drug = intent.getParcelableExtra("drug");
        if (drug != null) {
            nameInput.setText(drug.getName());

            // Get the category name from the Category object
            if (drug.getCategory() != null) {
                category = drug.getCategory();
                int pos =-1;
                int index = 0 ;
                for (Category c : Categories) {
                    if (c.getId().equals(category.getId())) {
                        pos = index;
                        break;
                    }
                    index++;
                }

                categorySpinner.setSelection(pos);
                Toast.makeText(DrugDetailActivity.this, "Chọn 2 : " + category.getId(), Toast.LENGTH_SHORT).show();
            }

            priceInput.setText(String.valueOf(drug.getPrice()));
            quantityInput.setText(String.valueOf(drug.getQuantity()));

            if (drug.getImage() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(drug.getImage(), 0, drug.getImage().length);
                drugImage.setImageBitmap(bitmap);
            }
        } else {
            drug = null; // Clearly indicate that drug will be null if no drug is passed
            deleteBtn.setVisibility(View.GONE);  // ẩn không hiển thị
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {

                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                byte[] imageBytes = byteArrayOutputStream.toByteArray();


                if (drug == null) {
                    drug = new Drug(); // Initialize drug if it is null
                }

                // Set the image byte[] to the Drug object
                drug.setImage(imageBytes);

                // Update the ImageView to display the selected image
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                drugImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}