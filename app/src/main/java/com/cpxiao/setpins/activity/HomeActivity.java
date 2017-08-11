package com.cpxiao.setpins.activity;

import android.os.Bundle;

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
        //        initFbAds50("370166039990535_370325199974619");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZAdManager.getInstance().destroyAllPosition();
    }
}
