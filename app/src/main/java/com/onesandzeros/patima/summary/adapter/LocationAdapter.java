package com.onesandzeros.patima.summary.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.onesandzeros.patima.R;
import com.onesandzeros.patima.core.utils.UrlUtils;
import com.onesandzeros.patima.prediction.activity.ViewComparisonActivity;
import com.onesandzeros.patima.summary.model.NearbyPredictions;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private final List<NearbyPredictions> locationList;

    public LocationAdapter(List<NearbyPredictions> locationList) {
        this.locationList = locationList;
    }

    @NonNull
    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_image, parent, false);
        return new LocationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, int position) {
        NearbyPredictions nearbyPrediction = locationList.get(position);
        String picturePath = nearbyPrediction.getPredicted_image_path();
        String fullPicturePath = UrlUtils.getFullUrl(picturePath);
        Uri fullPictureUri = Uri.parse(fullPicturePath);
        holder.objImg.setImageURI(fullPictureUri);

//        Picasso.get()
//                .load(fullPicturePath)
//                .placeholder(R.drawable.placeholder_profile)
//                .into(holder.objImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewComparisonActivity.class);
                intent.putExtra("nearbyPrediction", nearbyPrediction);
                intent.putExtra("isFeedback", false);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView objImg;

        SimpleDraweeView objImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            objImg = itemView.findViewById(R.id.obj_img);
        }
    }
}
