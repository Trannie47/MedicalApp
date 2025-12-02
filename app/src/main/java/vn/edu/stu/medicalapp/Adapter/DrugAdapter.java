package vn.edu.stu.medicalapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import vn.edu.stu.medicalapp.R;
import vn.edu.stu.medicalapp.Class.Drug;
import java.util.List;

public class DrugAdapter extends ArrayAdapter<Drug> {
    private Context context;
    private int resource;
    private List<Drug> drugs;
    private  TextView nameTextView, categoryTextView, priceTextView,quantityTextView ;
    private ImageView drugImageView;
    // Constructor receiving context, resource, and drugs
    public DrugAdapter(Context context, int resource, List<Drug> drugs) {
        super(context, resource, drugs);
        this.context = context;
        this.resource = resource;
        this.drugs = drugs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Drug drug = getItem(position); // Get the Drug object at current position

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

         nameTextView = convertView.findViewById(R.id.drugNameTextView);
         categoryTextView = convertView.findViewById(R.id.drugCategoryValueView);
         priceTextView=convertView.findViewById(R.id.drugPriceValueView);
         quantityTextView=convertView.findViewById(R.id.drugQuantityValueView);
         drugImageView = (ImageView) convertView.findViewById(R.id.drugImageViews);

        nameTextView.setText(drug.getName());
        categoryTextView.setText(drug.getCategory().getName());
        priceTextView.setText(String.valueOf(drug.getPrice()));
        quantityTextView.setText(String.valueOf(drug.getQuantity()));

        byte[] imageBytes = drug.getImage();

        Glide.with(context)
                .load(drug.getImage())
                .placeholder(R.drawable.ic_no_image)
                .error(R.drawable.ic_no_image)
                .override(200, 200)
                .into(drugImageView);

        return convertView;
    }
}
