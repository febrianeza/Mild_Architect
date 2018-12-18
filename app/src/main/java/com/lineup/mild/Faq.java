package com.lineup.mild;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.lineup.mild.Adapter.FaqAdapter;
import com.lineup.mild.Util.Font;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Faq extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;

    // FaqItems
    String[][] faqItems = {
            {"What is this app?", "Mild Architect is an android application that provides simple and elegant architectural photography, and is carefully selected to provide beautiful images to be used as mobile wallpapers. consists of images with 4K resolution of buildings, bridges, skyscrapers etc."},
            {"How is this app work?", "Just pick whatever image wallpaper that you like and on the bottom right corner you'll see a 'set as' button, and you tap it to set you device wallpaper with image that you pick in this app."},
            {"Can i just download the image?", "Yes you can, on the top right corner you'll see a download icon button, tap it and your image is downloading."},
            {"I like a picture, but I don't want to use it as wallpaper for now.", "Save it on favorite page, on the image preview on the top right corner you can see a 'heart' shaped icon, tap it an your image will be saved in favorite page so you can use it as wallpaper or download it in the future."}
    };

    @BindView(R.id.faqRecycler)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        toolbarTitle.setTypeface(new Font(this).nunitoSans());

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
