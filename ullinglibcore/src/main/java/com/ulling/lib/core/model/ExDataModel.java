package com.ulling.lib.core.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.ulling.lib.core.db.ExQcBaseAppDatabase;
import com.ulling.lib.core.entities.QcBaseItem;

import java.util.List;

/**
 * 데이터모델(Repository) 예제
 * ㄴ 데이터 쿼리 , api연결작업
 * ㄴ 뷰에서 필요한 데이터는 로컬 DB 또는 Api를 사용하여 네트쿼크를 통하여 가져온다
 * ㄴ db에서 데이터가 널인 경우는 네트워크를 사용하는 경우 api를 통해 가져온다
 * ㄴ 네트워크를 통해 가져온 데이터는 로컬에 저장(옵션)하여 뷰모델로 전달
 * ㄴ 가져온 데이터는 liveData로 감싸 뷰모델로 전달한다
 * <p>
 * <p>
 * <p>
 * <p>
 * LiveData
 * ㄴ 값을 관찰할 수 있는 추상 클래스, 오직 관찰만 가능, observe 메서드를 통해 값의 변화를 추적할 수 있고, getValue 매서드로 현제 값을 가져올 수 있음
 * <p>
 * <p>
 * MutableLiveData (extends LiveData)
 * ㄴ concrete class, LiveData의 setValue, postValue 메서드를 expose(public)한 클래스로 사이드이펙트를 줄 수 있음
 * <p>
 * <p>
 * ComputableLiveData
 * ㄴ disk IO에서 실행되는 LiveData, LiveData class를 상속하지 않지만, getLiveData 메서드를 통해 실제 LiveData를 반환
 * <p>
 * <p>
 * MediatorLiveData (extends MutableLiveData)
 * ㄴ concrete class, addSource 메서드를 통해 여러 LiveData를 구독 혹은 머지하고,
 * onChanged 콜백 내에서 자신의 값으로 발행할 수 있음,
 * Rx에서 결합 혹은 변환 연산자와 비슷한 역할,
 * 만약 기존의 LiveData를 다른 형태로 변환하여 새로운 LiveData를 만든다면
 * Transformations 클래스를 사용(내부적으로 MediatorLiveData 를 사용)
 */
public class ExDataModel extends QcBaseDataModel {
    private static ExDataModel sInstance;
    private ExQcBaseAppDatabase mDatabase;

    private MutableLiveData<QcBaseItem> user = null;
    private MutableLiveData<List<QcBaseItem>> users = null;
    private MediatorLiveData<List<QcBaseItem>> test = null;

    public ExDataModel(ExQcBaseAppDatabase mDatabase) {
        super();
        this.mDatabase = mDatabase;
    }

    public static ExDataModel getInstance(final ExQcBaseAppDatabase database) {
        if (sInstance == null) {
            synchronized (ExDataModel.class) {
                if (sInstance == null) {
                    sInstance = new ExDataModel(database);
                }
            }
        }
        return sInstance;
    }


    /*********
     * 단일 데이터 가져오기
     */

    /**
     * 데이터를 가져온다
     * <p>
     * if 데이터가 없는 경우, 로컬 또는 네트워크에서 가져온다 ?
     * <p>
     * <p>
     * 데이터 가져오는 경우
     * 1. 새로 갱신
     * 2. 다음 데이터 가져오기
     *
     * @return
     */
    // 데이터 연결
    public MutableLiveData<QcBaseItem> loadUser() {
        if (user == null) {
            user = new MutableLiveData<QcBaseItem>();
//            // 로컬에서 가져오는 경우
//            if (mDatabase != null) {
//                LiveData<QcBaseItem> user_ = mDatabase.getUser();
//                user.postValue(user_.getValue());
//            }
            // 로컬에서 가져오는 경우
            if (mDatabase != null) {
                LiveData<QcBaseItem> user_ = mDatabase.exUserDao().loadUser();
                user.postValue(user_.getValue());
            }
        } else {
            return user;
        }
        return user;
    }

    // 데이터 리셋
    public void resetUser() {
        user = null;
    }


    /*********
     * 데이터 리스트 가져오기
     */

    public MutableLiveData<List<QcBaseItem>> loadUsers() {
        if (users == null) {
            users = new MutableLiveData<List<QcBaseItem>>();
//            // 로컬에서 가져오는 경우
//            if (mDatabase != null) {
//                LiveData<List<QcBaseItem>> user_ = mDatabase.getUsers();
//                users.postValue(user_.getValue());
//            }
            // 로컬에서 가져오는 경우
            if (mDatabase != null) {
                LiveData<List<QcBaseItem>> user_ = mDatabase.exUserDao().loadUsers();
                users.postValue(user_.getValue());
            }
        } else {
            return users;
        }
        return users;
    }

    // 데이터 리셋
    public void resetUsers() {
        users = null;
    }


    /*********
     * 데이터 더보기 가져오기
     */


    /**
     * 데이터 더 가져오기 및 새로 가져오기
     * 로컬 또는 네트워크에서 가져온 데이터를 기존 데이터 리스트에 추가한다
     * 추가된 데이터는 옵벼저에 의해 뷰에서 갱신된다
     */
    public MutableLiveData<List<QcBaseItem>> loadMoreUsers(int index) {
        if (users == null) {
            users = new MutableLiveData<List<QcBaseItem>>();
//            // 로컬에서 가져오는 경우
//            if (mDatabase != null) {
//                LiveData<List<QcBaseItem>> user_ = mDatabase.getUsers(index);
//                users.postValue(user_.getValue());
//            }
            // 로컬에서 가져오는 경우
            if (mDatabase != null) {
                LiveData<List<QcBaseItem>> user_ = mDatabase.exUserDao().loadUsers(index);
                users.postValue(user_.getValue());
            }
            return users;
        }
        return users;
    }

    public void insertUsers(List<QcBaseItem> mUsers) {
        // 네트워크에서 가져오는 경우
        if (mDatabase != null) {
            mDatabase.exUserDao().insertAll(mUsers);
            // 로컬에서 가져오는 경우
            users = new MutableLiveData<List<QcBaseItem>>();
            LiveData<List<QcBaseItem>> user_ = mDatabase.exUserDao().loadUsers();
            users.postValue(user_.getValue());
        }
    }


    /*********
     * 로컬 또는 네트워크에서 데이터 가져오기
     */


//    public MutableLiveData<QcBaseItem> getLocalData() {
//        return mDatabase.getUser();
//    }
//
//    public MutableLiveData<QcBaseItem> getNetworkData() {
////        QcBaseRetrofitService.getInstance()
//        return user;
//    }

}
