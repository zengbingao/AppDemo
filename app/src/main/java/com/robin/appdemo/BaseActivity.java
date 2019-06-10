package com.robin.appdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.transition.Slide;
import android.view.Gravity;

public class BaseActivity extends Activity {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Slide slide = new Slide(Gravity.END);
        slide.setDuration(200);
        getWindow().setEnterTransition(slide);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void redirectAct(Class clazz) {
//        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
//        startActivity(new Intent(this, clazz), compat.toBundle());
        startActivity(new Intent(this, clazz));
        overridePendingTransition(R.anim.open_enter_anim, R.anim.open_exit_anim);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.close_enter_anim, R.anim.close_exit_anim);
//        Slide slide = new Slide(Gravity.START);
//        slide.setDuration(200);
//        getWindow().setExitTransition(slide);
    }
}
