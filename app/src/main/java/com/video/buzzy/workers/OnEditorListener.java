package com.video.buzzy.workers;

public interface OnEditorListener {
    void onFailure();

    void onProgress(float f);

    void onSuccess();
}

