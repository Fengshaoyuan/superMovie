package dev.baofeng.com.supermovie.presenter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


import dev.baofeng.com.supermovie.domain.AppUpdateInfo;
import dev.baofeng.com.supermovie.http.ApiService;
import dev.baofeng.com.supermovie.http.BaseApi;
import dev.baofeng.com.supermovie.presenter.iview.IupdateView;

/**
 * @author Huangyong
 * @version 1.0
 * 2018/9/27 ：created
 * 应用内更新
 */
public class UpdateAppPresenter extends BasePresenter<IupdateView> {

    public UpdateAppPresenter(Context context, IupdateView iupdateView) {
        super(context, iupdateView);
    }

    @Override
    public void release() {
        unSubcription();
    }

    /**
     * 获取更新信息
     */
    public void getAppUpdate(Context context) {
        BaseApi.request(BaseApi.createApi(ApiService.class)
                        .getAppUpdate(), new BaseApi.IResponseListener<AppUpdateInfo>() {
                    @Override
                    public void onSuccess(AppUpdateInfo result) {
                        if (result.getData().getVersionCode() > getVersionCode(context)) {
                            //如果服务端的版本号大于本地，即更新
                            iview.updateYes(result);
                        } else {
                            iview.noUpdate(result.getData().getDownloadUrl());
                        }
                    }

                    @Override
                    public void onFail() {
                    }
                }
        );
    }

    /**
     * 获取本地版本号
     */
    private static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            String PACKAGE_NAME = "dev.baofeng.com.supermovie";
            PackageInfo info = manager.getPackageInfo(PACKAGE_NAME, 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
