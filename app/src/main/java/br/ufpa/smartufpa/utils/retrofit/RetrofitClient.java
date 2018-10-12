package br.ufpa.smartufpa.utils.retrofit;

import android.content.Context;

import org.jetbrains.annotations.Nullable;

import br.ufpa.smartufpa.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit client = null;

    public static Retrofit getInstance(final Context context, @Nullable String url){
        final String baseUrl = url == null ? context.getString(R.string.server_ip) : url;
        client = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return client;
    }

}
