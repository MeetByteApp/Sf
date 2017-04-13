package com.example.android.sf;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import static android.R.attr.resource;
import static android.R.attr.x;

/**
 * Created by Indravasan on 13-Apr-17.
 */

public class BedsAdapter extends ArrayAdapter<Beds> {
    public BedsAdapter(@NonNull Context context, @NonNull List<Beds> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_view, parent, false);
        }

        Beds currentBed = getItem(position);

        TextView textView = (TextView) listItemView.findViewById(R.id.item_name);
        textView.setText(currentBed.getModel());

        TextView textViewSize = (TextView) listItemView.findViewById(R.id.item_size);
        textViewSize.setText(currentBed.getLength() + "x" + currentBed.getWidth() + "x" + currentBed.getThickness());

        return listItemView;
    }
}
