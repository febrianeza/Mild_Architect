package com.lineup.mild.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.lineup.mild.databinding.ActivityPreviewOriginalBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

public class PreviewOriginal extends AppCompatActivity {

    static final String DIRPATH = Environment.DIRECTORY_PICTURES + "/Mild_Architecture/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPreviewOriginalBinding binding = ActivityPreviewOriginalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                Picasso.get().load(file).into(binding.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        binding.progressBar.setVisibility(View.GONE);
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
                Picasso.get().load(imageOriginalUrl).into(binding.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        binding.progressBar.setVisibility(View.GONE);
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
