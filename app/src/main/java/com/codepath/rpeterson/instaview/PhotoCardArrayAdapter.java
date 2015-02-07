package com.codepath.rpeterson.instaview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rohen on 2/7/15.
 */
public class PhotoCardArrayAdapter extends ArrayAdapter<PhotoCard> {

    public PhotoCardArrayAdapter(Context context, ArrayList<PhotoCard> photoCards) {
        super(context, android.R.layout.simple_list_item_1, photoCards);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PhotoCard photoCard = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo_card, parent,
                    false);
        }

        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);

        tvCaption.setText(photoCard.caption);

        //Clear image
        ivPhoto.setImageResource(0);

        Picasso.with(getContext()).load(photoCard.imageUrl).into(ivPhoto);

        // Return the completed view to render on screen
        return convertView;
    }
}
