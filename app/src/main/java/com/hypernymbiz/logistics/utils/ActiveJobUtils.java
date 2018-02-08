package com.hypernymbiz.logistics.utils;

import android.content.Context;

import com.hypernymbiz.logistics.models.ActiveJobResume;
import com.hypernymbiz.logistics.models.User;

/**
 * Created by Metis on 07-Feb-18.
 */

public class ActiveJobUtils {
    public static void jobResumed(Context context) {
        PrefUtils.persistBoolean(context, Constants.JOB_RESUME_STATUS, true);
    }
    public static boolean isJobResumed(Context context) {
        return PrefUtils.getBoolean(context, Constants.JOB_RESUME_STATUS, false);
    }

    public static void clearJobResumed(Context context) {
        PrefUtils.remove(context, Constants.JOB_RESUME_STATUS);
        PrefUtils.remove(context, Constants.JOB);
    }

    public static void saveJobResume(Context context, ActiveJobResume job) {
        if (job == null)
            return;

        PrefUtils.persistString(context, Constants.JOB, GsonUtils.toJson(job));
    }

    public static ActiveJobResume getJobResume(Context context) {
        return GsonUtils.fromJson(PrefUtils.getString(context, Constants.JOB), ActiveJobResume.class);
    }
}
