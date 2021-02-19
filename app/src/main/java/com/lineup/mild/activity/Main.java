package com.lineup.mild.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lineup.mild.R;
import com.lineup.mild.adapter.Adapter;
import com.lineup.mild.databinding.ActivityMainBinding;
import com.lineup.mild.viewmodel.MainViewModel;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

public class Main extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    private Adapter randomRecyclerAdapter;
    private Adapter recentRecyclerAdapter;

    private static final String PLAYLINK = "https://play.google.com/store/apps/details?id=";
    private static final String LINEUPSTUDIO_LINK = "https://play.google.com/store/apps/developer?id=LineUp+Studio";

    private boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        binding.recentList.setNestedScrollingEnabled(false);
        binding.randomList.setNestedScrollingEnabled(false);

        binding.swipeRefresh.setOnRefreshListener(this);

        randomRecyclerAdapter = new Adapter(this, "random");
        binding.randomList.setHasFixedSize(true);
        binding.randomList.setLayoutManager(new GridLayoutManager(Main.this, 2));
        binding.randomList.setAdapter(randomRecyclerAdapter);

        recentRecyclerAdapter = new Adapter(this, "recent");
        binding.recentList.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        binding.recentList.setAdapter(recentRecyclerAdapter);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // NavigationDrawer
        // _________________________________________________________________________________________
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSavedInstance(savedInstanceState)
                .build();

        new DrawerBuilder()
                .withActivity(this)
                .withToolbar(binding.toolbar)
                .withHasStableIds(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Saved").withIcon(GoogleMaterial.Icon.gmd_bookmark_border).withSelectable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("About").withIcon(GoogleMaterial.Icon.gmd_info).withSelectable(false),
                        new PrimaryDrawerItem().withName("Share").withIcon(GoogleMaterial.Icon.gmd_share).withSelectable(false),
                        new PrimaryDrawerItem().withName("Rate this App").withIcon(GoogleMaterial.Icon.gmd_star).withSelectable(false),
                        new PrimaryDrawerItem().withName("More Apps").withIcon(GoogleMaterial.Icon.gmd_apps).withSelectable(false).withDescription("By LineUp Studio"),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Information").withIcon(GoogleMaterial.Icon.gmd_info_outline).withSelectable(false)
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    String itemNames = ((Nameable) drawerItem).getName().getText(Main.this);

                    switch (itemNames) {
                        case "Saved":
                            Intent savedIntent = new Intent(Main.this, Saved.class);
                            startActivity(savedIntent);
                            break;
                        case "About":
                            Intent aboutIntent = new Intent(Main.this, About.class);
                            startActivity(aboutIntent);
                            break;
                        case "Share":
                            ShareApp();
                            break;
                        case "Rate this App":
                            RateThisApp();
                            break;
                        case "More Apps":
                            Uri uri = Uri.parse(LINEUPSTUDIO_LINK);
                            Intent toPlay = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(toPlay);
                            break;
                        case "Information":
                            Intent informationIntent = new Intent(Main.this, Information.class);
                            startActivity(informationIntent);
                            break;
                    }

                    return false;
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(false)
                .withSelectedItem(-1)
                .build();

        initLiveData();
    }

    private void initLiveData() {
        viewModel.randomList.observe(this, randomList -> {
            if (!randomList.isEmpty()) {
                binding.randomProgress.setVisibility(View.GONE);
                binding.randomError.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);

                randomRecyclerAdapter.setData(randomList);
            } else {
                binding.randomProgress.setVisibility(View.GONE);
                binding.randomError.setVisibility(View.VISIBLE);
                binding.swipeRefresh.setRefreshing(false);
            }
        });

        viewModel.recentList.observe(this, recentList -> {
            if (!recentList.isEmpty()) {
                binding.recentProgress.setVisibility(View.GONE);
                binding.recentError.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);

                recentRecyclerAdapter.setData(recentList);
            } else {
                binding.recentProgress.setVisibility(View.GONE);
                binding.recentError.setVisibility(View.VISIBLE);
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        viewModel.refresh();
    }

    private void ShareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_sub));
        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_short_desc) + "\n\n" + PLAYLINK + getPackageName());
        startActivity(Intent.createChooser(intent, "Share using"));
    }

    private void RateThisApp() {
        try {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent toPlay = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(toPlay);
        } catch (ActivityNotFoundException e) {
            Uri uri = Uri.parse(PLAYLINK + getPackageName());
            Intent toPlay = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(toPlay);
        }
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            Intent intent = new Intent(Main.this, SplashScreen.class);
            intent.putExtra("exit", "exit");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press Back Again to Exit", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(() -> exit = false, 3 * 1000);
        }
    }
}