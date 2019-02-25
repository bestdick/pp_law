package com.storyvendingmachine.www.pp_law;

import android.app.Activity;
import android.app.Application;

import com.kakao.auth.KakaoSDK;

/**
 * Created by Administrator on 2018-03-04.
 */

public class GlobalApplication extends Application {



    private static volatile GlobalApplication obj = null;
    private static volatile Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        obj = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    public static GlobalApplication getGlobalApplicationContext() {
        return obj;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    // Activity가 올라올때마다 Activity의 onCreate에서 호출해줘야한다.
    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }





    //    private class KakaoSDKAdapter extends KakaoAdapter {
//        /**
//         * Session Config에 대해서는 default값들이 존재한다.
//         * 필요한 상황에서만 override해서 사용하면 됨.
//         * @return Session의 설정값.
//         */
//        @Override
//        public ISessionConfig getSessionConfig() {
//            return new ISessionConfig() {
//                @Override
//                public AuthType[] getAuthTypes() {
//                    return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
//                }
//
//                @Override
//                public boolean isUsingWebviewTimer() {
//                    return false;
//                }
//
//                @Override
//                public boolean isSecureMode() {
//                    return false;
//                }
//
//                @Override
//                public ApprovalType getApprovalType() {
//                    return ApprovalType.INDIVIDUAL;
//                }
//
//                @Override
//                public boolean isSaveFormData() {
//                    return true;
//                }
//            };
//        }
//
//        @Override
//        public IApplicationConfig getApplicationConfig() {
//            return new IApplicationConfig() {
//                @Override
//                public Context getApplicationContext() {
//                    return GlobalApplication.this.getApplicationContext();
//                }
//            };
//        }
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        KakaoSDK.init(new KakaoSDKAdapter());
//    }
}
