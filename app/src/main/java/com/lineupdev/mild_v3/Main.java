package com.lineupdev.mild_v3;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lineupdev.mild_v3.API.ApiService;
import com.lineupdev.mild_v3.API.BaseApi;
import com.lineupdev.mild_v3.Adapter.Adapter;
import com.lineupdev.mild_v3.Model.Model;
import com.lineupdev.mild_v3.Util.GridSpacingItemDecoration;
import com.lineupdev.mild_v3.Util.Utils;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.randomList)
    RecyclerView randomList;
    @BindView(R.id.recentList)
    RecyclerView recentList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.txtRandom)
    TextView txtRandom;
    @BindView(R.id.txtRecent)
    TextView txtRecent;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.titleToolbar)
    TextView titleToolbar;
    @BindView(R.id.randomProgress)
    ProgressBar randomProgress;
    @BindView(R.id.recentProgress)
    ProgressBar recentProgress;

    private Adapter adapter;

    private AccountHeader headerResult = null;
    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Typeface fontToolbar = Typeface.createFromAsset(getAssets(), "fonts/Chapaza.ttf");
        Typeface fontRandom_Recent = Typeface.createFromAsset(getAssets(), "fonts/NunitoSans-Light.ttf");
        titleToolbar.setTypeface(fontToolbar);
        txtRandom.setTypeface(fontRandom_Recent);
        txtRecent.setTypeface(fontRandom_Recent);

        recentList.setNestedScrollingEnabled(false);
        randomList.setNestedScrollingEnabled(false);
        randomList.addItemDecoration(new GridSpacingItemDecoration(2, 32, true));
        recentList.addItemDecoration(new GridSpacingItemDecoration(2, 32, true));

        swipeRefreshLayout.setOnRefreshListener(this);

        Utils.isStoragePermissionGranted(this);

        // NavigationDrawer
        // _________________________________________________________________________________________
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSavedInstance(savedInstanceState)
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(0).withName("About").withIcon(GoogleMaterial.Icon.gmd_info).withSelectable(false),
                        new PrimaryDrawerItem().withIdentifier(1).withName("Share").withIcon(GoogleMaterial.Icon.gmd_share).withSelectable(false),
                        new PrimaryDrawerItem().withIdentifier(2).withName("Rate this App").withIcon(GoogleMaterial.Icon.gmd_star).withSelectable(false),
                        new PrimaryDrawerItem().withIdentifier(3).withName("More Apps").withIcon(GoogleMaterial.Icon.gmd_apps).withSelectable(false).withDescription("By LineUp Studio"),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withIdentifier(4).withName("Help").withIcon(GoogleMaterial.Icon.gmd_help).withSelectable(false),
                        new PrimaryDrawerItem().withIdentifier(5).withName("Changelog").withIcon(GoogleMaterial.Icon.gmd_history).withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        String itemNames = ((Nameable) drawerItem).getName().getText(Main.this);

                        switch (itemNames) {
                            case "About":

                                break;
                            case "Share":

                                break;
                            case "Rate this App":

                                break;
                            case "More Apps":

                                break;
                            case "Help":

                                break;
                            case "Changelog":

                                break;
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(false)
                .withSelectedItem(-1)
                .build();

        callData();
    }

    public void callData() {
        /*Random Image Load*/
        ApiService service = BaseApi.getRetrofit().create(ApiService.class);
        Call<List<Model>> call = service.getRandom();
        call.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                randomProgress.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

                adapter = new Adapter(Main.this, response.body());
                randomList.setHasFixedSize(true);
                randomList.setLayoutManager(new GridLayoutManager(Main.this, 2));

                randomList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {

            }
        });

        /*Recent Image Load*/
        ApiService recentService = BaseApi.getRetrofit().create(ApiService.class);
        final Call<List<Model>> recentCall = recentService.getRecent();
        recentCall.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                recentProgress.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

                adapter = new Adapter(Main.this, response.body());
                recentList.setHasFixedSize(true);
                recentList.setLayoutManager(new GridLayoutManager(Main.this, 2));

                recentList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onRefresh() {
        callData();
    }
}
