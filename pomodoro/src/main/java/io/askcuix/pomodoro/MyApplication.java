package io.askcuix.pomodoro;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.os.*;
import android.util.Log;

import java.util.List;

import io.askcuix.pomodoro.model.AppModel;

/**
 * Created by Chris on 15/11/19.
 */
public class MyApplication extends Application {
    private static final String TAG = "pomodoro";

    public static boolean firstLaunch = false;
    public static Handler handler = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "Application onCreate");

        if (!AppModel.INSTANCE.hasInit()) {
            AppModel.INSTANCE.init();
        }

        firstLaunch = true;
        if (handler == null) {
            handler = new Handler(getMainLooper());
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        Log.w(TAG, "on low memory.");
    }

    public static boolean isTaskInForeground(Context context) {
        ComponentName topActivity = getTopActivity(context);
        if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
            return false;
        }

        return true;
    }

    public static ComponentName getTopActivity(Context context) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList = activityManager.getRunningTasks(1);
        if (runningTaskInfoList != null && !runningTaskInfoList.isEmpty()) {
            ComponentName topActivity = runningTaskInfoList.get(0).topActivity;
            return topActivity;
        }
        return null;
    }

    public static String getTopProcessName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();
        if (runningAppProcessInfoList != null && !runningAppProcessInfoList.isEmpty()) {
            String topProcess = runningAppProcessInfoList.get(0).processName;
            return topProcess;
        }
        return "";
    }

    public static void runInMainThread(Runnable runnable) {
        handler.post(runnable);
    }

    private boolean shouldInit() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }

        return false;
    }
}
