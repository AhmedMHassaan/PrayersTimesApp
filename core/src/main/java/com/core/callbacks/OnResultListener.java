package com.core.callbacks;

import androidx.activity.result.ActivityResult;

public interface OnResultListener {

    void onResult(ActivityResult result, int requestCode);

}
