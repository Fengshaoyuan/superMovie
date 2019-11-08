package com.huangyong.downloadlib.model;

import android.os.Environment;
import android.util.Log;

import com.huangyong.downloadlib.TaskLibHelper;

import app.huangyong.com.common.SharePreferencesUtil;

public class Params {
    public static final String DOWING_DATA_KEY = "DOWING_DATA_KEY";
    public static final String URL_MD5_KEY = "URL_MD5_KEY";
    public static final String TASK_COMMPLETE = "TASK_COMMPLETE";
    public static final String TASK_TITLE_KEY = "PLAY_TITLE_KEY";
    public static final String TASK_ID_KEY = "TASK_ID_KEY";
    public static final String IS_TASK_NEW = "IS_TASK_NEW";
    public static final String PROXY_PALY_URL = "PROXY_PALY_URL";
    public static final String ACTION_PLAY_URL = "action_play_video";
    public static final String ACTION_UPDATE_PAGER_POSTER = "com_update_viewpager_poster";
    public static final String HISTORY_SAVE = "com.movie.history";
    public static final String MOVIE_PROGRESS = "MOVIE_PROGRESS";
    public static final String UPDATE_MEMERY_SIZE = "UPDATE_MEMERY_SIZE";
    public static final String UMENG_KEY = "5bbab8d1b465f5d1e8000186";
    public static final String DURL = "DURL";
    public static final String ACTION_UPDATE_PROGERSS = "action.update.progress";
    public static final String HAVE_UPDATE = "HAVE_UPDATE";
    public static final String PALY_LIST_URL = "PALY_LIST_URL";
    public static String NetWorkChangeAction = "downlib.network.changed";
    public static String TASK_DELETE = "downlib.stop.task";
    public static String TASK_START ="downlib.start.task";
    public static String TASK_ID = "TASK_ID";
    public static String LOCAL_PATH_KEY = "PLAY_PATH_KEY";
    public static String POST_IMG_KEY = "POSTER_IMG_KEY";

    public static String TASK_URL_KEY = "TASK_URL_KEY";
    public static String UPDATE_PROGERSS ="UPDATE_PROGERSS";



    public static String DEFAULT_PATH = Environment.getExternalStorageDirectory()+"/MovieDownload";

    public static String getPath(){
       return SharePreferencesUtil.getStringSharePreferences(TaskLibHelper.getContexts(),LOCAL_PATH_KEY, Environment.getExternalStorageDirectory()+"/MovieDownload");
    }
}
