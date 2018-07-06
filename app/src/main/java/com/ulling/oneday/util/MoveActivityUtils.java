package com.ulling.oneday.util;

import android.app.Activity;
import android.content.Intent;

import com.ulling.oneday.R;
import com.ulling.oneday.common.Define;
import com.ulling.oneday.entities.NotiOfUlling;
import com.ulling.oneday.ui.FirstAppActivity;
import com.ulling.oneday.ui.MainActivity;

/**
 * 액티비티 이동 유틸
 */
public class MoveActivityUtils {
    /**
     * 메인 액티비티 이동
     *
     * @param activity
     * @param mNotiOfUlling
     */
    public static void moveMainActivity(Activity activity, NotiOfUlling mNotiOfUlling) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.hold);
    }

    /**
     * 닉네임 초기 설정 이동
     *
     * @param activity
     * @param mNotiOfUlling
     */
    public static void moveFirstAppActivity(Activity activity, NotiOfUlling mNotiOfUlling) {
        Intent intent = new Intent(activity, FirstAppActivity.class);
        intent.putExtra(Define.INTENT_NOTI_ULLING, mNotiOfUlling);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.fade_in, R.anim.hold);
    }
}
