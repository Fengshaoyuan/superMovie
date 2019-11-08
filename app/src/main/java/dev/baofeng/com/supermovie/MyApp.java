package dev.baofeng.com.supermovie;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import android.util.Log;

import com.huangyong.downloadlib.TaskLibHelper;
import com.huangyong.downloadlib.model.Params;
import com.huangyong.playerlib.PlayerApplication;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;
import com.youngfeng.snake.Snake;

import java.util.concurrent.TimeUnit;
import dev.baofeng.com.supermovie.utils.SPUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by oceanzhang on 2017/9/28.
 */

public class MyApp extends Application {

    public static MyApp instance = null;
    public SPUtils spUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化SP
        spUtils = new SPUtils(this, "SuperMovie");
        initDownloadLib();
        //滑动关闭功能
        Snake.init(this);
        //初始化友盟统计
        UMConfigure.init(this, Params.UMENG_KEY, "zmovie", UMConfigure.DEVICE_TYPE_PHONE, "");
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }
            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
        //初始化播放器相关，投屏相关
        PlayerApplication.init(this);
    }

    private void initDownloadLib() {
        TaskLibHelper.init(instance);
    }

    public static MyApp appInstance() {
        return instance;
    }

    public static Context getContext() {
        return appInstance();
    }

    /**
     * 获取包名
     */
    @Override
    public String getPackageName() {
        if (Log.getStackTraceString(new Throwable()).contains("com.xunlei.downloadlib")) {
            return "com.xunlei.downloadprovider";
        }
        return super.getPackageName();
    }

    /**
     * 获取包管理器
     */
    @Override
    public PackageManager getPackageManager() {
        if (Log.getStackTraceString(new Throwable()).contains("com.xunlei.downloadlib")) {
            return new DelegateApplicationPackageManager(super.getPackageManager());
        }
        return super.getPackageManager();
    }

    private static final int TIMEOUT_READ = 15;
    private static final int TIMEOUT_CONNECTION = 15;
    private static OkHttpClient mOkHttpClient;

    /**
     * 生成客户端 OkHttpClient
     */
    public static OkHttpClient genericClient() {
        if (mOkHttpClient != null)
            return mOkHttpClient;
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        HttpLoggingInterceptor.Level level = BuildConfig.DEBUG ?
                HttpLoggingInterceptor.Level.HEADERS :
                HttpLoggingInterceptor.Level.NONE;
        logInterceptor.setLevel(level);
        return mOkHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(logInterceptor)
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
                .build();
    }
}
