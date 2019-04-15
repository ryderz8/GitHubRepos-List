package com.test.githubrepos.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.util.Log;

import com.test.githubrepos.realm.module.AllAppModule;
import com.test.githubrepos.realm.GitRepoMigration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by amresh on 15/04/2019
 */
public class AppUtils {

    public static RealmConfiguration getRealmConfig() {
        RealmConfiguration gitReposConfig = new RealmConfiguration.Builder()
                .name(Constants.GITREPOS_SCHEMA)
                .schemaVersion(Constants.GITREPOS_SCHEMA_VERSION)
                .migration(new GitRepoMigration())
                .modules(Realm.getDefaultModule(), new AllAppModule())
                .build();
        return gitReposConfig;
    }

    public static Realm getRealmGitReposInstance() {
        return getRealmGitReposInstance(false);
    }


    public static Realm getRealmGitReposInstance(boolean compactRealm) {
        RealmConfiguration gitReposConfig = getRealmConfig();
        if (compactRealm) {
            Realm.compactRealm(gitReposConfig);
        }
        Log.i("GitRepos realm open instance(s)", "" + Realm.getGlobalInstanceCount(gitReposConfig));

        Realm realm = Realm.getInstance(gitReposConfig);
        if (Looper.myLooper() != Looper.getMainLooper()) {
            // only refresh when on background thread as per
            realm.refresh();
        }
        return realm;
    }

    public static void closeRealm(Realm realm) {
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
