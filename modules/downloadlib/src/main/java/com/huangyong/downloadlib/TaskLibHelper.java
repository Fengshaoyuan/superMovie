package com.huangyong.downloadlib;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.huangyong.downloadlib.model.Params;
import com.huangyong.downloadlib.service.DownLoadService;
import com.huangyong.downloadlib.utils.BroadCastUtils;
import com.huangyong.downloadlib.utils.FileUtils;
import com.huangyong.downloadlib.utils.MD5Utils;
import com.xunlei.downloadlib.XLTaskHelper;

import java.io.IOException;

public class TaskLibHelper {

    private static Context contexts;

    public static void init(Context context){
        contexts = context;
        //初始化本地下载
        XLTaskHelper.init(context.getApplicationContext());
        //创建本地下载路径
        try {
           FileUtils.isExistDir(Params.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(context, DownLoadService.class);
        context.startService(intent);
    }

    public static Context getContexts(){
        return contexts;
    }

    public static void deleteTask(String url,Context context){
        Intent intent = new Intent();
        intent.putExtra(Params.TASK_URL_KEY,url);
        BroadCastUtils.sendIntentBroadCask(context,intent, Params.TASK_DELETE);
    }


    public static void addNewTask(String url, String savepath, String postImgUrl, Context applicationContext) {
        if (TextUtils.isEmpty(url)){
            Toast.makeText(applicationContext, "下载地址为空", Toast.LENGTH_SHORT).show();
        }
        String savePath = null;
        try {
            savePath = FileUtils.isExistDir(savepath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (savePath==null){
            Toast.makeText(applicationContext, "文件目录创建失败，检查是否具有文件读写权限", Toast.LENGTH_SHORT).show();
            return;
        }
        String urlMd5 = MD5Utils.stringToMD5(url);
        Intent intent = new Intent();
        intent.putExtra(Params.TASK_URL_KEY,url);
        intent.putExtra(Params.POST_IMG_KEY,postImgUrl);
        intent.putExtra(Params.LOCAL_PATH_KEY,savePath);
        intent.putExtra(Params.URL_MD5_KEY,urlMd5);
        intent.putExtra(Params.IS_TASK_NEW,true);
        BroadCastUtils.sendIntentBroadCask(applicationContext,intent, Params.TASK_START);
    }

}
