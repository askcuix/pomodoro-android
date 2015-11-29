package io.askcuix.pomodoro.model;

/**
 * Created by Chris on 15/11/29.
 */
public class AppModel {
    public static AppModel INSTANCE = new AppModel();

    private boolean hasInit = false;

    private AppModel() {}

    public void init() {
        hasInit = true;
    }

    public boolean hasInit() {
        return hasInit;
    }
}
