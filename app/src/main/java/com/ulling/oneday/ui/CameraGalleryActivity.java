package com.ulling.oneday.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import com.ulling.lib.core.common.QcDefine;
import com.ulling.lib.core.ui.QcBaseLifeActivity;
import com.ulling.lib.core.util.QcBitmapUtil;
import com.ulling.lib.core.util.QcLog;
import com.ulling.oneday.OnedayApplication;
import com.ulling.oneday.R;
import com.ulling.oneday.asynctask.SaveBitmapToImageAsyncTask;
import com.ulling.oneday.common.Define;
import com.ulling.oneday.databinding.ActivityCameraGalleryBinding;
import com.ulling.oneday.permission.PermissionsActivity;
import com.ulling.oneday.permission.PermissionsChecker;
import com.ulling.oneday.util.CameraGalleryUtil;

import java.io.IOException;


/**
 * 카메라 샘플 액티비티
 * <p>
 * http://programmar.tistory.com/5?category=721123
 * <p>
 * https://github.com/LeeHanYeong/AndroidCameraGuide
 */
public class CameraGalleryActivity extends QcBaseLifeActivity {
    private OnedayApplication qApp;
    private ActivityCameraGalleryBinding viewBinding;

    // 업로드 주소
    private String intentImageUploadUrl = "";
    // 카메라 / 앨범
    private int intentSelectedType = QcDefine.REQUEST_PICK_CAMERA;
    // 이미지 리사이징
    private boolean intentResizeType = false;
    // 크롭 유무
    private boolean intentCrop = false;
    // 크롭 비율
    private int intentRatioWidth = 0;
    private int intentRatioHeight = 0;
    private int permissionSelected = QcDefine.REQUEST_PICK_CAMERA;

    // 권한 체크
    private PermissionsChecker checker;

    @Override
    protected int needGetLayoutId() {
        return R.layout.activity_camera_gallery;
    }

    @Override
    protected void needInitToOnCreate() {
        qApp = OnedayApplication.getInstance();

        checker = new PermissionsChecker(this);
        CameraGalleryUtil.getInstance(CameraGalleryActivity.this).reset();
    }

    @Override
    protected void optGetSavedInstanceState(Bundle savedInstanceState) {
    }

    @Override
    protected void optGetIntent(Intent intent) {
        intentImageUploadUrl = intent.getExtras().getString(QcDefine.INTENT_IMAGE_UPLOAD_URL, "");
        intentSelectedType = intent.getExtras().getInt(QcDefine.INTENT_SELCTED_TYPE, QcDefine.REQUEST_PICK_CAMERA);
        intentResizeType = intent.getExtras().getBoolean(QcDefine.INTENT_RESIZE_TYPE, false);
        intentCrop = intent.getExtras().getBoolean(QcDefine.INTENT_CROP_TYPE, false);
        intentRatioWidth = intent.getExtras().getInt(QcDefine.INTENT_RATIO_WIDTH, 0);
        intentRatioHeight = intent.getExtras().getInt(QcDefine.INTENT_RATIO_HEIGHT, 0);
    }

    @Override
    protected void needResetData() {
    }

    @Override
    protected void needUIBinding() {
        viewBinding = (ActivityCameraGalleryBinding) getViewDataBinding();
        viewBinding.progressBar.setMax(100);
        viewBinding.llprogressBarBitmap.setVisibility(View.GONE);
    }

    @Override
    protected void needUIEventListener() {
        viewBinding.btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCamera();
            }
        });
        viewBinding.btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGallery();
            }
        });
        viewBinding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getUploadFilePath()) && !TextUtils.isEmpty(intentImageUploadUrl)) {
                    Intent intent = new Intent();
                    intent.putExtra(Define.INTENT_IMG_DOWNLOAD_RESULT, true);
                    intent.putExtra(Define.INTENT_IMG_FILE_ABSOLUTE_PATH, CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getUploadFile().getAbsolutePath());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        if (intentSelectedType == QcDefine.REQUEST_PICK_CAMERA) {
            startCamera();
        } else if (intentSelectedType == QcDefine.REQUEST_PICK_GALLERY) {
            startGallery();
        }
    }

    @Override
    protected void needSubscribeUiFromViewModel() {
    }

    @Override
    protected void needSubscribeUiClear() {
    }

    @Override
    protected void needOnShowToUser() {
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_down);
    }

    /**
     * 카메라 시작하기
     */
    private void startCamera() {
        if (checker.lacksPermissions(QcDefine.PERMISSIONS_CAMERA_STORAGE)) {
            permissionSelected = QcDefine.REQUEST_PICK_CAMERA;
            PermissionsActivity.startActivityForResult(CameraGalleryActivity.this, QcDefine.REQUEST_PERMISSIONS, QcDefine.PERMISSIONS_CAMERA_STORAGE);
        } else {
            viewBinding.txtResult.setText("Ready ... ");
            viewBinding.progressBar.setProgress(0);
            CameraGalleryUtil.getInstance(CameraGalleryActivity.this).openCamera(this);
        }
    }

    /**
     * 갤러리 시작하기
     */
    private void startGallery() {
        if (checker.lacksPermissions(QcDefine.PERMISSIONS_CAMERA_STORAGE)) {
            permissionSelected = QcDefine.REQUEST_PICK_GALLERY;
            PermissionsActivity.startActivityForResult(CameraGalleryActivity.this, QcDefine.REQUEST_PERMISSIONS, QcDefine.PERMISSIONS_CAMERA_STORAGE);
        } else {
            viewBinding.txtResult.setText("Ready ... ");
            viewBinding.progressBar.setProgress(0);
            CameraGalleryUtil.getInstance(CameraGalleryActivity.this).openGallery(this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            QcLog.e("onActivityResult RESULT_CANCELED");
            onBackPressed();
            return;
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == QcDefine.REQUEST_PERMISSIONS) {
                int result = data.getIntExtra(PermissionsActivity.REQUEST_RESULT, 0);
                if (result == PermissionsActivity.PERMISSIONS_GRANTED) {
                    // 권한획득 경우
                    if (permissionSelected == QcDefine.REQUEST_PICK_CAMERA) {
                        startCamera();
                    } else {
                        startGallery();
                    }
                } else {
                    onBackPressed();
                }

            } else if (requestCode == QcDefine.REQUEST_PICK_CAMERA) {
                if (intentCrop) {
                    // 크롭선택한 경우
                    CameraGalleryUtil.getInstance(CameraGalleryActivity.this).openCropImage(this, intentRatioWidth, intentRatioHeight);
                } else {
                    pickCamera();
                }

            } else if (requestCode == QcDefine.REQUEST_PICK_GALLERY) {
                if (data != null && data.getData() != null) {
                    if (intentCrop) {
                        CameraGalleryUtil.getInstance(CameraGalleryActivity.this).setUploadFileUri(data.getData());
                        CameraGalleryUtil.getInstance(CameraGalleryActivity.this).openCropImage(this, intentRatioWidth, intentRatioHeight);
                    } else {
                        pickGallery(data);
                    }
                }

            } else if (requestCode == QcDefine.REQUEST_PICK_CROP) {
//                pickCrop();

                if (!TextUtils.isEmpty(CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getUploadFilePath()) && !TextUtils.isEmpty(intentImageUploadUrl)) {
                    Intent intent = new Intent();
                    intent.putExtra(Define.INTENT_IMG_DOWNLOAD_RESULT, true);
                    intent.putExtra(Define.INTENT_IMG_FILE_ABSOLUTE_PATH, CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getUploadFile().getAbsolutePath());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }
    }

    /**
     * 카메라에서 이미지 가져와 뷰에 그리기
     */
    private void pickCamera() {
        viewBinding.zoomImageView.setImageBitmap(null);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getUploadFileUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            int exifDegree = QcBitmapUtil.getInstance().getImageDegree(CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getUploadFilePath());
            // 이미지 리사이징 체크
            Bitmap resizeBitmap = QcBitmapUtil.getInstance().resizeMaxBitmap(intentResizeType, bitmap,
                    CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getMaxPictureSize(), true);
            // 이미지 회전 체크
            Bitmap rotateBitmap = QcBitmapUtil.getInstance().rotate(resizeBitmap, exifDegree);

            viewBinding.zoomImageView.setImageBitmap(rotateBitmap);
            viewBinding.txtFileInfo.setText("저장 파일위치 : \n" + CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getUploadFilePath());
            viewBinding.txtResult.setText("카메라 이미지 가져오기 완료");

//            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 128, 128);
//            ByteArrayOutputStream bs = new ByteArrayOutputStream();
//            thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs); //이미지가 클 경우 OutOfMemoryException 발생이 예상되어 압축
        }
    }

    /**
     * 갤러리에서 이미지 가져와 뷰에 그리기
     *
     * @param data
     */
    private void pickGallery(Intent data) {
        viewBinding.zoomImageView.setImageBitmap(null);
        Uri photoUri = data.getData();
        String imgPath = data.getData().getPath();
        try {
            viewBinding.llprogressBarBitmap.setVisibility(View.VISIBLE);
            int exifDegree = QcBitmapUtil.getInstance().getImageDegree(imgPath);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            // 이미지 리사이징 체크
            Bitmap resizeBitmap = QcBitmapUtil.getInstance().resizeMaxBitmap(intentResizeType, bitmap,
                    CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getMaxPictureSize(), true);
            // 이미지 회전 체크
            final Bitmap rotateBitmap = QcBitmapUtil.getInstance().rotate(resizeBitmap, exifDegree);

            // 갤러리에서 선택한 이미지를 비트맵으로 가져오고,
            // 파일로 새로 저장한다
            new SaveBitmapToImageAsyncTask(
                    rotateBitmap,
                    CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getUploadFile(),
                    new SaveBitmapToImageAsyncTask.SaveBitmapListener() {
                        @Override
                        public void onComplete(Boolean result) {
                            viewBinding.zoomImageView.setImageBitmap(rotateBitmap);
                            viewBinding.txtFileInfo.setText("저장 파일위치 : \n" + CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getUploadFilePath());
                            viewBinding.txtResult.setText("갤러리 이미지 가져오기 완료");
                            viewBinding.llprogressBarBitmap.setVisibility(View.GONE);
                        }
                    }).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 크롭하기
     */
    private void pickCrop() {
        try {
            viewBinding.zoomImageView.setImageBitmap(null);
            viewBinding.llprogressBarBitmap.setVisibility(View.VISIBLE);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                    CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getUploadFileUri());
            Bitmap resizeBitmap = QcBitmapUtil.getInstance().resizeMaxBitmap(intentResizeType, bitmap,
                    CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getMaxPictureSize(), true);

            int exifDegree = QcBitmapUtil.getInstance().getImageDegree(CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getUploadFilePath());
            final Bitmap rotateBitmap = QcBitmapUtil.getInstance().rotate(resizeBitmap, exifDegree);

            new SaveBitmapToImageAsyncTask(
                    rotateBitmap,
                    CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getUploadFile(),
                    new SaveBitmapToImageAsyncTask.SaveBitmapListener() {
                        @Override
                        public void onComplete(Boolean result) {
                            viewBinding.zoomImageView.setImageBitmap(rotateBitmap);
                            viewBinding.txtFileInfo.setText("저장 파일위치 : \n" + CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getUploadFilePath());
                            viewBinding.txtResult.setText("갤러리 이미지 가져오기 완료");
                            viewBinding.llprogressBarBitmap.setVisibility(View.GONE);
                        }
                    }).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 파일 업로드
//     */
//    private void fileUploadOkhttp() {
//        if (!TextUtils.isEmpty(CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getUploadFilePath())
//                && !TextUtils.isEmpty(intentImageUploadUrl)) {
//            viewBinding.txtResult.setText("Upload ... 0%");
//            viewBinding.progressBar.setProgress(0);
//            ApiCall.uploadImage(intentImageUploadUrl, CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getUploadFile(),
//                    new UploadProgressListener() {
//                        @Override
//                        public void onResponse(Response response) {
//                            Intent intent = new Intent();
//                            intent.putExtra("result", CameraGalleryUtil.getInstance(CameraGalleryActivity.this).getUploadFilePath());
//                            setResult(RESULT_OK, intent);
//                            finish();
//                        }
//
//                        @Override
//                        public void onFailure(IOException e) {
//                            QcLog.e("onFailure " + e.toString());
//                            Intent intent = new Intent();
//                            intent.putExtra("result", "");
//                            setResult(RESULT_OK, intent);
//                            finish();
//                        }
//
//                        @Override
//                        public void onProgressUpdate(final int values) {
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    runOnUiThread(new Runnable() {
//                                        public void run() {
//                                            viewBinding.txtResult.setText("Upload ... " + values + "%");
//                                            viewBinding.progressBar.setProgress(values);
//                                        }
//                                    });
//                                }
//                            }).start();
//                        }
//                    });
//        } else {
//            QcToast.getInstance().show("파일이 없습니다 또는 업로드주소가 없습니다", false);
//        }
//    }
}