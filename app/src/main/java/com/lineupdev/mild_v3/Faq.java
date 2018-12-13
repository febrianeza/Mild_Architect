package com.lineupdev.mild_v3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lineupdev.mild_v3.Adapter.FaqAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Faq extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // FaqItems
    String[][] faqItems = {
            {"How to download the wallpaper?", "To promote your app through ads, you can create a Google Ads Universal App Campaign or manage existing campaigns from your Play Console."},
            {"Why when i set as wallpaper, the picture doesnt seem like on the preview", "On the left menu, click User Acquisition >  Google Ads Campaigns. If you need to make changes to a campaign, click a campaign link in the table. You'll be linked to Google Ads to manage your campaign settings."},
            {"How can i share the wallpaper", "Before users can create a Google Ads campaign, the account owner needs to create the first campaign or link to an existing Google Ads account. For future campaigns, other users with the \"Create Google Ads campaigns\" permission can create campaigns."},
            {"Is there any spesific screen resolution needed to enjoy the wallpaper through this application?", "Khadafi"},
            {"are there any copyright on images in this application?", "Kanebos"},
            {"i see an image that i know, but there is no name of the creator, what should i do?", "kanebos 2"}
    };

    @BindView(R.id.faqRecycler)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new FaqAdapter(this, recyclerView, faqItems));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
