package com.lineup.mild.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lineup.mild.R;
import com.lineup.mild.activity.Preview;
import com.lineup.mild.model.Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Model> models = new ArrayList<>();
    private final Context context;
    private final String display;

    public Adapter(Context context, String display) {
        this.context = context;
        this.display = display;
    }

    public void setData(List<Model> models) {
        this.models.clear();
        this.models = models;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (display.equals("recent")) {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.image_card, parent, false);
        } else if (display.equals("random")) {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.image_card_random, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model model = models.get(position);

        Picasso.get()
                .load(model.getThumbnail_url())
                .into(holder.imageView);

        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Preview.class);
            intent.putExtra("imgId", model.getId());
            intent.putExtra("txtCredit", model.getCredit());
            intent.putExtra("imgCreditWebsite", model.getCredit_website());
            intent.putExtra("imgDimension", model.getDimensions());
            intent.putExtra("imgOriginalUrl", model.getOriginal_url());
            intent.putExtra("imgPreviewUrl", model.getPreview_url());
            intent.putExtra("imgThumbnailUrl", model.getThumbnail_url());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
        }

        public float px2dp(float px) {
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            float dp = px / (metrics.densityDpi / 160f);
            return Math.round(dp);
        }
    }
}
