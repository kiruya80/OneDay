package com.ulling.oneday.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;

import com.ulling.lib.core.common.QcDefine;
import com.ulling.lib.core.util.QcCameraUtils;
import com.ulling.lib.core.util.QcLog;
import com.ulling.lib.core.util.QcPreferences;
import com.ulling.lib.core.util.QcToast;
import com.ulling.oneday.common.Define;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 카메라 갤러리 유틸
 */
public class CameraGalleryUtil {
    private static CameraGalleryUtil SINGLE_U;
    private static final int DEAULT_PICTURE_SIZE = 1080;

    // 업로드 파일
    private File uploadFile = null;
    // 업로드 파일 경로
    private String uploadFilePath = "";
    // 업로드 파일 uri
    private Uri uploadFileUri;
    // 이미지 가로 또는 세로 중 최대 길이
    private int maxPictureSize = 0;

    private int screenWidth;
    private int screenHeight;
    // 해상도에 비례한 사진 사이즈 퍼센트
    private int sizePercent = 80;

    public void setUploadFileUri(Uri uploadFileUri) {
        this.uploadFileUri = uploadFileUri;
    }

    public File getUploadFile() {
        return uploadFile;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public Uri getUploadFileUri() {
        return uploadFileUri;
    }

    public int getMaxPictureSize() {
        return maxPictureSize;
    }

    public static synchronized CameraGalleryUtil getInstance(Activity activity) {
        if (SINGLE_U == null) {
            SINGLE_U = new CameraGalleryUtil(activity);
        }
        return SINGLE_U;
    }

    public CameraGalleryUtil(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        if (screenHeight > screenWidth) {
            maxPictureSize = screenHeight;
        } else {
            maxPictureSize = screenWidth;
        }

        if (maxPictureSize >= DEAULT_PICTURE_SIZE)
            maxPictureSize = DEAULT_PICTURE_SIZE;
        QcLog.e("maxPictureSize === " + maxPictureSize);

        // 업로드 파일
        uploadFile = null;
        // 업로드 파일 경로
        uploadFilePath = "";
        // 업로드 파일 uri
        uploadFileUri = null;
    }


    public void reset() {
        // 업로드 파일
        uploadFile = null;
        // 업로드 파일 경로
        uploadFilePath = "";
        // 업로드 파일 uri
        uploadFileUri = null;
    }


    /**
     * 카메라 열기
     *
     * @param activity
     */
    public synchronized void openCamera(Activity activity) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {

            Camera camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();

            Camera.Size bestSize = QcPreferences.getInstance().get(Define.PRE_BEST_PICTURE_SIZE, Camera.Size.class);
            if (bestSize == null) {
                if (maxPictureSize > DEAULT_PICTURE_SIZE) {
                    screenWidth = screenWidth / 2;
                    screenHeight = screenHeight / 2;
                }
                List<Camera.Size> equalSizeList = QcCameraUtils.getEqualSize(parameters);

                bestSize = QcCameraUtils.getBestDisplayPreviewSize(
                        QcCameraUtils.getDisplayOrientation(activity),
                        screenWidth,
                        screenHeight,
                        sizePercent,
                        equalSizeList);
                QcPreferences.getInstance().put(Define.PRE_BEST_PICTURE_SIZE, bestSize);
            }
            if (bestSize != null) {
                QcLog.e("bestSize ====== Width : " + bestSize.width + ", Height : " + bestSize.height);

                parameters.setPreviewSize(bestSize.width, bestSize.height);
                parameters.setPictureSize(bestSize.width, bestSize.height);
                camera.setParameters(parameters);
            }
            camera.release();

            try {
                uploadFile = FileUtils.createImageFile(activity);
                if (uploadFile != null) {
                    uploadFilePath = uploadFile.getAbsolutePath();
                    uploadFileUri = FileProvider.getUriForFile(activity, activity.getPackageName(), uploadFile);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uploadFileUri);
                    activity.startActivityForResult(takePictureIntent, QcDefine.REQUEST_PICK_CAMERA);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 갤러리 호출
     */
    public synchronized void openGallery(Activity activity) {
        try {
            uploadFile = FileUtils.createImageFile(activity);
            if (uploadFile != null) {
                uploadFilePath = uploadFile.getAbsolutePath();
                uploadFileUri = FileProvider.getUriForFile(activity, activity.getPackageName(), uploadFile);

                Intent intent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    // 구굴 포토등 권한 문제
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                    intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                } else {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                }
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("image/*");
                activity.startActivityForResult(intent, QcDefine.REQUEST_PICK_GALLERY);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 크롭하기
     *
     * @param intentRatioWidth
     * @param intentRatioHeight
     */
    public synchronized void openCropImage(Activity activity, int intentRatioWidth, int intentRatioHeight) {
        activity.grantUriPermission("com.android.camera", uploadFileUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intentCrop = new Intent("com.android.camera.action.CROP");
        intentCrop.setDataAndType(uploadFileUri, "image/*");

        List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(intentCrop, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : list) {
            String packageName = resolveInfo.activityInfo.packageName;
            activity.grantUriPermission(packageName, uploadFileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        int size = list.size();
        if (size == 0) {
            QcToast.getInstance().show("취소되었습니다", false);
        } else {
            QcToast.getInstance().show("용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", false);

            intentCrop.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentCrop.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intentCrop.putExtra("crop", "true");
            if (intentRatioWidth != 0 || intentRatioHeight != 0) {
                intentCrop.putExtra("aspectX", intentRatioWidth);
                intentCrop.putExtra("aspectY", intentRatioHeight);
            }

            intentCrop.putExtra("scale", true);

            File folder = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File tempFile = new File(folder.toString(), uploadFile.getName());

            uploadFileUri = FileProvider.getUriForFile(activity, activity.getPackageName(), tempFile);
            QcLog.e("openCropImage uploadFile == " + uploadFile.getName());
            QcLog.e("openCropImage uploadFile == " + tempFile.getName());
            QcLog.e("openCropImage uploadFileUri == " + uploadFileUri.toString());

            intentCrop.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentCrop.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            // 크롭 화면에서 저장을 누르면 파일로 저장
            intentCrop.putExtra("return-data", false);
            // 크롭 화면에서 저장을 누르면 Bundle을 통해 bitmap으로 데이터를 받아오겠다
            intentCrop.putExtra(MediaStore.EXTRA_OUTPUT, uploadFileUri);
            //Bitmap 형태로 받기 위해 해당 작업 진행
            intentCrop.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());

            Intent intent = new Intent(intentCrop);
            ResolveInfo res = list.get(0);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            activity.grantUriPermission(res.activityInfo.packageName, uploadFileUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            activity.startActivityForResult(intent, QcDefine.REQUEST_PICK_CROP);
        }
    }

}
