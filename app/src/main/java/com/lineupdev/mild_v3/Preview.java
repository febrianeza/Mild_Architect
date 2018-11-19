package com.lineupdev.mild_v3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lineupdev.mild_v3.API.ApiService;
import com.lineupdev.mild_v3.API.BaseApi;
import com.lineupdev.mild_v3.Model.ModelPreview;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Preview extends AppCompatActivity {

    @BindView(R.id.imagePreview)
    ImageView imagePreview;
    @BindView(R.id.imgCredit)
    TextView imgCredit;
    @BindView(R.id.toolbarText)
    TextView toolbarText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.error_btn_retry)
    Button error_btn_retry;

    private String imageTitle = null;
    private String imageCredit = null;
    private String imageCreditWebsite = null;
    private String imageDimension = null;
    private String imageOriginalUrl = null;
    private String imagePreviewUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        imageTitle = getIntent().getStringExtra("imgTitle");
        imageCredit = getIntent().getStringExtra("imgCredit");
        imageCreditWebsite = getIntent().getStringExtra("imgCreditWebsite");
        imageDimension = getIntent().getStringExtra("imgDimension");
        imageOriginalUrl = getIntent().getStringExtra("imgOriginalUrl");
        imagePreviewUrl = getIntent().getStringExtra("imgPreviewUrl");

        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/NunitoSans-Light.ttf");
        toolbarText.setTypeface(typeface);

        imagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageOriginalUrl != null) {
                    Intent intent = new Intent(Preview.this, PreviewOriginal.class);
                    intent.putExtra("imageOriginalUrl", imageOriginalUrl);
                    startActivity(intent);
                }
            }
        });

        error_btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Picasso.get().load(imagePreviewUrl).into(imagePreview, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        toolbarText.setText(imageTitle);
        imgCredit.setText(imageCredit);

        if(imageCreditWebsite != null) {
            imgCredit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToWeb = new Intent(Intent.ACTION_VIEW);
                    goToWeb.setData(Uri.parse(imageCreditWebsite));
                    startActivity(goToWeb);
                }
            });
        }
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
