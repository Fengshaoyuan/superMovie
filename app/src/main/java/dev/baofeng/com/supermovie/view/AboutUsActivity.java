package dev.baofeng.com.supermovie.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.baofeng.com.supermovie.R;
import dev.baofeng.com.supermovie.http.UrlConfig;

/**
 * Created by huangyong on 2018/2/23.
 */

public class AboutUsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.about_content)
    WebView content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        content.setWebChromeClient(new WebChromeClient());
        content.setWebViewClient(new WebViewClient());

        content.loadUrl(UrlConfig.ABOUT_US);

        toolbar.setNavigationOnClickListener((View view) ->
                finish()
        );
    }

    public void payfordever(View view) {
        Intent intent = new Intent(AboutUsActivity.this, PayMeActivity.class);
        startActivity(intent);
    }
}
