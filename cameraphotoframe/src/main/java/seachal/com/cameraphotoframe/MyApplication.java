package seachal.com.cameraphotoframe;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    public static Context sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;
    }
}
