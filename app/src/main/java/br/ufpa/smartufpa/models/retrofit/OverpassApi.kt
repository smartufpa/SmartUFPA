package br.ufpa.smartufpa.models.retrofit

import br.ufpa.smartufpa.models.overpass.OverpassModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OverpassApi {

    @GET("interpreter")
    fun getData(@Query("data") data : String) : Call<OverpassModel>
}