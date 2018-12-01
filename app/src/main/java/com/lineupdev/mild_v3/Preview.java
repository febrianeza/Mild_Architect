package com.lineupdev.mild_v3;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.txtSetAs)
    TextView txtSetAs;
    @BindView(R.id.btnSetAs)
    CardView btnSetAs;
    @BindView(R.id.btnDownload)
    ImageView btnDownload;
    @BindView(R.id.dlProgress)
    ProgressBar dlProgress;

    private String imageTitle = null;
    private String imageCredit = null;
    private String imageCreditWebsite = null;
    private String imageDimension = null;
    private String imageOriginalUrl = null;
    private String imagePreviewUrl = null;
    private long ImageDownloadId;

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

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NunitoSans-Light.ttf");
        toolbarText.setTypeface(typeface);
        txtSetAs.setTypeface(typeface);

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);

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

//        Picasso.get().load(imagePreviewUrl).into(new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                assert imagePreview != null;
//
//                imagePreview.setImageBitmap(bitmap);
//                Palette.from(bitmap)
//                        .generate(new Palette.PaletteAsyncListener() {
//                            @Override
//                            public void onGenerated(@NonNull Palette palette) {
//                                Palette.Swatch swatch = palette.getVibrantSwatch();
//                                if (swatch == null) {
//                                    Toast.makeText(Preview.this, "Null Swatch", Toast.LENGTH_SHORT).show();
//                                }
//
//                                btnSetAs.setCardBackgroundColor(swatch.getRgb());
//                            }
//                        });
//            }
//
//            @Override
//            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//            }
//        });

        toolbarText.setText(imageTitle);
        imgCredit.setText(imageCredit);

        if (imageCreditWebsite != null) {
            imgCredit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToWeb = new Intent(Intent.ACTION_VIEW);
                    goToWeb.setData(Uri.parse(imageCreditWebsite));
                    startActivity(goToWeb);
                }
            });
        }

        btnSetAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Preview.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
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

    private void checkStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            btnDownload.setVisibility(View.GONE);
                            dlProgress.setVisibility(View.VISIBLE);


                            // if permission granted,
                            // execute download method.
                            Uri uriImageLink = Uri.parse(imageOriginalUrl);
                            ImageDownloadId = DownloadImage(uriImageLink);
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {

                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    DownloadManager downloadManager = null;

    private long DownloadImage(Uri uriImageLink) {
        long downloadReference;

        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uriImageLink);

        //Settings title
        request.setTitle("Image Download");

        //settings description
        request.setDescription("is downloading...");

        //set local destination
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "/Mild_Architecture/" + imageTitle.replaceAll("\\s+","") + "_PhotoBy_" + imageCredit.replaceAll("\\s+","") + ".jpg");

        downloadReference = downloadManager.enqueue(request);

        return downloadReference;
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //check if the broadcast message is for our Enqueued download
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            String location = Environment.DIRECTORY_PICTURES + "/Mild_Architecture/" + imageTitle.replaceAll("\\s+","") + "_PhotoBy_" + imageCredit.replaceAll("\\s+","") + ".jpg";

            if (referenceId == ImageDownloadId) {
                Toast.makeText(Preview.this, "Download completed! : " + location, Toast.LENGTH_SHORT).show();
            }

            dlProgress.setVisibility(View.GONE);
            btnDownload.setVisibility(View.VISIBLE);
        }
    };
}