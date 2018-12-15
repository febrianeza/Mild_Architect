package com.lineupdev.mild_v3;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lineupdev.mild_v3.Util.Font;

import butterknife.BindView;
import butterknife.ButterKnife;

public class About extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mWebsite)
    TextView mWebsite;
    @BindView(R.id.toolbarText)
    TextView toolbarText;
    @BindView(R.id.mDesc)
    TextView mDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbarText.setTypeface(new Font(this).nunitoSans());
        mDesc.setTypeface(new Font(this).nunitoSans());
        mWebsite.setTypeface(new Font(this).nunitoSans());

        mWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toWebsite = new Intent(Intent.ACTION_VIEW);
                toWebsite.setData(Uri.parse(getResources().getString(R.string.developer_website)));
                startActivity(toWebsite);
            }
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
