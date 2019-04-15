package com.test.githubrepos.realm;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by amresh on 15/04/2019
 */
public class GitRepoMigration implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

    }

    @Override
    public int hashCode() {
        return GitRepoMigration.class.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof GitRepoMigration;
    }

}
