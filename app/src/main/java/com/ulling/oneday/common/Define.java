package com.ulling.oneday.common;


public class Define {
    public static final int COMMON_NOTIFICATION_ID = 1000;
    /**
     * 인트로 핸들러
     */
    public static final int INIT_APP_VERSION_CHECK = 10;
    public static final int INIT_APP_NOTI_CHECK = 20;
    public static final int INIT_SPLASH = 30;

    /**
     * 액티비티
     */
    public static final int MOVE_MAIN_ACTIVITY = 1000;
    public static final int MOVE_CHOOSE_IMG_ACTIVITY = 1500;

    /**
     * 액티비티 intent
     */
    public static final String INTENT_NOTI_ULLING = "INTENT_NOTI_ULLING";
    public static final String INTENT_TITLE = "INTENT_TITLE";
    public static final String INTENT_URL = "INTENT_URL";
    public static final String INTENT_IMG_DOWNLOAD_RESULT = "INTENT_IMG_DOWNLOAD_RESULT";
    public static final String INTENT_IMG_FILE_ABSOLUTE_PATH = "INTENT_IMG_FILE_ABSOLUTE_PATH";

    /**
     * 파이어베이스 데이터
     */
    // root
    public static final String FIREBASE_DATA_ONEDAY_ROOT = "onedayRoot";

    public static final String FIREBASE_DATA_COLLECTION_ONEDAY = "oneday";
    public static final String FIREBASE_DATA_DOCUMENT_ADMIN = "admin";

    public static final String FIREBASE_DATA_COLLECTION_APP_VERSION = "appVersion";
    public static final String FIREBASE_DATA_COLLECTION_NOTI_OF_ULLING = "notiOfUlling";

    // 게시글
    public static final String FIREBASE_COLLECTION_CONTENTS = "contents";
    // 댓글
    public static final String FIREBASE_COLLECTION_COMMENTS = "comments";
    // 광고
    public static final String FIREBASE_COLLECTION_AD = "ad";

    /**
     * 컬럼들
     */
//    public static final String FIREBASE_COLUMN_ALL = "_ALL";
    /**
     * 파이어베이스 Storage
     */
    // root
    public static final String FIREBASE_STORAGE_DOODLE_ROOT = "oneday";
    /**
     * 프리퍼런스
     */
    public static final String PRE_ULLING_NOTI = "PRE_ULLING_NOTI";
    public static final String PRE_BEST_PICTURE_SIZE = "PRE_BEST_PICTURE_SIZE";


    public static final int DEFAULT_PAGE = 10;

}
