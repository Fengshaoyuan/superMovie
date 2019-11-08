package zmovie.com.dlan;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yanbo.lib_screen.callback.ControlCallback;
import com.yanbo.lib_screen.entity.AVTransportInfo;
import com.yanbo.lib_screen.entity.ClingDevice;
import com.yanbo.lib_screen.entity.RemoteItem;
import com.yanbo.lib_screen.entity.RenderingControlInfo;
import com.yanbo.lib_screen.event.ControlEvent;
import com.yanbo.lib_screen.manager.ClingManager;
import com.yanbo.lib_screen.manager.ControlManager;
import com.yanbo.lib_screen.manager.DeviceManager;
import com.yanbo.lib_screen.utils.LogUtils;
import com.yanbo.lib_screen.utils.VMDate;

import org.fourthline.cling.support.model.item.Item;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import per.goweii.anylayer.AnimHelper;
import per.goweii.anylayer.AnyLayer;

/**
 * 描述：
 *
 * @author Yanbo
 * @date 2018/11/6
 */
public class MediaPlayActivity extends AppCompatActivity implements View.OnClickListener {

    TextView contentTitleView;
    TextView contentUrlView;
    TextView volumeView;
    SeekBar volumeSeekbar;
    SeekBar progressSeekbar;
    TextView playTimeView;
    TextView playMaxTimeView;
    ImageView stopView;
    ImageView previousView;
    ImageView playView;
    ImageView nextView;


    public Item localItem;
    public RemoteItem remoteItem;

    private int defaultVolume = 10;
    private int currVolume = defaultVolume;
    private boolean isMute = false;
    private int currProgress = 0;
    private ImageView reduce;
    private ImageView plus;

    public static void startSelf(Activity context) {
        Intent intent = new Intent(context, MediaPlayActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_media_play);
        contentTitleView=findViewById(R.id.text_content_title);
        contentUrlView=findViewById(R.id.text_content_url);
        volumeView=findViewById(R.id.img_volume);
        volumeSeekbar=findViewById(R.id.seek_bar_volume);
        progressSeekbar=findViewById(R.id.seek_bar_progress);
        playTimeView=findViewById(R.id.text_play_time);
        playMaxTimeView=findViewById(R.id.text_play_max_time);
        stopView=findViewById(R.id.img_stop);
        previousView=findViewById(R.id.img_previous);

        reduce = findViewById(R.id.reduce_volume);
        plus = findViewById(R.id.plus_volume);
        reduce.setOnClickListener(this);
        plus.setOnClickListener(this);


        playView=findViewById(R.id.img_play);
        nextView=findViewById(R.id.img_next);
        playView.setOnClickListener(this);
        nextView.setOnClickListener(this);
        stopView.setOnClickListener(this);
        playTimeView.setOnClickListener(this);
        init();


    }




    private void init() {

        localItem = ClingManager.getInstance().getLocalItem();
        remoteItem = ClingManager.getInstance().getRemoteItem();
        String url = "";
        String duration = "";
        if (localItem != null) {
            url = localItem.getFirstResource().getValue();
            duration = localItem.getFirstResource().getDuration();
        }
        if (remoteItem != null) {

            url = remoteItem.getUrl();
            duration = remoteItem.getDuration();
        }

        contentTitleView.setText("  ");
        contentUrlView.setText(url);

        if (!TextUtils.isEmpty(duration)) {
            playMaxTimeView.setText(duration);
            progressSeekbar.setMax((int) VMDate.fromTimeString(duration));
        }

        setVolumeSeekListener();
        setProgressSeekListener();
    }


    /**
     * 设置音量拖动监听
     */
    private void setVolumeSeekListener() {
        volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtils.d("Volume seek position: %d", progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setVolume(seekBar.getProgress());
            }
        });
    }

    /**
     * 设置播放进度拖动监听
     */
    private void setProgressSeekListener() {
        progressSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currProgress = seekBar.getProgress();
                playTimeView.setText(VMDate.toTimeString(currProgress));
                seekCast(currProgress);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.img_volume) {
            mute();

        } else if (i == R.id.img_stop) {
            stop();

        } else if (i == R.id.img_previous) {
        } else if (i == R.id.img_play) {
            play();

        } else if (i == R.id.img_next) {
        }

        if (i == R.id.reduce_volume){
            currVolume-=5;
            setVolume(currVolume);
        }
        if (i == R.id.plus_volume){
            currVolume+=5;
            setVolume(currVolume);
        }
    }

    /**
     * 静音开关
     */
    private void mute() {
        // 先获取当前是否静音
        isMute = ControlManager.getInstance().isMute();
        ControlManager.getInstance().muteCast(!isMute, new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setMute(!isMute);
                if (isMute) {
                    if (currVolume == 0) {
                        currVolume = defaultVolume;
                    }
                    setVolume(currVolume);
                }
                // 这里是根据之前的状态判断的
                if (isMute) {
                    volumeView.setText("声音");
                } else {
                    volumeView.setText("静音");
                }
            }

            @Override
            public void onError(int code, String msg) {
               // showToast(String.format("Mute cast failed %s", msg));
            }
        });
    }

    /**
     * 设置音量大小
     */
    private void setVolume(int volume) {
        currVolume = volume;
        ControlManager.getInstance().setVolumeCast(volume, new ControlCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int code, String msg) {
                //showToast(String.format("音量设置无效，请重试 %s", msg));
            }
        });
    }


    /**
     * 播放开关
     */
    private void play() {
        if (ControlManager.getInstance().getState() == ControlManager.CastState.STOPED) {
            if (localItem != null) {
                newPlayCastLocalContent();
            } else {
                newPlayCastRemoteContent();
            }
        } else if (ControlManager.getInstance().getState() == ControlManager.CastState.PAUSED) {
            playCast();
        } else if (ControlManager.getInstance().getState() == ControlManager.CastState.PLAYING) {
            pauseCast();
        } else {
            Toast.makeText(getBaseContext(), "正在连接设备，稍后操作", Toast.LENGTH_SHORT).show();
        }
    }

    private void stop() {
        ControlManager.getInstance().unInitScreenCastCallback();
        stopCast();
    }

    private void newPlayCastLocalContent() {
        ControlManager.getInstance().setState(ControlManager.CastState.TRANSITIONING);
        ControlManager.getInstance().newPlayCast(localItem, new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);
                ControlManager.getInstance().initScreenCastCallback();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playView.setSelected(true);
                        Toast.makeText(MediaPlayActivity.this, "播放已开始", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
                showToast(String.format("没有连接到设备", msg));
            }
        });
    }

    private void newPlayCastRemoteContent() {
        ControlManager.getInstance().setState(ControlManager.CastState.TRANSITIONING);
        ControlManager.getInstance().newPlayCast(remoteItem, new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);
                ControlManager.getInstance().initScreenCastCallback();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playView.setSelected(true);
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
                showToast(String.format("没有连接到设备", msg));
            }
        });
    }

    private void playCast() {
        ControlManager.getInstance().playCast(new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playView.setSelected(true);
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                showToast(String.format("播放失败 %s", msg));
            }
        });
    }

    private void pauseCast() {
        ControlManager.getInstance().pauseCast(new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.PAUSED);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playView.setSelected(false);
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                showToast(String.format("暂停失败 %s", msg));
            }
        });
    }

    private void stopCast() {
        ControlManager.getInstance().stopCast(new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playView.setSelected(false);
                        finish();
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                showToast(String.format("停止投屏失败 %s", msg));
            }
        });
    }

    /**
     * 改变投屏进度
     */
    private void seekCast(int progress) {
        String target = VMDate.toTimeString(progress);
        ControlManager.getInstance().seekCast(target, new ControlCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int code, String msg) {
                showToast(String.format("更新进度失败 %s", msg));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(ControlEvent event) {
        AVTransportInfo avtInfo = event.getAvtInfo();
        if (avtInfo != null) {
            if (!TextUtils.isEmpty(avtInfo.getState())) {
                if (avtInfo.getState().equals("TRANSITIONING")) {
                    ControlManager.getInstance().setState(ControlManager.CastState.TRANSITIONING);
                } else if (avtInfo.getState().equals("PLAYING")) {
                    ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);
                    playView.setSelected(true);
                } else if (avtInfo.getState().equals("PAUSED_PLAYBACK")) {
                    ControlManager.getInstance().setState(ControlManager.CastState.PAUSED);
                    playView.setSelected(false);
                } else if (avtInfo.getState().equals("STOPPED")) {
                    ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
                    playView.setSelected(false);

                    finish();
                } else {
                    ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
                    playView.setSelected(false);
//
//                  finish();
                }
            }
            if (!TextUtils.isEmpty(avtInfo.getMediaDuration())) {
                playMaxTimeView.setText(avtInfo.getMediaDuration());
            }
            if (!TextUtils.isEmpty(avtInfo.getTimePosition())) {
                long progress = VMDate.fromTimeString(avtInfo.getTimePosition());
                progressSeekbar.setProgress((int) progress);
                playTimeView.setText(avtInfo.getTimePosition());
            }
        }

        RenderingControlInfo rcInfo = event.getRcInfo();
        if (rcInfo != null && ControlManager.getInstance()
                .getState() != ControlManager.CastState.STOPED) {
            if (rcInfo.isMute() || rcInfo.getVolume() == 0) {
                volumeView.setText("静音");
                ControlManager.getInstance().setMute(true);
            } else {
                volumeView.setText("声音");
                ControlManager.getInstance().setMute(false);
            }
            volumeSeekbar.setProgress(rcInfo.getVolume());
        }
    }

    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
