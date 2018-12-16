package com.lineupdev.mild_v3;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewOriginal extends AppCompatActivity {

    @BindView(R.id.imageView)
    ZoomageView imageView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    static final String DIRPATH = Environment.DIRECTORY_PICTURES + "/Mild_Architecture/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_original);
        ButterKnife.bind(this);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        String imageOriginalUrl = getIntent().getStringExtra("imageOriginalUrl");
        String imageFileName = getIntent().getStringExtra("imageFileName");

        File path = Environment.getExternalStoragePublicDirectory(DIRPATH);

        File file = new File(path, imageFileName).getAbsoluteFile();
        if (!path.exists())
            path.mkdirs();

        if (file.exists()) {
            try {
                Picasso.get().load(file).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Picasso.get().load(imageOriginalUrl).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }
}
