package com.lineup.mild.activity;

import android.annotation.SuppressLint;
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
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.lineup.mild.R;
import com.lineup.mild.database.DatabaseHelper;
import com.lineup.mild.databinding.ActivityPreviewBinding;
import com.lineup.mild.dialog.DialogSetWallpaper;
import com.lineup.mild.model.IntentModel;
import com.lineup.mild.util.Font;
import com.lineup.mild.util.Utils;
import com.pepperonas.materialdialog.MaterialDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

public class Preview extends AppCompatActivity {

    private ActivityPreviewBinding binding;
    private ReviewManager manager;

    long imageDownloadId;

    static final String DIRPATH = Environment.DIRECTORY_PICTURES + "/Mild_Architecture/";
    static String FILENAME;

    DatabaseHelper db;
    DownloadManager downloadManager = null;
    IntentModel intentModel;
    DialogSetWallpaper dialogSetWallpaper;
    File files;
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

            if (binding.btnDownload.getVisibility() == View.GONE) {
                binding.dlProgress.setVisibility(View.GONE);
                binding.btnDownload.setVisibility(View.VISIBLE);
                Toast.makeText(Preview.this, "Download completed: " + file, Toast.LENGTH_SHORT).show();

            }
        }
    };

    IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

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

        binding.txtResolution.setTypeface(new Font(this).nunitoSans());
        binding.txtSetAs.setTypeface(new Font(this).nunitoSans());
        binding.txtImageBy.setTypeface(new Font(this).nunitoSans());
        binding.txtCredit.setTypeface(new Font(this).nunitoSans());

        binding.txtResolution.setText(intentModel.getImageDimension());

        //Underline text if imageCreditWebsite is not null
        if (intentModel.getImageCreditWebsite() != null && !intentModel.getImageCreditWebsite().equals("")) {
            binding.txtCredit.setOnClickListener(v -> {
                Intent goToWeb = new Intent(Intent.ACTION_VIEW);
                goToWeb.setData(Uri.parse(intentModel.getImageCreditWebsite()));
                startActivity(goToWeb);
            });

            SpannableString content = new SpannableString(intentModel.getImageCredit());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            binding.txtCredit.setText(content);
        } else {
            binding.txtCredit.setText(intentModel.getImageCredit());
        }

        FILENAME = "PhotoBy_" + intentModel.getImageCredit().replaceAll("\\s+", "") + "_" + intentModel.getImageId() + ".jpg";

        db = new DatabaseHelper(this);

        if (db.dataIsExists(intentModel.getImageId())) {
            binding.btnSave.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_black_24dp, getApplicationContext().getTheme()));
            isSaved = true;
        } else {
            isSaved = false;
        }

        loadImage();

        Utils.isStoragePermissionGranted(this);

        manager = ReviewManagerFactory.create(this);
    }

    private void loadImage() {
        Picasso.get().load(intentModel.getImagePreviewUrl()).into(binding.imagePreview, new Callback() {
            @Override
            public void onSuccess() {
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                binding.errorBtnRetry.setVisibility(View.VISIBLE);
                Toast.makeText(Preview.this, "Image load error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btnDownload:
                if (Utils.isStoragePermissionGranted(this)) {
                    binding.btnDownload.setVisibility(View.GONE);
                    binding.dlProgress.setVisibility(View.VISIBLE);
                    imageDownloadId = downloadWallpaper(intentModel.getImageOriginalUrl());
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
                                dialogSetWallpaper.setListener(() -> downloadManager.remove(imageDownloadId));

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
                    binding.btnSave.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_border_black_24dp, getApplicationContext().getTheme()));
                } else {
                    savedWallpaper();
                    isSaved = true;
                    binding.btnSave.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_black_24dp, getApplicationContext().getTheme()));

                    Snackbar snackbar = Snackbar.make(binding.previewLayout, "Saved to Collection", Snackbar.LENGTH_LONG)
                            .setAction("View Saved", v -> {
                                Intent toFavorite = new Intent(Preview.this, Saved.class);
                                startActivity(toFavorite);
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
                binding.errorBtnRetry.setVisibility(View.GONE);
                loadImage();
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
    }

    private void setWallpaperAction(File file) {
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

        // start app review card
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(this, reviewInfo);
                flow.addOnCompleteListener(taskFlow -> {

                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                    Log.d("PreviewActivity", "onCreate: Review Complete");
                });
            }
        });
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