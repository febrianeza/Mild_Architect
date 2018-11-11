package com.lineupdev.mild_v3.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.lineupdev.mild_v3.Model.Model;
import com.lineupdev.mild_v3.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context context;
    private List<Model> models;

    public Adapter(Context context, List<Model> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.image_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model model = models.get(position);

//        final int thumb_height = (int) holder.px2dp(model.getThumbnailHeight());
//
//        Log.d("THUMB_HEIGHT", "height position " + position + " = " + thumb_height);

//        holder.imageView.getLayoutParams().height = thumb_height;

        Picasso.get()
                .load(model.getThumbnailUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public float px2dp(float px) {
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            float dp = px / (metrics.densityDpi / 160f);
            return Math.round(dp);
        }
    }
}
