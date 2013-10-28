package com.loopj.android.http.sample;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.sample.util.FileUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import java.io.File;

public class FileSample extends SampleParentActivity {
    private static final String LOG_TAG = "FileSample";

    @Override
    protected int getSampleTitle() {
        return R.string.title_file_sample;
    }

    @Override
    protected boolean isRequestBodyAllowed() {
        return false;
    }

    @Override
    protected boolean isRequestHeadersAllowed() {
        return true;
    }

    @Override
    protected String getDefaultURL() {
        return "https://httpbin.org/robots.txt";
    }

    @Override
    protected AsyncHttpResponseHandler getResponseHandler() {
        try {
            File tmpFile = File.createTempFile("temp_", "_response", getCacheDir());
            return new FileAsyncHttpResponseHandler(tmpFile) {
                @Override
                public void onStart() {
                    clearOutputs();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
                    debugHeaders(LOG_TAG, headers);
                    debugStatusCode(LOG_TAG, statusCode);
                    debugFile(getTargetFile());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    debugHeaders(LOG_TAG, headers);
                    debugStatusCode(LOG_TAG, statusCode);
                    debugThrowable(LOG_TAG, e);
                    debugFile(getTargetFile());
                }

                private void debugFile(File file) {
                    if (file == null || !file.exists()) {
                        debugResponse(LOG_TAG, "Response is null");
                        return;
                    }
                    try {
                        debugResponse(LOG_TAG, FileUtil.getStringFromFile(file));
                    } catch (Throwable t) {
                        Log.e(LOG_TAG, "Cannot debug file contents", t);
                    }
                }
            };
        } catch (Throwable t) {
            Log.e("FileSample", "Cannot create temporary file", t);
        }
        return null;
    }

    @Override
    protected void executeSample(AsyncHttpClient client, String URL, Header[] headers, HttpEntity entity, AsyncHttpResponseHandler responseHandler) {
        client.get(this, URL, headers, null, responseHandler);
    }
}
