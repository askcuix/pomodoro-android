package io.askcuix.pomodoro.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.Random;

import io.askcuix.pomodoro.R;
import io.askcuix.pomodoro.util.DimensionUtil;

/**
 * Created by Chris on 15/12/2.
 */
public abstract class BaseDialog extends DialogFragment {
    private static final String TAG = "BaseDialog";

    public interface PauseAble {
        boolean isPaused();
    }

    protected Builder builder;
    private boolean isShown = false;
    private final Random random = new Random();

    public void show(FragmentActivity activity) {
        if (activity == null) {
            return;
        }
        if (activity instanceof PauseAble && ((PauseAble) activity).isPaused()) {
            return;
        }
        String tag = getTag();
        if (tag == null) {
            tag = random.nextInt() + "";
        }
        Log.v(TAG, "dialog tag " + tag);
        show(activity.getSupportFragmentManager(), tag);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        isShown = true;
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        int id = super.show(transaction, tag);
        isShown = true;
        return id;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isShown = false;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        isShown = false;
    }

    public boolean isShown() {
        return isShown;
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
            isShown = false;
        } catch (Exception e) {
            Log.e(TAG, "dismiss dialog error " + this.getActivity(), e);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 11) {
            setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.DialogFragmentStyleOver11);
        } else {
            setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.Translucent_NoTitle);
        }
        if (builder != null) {
            setCancelable(builder.isCancelable);
        }
    }

    protected DialogInterface.OnCancelListener getCancelListener() {
        if (builder.onCancelListener != null) {
            return builder.onCancelListener;
        }
        final Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            if (targetFragment instanceof DialogInterface.OnCancelListener) {
                return (DialogInterface.OnCancelListener) targetFragment;
            }
        } else {
            if (getActivity() instanceof DialogInterface.OnCancelListener) {
                return (DialogInterface.OnCancelListener) getActivity();
            }
        }
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        // safety check
        if (getDialog() == null) {
            return;
        }
        if (builder.width == null) {
            builder.width = (int) (DimensionUtil.getScreenWidth(getActivity()) * 0.8);
        }

        if (builder.height == null) {
            builder.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        getDialog().getWindow().setLayout(builder.width, builder.height);
        getDialog().setCanceledOnTouchOutside(builder.isCanceledOnTouchOutside);
        getDialog().setCancelable(builder.isCancelable);
        getDialog().setOnCancelListener(getCancelListener());
    }

    @Override
    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    public static class Builder implements Serializable {

        protected String title;
        protected boolean isCancelable = true;
        protected boolean isCanceledOnTouchOutside = true;
        protected Integer width;
        protected Integer height;
        protected Integer requestCode = 0;
        private DialogInterface.OnCancelListener onCancelListener;


        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setCancelable(boolean cancel) {
            isCancelable = cancel;
            if (!cancel) {
                setCanceledOnTouchOutside(false);
            }
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean cancel) {
            isCanceledOnTouchOutside = cancel;
            return this;
        }

        public Builder setWidth(Integer width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(Integer height) {
            this.height = height;
            return this;
        }

        public Builder setRequestCode(Integer requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            this.onCancelListener = onCancelListener;
            return this;
        }

        public DialogInterface.OnCancelListener getOnCancelListener() {
            return onCancelListener;
        }

        public String getTitle() {
            return title;
        }

    }
}
