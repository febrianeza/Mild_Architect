package com.lineupdev.mild_v3;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
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
import com.lineupdev.mild_v3.Util.DirUtils;
import com.pepperonas.materialdialog.MaterialDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.theartofdev.edmodo.cropper.CropImage;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

    private String imageId = null;
    private String imageTitle = null;
    private String imageCredit = null;
    private String imageCreditWebsite = null;
    private String imageDimension = null;
    private String imageOriginalUrl = null;
    private String imagePreviewUrl = null;
    private long ImageDownloadId;

    private static String DIRPATH;
    private static int DEVICE_WIDTH;
    private static int DEVICE_HEIGHT;
    private static int ASPECTX;
    private static int ASPECTY;

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

        imageId = getIntent().getStringExtra("imgId");
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

        DIRPATH = DirUtils.getRootDirPath(Preview.this);

        DeviceDimension dm = new DeviceDimension();
        DEVICE_HEIGHT = dm.HEIGHT;
        DEVICE_WIDTH = dm.WIDTH;

        int gcd = GCD(DEVICE_WIDTH, DEVICE_HEIGHT);
        ASPECTX = DEVICE_WIDTH / gcd;
        ASPECTY = DEVICE_HEIGHT / gcd;

        Log.d("SCREENRES", "W: " + DEVICE_WIDTH + " H: " + DEVICE_HEIGHT);
        Log.d("SCREENASPECT", "X: " + ASPECTX + " Y: " + ASPECTY);

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

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");

        btnSetAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Code Here*/
                new MaterialDialog.Builder(Preview.this)
                        .title("SET AS WALLPAPER")
                        .positiveText("Home Screen")
                        .negativeText("Lock Screen")
                        .neutralText("Both")
                        .buttonCallback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                /*For Home Screen*/
                                progressDialog.show();

                                Picasso.get()
                                        .load(imageOriginalUrl)
                                        .into(new Target() {
                                            @Override
                                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                                                File file = new File(DIRPATH + "/image");
//                                                if (!file.exists())
//                                                    file.mkdirs();
//
//                                                file = new File(file, imageId);
//                                                if (!file.exists()) {
//                                                    try {
//                                                        FileOutputStream outputStream = new FileOutputStream(file);
//                                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//
//                                                        outputStream.flush();
//                                                        outputStream.close();
//
//                                                    } catch (IOException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                }

                                                progressDialog.dismiss();

                                                setWallPaperWithBitmap(Preview.this, bitmap);

//                                                UCrop.of(Uri.fromFile(file), Uri.fromFile(new File(DIRPATH + "/image/" + "c_" + imageId)))
//                                                        .withAspectRatio(ASPECTX, ASPECTY)
//                                                        .start(Preview.this);

//                                                CropImage.activity(Uri.fromFile(file))
//                                                        .setActivityTitle("Set as Wallpaper")
//                                                        .setAutoZoomEnabled(true)
//                                                        .setAllowFlipping(false)
//                                                        .setAllowRotation(false)
//                                                        .setAllowCounterRotation(false)
//                                                        .setAspectRatio(ASPECTX,ASPECTY)
//                                                        .start(Preview.this);

//                                                Intent wallpaperIntent = WallpaperManager.getInstance(Preview.this).getCropAndSetWallpaperIntent(Uri.fromFile(file));
//                                                wallpaperIntent.setDataAndType(Uri.fromFile(file), "image/*");
//                                                wallpaperIntent.putExtra("mimeType", "image/*");
//                                                startActivityForResult(wallpaperIntent, 13451);
                                            }

                                            @Override
                                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                                e.printStackTrace();
                                            }

                                            @Override
                                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                                            }
                                        });
                            }

                            @Override
                            public void onNeutral(MaterialDialog dialog) {
                                super.onNeutral(dialog);
                                /*For Both*/
                                Toast.makeText(Preview.this, "Both", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                                /*For Lock Screen*/
                                Toast.makeText(Preview.this, "LockScreen", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
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
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "/Mild_Architecture/" + imageTitle.replaceAll("\\s+", "") + "_PhotoBy_" + imageCredit.replaceAll("\\s+", "") + ".jpg");

        downloadReference = downloadManager.enqueue(request);

        return downloadReference;
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //check if the broadcast message is for our Enqueued download
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            String location = Environment.DIRECTORY_PICTURES + "/Mild_Architecture/" + imageTitle.replaceAll("\\s+", "") + "_PhotoBy_" + imageCredit.replaceAll("\\s+", "") + ".jpg";

            if (referenceId == ImageDownloadId) {
                Toast.makeText(Preview.this, "Download completed! : " + location, Toast.LENGTH_SHORT).show();
            }

            dlProgress.setVisibility(View.GONE);
            btnDownload.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(downloadReceiver);
    }

    class DeviceDimension {
        private int HEIGHT;
        private int WIDTH;

        DeviceDimension() {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            this.WIDTH = size.x;
            this.HEIGHT = size.y;
        }
    }

    // simple utility function to calculate GCD
    private int GCD(int a, int b) {
        return (b == 0 ? a : GCD(b, a % b));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                setWallpapers(resultUri, data);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void setWallpapers(Uri resultUri, Intent data) {
//        try {
//            InputStream inputStream = getContentResolver().openInputStream(resultUri);
//
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//
//            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, DEVICE_WIDTH, DEVICE_HEIGHT, true);
//
//            WallpaperManager wallpaperManager = WallpaperManager.getInstance(Preview.this);
//            try {
//                wallpaperManager.setBitmap(bitmap2);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Intent wallpaperIntent = WallpaperManager.getInstance(this).getCropAndSetWallpaperIntent(resultUri);
//        wallpaperIntent.setDataAndType(resultUri, "image/*");
//        wallpaperIntent.putExtra("mimeType", "image/*");
//        startActivityForResult(wallpaperIntent, 13451);
    }

    private static void setWallPaperWithBitmap(Context context, Bitmap bitmap) {
        WallpaperManager manager = WallpaperManager.getInstance(context);
        manager.suggestDesiredDimensions(DEVICE_WIDTH, DEVICE_HEIGHT);
        Uri bitmapUri = getBitmapUri(context, bitmap);
        if (bitmapUri != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                context.startActivity(manager.getCropAndSetWallpaperIntent(bitmapUri));
            } else {
                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.setDataAndType(bitmapUri, "image/*");
                intent.putExtra("mimeType", "image/*");
                context.startActivity(Intent.createChooser(intent, context.getString(R.string.set_as)));
            }
        } else {
            try {
                manager.setBitmap(bitmap);
                Toast.makeText(context, "Wallpaper Success", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(context, "Wallpaper Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private static Uri getBitmapUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "MildWallpaper", null);
        return Uri.parse(path);
    }
}