package com.cpxiao.zads.ads;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cpxiao.zads.ads.core.Advertisement;
import com.cpxiao.zads.ads.core.BaseZAd;
import com.cpxiao.zads.core.AdConfigBean;
import com.cpxiao.zads.core.ZAdSize;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import java.util.Queue;


/**
 * @author cpxiao on 2017/08/10.
 *         备注：fb的广告的laod可能会长时间卡住，什么回调都不会执行。
 */
public class FbBannerAd extends BaseZAd {
    private static final String TAG = "FbBannerAd";

    private AdView mAdManager;

    public FbBannerAd(AdConfigBean advertiser) {
        super(advertiser);
    }


    @Override
    public void loadAd(final Context c, final Queue<Advertisement> next) {
        //参数校验
        if (TextUtils.isEmpty(mPlaceId)) {
            if (DEBUG) {
                String msg = "loadAd: param error!";
                Log.d(TAG, msg);
                throw new IllegalArgumentException(msg);
            }
            return;
        }

        if (DEBUG) {
            Log.d(TAG, "loadAd: mPublishId = " + mPublishId + ", mPlaceId = " + mPlaceId);
        }
        if (mAdManager != null) {
            mAdManager.destroy();
            mAdManager = null;
        }
        mAdManager = new AdView(c, mPlaceId, getAdSize(mAdSize));
        mAdManager.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                String msg = "getErrorCode = " + adError.getErrorCode() + ", getErrorMessage = " + adError.getErrorMessage();
                if (DEBUG) {
                    Log.d(TAG, "onError: mPlaceId = " + mPlaceId + ", msg = " + msg);
                }
                onLoadZAdFail(get(), msg, next);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (DEBUG) {
                    Log.d(TAG, "onAdLoaded: mPlaceId = " + mPlaceId);
                }
                mLastAdView = generateView(c, mAdManager, mAdSize);
                onLoadZAdSuccess(get());
            }

            @Override
            public void onAdClicked(Ad ad) {
                if (DEBUG) {
                    Log.d(TAG, "onAdClicked: ");
                }
                if (mZAdListener != null) {
                    mZAdListener.onAdClick(get());
                }
            }
        });
        if (DEBUG) {
            AdSettings.addTestDevice("5abafbb8d38c6037c32c4652b08d1feb");// 20170810 HUAWEI G621-TL00
            AdSettings.addTestDevice("4cc7bc8aabecb9642ff2e06d6bcebac4");// 20170810 模拟器Nexus_5X_API_25_7.1.1
        }
        mAdManager.loadAd();
    }


    @Override
    public void destroyAllView() {
        super.destroyAllView();

        removeFromParent(mAdManager);
        if (mAdManager != null) {
            mAdManager.destroy();
            mAdManager = null;
        }
    }

    private View generateView(Context c, AdView bannerView, int size) {
        if (c == null || bannerView == null) {
            return null;
        }
        removeFromParent(bannerView);

        if (size == ZAdSize.BANNER_300X250) {
            LinearLayout view = new LinearLayout(c);
            view.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dip2px(250));
            view.addView(bannerView, params);
            return view;
        } else {
            if (size != ZAdSize.BANNER_320X50) {
                if (DEBUG) {
                    throw new IllegalArgumentException("No Size found in " + TAG);
                }
            }
            LinearLayout view = new LinearLayout(c);
            view.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dip2px(50));
            view.addView(bannerView, params);
            return view;
        }
    }

    private AdSize getAdSize(int size) {
        if (size == ZAdSize.BANNER_300X250) {
            return AdSize.RECTANGLE_HEIGHT_250;
        } else if (size == ZAdSize.BANNER_320X50) {
            return AdSize.BANNER_HEIGHT_50;
        } else {
            if (DEBUG) {
                throw new IllegalArgumentException("No Size found in " + TAG);
            }
            return AdSize.BANNER_HEIGHT_50;
        }
    }

    @Override
    public String toString() {
        return TAG;
    }

}
