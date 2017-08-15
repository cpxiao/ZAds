package com.cpxiao.zads.ads;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cpxiao.R;
import com.cpxiao.zads.ads.core.Advertisement;
import com.cpxiao.zads.ads.core.BaseZAd;
import com.cpxiao.zads.core.AdConfigBean;
import com.cpxiao.zads.core.ZAdSize;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.Queue;


/**
 * @author cpxiao on 2017/08/12.
 */
public class AdMobNativeExpressAd extends BaseZAd {
    private static final String TAG = "AdMobNativeExpressAd";

    private NativeExpressAdView mAdManager;

    public AdMobNativeExpressAd(AdConfigBean advertiser) {
        super(advertiser);
    }


    @Override
    public void loadAd(final Context c, final Queue<Advertisement> next) {
        //注意：参数配置在xml文件中了
        if (DEBUG) {
            Log.d(TAG, "loadAd: mPublishId = " + mPublishId + ", mPlaceId = " + mPlaceId);
        }
        View view = LayoutInflater.from(c).inflate(R.layout.layout_ads_admob_native_fullwidthx100_home, null, true);
        mAdManager = (NativeExpressAdView) view.findViewById(R.id.adView);

        mAdManager.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (DEBUG) {
                    Log.d(TAG, "onAdClosed: ");
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                String msg = "ErrorCode = " + i;
                if (DEBUG) {
                    Log.d(TAG, "onAdFailedToLoad: mPlaceId = " + mPlaceId + ", msg = " + msg);
                }
                onLoadZAdFail(get(), msg, next);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                if (DEBUG) {
                    Log.d(TAG, "onAdLeftApplication: ");
                }
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                if (DEBUG) {
                    Log.d(TAG, "onAdOpened: ");
                }
                if (mZAdListener != null) {
                    mZAdListener.onAdClick(get());
                }
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (DEBUG) {
                    Log.d(TAG, "onAdLoaded: mPlaceId = " + mPlaceId);
                }
                mLastAdView = generateView(c, mAdManager, mAdSize);
                onLoadZAdSuccess(get());
            }
        });
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
        mAdManager.loadAd(adRequest);
    }


    public View getLastAdView() {
        removeFromParent(mLastAdView);
        return mLastAdView;
    }

    @Override
    public void destroyAllView(Context context) {
        super.destroyAllView(context);

        removeFromParent(mAdManager);
        if (mAdManager != null) {
            mAdManager.destroy();
            mAdManager = null;
        }

    }

    private View generateView(Context c, NativeExpressAdView bannerView, int size) {
        if (c == null || bannerView == null) {
            return null;
        }
        removeFromParent(bannerView);
        if (size == ZAdSize.BANNER_320X50) {
            return createView(c, bannerView, 50);
        } else if (size == ZAdSize.BANNER_320X100) {
            return createView(c, bannerView, 100);
        } else if (size == ZAdSize.BANNER_300X250) {
            return createView(c, bannerView, 250);
        } else {
            if (DEBUG) {
                throw new IllegalArgumentException("No Size found in " + TAG);
            }
            return createView(c, bannerView, 50);
        }
    }

    private View createView(Context c, NativeExpressAdView bannerView, int height) {
        LinearLayout view = new LinearLayout(c);
        view.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dip2px(height));
        view.addView(bannerView, params);
        return view;
    }

    @Override
    public String toString() {
        return TAG;
    }

}
