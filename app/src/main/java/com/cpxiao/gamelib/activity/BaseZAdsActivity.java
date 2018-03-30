package com.cpxiao.gamelib.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.cpxiao.AppConfig;
import com.cpxiao.R;
import com.cpxiao.zads.ZAdManager;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.umeng.analytics.MobclickAgent;


/**
 * @author cpxiao on 2017/03/01.
 * @version cpxiao on 2017/08/10 改成使用ZAdsSdk.
 */
public class BaseZAdsActivity extends Activity {
    protected static final boolean DEBUG = AppConfig.DEBUG;
    protected final String TAG = getClass().getSimpleName();

    private InterstitialAd mAdMobInterstitialAd;
    private com.facebook.ads.InterstitialAd mFbInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //隐藏状态栏部分（电池电量、时间等部分）
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        if (mFbInterstitialAd != null) {
            mFbInterstitialAd.destroy();
            mFbInterstitialAd = null;
        }
        super.onDestroy();
    }

    protected void initAds(int zAdPosition) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_ads);
        ZAdManager.getInstance().loadAd(getApplicationContext(), zAdPosition, layout);
    }

    protected void initAdMobInterstitialAd(String unitId) {
        if (TextUtils.isEmpty(unitId)) {
            if (DEBUG) {
                throw new IllegalArgumentException("param error!");
            }
            return;
        }
        mAdMobInterstitialAd = new InterstitialAd(this);
        mAdMobInterstitialAd.setAdUnitId(unitId);

        mAdMobInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if (DEBUG) {
                    Log.d(TAG, "onAdLoaded: ");
                    showAdMobInterstitialAd();
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                if (DEBUG) {
                    Log.d(TAG, "onAdFailedToLoad: ");
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                if (DEBUG) {
                    Log.d(TAG, "onAdOpened: ");
                }
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                if (DEBUG) {
                    Log.d(TAG, "onAdLeftApplication: ");
                }
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                if (DEBUG) {
                    Log.d(TAG, "onAdClosed: ");
                }
                loadAdMobInterstitialAd();
            }
        });

        loadAdMobInterstitialAd();
    }

    private void loadAdMobInterstitialAd() {
        if (DEBUG) {
            Log.d(TAG, "loadAdMobInterstitialAd: ");
        }
        if (mAdMobInterstitialAd != null && !mAdMobInterstitialAd.isLoading()) {
            AdRequest adRequest;
            if (DEBUG) {
                adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)// All emulators
                        .addTestDevice("67F59060394DB36B95B18F5EE5B5D735")
                        .build();
            } else {
                adRequest = new AdRequest.Builder()
                        .build();
            }
            mAdMobInterstitialAd.loadAd(adRequest);
        }
    }

    protected void showAdMobInterstitialAd() {
        if (mAdMobInterstitialAd != null) {
            if (mAdMobInterstitialAd.isLoaded()) {
                mAdMobInterstitialAd.show();
            } else {
                loadAdMobInterstitialAd();
            }
        }
    }

    protected void initFbInterstitialAd(String placeId) {
        if (TextUtils.isEmpty(placeId)) {
            if (DEBUG) {
                throw new IllegalArgumentException("param error!");
            }
            return;
        }
        mFbInterstitialAd = new com.facebook.ads.InterstitialAd(this, placeId);
        mFbInterstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                if (DEBUG) {
                    Log.d(TAG, "onInterstitialDisplayed: ");
                }
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                if (DEBUG) {
                    Log.d(TAG, "onInterstitialDismissed: ");
                }
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                if (DEBUG) {
                    Log.d(TAG, "onError: " + adError.getErrorCode() + adError.getErrorMessage());
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (DEBUG) {
                    Log.d(TAG, "onAdLoaded: ");
                    showFbInterstitialAd();
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                if (DEBUG) {
                    Log.d(TAG, "onAdClicked: ");
                }
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                if (DEBUG) {
                    Log.d(TAG, "onLoggingImpression: ");
                }
            }
        });

        loadFbInterstitialAd();
    }

    private void loadFbInterstitialAd() {
        if (DEBUG) {
            Log.d(TAG, "loadFbInterstitialAd: ");
        }
        if (mFbInterstitialAd != null) {
            if (DEBUG) {
                //                AdSettings.addTestDevice("");
            }
            mFbInterstitialAd.loadAd();
        }
    }

    protected void showFbInterstitialAd() {
        if (mFbInterstitialAd != null) {
            if (mFbInterstitialAd.isAdLoaded()) {
                mFbInterstitialAd.show();
            } else {
                loadFbInterstitialAd();
            }
        }
    }


}
