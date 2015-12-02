package io.askcuix.pomodoro.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.util.List;

import io.askcuix.pomodoro.R;
import io.askcuix.pomodoro.util.FP;

/**
 * Created by Chris on 15/11/19.
 */
public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        ImageView splashImageView = (ImageView) findViewById(R.id.iv_splash);
        if (notFirstLaunch()) {
            finish();
            return;
        }

        startGuide();
    }

    private void startGuide() {
        Intent intent = new Intent(this, GuideActivity.class);
        intent.putExtra(GuideActivity.TAG_GUIDE_FROM_HELP, false);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private boolean notFirstLaunch() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        if (!FP.empty(runningTaskInfos) && runningTaskInfos.get(0).numActivities > 1) {
            Log.d(TAG, "not current activity");
            return true;
        }
        return false;
    }
}
