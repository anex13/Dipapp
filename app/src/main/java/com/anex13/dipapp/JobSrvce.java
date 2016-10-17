/*
 * Copyright 2014 Google Inc.
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

package com.anex13.dipapp;
import android.app.job.JobParameters;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobSrvce extends android.app.job.JobService {
    private JobParameters params;

    @Override
    public boolean onStartJob(JobParameters params) {
        this.params = params;
        Toast.makeText(getApplicationContext(), "starting job service...", Toast.LENGTH_SHORT).show();
        IntentSrvs.alrmCheck(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}