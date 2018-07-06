package com.ulling.lib.core.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ulling.lib.core.base.ExQcBaseApplication;
import com.ulling.lib.core.base.QcBaseApplication;
import com.ulling.lib.core.entities.QcBaseItem;
import com.ulling.lib.core.model.ExDataModel;

import java.util.List;

/**
 * ViewModel
 * ㄴ 뷰에서 필요한 데이터를 생성 및 초기화 (liveData)
 * ㄴ 모델(레파지토리)에서 뷰에서 필요한 데이터를 가져온다
 * ㄴ
 * <p>
 * 이슈)
 * ㄴ 데이터 가공이 필요한 경우는 어디서 하는지
 */
public class ExViewModel extends QcBaseViewModel {

    private ExDataModel mExDataModel;
    private int mProductId;

    private MutableLiveData<QcBaseItem> user = null;
    private MutableLiveData<List<QcBaseItem>> users = null;

    // Factory 에서 생성할때 필요
    public ExViewModel(@NonNull Application application,
                       ExDataModel mExDataModel,
                       final int productId) {
        super(application);
        this.mProductId = productId;
        this.mExDataModel = mExDataModel;

        // 초기화 필요한 경우
//        this.mQcBaseItem = mExDataModel.loadQcBaseItem();
//        mObservableComments = repository.loadComments(mProductId);
//        mObservableProduct = repository.loadProduct(mProductId);
//
////        LiveData<List<QcBaseItem>> products = ((QcBaseApplication) application).getRepository()
////                .getUsers();
    }

    public void setExViewModel(
            ExDataModel mExDataModel,
            final int productId) {
        this.mProductId = productId;
        this.mExDataModel = mExDataModel;

        // 초기화 필요한 경우
//        this.mQcBaseItem = mExDataModel.loadQcBaseItem();
//        mObservableComments = repository.loadComments(mProductId);
//        mObservableProduct = repository.loadProduct(mProductId);
//
////        LiveData<List<QcBaseItem>> products = ((QcBaseApplication) application).getRepository()
////                .getUsers();
    }

    public void setProductId(int productId) {
        mProductId = productId;
    }

    /**
     * 데이터모델에서 데이터를 가져온다
     * 뷰는 이 데이터를 구독한다
     *
     * @return
     */
    public LiveData<QcBaseItem> getUser() {
        if (user == null) {
            user = mExDataModel.loadUser();
        }
        return user;
    }

    public LiveData<List<QcBaseItem>> getUsers() {
        if (users == null) {
            users = mExDataModel.loadUsers();
        }
        return users;
    }


    /**
     * 데이터 초기화
     */
    public void resetUser() {
        mExDataModel.resetUser();
    }

    public void resetUsers() {
        mExDataModel.resetUsers();
    }


    /**
     * 데이터 더 불러오기
     *
     * @param index
     */
    public void moreUsers(int index) {
        mExDataModel.loadMoreUsers(index);
    }


    // http://tourspace.tistory.com/25
    // 위 코드는 우편번호를 얻는 코드(getPostalCode(String address))를 수행하면 새로운 LiveData를 반환합니다.
    //
    //따라서 UI는 기존 LiveData를 unregiter 하고 새로운 LiveData object에 register 해야합니다.
    //
    //또한 UI가 재생성 되는 경우에도 repository.getPostCode(address)를 호출하여야 합니다.
    //
    //이런경우 transformation을 이용하여 아래와 같이 수정할 수 있습니다.

    /**
     * Transformations 사용법
     * <p>
     * ㄴ map : LiveData를 return하며, source에 이벤트가 생길때마다 main thread에서 function이 수행된다.
     * <p>
     * ㄴ switchMap : trigger LiveData가 변경에 따라 이벤트를 발생 시키면 function을 적용하여 결과를 새로만든 새로운 LiveData에 set한다.
     * 또한 이때 새로운 LiveData에 등록된 observer들에게 재전송 된다.
     * <p>
     * <p>
     * <p>
     * http://tourspace.tistory.com/25
     * 위 코드는 우편번호를 얻는 코드(getPostalCode(String address))를 수행하면 새로운 LiveData를 반환합니다.
     * <p>
     * 따라서 UI는 기존 LiveData를 unregiter 하고 새로운 LiveData object에 register 해야합니다.
     * 또한 UI가 재생성 되는 경우에도 repository.getPostCode(address)를 호출하여야 합니다.
     * 이런경우 transformation을 이용하여 아래와 같이 수정할 수 있습니다.
     * <p>
     * postalCode는 final로 지정되어 변경되지 않습니다. addressInput이 변경될때
     * 등록되어있는 active한 observer가 있다면 function이 수행되고, 그렇지 않으면 수행되지 않습니다.
     */
//    private final PostalCodeRepository repository;
//    private final MutableLiveData<String> addressInput = new MutableLiveData();
//    public final LiveData<String> postalCode =
//            Transformations.switchMap(addressInput, (address) -> {
//                return repository.getPostCode(address);
//            });
//
//    public MyViewModel(PostalCodeRepository repository) {
//        this.repository = repository
//    }
//
//    private void setInput(String address) {
//        addressInput.setValue(address);
//    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mProductId;

        private ExDataModel mExDataModel;

        public Factory(@NonNull Application application, int productId) {
            mApplication = application;
            mProductId = productId;
            mExDataModel = ((ExQcBaseApplication) application).getExDataModel();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ExViewModel(mApplication, mExDataModel, mProductId);
        }
    }
}
