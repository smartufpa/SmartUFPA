package br.ufpa.smartufpa.utils.retrofit;

import android.content.Context;

import br.ufpa.smartufpa.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit client = null;

    public static Retrofit getInstance(final Context context){
        client = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.server_ip))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return client;
    }

}
