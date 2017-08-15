package com.cpxiao.setpins.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cpxiao.R;
import com.cpxiao.gamelib.activity.BaseActivity;
import com.cpxiao.zads.ZAdManager;
import com.cpxiao.zads.core.ZAdPosition;


/**
 * HomeActivity
 *
 * @author cpxiao on 2017/08/10.
 */
public class HomeActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ZAdManager.getInstance().init(getApplicationContext());

        initAds(ZAdPosition.POSITION_HOME);

        initFbInterstitialAd("1579509002351231_1718445338457596");
        //        initAdMobInterstitialAd( "ca-app-pub-4157365005379790/3212784412");

        Button showAdsBtn = (Button) findViewById(R.id.show_ads_btn);
        showAdsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdMobInterstitialAd();
                showFbInterstitialAd();
            }
        });
    }

    @Override
    protected void onDestroy() {
        ZAdManager.getInstance().destroyAllPosition(this);
        super.onDestroy();
    }
}
