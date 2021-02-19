package com.lineup.mild.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.lineup.mild.R;
import com.lineup.mild.databinding.ActivityAboutBinding;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAboutBinding binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.mWebsite.setOnClickListener(v -> {
            Intent toWebsite = new Intent(Intent.ACTION_VIEW);
            toWebsite.setData(Uri.parse(getResources().getString(R.string.developer_website)));
            startActivity(toWebsite);
        });

        binding.mPaypal.setOnClickListener(v -> {
            Intent toWebsite = new Intent(Intent.ACTION_VIEW);
            toWebsite.setData(Uri.parse(getResources().getString(R.string.my_paypal_https)));
            startActivity(toWebsite);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
