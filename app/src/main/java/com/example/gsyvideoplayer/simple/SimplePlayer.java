package com.example.gsyvideoplayer.simple;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.gsyvideoplayer.R;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.Arrays;

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 横屏不旋转的 Demo
 */
public class SimplePlayer extends AppCompatActivity {

    StandardGSYVideoPlayer videoPlayer;

    OrientationUtils orientationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_play);
        init();
    }

    private void init() {
        GSYVideoType.setRenderType(GSYVideoType.SUFRACE);
        //ijk关闭log
        IjkPlayerManager.setLogLevel(IjkMediaPlayer.IJK_LOG_SILENT);
        //EXOPlayer内核，支持格式更多
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);
        int maxSize = 5000 * 1024;
        VideoOptionModel[] optionArr = new VideoOptionModel[]{
            new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "reconnect", 5),

            new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "no-time-adjust", 1),
            new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0),
            new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0),
            new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 0),
//
            new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1),
            new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0),
            new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 5),

            new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100),
            new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 10240),
            new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT,"analyzeduration",1),

            new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1),
//                new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp"),
            new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"max-buffer-size",maxSize),
//                new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-frames", 100)
            //指定位置播放 但是会黑很久 而且位置也不对
//                new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "seek-at-start", 6000)
//                new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"enable-accurate-seek",1),
            new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"mediacodec",1),

        };
        GSYVideoManager.instance().setOptionModelList(Arrays.asList(optionArr));

        videoPlayer =  (StandardGSYVideoPlayer)findViewById(R.id.video_player);



        //String source1 = "http://10.1.1.188:9901/tsfile/live/0001_1.m3u8";
//        String source1 = "rtsp://admin:lonbon1997@192.168.4.49:554/Streaming/Channels/101";
//        String source1 = "rtsp://192.168.3.219:8554/lonbon";
//        String source1 = "file:///sdcard/test.mkv";
        String source1 = "rtmp://192.168.4.239:1935/live/demo10";
        videoPlayer.setUp(source1, true, "测试视频");

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.xxx1);
        videoPlayer.setThumbImageView(imageView);
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ------- ！！！如果不需要旋转屏幕，可以不调用！！！-------
                // 不需要屏幕旋转，还需要设置 setNeedOrientationUtils(false)
                //orientationUtils.resolveByClick();
                finish();
            }
        });
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ///不需要屏幕旋转
        videoPlayer.setNeedOrientationUtils(false);
        videoPlayer.startPlayLogic();
    }


    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
///       不需要回归竖屏
//        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            videoPlayer.getFullscreenButton().performClick();
//            return;
//        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }
}
