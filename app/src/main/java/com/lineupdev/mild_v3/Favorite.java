package com.lineupdev.mild_v3;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.lineupdev.mild_v3.Adapter.SavedAdapter;
import com.lineupdev.mild_v3.Database.DatabaseHelper;
import com.lineupdev.mild_v3.Model.DbModel;
import com.lineupdev.mild_v3.Util.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Favorite extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.icon_empty)
    LinearLayout icon_empty;

    SavedAdapter savedAdapter;
    List<DbModel> dbModels = new ArrayList<>();
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        db = new DatabaseHelper(this);

        dbModels.addAll(db.getAllFavoriteWallpaper());
        savedAdapter = new SavedAdapter(this, dbModels);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(Favorite.this, 2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 32, true));
        recyclerView.setAdapter(savedAdapter);
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
            icon_empty.setVisibility(View.GONE);
        } else {
            icon_empty.setVisibility(View.VISIBLE);
        }
    }
}
