package uk.co.jatra.bezierplay;

import android.app.Application;

/**
 * Created by tim on 13/12/2015.
 */
public class BezierApplication extends Application {
    private static AppComponent appComponent;
    private static AppModule appModule;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static AppComponent getComponent() {
        if (appComponent == null) {
            if (appModule == null) {
                appModule = new AppModule();
            }
            appComponent = DaggerAppComponent.builder()
                    .appModule(appModule)
                    .build();
        }
        return appComponent;
    }

    public static void setAppModule(AppModule appModule) {
        BezierApplication.appModule = appModule;
    }
}
