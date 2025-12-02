package vn.edu.stu.medicalapp.Class;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Drug implements Parcelable {
    private String id;
    private String name;
    private Category category;
    private byte[] image;
    private double price;
    private int quantity;

    public Drug(String id, String name, Category category, byte[] image, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    public Drug() {

    }



    public static final Creator<Drug> CREATOR = new Creator<Drug>() {
        @Override
        public Drug createFromParcel(Parcel in) {
            return new Drug(in);
        }

        @Override
        public Drug[] newArray(int size) {
            return new Drug[size];
        }
    };

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeParcelable(category, i);
        if (image != null) {
            parcel.writeInt(image.length);
            parcel.writeByteArray(image);
        } else {
            parcel.writeInt(0);
        }

        parcel.writeDouble(price);
        parcel.writeInt(quantity);
    }

    protected Drug(Parcel in) {
        id = in.readString();
        name = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
        int imageSize = in.readInt();
        if (imageSize > 0) {
            image = new byte[imageSize];
            in.readByteArray(image);
        }

        price = in.readDouble();
        quantity = in.readInt();
    }

}
