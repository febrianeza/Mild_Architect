package com.lineup.mild.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;

import com.lineup.mild.R;

public class DialogSetWallpaper {

    Activity activity;
    Dialog dialog;

    //..we need the context else we can not create the dialog so get context in constructor
    public DialogSetWallpaper(Activity activity) {
        this.activity = activity;
    }

    public interface DialogSetWallpaperListener {
        void onCancel();
    }

    private DialogSetWallpaperListener listener;
    private boolean downloadFinished = false;

    public void setListener(DialogSetWallpaperListener listener) {
        this.listener = listener;
    }

    public void setDownloadFinished(boolean downloadFinished) {
        this.downloadFinished = downloadFinished;
    }

    public void showDialog() {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(true);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.dialog_set_wallpaper);

        dialog.setOnDismissListener(dialog -> {
            if (!downloadFinished && listener != null) {
                listener.onCancel();
            }
            downloadFinished = false;
        });

        //...finaly show it
        dialog.show();
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideDialog() {
        dialog.dismiss();
    }
}
