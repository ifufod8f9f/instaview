package com.codepath.rpeterson.instaview;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by rohen on 2/7/15.
 */
public class PhotoCardArrayAdapter extends ArrayAdapter<PhotoCard> {
    NumberFormat numberFormatter = NumberFormat.getNumberInstance(Locale.US);

    private static class ViewHolder {
        TextView tvCaption;
        TextView tvTime;
        TextView tvLikes;
        TextView tvUsername;
        TextView tvCommentOne;
        TextView tvCommentTwo;
        ImageView ivPhoto;
        ImageView ivUserProfile;
    }

    public PhotoCardArrayAdapter(Context context, ArrayList<PhotoCard> photoCards) {
        super(context, android.R.layout.simple_list_item_1, photoCards);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PhotoCard photoCard = getItem(position);
        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo_card, parent,
                    false);
            viewHolder = new ViewHolder();
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.ivUserProfile = (ImageView) convertView.findViewById(R.id.ivUserProfile);
            viewHolder.tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvCommentOne = (TextView) convertView.findViewById(R.id.tvCommentOne);
            viewHolder.tvCommentTwo = (TextView) convertView.findViewById(R.id.tvCommentTwo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String commentOneName = null;
        String commentTwoName = null;
        String commentOneContent = null;
        String commentTwoContent = null;

        try {
            JSONObject lastComment = (JSONObject) photoCard.comments.get(photoCard.comments.length() - 1);
            JSONObject lastCommentFrom = lastComment.getJSONObject("from");

            JSONObject secondToLastComment = (JSONObject) photoCard.comments.get(photoCard.comments.length() - 2);
            JSONObject secondToLastCommentFrom = secondToLastComment.getJSONObject("from");
            commentOneName = lastCommentFrom.getString("username");
            commentTwoName = secondToLastCommentFrom.getString("username");
            commentOneContent = lastComment.getString("text");
            commentTwoContent = secondToLastComment.getString("text");
        } catch (JSONException ex) {
        }

        viewHolder.tvCaption.setText(photoCard.caption);
        viewHolder.ivPhoto.setImageResource(0);
        viewHolder.tvTime.setText(photoCard.timeString);
        viewHolder.ivUserProfile.setImageResource(0);
        viewHolder.tvLikes.setText("\u2665 " + numberFormatter.format(photoCard.likeCount) + " likes");
        viewHolder.tvUsername.setText(photoCard.username);

        if (commentOneName != null && commentTwoName != null && commentOneContent != null && commentTwoContent != null) {
            String htmlOne = "<font color='#0E5078'>" + commentOneName + "</font>" + " " + commentOneContent;
            String htmlTwo = "<font color='#0E5078'>" + commentTwoName + "</font>" + " " + commentTwoContent;
            viewHolder.tvCommentOne.setText(Html.fromHtml(htmlOne));
            viewHolder.tvCommentTwo.setText(Html.fromHtml(htmlTwo));
        }

        Picasso.with(getContext()).load(photoCard.imageUrl).placeholder(R.drawable.load_photo).into(viewHolder.ivPhoto);
        Picasso.with(getContext()).load(photoCard.userProfileImageUrl).into(viewHolder.ivUserProfile);

        // Return the completed view to render on screen
        return convertView;
    }
}
