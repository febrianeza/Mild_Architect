package com.lineup.mild;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.lineup.mild.API.ApiService;
import com.lineup.mild.API.BaseApi;
import com.lineup.mild.Adapter.Adapter;
import com.lineup.mild.Model.Model;
import com.lineup.mild.Util.Font;
import com.lineup.mild.Util.GridSpacingItemDecoration;
import com.lineup.mild.Util.MILD;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
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
    @BindView(R.id.randomError)
    TextView randomError;
    @BindView(R.id.recentError)
    TextView recentError;
    @BindView(R.id.adView)
    AdView adView;
    AdRequest adRequest;
    private Adapter adapter;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        titleToolbar.setTypeface(new Font(this).chapaza());
        txtRandom.setTypeface(new Font(this).nunitoSans());
        txtRecent.setTypeface(new Font(this).nunitoSans());

        recentList.setNestedScrollingEnabled(false);
        randomList.setNestedScrollingEnabled(false);
        recentList.addItemDecoration(new GridSpacingItemDecoration(2, 32, true));
        randomList.addItemDecoration(new GridSpacingItemDecoration(2, 32, true));

        swipeRefreshLayout.setOnRefreshListener(this);

        // NavigationDrawer
        // _________________________________________________________________________________________
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSavedInstance(savedInstanceState)
                .build();

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
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
                        new SecondaryDrawerItem().withName("Help").withIcon(GoogleMaterial.Icon.gmd_help).withSelectable(false),
                        new SecondaryDrawerItem().withName("Information").withIcon(GoogleMaterial.Icon.gmd_info_outline).withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
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
                                mFirebaseAnalytics.logEvent(MILD.FIREBASE_MORE_APPS, null);
                                Uri uri = Uri.parse(LINEUPSTUDIO_LINK);
                                Intent toPlay = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(toPlay);
                                break;
                            case "Help":
                                Intent toFaq = new Intent(Main.this, Faq.class);
                                startActivity(toFaq);
                                break;
                            case "Information":
                                Intent informationIntent = new Intent(Main.this, Information.class);
                                startActivity(informationIntent);
                                break;
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(false)
                .withSelectedItem(-1)
                .build();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        MobileAds.initialize(this, getResources().getString(R.string.banner_main_unit));
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

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
                randomError.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

                adapter = new Adapter(Main.this, response.body());
                randomList.setHasFixedSize(true);
                randomList.setLayoutManager(new GridLayoutManager(Main.this, 2));

                randomList.setAdapter(adapter);
                mFirebaseAnalytics.logEvent(MILD.FIREBASE_LOAD_RANDOM_SUCCESS, null);
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                randomProgress.setVisibility(View.GONE);
                randomError.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                mFirebaseAnalytics.logEvent(MILD.FIREBASE_LOAD_RANDOM_FAILED, null);
            }
        });

        /*Recent Image Load*/
        ApiService recentService = BaseApi.getRetrofit().create(ApiService.class);
        final Call<List<Model>> recentCall = recentService.getRecent();
        recentCall.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                recentProgress.setVisibility(View.GONE);
                recentError.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

                adapter = new Adapter(Main.this, response.body());
                recentList.setHasFixedSize(true);
                recentList.setLayoutManager(new GridLayoutManager(Main.this, 2));

                recentList.setAdapter(adapter);
                mFirebaseAnalytics.logEvent(MILD.FIREBASE_LOAD_RECENT_SUCCESS, null);
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                recentProgress.setVisibility(View.GONE);
                recentError.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                mFirebaseAnalytics.logEvent(MILD.FIREBASE_LOAD_RECENT_FAILED, null);
            }
        });
    }

    @Override
    public void onRefresh() {
        callData();
    }

    private static final String PLAYLINK = "https://play.google.com/store/apps/details?id=";
    private static final String LINEUPSTUDIO_LINK = "https://play.google.com/store/apps/developer?id=LineUp+Studio";

    private void ShareApp() {
        mFirebaseAnalytics.logEvent(MILD.FIREBASE_SHARE_APPS, null);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_sub));
        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_short_desc) + "\n\n" + PLAYLINK + getPackageName());
        startActivity(Intent.createChooser(intent, "Share using"));
    }

    private void RateThisApp() {
        mFirebaseAnalytics.logEvent(MILD.FIREBASE_RATE_APPS, null);
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

    private boolean exit = false;

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
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }
}
