package com.ulling.lib.core.network;

import android.os.Build;
import android.text.TextUtils;

import com.ulling.lib.core.util.QcLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class QcBaseRetrofitService {
    public static final int HTTP_READ_TIMEOUT = 5000;
    public static final int HTTP_CONNECT_TIMEOUT = 4000;

    public interface OnRetrofitListener<T> {
        void onSuccessful();

        void onErrorBody(T t);

        void onFailure(String msg);
    }

//
//    public static Retrofit getRetrofit(String baseURL, OkHttpClient client) {
//        if (retrofit == null && !TextUtils.isEmpty(baseURL)) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(baseURL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .addConverterFactory(ScalarsConverterFactory.create())  // post 날릴려면 필요!
//                    //execute call back in background thread
//                    .callbackExecutor(Executors.newSingleThreadExecutor())
//                    .client(client)
//                    .build();
//        }
//        return retrofit;
//    }

    protected static OkHttpClient.Builder enableTlsOnPreLollipop(OkHttpClient.Builder builder) {
//        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 22) {
//            try {
//                SSLContext sc = SSLContext.getInstance("TLSv1.2");
//                sc.init(null, null, null);
//                builder.sslSocketFactory(new TLSSocketFactory(sc.getSocketFactory()));
//
//                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
//                        .tlsVersions(TlsVersion.TLS_1_2)
//                        .build();
//
//                List<ConnectionSpec> specs = new ArrayList<>();
//                specs.add(cs);
//                specs.add(ConnectionSpec.COMPATIBLE_TLS);
//                specs.add(ConnectionSpec.CLEARTEXT);
//
//                builder.connectionSpecs(specs);
//            } catch (Exception exc) {
//                QcLog.e("Error while setting TLS 1.2" + exc);
//            }
//        }

        return builder;
    }
}
