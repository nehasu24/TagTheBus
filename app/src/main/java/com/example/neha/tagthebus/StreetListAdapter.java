package com.example.neha.tagthebus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by NEHA on 5/6/2017.
 */

//Street list adapter to bind street details  to a list
public class StreetListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Streets> streetsList;

    public StreetListAdapter(Context context, int layout, ArrayList<Streets> streetsList) {
        this.context = context;
        this.layout = layout;
        this.streetsList = streetsList;
    }

    @Override
    public int getCount() {
        return streetsList.size();
    }

    @Override
    public Object getItem(int position) {
        return streetsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtName = (TextView) row.findViewById(R.id.txtName);
            holder.txttitle = (TextView) row.findViewById(R.id.txttitle);
            holder.imageView = (ImageView) row.findViewById(R.id.imgstreet);
            holder.txttime = (TextView) row.findViewById(R.id.txttime);
            holder.txtuser = (TextView) row.findViewById(R.id.txtuser);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Streets st = streetsList.get(position);
        holder.txtName.setText(st.getName());
        holder.txttitle.setText(st.getTitle());
        byte[] StreetImage = st.getImage();
        holder.txttime.setText(st.getTime());
        holder.txtuser.setText(st.getUser());
        Bitmap bitmap = BitmapFactory.decodeByteArray(StreetImage, 0, StreetImage.length);
        holder.imageView.setImageBitmap(bitmap);
        return row;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtName, txttitle, txttime, txtuser;
    }
}