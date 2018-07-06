package com.ulling.oneday.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

/**
 * 파일유틸
 */
public class FileUtils {

    /**
     * 저장할 이미지 파일 생성
     * 서버에 전송할 이미지 파일
     *
     * @param context
     * @return
     * @throws IOException
     */
    public synchronized static File createImageFile(Context context) throws IOException {
//        String timeStamp = new SimpleDateFormat("yyMMdd_HHmm", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + System.currentTimeMillis();
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File uploadFile = File.createTempFile(imageFileName, ".png", storageDir);
//        uploadFilePath = uploadFile.getAbsolutePath();
        return uploadFile;
    }


    public static String getPathFromUri(Context context, Uri uri) {

        Cursor cursor = context.getContentResolver()
                .query(uri, null, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex("_data"));

        cursor.close();
        return path;

    }


    public static Uri getUriFromPath(Context context, String path, String fileName) {

        Uri fileUri = Uri.parse(path + fileName);
        String filePath = fileUri.getPath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePath + "'", null, null);

        cursor.moveToNext();
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

        return uri;
    }
}
