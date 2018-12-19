package com.lineup.mild;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.lineup.mild.Database.DatabaseHelper;
import com.lineup.mild.Dialog.DialogSetWallpaper;
import com.lineup.mild.Model.IntentModel;
import com.lineup.mild.Util.Font;
import com.lineup.mild.Util.MILD;
import com.lineup.mild.Util.Utils;
import com.pepperonas.materialdialog.MaterialDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Preview extends AppCompatActivity {

    @BindView(R.id.imagePreview)
    ImageView imagePreview;
    @BindView(R.id.txtCredit)
    TextView txtCredit;
    @BindView(R.id.txtResolution)
    TextView txtResolution;
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
    @BindView(R.id.btnSave)
    ImageView btnSave;
    @BindView(R.id.previewLayout)
    FrameLayout previewLayout;
    @BindView(R.id.txtImageBy)
    TextView txtImageBy;
    @BindView(R.id.adView)
    AdView adView;

    long imageDownloadId;

    static final String DIRPATH = Environment.DIRECTORY_PICTURES + "/Mild_Architecture/";
    static String FILENAME;

    DatabaseHelper db;
    DownloadManager downloadManager = null;
    IntentModel intentModel;
    DialogSetWallpaper dialogSetWallpaper;
    File files;
    FirebaseAnalytics mFirebaseAnalytics;
    boolean set_wallpaper = false;
    boolean isSaved = false;

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //check if the broadcast message is for our Enqueued download
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            String file = DIRPATH + FILENAME;

            if (referenceId == imageDownloadId && set_wallpaper) {
                if (files.exists()) {
                    setWallpaperAction(files);
                    set_wallpaper = false;
                }
            }

            if (btnDownload.getVisibility() == View.GONE) {
                dlProgress.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                Toast.makeText(Preview.this, "Download completed: " + file, Toast.LENGTH_SHORT).show();
                mFirebaseAnalytics.logEvent(MILD.FIREBASE_DOWNLOAD_SUCCESS, null);
            }
        }
    };

    IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

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

        intentModel = new IntentModel(
                getIntent().getStringExtra("imgId"),
                getIntent().getStringExtra("txtCredit"),
                getIntent().getStringExtra("imgCreditWebsite"),
                getIntent().getStringExtra("imgDimension"),
                getIntent().getStringExtra("imgOriginalUrl"),
                getIntent().getStringExtra("imgPreviewUrl"),
                getIntent().getStringExtra("imgThumbnailUrl")
        );

        txtResolution.setTypeface(new Font(this).nunitoSans());
        txtSetAs.setTypeface(new Font(this).nunitoSans());
        txtImageBy.setTypeface(new Font(this).nunitoSans());
        txtCredit.setTypeface(new Font(this).nunitoSans());

        txtResolution.setText(intentModel.getImageDimension());

        //Underline text if imageCreditWebsite is not null
        if (intentModel.getImageCreditWebsite() != null && !intentModel.getImageCreditWebsite().equals("")) {
            txtCredit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToWeb = new Intent(Intent.ACTION_VIEW);
                    goToWeb.setData(Uri.parse(intentModel.getImageCreditWebsite()));
                    startActivity(goToWeb);
                }
            });

            SpannableString content = new SpannableString(intentModel.getImageCredit());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            txtCredit.setText(content);
        } else {
            txtCredit.setText(intentModel.getImageCredit());
        }

        FILENAME = "PhotoBy_" + intentModel.getImageCredit().replaceAll("\\s+", "") + "_" + intentModel.getImageId() + ".jpg";

        db = new DatabaseHelper(this);

        if (db.dataIsExists(intentModel.getImageId())) {
            btnSave.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_black_24dp, getApplicationContext().getTheme()));
            isSaved = true;
        } else {
            isSaved = false;
        }

        Picasso.get().load(intentModel.getImagePreviewUrl()).into(imagePreview, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        Utils.isStoragePermissionGranted(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        MobileAds.initialize(this, getResources().getString(R.string.banner_test_unit));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btnDownload:
                if (Utils.isStoragePermissionGranted(this)) {
                    btnDownload.setVisibility(View.GONE);
                    dlProgress.setVisibility(View.VISIBLE);
                    imageDownloadId = downloadWallpaper(intentModel.getImageOriginalUrl());
                    mFirebaseAnalytics.logEvent(MILD.FIREBASE_DOWNLOAD, null);
                }

                break;
            case R.id.btnSetAs:
                new MaterialDialog.Builder(Preview.this)
                        .message("Set as Wallpaper?")
                        .positiveText("Ok")
                        .negativeText("Cancel")
                        .buttonCallback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);

                                dialogSetWallpaper = new DialogSetWallpaper(Preview.this);
                                dialogSetWallpaper.setListener(new DialogSetWallpaper.DialogSetWallpaperListener() {
                                    @Override
                                    public void onCancel() {
                                        downloadManager.remove(imageDownloadId);
                                    }
                                });

                                dialogSetWallpaper.showDialog();

                                // If file exists then set wallpaper from it,
                                // else download first, store it to picture dir
                                // set wallpaper from it.
                                File path = Environment.getExternalStoragePublicDirectory(DIRPATH);

                                File file = new File(path, FILENAME).getAbsoluteFile();
                                if (!path.exists())
                                    path.mkdirs();

                                files = file;

                                if (file.exists()) {
                                    setWallpaperAction(file);
                                } else {
                                    set_wallpaper = true;
                                    imageDownloadId = downloadWallpaper(intentModel.getImageOriginalUrl());
                                }
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        })
                        .show();

                break;

            case R.id.btnSave:
                if (isSaved) {
                    db.deleteFavoriteWallpaper(intentModel.getImageId());
                    isSaved = false;
                    btnSave.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_border_black_24dp, getApplicationContext().getTheme()));
                } else {
                    savedWallpaper();
                    isSaved = true;
                    btnSave.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_black_24dp, getApplicationContext().getTheme()));

                    Snackbar snackbar = Snackbar.make(previewLayout, "Saved to Collection", Snackbar.LENGTH_LONG)
                            .setAction("View Saved", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent toFavorite = new Intent(Preview.this, Saved.class);
                                    startActivity(toFavorite);
                                }
                            });

                    snackbar.setActionTextColor(Color.WHITE);
                    snackbar.show();
                }
                break;
            case R.id.imagePreview:
                if (intentModel.getImageOriginalUrl() != null) {
                    Intent intent = new Intent(Preview.this, PreviewOriginal.class);
                    intent.putExtra("imageOriginalUrl", intentModel.getImageOriginalUrl());
                    intent.putExtra("imageFileName", FILENAME);
                    startActivity(intent);
                }

                break;
            case R.id.error_btn_retry:

                break;
        }
    }

    private void savedWallpaper() {
        db.favoriteWallpaper(
                intentModel.getImageId(),
                intentModel.getImageCredit(),
                intentModel.getImageCreditWebsite(),
                intentModel.getImageDimension(),
                intentModel.getImageOriginalUrl(),
                intentModel.getImagePreviewUrl(),
                intentModel.getImageThumbnailUrl()
        );
        mFirebaseAnalytics.logEvent(MILD.FIREBASE_SAVED_IMAGE, null);
    }

    private void setWallpaperAction(File file) {
        mFirebaseAnalytics.logEvent(MILD.FIREBASE_SET_WALLPAPER, null);
        Uri uri = FileProvider.getUriForFile(Preview.this, "com.lineup.mild.fileprovider", file);
        getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        try {
            Intent wallpaperIntent = WallpaperManager.getInstance(Preview.this).getCropAndSetWallpaperIntent(uri);
            wallpaperIntent.setDataAndType(uri, "image/*");
            wallpaperIntent.putExtra("mimeType", "image/*");
            startActivityForResult(wallpaperIntent, 13451);
        } catch (Exception e) {
            e.printStackTrace();
            Intent wallpaperIntent = new Intent(Intent.ACTION_ATTACH_DATA);
            wallpaperIntent.setDataAndType(uri, "image/*");
            wallpaperIntent.putExtra("mimeType", "image/*");
            wallpaperIntent.addCategory(Intent.CATEGORY_DEFAULT);
            wallpaperIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            wallpaperIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            wallpaperIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivity(Intent.createChooser(wallpaperIntent, "Set as wallpaper"));
        }
        dialogSetWallpaper.setDownloadFinished(true);
        dialogSetWallpaper.hideDialog();
    }

    private long downloadWallpaper(String imageOriginalUrl) {
        long downloadReference;

        Uri uri = Uri.parse(imageOriginalUrl);

        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setTitle("Mild Download")
                .setDescription("downloading...")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "/Mild_Architecture/" + FILENAME);

        downloadReference = downloadManager.enqueue(request);

        return downloadReference;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(downloadReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(downloadReceiver, filter);
    }
}