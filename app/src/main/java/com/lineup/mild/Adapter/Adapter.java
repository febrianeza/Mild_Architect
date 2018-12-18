package com.lineup.mild.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lineup.mild.Model.Model;
import com.lineup.mild.Preview;
import com.lineup.mild.R;
import com.squareup.picasso.Picasso;

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
                .load(model.getThumbnail_url())
                .fit()
                .centerCrop()
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Preview.class);
                intent.putExtra("imgId", model.getId());
                intent.putExtra("imgTitle", model.getTitle());
                intent.putExtra("txtCredit", model.getCredit());
                intent.putExtra("imgCreditWebsite", model.getCredit_website());
                intent.putExtra("imgDimension", model.getDimensions());
                intent.putExtra("imgOriginalUrl", model.getOriginal_url());
                intent.putExtra("imgPreviewUrl", model.getPreview_url());
                intent.putExtra("imgThumbnailUrl", model.getThumbnail_url());
                context.startActivity(intent);
            }
        });
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
