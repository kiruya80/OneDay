package com.ulling.oneday.util;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ulling.lib.core.util.QcLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 파이어 베이스
 * ㄴ 스토리지
 * <p>
 * https://firebase.google.com/docs/storage/android/upload-files?hl=ko
 * <p>
 * https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934
 */
public class FireBaseStorageUtils {
    private static FireBaseStorageUtils SINGLE_U;

    private FirebaseStorage firebaseStorage;

    public static synchronized FireBaseStorageUtils getInstance() {
        if (SINGLE_U == null) {
            SINGLE_U = new FireBaseStorageUtils();
        }
        return SINGLE_U;
    }

    public FireBaseStorageUtils() {
        getFirebaseStorage();
    }


    public FirebaseStorage getFirebaseStorage() {
        if (firebaseStorage == null)
            firebaseStorage = FirebaseStorage.getInstance();
        return firebaseStorage;
    }

    /**
     * 파일 업로드
     * @param uploadFile
     * @param folderName
     * @param fileName
     * @param listener
     */
    public void uploadFile(final File uploadFile,
                           String folderName,
                           String fileName,
                           final FileUploadListener listener) {
        if (fileName != null && !TextUtils.isEmpty(fileName)) {
            StorageReference storageRef = getFirebaseStorage().getReference();
            final StorageReference desertRef = storageRef.child(
                    folderName + "/" + fileName);

            InputStream stream = null;
            try {
                stream = new FileInputStream(uploadFile);
                QcLog.e("fileUpload uploadFile.length() ====  " + uploadFile.length());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            UploadTask uploadTask = desertRef.putStream(stream);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / uploadFile.length();
//                    QcLog.e("fileUpload Upload is " + progress + "% done");
                    if (listener != null)
                        listener.onProgress(Math.round(progress));
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    QcLog.e("fileUpload onSuccess ===== " + taskSnapshot.getDownloadUrl());
                    if (listener != null)
                        listener.onSuccess(taskSnapshot, taskSnapshot.getDownloadUrl());
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    if (listener != null)
                        listener.onPaused(taskSnapshot);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if (listener != null)
                        listener.onFailure(exception);
                }
            });
        }
    }

    /**
     * 파일 업로드 리스너
     */
    public interface FileUploadListener {
        void onProgress(double progress);
        void onSuccess(UploadTask.TaskSnapshot taskSnapshot, Uri downloadUri);
        void onPaused(UploadTask.TaskSnapshot taskSnapshot);
        void onFailure(Exception exception);
    }

    /**
     * 파일 삭제
     * @param folderName
     * @param fileName
     * @param listener
     */
    public void deleteFile(
                           String folderName,
                           String fileName,
                           final FileDeleteListener listener) {
        if (fileName != null && !TextUtils.isEmpty(fileName)) {
            StorageReference storageRef = getFirebaseStorage().getReference();
            final StorageReference desertRef = storageRef.child(  folderName + "/" + fileName);

            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    if (listener != null)
                        listener.onSuccess();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if (listener != null)
                        listener.onFailure(exception);
                }
            });
        }
    }

    /**
     * 파일 삭제 리스너
     */
    public interface FileDeleteListener {
        void onSuccess();
        void onFailure(Exception exception);
    }
}
