package com.lineupdev.mild_v3.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lineupdev.mild_v3.Model.DbModel;
import com.lineupdev.mild_v3.Preview;
import com.lineupdev.mild_v3.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DbModel dbModel = dbModels.get(position);

        Picasso.get()
                .load(dbModel.getThumbnail_url())
                .centerCrop()
                .fit()
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Preview.class);
                intent.putExtra("imgId", dbModel.getU_id());
                intent.putExtra("imgTitle", dbModel.getTitle());
                intent.putExtra("txtCredit", dbModel.getCredit());
                intent.putExtra("imgCreditWebsite", dbModel.getCredit_website());
                intent.putExtra("imgDimension", dbModel.getDimensions());
                intent.putExtra("imgOriginalUrl", dbModel.getOriginal_url());
                intent.putExtra("imgPreviewUrl", dbModel.getPreview_url());
                intent.putExtra("imgThumbnailUrl", dbModel.getThumbnail_url());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dbModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
