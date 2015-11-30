package io.askcuix.pomodoro.model;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.j256.ormlite.android.AndroidConnectionSource;

/**
 * Created by Chris on 15/11/29.
 */
public class AppModel {
    public static AppModel INSTANCE = new AppModel();

    private boolean hasInit = false;
    private Application application;

    private Handler mainThreadHandler;
    private Handler ioHandler;

    private LoginInfo loginInfo;
    private AndroidConnectionSource connectionSource;

    private final String POMODORO_DB_NAME = "pomodoro";

    private AppModel() {}

    public void init(final Application application) {
        this.application = application;
        hasInit = true;

        mainThreadHandler = new Handler(Looper.getMainLooper());

        HandlerThread ioThread = new HandlerThread("IOThread");
        ioThread.start();
        ioHandler = new Handler(ioThread.getLooper());

        loginInfo = new LoginInfo();
        loginInfo.init(application);

        SQLiteDatabase database = null;
        try {
            database = application.openOrCreateDatabase(POMODORO_DB_NAME, 1, null);
        }catch (SQLiteException ex) {
            final String path = application.getDatabasePath(POMODORO_DB_NAME).getPath();
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        }
        connectionSource = new AndroidConnectionSource(database);

    }

    public boolean hasInit() {
        return hasInit;
    }
}
