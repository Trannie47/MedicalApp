package vn.edu.stu.medicalapp.Class;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable{
    private String id;
    private String name;

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category (String id) {
        this.id = id;
    }
    public Category () {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(name);
    }

    protected Category(Parcel in) {
        id = in.readString();
        name = in.readString();
    }


    public int describeContents() {
        return 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
