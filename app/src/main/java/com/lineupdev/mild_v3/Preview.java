package com.lineupdev.mild_v3;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

    private String imgId = null;

    ProgressDialog progressDialog;

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

        imgId = getIntent().getStringExtra("imgId");

        progressDialog = new ProgressDialog(Preview.this);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/NunitoSans-Light.ttf");
        toolbarText.setTypeface(typeface);

        if (imgId != null) {
            progressDialog.setMessage("Loading");
            progressDialog.show();
            getData();
        }
    }

    private void getData() {
        ApiService apiService = BaseApi.getRetrofit().create(ApiService.class);
        Call<ModelPreview> previewCall = apiService.getContentFromId(imgId);

        previewCall.enqueue(new Callback<ModelPreview>() {
            @Override
            public void onResponse(Call<ModelPreview> call, Response<ModelPreview> response) {
                //Toast.makeText(Preview.this, "Credit : " + response.body().getCredit(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Picasso.get().load(response.body().getPreview_url()).into(imagePreview);
                imgCredit.setText(response.body().getCredit());
                toolbarText.setText(response.body().getTitle());
            }

            @Override
            public void onFailure(Call<ModelPreview> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Preview.this, "Something wrong..", Toast.LENGTH_SHORT).show();
            }
        });
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
