package com.example.lab_10.rutinas.network

import com.example.lab_10.rutinas.model.RutinaModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RutinasApiService {
    @GET("api/rutina/")
    suspend fun selectRutinas(): List<RutinaModel>

    @POST("api/rutina/")
    suspend fun insertRutina(@Body rutina: RutinaModel): Response<RutinaModel>

    @PUT("api/rutina/{id}/")
    suspend fun updateRutina(@Path("id") id: Int, @Body rutina: RutinaModel): Response<RutinaModel>

    @DELETE("api/rutina/{id}/")
    suspend fun deleteRutina(@Path("id") id: Int): Response<Unit>
}