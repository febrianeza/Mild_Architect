package com.lineup.mild.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.lineup.mild.R;
import com.lineup.mild.fragments.SampleSlide;

public class Intro extends AppIntro2 {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(SampleSlide.newInstance(R.layout.app_intro_page1));
        addSlide(SampleSlide.newInstance(R.layout.app_intro_page2));
        addSlide(SampleSlide.newInstance(R.layout.app_intro_page3));
        addSlide(SampleSlide.newInstance(R.layout.app_intro_page4));
        addSlide(SampleSlide.newInstance(R.layout.app_intro_page5));
        addSlide(SampleSlide.newInstance(R.layout.app_intro_page6));

        askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);

        showSkipButton(false);
        setProgressIndicator();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        startActivity(new Intent(this, Main.class));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        startActivity(new Intent(this, Main.class));
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
