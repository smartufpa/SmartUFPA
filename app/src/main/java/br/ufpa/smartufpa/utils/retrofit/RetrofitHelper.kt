package br.ufpa.smartufpa.utils.retrofit

import br.ufpa.smartufpa.models.retrofit.OverpassApi
import br.ufpa.smartufpa.utils.BooleanTypeAdapter
import br.ufpa.smartufpa.utils.Constants.URL_OVERPASS_SERVER
import com.google.gson.GsonBuilder
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {
    private val gsonRetrofit: Retrofit
        get() = gsonBuilder.build()

    private val gsonBuilder: Retrofit.Builder
        get() = Retrofit.Builder()
                .baseUrl(URL_OVERPASS_SERVER)
                .addConverterFactory(gsonConverter)

    val overpassApi: OverpassApi
        get() = this.gsonRetrofit.create(OverpassApi::class.java)


    private val gsonConverter: Converter.Factory
        get() {
            val builder = GsonBuilder()
            builder.registerTypeAdapter(Boolean::class.javaPrimitiveType, BooleanTypeAdapter())
            val gson = builder.create()

            return GsonConverterFactory.create(gson)
        }
}
