package com.lineup.mild.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lineup.mild.activity.Preview;
import com.lineup.mild.databinding.ImageCardSavedBinding;
import com.lineup.mild.model.DbModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.ViewHolder> {

    private Context context;
    private List<DbModel> dbModels;

    public SavedAdapter(Context context, List<DbModel> dbModels) {
        this.context = context;
        this.dbModels = dbModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ImageCardSavedBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DbModel dbModel = dbModels.get(position);

        Picasso.get()
                .load(dbModel.getThumbnail_url())
                .centerCrop()
                .fit()
                .into(holder.binding.imageView);

        holder.binding.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Preview.class);
            intent.putExtra("imgId", dbModel.getU_id());
            intent.putExtra("txtCredit", dbModel.getCredit());
            intent.putExtra("imgCreditWebsite", dbModel.getCredit_website());
            intent.putExtra("imgDimension", dbModel.getDimensions());
            intent.putExtra("imgOriginalUrl", dbModel.getOriginal_url());
            intent.putExtra("imgPreviewUrl", dbModel.getPreview_url());
            intent.putExtra("imgThumbnailUrl", dbModel.getThumbnail_url());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dbModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageCardSavedBinding binding;

        public ViewHolder(ImageCardSavedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
