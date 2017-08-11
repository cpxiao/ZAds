//package com.cpxiao.zads.ads;
//
//import android.content.Context;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//
//import com.cpxiao.zads.ads.core.Advertisement;
//import com.cpxiao.zads.ads.core.BaseZAd;
//import com.cpxiao.zads.core.AdConfigBean;
//import com.cpxiao.zads.core.ZAdSize;
//import com.google.android.gms.ads.AdSize;
//import com.inmobi.ads.InMobiAdRequestStatus;
//import com.inmobi.ads.InMobiBanner;
//
//import java.util.Map;
//import java.util.Queue;
//
//
///**
// * @author cpxiao on 2017/08/10.
// */
//public class InMobiBannerAd extends BaseZAd {
//    private static final String TAG = "InMobiBannerAd";
//
//    private InMobiBanner mAdManager;
//
//    public InMobiBannerAd(AdConfigBean advertiser) {
//        super(advertiser);
//    }
//
//
//    @Override
//    public void loadAd(final Context c, final Queue<Advertisement> next) {
//        //参数校验
//        if (TextUtils.isEmpty(mPlaceId)) {
//            if (DEBUG) {
//                String msg = "loadAd: param error!";
//                Log.d(TAG, msg);
//                throw new IllegalArgumentException(msg);
//            }
//            return;
//        }
//
//        if (DEBUG) {
//            Log.d(TAG, "loadAd: mPublishId = " + mPublishId + ", mPlaceId = " + mPlaceId);
//        }
//        if (mAdManager != null) {
//            mAdManager = null;
//        }
//        mAdManager = new InMobiBanner(c, Long.valueOf(mPlaceId));
//        mAdManager.setListener(new InMobiBanner.BannerAdListener() {
//            @Override
//            public void onAdLoadSucceeded(InMobiBanner inMobiBanner) {
//                if (DEBUG) {
//                    Log.d(TAG, "onAdLoadSucceeded: mPlaceId = " + mPlaceId);
//                }
//                mLastAdView = generateView(c, mAdManager, mAdSize);
//                onLoadZAdSuccess(get());
//            }
//
//            @Override
//            public void onAdLoadFailed(InMobiBanner inMobiBanner, InMobiAdRequestStatus inMobiAdRequestStatus) {
//                String msg = "ErrorMessage = " + inMobiAdRequestStatus.getMessage();
//                if (DEBUG) {
//                    Log.d(TAG, "onAdFailedToLoad: mPlaceId = " + mPlaceId + ", msg = " + msg);
//                }
//                onLoadZAdFail(get(), msg, next);
//            }
//
//            @Override
//            public void onAdDisplayed(InMobiBanner inMobiBanner) {
//                if (DEBUG) {
//                    Log.d(TAG, "onAdDisplayed: ");
//                }
//            }
//
//            @Override
//            public void onAdDismissed(InMobiBanner inMobiBanner) {
//                if (DEBUG) {
//                    Log.d(TAG, "onAdDismissed: ");
//                }
//            }
//
//            @Override
//            public void onAdInteraction(InMobiBanner inMobiBanner, Map<Object, Object> map) {
//                if (DEBUG) {
//                    Log.d(TAG, "onAdInteraction: ");
//                }
//            }
//
//            @Override
//            public void onUserLeftApplication(InMobiBanner inMobiBanner) {
//                if (DEBUG) {
//                    Log.d(TAG, "onUserLeftApplication: ");
//                }
//            }
//
//            @Override
//            public void onAdRewardActionCompleted(InMobiBanner inMobiBanner, Map<Object, Object> map) {
//                if (DEBUG) {
//                    Log.d(TAG, "onAdRewardActionCompleted: ");
//                }
//            }
//        });
//        mAdManager.load();
//    }
//
//
//    public View getLastAdView() {
//        removeFromParent(mLastAdView);
//        return mLastAdView;
//    }
//
//    @Override
//    public void destroyAllView() {
//        super.destroyAllView();
//
//        removeFromParent(mAdManager);
//        if (mAdManager != null) {
//            mAdManager = null;
//        }
//    }
//
//    private View generateView(Context c, InMobiBanner bannerView, int size) {
//        if (c == null || bannerView == null) {
//            return null;
//        }
//        removeFromParent(bannerView);
//
//        if (size == ZAdSize.BANNER_300X250) {
//            LinearLayout view = new LinearLayout(c);
//            view.setGravity(Gravity.CENTER);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, dip2px(250));
//            view.addView(bannerView, params);
//            return view;
//        } else {
//            if (size != ZAdSize.BANNER_320X50) {
//                if (DEBUG) {
//                    throw new IllegalArgumentException("No Size found in " + TAG);
//                }
//            }
//            LinearLayout view = new LinearLayout(c);
//            view.setGravity(Gravity.CENTER);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, dip2px(50));
//            view.addView(bannerView, params);
//            return view;
//        }
//    }
//
//    private AdSize getAdSize(int size) {
//        if (DEBUG) {
//            Log.d(TAG, "getAdSize: size = " + size);
//        }
//        if (size == ZAdSize.BANNER_300X250) {
//            return AdSize.MEDIUM_RECTANGLE;
//        } else if (size == ZAdSize.BANNER_320X50) {
//            return AdSize.SMART_BANNER;
//        } else {
//            if (DEBUG) {
//                throw new IllegalArgumentException("No Size found in " + TAG);
//            }
//            return AdSize.SMART_BANNER;
//        }
//    }
//
//    @Override
//    public String toString() {
//        return TAG;
//    }
//
//}
