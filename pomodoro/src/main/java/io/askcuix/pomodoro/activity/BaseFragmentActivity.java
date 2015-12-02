package io.askcuix.pomodoro.activity;

import android.support.v4.app.FragmentActivity;

/**
 * Created by Chris on 15/12/2.
 */
public class BaseFragmentActivity extends FragmentActivity implements BaseDialog.PauseAble  {
    @Override
    public boolean isPaused() {
        return false;
    }
}
