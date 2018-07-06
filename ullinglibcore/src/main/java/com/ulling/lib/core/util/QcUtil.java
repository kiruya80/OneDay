package com.ulling.lib.core.util;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import static android.content.Context.WINDOW_SERVICE;

public class QcUtil {

//    public void sendSMS(String coinSymbol) {
//        String smsNum = "010-3109-3050";
//        String smsText = coinSymbol + " 원화 상장 시그널 포착!";
//
//        if (smsNum.length() > 0 && smsText.length() > 0) {
//            sendSMS(smsNum, smsText);
//        }
//    }
//
//    private void sendSMS(String phoneNumber, String message) {
//        SmsManager sms = SmsManager.getDefault();
//        sms.sendTextMessage(phoneNumber, null, message, null, null);
//    }


    public static void hiddenSoftKey(Context c, EditText editText) {
        if (editText == null)
            return;
        editText.clearFocus();
        InputMethodManager im = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showSoftKey(Context c, EditText editText) {
        if (editText == null)
            return;
        editText.requestFocus();
        InputMethodManager im = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 소프트키 유무체크
     *
     * @param context
     * @return
     */
    public static boolean hasSoftMenu(Context context) {
        //메뉴버튼 유무
        boolean hasMenuKey = ViewConfiguration.get(context.getApplicationContext()).hasPermanentMenuKey();

        //뒤로가기 버튼 유무
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

        // lg폰 소프트키일 경우
// 삼성폰 등.. 메뉴 버튼, 뒤로가기 버튼 존재
        QcLog.e("hasSoftMenu ===== hasMenuKey = " + hasMenuKey + " , hasBackKey = " + hasBackKey);
//        return !hasMenuKey && !hasBackKey;
        if (!hasMenuKey && !hasBackKey) { // lg폰 소프트키일 경우
            return true;
        } else { // 삼성폰 등.. 메뉴 버튼, 뒤로가기 버튼 존재
            return false;
        }
    }


    /**
     * 소프트키 높이 가져오기
     *
     * @param context
     * @return
     */
    public static int getSoftMenuHeight(Context context) {
        if (!hasSoftMenu(context)) {
            QcLog.e("hasSoftMenu !hasSoftMenu(context =====  = ");
            return 0;
        }
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int softKeyHeight = 0;
        if (resourceId > 0) {
            softKeyHeight = resources.getDimensionPixelSize(resourceId);
        }
        return softKeyHeight;
    }

    public static float getRatioLength(float length, float ratioWidth, float ratioHeight) {
        return length * ratioHeight / ratioWidth;

    }

    public static int getPixelToDp(Context context, float dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
        return px;
    }

    public static int getStatusBarHeight(final Context context) {
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        } else {
            return (int) Math.ceil((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25) *
                    resources.getDisplayMetrics().density);
        }
    }

    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean isTablet(Context context) {
        boolean bTablet = false;
        int screenSizeType = context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenSizeType) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                bTablet = true;
                break;
            default:
                break;
        }

        return bTablet;
    }

    public static int getScreenOrientation(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (context instanceof Activity) {
            ((Activity) context).getWindowManager()
                    .getDefaultDisplay().getMetrics(displayMetrics);

        } else if (context instanceof Application) {
            WindowManager windowManager = (WindowManager) context.getSystemService
                    (WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        } else if (context instanceof Service) {
            WindowManager windowManager = (WindowManager) context.getSystemService
                    (WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }

        return (displayMetrics.widthPixels < displayMetrics.heightPixels) ?
                Configuration.ORIENTATION_PORTRAIT : Configuration.ORIENTATION_LANDSCAPE;
    }


    public static boolean isIntegerFromStr(String num) {
        try {
            Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * Statusbar Drawable 및 Icon 밝기 적용
     *
     * @param activity
     * @param drawableId
     */
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public static void setStatusBarDrawable(Activity activity, int drawableId, boolean isDark, boolean isFullLayout) {
//        Window window = activity.getWindow();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//            if (isFullLayout) {
//                window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            }
//
//            Drawable background = activity.getResources().getDrawable(drawableId);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(activity.getResources().getColor(R.color.transparent));
//            window.setNavigationBarColor(activity.getResources().getColor(R.color.transparent));
//            window.setBackgroundDrawable(background);
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            View decor = window.getDecorView();
//            if (isDark) {
//                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            } else {
//                decor.setSystemUiVisibility(0);
//            }
//        }
//    }

    /**
     * Status bar color 및 Icon 밝기 적용
     */
    public static void setChangeStatusBar(Activity act, int colorId, boolean isDark) {
        Window window = act.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(act, colorId));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = window.getDecorView();
            if (isDark) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decor.setSystemUiVisibility(0);
            }
        }
    }


//    public static int getColor(Context context, int id) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return context.getResources().getColor(id);
//        } else {
//            return context.getColor(id);
//        }
//    }
//
//    public static Drawable getDrawable(Context context, int id) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            return context.getResources().getDrawable(id);
//        } else {
//            return context.getDrawable(id);
//        }
//    }

    public static final String PLAY_STORE_MARKET_DETAIL = "market://details?id=";
    public static final String PLAY_STORE_MARKET_DETAIL_APPS = "https://play.google.com/store/apps/details?id=";

    /**
     * 구글 스토어 이동
     *
     * @param activity
     */
    public static void startGoogleStore(Activity activity) {
        final String appPackageName = activity.getPackageName();
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_MARKET_DETAIL + appPackageName));
            activity.startActivity(intent);
            activity.finish();
        } catch (android.content.ActivityNotFoundException anfe) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_MARKET_DETAIL_APPS + appPackageName));
            activity.startActivity(intent);
            activity.finish();
        }
    }

    /**
     * 패키지 이름으로 구글 스토어 이동
     *
     * @param activity
     * @param appPackageName
     */
    public static void startGoogleStore(Activity activity, String appPackageName) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_MARKET_DETAIL + appPackageName));
            activity.startActivity(intent);

        } catch (android.content.ActivityNotFoundException anfe) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_MARKET_DETAIL_APPS + appPackageName));
            activity.startActivity(intent);
        }
    }

    public static void startGoogleStore(Context context, String appPackageName) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_MARKET_DETAIL + appPackageName));
            context.startActivity(intent);

        } catch (android.content.ActivityNotFoundException anfe) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_MARKET_DETAIL_APPS + appPackageName));
            context.startActivity(intent);
        }
    }
}
