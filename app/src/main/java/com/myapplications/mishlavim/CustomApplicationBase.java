package com.myapplications.mishlavim;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public class CustomApplicationBase extends Application implements Application.ActivityLifecycleCallbacks {

    private static WeakReference<Activity> currentActivityRef = new WeakReference<>(null);

    public static Activity getCurrentActivity() {
        return CustomApplicationBase.currentActivityRef.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        CustomApplicationBase.currentActivityRef = new WeakReference<>(activity);
    }


    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }
}