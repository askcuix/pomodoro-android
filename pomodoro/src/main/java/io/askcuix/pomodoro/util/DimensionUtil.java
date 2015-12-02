package io.askcuix.pomodoro.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Chris on 15/12/2.
 */
public class DimensionUtil {
    public DimensionUtil() {
    }

    public static int dipToPx(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

    public static int pxToDip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5F);
    }

    public static int getScreenWidth(Context context) {
        return getScreenSize(context, (Point)null).x;
    }

    public static int getScreenHeight(Context context) {
        return getScreenSize(context, (Point)null).y;
    }

    @TargetApi(13)
    public static Point getScreenSize(Context context, Point outSize) {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Point ret = outSize == null?new Point():outSize;
        Display defaultDisplay = wm.getDefaultDisplay();
        if(Build.VERSION.SDK_INT >= 13) {
            defaultDisplay.getSize(ret);
        } else {
            ret.x = defaultDisplay.getWidth();
            ret.y = defaultDisplay.getHeight();
        }

        return ret;
    }

    public static int getDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }
}
