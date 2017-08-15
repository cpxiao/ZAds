package com.cpxiao.zads.ads;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cpxiao.R;
import com.cpxiao.zads.ads.core.Advertisement;
import com.cpxiao.zads.ads.core.BaseZAd;
import com.cpxiao.zads.core.AdConfigBean;
import com.cpxiao.zads.core.ZAdSize;
import com.cpxiao.zads.views.ZBannerView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author cpxiao on 2017/08/10.
 */
public class FbNativeAd extends BaseZAd {
    private static final String TAG = "FbNativeAd";

    private NativeAdsManager mAdManager;

    public FbNativeAd(AdConfigBean advertiser) {
        super(advertiser);
    }

    @Override
    public void loadAd(final Context c, final Queue<Advertisement> next) {
        //参数校验
        if (TextUtils.isEmpty(mPlaceId)) {
            if (DEBUG) {
                String msg = "loadAd() error!  param error!";
                Log.d(TAG, msg);
                throw new IllegalArgumentException(msg);
            }
            return;
        }

        if (DEBUG) {
            Log.d(TAG, "load: mPlaceId = " + mPlaceId);
        }
        mAdManager = new NativeAdsManager(c, mPlaceId, 1);
        mAdManager.setListener(new NativeAdsManager.Listener() {
            @Override
            public void onAdsLoaded() {
                for (int i = 0; i < mAdManager.getUniqueNativeAdCount(); i++) {
                    NativeAd nativeAd = mAdManager.nextNativeAd();
                    mLastAdView = generateView(c, nativeAd);
                }
                onLoadZAdSuccess(get());
            }

            @Override
            public void onAdError(AdError adError) {
                onLoadZAdFail(get(), adError.getErrorMessage(), next);
            }
        });
        if (DEBUG) {
            //            AdSettings.addTestDevice("e6298923190b4e7e7119e0f14c44f097");

            // 如果想要添加多台测试设备，只需创建一个字符串列表，添加到加载广告前的位置：
            List<String> testDevices = new ArrayList<>();
            testDevices.add("37d6f63e9a828b876eda13efa16483c8");
            AdSettings.addTestDevices(testDevices);
        }
        mAdManager.loadAds();
    }

    @Override
    public void destroyAllView(Context context) {
        if (mLastAdView != null) {
            NativeAd ad = (NativeAd) mLastAdView.getTag(R.id.tag_info);
            if (ad != null) {
                ad.unregisterView();
            }
            mLastAdView = null;
        }

        if (mAdManager != null) {
            mAdManager.setListener(null);
            mAdManager = null;
        }
    }

    @Override
    public View getLastAdView() {
        if (mLastAdView != null) {
            NativeAd ad = (NativeAd) mLastAdView.getTag(R.id.tag_info);
            if (ad != null) {
                ad.registerViewForInteraction(mLastAdView);
            }
        }
        return mLastAdView;
    }

    private View generateView(Context c, NativeAd ad) {
        String picture = null;
        String icon = null;
        try {
            picture = ad.getAdCoverImage().getUrl();
        } catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
            }
        }
        try {
            icon = ad.getAdIcon().getUrl();
        } catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
            }
        }
        if (DEBUG) {
            Log.d(TAG, "generateView: picture = " + picture);
            Log.d(TAG, "generateView: icon = " + icon);
        }
        if (mAdSize == ZAdSize.BANNER_320X50) {
            ZBannerView view = new ZBannerView(c);
            view.bindData(c, icon, ad.getAdTitle(), ad.getAdSubtitle());
            view.setTag(R.id.tag_info, ad);
            return view;
        } else {
            if (TextUtils.isEmpty(picture)) {
                return null;
            }
            ImageView view = new ImageView(c);
            Glide.with(c).load(picture).into(view);
            view.setTag(R.id.tag_info, ad);
            return view;
        }
    }

    @Override
    public String toString() {
        return TAG;
    }

}
