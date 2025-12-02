package vn.edu.stu.medicalapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import vn.edu.stu.medicalapp.R;
import vn.edu.stu.medicalapp.Class.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList){
        this.context=context;
        this.categoryList=categoryList;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            boolean isDropdown = (viewGroup instanceof Spinner) ||
                    viewGroup.toString().contains("Dropdown") ||
                    viewGroup.toString().contains("Popup");
            LayoutInflater inflater=LayoutInflater.from(context);

            if (isDropdown) {
                // DROPLIST → DÙNG LAYOUT MÃ + TÊN
                view = inflater.inflate(R.layout.droplist_category, viewGroup, false);
                holder = new ViewHolder();
                holder.codeTextView = view.findViewById(R.id.tv_id_category);
                holder.nameTextView = view.findViewById(R.id.tv_name_category);
            } else {
                // LISTVIEW → DÙNG LAYOUT CŨ (chỉ tên)
                view = inflater.inflate(R.layout.item_category, viewGroup, false);
                holder = new ViewHolder();
                holder.codeTextView = view.findViewById(R.id.categoryCodeTextView);
                holder.nameTextView = view.findViewById(R.id.categoryNameTextView);
            }
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        Category category = categoryList.get(i);
        holder.nameTextView.setText(category.getName());
        if (holder.codeTextView != null) {
            holder.codeTextView.setText(category.getId());
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.droplist_category, parent, false);
            holder = new ViewHolder();
            holder.nameTextView = convertView.findViewById(R.id.tv_name_category);
            holder.codeTextView = convertView.findViewById(R.id.tv_id_category);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Category category = categoryList.get(position);
        holder.nameTextView.setText(category.getName());
        holder.codeTextView.setText(category.getId());
        return convertView;
    }
    private static class ViewHolder {
        TextView nameTextView;
        TextView codeTextView = null;
    }
}
