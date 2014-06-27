/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2014 Carmen Alvarez (c@rmen.ca)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.rmen.android.networkmonitor.app.service.datasources;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;

import ca.rmen.android.networkmonitor.Constants;
import ca.rmen.android.networkmonitor.app.speedtest.SpeedTestPreferences;
import ca.rmen.android.networkmonitor.app.speedtest.SpeedTestPreferences.SpeedTestUploadConfig;
import ca.rmen.android.networkmonitor.app.speedtest.SpeedTestResult;
import ca.rmen.android.networkmonitor.app.speedtest.SpeedTestResult.SpeedTestStatus;
import ca.rmen.android.networkmonitor.app.speedtest.UploadSpeedTest;
import ca.rmen.android.networkmonitor.provider.NetMonColumns;
import ca.rmen.android.networkmonitor.util.Log;

/**
 * Tests upload speed by uploading a file.
 */
class UploadSpeedTestDataSource implements NetMonDataSource {
    private static final String TAG = Constants.TAG + UploadSpeedTestDataSource.class.getSimpleName();

    private SpeedTestPreferences mPreferences;

    @Override
    public void onCreate(Context context) {
        Log.v(TAG, "onCreate");
        mPreferences = SpeedTestPreferences.getInstance(context);
    }

    @Override
    public void onDestroy() {}

    @Override
    public ContentValues getContentValues() {
        Log.v(TAG, "getContentValues");
        ContentValues values = new ContentValues();
        File file = new File("/mnt/sdcard/test.txt");//TODO use the downloaded file

        if (!mPreferences.isEnabled()) return values;
        SpeedTestUploadConfig uploadConfig = mPreferences.getUploadConfig();
        if (!uploadConfig.isValid()) return values;
        SpeedTestResult result = UploadSpeedTest.upload(file, uploadConfig);
        if (result.status == SpeedTestStatus.SUCCESS) values.put(NetMonColumns.UPLOAD_SPEED, result.getSpeedMbps());
        return values;
    }
}