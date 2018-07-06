package com.ulling.oneday.util;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.ulling.lib.core.util.QcLog;

/**
 * 파이어베이스 인증
 * <p>
 * https://firebase.google.com/docs/auth/android/manage-users?authuser=0
 */
public class FireBaseAuthUtils {
    private static FireBaseAuthUtils SINGLE_U;
    private FirebaseUser user;

    public static synchronized FireBaseAuthUtils getInstance() {
        if (SINGLE_U == null) {
            SINGLE_U = new FireBaseAuthUtils();
        }
        return SINGLE_U;
    }

    /**
     * 익명 유져 초기화 및 가입
     */
    public void init() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            FirebaseAuth.getInstance()
                    .signInAnonymously()
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                QcLog.e("signInAnonymously  success ==== ");
                                user = FirebaseAuth.getInstance().getCurrentUser();
                            } else {
                                QcLog.e("signInAnonymously failure ==== " + task.getException());
                            }
                        }
                    });
        }
    }

    /**
     * 유져정보 가져오기
     *
     * @return
     */
    public FirebaseUser getCurrentUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();

//        emailVerified = user.isEmailVerified();
        getUserInfoToString();
        return user;
    }

    public void getUserInfoToString() {
        if (user != null) {
            QcLog.e("FirebaseUser : getUid = " + user.getUid()
                    + ", getDisplayName = " + user.getDisplayName()
                    + ", getEmail = " + user.getEmail()
                    + ", getPhoneNumber = " + user.getPhoneNumber()
                    + ", getProviderId = " + user.getProviderId()
                    + ", getPhotoUrl = " + user.getPhotoUrl()
            );
        }
    }

    /**
     * 유져정보 업데이트
     *
     * @param displayName
     * @param photoUrl
     */
    public void upadteUserInfo(String displayName, String photoUrl) {

        UserProfileChangeRequest.Builder userProfilebuild = new UserProfileChangeRequest.Builder();
        if (displayName != null && !TextUtils.isEmpty(displayName))
            userProfilebuild.setDisplayName(displayName);
        if (photoUrl != null && !TextUtils.isEmpty(photoUrl))
            userProfilebuild.setPhotoUri(Uri.parse(photoUrl));
        UserProfileChangeRequest profileUpdates = userProfilebuild.build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            QcLog.e("User profile updated.");
                            getCurrentUser();
                        }
                    }
                });
    }
}
