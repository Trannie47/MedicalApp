package vn.edu.stu.medicalapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import vn.edu.stu.medicalapp.Class.Category;
import vn.edu.stu.medicalapp.Class.Drug;
import vn.edu.stu.medicalapp.utils.Constants;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper   extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "drugstore.db";
    private static final int DATABASE_VERSION = 1;

    // Bảng user
    private static final String TABLE_USERS = "user";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_USERNAME = "username";
    private static final String COLUMN_USER_PASSWORD = "password";

    // Bảng thuốc
    private static final String TABLE_DRUGS = "drugs";
    private static final String COLUMN_DRUG_ID = "id";
    private static final String COLUMN_DRUG_NAME = "name";
    private static final String COLUMN_DRUG_CATEGORY = "category";
    private static final String COLUMN_DRUG_CATEGORY_ID = "category.id";
    private static final String COLUMN_DRUG_IMAGE = "image";
    private static final String COLUMN_DRUG_PRICE = "price";
    private static final String COLUMN_DRUG_QUANTITY = "quantity";

    // Bảng loại
    private static final String TABLE_CATEGORY = "categories";
    private static final String COLUMN_CATEGORY_ID = "id";
    private static final String COLUMN_CATEGORY_NAME = "name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng user
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_USERNAME + " TEXT, "
                + COLUMN_USER_PASSWORD + " TEXT)";
        db.execSQL(createUserTable);
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USERNAME, "admin");
        values.put(COLUMN_USER_PASSWORD, "123");
        db.insert(TABLE_USERS, null, values);

        // Tạo bảng thuốc
        String createDrugTable = "CREATE TABLE " + TABLE_DRUGS + " ("
                + COLUMN_DRUG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DRUG_NAME + " TEXT, "
                + COLUMN_DRUG_CATEGORY + " TEXT, "
                + COLUMN_DRUG_IMAGE + " BLOB, "
                + COLUMN_DRUG_PRICE + " REAL, "
                + COLUMN_DRUG_QUANTITY + " INTEGER)";
        db.execSQL(createDrugTable);
        // Tạo bảng loại
//        String createCategoryTable = "CREATE TABLE " + TABLE_CATEGORY + " ("
//                + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + COLUMN_CATEGORY_NAME + " TEXT)";
        String createCategoryTable = "CREATE TABLE " + TABLE_CATEGORY + " ("
                + COLUMN_CATEGORY_ID + " TEXT PRIMARY KEY , "
                + COLUMN_CATEGORY_NAME + " TEXT)";
        db.execSQL(createCategoryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Phương thức kiểm tra thông tin đăng nhập
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE "
                        + COLUMN_USER_USERNAME + "=? AND "
                        + COLUMN_USER_PASSWORD + "=?",
                new String[]{username, password});
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }
    public long insertDrug(String name, String category, byte[] image, double price, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("category", category);
        values.put("image", image); // Kiểu BLOB cho ảnh
        values.put("price", price);
        values.put("quantity", quantity);

        // Trả về giá trị ID của dòng mới được thêm, hoặc -1 nếu thêm thất bại
        return db.insert(TABLE_DRUGS, null, values);
    }



    public int updateDrug(String id, String name, String category, byte[] image, double price, int quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DRUG_NAME, name);
        values.put(COLUMN_DRUG_CATEGORY, category);
        values.put(COLUMN_DRUG_IMAGE, image);
        values.put(COLUMN_DRUG_PRICE, price);
        values.put(COLUMN_DRUG_QUANTITY, quantity);
        return db.update(TABLE_DRUGS, values, COLUMN_DRUG_ID + "=?", new String[]{String.valueOf(id)});
    }

    public List<Drug>  getAllDrug(){
        List<Drug> drugList=new ArrayList<>();
        List<Category> categories = getAllCategories();
        Category category;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_DRUGS, null);
        if (cursor.moveToFirst()){
           do {
               Drug drug=new Drug();
               drug.setId(cursor.getString(0));
               drug.setName(cursor.getString(1));
               category = categories.stream()
                       .filter(c -> c.getId().equals(cursor.getString(2)))
                       .findFirst()
                       .orElse(null);
               drug.setCategory(category);

               drug.setImage(cursor.getBlob(3));
               drug.setPrice(cursor.getDouble(4));
               drug.setQuantity(cursor.getInt(5));
               drugList.add(drug);
           }while (cursor.moveToNext());

        }
        cursor.close();
        return drugList;
    }

    public  void  deleteDrug(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_DRUGS, COLUMN_DRUG_ID+"=?", new String[]{String.valueOf(id)});
    }
    public long insertCategory(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COLUMN_CATEGORY_NAME,name);
        return db.insert(TABLE_CATEGORY, null, values);
    }

    public long insertCategoryNew(String code,String name){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COLUMN_CATEGORY_ID,code);
        values.put(COLUMN_CATEGORY_NAME,name);
        return db.insert(TABLE_CATEGORY, null, values);
    }

    public List<Category> getAllCategories(){
        List<Category> categoryList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(" SELECT * FROM "+TABLE_CATEGORY,null);
        if (cursor.moveToFirst()){
            do {
                Category category=new Category();
                category.setId(cursor.getString(0));
                category.setName(cursor.getString(1));
                categoryList.add(category);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return categoryList;
    }
    public int updateCategory(String id, String name){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);
        return db.update(TABLE_CATEGORY,values,COLUMN_CATEGORY_ID+"=?", new String[]{String.valueOf(id)});
    }
    public void deleteCategory(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, COLUMN_CATEGORY_ID+"=?", new String[]{String.valueOf(id)});
        db.delete(TABLE_DRUGS, COLUMN_DRUG_CATEGORY+"=?", new String[]{String.valueOf(id)});

    }


}



