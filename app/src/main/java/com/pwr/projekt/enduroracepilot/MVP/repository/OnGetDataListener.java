package com.pwr.projekt.enduroracepilot.MVP.repository;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by Sebastian on 2017-04-09.
 */

public interface OnGetDataListener {

    void onStart();

    void onSuccess(DataSnapshot data);

    void onFailed(DatabaseError databaseError);

}
