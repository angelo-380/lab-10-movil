package com.example.lab_10.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SerieApiService {
    @GET("api/serie/")
    suspend fun selectSeries(): List<SerieModel>

    // 👈 SE AGREGÓ "api/" A TODAS LAS DE ABAJO
    @GET("api/serie/{id}/")
    suspend fun selectSerie(@Path("id") id: String): Response<SerieModel>

    @Headers("Content-Type: application/json")
    @POST("api/serie/")
    suspend fun insertSerie(@Body serie: SerieModel): Response<SerieModel>

    @PUT("api/serie/{id}/")
    suspend fun updateSerie(@Path("id") id: String, @Body serie: SerieModel): Response<SerieModel>

    @DELETE("api/serie/{id}/")
    suspend fun deleteSerie(@Path("id") id: String): Response<SerieModel>
}