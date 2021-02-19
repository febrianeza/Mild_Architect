package com.lineup.mild.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.lineup.mild.adapter.SavedAdapter;
import com.lineup.mild.database.DatabaseHelper;
import com.lineup.mild.databinding.ActivitySavedBinding;
import com.lineup.mild.model.DbModel;
import com.lineup.mild.util.Font;
import com.lineup.mild.util.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class Saved extends AppCompatActivity {

    SavedAdapter savedAdapter;
    List<DbModel> dbModels = new ArrayList<>();
    DatabaseHelper db;

    private ActivitySavedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        binding.emptyText.setTypeface(new Font(this).nunitoSans());
        binding.toolbarText.setTypeface(new Font(this).nunitoSans());

        db = new DatabaseHelper(this);

        dbModels.addAll(db.getAllFavoriteWallpaper());
        savedAdapter = new SavedAdapter(this, dbModels);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(Saved.this, 2));
        binding.recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 32, true));
        binding.recyclerView.setAdapter(savedAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        dbModels.clear();
        dbModels.addAll(db.getAllFavoriteWallpaper());
        savedAdapter.notifyDataSetChanged();
        toogleEmptyFav();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void toogleEmptyFav() {
        if (db.getFavoriteWallpaperCount() > 0) {
            binding.iconEmpty.setVisibility(View.GONE);
        } else {
            binding.iconEmpty.setVisibility(View.VISIBLE);
        }
    }
}
