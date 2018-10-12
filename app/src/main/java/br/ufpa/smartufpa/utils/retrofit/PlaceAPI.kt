package br.ufpa.smartufpa.utils.retrofit

import br.ufpa.smartufpa.models.smartufpa.APIResponse
import br.ufpa.smartufpa.models.smartufpa.Building
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface PlaceAPI {
    @POST("buildings")
    fun addBuilding(@Body building: Building) : Call<APIResponse>
}