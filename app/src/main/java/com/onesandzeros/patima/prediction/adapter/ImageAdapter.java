package com.onesandzeros.patima.prediction.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onesandzeros.patima.R;
import com.onesandzeros.patima.summary.activity.SummaryActivity;
import com.onesandzeros.patima.core.utils.UrlUtils;
import com.onesandzeros.patima.prediction.model.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<Image> imageList;
    private Context context;

    public ImageAdapter(List<Image> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        Image image = imageList.get(position);

        String picturePath = image.getInputImagePath();

        if (picturePath != null) {
            String fullUrl = UrlUtils.getFullUrl(picturePath);
            Picasso.get()
                    .load(fullUrl)
                    .placeholder(R.drawable.placeholder_profile)
                    .into(holder.objImg);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SummaryActivity.class);
                intent.putExtra("imgId", image.getImageId());
                intent.putExtra("base_path", image.getInputImagePath());
                intent.putExtra("detection_path", image.getPredictedImagePath());
                intent.putExtra("timestamp", image.getCreatedAt());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView objImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            objImg = itemView.findViewById(R.id.obj_img);
        }
    }

}
